package com.example.gitapp.screens

import android.Manifest
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.example.gitapp.R
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.main.RepoAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_OWNER = "google"

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Test
    fun searchRepo_mainActivity() {
        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText(TEST_OWNER), closeSoftKeyboard())
        //start search repository
        onView(withId(R.id.image_enter)).perform(click())
        runBlocking {
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RepoAdapter.RepoViewHolder>(
                    1,
                    click()
                )
            )
        }
    }

    @Test
    fun repos_reachLimit() {
        var timeOut = false
        val handler = Handler(Looper.getMainLooper())
        handler.post {
            val timer = object : CountDownTimer(1800000, 60000) {
                override fun onTick(millisUntilFinished: Long) { /** nothing **/ }

                override fun onFinish() {
                    timeOut = true
                }
            }
            timer.start()
        }
        var limitReached = false
        var nextScrollPosition = 99
        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText(TEST_OWNER), closeSoftKeyboard())
        //start search repository
        onView(withId(R.id.image_enter)).perform(click())
        runBlocking {
            while (!limitReached && !timeOut) {
                delay(15000)
                try {
                    onView(withId(R.id.button_ok)).perform(click())
                    limitReached = true
                } catch (exception: NoMatchingViewException) {
                    onView(withId(R.id.repositories)).perform(
                        RecyclerViewActions.scrollToPosition<RepoAdapter.RepoViewHolder>(
                            nextScrollPosition
                        )
                    )
                    nextScrollPosition += 100
                }
            }

        }

    }

    @Test
    fun searchRepos_disableInternet() {
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText(TEST_OWNER), closeSoftKeyboard())
        onView(withId(R.id.image_enter)).perform(click())
        runBlocking {
            delay(2000)
            onView(withId(R.id.button_ok)).perform(click())
            onView(withId(R.id.button_again)).perform(click())
            delay(2000)
        }
    }

    @Test
    fun searchRepos_withScrolling() {
        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText(TEST_OWNER), closeSoftKeyboard())
        //start search repository
        onView(withId(R.id.image_enter)).perform(click())
        runBlocking {
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions.scrollToPosition<RepoAdapter.RepoViewHolder>(99)
            )
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions.actionOnItemAtPosition<RepoAdapter.RepoViewHolder>(
                    105,
                    click()
                )
            )
        }
    }

    @Test
    fun searchRepos_notFoundOwner() {
        //write ogppap to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText("ogppap"), closeSoftKeyboard())
        //start search repository
        onView(withId(R.id.image_enter)).perform(click())
        runBlocking {
            delay(2000)
        }
    }
}