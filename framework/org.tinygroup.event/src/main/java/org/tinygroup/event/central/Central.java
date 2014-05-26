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
package org.tinygroup.event.central;


/**
 * 服务中心接口
 * 
 * @author luoguo
 * 
 */
public interface Central {
	String getName();

	/**
	 * 注册节点，把一个目标节点注册过来，目标节点注册的时候，会提供节点的信息，利用节点的信息回调，以获取节点提供的服务情况
	 * 
	 * @param node
	 */
	boolean registerNode(Node node);

	/**
	 * 取消注册节点，并清理与之相关的内容
	 * 
	 * @param node
	 */
	boolean unregisterNode(Node node);


}
