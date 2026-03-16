package fr.democraft.rcs.api;

public record ProvisionResult(
        OperationStatus status,
        String providerId,
        String externalServerId,
        String message
) {
    public static ProvisionResult success(String providerId, String externalServerId, String message) {
        return new ProvisionResult(OperationStatus.SUCCESS, providerId, externalServerId, message);
    }

    public static ProvisionResult failure(OperationStatus status, String providerId, String message) {
        if (status == OperationStatus.SUCCESS) {
            throw new IllegalArgumentException("Use success() for SUCCESS status");
        }
        return new ProvisionResult(status, providerId, null, message);
    }

    public boolean isSuccess() {
        return status == OperationStatus.SUCCESS;
    }
}

