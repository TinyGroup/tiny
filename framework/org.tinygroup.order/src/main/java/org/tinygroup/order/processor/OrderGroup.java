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
package org.tinygroup.order.processor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.ObjectUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.order.order.AfterFeature;
import org.tinygroup.order.order.BeforeFeature;
import org.tinygroup.order.order.FeatureOrder;
import org.tinygroup.order.order.RequiresFeature;
import org.tinygroup.parser.filter.NameFilter;
import org.tinygroup.xmlparser.node.XmlNode;

/**
 * 排序组信息
 * 
 * @author renhui
 * 
 */
public class OrderGroup<T> {

	private static final String ORDER_OBJECT = "order-object";

	private static Logger logger = LoggerFactory.getLogger(OrderGroup.class);

	private Map<Class<T>, String> groupMap = CollectionUtil.createHashMap();

	private Map<Class<T>, FeatureOrder[]> featureOrderMap = CollectionUtil
			.createHashMap();

	private static final String DEFAULT_FEATURE_NAME = "feature";

	private FeatureOrder defaultFeatureOrder;

	protected void setDefaultFeatureOrder(String featureType, String feature) {
		this.defaultFeatureOrder = createFeatureOrder(featureType, feature);
	}

	public String getFeature(Class<T> type) {
		String feature = groupMap.get(type);
		if (feature == null) {
			feature = DEFAULT_FEATURE_NAME;
		}
		return feature;
	}

	public FeatureOrder[] getFeatureOrders(Class<T> type) {
		FeatureOrder[] featureOrders = featureOrderMap.get(type);
		if (featureOrders == null) {
			featureOrders = new FeatureOrder[] { defaultFeatureOrder };
		}
		return featureOrders;
	}

	public List<T> initProcessorOrder(List<T> processorList) {

		logger.logMessage(LogLevel.INFO, "文件处理器加载顺序处理开始");
		Map<Integer, T> processorMap = CollectionUtil.createLinkedHashMap();
		Set<Integer> processorSet = CollectionUtil.createHashSet();
		for (int i = 0; i < processorList.size(); i++) {

			if (!processorMap.containsKey(i)) {
				sort(i, processorList.get(i), processorList, processorMap,
						processorSet);
			}

		}
		ArrayList<T> newProcessorList = CollectionUtil
				.createArrayList(processorMap.values());

		// 检查缺失的features
		Set<String> usableFeatures = CollectionUtil.createHashSet();
		for (T f : newProcessorList) {
			usableFeatures.add(getFeature(getType(f)));
			FeatureOrder[] featureOrders = getFeatureOrders(getType(f));
			if (featureOrders != null) {
				for (FeatureOrder requiresFeature : featureOrders) {
					if (requiresFeature instanceof RequiresFeature
							&& !usableFeatures.contains(requiresFeature
									.getFeature())) {
						throw new IllegalArgumentException(
								String.format(
										"Missing feature of %s, which is required by %s",
										requiresFeature.getFeature(), f));
					}
				}
			}
		}
		logger.logMessage(LogLevel.INFO, "文件处理器加载顺序处理结束");
		return newProcessorList;

	}

	@SuppressWarnings("unchecked")
	private Class<T> getType(T f) {
		return (Class<T>) f.getClass();
	}

	private void sort(int index, T processor, List<T> processorList,
			Map<Integer, T> processorMap, Set<Integer> processorSet) {

		if (!processorSet.contains(index)) {
			processorSet.add(index);

			for (Map.Entry<Integer, T> entry : getBefore(processor,
					processorList).entrySet()) {
				sort(entry.getKey(), entry.getValue(), processorList,
						processorMap, processorSet);
			}

			processorMap.put(index, processor);
			processorSet.remove(index);
		}

	}

	private Map<Integer, T> getBefore(T processor, List<T> processorList) {
		Map<Integer, T> allBefore = CollectionUtil.createLinkedHashMap();
		for (int i = 0; i < processorList.size(); i++) {
			T test = processorList.get(i);

			if (!processor.equals(test) && compare(test, processor) < 0) {
				allBefore.put(i, test);
			}
		}

		return allBefore;
	}

