package com.udacity.spyrakis.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.models.IngredientsItem;
import com.udacity.spyrakis.bakingapp.models.Recipe;

/**
 * Created by pspyrakis on 30/5/18.
 */
public class RecipeIngredientsAdapter extends RecyclerView.Adapter<RecipeIngredientsAdapter.ViewHolder> {

    private Recipe recipe;
    private final boolean mTwoPane;

    private final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            IngredientsItem item = (IngredientsItem) view.getTag();
            //play video
            if (mTwoPane) {

            } else {

            }
        }
    };

    public RecipeIngredientsAdapter(Recipe item, boolean twoPane) {
        recipe = item;
        mTwoPane = twoPane;
    }

    @NonNull
    @Override
    public RecipeIngredientsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new RecipeIngredientsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientsAdapter.ViewHolder holder, int position) {
        int adjustedPosition = position - 1;
        if (position == 0) {
            holder.mContentView.setText(recipe.getIngredientsAsString());
        } else {
            holder.mContentView.setText(recipe.getSteps().get(adjustedPosition).getShortDescription());
            holder.itemView.setTag(recipe.getIngredients().get(adjustedPosition));
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