package edu.thinktank.togglz;

import io.dropwizard.Configuration;
import io.dropwizard.jetty.setup.ServletEnvironment;
import io.dropwizard.setup.AdminEnvironment;
import io.dropwizard.setup.Environment;
import org.junit.Test;
import org.togglz.console.TogglzConsoleServlet;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.mem.InMemoryStateRepository;

import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;

public class AbstractFeatureToggleBundleTest {

    @Test
    public void buildFeatureManager() throws Exception {
        final FeatureToggleConfig featureToggleConfig = new FeatureToggleConfig();
        featureToggleConfig.setFeatureSpec(TestFeature.class.getCanonicalName());
        final AbstractFeatureToggleBundle<Configuration> featureToggleBundle = createDefaultBundle(null);

        final FeatureManager featureManager = featureToggleBundle.buildFeatureManager(featureToggleConfig);

        assertThat(featureManager.getFeatures()).isEmpty();
        assertThat(featureManager.getCurrentFeatureUser().getName()).isEqualTo("admin");
        assertThat(featureManager.getCurrentFeatureUser().isFeatureAdmin()).isTrue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void missingFeatureSpecWillThrowExceptionWhileBuildingFeatureManager() throws Exception {
        final FeatureToggleConfig featureToggleConfig = new FeatureToggleConfig();
        final AbstractFeatureToggleBundle<Configuration> featureToggleBundle = createDefaultBundle(null);

        final FeatureManager featureManager = featureToggleBundle.buildFeatureManager(featureToggleConfig);
    }

    @Test
    public void overrideFeatureSettings() throws Exception {
        final AbstractFeatureToggleBundle<Configuration> featureToggleBundle = createDefaultBundle(null);
        final InMemoryStateRepository stateRepository = new InMemoryStateRepository();
        final Feature feature = new Feature() {
            public String name() {
                return "feature";
            }
        };
        stateRepository.setFeatureState(new FeatureState(feature, true));
        final FeatureManager featureManager = new FeatureManagerBuilder().stateRepository(stateRepository).featureEnum(TestFeature.class).build();
        final Map<String, Boolean> featureStatesOverride = new HashMap<>(1);
        featureStatesOverride.put("feature", false);

        assertThat(featureManager.getFeatureState(feature).isEnabled()).isTrue();

        featureToggleBundle.overrideFeatureStatesFromConfig(featureManager, featureStatesOverride);

        assertThat(featureManager.getFeatureState(feature).isEnabled()).isFalse();
    }

    @Test
    public void addServletToAdminContext() throws Exception {
        final Environment environment = mock(Environment.class);
        final AdminEnvironment admin = mock(AdminEnvironment.class, RETURNS_DEEP_STUBS);
        when(environment.admin()).thenReturn(admin);
        final AbstractFeatureToggleBundle<Configuration> featureToggleBundle = createDefaultBundle(null);

        featureToggleBundle.addServlet(new FeatureToggleConfig(), environment);

        verify(admin).addServlet("togglz", TogglzConsoleServlet.class);
    }

    @Test
    public void addServletToApplicationContext() throws Exception {
        final Environment environment = mock(Environment.class);
        final AdminEnvironment adminMock = mock(AdminEnvironment.class);
        when(environment.admin()).thenReturn(adminMock);
        final ServletEnvironment servletMock = mock(ServletEnvironment.class, RETURNS_DEEP_STUBS);
        when(environment.servlets()).thenReturn(servletMock);
        final AbstractFeatureToggleBundle<Configuration> featureToggleBundle = createDefaultBundle(null);

        final FeatureToggleConfig config = spy(new FeatureToggleConfig());
        when(config.isServletContextAdmin()).thenReturn(false);

        featureToggleBundle.addServlet(config, environment);

        verifyZeroInteractions(adminMock);
        verify(servletMock).addServlet("togglz", TogglzConsoleServlet.class);
    }

    private AbstractFeatureToggleBundle<Configuration> createDefaultBundle(final FeatureToggleConfig featureToggleConfig) {
        return new AbstractFeatureToggleBundle<Configuration>() {
            @Override
            public FeatureToggleConfig getBundleConfiguration(final Configuration configuration) {
                return featureToggleConfig;
            }
        };
    }
}