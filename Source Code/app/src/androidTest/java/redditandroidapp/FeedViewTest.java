package redditandroidapp;

import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import org.junit.Rule;
import org.junit.Test;

import redditandroidapp.features.feed.FeedActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@LargeTest
public class FeedViewTest {

    @Rule
    public ActivityTestRule<FeedActivity> feedActivityTestRule =
            new ActivityTestRule<>(FeedActivity.class);

    @Test
    public void clickOnListedItem_opensDetailedView() throws Exception {

        // Click on the first item.
        onView(ViewMatchers.withId(R.id.main_feed_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Verify if detailed view has been displayed.
        onView(withId(R.id.detailed_view_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickOnCrossButton_closesDetailedView() throws Exception {

        // Click on the first item.
        onView(withId(R.id.main_feed_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        // Click on the Cross button.
        onView(withId(R.id.btn_cross)).perform(ViewActions.click());

        // Verify if detailed view has been closed.
        onView(withId(R.id.detailed_view_container)).check(doesNotExist());
    }
}