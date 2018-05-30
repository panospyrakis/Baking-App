package com.udacity.spyrakis.bakingapp.services;

import com.udacity.spyrakis.bakingapp.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by pspyrakis on 30/5/18.
 */

public interface BakingAppApiService {
    @GET("baking.json")
    Call<List<Recipe>> getRecipies();
}
