package edu.harvard.we99.services.storage;

import java.io.IOException;

/**
 * @author mford
 */
public interface AppSettingsStorage {
    <T> T load(Class<T> setting);
    <T> void store(Class<T> setting, T t) throws IOException;
}
