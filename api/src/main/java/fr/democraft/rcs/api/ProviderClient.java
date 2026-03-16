package fr.democraft.rcs.api;

import java.util.concurrent.CompletableFuture;

public interface ProviderClient {
    String id();

    default int priority() {
        return 100;
    }

    boolean supportsCreate(ProvisionRequest request);

    boolean supportsDelete(DeleteRequest request);

    CompletableFuture<ProvisionResult> createServer(ProvisionRequest request);

    CompletableFuture<ProvisionResult> deleteServer(DeleteRequest request);
}

