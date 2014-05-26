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
package org.tinygroup.flow;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.flow.component.AbstractFlowComponent;
import org.tinygroup.flow.config.Component;
import org.tinygroup.flow.config.Flow;
import org.tinygroup.flow.config.FlowProperty;
import org.tinygroup.flow.config.NextNode;
import org.tinygroup.flow.config.Node;

import com.thoughtworks.xstream.XStream;

public class FlowExecutorTest extends AbstractFlowComponent {
	

	protected void setUp() throws Exception {
		super.setUp();
		Flow flow = new Flow();
		flow.setId("aa");
		flow.setName("aa");
		flowExecutor.addFlow(flow);
		List<Node> nodes = new ArrayList<Node>();
		flow.setNodes(nodes);
		Node node = new Node();
		nodes.add(node);
		node.setId("begin");
		Component component = new Component();
		node.setComponent(component);
		component.setName("helloflow");
		List<FlowProperty> properties = new ArrayList<FlowProperty>();
		FlowProperty property = new FlowProperty();
		properties.add(property);
		property.setName("name");
		property.setValue("\"luoguo\"");
		property = new FlowProperty();
		properties.add(property);
		property.setName("resultKey");
		property.setValue("\"helloInfo\"");
		List<NextNode> nextNodes = new ArrayList<NextNode>();
		node.setNextNodes(nextNodes);
		NextNode nextNode = new NextNode();
		nextNodes.add(nextNode);
		nextNode.setEl("1==1");
		nextNode.setNextNodeId("end");
		component.setProperties(properties);
		XStream stream = new XStream();
		stream.autodetectAnnotations(true);
		System.out.println(stream.toXML(flow));
	}

	public void testExecuteStringStringContext() {
		Context context = new ContextImpl();
		flowExecutor.execute("aa", context);
		assertEquals("Hello, luoguo", context.get("helloInfo"));
	}

}
