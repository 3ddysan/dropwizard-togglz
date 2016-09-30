# dropwizard-togglz
Dropwizard 1.0.2 Bundle for Feature Toggles using https://www.togglz.org/

## Todo
- [x] ~~persist toggles~~ StateRepository can be implemented via AbstractFeatureToggleBundle#getStateRepository
- [ ] auth (basic)

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

## Testing
FeatureToggleTestSupport class contains convenience methods for testing.
```java
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
}
```


## Persistence
Example with [MongoDB Driver](https://mongodb.github.io/mongo-java-driver/3.3/).
```java
@Override
public void initialize(final Bootstrap<Config> bootstrap) {
    bootstrap.addBundle(new AbstractFeatureToggleBundle<Config>() {
 
        public FeatureToggleConfig getBundleConfiguration(final Config configuration) {
            return configuration.toggleConfig;
        }
        
        public StateRepository getStateRepository() {
            return new StateRepository() {
                MongoCollection<Document> collection;
                StateRepository() {
                    MongoClient mongoClient = new MongoClient();
                    MongoDatabase db = mongoClient.getDatabase("test");
                    collection = database.getCollection("features");
                }
                public FeatureState getFeatureState(Feature feature) {
                    Boolean isEnabled = collection.find(eq("name", feature.name())).first().getBoolean("enabled");
                    return new FeatureState(feature, isEnabled);
                }
                public void setFeatureState(FeatureState state) {
                    collection.insertOne(new Document("name", state.getFeature().name()).append("enabled", state.isEnabled()))
                }
            }
        }
        
    });
}
```
