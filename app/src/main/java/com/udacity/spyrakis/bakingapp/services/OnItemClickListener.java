package com.udacity.spyrakis.bakingapp.services;

import android.view.View;

import com.udacity.spyrakis.bakingapp.models.Recipe;

/**
 * Created by pspyrakis on 30/5/18.
 */
public interface OnItemClickListener {
    void onItemClick(View view,Recipe item);
}
