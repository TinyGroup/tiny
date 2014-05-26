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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.htmlparser.node.HtmlNode;
import org.tinygroup.parser.NodeFilter;
import org.tinygroup.tinyspider.Processor;
import org.tinygroup.tinyspider.Watcher;

public class WatcherImpl implements Watcher {

	private NodeFilter<HtmlNode> nodeFilter;
	private List<Processor> processorList = new ArrayList<Processor>();

	public void setNodeFilter(NodeFilter<HtmlNode> nodeFilter) {
		this.nodeFilter = nodeFilter;
	}

	public NodeFilter<HtmlNode> getNodeFilter() {
		return this.nodeFilter;
	}

	public void addProcessor(Processor processor) {
		processorList.add(processor);
	}

	public List<Processor> getProcessorList() {
		return this.processorList;
	}

}
