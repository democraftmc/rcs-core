package fr.democraft.rcs.core.config;

import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.DeclarativeYAML;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Config;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Namespace;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Node;

import java.util.Collections;
import java.util.List;

@Namespace("rustyconnector-modules")
@Config("/smart/providers.yml")
public class ProviderConfig {
    @Node
    public final List<Provider> providers = Collections.singletonList(new Provider());

    public static ProviderConfig New() {
        return DeclarativeYAML.From(ProviderConfig.class);
    }
}
