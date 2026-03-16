package fr.democraft.rcs.core.listeners;

import fr.democraft.rcs.api.ProvisionRequest;
import fr.democraft.rcs.api.ProvisionResult;
import fr.democraft.rcs.core.SmartProvider;
import group.aelysium.rustyconnector.common.events.EventListener;
import group.aelysium.rustyconnector.proxy.events.ServerPreJoinEvent;
import group.aelysium.rustyconnector.proxy.family.Family;
import group.aelysium.rustyconnector.proxy.family.Server;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static fr.democraft.rcs.core.utils.ProviderUtils.selectProvider;

public class OnServerPreJoin {
    @EventListener
    public static void handler(ServerPreJoinEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        Server s = event.server;
        if (s.players() == (s.softPlayerCap() + SmartProvider.config.playerCap)) {
            Optional<Family> optionalSmartFamily = s.family();
            if (optionalSmartFamily.isPresent()) {
                Family smartFamily = optionalSmartFamily.get();

                int ram = 1024;
                Optional<Integer> optionalRam = smartFamily.fetchMetadata("smart.ram");
                if (optionalRam.isPresent()) {
                    ram = optionalRam.get();
                }

                String id = selectProvider(smartFamily.id(), ram);
                if (id == null) {
                    SmartProvider.logger.error("No providers want to create a " + smartFamily.displayName() + " server!");
                    return;
                }

                ProvisionRequest request = new ProvisionRequest(
                        id,
                        smartFamily.id(),
                        "paper",
                        ram,
                        Map.of("familyDisplayName", String.valueOf(smartFamily.displayName()))
                );

                ProvisionResult result = SmartProvider.api.createServer(request).get(10, TimeUnit.SECONDS);
                if (!result.isSuccess()) {
                    SmartProvider.logger.error("Provider failed to create a " + smartFamily.displayName() + " server: " + result.message());
                }
            }
        }
    }
}
