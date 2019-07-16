package mindvalley.demo

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import mindvalley.demo.view.MainActivity
import mindvalley.loader.R
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @Rule
    @JvmField
    val activityRule  = ActivityTestRule(MainActivity::class.java)

    @Test
    fun verifyTitle(){
        onView(withId(R.id.tvTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.tvTitle)).check(matches(withText(R.string.app_name)))
    }

    @Test
    fun verifyImageViewDisplayed(){
        onView(withId(R.id.avatarImageView)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyNextButtonIsDisplayed(){
        onView(withId(R.id.nextButton)).check(matches(withText(R.string.load_next_avatar)))
        onView(withId(R.id.nextButton)).check(matches(isDisplayed()))
    }
}