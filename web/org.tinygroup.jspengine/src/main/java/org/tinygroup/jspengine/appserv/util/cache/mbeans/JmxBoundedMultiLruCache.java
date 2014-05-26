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
 * @Version $Id: JmxBoundedMultiLruCache.java,v 1.6 2007/05/05 05:35:30 tcfujii Exp $
 * Created on May 4, 2005 07:40 PM
 */

package org.tinygroup.jspengine.appserv.util.cache.mbeans;

import org.tinygroup.jspengine.appserv.util.cache.BoundedMultiLruCache;
import org.tinygroup.jspengine.appserv.util.cache.Constants;
/**
 * This class provides implementation for JmxLruCache MBean
 *
 * @author Krishnamohan Meduri (Krishna.Meduri@Sun.com)
 *
 */
public class JmxBoundedMultiLruCache extends JmxMultiLruCache 
                              implements JmxBoundedMultiLruCacheMBean {

    private BoundedMultiLruCache boundedMultiLruCache;

    public JmxBoundedMultiLruCache(BoundedMultiLruCache boundedMultiLruCache, 
                                   String name) {
        super(boundedMultiLruCache, name);
        this.boundedMultiLruCache = boundedMultiLruCache;
    }

    /**
     * Returns the current size of the cache in bytes
     */
    public Long getCurrentSize() {
        return (Long) boundedMultiLruCache.getStatByName(
                                        Constants.STAT_BOUNDEDMULTILRUCACHE_CURRENT_SIZE);
    }

    /**
     * Returns the upper bound on the cache size
     */
    public Long getMaxSize() {
        Object object = boundedMultiLruCache.getStatByName(
                                        Constants.STAT_BOUNDEDMULTILRUCACHE_MAX_SIZE);
        /*
         * BoundedMultiLruCache class returns java.lang.String with a value 
         * "default" if the maxSize == Constants.DEFAULT_MAX_CACHE_SIZE
         * To take care of this case, the if/else is added below
         */
        if (object instanceof String &&
            ((String) object).equals(Constants.STAT_DEFAULT)) {
            return Long.valueOf(Constants.DEFAULT_MAX_CACHE_SIZE);
        }
        else {
            return (Long) object;
        }
    }
}
