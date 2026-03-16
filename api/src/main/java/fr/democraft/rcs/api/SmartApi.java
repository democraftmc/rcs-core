package fr.democraft.rcs.api;

import java.util.concurrent.CompletableFuture;

public interface SmartApi {
    ProviderRegistry providerRegistry();

    CompletableFuture<ProvisionResult> createServer(ProvisionRequest request);

    CompletableFuture<ProvisionResult> deleteServer(DeleteRequest request);
}

