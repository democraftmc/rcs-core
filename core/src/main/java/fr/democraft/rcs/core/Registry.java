package fr.democraft.rcs.core;

import fr.democraft.rcs.api.DeleteRequest;
import fr.democraft.rcs.api.ProviderClient;
import fr.democraft.rcs.api.ProvisionRequest;
import fr.democraft.rcs.api.ProviderRegistry;

import java.util.Collection;
import java.util.Comparator;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public final class Registry implements  ProviderRegistry{
    private final Map<String, ProviderClient> providers = new ConcurrentHashMap<>();

    @Override
    public void register(ProviderClient provider) {
        ProviderClient validated = Objects.requireNonNull(provider, "provider");
        providers.put(normalize(validated.id()), validated);
    }

    @Override
    public void unregister(String providerId) {
        if (providerId == null || providerId.isBlank()) {
            return;
        }
        providers.remove(normalize(providerId));
    }

    @Override
    public Optional<ProviderClient> findById(String providerId) {
        if (providerId == null || providerId.isBlank()) {
            return Optional.empty();
        }
        return Optional.ofNullable(providers.get(normalize(providerId)));
    }

    @Override
    public Optional<ProviderClient> selectForCreate(ProvisionRequest request) {
        if (request.preferredProviderId() != null && !request.preferredProviderId().isBlank()) {
            return findById(request.preferredProviderId()).filter(provider -> provider.supportsCreate(request));
        }

        return providers.values().stream()
                .filter(provider -> provider.supportsCreate(request))
                .min(Comparator.comparingInt(ProviderClient::priority));
    }

    @Override
    public Optional<ProviderClient> selectForDelete(DeleteRequest request) {
        if (request.preferredProviderId() != null && !request.preferredProviderId().isBlank()) {
            return findById(request.preferredProviderId()).filter(provider -> provider.supportsDelete(request));
        }

        return providers.values().stream()
                .filter(provider -> provider.supportsDelete(request))
                .min(Comparator.comparingInt(ProviderClient::priority));
    }

    @Override
    public Collection<ProviderClient> all() {
        return providers.values();
    }

    private static String normalize(String value) {
        return value.toLowerCase().trim();
    }
}

