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
package org.tinygroup.tinyspider;

import java.util.List;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.filter.FastNameFilter;

public class PrintForumProcessor implements Processor {

	public void process(String url, HtmlNode node) {
		FastNameFilter<HtmlNode> filter = new FastNameFilter<HtmlNode>(node);
		filter.setNodeName("h3");
		filter.setIncludeNode("a");
		HtmlNode h3 = filter.findNode();
		if (h3 != null) {
			System.out.println(h3.getSubNode("a").getContent());

			FastNameFilter<HtmlNode> forumFilter = new FastNameFilter<HtmlNode>(
					node);
			forumFilter.setNodeName("div");
			forumFilter.setIncludeAttribute("class", "categoryInfo");
			forumFilter.setIncludeNode("h5");
			List<HtmlNode> divList = forumFilter.findNodeList();
			for (HtmlNode div : divList) {
				System.out.print("\t");
				System.out.println(div.getSubNode("h5").getSubNode("a")
						.getContent());
			}
		}
	}

}
