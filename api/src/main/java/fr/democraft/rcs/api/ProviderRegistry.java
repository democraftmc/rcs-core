package fr.democraft.rcs.api;

import java.util.Collection;
import java.util.Optional;

public interface ProviderRegistry {
    void register(ProviderClient provider);

    void unregister(String providerId);

    Optional<ProviderClient> findById(String providerId);

    Optional<ProviderClient> selectForCreate(ProvisionRequest request);

    Optional<ProviderClient> selectForDelete(DeleteRequest request);

    Collection<ProviderClient> all();
}

