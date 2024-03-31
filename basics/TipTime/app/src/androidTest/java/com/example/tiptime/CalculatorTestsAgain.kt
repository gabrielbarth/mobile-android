package com.example.tiptime

import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CalculatorTestsAgain {

    @get:Rule()
    val activity = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun calculate_20_percent_tip() {
        Espresso.onView(ViewMatchers.withId(R.id.cost_of_service_edit_text))
            .perform(ViewActions.typeText("50.00"))

        Espresso.onView(ViewMatchers.withId(R.id.calculate_button)).perform(ViewActions.click())

        Espresso.onView(ViewMatchers.withId(R.id.tip_result))
            .check(ViewAssertions.matches(ViewMatchers.withText(Matchers.containsString("$10.00"))))
    }

}