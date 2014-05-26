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
package org.tinygroup.tinydb.test.beanutil;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.util.TinyBeanUtil;

public class TinyBeanUtilTest  extends TestCase{

	public void testBean2Object(){
		Bean bean=new Bean("Classes");
		bean.setProperty("className", "class one");
		List<Student> students=new ArrayList<Student>();
		Student student1=new Student();
		student1.setAge("11");
		student1.setName("xiaoming");
		Student student2=new Student();
		student2.setAge("10");
		student1.setName("xiaoxuan");
		students.add(student1);
		students.add(student2);
		bean.setProperty("students", students);
		Student[] bests=new Student[2];
		bests[0]=student1;
		bests[1]=student2;
		bean.setProperty("bests", bests);
		Classes classes= TinyBeanUtil.bean2Object(bean, Classes.class);
		assertEquals("class one", classes.getClassName());
		assertEquals(students, classes.getStudents());
		assertEquals(bests, classes.getBests());
		
		
	}
	
	public void testObject2Bean(){
		Classes classes=new Classes();
		classes.setClassName("class one");
		List<Student> students=new ArrayList<Student>();
		Student student1=new Student();
		student1.setAge("11");
		student1.setName("xiaoming");
		Student student2=new Student();
		student2.setAge("10");
		student1.setName("xiaoxuan");
		students.add(student1);
		students.add(student2);
		classes.setStudents(students);
		Student[] bests=new Student[2];
		bests[0]=student1;
		bests[1]=student2;
		classes.setBests(bests);
		Bean bean=TinyBeanUtil.object2Bean(classes);
		assertEquals("Classes", bean.getType());
		assertEquals(students, bean.getProperty("students"));
		assertEquals(bests, bean.getProperty("bests"));
	}
	
	
	
	
	
	
}
