package fr.democraft.rcs.core.utils;

import fr.democraft.rcs.core.SmartProvider;
import fr.democraft.rcs.core.config.Provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class ProviderUtils {
    private static final Map<String, AtomicInteger> currentProcesses = new ConcurrentHashMap<>();
    private static final Map<String, AtomicInteger> currentRamUsage = new ConcurrentHashMap<>();

    public static String selectProvider(String familyName, int ramAmount) {
        List<Provider> eligibleProviders = new ArrayList<>();

        for (Provider provider : SmartProvider.providerConfig.providers) {
            if (isProviderEligible(provider, familyName, ramAmount)) {
                eligibleProviders.add(provider);
            }
        }

        if (eligibleProviders.isEmpty()) {
            return null;
        }
        Provider selectedProvider = eligibleProviders.get(0);
        allocateResources(selectedProvider.id, ramAmount);

        return selectedProvider.id;
    }

    private static boolean isProviderEligible(Provider provider, String familyName, int ramAmount) {
        // Check included families
        if (provider.includedFamilies != null && !provider.includedFamilies.isEmpty()) {
            if (!provider.includedFamilies.contains(familyName)) {
                return false;
            }
        }

        // Check excluded families
        if (provider.excludedFamilies != null && !provider.excludedFamilies.isEmpty()) {
            if (provider.excludedFamilies.contains(familyName)) {
                return false;
            }
        }

        // Check process limit
        int currentProcessCount = getCurrentProcessCount(provider.id);
        if (provider.maxProcesses > 0 && currentProcessCount >= provider.maxProcesses) {
            return false;
        }

        // Check RAM availability
        int currentRam = getCurrentRamUsage(provider.id);
        if (provider.ramAmount > 0 && (currentRam + ramAmount) > provider.ramAmount) {
            return false;
        }

        return true;
    }

    private static void allocateResources(String providerId, int ramAmount) {
        currentProcesses.computeIfAbsent(providerId, k -> new AtomicInteger(0)).incrementAndGet();
        currentRamUsage.computeIfAbsent(providerId, k -> new AtomicInteger(0)).addAndGet(ramAmount);
    }

    public static void releaseResources(String providerId, int ramAmount) {
        currentProcesses.computeIfPresent(providerId, (k, v) -> {
            v.decrementAndGet();
            return v.get() > 0 ? v : null;
        });

        currentRamUsage.computeIfPresent(providerId, (k, v) -> {
            v.addAndGet(-ramAmount);
            return v.get() > 0 ? v : null;
        });
    }

    public static int getCurrentProcessCount(String providerId) {
        AtomicInteger count = currentProcesses.get(providerId);
        return count != null ? count.get() : 0;
    }

    public static int getCurrentRamUsage(String providerId) {
        AtomicInteger usage = currentRamUsage.get(providerId);
        return usage != null ? usage.get() : 0;
    }
}
