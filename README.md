# dropwizard-togglz
Dropwizard Bundle for Feature Toggles using https://www.togglz.org/

## Example
Create enum implementing Feature interface

```java
public enum Feature implements org.togglz.core.Feature {

    FEATURE_ONE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
```

Define default config with reference to Feature enum.
```yaml
togglz:
  spec: "edu.thinktank.togglz.example.Feature"
  features:
    FEATURE_ONE: true
```

Start using Enum.
```java
if(Feature.FEATURE_ONE.isActive()) {
    ...
}
```

Enable/Disable feature at runtime via admin console
http://localhost:8080/admin/togglz/

## Todo
- [ ] persist toggles
- [ ] auth (basic)
