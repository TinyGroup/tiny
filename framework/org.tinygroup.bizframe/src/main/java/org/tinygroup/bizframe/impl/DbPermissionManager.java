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
package org.tinygroup.bizframe.impl;

import java.util.ArrayList;
import java.util.List;

import org.tinygroup.bizframe.PermissionObject;
import org.tinygroup.bizframe.PermissionSubject;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.util.TinyBeanUtil;

/**
 * 
 * 功能说明: 权限管理的数据库存储实现
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2013-7-31 <br>
 * <br>
 */
@SuppressWarnings("rawtypes")
public class DbPermissionManager<K extends Comparable<K>> extends
		AbstractPermissionManager<K> {
	private static final String PARENT_ID = "parentId";

	/**
	 * 关联表状态字段 1、允许 0、禁止
	 */
	private static final String STATUS = "status";

	private static final String ALLOW = "1";

	private static final String BLOCK = "0";
	/**
	 * tinydb 数据库操作管理类
	 */
	private BeanOperatorManager manager;

	// /**
	// * 缓存起来，不用每次从管理器中获取
	// */
	// private Map<String,DBOperator> operatorMap=new HashMap<String,
	// DBOperator>();

	public BeanOperatorManager getManager() {
		if (manager == null) {
			manager = SpringUtil.getBean("beanOperatorManager");
		}
		return manager;
	}

	public void setManager(BeanOperatorManager manager) {
		this.manager = manager;
	}

	/**
	 * 保存权限主体信息到数据库中
	 */
	@SuppressWarnings("unchecked")
	public PermissionSubject addPermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionSubject.getType());
		Bean bean = operator.insert(TinyBeanUtil.object2Bean(permissionSubject,
				new Bean(permissionSubject.getType())));
		PermissionSubject subject = TinyBeanUtil.bean2Object(bean,
				permissionSubject.getClass());
		String primaryKeyName = getPrimaryKeyName(permissionSubject.getType());
		subject.setId(String.valueOf(bean.getProperty(operator
				.getBeanDbNameConverter().dbFieldNameToPropertyName(
						primaryKeyName))));
		return subject;

	}

	/**
	 * 保存权限客体到数据库中
	 */
	@SuppressWarnings("unchecked")
	public PermissionObject<K, ?> addPermissionObject(
			PermissionObject<K, ?> permissionObject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionObject.getType());
		Bean bean = operator.insert(TinyBeanUtil.object2Bean(permissionObject,
				new Bean(permissionObject.getType())));
		PermissionObject<K, ?> object = TinyBeanUtil.bean2Object(bean,
				permissionObject.getClass());
		String primaryKeyName = getPrimaryKeyName(permissionObject.getType());
		object.setId((K) bean.getProperty(operator.getBeanDbNameConverter()
				.dbFieldNameToPropertyName(primaryKeyName)));
		return object;
	}

	public void removePermissionObject(PermissionObject<K, ?> permissionObject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionObject.getType());
		operator.delete(TinyBeanUtil.object2Bean(permissionObject, new Bean(
				permissionObject.getType())));

	}

	public void removePermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionSubject.getType());
		operator.delete(TinyBeanUtil.object2Bean(permissionSubject, new Bean(
				permissionSubject.getType())));
	}

	@SuppressWarnings("unchecked")
	public PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue, Class<? extends PermissionSubject> subjectClassType) {
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				subjectBeanType);
		Bean bean = operator.getBean(keyValue);
		String primaryKeyName = getPrimaryKeyName(subjectBeanType);
		PermissionSubject permissionSubject = (PermissionSubject) TinyBeanUtil
				.bean2Object(bean, subjectClassType);
		permissionSubject.setId(getPrimaryKeyValue(operator, bean,
				primaryKeyName));
		return permissionSubject;
	}

	@SuppressWarnings("unchecked")
	public PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue, Class<? extends PermissionObject> objectClassType) {
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				objectBeanType);
		Bean bean = operator.getBean(keyValue);
		String primaryKeyName = getPrimaryKeyName(objectBeanType);
		PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
				.bean2Object(bean, objectClassType);
		permissionObject.setId(getPrimaryKeyValue(operator, bean,
				primaryKeyName));
		return permissionObject;
	}

	/**
	 * 
	 * 获取主键值
	 * 
	 * @param operator
	 * @param bean
	 * @param primaryKeyName
	 * @return
	 */
	private String getPrimaryKeyValue(DBOperator<K> operator, Bean bean,
			String primaryKeyName) {
		return String.valueOf(bean.getProperty(operator
				.getBeanDbNameConverter().dbFieldNameToPropertyName(
						primaryKeyName)));
	}

	public boolean isBlockDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		RelationTempObject relation = createRelationTempObject(BLOCK,
				permissionSubject.getType(), permissionSubject.getId(),
				permissionObject.getType(), permissionObject.getId());
		Bean[] beans = getManager().getDbOperator(relation.getRelationType())
				.getBeans(relation.getBean());
		if (beans != null && beans.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isAllowDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		RelationTempObject relation = createRelationTempObject(ALLOW,
				permissionSubject.getType(), permissionSubject.getId(),
				permissionObject.getType(), permissionObject.getId());
		Bean[] beans = getManager().getDbOperator(relation.getRelationType())
				.getBeans(relation.getBean());
		if (beans != null && beans.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isBlockDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(BLOCK,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		Bean[] beans = getManager().getDbOperator(relation.getRelationType())
				.getBeans(relation.getBean());
		if (beans != null && beans.length > 0) {
			return true;
		}
		return false;
	}

	public boolean isAllowDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(ALLOW,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		Bean[] beans = getManager().getDbOperator(relation.getRelationType())
				.getBeans(relation.getBean());
		if (beans != null && beans.length > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 为权限主体与客体绑定关联关系
	 */
	public void addAllowPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		addRelationInfo(ALLOW, permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	public void addAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = createRelationTempObject(ALLOW,
				permissionSubjectList, permissionObjectList);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).batchInsert(
					relation.getBeans());
		}

	}

	public void removeAllowPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		deleteRelationInfo(ALLOW, permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	public void removeAllowPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = createRelationTempObject(ALLOW,
				permissionSubjectList, permissionObjectList);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).batchDelete(
					relation.getBeans());
		}

	}

	public void addAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		addRelationInfo(ALLOW, permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
	}

	public void removeAllowPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		deleteRelationInfo(ALLOW, permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
	}

	public void addBlockPermission(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		addRelationInfo(BLOCK, permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	public void addBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = createRelationTempObject(BLOCK,
				permissionSubjectList, permissionObjectList);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).batchInsert(
					relation.getBeans());
		}

	}

	public void removeBlockPermission(
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		deleteRelationInfo(BLOCK, permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());

	}

	public void removeBlockPermission(
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = createRelationTempObject(BLOCK,
				permissionSubjectList, permissionObjectList);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).batchDelete(
					relation.getBeans());
		}

	}

	public void addBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		addRelationInfo(BLOCK, permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);

	}

	public void removeBlockPermission(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		deleteRelationInfo(BLOCK, permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);

	}

	private String getRelationType(String permissionSubjectType,
			String permissionObjectType) {
		return permissionSubjectType + permissionObjectType;
	}

	private String getPrimaryKeyName(String beanType) {
		TableConfiguration configuration = getManager()
				.getTableConfiguration(beanType);
		if (configuration != null) {
			return configuration.getPrimaryKey().getColumnName();
		}
		throw new DBRuntimeException("tinydb.notExistPrimaryField", beanType);
	}

	private void addRelationInfo(String status, String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(status,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).insert(
					relation.getBean());
		}
	}

	private void deleteRelationInfo(String status,
			String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(status,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (relation.existRelation()) {
			getManager().getDbOperator(relation.getRelationType()).delete(
					relation.getBean());
		}
	}

	/**
	 * 
	 * 创建权限主体与客体关联关系的bean
	 * 
	 * @param status
	 * @param permissionSubject
	 * @param permissionObject
	 * @param relationType
	 * @return
	 */
	private RelationTempObject createRelationTempObject(String status,
			String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		RelationTempObject relation = new RelationTempObject();
		String relationType = getRelationType(permissionSubjectType,
				permissionObjectType);
		Bean relationBean = new Bean(relationType);
		String subjectPrimaryKeyName = getPrimaryKeyName(permissionSubjectType);
		DBOperator subjectOperator = getManager().getDbOperator(
				permissionSubjectType);
		relationBean.setProperty(
				getPrimaryPropertyName(subjectPrimaryKeyName, subjectOperator),
				permissionSubjectId);
		String objectPrimaryKeyName = getPrimaryKeyName(permissionObjectType);
		DBOperator objectOperator = getManager().getDbOperator(
				permissionObjectType);
		relationBean.setProperty(
				getPrimaryPropertyName(objectPrimaryKeyName, objectOperator),
				permissionObjectId);
		relationBean.setProperty(STATUS, status);
		relation.setBean(relationBean);
		relation.setRelationType(relationType);
		return relation;
	}

	/**
	 * 
	 * 创建权限主体与客体关联关系的bean
	 * 
	 * @param status
	 * @param permissionSubject
	 * @param permissionObject
	 * @param relationType
	 * @return
	 */
	private RelationTempObject createRelationTempObject(String status,
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = new RelationTempObject();
		List<Bean> beans = new ArrayList<Bean>();
		if (permissionSubjectList.size() > 0 && permissionObjectList.size() > 0) {
			for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
				for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
					beans.add(createRelationBean(status, permissionSubject,
							permissionObject));
				}
			}
			relation.setBeans(beans);
			String relationType = getRelationType(permissionSubjectList.get(0)
					.getType(), permissionObjectList.get(0).getType());
			relation.setRelationType(relationType);
		}
		return relation;
	}

	private Bean createRelationBean(String status,
			PermissionSubject permissionSubject,
			PermissionObject permissionObject) {
		String relationType = getRelationType(permissionSubject.getType(),
				permissionObject.getType());
		Bean relationBean = new Bean(relationType);
		String subjectPrimaryKeyName = getPrimaryKeyName(permissionSubject
				.getType());
		DBOperator subjectOperator = getManager().getDbOperator(
				permissionSubject.getType());
		relationBean.setProperty(
				getPrimaryPropertyName(subjectPrimaryKeyName, subjectOperator),
				permissionSubject.getId());
		String objectPrimaryKeyName = getPrimaryKeyName(permissionObject
				.getType());
		DBOperator objectOperator = getManager().getDbOperator(
				permissionObject.getType());
		relationBean.setProperty(
				getPrimaryPropertyName(objectPrimaryKeyName, objectOperator),
				permissionObject.getId());
		relationBean.setProperty(STATUS, status);
		return relationBean;
	}

	private class RelationTempObject {
		List<Bean> beans;
		String relationType;
		Bean bean;

		public List<Bean> getBeans() {
			return beans;
		}

		public void setBeans(List<Bean> beans) {
			this.beans = beans;
		}

		public String getRelationType() {
			return relationType;
		}

		public void setRelationType(String relationType) {
			this.relationType = relationType;
		}

		public boolean existRelation() {
			return relationType != null;
		}

		public Bean getBean() {
			return bean;
		}

		public void setBean(Bean bean) {
			this.bean = bean;
		}

	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K,?>> getPermissionObjects(
			String objectBeanType,
			Class<? extends PermissionObject> objectClassType) {
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				objectBeanType);
		Bean[] objectBeans = operator.getBeans(new Bean(objectBeanType));
		List<PermissionObject<K,?>> permissionObjects = new ArrayList<PermissionObject<K,?>>();
		String primaryKeyName = getPrimaryKeyName(objectBeanType);
		if(objectBeans!=null){
			for (Bean bean : objectBeans) {
				PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
						.bean2Object(bean, objectClassType);
				permissionObject.setId(getPrimaryKeyValue(operator, bean,
						primaryKeyName));
				permissionObjects.add(permissionObject);
			}	
		}
		return permissionObjects;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionSubject<K,?>> getPermissionSubjects(
			String subjectBeanType,
			Class<? extends PermissionSubject> subjectClassType) {
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				subjectBeanType);
		Bean[] subjectBeans = operator.getBeans(new Bean(subjectBeanType));
		List<PermissionSubject<K,?>> permissionSubjects = new ArrayList<PermissionSubject<K,?>>();
		String primaryKeyName = getPrimaryKeyName(subjectBeanType);
		if(subjectBeans!=null){
			for (Bean bean : subjectBeans) {
				PermissionSubject permissionSubject = (PermissionSubject) TinyBeanUtil
						.bean2Object(bean, subjectClassType);
				permissionSubject.setId(getPrimaryKeyValue(operator, bean,
						primaryKeyName));
				permissionSubjects.add(permissionSubject);
			}
		}
		return permissionSubjects;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K,?>> getAssignedPermission(
			String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType) {
		
		Bean[] beans = getRelationBeans(subjectBeanType, objectBeanType,
				permissionSubjectId);
		DBOperator objectOperator = getManager()
				.getDbOperator(objectBeanType);
		String objectPrimaryKeyName = getPrimaryKeyName(objectBeanType);
		List<PermissionObject<K,?>> permissionObjects=new ArrayList<PermissionObject<K,?>>();
		if(beans!=null){
			for (Bean bean : beans) {
				Bean objectBean= bean.getProperty(objectBeanType);
				if(objectBean!=null){
					PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
							.bean2Object(objectBean, objectClassType);
					permissionObject.setId(getPrimaryKeyValue(objectOperator, objectBean,
							objectPrimaryKeyName));
					permissionObjects.add(permissionObject);
				}
			}
		}
		return permissionObjects;
	}
	
	@SuppressWarnings("unchecked")
	private Bean[] getRelationBeans(String subjectBeanType,
			String objectBeanType, K permissionSubjectId) {
		String relationBeanType = getRelationType(subjectBeanType,
				objectBeanType);
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				relationBeanType);
//		operator.setRelation(getManager().getRelationByBeanType(relationBeanType));
		Bean relationBean = new Bean(relationBeanType);
		String subjectPrimaryKeyName = getPrimaryKeyName(subjectBeanType);
		DBOperator subjectOperator = getManager()
				.getDbOperator(subjectBeanType);
		relationBean.setProperty(
				getPrimaryPropertyName(subjectPrimaryKeyName, subjectOperator),
				permissionSubjectId);
		Bean[] beans=operator.getBeans(relationBean);
		return beans;
	}

	private String getPrimaryPropertyName(String primaryKeyName,
			DBOperator operator) {
		return operator.getBeanDbNameConverter()
				.dbFieldNameToPropertyName(primaryKeyName);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionSubject<K,?>> getChildPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		
		Bean queryBean=new Bean(subjectBeanType);
		queryBean.setProperty(PARENT_ID, parentSubjectId);
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				subjectBeanType);
		Bean[] beans=operator.getBeans(queryBean);
		List<PermissionSubject<K,?>> children = new ArrayList<PermissionSubject<K,?>>();
		if(beans!=null){
			String primaryKeyName = getPrimaryKeyName(subjectBeanType);
			for (Bean bean : beans) {
				PermissionSubject permissionSubject = (PermissionSubject) TinyBeanUtil
						.bean2Object(bean, subjectClassType);
				permissionSubject.setId(getPrimaryKeyValue(operator, bean,
						primaryKeyName));
				children.add(permissionSubject);
			}
		}
		return children;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K,?>> getChildPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		Bean queryBean=new Bean(objectBeanType);
		queryBean.setProperty(PARENT_ID, parentObjectId);
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				objectBeanType);
		Bean[] beans=operator.getBeans(queryBean);
		List<PermissionObject<K,?>> children = new ArrayList<PermissionObject<K,?>>();
		if(beans!=null){
			String primaryKeyName = getPrimaryKeyName(objectBeanType);
			for (Bean bean : beans) {
				PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
						.bean2Object(bean, objectClassType);
				permissionObject.setId(getPrimaryKeyValue(operator, bean,
						primaryKeyName));
				children.add(permissionObject);
			}
		}
		return children;
	}

	public List<PermissionSubject<K, ?>> getBegatsPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return null;
	}

	public List<PermissionObject<K, ?>> getBegatsPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return null;
	}

	public PermissionSubject<K, ?> getChildPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return null;
	}

	public PermissionSubject<K, ?> getBegatsPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		return null;
	}

	public PermissionObject<K, ?> getChildPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return null;
	}

	public PermissionObject<K, ?> getBegatsPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		return null;
	}

}
