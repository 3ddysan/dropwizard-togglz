package edu.thinktank.togglz;

import org.junit.Before;
import org.junit.Test;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.LazyResolvingFeatureManager;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureToggleProviderTest {

    @Before
    public void resetProvider() {
        final Field field;
        final Class<?> aClass;
        try {
            aClass = Class.forName(FeatureToggleProvider.class.getCanonicalName());
            field = aClass.getDeclaredField("featureManager");
        } catch (final NoSuchFieldException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        field.setAccessible(true);
        final Object oldValue;
        try {
            oldValue = field.get(aClass);
            field.set(oldValue, null);
        } catch (final IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void bindManager() throws Exception {
        final FeatureManager fm = new LazyResolvingFeatureManager();

        FeatureToggleProvider.bind(fm);

        assertThat(new FeatureToggleProvider().getFeatureManager()).isEqualTo(fm);
    }

    @Test
    public void bindOnlyOnceManager() throws Exception {
        final FeatureManager fm = new LazyResolvingFeatureManager();
        final FeatureManager fm2 = new LazyResolvingFeatureManager();
        FeatureToggleProvider.bind(fm);

        FeatureToggleProvider.bind(fm2);

        assertThat(new FeatureToggleProvider().getFeatureManager()).isEqualTo(fm);
    }

    @Test
    public void defaultPriority() throws Exception {
        final FeatureToggleProvider featureToggleProvider = new FeatureToggleProvider();

        assertThat(featureToggleProvider.priority()).isEqualTo(10);
    }
}