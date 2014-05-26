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
package org.tinygroup.container;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.tinygroup.container.impl.BaseObjectImpl;
import org.tinygroup.container.impl.ContainerImpl;

import junit.framework.TestCase;

/**
 * 
 * 功能说明: 容器接口测试用例
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-25 <br>
 * <br>
 */
public class ContainerTest extends TestCase {

	private Container<String, BaseObject<String>> container = new ContainerImpl<String, BaseObject<String>>();
	{
		List<BaseObject<String>> list = new ArrayList<BaseObject<String>>();
		BaseObject<String> base1 = new BaseObjectImpl<String>();
		base1.setId("id2");
		base1.setName("name1");
		base1.setTitle("title1");
		base1.setOrder(1);
		base1.setDescription("description1");
		BaseObject<String> base2 = new BaseObjectImpl<String>();
		base2.setId("id1");
		base2.setName("name2");
		base2.setTitle("title2");
		base2.setOrder(2);
		base2.setDescription("description2");
		list.add(base1);
		list.add(base2);
		container.setList(list);
	}

	public final void testGetList() {
		List<BaseObject<String>> elements = container.getList();
		assertEquals(2, elements.size());
	}

	public final void testGetListComparatorOfT() {
		Comparator<BaseObject<String>> comparator = new Comparator<BaseObject<String>>() {

			public int compare(BaseObject<String> o1, BaseObject<String> o2) {
				return o1.getId().compareTo(o2.getId());
			}
		};
		List<BaseObject<String>> objects = container.getList();
		assertEquals("id2", objects.get(0).getId());
		objects = container.getList(comparator);
		assertEquals("id1", objects.get(0).getId());

	}

}
