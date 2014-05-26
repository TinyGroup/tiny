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
package org.tinygroup.mongodb.engine.view;

import java.util.ArrayList;
import java.util.List;

import org.bson.BSONObject;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.mongodb.common.MongoRelation;
import org.tinygroup.mongodb.common.RelationField;
import org.tinygroup.mongodb.db.MongodbPersistence;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.springutil.SpringUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * 
 * 功能说明:关系模型数据对象 

 * 开发人员: renhui <br>
 * 开发时间: 2013-12-5 <br>
 * <br>
 */
public class MongoRelationData {

	private MongoRelation relation;
	private String objectFieldName;
	private String sourcePropertyName;
	private String targetPropertyName;
	private MongodbPersistence persistence;
	private DBObject select;
	
	public MongoRelationData(String objectFieldName,String sourcePropertyName,String targetFieldId, MongoRelation relation) {
		this.relation = relation;
		this.objectFieldName=objectFieldName;
		this.sourcePropertyName=sourcePropertyName;
		ModelManager modelManager=SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
		MongoDBModel model=modelManager.getModel(relation.getRelationModelId());
		model.groups();
		this.targetPropertyName = model.getMongoFieldName(targetFieldId);
		select=new BasicDBObject();
		List<RelationField> fields=relation.getFields();
		for (RelationField relationField : fields) {
			String propertyName=model.getMongoFieldName(relationField.getFieldId());
			select.put(propertyName, 1);
		}
		persistence=new MongodbPersistence(relation.getRelationCollectionName());
	}

	public MongoRelation getRelation() {
		return relation;
	}
	public void setRelation(MongoRelation relation) {
		this.relation = relation;
	}
	public String getTargetPropertyName() {
		return targetPropertyName;
	}
	public void setTargetPropertyName(String targetPropertyName) {
		this.targetPropertyName = targetPropertyName;
	}
	public String getSourcePropertyName() {
		return sourcePropertyName;
	}
	public void setSourcePropertyName(String sourcePropertyName) {
		this.sourcePropertyName = sourcePropertyName;
	}
	
	
	public String getObjectFieldName() {
		return objectFieldName;
	}

	public void setObjectFieldName(String objectFieldName) {
		this.objectFieldName = objectFieldName;
	}

	public List<BSONObject> generateObjects(List<BSONObject> bsonObjects){
		List<BSONObject> objects=new ArrayList<BSONObject>();
		for (BSONObject bsonObject : bsonObjects) {
			if(objectFieldName!=null){//关联字段是否在嵌套对象中
				BasicDBList arrays= (BasicDBList) bsonObject.get(objectFieldName);
				if(arrays!=null){
					bsonObject.removeField(objectFieldName);//删除对象关联字段
					for (Object object : arrays) {
						BSONObject basicDBObject=(BSONObject)object;
						DBObject where=new BasicDBObject();
						where.put(targetPropertyName, basicDBObject.get(sourcePropertyName));
						BSONObject[] targetObjects=persistence.find(select, where);
						for (BSONObject targetObject : targetObjects) {
							BSONObject newObject=new BasicDBObject();
							newObject.putAll(bsonObject);
							newObject.putAll(basicDBObject);
							newObject.putAll(targetObject);
							objects.add(newObject);
						}
						
					}
				}
			}else{
				DBObject where=new BasicDBObject();
				where.put(targetPropertyName, bsonObject.get(sourcePropertyName));
				BSONObject[] targetObjects=persistence.find(select, where);
				for (BSONObject targetObject : targetObjects) {
					BSONObject newObject=new BasicDBObject();
					newObject.putAll(bsonObject);
					newObject.putAll(targetObject);
					objects.add(newObject);
				}
			}
		}
		return objects;
	}
	
}
