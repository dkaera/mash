package com.dkaera.mash.ui

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.dkaera.mash.Graph
import com.dkaera.mash.R
import com.dkaera.mash.ui.dashboard.Dashboard
import com.dkaera.mash.ui.onboarding.ConfirmEmail
import com.dkaera.mash.ui.onboarding.OnboardingViewModel
import com.dkaera.mash.ui.onboarding.SignIn
import com.dkaera.mash.ui.onboarding.SignUp

/**
 * Destinations used in the app.
 */
object MainDestinations {
    const val ONBOARDING_ROUTE = "onboarding"
    const val DASHBOARD_ROUTE = "dashboard"
}

fun NavGraphBuilder.dashboard(
    authComplete: State<Boolean>, // https://issuetracker.google.com/174783110
    actions: MainActions,
    onLogout: () -> Unit,
) {
    composable(MainDestinations.DASHBOARD_ROUTE) { backStackEntry ->
        LaunchedEffect(authComplete) {
            if (!authComplete.value) {
                actions.onboarding(backStackEntry)
            }
        }
        if (authComplete.value) { // Avoid glitch when showing onboarding
            Dashboard(onLogout)
        }
    }
}

fun NavGraphBuilder.onboarding(
    modifier: Modifier,
    finishActivity: () -> Unit = {},
    authComplete: () -> Unit = {},
    actions: MainActions,
) {
    composable(OnboardingScreens.SignIn.route) {
        BackHandler { finishActivity() }
        SignIn(
            modifier = modifier,
            { actions.signUp(it) },
            { email, password ->
                Graph.onboardingViewModel.doSignIn(email, password)
                authComplete()
            })
    }
    composable(OnboardingScreens.SignUp.route) {
        SignUp(
            modifier = modifier,
            { actions.upPress(it) },
            { email, password ->
                Graph.onboardingViewModel.doSignUp(email, password)
                authComplete()
            })
    }
    composable(OnboardingScreens.ConfirmEmail.route) { ConfirmEmail(modifier = modifier) { } }
}

enum class OnboardingScreens(
    @StringRes val title: Int,
    val route: String
) {
    SignIn(R.string.sign_in, OnboardingDestinations.SIGN_IN_ROUTE),
    SignUp(R.string.sign_up, OnboardingDestinations.SIGN_UP_ROUTE),
    ConfirmEmail(R.string.confirm_email, OnboardingDestinations.CONFIRM_EMAIL_ROUTE)
}

/**
 * Destinations used in the ([MashApp]).
 */
private object OnboardingDestinations {
    const val SIGN_IN_ROUTE = "onboarding/signIn"
    const val SIGN_UP_ROUTE = "onboarding/signUp"
    const val CONFIRM_EMAIL_ROUTE = "onboarding/confirmEmail"
}