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

import java.util.ArrayList;
import java.util.List;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;


/**
 * 
 * 功能说明: 对象字段

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
@XStreamAlias("object-field")
public class ObjectField  extends BaseObject{
	@XStreamAlias("is-array")
	@XStreamAsAttribute
	private boolean isArray;
    @XStreamAsAttribute
    @XStreamAlias("array-size")
    private int arraySize;
    @XStreamImplicit
	private List<Field> fields;
    @XStreamImplicit
    private List<ObjectField> objectFields;
    
	public List<Field> getFields() {
		if(fields==null){
			fields=new ArrayList<Field>();
		}
		return fields;
	}
	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	public List<ObjectField> getObjectFields() {
		if(objectFields==null){
            objectFields=new ArrayList<ObjectField>();
		}
		return objectFields;
	}
	public void setObjectFields(List<ObjectField> objectFields) {
		this.objectFields = objectFields;
	}
	public int getArraySize() {
		return arraySize;
	}
	public void setArraySize(int arraySize) {
		this.arraySize = arraySize;
	}
	public boolean isArray() {
		return isArray;
	}
	public void setArray(boolean isArray) {
		this.isArray = isArray;
	}
	
    
}
