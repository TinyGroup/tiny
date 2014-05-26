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
 * @Version $Id: JmxMultiLruCache.java,v 1.5 2007/05/05 05:35:30 tcfujii Exp $
 * Created on May 4, 2005 02:30 PM
 */

package org.tinygroup.jspengine.appserv.util.cache.mbeans;

import org.tinygroup.jspengine.appserv.util.cache.Constants;
import org.tinygroup.jspengine.appserv.util.cache.MultiLruCache;
/**
 * This class provides implementation for JmxLruCache MBean
 *
 * @author Krishnamohan Meduri (Krishna.Meduri@Sun.com)
 *
 */
public class JmxMultiLruCache extends JmxBaseCache 
                              implements JmxMultiLruCacheMBean {

    private MultiLruCache multiLruCache;

    public JmxMultiLruCache(MultiLruCache multiLruCache, String name) {
        super(multiLruCache,name);
        this.multiLruCache = multiLruCache;
    }

    /**
     * Returns the number of entries that have been trimmed
     */
    public Integer getTrimCount() {
        return (Integer) multiLruCache.getStatByName(
                                        Constants.STAT_MULTILRUCACHE_TRIM_COUNT);
    }

    /**
     * Returns the size of each segment
     */
    public Integer getSegmentSize() {
        return (Integer) multiLruCache.getStatByName(
                                        Constants.STAT_MULTILRUCACHE_SEGMENT_SIZE);
    }

    /**
     * Returns the legnth of the segment list
     */
    public Integer[] getSegmentListLength() {
        return (Integer[]) multiLruCache.getStatByName(
                                        Constants.STAT_MULTILRUCACHE_SEGMENT_LIST_LENGTH);
    }
}
