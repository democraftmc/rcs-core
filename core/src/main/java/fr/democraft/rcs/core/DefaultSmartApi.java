package fr.democraft.rcs.core;

import fr.democraft.rcs.api.DeleteRequest;
import fr.democraft.rcs.api.OperationStatus;
import fr.democraft.rcs.api.ProviderClient;
import fr.democraft.rcs.api.ProviderRegistry;
import fr.democraft.rcs.api.ProvisionRequest;
import fr.democraft.rcs.api.ProvisionResult;
import fr.democraft.rcs.api.SmartApi;
import fr.democraft.rcs.api.SmartEventPublisher;

import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

public final class DefaultSmartApi implements SmartApi {
    private final ProviderRegistry providerRegistry;
    private final SmartEventPublisher eventPublisher;
    private final Executor executor;
    private final Duration timeout;

    public DefaultSmartApi(ProviderRegistry providerRegistry, SmartEventPublisher eventPublisher, Executor executor, Duration timeout) {
        this.providerRegistry = Objects.requireNonNull(providerRegistry, "providerRegistry");
        this.eventPublisher = Objects.requireNonNull(eventPublisher, "eventPublisher");
        this.executor = Objects.requireNonNull(executor, "executor");
        this.timeout = Objects.requireNonNull(timeout, "timeout");
    }

    @Override
    public ProviderRegistry providerRegistry() {
        return providerRegistry;
    }

    @Override
    public CompletableFuture<ProvisionResult> createServer(ProvisionRequest request) {
        Objects.requireNonNull(request, "request");
        eventPublisher.onCreateRequested(request);

        return CompletableFuture.supplyAsync(() -> providerRegistry.selectForCreate(request), executor)
                .thenCompose(optionalProvider -> optionalProvider
                        .map(provider -> executeCreate(provider, request))
                        .orElseGet(() -> CompletableFuture.completedFuture(
                                ProvisionResult.failure(OperationStatus.FAILURE, "none", "No provider can create this request")
                        )))
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionally(error -> ProvisionResult.failure(OperationStatus.RETRYABLE_FAILURE, "none", error.getMessage()))
                .whenComplete((result, ignored) -> eventPublisher.onCreateCompleted(request, result));
    }

    @Override
    public CompletableFuture<ProvisionResult> deleteServer(DeleteRequest request) {
        Objects.requireNonNull(request, "request");
        eventPublisher.onDeleteRequested(request);

        return CompletableFuture.supplyAsync(() -> providerRegistry.selectForDelete(request), executor)
                .thenCompose(optionalProvider -> optionalProvider
                        .map(provider -> executeDelete(provider, request))
                        .orElseGet(() -> CompletableFuture.completedFuture(
                                ProvisionResult.failure(OperationStatus.FAILURE, "none", "No provider can delete this request")
                        )))
                .orTimeout(timeout.toMillis(), TimeUnit.MILLISECONDS)
                .exceptionally(error -> ProvisionResult.failure(OperationStatus.RETRYABLE_FAILURE, "none", error.getMessage()))
                .whenComplete((result, ignored) -> eventPublisher.onDeleteCompleted(request, result));
    }

    private CompletableFuture<ProvisionResult> executeCreate(ProviderClient provider, ProvisionRequest request) {
        return provider.createServer(request).thenApply(result -> ensureProviderId(result, provider.id()));
    }

    private CompletableFuture<ProvisionResult> executeDelete(ProviderClient provider, DeleteRequest request) {
        return provider.deleteServer(request).thenApply(result -> ensureProviderId(result, provider.id()));
    }

    private ProvisionResult ensureProviderId(ProvisionResult result, String providerId) {
        if (result.providerId() != null && !result.providerId().isBlank()) {
            return result;
        }
        return new ProvisionResult(result.status(), providerId, result.externalServerId(), result.message());
    }
}

