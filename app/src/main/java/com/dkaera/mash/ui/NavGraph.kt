/*
 * Copyright 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dkaera.mash.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import com.dkaera.mash.Graph

@Composable
fun NavGraph(
    modifier: Modifier,
    finishActivity: () -> Unit = {},
    navController: NavHostController = rememberNavController(),
    startDestination: String = MainDestinations.DASHBOARD_ROUTE,
) {
    val onboardingComplete = remember { Graph.onboardingViewModel.currentUserState }
    val actions = remember(navController) { MainActions(navController) }

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination
    ) {

        dashboard(onboardingComplete, actions) {
            onboardingComplete.value = false
            Graph.onboardingViewModel.logout()
            actions.logout()
        }

        navigation(
            route = MainDestinations.ONBOARDING_ROUTE,
            startDestination = OnboardingScreens.SignIn.route
        ) {
            onboarding(
                modifier = modifier,
                finishActivity = finishActivity,
                authComplete = {
                    onboardingComplete.value = true
                    actions.onboardingComplete()
                },
                actions = actions,
            )
        }
    }
}

/**
 * Models the navigation actions in the app.
 */
class MainActions(navController: NavHostController) {
    val onboardingComplete: () -> Unit = {
        navController.popBackStack()
    }

    // Used from DASHBOARD_ROUTE
    val onboarding = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(MainDestinations.ONBOARDING_ROUTE)
        }
    }

    // Used from DASHBOARD_ROUTE
    val logout = {
        navController.navigate(MainDestinations.ONBOARDING_ROUTE)
    }

    // Used from DASHBOARD_ROUTE
    val signUp = { from: NavBackStackEntry ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigate(OnboardingScreens.SignUp.route)
        }
    }

    val upPress: (from: NavBackStackEntry) -> Unit = { from ->
        // In order to discard duplicated navigation events, we check the Lifecycle
        if (from.lifecycleIsResumed()) {
            navController.navigateUp()
        }
    }
}

/**
 * If the lifecycle is not resumed it means this NavBackStackEntry already processed a nav event.
 *
 * This is used to de-duplicate navigation events.
 */
private fun NavBackStackEntry.lifecycleIsResumed() =
    this.lifecycle.currentState == Lifecycle.State.RESUMED
