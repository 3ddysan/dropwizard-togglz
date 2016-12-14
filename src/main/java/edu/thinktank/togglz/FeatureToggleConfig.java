package edu.thinktank.togglz;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotBlank;
import org.togglz.core.Feature;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

public class FeatureToggleConfig {

    private static final String DEFAULT_TOGGLZ_MAPPING = "/togglz/*";
    private static final String TOGGLZ_SERVLET_NAME = "togglz";

    @JsonProperty("alwaysOverrideFeatureStates")
    private Boolean alwaysOverrideFeatureStates = false;

    @JsonProperty("features")
    private Map<String, Boolean> features = new HashMap<>();

    private Class<? extends Feature> featureSpec;

    @JsonProperty("servletURLMapping")
    @NotBlank
    @NotNull
    private String servletURLMapping = DEFAULT_TOGGLZ_MAPPING;

    @JsonProperty("servletName")
    @NotBlank
    @NotNull
    private String servletName = TOGGLZ_SERVLET_NAME;

    @JsonProperty("servletContextAdmin")
    private Boolean servletContextAdmin = true;

    @JsonProperty("spec")
    @NotNull
    @NotBlank
    public void setFeatureSpec(final String specReference) {
        try {
            this.featureSpec = (Class<? extends Feature>) Class.forName(specReference);
        } catch (final ClassNotFoundException e) {
            throw new IllegalArgumentException("Feature spec must be a reference to an existing class.");
        }
    }

    public String getServletURLMapping() {
        return servletURLMapping;
    }

    public String getServletName() {
        return servletName;
    }

    public Boolean isServletContextAdmin() {
        return servletContextAdmin;
    }

    public Class<? extends Feature> getFeatureSpec() {
        return this.featureSpec;
    }

    public Map<String, Boolean> getFeatures() {
        return features;
    }

    public Boolean alwaysOverrideFeatureStates() {
        return alwaysOverrideFeatureStates;
    }
}
