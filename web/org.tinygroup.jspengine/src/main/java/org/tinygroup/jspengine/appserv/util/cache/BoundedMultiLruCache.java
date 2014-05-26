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

import java.text.MessageFormat;

import java.util.Properties;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * MultiLruCache -- in-memory bounded LRU cache with multiple LRU lists
 * Underlying Hashtable is made into logical segments, with each segment
 * having its own LRU list.
 */
public class BoundedMultiLruCache extends MultiLruCache {

    // upper bound on the cache size
    protected long maxSize = Constants.DEFAULT_MAX_CACHE_SIZE;
    protected long currentSize;
    private Object currentSizeLk = new Object();

    /**
     * initialize the LRU cache
     * @param maxCapacity maximum number of entries this cache may hold
     */
    public void init(int maxCapacity, Properties props) throws Exception {
        super.init(maxCapacity, props);
        currentSize = 0;

        if (props != null) {
            String strMaxSize = props.getProperty("MaxSize");
            int multiplier = 1;
            long size = -1;

            String prop = strMaxSize;
            if (prop != null) {
                int index;

                // upper case the string
                prop = prop.toUpperCase();

                // look for 200KB or 80Kb or 1MB or 2Mb like suffixes
                if ((index = prop.indexOf("KB")) != -1) {
                    multiplier = Constants.KB;
                    prop = prop.substring(0, index);
                } else if ((index = prop.indexOf("MB")) != -1) {
                    multiplier = Constants.MB;
                    prop = prop.substring(0, index);
                }

                try {
                    size = Long.parseLong(prop.trim());
                } catch (NumberFormatException nfe) {}
            }

            // sanity check and convert
            if (size > 0)
                maxSize = (size * multiplier);
            else  {
                String msg = _rb.getString("cache.BoundedMultiLruCache.illegalMaxSize");

                Object[] params = { strMaxSize };
                msg = MessageFormat.format(msg, params);

                throw new IllegalArgumentException(msg);
            }
        }
    }

    /**
     * this item is just added to the cache
     * @param item <code>CacheItem</code> that was created
     * @return a overflow item; may be null
     *
     * Cache bucket is already synchronized by the caller
     */
    protected CacheItem itemAdded(CacheItem item) {
        LruCacheItem overflow = (LruCacheItem) super.itemAdded(item);

        // update the size
        if (overflow != null) {
            decrementCurrentSize(overflow.getSize());
        }
        incrementCurrentSize(item.getSize());

        return overflow;
    }

    /**
     * item value has been refreshed
     * @param item <code>CacheItem</code> that was refreshed
     * @param oldSize size of the previous value that was refreshed
     * Cache bucket is already synchronized by the caller
     */
    protected void itemRefreshed(CacheItem item, int oldSize) {
        super.itemRefreshed(item, oldSize);

        /** reduce the cache by the size of the size of the previous value 
         *  and increment by the value being refreshed with.
         */
        decrementCurrentSize(oldSize);
        incrementCurrentSize(item.getSize());
    }

    /**
     * item value has been removed from the cache
     * @param item <code>CacheItem</code> that was just removed
     *
     * Cache bucket is already synchronized by the caller
     */
    protected void itemRemoved(CacheItem item) {
        super.itemRemoved(item);

        // update the size
        decrementCurrentSize(item.getSize());
    }

    /**
     * has cache reached its threshold
     * @return true when the cache reached its threshold
     */
    protected boolean isThresholdReached() {
        return (currentSize > maxSize || super.isThresholdReached());
    }

    /**
     * synchronized counter updates
     */
    protected final void incrementCurrentSize(int size) {
        synchronized(currentSizeLk) {
            currentSize += size;
        }
    }

    protected final void decrementCurrentSize(int size) {
        synchronized(currentSizeLk) {
            currentSize -= size;
        }
    }

    /**
     * get generic stats from subclasses 
     */

    /**
     * get the desired statistic counter
     * @param key to corresponding stat
     * @return an Object corresponding to the stat
     * See also: Constant.java for the key
     */
    public Object getStatByName(String key) {
        Object stat = super.getStatByName(key);

        if (stat == null && key != null) {
            if (key.equals(Constants.STAT_BOUNDEDMULTILRUCACHE_CURRENT_SIZE))
                stat = Long.valueOf(currentSize);
            else if (key.equals(Constants.STAT_BOUNDEDMULTILRUCACHE_MAX_SIZE)) {
                if (maxSize == Constants.DEFAULT_MAX_CACHE_SIZE)
                    stat = Constants.STAT_DEFAULT;
                else
                    stat = Long.valueOf(maxSize);
            }
        }

        return stat;
    }

    public Map getStats() {
        Map stats = super.getStats();

        // cache size in KB
        stats.put(Constants.STAT_BOUNDEDMULTILRUCACHE_CURRENT_SIZE,
                  Long.valueOf(currentSize));
        if (maxSize == Constants.DEFAULT_MAX_CACHE_SIZE) {
            stats.put(Constants.STAT_BOUNDEDMULTILRUCACHE_MAX_SIZE,
                      Constants.STAT_DEFAULT);
        }
        else {
            stats.put(Constants.STAT_BOUNDEDMULTILRUCACHE_MAX_SIZE,
                      Long.valueOf(maxSize));
        }
        return stats;
    }
}
