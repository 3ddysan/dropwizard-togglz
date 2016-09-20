# dropwizard-togglz
Dropwizard Bundle for Feature Toggles using https://www.togglz.org/

## Example
Create Enum implementing Feature interface

```java
public enum Feature implements org.togglz.core.Feature {

    FEATURE_ONE;

    public boolean isActive() {
        return FeatureContext.getFeatureManager().isActive(this);
    }
}
```

Define Default Config with reference to Feature enum
```yaml
togglz:
  spec: "edu.thinktank.togglz.example.Feature"
  features:
    FEATURE_ONE: true
```

Start using Enum
```java
if(Feature.FEATURE_ONE.isActive()) {
    ...
}
```

## Todo
- [ ] persist toggles
