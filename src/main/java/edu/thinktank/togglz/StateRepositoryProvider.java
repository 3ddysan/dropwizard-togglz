package edu.thinktank.togglz;

import org.togglz.core.repository.StateRepository;
import org.togglz.core.repository.mem.InMemoryStateRepository;

public interface StateRepositoryProvider {
    default StateRepository getStateRepository() {
        return new InMemoryStateRepository();
    }
}
