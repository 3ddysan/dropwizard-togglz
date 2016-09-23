package edu.thinktank.togglz;

import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.user.NoOpUserProvider;

public class FeatureToggleTestSupport {

    public static FeatureToggleProvider featureManagerProvider = new FeatureToggleProvider();

    public static void enableAllFeatures(Class<? extends Feature> featureEnum) {
        initAllFeatures(featureEnum, true);
    }

    public static void initAllFeatures(Class<? extends Feature> featureEnum, final Boolean state) {
        FeatureManager featureManager = getFeatureManager(featureEnum);
        for(Feature feature : featureEnum.getEnumConstants()) {
            featureManager.setFeatureState(new FeatureState(feature, state));
        }
    }

    public static void enable(final Feature feature) {
        getFeatureManager(feature.getClass()).setFeatureState(new FeatureState(feature, true));
    }

    public static void disable(final Feature feature) {
        getFeatureManager(feature.getClass()).setFeatureState(new FeatureState(feature, false));
    }

    public static FeatureManager getFeatureManager(Class<? extends Feature> featureEnum) {
        FeatureManager featureManager = featureManagerProvider.getFeatureManager();
        if(featureManager == null) {
            featureManager = buildFeatureManager(featureEnum);
            FeatureToggleProvider.bind(featureManager);
        }
        return featureManager;
    }

    private static FeatureManager buildFeatureManager(Class<? extends Feature> featureEnum) {
        return new FeatureManagerBuilder()
                .stateRepository(new InMemoryStateRepository())
                .featureEnum(featureEnum)
                .userProvider(new NoOpUserProvider()).build();
    }
}
