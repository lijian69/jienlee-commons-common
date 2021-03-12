package com.jienlee.common.cache;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

/**
 *
 * @author jien.lee
 */
public class LocalCacheBuilder {

    private static Logger logger = LoggerFactory.getLogger(LocalCacheBuilder.class);

    public static <K, V> LoadingCache<K, V> build(final CacheLoader<K, V> loader) {
        return build(loader, 300);
    }

    public static <K, V> LoadingCache<K, V> build(final CacheLoader<K, V> loader, int expireSecond) {

        return CacheBuilder
                .newBuilder()
                .maximumSize(100000)
                .expireAfterWrite(expireSecond, TimeUnit.SECONDS)
                .build(new CacheLoader<K, V>() {
                    @Override
                    public V load(K key) throws Exception {
                        V value = null;
                        long start = System.currentTimeMillis();
                        try {
                            value = loader.load(key);
                        } catch (Exception e) {
                            logger.error("LocalCache load {} failed.", key, e);
                        }
                        return (V) value;
                    }
                });
    }
}
