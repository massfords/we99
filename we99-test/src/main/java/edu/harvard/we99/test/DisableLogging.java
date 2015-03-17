package edu.harvard.we99.test;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Disables the logging for tests that generate stacktraces since it's
 * disconcerting to see stacktraces during the build.
 * @author mford
 */
public class DisableLogging {

    private static final Map<String,Level> restore = new HashMap<>();

    public static synchronized void turnOff(Class...clazzes) {
        for(Class c : clazzes) {
            turnOff(c.getName());
        }
    }

    public static synchronized void turnOff(String...category) {
        for(String s : category) {
            if (restore.containsKey(s)) {
                return;
            }
            Logger logger = Logger.getLogger(s);
            restore.put(s, logger.getLevel());
            logger.setLevel(Level.OFF);
        }
    }

    public static synchronized  void restoreAll() {
        List<String> keys = new ArrayList<>(restore.keySet());
        restore(keys.toArray(new String[keys.size()]));
    }

    public static synchronized void restore(String...category) {
        for(String s : category) {
            Level level = restore.get(s);
            Logger.getLogger(s).setLevel(level);
            restore.remove(s);
        }
    }
}
