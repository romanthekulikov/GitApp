package com.example.gitapp

import android.Manifest
import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.hasDescendant
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.activityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.GrantPermissionRule
import com.example.gitapp.ui.main.MainActivity
import com.example.gitapp.ui.main.RepoAdapter
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.YAxis
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

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
            .perform(ViewActions.typeText("google"), ViewActions.closeSoftKeyboard())

        //start search repository
        onView(withId(R.id.image_enter)).perform(click())

        //click on accompanist
        runBlocking {
            delay(5000)
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .scrollTo<RepoAdapter.RepoViewHolder>(hasDescendant(withText("accompanist")))
            )
            onView(withId(R.id.repositories)).perform(
                RecyclerViewActions
                    .actionOnItem<RepoAdapter.RepoViewHolder>(hasDescendant(withText("accompanist")), click())
            )
            delay(1000)
        }
    }

    @Test
    fun diagram_test() {
        runBlocking {
            onView(withId(R.id.image_next_button)).perform(click())
            delay(2000)
            onView(withId(R.id.image_next_button)).perform(click())
            delay(2000)
            onView(withId(R.id.image_next_button)).perform(click())
            delay(2000)
            onView(withId(R.id.image_next_button)).perform(click())
            delay(2000)
            onView(withId(R.id.tab_layout_period)).perform(selectTabAtPosition(2))
            delay(3000)
            onView(withId(R.id.image_previous_button)).perform(click())

            delay(2000)
            onView(withId(R.id.tab_layout_period)).perform(selectTabAtPosition(1))
            delay(500)
            onView(withId(R.id.image_previous_button)).perform(click())
            delay(500)
            onView(withId(R.id.image_previous_button)).perform(click())
            onView(withId(R.id.bar_chart_histogram)).perform(selectBarEntry(0))

            delay(10000)
        }
    }

    private fun selectBarEntry(entryIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription(): String = "with entry at index $entryIndex"

            override fun getConstraints(): Matcher<View> = allOf(isDisplayed(), isAssignableFrom(BarChart::class.java))

            override fun perform(uiController: UiController?, view: View?) {
                val barChart = (view as BarChart)
                val entry = barChart.barData.getDataSetByIndex(0).getEntryForIndex(0)
                val t = barChart.getPosition(entry, YAxis.AxisDependency.LEFT)
                barChart.performContextClick()
            }

        }
    }

    private fun selectTabAtPosition(tabIndex: Int): ViewAction {
        return object : ViewAction {
            override fun getDescription() = "with tab at index $tabIndex"

            override fun getConstraints() = allOf(isDisplayed(), isAssignableFrom(TabLayout::class.java))

            override fun perform(uiController: UiController, view: View) {
                val tabLayout = view as TabLayout
                val tabAtIndex: TabLayout.Tab = tabLayout.getTabAt(tabIndex)
                    ?: throw PerformException.Builder()
                        .withCause(Throwable("No tab at index $tabIndex"))
                        .build()

                tabAtIndex.select()
            }
        }
    }
}