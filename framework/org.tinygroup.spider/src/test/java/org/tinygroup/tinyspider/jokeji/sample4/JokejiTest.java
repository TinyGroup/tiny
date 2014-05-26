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
package org.tinygroup.tinyspider.jokeji.sample4;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.QuickNameFilter;
import org.tinygroup.tinyspider.Spider;
import org.tinygroup.tinyspider.UrlRepository;
import org.tinygroup.tinyspider.Watcher;
import org.tinygroup.tinyspider.impl.SpiderImpl;
import org.tinygroup.tinyspider.impl.UrlRepositoryMemory;
import org.tinygroup.tinyspider.impl.WatcherImpl;

public class JokejiTest {
    static UrlRepository repository = new UrlRepositoryMemory();

    public static void main(String[] args) {
        processUrl("http://www.jokeji.cn");
    }

    public static void processUrl(String url) {
        System.out.println("processing:"+url);
        Spider spider = new SpiderImpl("GBK");
        spider.setUrlRepository(repository);
        spider.addWatcher(getHrefWatcher());
        spider.addWatcher(getJokeTitleWatcher());
        spider.addWatcher(getJokeContentWatcher());
        spider.processUrl(url);
    }

    private static Watcher getHrefWatcher() {
        Watcher hrefWatcher = new WatcherImpl();
        hrefWatcher.addProcessor(new JokejiHrefProcessor());
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
        nodeFilter.setNodeName("a");
        hrefWatcher.setNodeFilter(nodeFilter);
        return hrefWatcher;
    }

    public static Watcher getJokeTitleWatcher() {
        Watcher hrefWatcher = new WatcherImpl();
        hrefWatcher.addProcessor(new JokeTitleProcessor());
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
        nodeFilter.setNodeName("a");
        nodeFilter.setIncludeAttribute("class", "user_14");
        hrefWatcher.setNodeFilter(nodeFilter);
        return hrefWatcher;
    }

    public static Watcher getJokeContentWatcher() {
        Watcher hrefWatcher = new WatcherImpl();
        hrefWatcher.addProcessor(new JokeContentProcessor());
        QuickNameFilter<HtmlNode> nodeFilter = new QuickNameFilter<HtmlNode>();
        nodeFilter.setNodeName("span");
        nodeFilter.setIncludeAttribute("class", "text110");
        hrefWatcher.setNodeFilter(nodeFilter);
        return hrefWatcher;
    }
}
