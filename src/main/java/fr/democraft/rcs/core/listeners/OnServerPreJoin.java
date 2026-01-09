package fr.democraft.rcs.core.listeners;

import fr.democraft.rcs.core.SmartProvider;

import fr.democraft.rcs.api.events.CreatePhysicalServer;

import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.common.events.EventListener;
import group.aelysium.rustyconnector.proxy.events.ServerPreJoinEvent;
import group.aelysium.rustyconnector.proxy.family.Family;
import group.aelysium.rustyconnector.proxy.family.Server;

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

                Integer ram = 0;
                Optional<Integer> optionalRam = smartFamily.fetchMetadata("smart.ram");
                if (optionalRam.isPresent()) { ram = optionalRam.get(); }

                String id = selectProvider(smartFamily.id(), 10240);
                if (id == null) {
                    SmartProvider.logger.error("No providers want to created a " + smartFamily.displayName() + " server!");
                    return;
                }
                CreatePhysicalServer subEvent = new CreatePhysicalServer(id, smartFamily);
                boolean status = RC.EventManager().fireEvent(subEvent).get(10, TimeUnit.SECONDS);
                if (!status) {
                    SmartProvider.logger.error("No server has created a " + smartFamily.displayName() + " server!");
                }
            }
        }
    }
}
