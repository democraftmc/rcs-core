package fr.democraft.rcs.core;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;

import dev.faststats.core.ErrorTracker;
import dev.faststats.core.Metrics;
import dev.faststats.velocity.VelocityMetrics;

import fr.democraft.rcs.api.SmartApi;
import fr.democraft.rcs.api.SmartApiAccess;
import fr.democraft.rcs.core.config.MainConfig;
import fr.democraft.rcs.core.config.Provider;
import fr.democraft.rcs.core.config.ProviderConfig;
import fr.democraft.rcs.core.listeners.OnServerLeave;
import fr.democraft.rcs.core.listeners.OnServerPreJoin;
import fr.democraft.rcs.core.loggers.ClassicLogger;
import fr.democraft.rcs.core.loggers.FancyLogger;
import fr.democraft.rcs.core.loggers.SmartLogger;

import group.aelysium.rustyconnector.RC;
import group.aelysium.rustyconnector.common.events.EventManager;
import group.aelysium.rustyconnector.proxy.ProxyKernel;

import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Plugin(id = "rcm-smart", name = "Rusty Smart", version = "0.1.0-SNAPSHOT",
        url = "https://smart.democraft.fr", description = "Create & Delete server just like they where legos.", authors = {"Funasitien"})
public class SmartProvider {

    private final ProxyServer server;
    public static boolean DEBUG = false;
    public static final boolean useFancyLogger = true;
    public static SmartLogger logger;
    public static MainConfig config;
    public static ProviderConfig providerConfig;
    public static ProxyKernel kernel;
    public static SmartApi api;
    public static ExecutorService executor;

    public static final ErrorTracker ERROR_TRACKER = ErrorTracker.contextAware();
    private final VelocityMetrics.Factory metricsFactory;
    private @Nullable Metrics metrics = null;

    @Inject
    public SmartProvider(ProxyServer server, Logger logger, final VelocityMetrics.Factory factory) {
        this.server = server;
        this.metricsFactory = factory;
        SmartProvider.logger = useFancyLogger ? new FancyLogger() : new ClassicLogger();
        SmartProvider.logger.log("Rusty's Smart Provider is booting up, please wait...");

        kernel = RC.P.Kernel();

        config = MainConfig.New();
        providerConfig = ProviderConfig.New();
        DEBUG = config.debug;

        executor = Executors.newFixedThreadPool(4);
        api = new DefaultSmartApi(new Registry(), new EventPublisher(), executor, Duration.ofSeconds(10));
        SmartApiAccess.set(api);

        kernel.<EventManager>fetchModule("EventManager").onStart(m -> {
            m.listen(OnServerPreJoin.class);
            m.listen(OnServerLeave.class);
        });

        for (Provider provider : providerConfig.providers) {
            logger.debug("Loaded provider: {}", provider.id);
        }

        logger.debug("Event registered successfully");
        logger.info("Module loaded successfully");
    }

    @Subscribe
    public void onProxyInitialize(final ProxyInitializeEvent event) {
        this.metrics = metricsFactory
                .errorTracker(ERROR_TRACKER)
                .token("17f77f0d0e81f96220b454e0595df8ca")
                .create(this);
    }

    @Subscribe
    public void onProxyStop(final ProxyShutdownEvent event) {
        if (metrics != null) metrics.shutdown();
    }
}