package com.example.gitapp

import android.Manifest
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.BoundedMatcher
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.rule.GrantPermissionRule
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.main.RepoAdapter
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainInstrumentedTest {

    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Test
    fun searchRepo_mainActivity() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.gitapp", appContext.packageName)

        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner)).perform(typeText("google"), closeSoftKeyboard())
        //start search repository
        onView(withId(R.id.image_enter)).perform(click())
        onView(withId(R.id.repositories)).check(matches(recyclerViewWaitMatcher(2)))
        onView(withId(R.id.repositories))
            .perform(RecyclerViewActions
                .actionOnItemAtPosition<RepoAdapter.RepoViewHolder>(2, click()))
    }

    private fun recyclerViewWaitMatcher(untilMinimumCount: Int): Matcher<View?> {
        return object : BoundedMatcher<View?, RecyclerView>(RecyclerView::class.java) {
            override fun describeTo(description: Description) {

            }

            override fun matchesSafely(recyclerView: RecyclerView): Boolean {
                var count = recyclerView.adapter!!.itemCount
                while (count < untilMinimumCount) {
                    count = recyclerView.adapter!!.itemCount
                }
                return true
            }
        }
    }
}