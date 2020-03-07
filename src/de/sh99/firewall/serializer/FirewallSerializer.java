package de.sh99.firewall.serializer;

import de.sh99.firewall.entity.FirewallEntry;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class FirewallSerializer
{
    public static String key2Hash(String str)
    {
        return str.replace("class ", "").replace(".", "_");
    }

    public static List<FirewallEntry> deserialize(HashMap<Integer, HashMap<String, Object>> entries)
    {
        List<FirewallEntry> deserialized = new ArrayList<>();


        for (HashMap<String, Object> entry:entries.values()){
            Plugin plugin = Bukkit.getPluginManager().getPlugin((String) entry.get("name"));

            if(null == plugin){
                continue;
            }

            FirewallEntry firewallEntry = new FirewallEntry(plugin, (boolean) entry.get("enabled"));
            deserialized.add(firewallEntry);
        }

        return deserialized;
    }

    public static HashMap<String, HashMap<String, Object>> serialize(List<FirewallEntry> entries)
    {
        HashMap<String, HashMap<String, Object>> serialized = new HashMap<>();

        for (FirewallEntry entry:entries){
            String hash = FirewallSerializer.key2Hash(entry.getPluginClass());

            HashMap<String, Object> hm = new HashMap<>();
            hm.put("enabled", entry.isEnabled());
            hm.put("name", entry.getName());
            hm.put("class", entry.getPluginClass());
            hm.put("priority", entry.getPriority());

            serialized.put(hash, hm);
        }

        return serialized;
    }
}
