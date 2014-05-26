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
package org.tinygroup.validate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.tinygroup.validate.annotation.AssertEquals;
import org.tinygroup.validate.annotation.AssertNotNull;
import org.tinygroup.validate.annotation.Field;
import org.tinygroup.validate.annotation.IntegerRange;
import org.tinygroup.validate.annotation.Pattern;
import org.tinygroup.validate.annotation.Size;
import org.tinygroup.validate.annotation.Validation;

@Validation(name = "user")
public class User {
	@Size(min = 10, max = 20)
	@AssertEquals("sfg")
	@AssertNotNull
	@Field(name = "name", title = "AAAA")
	String name;
	@Pattern(pattern = "^[A-Za-z0-9]+@([_A-Za-z0-9]+\\.)+[A-Za-z0-9]{2,3}$")
	@AssertNotNull
	@Field(name = "email", title = "BBBB")
	String email;
	@IntegerRange(min = 0, max = 120)
	@AssertNotNull
	@Field(name = "age", title = "CCCC")
	int age;
	@AssertNotNull
	@Field(name = "list", title = "DDDD")
	private List<String> strList;
	@AssertNotNull
	@Field(name = "map", title = "EEEE")
	private Map<String, String> strMap;
	@AssertNotNull
	@Field(name = "address", title = "FFFF")
	private Address address;
	@AssertNotNull
	@Field(name = "addList", title = "GGGG")
	private List<Address> adds;
	@AssertNotNull
	@Field(name = "addarray", title = "HHHH")
	private Address[] addressArray;
	@AssertNotNull
	@Field(name = "addset", title = "IIII")
	private Set<Address> addressSet;
	@AssertNotNull
	@Field(name = "addMap", title = "JJJJ")
	private Map<String,Address> addressMap;
	
	

	public Map<String, Address> getAddressMap() {
		return addressMap;
	}

	public void setAddressMap(Map<String, Address> addressMap) {
		this.addressMap = addressMap;
	}

	public Set<Address> getAddressSet() {
		return addressSet;
	}

	public void setAddressSet(Set<Address> addressSet) {
		this.addressSet = addressSet;
	}

	public Address[] getAddressArray() {
		return addressArray;
	}

	public void setAddressArray(Address[] addressArray) {
		this.addressArray = addressArray;
	}

	public List<Address> getAdds() {
		return adds;
	}

	public void setAdds(List<Address> adds) {
		this.adds = adds;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public List<String> getStrList() {
		return strList;
	}

	public void setStrList(List<String> strList) {
		this.strList = strList;
	}

	public Map<String, String> getStrMap() {
		return strMap;
	}

	public void setStrMap(Map<String, String> strMap) {
		this.strMap = strMap;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	

	public static void main(String[] args) throws SecurityException,
			NoSuchFieldException {

		java.lang.reflect.Field field = User.class.getDeclaredField("adds");
		java.lang.reflect.Field field1 = User.class.getDeclaredField("addressArray");
		System.out.println(field.isSynthetic());
		System.out.println(field.getDeclaringClass());
		System.out.println(field.getGenericType());
		System.out.println(field.getType().getTypeParameters());
		System.out.println(field.getType());
		System.out.println(field1.getType().isArray());
		System.out.println(field1.getType().getComponentType());
		

		System.out.println(isWrapClass(List.class));
		List<Address> addresses=new ArrayList<Address>();
		Address address=new Address();
		address.setName("武林门新村");
		addresses.add(address);
		

	}

	public static boolean isWrapClass(Class clz) {
		boolean isPrimitive = false;
		try {
			if (clz.isPrimitive() || clz.isAssignableFrom(String.class)) {
				isPrimitive = true;
			} else {
				isPrimitive = ((Class) clz.getField("TYPE").get(null))
						.isPrimitive();
			}
		} catch (Exception e) {
			isPrimitive = false;
		}
		return isPrimitive;
	}



}
