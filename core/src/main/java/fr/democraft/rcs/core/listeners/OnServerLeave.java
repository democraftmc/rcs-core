package fr.democraft.rcs.core.listeners;

import fr.democraft.rcs.api.DeleteRequest;
import fr.democraft.rcs.api.ProvisionResult;
import fr.democraft.rcs.core.SmartProvider;
import group.aelysium.rustyconnector.common.events.EventListener;
import group.aelysium.rustyconnector.proxy.events.ServerLeaveEvent;
import group.aelysium.rustyconnector.proxy.family.Family;
import group.aelysium.rustyconnector.proxy.family.Server;

import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class OnServerLeave {
    @EventListener
    public static void handler(ServerLeaveEvent event) throws ExecutionException, InterruptedException, TimeoutException {
        Server s = event.server;
        if (s.players() == (s.softPlayerCap() + SmartProvider.config.playerCap)) {
            Optional<Family> optionalSmartFamily = s.family();
            if (optionalSmartFamily.isPresent()) {
                Family smartFamily = optionalSmartFamily.get();

                DeleteRequest request = new DeleteRequest(null, smartFamily.id(), null);
                ProvisionResult result = SmartProvider.api.deleteServer(request).get(10, TimeUnit.SECONDS);
                if (!result.isSuccess()) {
                    SmartProvider.logger.error("No provider deleted a " + smartFamily.displayName() + " server: " + result.message());
                }
            }
        }
    }
}
