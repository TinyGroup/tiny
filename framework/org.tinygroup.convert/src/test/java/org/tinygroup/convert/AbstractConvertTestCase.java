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
package org.tinygroup.convert;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractConvertTestCase extends TestCase {
	
	protected void setUp() throws Exception {
		super.setUp();
		
	}

	
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	protected Classes createClasses() {
		Classes classes =new Classes();
		List<Student> students=new ArrayList<Student>();
		Student student1=new Student();
		student1.setId(1);
		student1.setName("haha");
		student1.setAddress("address");
		student1.setEmail("email");
		Birthday birthday=new Birthday("2010-11-22");
		student1.setBirthday(birthday);
		Student student2=new Student();
		student2.setId(2);
		student2.setName("haha2");
		student2.setAddress("address2");
		student2.setEmail("email2");
		Birthday birthday2=new Birthday("2010-11-22");
		student2.setBirthday(birthday2);
		students.add(student1);
		students.add(student2);
		classes.setStudents(students);
		return classes;
	}
	
	protected Student createStudent() {
		Student student=new Student();
		student.setId(1);
		student.setName("haha");
		student.setAddress("address");
		student.setEmail("email");
		Birthday birthday=new Birthday("2010-11-22");
		student.setBirthday(birthday);
		return student;
	}
	protected Student createStudent1() {
		Student student=new Student();
		student.setId(2);
		student.setName("haha2");
		student.setAddress("address2");
		student.setEmail("email2");
		Birthday birthday=new Birthday("2010-11-23");
		student.setBirthday(birthday);
		return student;
	}
}
