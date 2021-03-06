package com.udacity.spyrakis.bakingapp.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.activities.BaseActivity;
import com.udacity.spyrakis.bakingapp.activities.RecipeDetailActivity;
import com.udacity.spyrakis.bakingapp.activities.RecipeListActivity;
import com.udacity.spyrakis.bakingapp.adapters.RecipeIngredientsAdapter;
import com.udacity.spyrakis.bakingapp.models.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Recipe detail screen.
 * This fragment is either contained in a {@link RecipeListActivity}
 * in two-pane mode (on tablets) or a {@link RecipeDetailActivity}
 * on handsets.
 */
public class RecipeDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_TWO_PANE = "ARG_TWO_PANE";
    public static final String ARG_ITEM = "ARG_ITEM";

    private Recipe mItem;
    private Boolean mTwoPane;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public RecipeDetailFragment() {
    }

    public static RecipeDetailFragment newInstance(Recipe item, Boolean twoPane) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_ITEM, item);
        args.putBoolean(ARG_TWO_PANE, twoPane);
        RecipeDetailFragment fragment = new RecipeDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM)) {
            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = getArguments().getParcelable(ARG_ITEM);
            mTwoPane = getArguments().getBoolean(ARG_TWO_PANE);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && mItem != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @BindView(R.id.recipe_ingedients_list)
    RecyclerView recipeList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.recipe_detail, container, false);
        ButterKnife.bind(this, rootView);

        if (mItem != null) {
            recipeList.setLayoutManager(new LinearLayoutManager(getContext()));
            recipeList.setAdapter(new RecipeIngredientsAdapter((BaseActivity) getActivity(), mItem, mTwoPane));
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putBoolean(ARG_TWO_PANE,mTwoPane);
        outState.putParcelable(ARG_ITEM,mItem);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState == null) return;
        mItem = savedInstanceState.getParcelable(ARG_ITEM);
        mTwoPane = savedInstanceState.getBoolean(ARG_TWO_PANE);
    }
}
