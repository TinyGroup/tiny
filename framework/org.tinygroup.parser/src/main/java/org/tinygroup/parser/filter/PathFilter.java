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
package org.tinygroup.parser.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.tinygroup.parser.Node;

/**
 * 根据路径进行过滤
 * 
 * @author luoguo
 * 
 * @param <T>
 */
public class PathFilter<T extends Node<T>> extends AbstractFilterImpl<T> {
	public PathFilter() {
	}

	public PathFilter(T node) {
		super(node);
	}

	private List<String> splitPath(String path) {
		List<String> v = new ArrayList<String>();
		Pattern pattern = Pattern.compile("/[^/]*");
		Matcher matcher = pattern.matcher(path);
		int pos = 0;
		while (matcher.find()) {
			if (pos < matcher.start()) {
				v.add(path.substring(pos, matcher.start()));
				pos = matcher.start();
			}
			v.add(path.substring(pos, matcher.end()));
			pos = matcher.end();
		}
		if (pos < path.length()) {
			v.add(path.substring(pos));
		}
		return v;
	}

	private void locateNode(List<T> nodeWithPath, T node, List<String> paths,
			int index) {
		String path = paths.get(index);
		if (index == paths.size() - 1) {
			if (node.getNodeName() != null
					&& node.getNodeName().equals(
							node.getCaseSensitiveName(path.substring(1)))) {
				// if
				// (!nodeWithPath.contains(node))//前面有重复诸，所以增加此行，后来修正完毕后没再出现，为提高效率去掉
				nodeWithPath.add(node);
			}
		} else if (node.getNodeName() != null
				&& node.getNodeName().equals(
						node.getCaseSensitiveName(path.substring(1)))) {
			List<T> nv = node.getSubNodes();
			if (nv != null) {
				for (T n : nv) {
					if (n.getNodeName() != null) {
						locateNode(nodeWithPath, n, paths, index + 1);
					}
				}
			}
		}
	}

	public T findNode(String path) {
		List<T> result = new ArrayList<T>();
		if (getNode() != null && path != null) {
			T currentNode = getNode();
			int index = 0;
			List<String> paths = splitPath(path);
			if (paths.get(0).startsWith("/")) {
				if (getNode().getRoot() != currentNode) {
					currentNode = currentNode.getRoot();
				}
			} else {
				String p = paths.get(index);
				while (p.equals("..") || p.equals("/..")) {
					currentNode = currentNode.getParent();
					index++;
					p = paths.get(index);
				}
			}
			List<T> nodeWithPath = new ArrayList<T>();
			locateNode(nodeWithPath, currentNode, paths, index);
			result = filteNode(nodeWithPath);// 进行条件过滤
		}
		if (result.size() >= 1) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 根据指定路径查找过滤后的元素
	 * 
	 * @param path
	 * @return List<T>
	 */
	public List<T> findNodeList(String path) {
		List<T> result = new ArrayList<T>();
		if (path != null) {
			T currentNode = getNode();
			int index = 0;
			List<String> paths = splitPath(path);
			if (paths.get(0).startsWith("/")) {
				if (getNode().getRoot() != currentNode) {
					currentNode = currentNode.getRoot();
				}
			} else {
				String p = paths.get(index);
				while (p.equals("..") || p.equals("/..")) {
					currentNode = currentNode.getParent();
					index++;
					p = paths.get(index);
				}
			}
			List<T> nodeWithPath = new ArrayList<T>();
			locateNode(nodeWithPath, currentNode, paths, index);
			result = filteNode(nodeWithPath);// 进行条件过滤
		}
		return result;
	}
}
