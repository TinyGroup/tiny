/**
 *  Copyright (c) 1997-2013, www.tinygroup.org (luo_guo@icloud.com).
 *
 *  Licensed under the GPL, Version 3.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.gnu.org/licenses/gpl.html
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
/**
 * Copyright 2000-2001 by iPlanet/Sun Microsystems, Inc.,
 * 901 San Antonio Road, Palo Alto, California, 94303, U.S.A.
 * All rights reserved.
 */
 
package org.tinygroup.jspengine.appserv.util.cache;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Enumeration;
import java.util.Map;
import java.util.Properties;

/**
 * Cache
 * Generic cache interface
 */
public interface Cache {
    
    /**
     * initialize the cache
     * @param maxEntries maximum number of entries expected in the cache
     * @param loadFactor the load factor
     * @param props opaque list of properties for a given cache implementation
     * @throws a generic Exception if the initialization failed
     */
    public void init(int maxEntries, 
                         float loadFactor, Properties props) throws Exception;

    /**
     * initialize the cache with the default load factor (0.75)
     * @param maxEntries maximum number of entries expected in the cache
     * @param props opaque list of properties for a given cache implementation
     * @throws a generic Exception if the initialization failed
     */
    public void init(int maxEntries, Properties props) throws Exception;

    /**
     * add the cache module listener
     * @param listener <code>CacheListener</code> implementation
     */
    public void addCacheListener(CacheListener listener);

    /** 
     * get the index of the item given a key
     * @param key of the entry
     * @return the index to be used in the cache
     */
    public int getIndex(Object key);

    /**
     * get the item stored at the key.
     * @param key lookup key
     * @returns the item stored at the key; null if not found.
     *
     * This function returns first value, for a multi-valued key.
     */
    public Object get(Object key);

    /**
     * get all the items with the given key.
     * @param key lookup key
     * @returns an Iterator over the items with the given key
     */
    public Iterator getAll(Object key);

    /**
     * check if the cache contains the item at the key
     * @param key lookup key
     * @returns true if there is an item stored at the key; false if not.
     */
    public boolean contains(Object key);
    
    /**
     * get an Iterator for the keys stored in the cache
     * @returns an Iterator
     */
    public Iterator keys();

    /**
     * get an Enumeration for the keys stored in the cache
     * @returns an Enumeration
     * XXX: should use Iterator which is based on Collections
     */
    public Enumeration elements();

    /**
     * get an Iterator for the values stored in the cache
     * @returns an Iterator
     */
    public Iterator values();

    /**
     * cache the given value at the specified key and return previous value
     * @param key lookup key
     * @param object item value to be stored
     * @returns the previous item stored at the key; null if not found.
     *
     * This function replaces first value, for a multi-valued key.
     */
    public Object put(Object key, Object value);

    /**
     * cache the given value at the specified key and return previous value
     * @param key lookup key
     * @param object item value to be stored
     * @param size in bytes of the value being cached
     * @returns the previous item stored at the key; null if not found.
     *
     * This function replaces first value, for a multi-valued key.
     */
    public Object put(Object key, Object value, int size);

    /**
     * add the given value to the cache at the specified key
     * @param key lookup key
     * @param object item value to be stored
     *
     * This function is suitable for multi-valued keys.
     */
    public void add(Object key, Object value);

    /**
     * add the given value with specified size to the cache at specified key
     * @param key lookup key
     * @param object item value to be stored
     * @param size in bytes of the value being added
     *
     * This function is suitable for multi-valued keys.
     */
    public void add(Object key, Object value, int size);

    /**
     * remove the item with the given key.
     * @param key lookup key
     * @returns the item stored at the key; null if not found.
     *
     * This function removes first value, for a multi-valued key.
     */
    public Object remove(Object key);

    /**
     * remove the given value stored at the key.
     * @param key lookup key
     * @param value to match (for multi-valued keys)
     * @returns the item stored at the key; null if not found.
     */
    public Object remove(Object key, Object value);

    /**
     * remove all the item with the given key.
     * @param key lookup key
     */
    public void removeAll(Object key);

    /**
     * wait for a refresh on the object associated with the key
     * @param index index of the entry. The index must be obtained via 
     * one of the <code>getIndex()</code> methods.
     * @returns <code>true</code> on successful notification, or 
     * <code>false</code> if there is no thread refreshing this entry.
     */
    public boolean waitRefresh(int index);

    /**
     * notify threads waiting for a refresh on the object associated with the key
     * @param index index of the entry. The index must be obtained via 
     * one of the <code>getIndex()</code> methods.
     */
    public void notifyRefresh(int index);

    /**
     * clear all the entries from the cache.
     * @returns the number of entries cleared from the cache
     */
    public int clear();

    /**
     * is this cache empty?
     * @returns true if the cache is empty; false otherwise.
     */
    public boolean isEmpty();

    /**
     * get the number of entries in the cache
     * @return the number of entries the cache currently holds
     */
    public int getEntryCount();

    /**
     * get the stats map
     */
    /**
     * get the desired statistic counter
     * @param key to corresponding stat
     * @return an Object corresponding to the stat
     * See also: Constant.java for the key
     */
    public Object getStatByName(String key);

    /**
     * get the stats snapshot
     * @return a Map of stats
     * See also: Constant.java for the keys
     */
    public Map getStats();

    /**
     * clear all stats
     */
    public void clearStats();

    /**
     * trim the expired entries from the cache.
     * @param maxCount maximum number of invalid entries to trim
     *        specify Integer.MAX_VALUE to trim all timedout entries
     *
     * This call is to be scheduled by a thread managed by the container.
     */
    public void trimExpiredEntries(int maxCount);

    /**
     * Destroys this cache. This method should perform final clean ups.
     */
    public void destroy();
}
