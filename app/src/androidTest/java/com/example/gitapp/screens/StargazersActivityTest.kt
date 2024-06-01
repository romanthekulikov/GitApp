package com.example.gitapp.screens

import android.Manifest
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.GrantPermissionRule
import com.example.gitapp.GitViewActions
import com.example.gitapp.R
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.main.RepoAdapter
import com.example.gitapp.ui.stargazers.StargazersAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class StargazersActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Before
    fun openStargazersActivity_GoogleAccompanist() {
        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner))
            .perform(typeText("google"), closeSoftKeyboard())

        //start search repository
        onView(withId(R.id.image_enter)).perform(click())

        runBlocking {
            //click on accompanist
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .scrollTo<RepoAdapter.RepoViewHolder>(hasDescendant(withText("accompanist")))
            )
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .actionOnItem<RepoAdapter.RepoViewHolder>(hasDescendant(withText("accompanist")), click())
            )
            delay(5000)

            onView(withId(R.id.tab_layout_period)).perform(GitViewActions().selectTabAtPosition(2))
            delay(5000)
            // move back period
            onView(withId(R.id.image_next_button)).check { view, _ ->
                view.performClick()
            }
            delay(10000)
            // select entry
            onView(withId(R.id.bar_chart_histogram)).perform(click())
            delay(2000)
        }
    }

    @Test
    fun stargazers_checkList() {
        runBlocking {
            onView(withId(R.id.recyclerview_stargazers))
                .perform(RecyclerViewActions.scrollToPosition<StargazersAdapter.StargazerViewHolder>(2))
        }
    }
}