package sg.edu.nus.iss.se8.medipal;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import sg.edu.nus.iss.se8.medipal.activities.MainActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class UiNavigationTest {
    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void clickTestButton() {
       // onView(withId(R.id.testButton)).perform(click());

        onView(withText("test button"))
                .inRoot(withDecorView(not(activityTestRule.getActivity().getWindow().getDecorView())))
                .check(matches(isDisplayed()));

//        onView(withId(R.id.editTextUserInput)).perform(typeText(STRING_TO_BE_TYPED), closeSoftKeyboard());
//
//        onView(withId(R.id.changeTextBt)).perform(click());
//
//        onView(withId(R.id.textToBeChanged)).check(matches(withText(STRING_TO_BE_TYPED)));
    }

//    @Test
//    public void exerciseTabsInTabLayout() {
//        Matcher<View> matcher = allOf(withText("MP"), isDescendantOfA(withId(R.id.tab_layout)));
//        onView(matcher).perform(click());
//        SystemClock.sleep(800);
//    }

//    @Test
//    public void useAppContext() throws Exception {
//        // Context of the app under test.
//        Context appContext = InstrumentationRegistry.getTargetContext();
//
//        assertEquals("sg.edu.nus.iss.se8.medipal", appContext.getPackageName());
//    }
}
