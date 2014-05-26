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
package org.tinygroup.tinyspider.jokeji.sample1;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.FastNameFilter;
import org.tinygroup.tinyspider.Processor;

import java.util.List;

public class PrintJokejiProcessor implements Processor {
    public void process(String url, HtmlNode node) {
        FastNameFilter<HtmlNode> filter = new FastNameFilter<HtmlNode>(node);
        filter.setNodeName("a");
        List<HtmlNode> aList = filter.findNodeList();
        for (HtmlNode a : aList) {
            System.out.print(a.getContent());
            System.out.print(" ");
        }
    }
}
