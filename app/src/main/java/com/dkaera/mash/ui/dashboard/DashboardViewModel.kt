package com.dkaera.mash.ui.dashboard

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import com.dkaera.mash.domain.entity.NumberModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import timber.log.Timber

class DashboardViewModel : ViewModel() {

    init {
        Firebase.auth.addAuthStateListener {
            if (it.currentUser != null) {
                fetchHistory()
            } else {
                logout()
            }
        }
    }

    private val db = Firebase.firestore
    private val userId: String?
        get() {
            return Firebase.auth.currentUser?.uid
        }

    var savedNumbersState = mutableStateListOf(*emptyArray<NumberModel>())
    val numbersState: SnapshotStateList<Int> = mutableStateListOf(*emptyArray())

    fun generateFibonacci() {
        val n = 30
        var t1 = 0
        var t2 = 1
        (1..n).map {
            val sum = t1 + t2
            t1 = t2
            t2 = sum
            sum
        }.filter { number -> savedNumbersState.firstOrNull { number == it.number } == null }
            .apply {
            numbersState.clear()
            numbersState.addAll(this)
        }
    }

    private fun fetchHistory() {
        Timber.d("Fetch history")
        userId?.apply {
            db.collection(this).get().addOnCompleteListener { task ->
                task.result
                    .documents
                    .map {
                        val data = it.data!!
                        NumberModel((data["number"] as Long).toInt(), data["date"] as Long)
                    }
                    .toList().let {
                        savedNumbersState.clear()
                        savedNumbersState.addAll(it)
                        updateSate()
                    }
            }
        }
    }

    fun add(number: Int) {
        val entity = NumberModel(
            number,
            System.currentTimeMillis()
        )
        savedNumbersState.add(entity)
        updateSate()
        userId?.let { db.collection(it).add(entity) }
    }

    fun remove(number: Int) {
        savedNumbersState.firstOrNull { it.number == number }
            ?.let { model ->
                savedNumbersState.remove(model)
                updateSate()
                // Remove from Firestore
                userId?.apply {
                    val collectionReference = db.collection(this)
                    collectionReference.get().addOnCompleteListener { task ->
                        task.result
                            .documents
                            .firstOrNull {
                                (it.data!!["number"] as Long).toInt() == number
                            }
                            ?.apply { collectionReference.document(id).delete() }
                    }
                }
            }
    }

    private fun updateSate() {
        generateFibonacci()
    }

    fun removeAll() {
        savedNumbersState.clear()
        updateSate()
        userId?.apply {
            db.collection(this)
                .document()
                .delete()
        }
    }

    private fun logout() {
        savedNumbersState.clear()
        updateSate()
    }
}