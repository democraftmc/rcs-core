package fr.democraft.rcs.api;

import java.util.Optional;

public final class SmartApiAccess {
    private static volatile SmartApi api;

    private SmartApiAccess() {
    }

    public static void set(SmartApi instance) {
        api = instance;
    }

    public static Optional<SmartApi> get() {
        return Optional.ofNullable(api);
    }

    public static SmartApi require() {
        if (api == null) {
            throw new IllegalStateException("Smart API is not available yet");
        }
        return api;
    }
}

