package com.udacity.spyrakis.bakingapp.activities;

import android.content.ContentValues;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.fragments.RecipeDetailFragment;
import com.udacity.spyrakis.bakingapp.models.IngredientsItem;
import com.udacity.spyrakis.bakingapp.models.Recipe;
import com.udacity.spyrakis.bakingapp.provider.RecipiesContract;
import com.udacity.spyrakis.bakingapp.widget.RecipeAppWidgetProvider;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * An activity representing a single Recipe detail screen. This
 * activity is only used on narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link RecipeListActivity}.
 */
public class RecipeDetailActivity extends BaseActivity {

    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            recipe = getIntent().getParcelableExtra(RecipeDetailFragment.ARG_ITEM);
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipe, mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.recipe_detail_list, fragment)
                    .commit();
        } else {
            recipe = savedInstanceState.getParcelable(RecipeDetailFragment.ARG_ITEM);
            RecipeDetailFragment fragment = RecipeDetailFragment.newInstance(recipe, mTwoPane);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_detail_list, fragment)
                    .commit();
        }
        if (recipe != null) {
            getContentResolver().delete(RecipiesContract.RecipeEntry.CONTENT_URI, null, null);
            for (IngredientsItem item : recipe.getIngredients()) {
                ContentValues itemToAdd = new ContentValues();
                itemToAdd.put(RecipiesContract.RecipeEntry.TITLE, recipe.getName());
                itemToAdd.put(RecipiesContract.RecipeEntry.INGREDIENTS, item.toDisplayString());
                getContentResolver().insert(RecipiesContract.RecipeEntry.CONTENT_URI, itemToAdd);
            }
            RecipeAppWidgetProvider.sendRefreshBroadcast(getApplicationContext());
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelable(RecipeDetailFragment.ARG_ITEM, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = savedInstanceState.getParcelable(RecipeDetailFragment.ARG_ITEM);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
