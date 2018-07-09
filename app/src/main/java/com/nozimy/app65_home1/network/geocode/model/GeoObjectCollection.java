package com.nozimy.app65_home1.network.geocode.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GeoObjectCollection {
    @SerializedName("featureMember")
    private List<FeatureMember> featureMembers;

    public List<FeatureMember> getFeatureMembers() {
        return featureMembers;
    }

    public void setFeatureMembers(List<FeatureMember> featureMembers) {
        this.featureMembers = featureMembers;
    }
}
