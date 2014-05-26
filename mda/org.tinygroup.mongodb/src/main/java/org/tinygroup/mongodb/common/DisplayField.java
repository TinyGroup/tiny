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
 * 视图的显示属性，视图字段
 * */
@XStreamAlias("display-field")
public class DisplayField extends OperationField {
	// String standardFieldName;


	@XStreamAsAttribute
	@XStreamAlias("aggregate-function")
	// none", "count", "sum", "average", "min", "max", "std", "stdPop", "var", "varPop" and "custom".
	String aggregateFunction;// 聚合函数,having

	@XStreamAsAttribute
	@XStreamAlias("aggregate-title")
	String aggregateTitle;// 聚合标题
	@XStreamAsAttribute
	@XStreamAlias("aggregate-by-view")
	boolean aggregateByView;// true表示前台，false表示后台
	@XStreamAlias("cell-formatter")
	String cellFormatter;

	public boolean isAggregateByView() {
		return aggregateByView;
	}

	public void setAggregateByView(boolean aggregateByView) {
		this.aggregateByView = aggregateByView;
	}

	public String getAggregateTitle() {
		return aggregateTitle;
	}

	public void setAggregateTitle(String aggregateTitle) {
		this.aggregateTitle = aggregateTitle;
	}


	public String getAggregateFunction() {
		return aggregateFunction;
	}

	public void setAggregateFunction(String aggregateFunction) {
		this.aggregateFunction = aggregateFunction;
	}

	public String getCellFormatter() {
		return cellFormatter;
	}

	public void setCellFormatter(String cellFormatter) {
		this.cellFormatter = cellFormatter;
	}

	
}
