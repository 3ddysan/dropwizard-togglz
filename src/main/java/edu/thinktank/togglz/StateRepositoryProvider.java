package edu.thinktank.togglz;

import io.dropwizard.Configuration;
import io.dropwizard.setup.Environment;
import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.mem.InMemoryStateRepository;

public interface StateRepositoryProvider<T extends Configuration>{
    default StateRepository getStateRepository(final T configuration, final Environment environment) {
        return new InMemoryStateRepository();
    }
}
