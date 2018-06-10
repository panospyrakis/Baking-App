package com.udacity.spyrakis.bakingapp.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.udacity.spyrakis.bakingapp.R;
import com.udacity.spyrakis.bakingapp.models.Recipe;
import com.udacity.spyrakis.bakingapp.services.OnItemClickListener;

import java.util.List;

/**
 * Created by pspyrakis on 26/5/18.
 */
public class SimpleRecipeRecyclerViewAdapter
        extends RecyclerView.Adapter<SimpleRecipeRecyclerViewAdapter.ViewHolder> {

    private final List<Recipe> mValues;

    private final boolean mTwoPane;
    private final OnItemClickListener mOnClickListener;

    public SimpleRecipeRecyclerViewAdapter(List<Recipe> items,
                                           OnItemClickListener list,
                                           boolean twoPane) {
        mValues = items;
        mTwoPane = twoPane;
        mOnClickListener = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_list_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mContentView.setText(mValues.get(position).getName());
        if (mValues.get(position).getImage() != null && !mValues.get(position).getImage().equals("")) {
            Picasso.get().load(mValues.get(position).getImage()).into(holder.mImageView);
        }
        holder.itemView.setTag(mValues.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClickListener.onItemClick(v, (Recipe) v.getTag());
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        final TextView mContentView;
        final ImageView mImageView;

        ViewHolder(View view) {
            super(view);
            mContentView = (TextView) view.findViewById(R.id.content);
            mImageView = (ImageView) view.findViewById(R.id.recipe_image);
        }
    }
}
