package de.sh99.firewall.configuration.resource;

import sh99.persistence.storage.resource.YmlResource;

public class SettingsConfigurationResource implements YmlResource
{
    @Override
    public String path() {
        return "/settings.yml";
    }
}
