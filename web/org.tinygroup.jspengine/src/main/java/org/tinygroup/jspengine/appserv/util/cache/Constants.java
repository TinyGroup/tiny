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

/**
 * define all cache related constants
 */
public class Constants {
    public final static String STAT_DEFAULT = "default";

    // default maximum number of entries in the cache
    public final static int DEFAULT_MAX_ENTRIES = 8192;

    // default maximum size in bytes of the cache
    public final static long DEFAULT_MAX_CACHE_SIZE = Long.MAX_VALUE;

    // maxSize specified in bytes, KB or MB
    public final static int KB = 1024;
    public final static int MB = (KB * KB);

    public final static String STAT_BASECACHE_MAX_ENTRIES="cache.BaseCache.stat_maxEntries";
    public final static String STAT_BASECACHE_THRESHOLD="cache.BaseCache.stat_threshold";
    public final static String STAT_BASECACHE_TABLE_SIZE="cache.BaseCache.stat_tableSize";
    public final static String STAT_BASECACHE_ENTRY_COUNT="cache.BaseCache.stat_entryCount";
    public final static String STAT_BASECACHE_HIT_COUNT="cache.BaseCache.stat_hitCount";
    public final static String STAT_BASECACHE_MISS_COUNT="cache.BaseCache.stat_missCount";
    public final static String STAT_BASECACHE_REMOVAL_COUNT="cache.BaseCache.stat_removalCount";
    public final static String STAT_BASECACHE_REFRESH_COUNT="cache.BaseCache.stat_refreshCount";
    public final static String STAT_BASECACHE_OVERFLOW_COUNT="cache.BaseCache.stat_overflowCount";
    public final static String STAT_BASECACHE_ADD_COUNT="cache.BaseCache.stat_addCount";

    public final static String STAT_LRUCACHE_LIST_LENGTH="cache.LruCache.stat_lruListLength";
    public final static String STAT_LRUCACHE_TRIM_COUNT="cache.LruCache.stat_trimCount";

    public final static String STAT_MULTILRUCACHE_SEGMENT_SIZE="cache.MultiLruCache.stat_segmentSize";
    public final static String STAT_MULTILRUCACHE_SEGMENT_LIST_LENGTH="cache.MultiLruCache.stat_segmentListLength"; 
    public final static String STAT_MULTILRUCACHE_TRIM_COUNT="cache.MultiLruCache.stat_trimCount";

    public final static String STAT_BOUNDEDMULTILRUCACHE_CURRENT_SIZE="cache.BoundedMultiLruCache.stat_currentSize";
    public final static String STAT_BOUNDEDMULTILRUCACHE_MAX_SIZE="cache.BoundedMultiLruCache.stat_maxSize";
}
