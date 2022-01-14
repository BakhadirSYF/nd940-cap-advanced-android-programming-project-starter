package com.example.android.politicalpreparedness.launch

import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.example.android.politicalpreparedness.R
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@MediumTest
class LaunchFragmentTest {

    @Test
    fun onElectionsButtonClick_navigateToElectionsFragment() = runBlockingTest {
        // GIVEN - on LaunchFragment screen
        val scenario = launchFragmentInContainer<LaunchFragment>(null, R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - "Upcoming Elections" button clicked
        onView(withId(R.id.upcomingElectionsButton)).perform(click())

        // THEN - verify user navigated to elections fragment
        verify(navController).navigate(LaunchFragmentDirections.actionLaunchFragmentToElectionsFragment())
    }

    @Test
    fun onFindRepsButtonClick_navigateToRepresentativeFragment() = runBlockingTest {
        // GIVEN - on LaunchFragment screen
        val scenario = launchFragmentInContainer<LaunchFragment>(null, R.style.AppTheme)
        val navController = Mockito.mock(NavController::class.java)

        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // WHEN - "Find My Representatives" button clicked
        onView(withId(R.id.findRepButton)).perform(click())

        // THEN - verify user navigated to representative details fragment
        verify(navController).navigate(LaunchFragmentDirections.actionLaunchFragmentToRepresentativeFragment())
    }

}