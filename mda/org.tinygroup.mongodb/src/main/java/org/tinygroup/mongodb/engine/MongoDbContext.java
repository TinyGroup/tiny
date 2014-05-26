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

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.commons.cryptor.Cryptor;
import org.tinygroup.commons.cryptor.DefaultCryptor;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.context.Context;
import org.tinygroup.imda.ModelManager;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.mongodb.common.ConditionField;
import org.tinygroup.mongodb.common.Field;
import org.tinygroup.mongodb.common.ObjectField;
import org.tinygroup.mongodb.db.MongodbPersistence;
import org.tinygroup.mongodb.engine.comparemode.MongoCompareMode;
import org.tinygroup.mongodb.engine.comparemode.MongoCompareModeContain;
import org.tinygroup.mongodb.model.MongoDBModel;
import org.tinygroup.mongodb.util.ModelUtil;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.springutil.TypeConverterUtil;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryOperators;

/**
 * 
 * 功能说明: mongodb上下文对象，可以进行参数组装以及mongdb调用所需的一系列对象。
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-11-25 <br>
 * <br>
 */
public class MongoDbContext {

	public static final String COMPARE_MODE = "CompareMode";
	/**
	 * LengthEqualsCompareMode比较模式字段值长度参数名称的后缀
	 */
	public static final String LEVEL_LENGTH = "LevelLength";

	public static final String HAVING_FIELD = "HavingField";

	public static final String HAVING_COMPARE_MODE = "HavingCompareMode";

	public static final String OR = "or";

	public static final String DESC = "desc";

	public static final String TINY_LENGTH = "TINY_LENGTH";

	protected MongoDBModel model;

	protected Context context;

	private MongoCompareModeContain contain;

	protected MongodbPersistence persistence;
	
	protected Cryptor cryptor = new DefaultCryptor();
	
	protected Logger logger=LoggerFactory.getLogger(MongoDbContext.class);

	public MongoDbContext(MongoDBModel model, Context context) {
		super();
		this.model = model;
		this.context = context;
		init(model);
		contain = SpringUtil
				.getBean(MongoCompareModeContain.COMPARE_MODE_CONTAIN);
		persistence = new MongodbPersistence(model.getName());
	}

	public void init(MongoDBModel model) {
		//加载继承模型
		String parentModelId=model.getParentModelId();
		if(!StringUtil.isBlank(parentModelId)){
			ModelManager modelManager=SpringUtil.getBean(ModelManager.MODELMANAGER_BEAN);
			MongoDBModel parentModel=modelManager.getModel(parentModelId);
			if(parentModel!=null){
				model.fieldMap(parentModel.getGroups());
			}
		}
		model.groups();//加载自己的
	}

	public Field getField(String fieldId) {
		return model.getField(fieldId);
	}

	public String getMongoFieldName(String fieldId) {
		return model.getMongoFieldName(fieldId);
	}
	
	public String getFieldName(String fieldId) {
		return model.getFieldName(fieldId);
	}

	public MongoField getMongoField(String fieldId) {
		return model.getMongoField(fieldId);
	}

	public ObjectField getObjectField(String fieldId) {
		return model.getObjectField(fieldId);
	}

	public boolean isCollectionField(String fieldId) {
		Field field = getField(fieldId);
		if (field != null) {
			return field.isCollectionField();
		}
		return false;
	}

	/**
	 * 
	 * 只组装条件字段参数
	 * 
	 * @return
	 */
	public Context buildConditionFieldParamter(
			List<ConditionField> conditionFields, Context newContext) {
		if (conditionFields != null) {
			for (ConditionField conditionField : conditionFields) {
				processConditionParamter(newContext, conditionField);
			}
		}
		return newContext;
	}

	private void processConditionParamter(Context newContext,
			ConditionField conditionField) {
		MongoField mongoField = getMongoField(conditionField.getFieldId());
		String propertyName = mongoField.getFieldName();
		Field field=mongoField.getField();
		Object value = context.get(propertyName);
		if(value==null){
			value=context.get(field.getName());
		}
		if (value == null) {
			if (conditionField.isHidden()) {
				value = mongoField.getFieldDefaultValue();
			}
		}
		if (value != null) {
			newContext.put(propertyName, TypeConverterUtil.typeConverter(value, field.getDataType()));
		}
		String compareParamterName = ModelUtil.getSpliceParamterName(
				propertyName, COMPARE_MODE);
		Object compareModeValue = context.get(compareParamterName);
		if (compareModeValue != null) {
			newContext.put(compareParamterName, compareModeValue);
		}
		String lengthParamterName = ModelUtil.getSpliceParamterName(
				propertyName, LEVEL_LENGTH);
		Object lengthValue = context.get(lengthParamterName);
		if (lengthValue != null) {
			newContext.put(lengthParamterName, lengthValue);
		}
	}

	public DBObject generateConditionObject(List<ConditionField> conditionFields) {
		DBObject conditionObject=new BasicDBObject();
		List<ConditionField> orLists = new ArrayList<ConditionField>();
		List<ConditionField> andLists = new ArrayList<ConditionField>();
		for (ConditionField conditionField : conditionFields) {
			if (isCollectionField(conditionField.getFieldId())) {
				if (conditionField.getConnectMode().equalsIgnoreCase(OR)) {
					orLists.add(conditionField);
				} else {
					andLists.add(conditionField);
				}

			}
		}
		if (!CollectionUtil.isEmpty(andLists)) {
			BasicDBList andList = new BasicDBList();
			for (ConditionField conditionField : andLists) {
				addCondition(andList, conditionField);
			}
			if(andList.size()>0){
				conditionObject.put(QueryOperators.AND, andList);
			}
		}
		
		if (!CollectionUtil.isEmpty(orLists)) {
			BasicDBList list = new BasicDBList();
			for (ConditionField conditionField : orLists) {
				addCondition(list, conditionField);
			}
			if(list.size()>0){
				conditionObject.put(QueryOperators.OR, list);
			}
			
		}
		return conditionObject;

	}

	private void addCondition(BasicDBList list, ConditionField conditionField) {
		MongoField mongoField = getMongoField(conditionField.getFieldId());
		String propertyName = mongoField.getFieldName();
		String compareModeStr = checkCompareMode(
				conditionField.getCompareMode(), propertyName);
		MongoCompareMode compareMode = getCompareMode(compareModeStr);
		Object value = context.get(propertyName);
		if (compareMode != null) {
			if (value != null || !compareMode.needValue()) {
				list.add(compareMode.generateBSONObject(
						propertyName, value, context));
			}
		} else {
			  throw new RuntimeException("comparemode not found");
		}
	}

	protected String checkCompareMode(String defaultCompareMode,
			String propertyName) {
		String compareMode = context.get(ModelUtil.getSpliceParamterName(
				propertyName, COMPARE_MODE));
		if (compareMode == null) {
			compareMode = defaultCompareMode;
		}
		return compareMode;
	}

	public MongoCompareMode getCompareMode(String compareModeName) {
		return contain.getCompareMode(compareModeName);
	}

}
