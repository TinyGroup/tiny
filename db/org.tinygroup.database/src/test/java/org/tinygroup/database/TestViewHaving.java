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
package org.tinygroup.database;

import org.tinygroup.database.config.view.Having;
import org.tinygroup.database.config.view.ViewFieldRef;
import org.tinygroup.database.config.view.ViewHaving;

import com.thoughtworks.xstream.XStream;

public class TestViewHaving {
	public static void main(String[] args) {
		XStream stream = new XStream();
		stream.autodetectAnnotations(true);
		stream.processAnnotations(ViewHaving.class);
		ViewHaving h = new ViewHaving();
		h.setOperator("=");
		h.setValue("'a'");
		Having having = new Having();
		Having having2 = new Having();
		
		ViewFieldRef field = new ViewFieldRef();
		field.setTableFieldId("a");
		field.setViewFieldId("a");
		field.setViewTableId("a");

		having.setField(field);
		having.setAggregateFunction("sum");
		
		ViewFieldRef field2 = new ViewFieldRef();
		field2.setTableFieldId("b");
		field2.setViewFieldId("b");
		field2.setViewTableId("b");
		having2.setField(field2);
		having2.setAggregateFunction("sum");
		
		h.setKeyHaving(having);
		h.setValueHaving(having2);
		System.out.println( stream.toXML(h));

	}
}
