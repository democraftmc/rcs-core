package fr.democraft.rcs.api;

import java.util.Map;

public record ProvisionRequest(
        String preferredProviderId,
        String family,
        String template,
        int memoryMb,
        Map<String, String> metadata
) {
    public ProvisionRequest {
        if (family == null || family.isBlank()) {
            throw new IllegalArgumentException("family is required");
        }
        if (template == null || template.isBlank()) {
            throw new IllegalArgumentException("template is required");
        }
        if (memoryMb <= 0) {
            throw new IllegalArgumentException("memoryMb must be > 0");
        }
        metadata = metadata == null ? Map.of() : Map.copyOf(metadata);
    }
}

