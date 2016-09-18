package edu.thinktank.togglz.example;

import com.fasterxml.jackson.annotation.JsonProperty;
import edu.thinktank.togglz.FeatureToggleConfig;
import io.dropwizard.Configuration;

public class Config extends Configuration {

    @JsonProperty("togglz")
    public FeatureToggleConfig toggleConfig;

}
