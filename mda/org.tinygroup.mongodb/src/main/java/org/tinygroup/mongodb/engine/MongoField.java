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
package org.tinygroup.mongodb.engine;

import org.bson.BSONObject;
import org.tinygroup.context.Context;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.ObjectField;

/**
 * 
 * 功能说明: field类的包装
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-27 <br>
 * <br>
 */
public class MongoField {

	private Field field;

	private ObjectField objectField;

	private String fieldName;
	
	private boolean hasSetValue;

	public MongoField(String fieldName, Field field) {
		super();
		this.field = field;
		this.fieldName = fieldName;
	}
	
	public MongoField(String fieldName,ObjectField objectField){
		this.objectField = objectField;
		this.fieldName = fieldName;
	}

	public MongoField(String fieldName, Field field, ObjectField objectField) {
		super();
		this.field = field;
		this.objectField = objectField;
		this.fieldName = fieldName;
	}

	public Field getField() {
		return field;
	}

	public void setField(Field field) {
		this.field = field;
	}

	public ObjectField getObjectField() {
		return objectField;
	}

	public void setObjectField(ObjectField objectField) {
		this.objectField = objectField;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	
	public boolean isObjectIdField(){
		if(field!=null){
			return field.isObjectId();
		}
		return false;
	}

	public boolean isHasSetValue() {
		return hasSetValue;
	}

	public void setHasSetValue(boolean hasSetValue) {
		this.hasSetValue = hasSetValue;
	}
	
	public String getFieldDefaultValue(){
		return field.getDefaultValue();
	}

	public void fieldValueSet(int index, BSONObject bsonObject, Context context) {
		if(index==0){
			bsonObject.put(field.getName(), context.get(fieldName));
		}
		
		
        bsonObject.put(field.getName(), "");
        		
	}
	
}
