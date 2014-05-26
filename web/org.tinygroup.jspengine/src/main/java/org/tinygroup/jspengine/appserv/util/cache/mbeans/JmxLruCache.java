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
 * @Version $Id: JmxLruCache.java,v 1.5 2007/05/05 05:35:30 tcfujii Exp $
 * Created on May 4, 2005 1:20 PM
 */

package org.tinygroup.jspengine.appserv.util.cache.mbeans;

import org.tinygroup.jspengine.appserv.util.cache.Constants;
import org.tinygroup.jspengine.appserv.util.cache.LruCache;
/**
 * This class provides implementation for JmxLruCache MBean
 *
 * @author Krishnamohan Meduri (Krishna.Meduri@Sun.com)
 *
 */
public class JmxLruCache extends JmxBaseCache implements JmxLruCacheMBean {

    private LruCache lruCache;

    public JmxLruCache(LruCache lruCache, String name) {
        super(lruCache,name);
        this.lruCache = lruCache;
    }
    /**
     * Returns the current lenght of the LRU list
     */
    public Integer getLruListLength() {
        return (Integer) lruCache.getStatByName(
                                        Constants.STAT_LRUCACHE_LIST_LENGTH);
    }

    /**
     * Returns the number of entries that have been trimmed
     */
    public Integer getTrimCount() {
        return (Integer) lruCache.getStatByName(
                                        Constants.STAT_LRUCACHE_TRIM_COUNT);
    }
}
