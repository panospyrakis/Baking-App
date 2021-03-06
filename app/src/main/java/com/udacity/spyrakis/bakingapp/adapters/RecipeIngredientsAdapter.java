package com.udacity.spyrakis.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.activities.BaseActivity;
import com.udacity.spyrakis.bakingapp.activities.DetailsActivity;
import com.udacity.spyrakis.bakingapp.fragments.DetailsFragment;
import com.udacity.spyrakis.bakingapp.models.Recipe;
import com.udacity.spyrakis.bakingapp.models.StepsItem;

import java.util.ArrayList;

/**
 * Created by pspyrakis on 30/5/18.
 */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private Recipe recipe;
    private final boolean mTwoPane;
    private BaseActivity mParentActivity;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            ArrayList<StepsItem> argumentList = new ArrayList<>(recipe.getSteps());

            //play video
            if (mTwoPane) {
                DetailsFragment fragment = DetailsFragment.newInstance(argumentList, position);
                mParentActivity.getSupportFragmentManager().beginTransaction()
                        .replace(R.id.step_details_container, fragment)
                        .commit();
            } else {
                Context context = view.getContext();
                Intent intent = new Intent(context, DetailsActivity.class);

                intent.putParcelableArrayListExtra(DetailsActivity.ARG_STEPS, argumentList);
                intent.putExtra(DetailsActivity.ARG_POSITION, position);
                mParentActivity.startActivity(intent);
            }
        }
    };

    public RecipeIngredientsAdapter(BaseActivity parentActivity, Recipe item, boolean twoPane) {
        recipe = item;
        mTwoPane = twoPane;
        mParentActivity = parentActivity;
    }

    @NonNull
    @Override
    public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredients_list_content, parent, false);
        return new RecipeIngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsAdapter.ViewHolder holder, int position) {
        int adjustedPosition = position - 1;
        if (position == 0) {
            holder.mContentView.setText(recipe.getIngredientsAsString());
        } else {
            holder.mContentView.setText(recipe.getSteps().get(adjustedPosition).getShortDescription());
            holder.itemView.setTag(adjustedPosition);
            holder.itemView.setOnClickListener(mOnClickListener);
        }
    }

    @Override
    public int getItemCount() {

        return recipe.getSteps().size() + 1;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;

        ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
        }
    }
}
