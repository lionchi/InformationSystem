package ru.gavrilov.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MapUtil {

    private MapUtil() {
    }

    public static <K, V> V getOrDefault(Map<K, V> map, K key, V defaultValue) {
        synchronized (map) {
            V value = map.get(key);
            if (value != null) {
                return value;
            }
            return defaultValue;
        }
    }

    public static <K, V> V putIfAbsent(Map<K, V> map, K key, V value) {
        synchronized (map) {
            V existingValue = map.get(key);
            if (existingValue != null) {
                return existingValue;
            }
            map.put(key, value);
            return null;
        }
    }

    public static <K, V> List<V> createNewListIfAbsent(Map<K, List<V>> map, K key) {
        synchronized (map) {
            List<V> value = map.get(key);
            if (value != null) {
                return value;
            }
            value = new ArrayList<>();
            map.put(key, value);
            return value;
        }
    }

}
