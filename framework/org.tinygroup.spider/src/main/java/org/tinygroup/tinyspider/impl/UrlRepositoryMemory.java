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
package org.tinygroup.tinyspider.impl;

import org.tinygroup.tinyspider.UrlRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UrlRepositoryMemory implements UrlRepository {
    private Map<String, String> contentCache = new ConcurrentHashMap<String, String>();

    public boolean isExist(String url) {
        return contentCache.containsKey(url);
    }

    public boolean isExist(String url, Map<String, Object> parameter) {
        String fullUrl = getFullUrl(url, parameter);
        return isExist(fullUrl);
    }

    private String getFullUrl(String url, Map<String, Object> parameter) {
        List<String> keyList = new ArrayList<String>();
        keyList.addAll(parameter.keySet());
        Collections.sort(keyList);
        StringBuffer sb = new StringBuffer(url);
        sb.append('\0');
        for (String key : keyList) {
            sb.append('&');
            sb.append(key);
            sb.append('=');
            sb.append(parameter.get(key));
        }
        return sb.toString();
    }

    public void putUrlWithContent(String url, String content) {
        contentCache.put(url, content);
    }

    public void putUrlWithContent(String url, Map<String, Object> parameter,
                                  String content) {
        contentCache.put(getFullUrl(url, parameter), content);
    }

    public String getContent(String url) {
        return contentCache.get(url);
    }

    public String getContent(String url, Map<String, Object> parameter) {
        return getContent(getFullUrl(url, parameter));
    }

}
