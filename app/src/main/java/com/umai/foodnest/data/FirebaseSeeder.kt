package com.umai.foodnest.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.umai.foodnest.data.model.*

/**
 * FirebaseSeeder — Seeds all Firestore collections with sample data on first run.
 * Also creates seed Firebase Auth users (customer + admin).
 *
 * Seeded collections:
 *  - restaurants/{id}
 *  - menuItems/{id}
 *  - users/{uid}        (with role field for admin detection)
 *  - orders/{id}        (linked to seed customer uid)
 *  - offers/{id}
 *  - notifications/{id}
 *  - adminStats/global
 *
 * Usage: FirebaseSeeder.seedIfNeeded()
 */
object FirebaseSeeder {

    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private const val TAG = "FirebaseSeeder"

    // ── Seed Credentials ──────────────────────────────────────────────────────
    const val CUSTOMER_EMAIL    = "customer@foodnest.com"
    const val CUSTOMER_PASSWORD = "food123"
    const val CUSTOMER_NAME     = "John Customer"

    const val ADMIN_EMAIL    = "admin@foodnest.com"
    const val ADMIN_PASSWORD = "admin123"
    const val ADMIN_NAME     = "FoodNest Admin"

    // ── Entry Point ───────────────────────────────────────────────────────────
    fun seedIfNeeded() {
        // Check if restaurants already seeded (guard against double-seeding)
        db.collection("restaurants").limit(1).get()
            .addOnSuccessListener { snap ->
                if (snap.isEmpty) {
                    Log.d(TAG, "Database empty — starting seed...")
                    seedRestaurants()
                    seedMenuItems()
                    seedOffers()
                    seedNotifications()
                    seedAdminStats()
                    seedAuthUsers()
                } else {
                    Log.d(TAG, "Data already seeded — skipping.")
                }
            }
            .addOnFailureListener { e ->
                Log.e(TAG, "Seed check failed: ${e.message}")
            }
    }

