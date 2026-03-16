package fr.democraft.rcs.core.config;

import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.DeclarativeYAML;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Comment;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Config;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Namespace;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Node;

@Namespace("rustyconnector-modules")
@Config("/smart/config.yml")
public class MainConfig {
    @Node(1)
    @Comment("Enable or disable debug mode for the Smart Module. When enabled, more detailed logs will be shown.")
    public boolean debug = true;

    @Node(2)
    @Comment("Enable or disable fancy logs for the Smart Module. When enabled, logs will include colors and formatting.")
    public boolean fancyLogs = true;

    @Node(3)
    @Comment("Smart trigger the server creation when the player count reaches the soft limit + this number.")
    public int playerCap = 0;

    public static MainConfig New() {
        return DeclarativeYAML.From(MainConfig.class);
    }
}
