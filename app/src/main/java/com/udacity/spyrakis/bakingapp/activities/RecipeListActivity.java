package com.udacity.spyrakis.bakingapp.activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.adapters.SimpleRecipeRecyclerViewAdapter;
import com.udacity.spyrakis.bakingapp.fragments.RecipeDetailFragment;
import com.udacity.spyrakis.bakingapp.models.Recipe;
import com.udacity.spyrakis.bakingapp.services.OnItemClickListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity representing a list of Recipies. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link RecipeDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class RecipeListActivity extends BaseActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.recipe_list)
    View recyclerView;

    @BindView(R.id.main_title)
    TextView mainTitle;

    ProgressDialog progress;

    List<Recipe> recipeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        if (mTwoPane) {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        assert recyclerView != null;
        setUpNetworkCalls();
        makeSomeCalls();
    }

    @Override
    public void onStart() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
        super.onStart();
    }

    private void makeSomeCalls() {
        progress = new ProgressDialog(this);
        progress.setTitle(getApplicationContext().getString(R.string.loading));
        progress.setMessage(getApplicationContext().getString(R.string.wait));
        progress.setCancelable(false); // disable dismiss by tapping outside of the dialog
        progress.show();

        Call<List<Recipe>> listCall = service.getRecipies();


        listCall.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {

                if (!isActive) return;

                recipeList = response.body();
                setupRecyclerView((RecyclerView) recyclerView);
                progress.dismiss();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progress.dismiss();
                Log.d("call fail", "call failed", t);
            }
        });
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        OnItemClickListener listener = new OnItemClickListener() {
            @Override
            public void onItemClick(View view, Recipe item) {
                Context context = view.getContext();
                Intent intent = new Intent(context, RecipeDetailActivity.class);
                intent.putExtra(RecipeDetailFragment.ARG_ITEM, item);
                startActivity(intent);
            }
        };
        if (mTwoPane) {
            GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 3);
            recyclerView.setLayoutManager(manager);
        }
        recyclerView.setAdapter(new SimpleRecipeRecyclerViewAdapter(recipeList, listener, mTwoPane));
    }


}
