package edu.thinktank.togglz;

import org.togglz.core.manager.FeatureManager;
import org.togglz.core.spi.FeatureManagerProvider;

public class FeatureToggleProvider implements FeatureManagerProvider {

    private static final int DEFAULT_PRIORITY = 0;
    private static FeatureManager featureManager;

    public static void bind(final FeatureManager fm) {
        if(featureManager == null)
            featureManager = fm;
    }

    @Override
    public synchronized FeatureManager getFeatureManager() {
        return featureManager;
    }

    @Override
    public int priority() {
        return DEFAULT_PRIORITY;
    }
}
