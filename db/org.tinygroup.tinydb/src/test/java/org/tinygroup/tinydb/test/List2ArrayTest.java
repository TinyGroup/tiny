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
package org.tinygroup.tinydb.test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class List2ArrayTest {
	@SuppressWarnings("unchecked")
	public static <T> T[] collectionToArray(List<?> collection) {
		if (collection == null || collection.size() == 0) {
			throw new RuntimeException("集合为空或没有元素！");
		}
		T[] array = (T[]) Array.newInstance(collection.get(0).getClass(),
				collection.size());
		for (int i = 0; i < collection.size(); i++) {
			array[i] = (T) collection.get(i);
		}
		return array;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] collectionToArray(Collection<?> collection) {
		if (collection == null || collection.size() == 0) {
			throw new RuntimeException("集合为空或没有元素！");
		}
		
		T[] array = (T[]) Array.newInstance(collection.iterator().next().getClass(),
				collection.size());
		int i = 0;
		for (Object obj : collection) {
			array[i++] = (T) obj;
		}
		return array;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Collection<User> userList = new ArrayList<User>();
		userList.add(new User());
		userList.add(new User());
		User[] array = collectionToArray(userList);

		System.out.println(array.getClass().getName());

	}

}

class User {

}