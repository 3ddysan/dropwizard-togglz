package edu.thinktank.togglz.example;

import edu.thinktank.togglz.AbstractFeatureToggleBundle;
import edu.thinktank.togglz.FeatureToggleConfig;
import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class App extends Application<Config> {

    @Override
    public void initialize(final Bootstrap<Config> bootstrap) {
        bootstrap.addBundle(new AbstractFeatureToggleBundle<Config>() {
            @Override
            public FeatureToggleConfig getBundleConfiguration(final Config configuration) {
                return configuration.toggleConfig;
            }
        });
    }

    @Override
    public void run(final Config config, final Environment environment) {
        environment.jersey().register(new Resource());
    }

    public static void main(final String[] args) throws Exception {
        new App().run(args);
    }

}
