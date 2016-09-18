package edu.thinktank.togglz;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class FeatureToggleConfigTest {

    @Test
    public void createDefaultConfig() {
        final FeatureToggleConfig config = new FeatureToggleConfig();

        assertThat(config.getFeatures()).isEmpty();
        assertThat(config.isServletContextAdmin()).isTrue();
        assertThat(config.getFeatureSpec()).isNull();
        assertThat(config.getServletName()).isEqualTo("togglz");
        assertThat(config.getServletURLMapping()).isEqualTo("/togglz/*");
    }

    @Test(expected = IllegalArgumentException.class)
    public void setUnknownFeatureSpecOnConfig() throws Exception {
        final FeatureToggleConfig config = new FeatureToggleConfig();

        config.setFeatureSpec("unknown.package.UnknownFeature");
    }

    @Test
    public void setFeatureSpecOnConfig() throws Exception {
        final FeatureToggleConfig config = new FeatureToggleConfig();

        config.setFeatureSpec(TestFeature.class.getCanonicalName());

        assertThat(config.getFeatureSpec()).isAssignableFrom(TestFeature.class);
    }
}