package fr.democraft.rcs.api;

public record DeleteRequest(
        String preferredProviderId,
        String family,
        String externalServerId
) {
    public DeleteRequest {
        if ((family == null || family.isBlank()) && (externalServerId == null || externalServerId.isBlank())) {
            throw new IllegalArgumentException("family or externalServerId is required");
        }
    }
}

