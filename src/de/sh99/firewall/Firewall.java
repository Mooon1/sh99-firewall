package de.sh99.firewall;

import de.sh99.firewall.configuration.SettingsConfiguration;
import de.sh99.firewall.configuration.resource.SettingsConfigurationResource;
import de.sh99.firewall.entity.FirewallEntry;
import de.sh99.firewall.storage.FirewallStorage;
import de.sh99.firewall.storage.resource.FirewallStorageResource;
import de.sh99.firewall.utils.FirewallUpdater;
import org.bukkit.plugin.Plugin;
import sh99.persistence.PersistenceJavaPlugin;
import sh99.persistence.PersistencePlugin;
import sh99.persistence.VersionedPlugin;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Firewall extends PersistenceJavaPlugin implements VersionedPlugin, PersistencePlugin
{
    private SettingsConfiguration firewallConfig;

    private FirewallStorage firewallStorage;

    private List<FirewallEntry> entries;

    @Override
    public boolean needSql() {
        return false;
    }

    @Override
    public void configDeclarations()
    {
        this.addConfig(new SettingsConfiguration(new SettingsConfigurationResource(), this));
    }

    @Override
    public PersistenceJavaPlugin definePlugin() {
        return this;
    }

    @Override
    public void versionDeclarations() {
        this.addSupportedVersion("1.15");
        this.addSupportedVersion("1.14");
        this.addSupportedVersion("1.13");
        this.addSupportedVersion("1.12");
        this.addSupportedVersion("1.11");
        this.addSupportedVersion("1.10");
        this.addSupportedVersion("1.9");
        this.addSupportedVersion("1.8");
        this.addSupportedVersion("1.7");
    }

    @Override
    public void onUnversionedEnable()
    {
        this.firewallStorage = new FirewallStorage(new FirewallStorageResource(), this);
        this.firewallConfig = (SettingsConfiguration) this.getConfig(SettingsConfiguration.class);

        if(!this.firewallConfig.isEnabled()){
            System.out.println("\033[31mFirewall has been disabled by configuration.\033[0m");
            return;
        }

        this.entries = FirewallUpdater.update(this);

        System.out.println("\033[0m#################################################################");
        System.out.println("\033[0m#####################       FIREWALL        #####################");
        System.out.println("\033[0m#################################################################");


        for (FirewallEntry entry:entries){
            System.out.println("\033[0mPlugin(\033[33m" + entry.getName() + "\033[0m) Priority(\033[33m" + entry.getPriority() + "\033[0m) State(" + (entry.isEnabled() ? "\033[32mEnabled" : "\033[31mDisabled") + "\033[0m)\033[0m");
        }

        System.out.println("\033[0m#################################################################");

        System.out.println("\033[31mFIREWALL\033[0m\033[0m Disable all plugins.\033[0m");

        Map<Class<?>, Plugin> instances = new HashMap<>();

        for (Plugin p:this.getServer().getPluginManager().getPlugins()){
            if(p instanceof Firewall){
                continue;
            }

            instances.put(p.getClass(), p);
            p.getServer().getPluginManager().disablePlugin(p);
            System.out.println("\033[31mFIREWALL\033[0m \033[31m" + p.getName() + "\033[0m got disabled.\033[0m");
        }

        for (FirewallEntry entry:entries){
            if(!instances.containsKey(entry.getPlugin().getClass())){
                continue;
            }

            if(!entry.isEnabled()){
                System.out.println("\033[33mFIREWALL\033[0m \033[33m" + entry.getName() + "\033[0m is disabled by configuration.\033[0m");
                continue;
            }

            instances.get(entry.getPlugin().getClass()).getServer().getPluginManager().enablePlugin(entry.getPlugin());
            System.out.println("\033[32mFIREWALL\033[0m \033[32m" + entry.getName() + "\033[0m has been loaded.\033[0m");
        }
    }

    @Override
    public void onVersionedEnable()
    {

    }

    @Override
    public boolean isPersistence() {
        return false;
    }

    public SettingsConfiguration getFirewallConfig() {
        return firewallConfig;
    }

    public FirewallStorage getFirewallStorage() {
        return firewallStorage;
    }
}
