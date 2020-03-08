package de.sh99.firewall.utils;

import de.sh99.firewall.Firewall;
import de.sh99.firewall.entity.FirewallEntry;
import de.sh99.firewall.serializer.FirewallSerializer;
import org.bukkit.Bukkit;
import org.bukkit.block.data.type.Fire;
import org.bukkit.configuration.MemorySection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FirewallUpdater
{
    public static List<FirewallEntry> update(Firewall plugin)
    {
        Plugin[] loadedPlugins = Bukkit.getPluginManager().getPlugins();

        List<FirewallEntry> entries = new ArrayList<>();
        if(!plugin.getFirewallStorage().isPersisted()){
            System.out.println("\033[32mFirewall loaded from persistence.\033[0m");
            for (Plugin loadedPlugin:loadedPlugins){
                if(loadedPlugin instanceof Firewall){
                    continue;
                }
                entries.add(new FirewallEntry(loadedPlugin, !plugin.getFirewallConfig().isForcemodeEnabled()));
            }
        }else {
            HashMap<String, MemorySection> configEntries = plugin.getFirewallStorage().getEntries();
            List<FirewallEntry> entriesCopy = new ArrayList<>();
            List<FirewallEntry> backlog = new ArrayList<>();
            for (Plugin loadedPlugin:loadedPlugins){
                if(loadedPlugin instanceof Firewall){
                    continue;
                }
                String hash = FirewallSerializer.key2Hash(loadedPlugin.getClass().toString());

                if(!configEntries.containsKey(hash)){
                    entriesCopy.add(new FirewallEntry(loadedPlugin, !plugin.getFirewallConfig().isForcemodeEnabled(), entries.size()));
                    continue;
                }

                FirewallEntry fwe = new FirewallEntry(loadedPlugin, (boolean) configEntries.get(hash).get("enabled"));
                fwe.setPriority((int) configEntries.get(hash).get("priority"));

                if(!fwe.isEnabled()){
                    backlog.add(fwe);
                    continue;
                }

                entriesCopy.add(fwe);
            }
            entries = entriesCopy;
            entries = sortPriority(entries);
            entries.addAll(backlog);
            System.out.println("\033[32mFirewall loaded from persistence.\033[0m");
        }

        plugin.getFirewallStorage().setEntries(FirewallSerializer.serialize(entries));
        return entries;
    }

    private static List<FirewallEntry> sortPriority(List<FirewallEntry> unsorted)
    {
        System.out.println("\033[32mFIREWALL\033[0m\033[0m Sorting plugins by priorities, please wait...\033[0m");
        List<FirewallEntry> sorted = new ArrayList<>();

        int priority = 0;
        int affectedEntry = 0;
        int entriesSize = unsorted.size();

        List<Integer> affectedCache = new ArrayList<>();

        while (unsorted.size() > sorted.size()){
            if(affectedEntry == entriesSize){
                affectedEntry = 0;
                ++priority;
                System.out.println("\033[33mFIREWALL\033[0m Priority sort reset.\033[0m");
                continue;
            }
            FirewallEntry entry = unsorted.get(affectedEntry);

            if(affectedCache.contains(affectedEntry)){
                ++affectedEntry;
                System.out.println("\033[33mFIREWALL\033[0m Entry \033[33m" + entry.getName() + "\033[0m already exist.\033[0m");
                continue;
            }


            if(priority != entry.getPriority()){
                System.out.println("\033[33mFIREWALL\033[0m Priority does not match for entry \033[33m" + entry.getName() + "\033[0m. Expected: \033[32m" + priority + "\033[0m but got \033[33m" + entry.getPriority() + "\033[0m");
                ++affectedEntry;
                continue;
            }

            if(!entry.isEnabled()){
                System.out.println("\033[33mFIREWALL\033[0m Entry \033[33m" + entry.getName() + "\033[0m is disabled.\033[0m");
                ++affectedEntry;
                continue;
            }

            System.out.println("\033[32mFIREWALL\033[0m Entry \033[32m" + entry.getName() + "\033[0m sorted.\033[0m");
            affectedCache.add(affectedEntry);
            sorted.add(entry);
            ++affectedEntry;
        }

        return sorted;
    }
}
