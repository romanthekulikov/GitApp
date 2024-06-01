package com.example.gitapp.screens

import android.Manifest
import android.view.InputDevice
import android.view.MotionEvent
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.GeneralClickAction
import androidx.test.espresso.action.GeneralLocation
import androidx.test.espresso.action.Press
import androidx.test.espresso.action.Tap
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.example.gitapp.GitViewActions
import com.example.gitapp.R
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.main.RepoAdapter
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

private const val TEST_OWNER = "google"
private const val TEST_REPO = "accompanist"

@RunWith(AndroidJUnit4::class)
@LargeTest
class DiagramActivityTest {
    @get:Rule
    var activityScenarioRule = activityScenarioRule<MainActivity>()

    @get:Rule
    var permissionRule: GrantPermissionRule = GrantPermissionRule.grant(Manifest.permission.POST_NOTIFICATIONS)

    @Before
    fun openDiagramActivity_GoogleAccompanist() {
        //write google to field
        onView(withId(R.id.input_owner)).perform(click())
        onView(withId(R.id.input_owner))
            .perform(ViewActions.typeText(TEST_OWNER), ViewActions.closeSoftKeyboard())

        //start search repository
        onView(withId(R.id.image_enter)).perform(click())

        //click on accompanist
        runBlocking {
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .scrollTo<RepoAdapter.RepoViewHolder>(hasDescendant(withText(TEST_REPO)))
            )
            delay(10000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .actionOnItem<RepoAdapter.RepoViewHolder>(hasDescendant(withText(TEST_REPO)), click())
            )
            delay(1000)
        }
    }

    @Test
    fun diagram_test() {
        runBlocking {
            // move back period
            onView(withId(R.id.image_next_button)).check { view, _ ->
                view.performClick()
            }
            delay(2000)

            // move forward period
            onView(withId(R.id.image_previous_button)).check { view, _ ->
                view.performClick()
            }
            delay(2000)

            // change diagram mode to week
            onView(withId(R.id.tab_layout_period)).perform(GitViewActions().selectTabAtPosition(1))
            delay(2000)

            // change diagram mode to year
            onView(withId(R.id.tab_layout_period)).perform(GitViewActions().selectTabAtPosition(2))
            delay(2000)

            // move back period
            onView(withId(R.id.image_next_button)).check { view, _ ->
                view.performClick()
            }
            delay(10000)

            // select entry
            onView(withId(R.id.bar_chart_histogram)).perform(click())

            delay(10000)
        }
    }

    fun clickBottomCenter(): ViewAction {
        return ViewActions.actionWithAssertions(
            GeneralClickAction(
                Tap.SINGLE,
                GeneralLocation.VISIBLE_CENTER,
                Press.FINGER,
                InputDevice.SOURCE_UNKNOWN,
                MotionEvent.BUTTON_PRIMARY
            )
        )
    }

    @Test
    fun diagram_testReachLimit() {
        runBlocking {
            // change diagram mode to year
            onView(withId(R.id.tab_layout_period)).perform(GitViewActions().selectTabAtPosition(2))
            delay(5000)
            var limitReached = false
            while (!limitReached) {
                try {
                    // reach the limit via cycle of requests
                    onView(withId(R.id.button_ok)).perform(click())
                    limitReached = true
                } catch (exception: NoMatchingViewException) {
                    // move back period
                    onView(withId(R.id.image_next_button)).check { view, _ ->
                        view.performClick()
                    }
                    delay(7000)
                }
            }

        }
    }

    @Test
    fun diagram_testWithoutInternet() {
        onView(withId(R.id.button_ok)).perform(click())
    }
}