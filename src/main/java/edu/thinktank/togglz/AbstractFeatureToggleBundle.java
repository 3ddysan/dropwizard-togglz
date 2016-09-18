package edu.thinktank.togglz;

import io.dropwizard.Configuration;
import io.dropwizard.ConfiguredBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import org.togglz.console.TogglzConsoleServlet;
import org.togglz.core.Feature;
import org.togglz.core.manager.FeatureManager;
import org.togglz.core.manager.FeatureManagerBuilder;
import org.togglz.core.repository.FeatureState;
import org.togglz.core.repository.mem.InMemoryStateRepository;
import org.togglz.core.user.FeatureUser;
import org.togglz.core.user.SimpleFeatureUser;
import org.togglz.core.user.UserProvider;

import javax.servlet.ServletRegistration;
import java.util.Map;

public abstract class AbstractFeatureToggleBundle<T extends Configuration> implements ConfiguredBundle<T> {

    public abstract FeatureToggleConfig getBundleConfiguration(final T configuration);

    @Override
    public void initialize(final Bootstrap<?> bootstrap) {/*do nothing*/}

    @Override
    public void run(final T configuration, final Environment environment) throws Exception {
        final FeatureToggleConfig config = getBundleConfiguration(configuration);
        final FeatureManager fm = buildFeatureManager(config);
        overrideFeatureStatesFromConfig(fm, config);
        FeatureToggleProvider.bind(fm);
        addServlet(config, environment);
    }

    private void addServlet(final FeatureToggleConfig configuration, final Environment environment) {
        final ServletRegistration.Dynamic servletRegistration;
        if(configuration.isServletContextAdmin()) {
            servletRegistration = environment.admin().addServlet(configuration.getServletName(), TogglzConsoleServlet.class);
        } else {
            servletRegistration = environment.servlets().addServlet(configuration.getServletName(), TogglzConsoleServlet.class);
        }
        servletRegistration.addMapping(configuration.getServletURLMapping());
    }

    private void overrideFeatureStatesFromConfig(final FeatureManager fm, final FeatureToggleConfig config) {
        for(Map.Entry<String, Boolean> feature : config.getFeatures().entrySet()) {
            fm.setFeatureState(new FeatureState(new Feature() {
                public String name() {
                    return feature.getKey();
                }
            }, feature.getValue()));
        }
    }

    protected FeatureManager buildFeatureManager(final FeatureToggleConfig config) {
        return new FeatureManagerBuilder()
                .stateRepository(new InMemoryStateRepository())
                .featureEnum(config.getFeatureSpec())
                .userProvider(new UserProvider() {
                    @Override
                    public FeatureUser getCurrentUser() {
                        return new SimpleFeatureUser("admin", true);
                    }
                }).build();
    }

}
