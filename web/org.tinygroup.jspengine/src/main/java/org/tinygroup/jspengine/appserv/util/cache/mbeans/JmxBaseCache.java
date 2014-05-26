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
 * @Version $Id: JmxBaseCache.java,v 1.5 2007/05/05 05:35:30 tcfujii Exp $
 * Created on May 4, 2005 11:55 AM
 */

package org.tinygroup.jspengine.appserv.util.cache.mbeans;

import org.tinygroup.jspengine.appserv.util.cache.BaseCache;
import org.tinygroup.jspengine.appserv.util.cache.Constants;

/**
 * This class provides implementation for JmxBaseCacheMBean
 *
 * @author Krishnamohan Meduri (Krishna.Meduri@Sun.com)
 *
 */
public class JmxBaseCache implements JmxBaseCacheMBean {

    private String name;
    private BaseCache baseCache;

    public JmxBaseCache(BaseCache baseCache, String name) {
        this.baseCache = baseCache;
        this.name = name;
    }
    /**
     * Returns a unique identifier for this MBean inside the domain
     */
    public String getName() {
        return name;
    }

    /**
     * Returns maximum possible number of entries
     */
    public Integer getMaxEntries() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_MAX_ENTRIES);
    }

    /**
     * Returns threshold. This when reached, an overflow will occur
     */
    public Integer getThreshold() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_THRESHOLD);
    }

    /**
     * Returns current number of buckets
     */
    public Integer getTableSize() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_TABLE_SIZE);
    }

    /**
     * Returns current number of Entries
     */
    public Integer getEntryCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_ENTRY_COUNT);
    }

    /**
     * Return the number of cache hits
     */
    public Integer getHitCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_HIT_COUNT);
    }

    /**
     * Returns the number of cache misses
     */
    public Integer getMissCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_MISS_COUNT);
    }

    /**
     * Returns the number of entries that have been removed
     */
    public Integer getRemovalCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_REMOVAL_COUNT);
    }

    /**
     * Returns the number of values that have been refreshed 
     * (replaced with a new value in an existing extry)
     */
    public Integer getRefreshCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_REFRESH_COUNT);
    }

    /**
     * Returns the number of times that an overflow has occurred
     */
    public Integer getOverflowCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_OVERFLOW_COUNT);
    }

    /**
     * Returns the number of times new entries have been added
     */
    public Integer getAddCount() {
        return (Integer) baseCache.getStatByName(
                                        Constants.STAT_BASECACHE_ADD_COUNT);
    }
}