	/**
	 * 比较f1和f2，如f1在前，返回-1，如f1在后，则返回1，不确定则返回0。
	 * <p>
	 * 明确指定的胜于用*指定的。
	 * </p>
	 */
	private int compare(T f1, T f2) {
		boolean f1BeforeF2 = compare(f1, BeforeFeature.class, f2);
		boolean f2AfterF1 = compare(f2, AfterFeature.class, f1);

		if (f1BeforeF2 || f2AfterF1) {
			return -1;
		}

		boolean f2BeforeF1 = compare(f2, BeforeFeature.class, f1);
		boolean f1AfterF2 = compare(f1, AfterFeature.class, f2);

		if (f2BeforeF1 || f1AfterF2) {
			return 1;
		}

		boolean f1BeforeAll = compareWithAll(f1, BeforeFeature.class);
		boolean f2AfterAll = compareWithAll(f2, AfterFeature.class);

		if (f1BeforeAll || f2AfterAll) {
			return -1;
		}

		boolean f2BeforeAll = compareWithAll(f2, BeforeFeature.class);
		boolean f1AfterAll = compareWithAll(f1, AfterFeature.class);

		if (f2BeforeAll || f1AfterAll) {
			return 1;
		}

		return 0;
	}

	private boolean compareWithAll(T f1, Class<? extends FeatureOrder> type) {
		FeatureOrder[] featureOrders = getFeatureOrders(getType(f1));

		if (ArrayUtil.isEmptyArray(featureOrders)) {
			return false;
		}

		for (FeatureOrder featureOrder : featureOrders) {
			if (type.isInstance(featureOrder)
					&& ObjectUtil.isEquals(featureOrder.getFeature(), "*")) {
				return true;
			}
		}

		return false;
	}

	private boolean compare(T f1, Class<? extends FeatureOrder> type, T f2) {
		FeatureOrder[] featureOrders = getFeatureOrders(getType(f1));
		String feature = getFeature(getType(f2));

		if (feature == null || "".equals(feature.trim())
				|| ArrayUtil.isEmptyArray(featureOrders)) {
			return false;
		}
		for (FeatureOrder featureOrder : featureOrders) {
			if (type.isInstance(featureOrder)
					&& ObjectUtil.isEquals(featureOrder.getFeature(), feature)) {
				return true;
			}
		}

		return false;
	}

	/**
	 * 加载order-group中子节点内容
	 * 
	 * @param xmlNode
	 */
	@SuppressWarnings({ "unchecked" })
	public void load(XmlNode xmlNode) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> nodes = nameFilter.findNodeList(ORDER_OBJECT);
		for (XmlNode node : nodes) {
			String name = node.getAttribute("name");
			String className = node.getAttribute("type");
			Class<T> type = null;
			try {
				type = (Class<T>) Class.forName(className);
				groupMap.put(type, name);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException(String.format("找不到名称:<%s>对应的类",
						className), e);
			}
			if (type != null) {
				loadFeature(node, type);
			}

		}

	}

	/**
	 * 加载feature-order节点
	 * 
	 * @param node
	 * @param type
	 */
	private void loadFeature(XmlNode xmlNode, Class<T> type) {
		NameFilter<XmlNode> nameFilter = new NameFilter<XmlNode>(xmlNode);
		List<XmlNode> nodes = nameFilter.findNodeList("feature-order");
		List<FeatureOrder> orders = CollectionUtil.createArrayList();
		for (XmlNode node : nodes) {
			String featureType = node.getAttribute("type");
			String feature = node.getAttribute("feature");
			FeatureOrder featureOrder = createFeatureOrder(featureType, feature);
			orders.add(featureOrder);
		}
		featureOrderMap.put(type, orders.toArray(new FeatureOrder[0]));

	}

	private FeatureOrder createFeatureOrder(String featureType, String feature) {

		FeatureOrder order = null;
		if ("before".equals(featureType)) {
			order = new BeforeFeature(feature);
		} else if ("after".equals(featureType)) {
			order = new AfterFeature(feature);
		} else if ("require".equals(featureType)) {
			order = new RequiresFeature(feature);
		}
		return order;
	}

}
