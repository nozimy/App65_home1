package com.nozimy.app65_home1.network.route.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Leg {

    @SerializedName("steps")
    private List<Step> steps = null;

    public List<Step> getSteps() {return  steps;}

}
