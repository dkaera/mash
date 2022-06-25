package com.dkaera.mash.ui.onboarding

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import timber.log.Timber

class OnboardingViewModel : ViewModel() {

    private val auth: FirebaseAuth by lazy { FirebaseAuth.getInstance() }
    val currentUserState = mutableStateOf(auth.currentUser != null)

    fun doSignUp(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d("createUserWithEmail:success $currentUser")
                    currentUserState.value = currentUser != null
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w("createUserWithEmail:failure", task.exception)
                }
            }
    }

    fun doSignIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val currentUser = auth.currentUser
                    // Sign in success, update UI with the signed-in user's information
                    Timber.d( "signInWithEmail:success $currentUser")
                    currentUserState.value = currentUser != null
                } else {
                    // If sign in fails, display a message to the user.
                    Timber.w( "signInWithEmail:failure", task.exception)
                }
            }
    }

    fun logout() {
        auth.signOut()
        currentUserState.value = false
    }
}