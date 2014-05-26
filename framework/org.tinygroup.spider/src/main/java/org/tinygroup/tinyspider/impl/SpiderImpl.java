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

import org.tinygroup.htmlparser.HtmlDocument;
import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.htmlparser.parser.HtmlStringParser;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.parser.NodeFilter;
import org.tinygroup.tinyspider.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpiderImpl implements Spider {
    private static Logger logger = LoggerFactory.getLogger(SpiderImpl.class);


    private List<Watcher> watcherList = new ArrayList<Watcher>();
    private List<SiteVisitor> siteVisitorList = new ArrayList<SiteVisitor>();
    private UrlRepository urlRepository;
    private String responseCharset = "UTF-8";

    public SpiderImpl() {
        this("UTF-8");
    }

    public SpiderImpl(String charset) {
        responseCharset = charset;
    }

    public void addWatcher(Watcher watcher) {
        watcherList.add(watcher);
    }

    public void processUrl(String url) {
        processUrl(url, null);
    }

    public void setResponseCharset(String charset) {
        this.responseCharset = charset;
    }

    public String getResponseCharset() {
        return responseCharset;
    }

    public void addSiteVisitor(SiteVisitor siteVisitor) {
        siteVisitorList.add(siteVisitor);
    }

    public void setUrlRepository(UrlRepository urlRepository) {
        this.urlRepository = urlRepository;

    }

    public void processUrl(String url, Map<String, Object> parameter) {
        if (urlRepository == null) {
            urlRepository = new UrlRepositoryMemory();
        }
        if (urlRepository.isExist(url)) {
            return;
        }
        String content = null;
        if (siteVisitorList.size() == 0) {
            siteVisitorList.add(new SiteVisitorInclude(".*"));
        }
        for (SiteVisitor siteVisitor : siteVisitorList) {
            if (siteVisitor.isMatch(url)) {
                try {
                    content = siteVisitor.getContent(url, parameter, responseCharset);
                } catch (Exception e) {
                    logger.errorMessage("不能载入url:{},错误原因：{}", e, url, e.getMessage());
                    return;
                }
                break;
            }
        }
        // 如果没有拿到内容
        if (content == null) {
            logger.logMessage(LogLevel.ERROR, "url:{}内容为空！", url);
            return;
        }
        urlRepository.putUrlWithContent(url, content);
        HtmlDocument document = new HtmlStringParser().parse(content);
        processWatcher(url, document);
    }

    private void processWatcher(String url, HtmlDocument document) {
        for (Watcher watcher : watcherList) {
            NodeFilter<HtmlNode> nodeFilter = watcher.getNodeFilter();
            nodeFilter.init(document.getRoot());
            List<HtmlNode> nodeList = nodeFilter.findNodeList();
            for (HtmlNode htmlNode : nodeList) {
                for (Processor e : watcher.getProcessorList()) {
                    e.process(url, htmlNode);
                }
            }
        }
    }
}
