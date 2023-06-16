package net.njsharpe.developmentutility.configuration;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public abstract class ConfigurationTemplate {

    private static final Logger LOG = Bukkit.getLogger();

    private final String name;

    private final File file;
    private FileConfiguration config;

    public ConfigurationTemplate(Plugin plugin, String name) {
        this.name = name;
        if(!plugin.getDataFolder().exists()) {
            boolean mkdirs = plugin.getDataFolder().mkdirs();
            if(!mkdirs) {
                throw new IllegalArgumentException("Could not create required directories for configuration file!");
            }
        }
        this.file = new File(plugin.getDataFolder(), name);
        if(!this.file.exists()) {
            try {
                boolean createFile = this.file.createNewFile();
                if(!createFile) {
                    LOG.warning("Configuration file '%s' already exists, skipping.".formatted(name));
                }
            } catch (IOException ex) {
                throw new RuntimeException("Could not create configuration file: '%s'".formatted(name), ex);
            }
        }
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.loadDefaultConfig();
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        if(this.config == null) this.reload();
        return this.config;
    }

    public abstract void addDefaults();

    private void loadDefaultConfig() {
        this.addDefaults();
        this.config.options().copyDefaults(true);
        this.save();
    }

    public void save() {
        try {
            this.config.save(this.file);
        } catch (IOException ex) {
            throw new RuntimeException(String.format("could not save configuration: '%s'", this.name), ex);
        }
    }

    public void reload() {
        this.config = YamlConfiguration.loadConfiguration(this.file);
        this.save();
    }

}
