package com.davidecirillo.multichoicesample;

import android.graphics.drawable.ColorDrawable;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.davidecirillo.multichoicesample.api.BaseMultiChoiceActivityTest;
import com.davidecirillo.multichoicesample.sampleToolbar.SampleToolbarActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.StringContains.containsString;

@RunWith(AndroidJUnit4.class)
public class SampleToolbarActivityPluralsTest extends BaseMultiChoiceActivityTest {

    public SampleToolbarActivity mActivity;

    // region BaseMultiChoiceActivityTest

    @Override
    protected boolean isSelected(View view) {
        RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.get_started_relative_layout);

        return !(relativeLayout == null || relativeLayout.getBackground() == null) && ((ColorDrawable) relativeLayout.getBackground()).getColor() == ContextCompat.getColor(mActivity, R.color.colorPrimaryDark);
    }

    // endregion

    // region tests

    @Rule
    public ActivityTestRule<SampleToolbarActivity> mActivityRule = new ActivityTestRule<>(SampleToolbarActivity.class);

    @Before
    public void setUp() {
        mActivity = mActivityRule.getActivity();

        mActivity.runOnUiThread(new Runnable() {
            public void run() {
                mActivity.setQuantityMode(SampleToolbarActivity.QuantityMode.PLURALS);
            }
        });

        wakeScreen(mActivity);
    }

    /**
     * #5 testMultiChoiceToolbar
     * <p>
     * Make sure that the multi choice toolbar is displayed and updated with the correct number of selected items
     */
    @Test
    public void testMultiChoiceToolbarPlurals() {
        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, longClick()));

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(containsString("One"))));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(containsString("2"))));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(R.string.toolbar_controls)));
    }

    /**
     * #9 testSingleClickModeOptionMenu
     * <p>
     * Find and Click on the option menu item "Single Click Mode" and check that the functionality is activated on the
     * MultiChoiceRecyclerView
     */
    @Test
    public void testSingleClickModeOptionMenu() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        onView(withText("Single Click Mode"))
                .perform(click());

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()))
                .check(matches(isSelected()));

        //Check the toolbar as well
        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(containsString("One"))));

        onView(withId(R.id.multiChoiceRecyclerView))
                .perform(RecyclerViewActions.actionOnItemAtPosition(1, click()))
                .check(matches(isSelected()));

        onView(allOf(isAssignableFrom(TextView.class), withParent(isAssignableFrom(Toolbar.class))))
                .check(matches(withText(containsString("2"))));
    }

    // endregion
}
