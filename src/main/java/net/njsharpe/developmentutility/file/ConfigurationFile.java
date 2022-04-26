package net.njsharpe.developmentutility.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class ConfigurationFile {

    private final Plugin plugin;
    private final String name;

    private final File file;
    private FileConfiguration config;

    public ConfigurationFile(Plugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        if(!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }
        this.file = new File(plugin.getDataFolder(), name);
        if(!this.file.exists()) {
            try {
                this.file.createNewFile();
            } catch (IOException ex) {
                throw new RuntimeException(String.format("could not create configuration file: '%s'", name), ex);
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

    public void loadDefaultConfig() {
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
