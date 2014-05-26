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
 * 功能说明: 模型概念的权限管理数据库存储实现，权限关联表名：auth_relation
 * <p/>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-7-31 <br>
 * <br>
 */
@SuppressWarnings("rawtypes")
public class DbModelPermissionManager<K extends Comparable<K>> extends
		AbstractPermissionManager<K> {
	/**
	 * 权限关联关系表的beanType
	 */
	private static final String AUTH_RELATION_BEAN_NAME = "authRelation";
	private static final String PERMISSION_SUBJECT_ID = "subjectId";
	private static final String PERMISSION_OBJECT_ID = "objectId";
	private static final String PERMISSION_SUBJECT_TYPE = "subjectType";
	private static final String PERMISSION_OBJECT_TYPE = "objectType";
	private static final String PERMISSION_STATUS = "status";
	private static final String PARENT_ID = "parentId";

	private PermissionStorage<K> storage = new PermissionStorage<K>();

	/**
	 * 关联表状态字段 1、允许 0、禁止
	 */

	public static final String ALLOW = "1";

	public static final String BLOCK = "0";
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

	public void setPermissionSubjectInheritSupport(boolean support) {
		super.setPermissionSubjectInheritSupport(support);
		storage.setInheritSupport(support);
	}

	@SuppressWarnings("unchecked")
	public void init() {

		if (cacheSupport && existTable()) {
			Bean relation = new Bean(AUTH_RELATION_BEAN_NAME);
			DBOperator<?> operator = getManager().getDbOperator(
					AUTH_RELATION_BEAN_NAME);
			Bean[] beans = operator.getBeans(relation);
			for (Bean bean : beans) {
				PermissionSubject<K, ?> permissionSubject = getPermissionSubject((String)
						bean.getProperty(PERMISSION_SUBJECT_TYPE),
						(K) bean.getProperty(PERMISSION_SUBJECT_ID),
						PermissionSubject.class);
				PermissionObject<K, ?> permissionObject = getPermissionObject((String)
						bean.getProperty(PERMISSION_OBJECT_TYPE),
						(K) bean.getProperty(PERMISSION_OBJECT_ID),
						PermissionObject.class);
				addAllowPermission(permissionSubject, permissionObject);
			}
		}
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
		if (cacheSupport) {// 支持缓存
			storage.addPermissionSubject(permissionSubject);
		}
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
		if (cacheSupport) {
			storage.addPermissionObject(permissionObject);
		}
		return object;
	}

	public void removePermissionObject(PermissionObject<K, ?> permissionObject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionObject.getType());
		operator.delete(TinyBeanUtil.object2Bean(permissionObject, new Bean(
				permissionObject.getType())));
		if (cacheSupport) {
			storage.removePermissionObject(permissionObject);
		}

	}

	public void removePermissionSubject(
			PermissionSubject<K, ?> permissionSubject) {
		DBOperator<?> operator = getManager().getDbOperator(
				permissionSubject.getType());
		operator.delete(TinyBeanUtil.object2Bean(permissionSubject, new Bean(
				permissionSubject.getType())));
		if (cacheSupport) {
			storage.removePermissionSubject(permissionSubject);
		}
	}

	@SuppressWarnings("unchecked")
	public PermissionSubject<K, ?> getPermissionSubject(String subjectBeanType,
			K keyValue, Class<? extends PermissionSubject> subjectClassType) {
		PermissionSubject permissionSubject = null;
		if (cacheSupport) {
			permissionSubject = storage.getPermissionSubject(subjectBeanType,
					keyValue, subjectClassType);
		}
		if (permissionSubject == null) {
			DBOperator<K> operator = (DBOperator<K>) getManager()
					.getDbOperator(subjectBeanType);
			Bean bean = operator.getBean(keyValue);
			String primaryKeyName = getPrimaryKeyName(subjectBeanType);
			permissionSubject = (PermissionSubject) TinyBeanUtil.bean2Object(
					bean, subjectClassType);
			permissionSubject.setId(getPrimaryKeyValue(operator, bean,
					primaryKeyName));
		}
		return permissionSubject;
	}

	@SuppressWarnings("unchecked")
	public PermissionObject<K, ?> getPermissionObject(String objectBeanType,
			K keyValue, Class<? extends PermissionObject> objectClassType) {
		PermissionObject permissionObject = null;
		if (cacheSupport) {
			permissionObject = storage.getPermissionObject(objectBeanType,
					keyValue, objectClassType);
		}
		if (permissionObject == null) {
			DBOperator<K> operator = (DBOperator<K>) getManager()
					.getDbOperator(objectBeanType);
			Bean bean = operator.getBean(keyValue);
			String primaryKeyName = getPrimaryKeyName(objectBeanType);
			permissionObject = (PermissionObject) TinyBeanUtil.bean2Object(
					bean, objectClassType);
			permissionObject.setId(getPrimaryKeyValue(operator, bean,
					primaryKeyName));
		}
		return permissionObject;
	}

	/**
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
		return isBlockDirectly(permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	public boolean isAllowDirectly(PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return isAllowDirectly(permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	public boolean isBlockDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(BLOCK,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (relation.existTable()) {
			if (cacheSupport) {
				PermissionSubject<K, ?> permissionSubject = storage
						.getPermissionSubject(permissionSubjectType,
								permissionSubjectId);
				PermissionObject<K, ?> permissionObject = storage
						.getPermissionObject(permissionObjectType,
								permissionObjectId);
				if (permissionSubject != null && permissionObject != null) {
					return storage.isBlockDirectly(permissionSubject,
							permissionObject);
				}
			}
			Bean[] beans = getManager().getDbOperator(
					relation.getRelationType()).getBeans(relation.getBean());
			if (beans != null && beans.length > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isAllowDirectly(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(ALLOW,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (relation.existTable()) {
			if (cacheSupport) {
				PermissionSubject<K, ?> permissionSubject = storage
						.getPermissionSubject(permissionSubjectType,
								permissionSubjectId);
				PermissionObject<K, ?> permissionObject = storage
						.getPermissionObject(permissionObjectType,
								permissionObjectId);
				if (permissionSubject != null && permissionObject != null) {
					return storage.isAllowDirectly(permissionSubject,
							permissionObject);
				}
			}
			Bean[] beans = getManager().getDbOperator(
					relation.getRelationType()).getBeans(relation.getBean());
			if (beans != null && beans.length > 0) {
				return true;
			}
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
		addRelationInfos(ALLOW, permissionSubjectList, permissionObjectList);
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
		deleteRelationInfos(ALLOW, permissionSubjectList, permissionObjectList);

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
		addRelationInfos(BLOCK, permissionSubjectList, permissionObjectList);

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
		deleteRelationInfos(BLOCK, permissionSubjectList, permissionObjectList);
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
		if (relation.existTable()) {
			getManager().getDbOperator(relation.getRelationType()).insert(
					relation.getBean());
			if (cacheSupport) {
				PermissionSubject<K, ?> permissionSubject = storage
						.getPermissionSubject(permissionSubjectType,
								permissionSubjectId);
				PermissionObject<K, ?> permissionObject = storage
						.getPermissionObject(permissionObjectType,
								permissionObjectId);
				if (permissionSubject != null && permissionObject != null) {
					if (status.equals(ALLOW)) {
						storage.addAllowPermission(permissionSubject,
								permissionObject);
					} else if (status.equals(BLOCK)) {
						storage.addBlockPermission(permissionSubject,
								permissionObject);
					}
				}
			}
		}
	}

	private void addRelationInfos(String status,
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = new RelationTempObject();
		relation.setRelationType(AUTH_RELATION_BEAN_NAME);
		if (relation.existTable()) {
			List<Bean> beans = new ArrayList<Bean>();
			if (permissionSubjectList.size() > 0
					&& permissionObjectList.size() > 0) {
				for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
					for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
						Bean bean = createRelationBean(status,
								permissionSubject, permissionObject);
						beans.add(bean);
						if (cacheSupport) {
							if (permissionSubject != null
									&& permissionObject != null) {
								if (status.equals(ALLOW)) {
									storage.addAllowPermission(
											permissionSubject, permissionObject);
								} else if (status.equals(BLOCK)) {
									storage.addBlockPermission(
											permissionSubject, permissionObject);
								}
							}
						}
					}
				}
				relation.setBeans(beans);
				getManager().getDbOperator(relation.getRelationType())
						.batchInsert(beans);
			}
		}
	}

	private void deleteRelationInfos(String status,
			List<PermissionSubject<K, ?>> permissionSubjectList,
			List<PermissionObject<K, ?>> permissionObjectList) {
		RelationTempObject relation = new RelationTempObject();
		relation.setRelationType(AUTH_RELATION_BEAN_NAME);
		if (relation.existTable()) {
			List<Bean> beans = new ArrayList<Bean>();
			if (permissionSubjectList.size() > 0
					&& permissionObjectList.size() > 0) {
				for (PermissionSubject<K, ?> permissionSubject : permissionSubjectList) {
					for (PermissionObject<K, ?> permissionObject : permissionObjectList) {
						Bean bean = createRelationBean(status,
								permissionSubject, permissionObject);
						beans.add(bean);
						if (cacheSupport) {
							if (permissionSubject != null
									&& permissionObject != null) {
								if (status.equals(ALLOW)) {
									storage.removeAllowPermission(
											permissionSubject, permissionObject);
								} else if (status.equals(BLOCK)) {
									storage.removeBlockPermission(
											permissionSubject, permissionObject);
								}
							}
						}
					}
				}
				relation.setBeans(beans);
				getManager().getDbOperator(relation.getRelationType())
						.batchDelete(beans);
			}
		}
	}

	private void deleteRelationInfo(String status,
			String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		RelationTempObject relation = createRelationTempObject(status,
				permissionSubjectType, permissionSubjectId,
				permissionObjectType, permissionObjectId);
		if (relation.existTable()) {
			getManager().getDbOperator(relation.getRelationType()).delete(
					relation.getBean());
			if (cacheSupport) {
				PermissionSubject<K, ?> permissionSubject = storage
						.getPermissionSubject(permissionSubjectType,
								permissionSubjectId);
				PermissionObject<K, ?> permissionObject = storage
						.getPermissionObject(permissionObjectType,
								permissionObjectId);
				if (permissionSubject != null && permissionObject != null) {
					if (status.equals(ALLOW)) {
						storage.removeAllowPermission(permissionSubject,
								permissionObject);
					} else if (status.equals(BLOCK)) {
						storage.removeBlockPermission(permissionSubject,
								permissionObject);
					}
				}
			}
		}
	}

	/**
	 * 创建权限主体与客体关联关系的bean
	 * 
	 * @param status
	 * @param permissionSubjectType
	 * @param permissionSubjectId
	 * @param permissionObjectType
	 * @param permissionObjectId
	 * @return
	 */
	private RelationTempObject createRelationTempObject(String status,
			String permissionSubjectType, K permissionSubjectId,
			String permissionObjectType, K permissionObjectId) {
		RelationTempObject relation = new RelationTempObject();
		Bean relationBean = newRelationBean(status, permissionSubjectType,
				permissionSubjectId, permissionObjectType, permissionObjectId);
		relation.setBean(relationBean);
		relation.setRelationType(AUTH_RELATION_BEAN_NAME);
		return relation;
	}

	/**
	 * 创建权限关联bean实例
	 * 
	 * @param status
	 * @param permissionSubjectType
	 * @param permissionSubjectId
	 * @param permissionObjectType
	 * @param permissionObjectId
	 * @return
	 */
	private Bean newRelationBean(String status, String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType,
			K permissionObjectId) {
		Bean relationBean = new Bean(AUTH_RELATION_BEAN_NAME);
		relationBean.setProperty(PERMISSION_SUBJECT_ID, permissionSubjectId);
		relationBean
				.setProperty(PERMISSION_SUBJECT_TYPE, permissionSubjectType);
		relationBean.setProperty(PERMISSION_OBJECT_ID, permissionObjectId);
		relationBean.setProperty(PERMISSION_OBJECT_TYPE, permissionObjectType);
		relationBean.setProperty(PERMISSION_STATUS, status);
		return relationBean;
	}

	/**
	 * 创建权限关联bean实例
	 * 
	 * @param permissionSubjectType
	 * @param permissionSubjectId
	 * @param permissionObjectType
	 * @return
	 */
	private Bean newRelationBean(String permissionSubjectType,
			K permissionSubjectId, String permissionObjectType) {
		Bean relationBean = new Bean(AUTH_RELATION_BEAN_NAME);
		relationBean.setProperty(PERMISSION_SUBJECT_ID, permissionSubjectId);
		relationBean
				.setProperty(PERMISSION_SUBJECT_TYPE, permissionSubjectType);
		relationBean.setProperty(PERMISSION_OBJECT_TYPE, permissionObjectType);
		return relationBean;
	}

	private Bean createRelationBean(String status,
			PermissionSubject<K, ?> permissionSubject,
			PermissionObject<K, ?> permissionObject) {
		return newRelationBean(status, permissionSubject.getType(),
				permissionSubject.getId(), permissionObject.getType(),
				permissionObject.getId());
	}

	private class RelationTempObject {
		List<Bean> beans;
		String relationType;
		Bean bean;

		@SuppressWarnings("unused")
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

		public boolean existTable() {
			return DbModelPermissionManager.this.existTable();
		}

		public Bean getBean() {
			return bean;
		}

		public void setBean(Bean bean) {
			this.bean = bean;
		}

	}

	public boolean existTable() {
		return getManager().existsTableByType(AUTH_RELATION_BEAN_NAME, null);
	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K, ?>> getPermissionObjects(
			String objectBeanType,
			Class<? extends PermissionObject> objectClassType) {
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				objectBeanType);
		Bean[] objectBeans = operator.getBeans(new Bean(objectBeanType));
		List<PermissionObject<K, ?>> permissionObjects = new ArrayList<PermissionObject<K, ?>>();
		String primaryKeyName = getPrimaryKeyName(objectBeanType);
		if (objectBeans != null) {
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
	public List<PermissionSubject<K, ?>> getPermissionSubjects(
			String subjectBeanType,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> permissionSubjects = new ArrayList<PermissionSubject<K, ?>>();
		if (cacheSupport) {
			permissionSubjects = storage.getPermissionSubjects(subjectBeanType,
					subjectClassType);
		}
		if (permissionSubjects.size() == 0) {// 没有再查询数据库
			DBOperator<K> operator = (DBOperator<K>) getManager()
					.getDbOperator(subjectBeanType);
			Bean[] subjectBeans = operator.getBeans(new Bean(subjectBeanType));
			String primaryKeyName = getPrimaryKeyName(subjectBeanType);
			if (subjectBeans != null) {
				for (Bean bean : subjectBeans) {
					PermissionSubject permissionSubject = (PermissionSubject) TinyBeanUtil
							.bean2Object(bean, subjectClassType);
					permissionSubject.setId(getPrimaryKeyValue(operator, bean,
							primaryKeyName));
					permissionSubjects.add(permissionSubject);
				}
			}
		}
		return permissionSubjects;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K, ?>> getAssignedPermission(
			String subjectBeanType, String objectBeanType,
			K permissionSubjectId,
			Class<? extends PermissionObject> objectClassType) {

		List<PermissionObject<K, ?>> permissionObjects = new ArrayList<PermissionObject<K, ?>>();
		if (cacheSupport) {
			permissionObjects = storage.getAssignedPermission(subjectBeanType,
					objectBeanType, permissionSubjectId, objectClassType);
		}
		if (permissionObjects.size() == 0) {
			Bean[] beans = getRelationBeans(subjectBeanType, objectBeanType,
					permissionSubjectId);
			DBOperator objectOperator = getManager().getDbOperator(
					objectBeanType);
			if (beans != null) {
				for (Bean bean : beans) {
					K objectId = (K) bean.getProperty(PERMISSION_OBJECT_ID);
					Bean objectBean = objectOperator.getBean(objectId);
					PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
							.bean2Object(objectBean, objectClassType);
					permissionObject.setId(objectId);
					permissionObjects.add(permissionObject);
				}
			}
		}

		return permissionObjects;

	}

	@SuppressWarnings("unchecked")
	private Bean[] getRelationBeans(String subjectBeanType,
			String objectBeanType, K permissionSubjectId) {
		String relationBeanType = AUTH_RELATION_BEAN_NAME;
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				relationBeanType);
		Bean relationBean = newRelationBean(subjectBeanType,
				permissionSubjectId, objectBeanType);
		Bean[] beans = operator.getBeans(relationBean);
		return beans;
	}

	public List<PermissionSubject<K, ?>> getChildPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> children = new ArrayList<PermissionSubject<K, ?>>();
		if (cacheSupport) {
			children = storage.getChildPermissionSubjects(subjectBeanType,
					parentSubjectId, subjectClassType);
		}
		if (children.size() == 0) {
			children = getChildPermissionSubjectRecords(subjectBeanType,
					parentSubjectId, subjectClassType);
		}
		return children;
	}

	@SuppressWarnings("unchecked")
	public List<PermissionObject<K, ?>> getChildPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> children = new ArrayList<PermissionObject<K, ?>>();
		if (cacheSupport) {
			children = storage.getChildPermissionObjects(objectBeanType,
					parentObjectId, objectClassType);
		}
		if (children.size() == 0) {
			Bean queryBean = new Bean(objectBeanType);
			queryBean.setProperty(PARENT_ID, parentObjectId);
			DBOperator<K> operator = (DBOperator<K>) getManager()
					.getDbOperator(objectBeanType);
			Bean[] beans = operator.getBeans(queryBean);
			if (beans != null) {
				String primaryKeyName = getPrimaryKeyName(objectBeanType);
				for (Bean bean : beans) {
					PermissionObject permissionObject = (PermissionObject) TinyBeanUtil
							.bean2Object(bean, objectClassType);
					permissionObject.setId(getPrimaryKeyValue(operator, bean,
							primaryKeyName));
					children.add(permissionObject);
				}
			}
		}
		return children;
	}

	public List<PermissionSubject<K, ?>> getBegatsPermissionSubjects(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> begats = new ArrayList<PermissionSubject<K, ?>>();
		if (cacheSupport) {
			begats = storage.getBegatsPermissionSubjects(subjectBeanType,
					parentSubjectId, subjectClassType);
		}
		if (begats.size() == 0) {
			begats = getBegatsPermissionSubjectRecords(subjectBeanType,
					parentSubjectId, subjectClassType);
		}
		return begats;
	}

	@SuppressWarnings({ "unchecked" })
	private List<PermissionSubject<K, ?>> getChildPermissionSubjectRecords(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> children = new ArrayList<PermissionSubject<K, ?>>();
		Bean queryBean = new Bean(subjectBeanType);
		queryBean.setProperty(PARENT_ID, parentSubjectId);
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				subjectBeanType);
		Bean[] beans = operator.getBeans(queryBean);
		if (beans != null) {
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

	@SuppressWarnings({ "unchecked" })
	private List<PermissionObject<K, ?>> getChildPermissionObjectRecords(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> children = new ArrayList<PermissionObject<K, ?>>();
		Bean queryBean = new Bean(objectBeanType);
		queryBean.setProperty(PARENT_ID, parentObjectId);
		DBOperator<K> operator = (DBOperator<K>) getManager().getDbOperator(
				objectBeanType);
		Bean[] beans = operator.getBeans(queryBean);
		if (beans != null) {
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

	private List<PermissionSubject<K, ?>> getBegatsPermissionSubjectRecords(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		List<PermissionSubject<K, ?>> begats = new ArrayList<PermissionSubject<K, ?>>();
		List<PermissionSubject<K, ?>> children = getChildPermissionSubjectRecords(
				subjectBeanType, parentSubjectId, subjectClassType);
		for (PermissionSubject<K, ?> child : children) {
			begats.add(child);
			List<PermissionSubject<K, ?>> posts = getBegatsPermissionSubjectRecords(
					subjectBeanType, child.getId(), subjectClassType);
			if (posts.size() > 0) {
				begats.addAll(posts);
			}
		}
		return begats;
	}

	private List<PermissionObject<K, ?>> getBegatsPermissionObjectRecords(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> begats = new ArrayList<PermissionObject<K, ?>>();
		List<PermissionObject<K, ?>> children = getChildPermissionObjectRecords(
				objectBeanType, parentObjectId, objectClassType);
		for (PermissionObject<K, ?> child : children) {
			begats.add(child);
			List<PermissionObject<K, ?>> posts = getBegatsPermissionObjectRecords(
					objectBeanType, child.getId(), objectClassType);
			if (posts.size() > 0) {
				begats.addAll(posts);
			}
		}
		return begats;
	}

	public List<PermissionObject<K, ?>> getBegatsPermissionObjects(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		List<PermissionObject<K, ?>> begats = new ArrayList<PermissionObject<K, ?>>();
		if (cacheSupport) {
			begats = storage.getBegatsPermissionObjects(objectBeanType,
					parentObjectId, objectClassType);
		}
		if (begats.size() == 0) {
			begats = getBegatsPermissionObjectRecords(objectBeanType,
					parentObjectId, objectClassType);
		}
		return begats;
	}

	@SuppressWarnings("unchecked")
	public PermissionSubject<K, ?> getChildPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		PermissionSubject<K, ?> permissionSubject = null;
		if (cacheSupport) {
			permissionSubject = storage.getChildPermissionSubjectsWithTree(
					subjectBeanType, parentSubjectId, subjectClassType);
		}
		if (permissionSubject == null) {
			permissionSubject = getPermissionSubject(subjectBeanType,
					parentSubjectId, subjectClassType);
			List subList = getChildPermissionSubjects(subjectBeanType,
					parentSubjectId, subjectClassType);
			permissionSubject.setSubList(subList);
		}
		return permissionSubject;
	}

	public PermissionSubject<K, ?> getBegatsPermissionSubjectsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		PermissionSubject<K, ?> permissionSubject = null;
		if (cacheSupport) {
			permissionSubject = storage.getBegatsPermissionSubjectsWithTree(
					subjectBeanType, parentSubjectId, subjectClassType);
		}
		if (permissionSubject == null) {
			permissionSubject = getBegatsPermissionSubjectRecordsWithTree(
					subjectBeanType, parentSubjectId, subjectClassType);
		}
		return permissionSubject;
	}

	@SuppressWarnings("unchecked")
	private PermissionSubject<K, ?> getBegatsPermissionSubjectRecordsWithTree(
			String subjectBeanType, K parentSubjectId,
			Class<? extends PermissionSubject> subjectClassType) {
		PermissionSubject<K, ?> permissionSubject = getPermissionSubject(
				subjectBeanType, parentSubjectId, subjectClassType);
		List childrens = getChildPermissionSubjects(subjectBeanType,
				parentSubjectId, subjectClassType);
		if (childrens.size() > 0) {
			for (int i = 0; i < childrens.size(); i++) {
				PermissionSubject<K, ?> subject = (PermissionSubject<K, ?>) childrens
						.get(i);
				List subs = getChildPermissionSubjects(subjectBeanType,
						subject.getId(), subjectClassType);
				if (subs.size() > 0) {
					getBegatsPermissionSubjectRecordsWithTree(subjectBeanType,
							subject.getId(), subjectClassType);
				}

			}
			permissionSubject.setSubList(childrens);
		}
		return permissionSubject;
	}

	@SuppressWarnings("unchecked")
	private PermissionObject<K, ?> getBegatsPermissionObjectRecordsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		PermissionObject<K, ?> permissionObject = getPermissionObject(
				objectBeanType, parentObjectId, objectClassType);
		List childrens = getChildPermissionObjects(objectBeanType,
				parentObjectId, objectClassType);
		if (childrens.size() > 0) {
			for (int i = 0; i < childrens.size(); i++) {
				PermissionObject<K, ?> object = (PermissionObject<K, ?>) childrens
						.get(i);
				List subs = getChildPermissionObjects(objectBeanType,
						object.getId(), objectClassType);
				if (subs.size() > 0) {
					getBegatsPermissionObjectRecordsWithTree(objectBeanType,
							object.getId(), objectClassType);
				}

			}
			permissionObject.setSubList(childrens);
		}
		return permissionObject;
	}

	@SuppressWarnings("unchecked")
	public PermissionObject<K, ?> getChildPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		PermissionObject<K, ?> permissionObject = null;
		if (cacheSupport) {
			permissionObject = storage.getChildPermissionObjectsWithTree(
					objectBeanType, parentObjectId, objectClassType);
		}
		if (permissionObject == null) {
			permissionObject = getPermissionObject(objectBeanType,
					parentObjectId, objectClassType);
			List subList = getChildPermissionObjects(objectBeanType,
					parentObjectId, objectClassType);
			permissionObject.setSubList(subList);
		}
		return permissionObject;
	}

	public PermissionObject<K, ?> getBegatsPermissionObjectsWithTree(
			String objectBeanType, K parentObjectId,
			Class<? extends PermissionObject> objectClassType) {
		PermissionObject<K, ?> permissionObject = null;
		if (cacheSupport) {
			permissionObject = storage.getBegatsPermissionObjectsWithTree(
					objectBeanType, parentObjectId, objectClassType);
		}
		if (permissionObject == null) {
			permissionObject = getBegatsPermissionObjectRecordsWithTree(
					objectBeanType, parentObjectId, objectClassType);
		}
		return permissionObject;
	}

}
