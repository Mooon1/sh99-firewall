package de.sh99.firewall.configuration;

import de.sh99.firewall.Firewall;
import org.bukkit.plugin.Plugin;
import sh99.persistence.configuration.AbstractConfiguration;
import sh99.persistence.configuration.Configuration;
import sh99.persistence.storage.resource.YmlResource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsConfiguration extends AbstractConfiguration implements Configuration
{
    private boolean enabled = true;

    private boolean forcemodeEnabled = false;

    private boolean pluginEnabledWhitelist = false;
    private boolean pluginEnabledBlacklist = false;
    private List<String> pluginWhitelist = new ArrayList<>();
    private List<String> pluginBlacklist = new ArrayList<>();

    public SettingsConfiguration(YmlResource resource, Plugin plugin) {
        super(resource, plugin);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isForcemodeEnabled() {
        return forcemodeEnabled;
    }

    public void setForcemodeEnabled(boolean forcemodeEnabled) {
        this.forcemodeEnabled = forcemodeEnabled;
    }

    public boolean isPluginEnabledWhitelist() {
        return pluginEnabledWhitelist;
    }

    public void setPluginEnabledWhitelist(boolean pluginEnabledWhitelist) {
        this.pluginEnabledWhitelist = pluginEnabledWhitelist;
    }

    public boolean isPluginEnabledBlacklist() {
        return pluginEnabledBlacklist;
    }

    public void setPluginEnabledBlacklist(boolean pluginEnabledBlacklist) {
        this.pluginEnabledBlacklist = pluginEnabledBlacklist;
    }

    public List<String> getPluginWhitelist() {
        return pluginWhitelist;
    }

    public void setPluginWhitelist(List<String> pluginWhitelist) {
        this.pluginWhitelist = pluginWhitelist;
    }

    public List<String> getPluginBlacklist() {
        return pluginBlacklist;
    }

    public void setPluginBlacklist(List<String> pluginBlacklist) {
        this.pluginBlacklist = pluginBlacklist;
    }
}
