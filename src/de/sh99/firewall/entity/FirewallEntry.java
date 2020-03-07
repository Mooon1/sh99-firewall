package de.sh99.firewall.entity;

import org.bukkit.plugin.Plugin;

public class FirewallEntry
{
    private Plugin plugin;

    private boolean enabled = true;

    private int priority = 0;

    public FirewallEntry(Plugin plugin)
    {
        this(plugin, true);
    }

    public FirewallEntry(Plugin plugin, boolean enabled)
    {
        this.plugin = plugin;
        this.enabled = enabled;
        this.priority = 0;
    }

    public String getName() {
        return this.plugin.getName();
    }

    public String getPluginClass(){
        return this.plugin.getClass().toString().replace("class ", "");
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Plugin getPlugin() {
        return plugin;
    }
}
