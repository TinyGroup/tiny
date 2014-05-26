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

import org.tinygroup.httpvisit.HttpVisitor;
import org.tinygroup.httpvisit.impl.HttpVisitorImpl;
import org.tinygroup.tinyspider.SiteVisitor;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 站点访问器
 *
 * @author luoguo
 */
public class SiteVisitorInclude implements SiteVisitor {
    private Pattern pattern = null;
    private HttpVisitor httpVisitor;
    private String visitMode = VISIT_MODE_GET;

    public HttpVisitor getHttpVisitor() {
        return httpVisitor;
    }

    public void setHttpVisitor(HttpVisitor httpVisitor) {
        this.httpVisitor = httpVisitor;
    }

    /**
     * @param patternString 匹配的字串，只要匹配成功，即可以用其进行访问
     */
    public SiteVisitorInclude(String patternString) {
        pattern = Pattern.compile(patternString);
    }

    public boolean isMatch(String url) {
        Matcher matcher = pattern.matcher(url);
        return matcher.find();
    }

    public String getContent(String url, String charset) {
        return getContent(url, null, charset);
    }

    public String getContent(String url, Map<String, Object> parameter, String charset) {
        if (httpVisitor == null) {
            httpVisitor = new HttpVisitorImpl();
            httpVisitor.setResponseCharset(charset);
        }
        if (visitMode.equalsIgnoreCase(VISIT_MODE_GET)) {
            return httpVisitor.getUrl(url, parameter);
        }
        if (visitMode.equalsIgnoreCase(VISIT_MODE_POST)) {
            return httpVisitor.postUrl(url, parameter);
        }
        throw new RuntimeException("错误的访问方式：" + visitMode);
    }


    public void setVisitMode(String visitMode) {
        this.visitMode = visitMode;
    }

}
