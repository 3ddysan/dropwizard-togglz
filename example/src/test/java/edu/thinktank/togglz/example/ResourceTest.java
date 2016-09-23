package edu.thinktank.togglz.example;

import edu.thinktank.togglz.FeatureToggleTestSupport;
import io.dropwizard.testing.junit.ResourceTestRule;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceTest {

    @ClassRule
    public static final ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new Resource())
            .build();

    @Before
    public void setupFeatures() {
        FeatureToggleTestSupport.initAllFeatures(Feature.class, false);
    }

    @Test
    public void testFeatureOne() throws Exception {
        FeatureToggleTestSupport.enable(Feature.FEATURE_ONE);
        String response = resources.client().target("/").request().get().readEntity(String.class);
        assertThat(response).isEqualTo("feature one enabled, feature two disabled");
    }

    @Test
    public void testFeatureTwo() throws Exception {
        FeatureToggleTestSupport.enable(Feature.FEATURE_TWO);
        String response = resources.client().target("/").request().get().readEntity(String.class);
        assertThat(response).isEqualTo("feature one disabled, feature two enabled");
    }

    @Test
    public void testFeatureOneAndTwo() throws Exception {
        FeatureToggleTestSupport.enable(Feature.FEATURE_ONE);
        FeatureToggleTestSupport.enable(Feature.FEATURE_TWO);
        String response = resources.client().target("/").request().get().readEntity(String.class);
        assertThat(response).isEqualTo("feature one enabled, feature two enabled");
    }

}