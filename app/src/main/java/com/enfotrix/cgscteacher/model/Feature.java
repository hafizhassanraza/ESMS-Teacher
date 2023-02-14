package com.enfotrix.cgscteacher.model;

public class Feature {
    private String featureName;

    public Feature(String featureName, int resourceId) {
        this.featureName = featureName;
        this.resourceId = resourceId;
    }

    private int resourceId;

    public String getFeatureName() {
        return featureName;
    }

    public void setFeatureName(String featureName) {
        this.featureName = featureName;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }
}
