package org.nunnerycode.facecore.configuration;

import org.nunnerycode.kern.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class MasterConfiguration {

    private Map<String, Object> settingMap;

    public MasterConfiguration() {
        settingMap = new ConcurrentHashMap<>();
    }

    public static MasterConfiguration loadFromFiles(SmartYamlConfiguration... yamlConfigurations) {
        MasterConfiguration masterConfiguration = new MasterConfiguration();
        masterConfiguration.load(yamlConfigurations);
        return masterConfiguration;
    }

    public Map<String, Object> getSettingMap() {
        return new HashMap<>(settingMap);
    }

    public Object get(String key, Object fallback) {
        return settingMap == null || !settingMap.containsKey(key) ? fallback : settingMap.get(key);
    }

    public void set(String key, Object object) {
        if (settingMap == null) {
            return;
        }
        settingMap.put(key, object);
    }

    public String getString(String key, String fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (val instanceof String) {
            return (String) val;
        }
        return String.valueOf(val);
    }

    public String getString(String key) {
        return getString(key, "");
    }

    public List<String> getStringList(String key, List<String> fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (!(val instanceof List)) {
            return fallback;
        }
        List<?> valList = (List) val;
        List<String> ret = new ArrayList<>();
        for (Object o : valList) {
            ret.add(String.valueOf(o));
        }
        return ret;
    }

    public List<String> getStringList(String key) {
        return getStringList(key, new ArrayList<String>());
    }

    public boolean getBoolean(String key, boolean fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (val instanceof Boolean) {
            return (Boolean) val;
        }
        if (val instanceof String) {
            return Boolean.parseBoolean((String) val);
        }
        return fallback;
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public int getInt(String key, int fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (val instanceof Integer) {
            return (Integer) val;
        }
        if (val instanceof String) {
            return NumberUtils.toInt((String) val, fallback);
        }
        return fallback;
    }

    public int getInt(String key) {
        return getInt(key, 0);
    }

    public long getLong(String key, long fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (val instanceof Long) {
            return (Long) val;
        }
        if (val instanceof String) {
            return NumberUtils.toLong((String) val, fallback);
        }
        return fallback;
    }

    public long getLong(String key) {
        return getLong(key, 0);
    }

    public double getDouble(String key, double fallback) {
        if (settingMap == null || !settingMap.containsKey(key)) {
            return fallback;
        }
        Object val = settingMap.get(key);
        if (val instanceof Double) {
            return (Double) val;
        }
        if (val instanceof String) {
            return NumberUtils.toDouble((String) val, fallback);
        }
        return fallback;
    }

    public double getDouble(String key) {
        return getDouble(key, 0);
    }

    public void load(SmartYamlConfiguration... yamlConfigurations) {
        if (yamlConfigurations == null) {
            return;
        }
        for (SmartYamlConfiguration yc : yamlConfigurations) {
            String name = yc.getFileName().replace(".yml", "");
            for (String key : yc.getKeys(true)) {
                if (yc.isConfigurationSection(key)) {
                    continue;
                }
                Object value;
                if (yc.isBoolean(key) || yc.isDouble(key) || yc.isInt(key) || yc.isLong(key) || yc
                        .isString(key)) {
                    value = yc.getString(key);
                } else {
                    value = yc.get(key);
                }
                set(name + "." + key, value);
            }
        }
    }

}
