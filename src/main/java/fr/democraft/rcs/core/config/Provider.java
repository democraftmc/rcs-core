package fr.democraft.rcs.core.config;

import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.annotations.Node;
import group.aelysium.rustyconnector.shaded.group.aelysium.declarative_yaml.lib.Serializable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class Provider extends Serializable {
    @Node
    public String id;

    @Node(1)
    @Nullable
    public List<String> includedFamilies = null;

    @Node(2)
    @Nullable
    public List<String> excludedFamilies = null;

    @Node(3)
    public int maxProcesses = 0;

    @Node(4)
    public int ramAmount = 0;

    public Provider(String id, @Nullable List<String> includedFamilies, @Nullable List<String> excludedFamilies, int maxProcesses, int ramAmount) {
        super();
        this.id = id;
        this.includedFamilies = includedFamilies;
        this.excludedFamilies = excludedFamilies;
        this.maxProcesses = maxProcesses;
        this.ramAmount = ramAmount;
    }
    public Provider() {
        this("pterodactyl", List.of("lobby", "bedwars"), List.of("auth"), 5, 10240);
    }
}