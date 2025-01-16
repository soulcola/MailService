package ru.javaops.masterjava.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import lombok.extern.slf4j.Slf4j;

import java.io.File;

@Slf4j
public class Configs {

    public static Config getConfig(String resource) {
        String profile = System.getProperty("profile", System.getenv("profile"));
        String[] fileNameSplit = resource.split("\\.");
        if (profile != null) {
            resource = profile + "-" + profile;
            if (fileNameSplit.length > 1) {
                resource = fileNameSplit[0] + "-" + profile + "." + fileNameSplit[1];
            }
            log.info("Profile: {}. Loading config file: {}", profile, resource);
        } else {
            log.warn("Profile not set. Loading default config file");
        }
        return ConfigFactory.parseResources(resource).resolve();
    }

    public static Config getConfig(String resource, String domain) {
        return getConfig(resource).getConfig(domain);
    }

    public static File getConfigFile(String path) {
        return new File(AppConfig.APP_CONFIG.getString("configDir"), path);
    }

    private static class AppConfig {
        private static final Config APP_CONFIG = getConfig("app.conf", "app");
    }
}
