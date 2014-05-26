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
package org.tinygroup.commons.order;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * 功能说明: 实现ordered接口类的排序方法

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-20 <br>
 * <br>
 */
public class OrderUtil {

	public static void order(List<? extends Ordered> orderList){
		Collections.sort(orderList, new Comparator<Ordered>() {
			public int compare(Ordered o1, Ordered o2) {
				if (o1 != null && o2 != null) {
					return o1.getOrder() > o2.getOrder() ? 1
							: (o1.getOrder() == o2.getOrder() ? 0 : -1);
				}
				return 0;
			}
		});
	}
	
}
