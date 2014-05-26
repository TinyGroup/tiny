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
package org.tinygroup.dao.util;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.persistence.Entity;

import org.tinygroup.commons.namestrategy.NameStrategy;
import org.tinygroup.commons.namestrategy.impl.CamelCaseStrategy;
import org.tinygroup.dao.query.QueryCondition;
import org.tinygroup.dao.query.QueryObject;

public class DaoUtil {
	public static String QueryObjectToHql(QueryObject obj,List<Object> params) {
//		String fieldsStr = getFields(obj);
		StringBuffer sb = new StringBuffer();
//		sb.append("select ");
//		sb.append(fieldsStr);
		sb.append(" from ").append(obj.getEntityName());
		if(obj.getConditions() == null){
			return sb.toString();
		}
		StringBuffer conditionSb = new StringBuffer();
		for (QueryCondition conditon : obj.getConditions()) {
			if (conditionSb.length() == 0) {
				conditionSb.append(getCondition(conditon, obj,params));
			} else {
				conditionSb.append(" and ").append(getCondition(conditon, obj,params));
			}
		}
		if(conditionSb.length()!=0){
			sb.append(" where ");
			sb.append(conditionSb);
		}
		return sb.toString();
	}

	private static String getCondition(QueryCondition condition, QueryObject obj,List<Object> params) {
		
		StringBuffer sb = new StringBuffer();
		sb.append("(");
//		sb.append(entityName).append(" ").append(condition.getKey());
		sb.append(getMDAName(condition.getKey()));
		
		sb.append(condition.getOperator().getOperator());
		if(!condition.getOperator().isSingleOperator()){
			sb.append(" ? ");
			params.add(condition.getValue());
		}
		
		//sb.append(entityName).append(".").append(condition.getValue());
		StringBuffer subSb = new StringBuffer();
		for (QueryCondition sub : condition.getConditions()) {
			if (subSb.length() == 0) {
				subSb.append(getCondition(sub, obj,params));
			} else {
				subSb.append(" and ").append(getCondition(sub, obj,params));
			}
		}
		if (subSb.length() != 0) {
			sb.append(" or (").append(subSb).append(")");
		}
		sb.append(")");
		return sb.toString();
	}
	public static NameStrategy getNameStrategy(){
		return new CamelCaseStrategy();
	}
	public static String getMDAName(String name){
		return getNameStrategy().getPropertyName(name);
	}
	public static String getDBName(String propertyName){
		return getNameStrategy().getFieldName(propertyName);
	}
	public static String getEntityName(Class<?> entityClass){
		Annotation annotation = entityClass.getAnnotation(Entity.class);
		try {
			String annotationValue = getAnnotationStringValue(annotation,Entity.class,"name");
			if(annotationValue==null||"".equals(annotationValue))
				return entityClass.getSimpleName();
			return annotationValue;
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private static String getAnnotationStringValue(Annotation annotation,
			Class<?> annotationClazz, String name)
			throws IllegalAccessException, InvocationTargetException,
			NoSuchMethodException {
		Object[] args = null;
		Class<?>[] argsType = null;
		return annotationClazz.getMethod(name, argsType)
				.invoke(annotation, args).toString();
	}
}
