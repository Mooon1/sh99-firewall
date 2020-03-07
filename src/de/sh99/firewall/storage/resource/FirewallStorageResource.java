package de.sh99.firewall.storage.resource;

import sh99.persistence.storage.resource.YmlResource;

public class FirewallStorageResource implements YmlResource
{
    @Override
    public String path() {
        return "/firewall.yml";
    }
}