    // ── Restaurants ───────────────────────────────────────────────────────────
    private fun seedRestaurants() {
        val batch = db.batch()
        SampleData.restaurants.forEach { r ->
            val doc = db.collection("restaurants").document(r.id.toString())
            batch.set(doc, mapOf(
                "id"           to r.id,
                "name"         to r.name,
                "rating"       to r.rating,
                "deliveryTime" to r.deliveryTime,
                "deliveryFee"  to r.deliveryFee,
                "imageUrl"     to r.imageUrl,
                "category"     to r.category,
                "isOpen"       to r.isOpen,
                "address"      to r.address,
                "totalReviews" to r.totalReviews,
                "description"  to r.description
            ))
        }
        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "✅ Restaurants seeded (${SampleData.restaurants.size})") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Restaurants seed failed: ${e.message}") }
    }

    // ── Menu Items ────────────────────────────────────────────────────────────
    private fun seedMenuItems() {
        val allItems = SampleData.menuItems.values.flatten()
        val batch = db.batch()
        allItems.forEach { item ->
            val doc = db.collection("menuItems").document(item.id.toString())
            batch.set(doc, mapOf(
                "id"           to item.id,
                "restaurantId" to item.restaurantId,
                "name"         to item.name,
                "description"  to item.description,
                "price"        to item.price,
                "imageUrl"     to item.imageUrl,
                "category"     to item.category,
                "isPopular"    to item.isPopular
            ))
        }
        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "✅ Menu items seeded (${allItems.size})") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Menu items seed failed: ${e.message}") }
    }

    // ── Offers ────────────────────────────────────────────────────────────────
    private fun seedOffers() {
        val batch = db.batch()
        SampleData.offers.forEach { offer ->
            val doc = db.collection("offers").document(offer.id.toString())
            batch.set(doc, mapOf(
                "id"          to offer.id,
                "title"       to offer.title,
                "description" to offer.description,
                "code"        to offer.code,
                "discount"    to offer.discount,
                "imageUrl"    to offer.imageUrl,
                "type"        to offer.type.name,
                "bgColor"     to offer.bgColor
            ))
        }
        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "✅ Offers seeded (${SampleData.offers.size})") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Offers seed failed: ${e.message}") }
    }

    // ── Notifications ─────────────────────────────────────────────────────────
    private fun seedNotifications() {
        val batch = db.batch()
        SampleData.notifications.forEach { notif ->
            val doc = db.collection("notifications").document(notif.id.toString())
            batch.set(doc, mapOf(
                "id"        to notif.id,
                "title"     to notif.title,
                "message"   to notif.message,
                "timestamp" to notif.timestamp,
                "isRead"    to notif.isRead,
                "type"      to notif.type
            ))
        }
        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "✅ Notifications seeded") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Notifications seed failed: ${e.message}") }
    }

    // ── Admin Stats ───────────────────────────────────────────────────────────
    private fun seedAdminStats() {
        val stats = SampleData.adminStats
        db.collection("adminStats").document("global")
            .set(mapOf(
                "totalOrders"   to stats.totalOrders,
                "revenue"       to stats.revenue,
                "pending"       to stats.pending,
                "cancelled"     to stats.cancelled,
                "avgOrderValue" to stats.avgOrderValue
            ))
            .addOnSuccessListener { Log.d(TAG, "✅ Admin stats seeded") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Admin stats seed failed: ${e.message}") }
    }

    // ── Auth Users + User Profiles ────────────────────────────────────────────
    private fun seedAuthUsers() {
        // Create customer account
        auth.createUserWithEmailAndPassword(CUSTOMER_EMAIL, CUSTOMER_PASSWORD)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                seedUserProfile(uid, CUSTOMER_NAME, CUSTOMER_EMAIL, role = "customer")
                seedSampleOrders(uid)
                Log.d(TAG, "✅ Customer auth user created: $CUSTOMER_EMAIL")
            }
            .addOnFailureListener { e ->
                // Already exists — find by sign-in and seed profile
                Log.d(TAG, "Customer may already exist: ${e.message}")
                auth.signInWithEmailAndPassword(CUSTOMER_EMAIL, CUSTOMER_PASSWORD)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        seedUserProfile(uid, CUSTOMER_NAME, CUSTOMER_EMAIL, role = "customer")
                        seedSampleOrders(uid)
                        auth.signOut()
                    }
            }

        // Create admin account
        auth.createUserWithEmailAndPassword(ADMIN_EMAIL, ADMIN_PASSWORD)
            .addOnSuccessListener { result ->
                val uid = result.user?.uid ?: return@addOnSuccessListener
                seedUserProfile(uid, ADMIN_NAME, ADMIN_EMAIL, role = "admin")
                Log.d(TAG, "✅ Admin auth user created: $ADMIN_EMAIL")
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "Admin may already exist: ${e.message}")
                auth.signInWithEmailAndPassword(ADMIN_EMAIL, ADMIN_PASSWORD)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener
                        seedUserProfile(uid, ADMIN_NAME, ADMIN_EMAIL, role = "admin")
                        auth.signOut()
                    }
            }
    }

    private fun seedUserProfile(uid: String, name: String, email: String, role: String) {
        db.collection("users").document(uid)
            .set(mapOf(
                "id"              to uid,
                "name"            to name,
                "email"           to email,
                "phone"           to if (role == "admin") "+94 77 000 0001" else "+94 71 234 5678",
                "role"            to role,
                "profileImageUrl" to "",
                "createdAt"       to System.currentTimeMillis()
            ))
            .addOnSuccessListener { Log.d(TAG, "✅ User profile seeded: $email ($role)") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ User profile seed failed: ${e.message}") }
    }

    private fun seedSampleOrders(customerUid: String) {
        val batch = db.batch()
        SampleData.sampleOrders.forEach { order ->
            val doc = db.collection("orders").document(order.id)
            batch.set(doc, mapOf(
                "id"              to order.id,
                "userId"          to customerUid,
                "restaurantName"  to order.restaurantName,
                "restaurantImage" to order.restaurantImage,
                "status"          to order.status.name,
                "subtotal"        to order.subtotal,
                "deliveryFee"     to order.deliveryFee,
                "total"           to order.total,
                "deliveryAddress" to order.deliveryAddress,
                "paymentMethod"   to order.paymentMethod,
                "timestamp"       to order.timestamp,
                "estimatedArrival" to order.estimatedArrival,
                "items"           to order.items.map { cartItem ->
                    mapOf(
                        "foodItemId"   to cartItem.foodItem.id,
                        "foodItemName" to cartItem.foodItem.name,
                        "price"        to cartItem.foodItem.price,
                        "imageUrl"     to cartItem.foodItem.imageUrl,
                        "quantity"     to cartItem.quantity,
                        "totalPrice"   to cartItem.totalPrice
                    )
                }
            ))
        }
        batch.commit()
            .addOnSuccessListener { Log.d(TAG, "✅ Sample orders seeded for customer") }
            .addOnFailureListener { e -> Log.e(TAG, "❌ Orders seed failed: ${e.message}") }
    }
}
