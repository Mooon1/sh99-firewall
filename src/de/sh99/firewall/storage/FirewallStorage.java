package de.sh99.firewall.storage;

import de.sh99.firewall.Firewall;
import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.MemorySection;
import org.bukkit.plugin.Plugin;
import sh99.persistence.storage.YmlStorage;
import sh99.persistence.storage.resource.YmlResource;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirewallStorage extends YmlStorage
{
    public FirewallStorage(YmlResource resource, Plugin plugin) {
        super(resource, plugin);
    }

    public MemorySection getFirewall()
    {
        try {
            return (MemorySection) this.get(this.resource.getFullPath(this.plugin), "firewall");
        } catch (IOException | InvalidConfigurationException e) {
            if(((Firewall) plugin).isStacktrace()){
                e.printStackTrace();
            }
        }
        return null;
    }

    public MemorySection getFirewallEntry(String hashCode)
    {
        try {
            return (MemorySection) ((MemorySection) this.get(this.resource.getFullPath(this.plugin), "firewall")).get(hashCode);
        } catch (IOException | InvalidConfigurationException e) {
            if(((Firewall) plugin).isStacktrace()){
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean isPersisted()
    {
        return null != getFirewall();
    }

    public HashMap<String, MemorySection> getEntries()
    {
        HashMap<String, MemorySection> l = new HashMap<>();

        for (String hashCode:this.getFirewall().getKeys(false)){
            l.put(hashCode, this.getFirewallEntry(hashCode));
        }

        return l;
    }

    public void setEntries(HashMap<String, HashMap<String, Object>> entries)
    {
        try {
            if(this.delete(new File(this.resource.getFullPath(this.plugin)))){
                this.create(new File(this.resource.getFullPath(this.plugin)));
            }
            this.set(this.resource.getFullPath(this.plugin), "firewall", entries);
        } catch (IOException | InvalidConfigurationException e) {
            if(((Firewall) plugin).isStacktrace()){
                e.printStackTrace();
            }
        }
    }
}
