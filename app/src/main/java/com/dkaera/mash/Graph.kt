package com.dkaera.mash

import com.dkaera.mash.ui.dashboard.DashboardViewModel
import com.dkaera.mash.ui.onboarding.OnboardingViewModel

/*
    Migrate it to Hilt
 */
object Graph {
    val dashboardViewModel = DashboardViewModel()
    val onboardingViewModel = OnboardingViewModel()
}