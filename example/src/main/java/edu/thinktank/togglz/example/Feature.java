package edu.thinktank.togglz.example;

import org.togglz.core.annotation.EnabledByDefault;
import org.togglz.core.context.FeatureContext;

public enum Feature implements org.togglz.core.Feature {

    FEATURE_ONE,

    @EnabledByDefault
    FEATURE_TWO;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
