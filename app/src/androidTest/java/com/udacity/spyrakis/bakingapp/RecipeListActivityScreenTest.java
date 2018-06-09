package com.udacity.spyrakis.bakingapp;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.udacity.spyrakis.bakingapp.activities.RecipeListActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by pspyrakis on 10/6/18.
 */
@RunWith(AndroidJUnit4.class)
public class RecipeListActivityScreenTest {
    public static final String RECIPE_TEXT = "Baking App";

    @Rule
    public ActivityTestRule<RecipeListActivity> mActivityTestRule = new ActivityTestRule<>(RecipeListActivity.class);


    @Test
    public void clickRecyclerViewItem_OpensDetailsActivity() {

        onView(withId(R.id.main_title)).check(matches(withText(RECIPE_TEXT)));

    }
}
