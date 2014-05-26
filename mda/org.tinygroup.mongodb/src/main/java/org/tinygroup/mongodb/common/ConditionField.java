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
package org.tinygroup.mongodb.common;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 条件字段<br>
 * 操作的条件字段
 */
@XStreamAlias("condition-field")
public class ConditionField extends OperationField {

	private static final String DEFAULT_CONNECT_MODE = "AND";
	
	private static final String DEFAULT_COMPARE_MODE="equals";
	
	private static final String DEFAULT_GROUP="defaultGroup";

	/**
	 * 所属查询分组<br>
	 * 如果有多个，用逗号分隔，在界面中，可以根据分组名称进行字段过滤
	 */
	@XStreamAsAttribute
	String groups;

	@XStreamAsAttribute
	@XStreamAlias("compare-mode")
	/**
	 * 比较方式<br>
	 * 如果不设置，默认为等于
	 */
	String compareMode;
	@XStreamAsAttribute
	@XStreamAlias("connect-mode")
	/**
	 * 多个条件字段之间的连接方式
	 */
	String connectMode=DEFAULT_CONNECT_MODE;
	/**
	 * 聚合函数,如果有，就生成having子句
	 */
	@XStreamAsAttribute
	@XStreamAlias("aggregate-function")
	String aggregateFunction;

	public String getGroups() {
		if(groups==null){
			groups=DEFAULT_GROUP;
		}
		return groups;
	}

	public void setGroups(String groups) {
		this.groups = groups;
	}

	public String getCompareMode() {
		if(compareMode==null){
			compareMode=DEFAULT_COMPARE_MODE;
		}
		return compareMode;
	}

	public void setCompareMode(String compareMode) {
		this.compareMode = compareMode;
	}

	public String getAggregateFunction() {
		return aggregateFunction;
	}

	public void setAggregateFunction(String aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}


	public String getConnectMode() {
		if(connectMode==null){
			connectMode=DEFAULT_CONNECT_MODE;
		}
		return connectMode;
	}

	public void setConnectMode(String connectMode) {
		this.connectMode = connectMode;
	}

	
}
