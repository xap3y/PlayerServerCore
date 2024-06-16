package eu.xap3y.playerservercore.models;

import java.io.File;

public class PluginModel {
    private final String name;
    private final String version;
    private final String description;
    private final boolean enabled;
    private final File file;

    public PluginModel(String name, String version, String description, boolean enabled, File file) {
        this.name = name;
        this.version = version;
        this.description = description;
        this.enabled = enabled;
        this.file = file;
    }

    public boolean isFromRepository() {
        return this.file.getPath().startsWith("/plugins-repo");
    }

    public String getName() {
        return this.name;
    }

    public String getVersion() {
        return this.version;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isEnabled() {
        return this.enabled;
    }

    public File getFile() {
        return this.file;
    }
}
