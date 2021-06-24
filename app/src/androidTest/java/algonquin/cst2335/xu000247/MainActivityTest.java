package algonquin.cst2335.xu000247;


import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    /** This function is used to test the password that meets the requirements.
     */
    @Test
    public void mainActivityTest() {
        ViewInteraction appCompatEditText = onView( withId(R.id.editText) );
        appCompatEditText.perform(replaceText("passWORD123#$*"), closeSoftKeyboard());

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("Your password meets the requirements.")));
    }

    /** This function is used to test the password that is only missing upper case letter.
     */
    @Test
    public void testFindMissingUpperCase(){
        ViewInteraction appCompatEditText = onView( withId(R.id.editText) );
        appCompatEditText.perform(replaceText("password123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }
    /** This function is used to test the password that is only missing Lower case letter.
     */
    @Test
    public void testFindMissingLowerCase(){
        ViewInteraction appCompatEditText = onView( withId(R.id.editText) );
        appCompatEditText.perform(replaceText("PASSWORD123#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }
    /** This function is used to test the password that is only missing number.
     */
    @Test
    public void testFindMissingDigit(){
        ViewInteraction appCompatEditText = onView( withId(R.id.editText) );
        appCompatEditText.perform(replaceText("passWORD#$*"));

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }
    /** This function is used to test the password that is only missing special symbol.
     */
    @Test
    public void testFindMissingSymbol(){
        ViewInteraction appCompatEditText = onView( withId(R.id.editText) );
        appCompatEditText.perform(replaceText("passWORD123"));

        ViewInteraction materialButton = onView(withId(R.id.button));
        materialButton.perform(click());

        ViewInteraction textView = onView( withId(R.id.textView) );
        textView.check(matches(withText("You shall not pass!")));
    }


    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
