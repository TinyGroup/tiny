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
package org.tinygroup.tinydb.operator.impl;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameterValue;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.operator.DBOperator;
import org.tinygroup.tinydb.operator.DbSingleOperator;
import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinydb.util.TinyDBUtil;

import java.util.*;

public class BeanDBSingleOperator<K> extends BeanDBBaseOperator implements
		DbSingleOperator<K> {

	public BeanDBSingleOperator(JdbcTemplate jdbcTemplate) {
		super(jdbcTemplate);
	}

	/**
	 * 更新一个对象
	 * 
	 * @param bean
	 * @param conditionColumns
	 *
	 * @return
	 */
	private int updateBean(Bean bean, List<String> conditionColumns) {
		String sql = getUpdateSql(bean, conditionColumns);
		SqlParameterValue[] params = getSqlParamterValue(bean, conditionColumns);
		return executeBySqlParameterValues(sql, params);
	}

	private Bean queryBean(K beanId) {
		String sql = getQuerySql();
		return (Bean) queryObject(sql, getBeanType(), getSchema(), beanId);
	}

	public Bean insert(Bean bean) {
		insertTopBean(bean);// 新增最上层记录
		processRelation(bean, getRelation(), new InsertRelationCallBack());
		return bean;
	}

	/**
	 * 
	 * bean对象描述的数据库记录是否存在
	 * 
	 * @param bean
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected boolean beanNotExist(Bean bean) {
		DBOperator operator = getManager().getDbOperator(bean.getType());
		String primaryValue = getPrimaryKeyValue(operator, bean);
		if (primaryValue != null) {
			try {
				Bean selectBean = operator.getBean(primaryValue);
				if (selectBean != null) {
					return false;
				}
			} catch (Exception e) {
				if (e instanceof EmptyResultDataAccessException) {// jdbctemplate查询不存在会抛出此异常，这里判断记录是否存在，因此需要捕捉异常
					return true;
				}
			}

		}
		return true;
	}

	protected void insertTopBean(Bean bean) {
		String insertSql = getInsertSql(bean);
		SqlParameterValue[] sqlParameterValues = createSqlParameterValue(bean);
		executeBySqlParameterValues(insertSql, sqlParameterValues);
	}

	public int update(Bean bean) {
		int record = updateTopBean(bean);
		processRelation(bean, getRelation(), new UpdateRelationCallBack());
		return record;

	}

	public int update(Bean bean, List<String> conditionColumns) {
		int record = updateBean(bean, conditionColumns);
		processRelation(bean, getRelation(), new UpdateRelationCallBack());
		return record;
	}

	protected Bean[] getRelationBeans(Bean bean, String collectionMode,
			String relationKeyName) {
		Object object = bean.getProperty(relationKeyName);
		return toBeanArrayFromObject(object);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static Bean[] toBeanArrayFromObject(Object object) {
		Bean[] beans = new Bean[0];
		if (object == null) {
			return beans;
		}
		if (Collection.class.isAssignableFrom(object.getClass())) {
			return (Bean[]) ((Collection) object).toArray(beans);
		}
		if (object.getClass().isArray()) {
			return (Bean[]) object;
		}
		return beans;
	}

	/**
	 * 
	 * 更新最上层bean
	 * 
	 * @param bean
	 * @return
	 */
	protected int updateTopBean(Bean bean) {
		TableConfiguration table = TinyDBUtil.getTableConfigByBean(
				bean.getType(), getSchema());
		List<String> conditionColumns = new ArrayList<String>();
		conditionColumns.add(table.getPrimaryKey().getColumnName());
		return updateBean(bean, conditionColumns);
	}

	public int delete(Bean bean) {
		int record = deleteTopBean(bean);
		processRelation(bean, getRelation(), new DeleteRelationCallBack());
		return record;
	}

	protected int deleteTopBean(Bean bean) {
		SqlParameterValue[] params = getSqlParameterValues(bean);
		List<String> conditionColumns = getColumnNames(bean);
		String sql = getDeleteSql(bean.getType(), conditionColumns);
		return executeBySqlParameterValues(sql, params);
	}

	public Bean getBean(K beanId) {
		Bean bean = queryBean(beanId);
		bean = processRelation(bean, getRelation(), new QueryRelationCallBack());
		return bean;
	}

	protected Bean processRelation(Bean bean, Relation relation,
			RelationCallBack callBack) {
		if (relation != null) {
			if (relation.getType().equals(bean.getType())) {
				List<Relation> relations = relation.getRelations();
				if (relations != null) {
					for (Relation subRelation : relations) {
						if (subRelation.getMode().equals(Relation.MORE_TO_ONE)) {// n对1
							callBack.processMoreToOne(bean, subRelation,
									subRelation.getRelations());
						} else if (subRelation.getMode().equals(
								Relation.ONE_TO_MORE)) {
							callBack.processOneToMore(bean, subRelation,
									subRelation.getRelations());
						}
					}
				}
			} else {
				throw new DBRuntimeException("tinydb.typeOfInconsistency",
						relation.getType(), bean.getType());
			}
		}
		return bean;

	}

	protected void checkOneToMore(List<Relation> relations) {
		Set<String> set = new HashSet<String>();
		for (Relation relation : relations) {
			set.add(relation.getType());
		}
	}

	protected void checkMoreToOne(List<Relation> relations) {
		Set<String> set = new HashSet<String>();
		for (Relation relation : relations) {
			set.add(relation.getType());
		}
		if (set.size() != relations.size()) {
			throw new DBRuntimeException("tinydb.moreToOneError");
		}
	}

	@SuppressWarnings("unchecked")
	protected Bean createRelationBean(Bean bean, Relation relation) {
		String relationType = relation.getType();
		DBOperator<K> operator = (DBOperator<K>) getManager()
				.getDbOperator(relationType);
		operator.setRelation(relation);
		Bean queryBean = new Bean(relationType);
		queryBean.setProperty(relation.getMainName(),
				bean.getProperty(relation.getKeyName()));
		Bean[] beans = operator.getBeans(queryBean);
		if (beans != null && beans.length == 1) {
			return beans[0];
		}
		throw new DBRuntimeException("tinydb.onlyUniqueRecords");
	}

	@SuppressWarnings("unchecked")
	protected Bean[] createRelationBeans(Bean bean, Relation relation) {
		String relationType = relation.getType();
		DBOperator<K> operator = (DBOperator<K>) getManager()
				.getDbOperator(relationType);
		operator.setRelation(relation);
		Bean subBean = new Bean(relationType);
		subBean.setProperty(relation.getMainName(),
				bean.getProperty(relation.getKeyName()));
		return operator.getBeans(subBean);
	}

	public int deleteById(K beanId) {
		String sql = getDeleteSqlByKey(getBeanType());
		TableConfiguration table = TinyDBUtil.getTableConfigByBean(
				getBeanType(), getSchema());
		SqlParameterValue param = createSqlParamter(beanId,
				table.getPrimaryKey());
		return executeBySqlParameterValue(sql, param);
	}

	/**
	 * 
	 * 功能说明: 关联处理的回调接口
	 * <p>

	 * 开发人员: renhui <br>
	 * 开发时间: 2013-8-8 <br>
	 * <br>
	 */
	interface RelationCallBack {
		void processMoreToOne(Bean bean, Relation relation,
				List<Relation> subRelations);

		void processOneToMore(Bean bean, Relation relation,
				List<Relation> relations);
	}

	abstract class CrudRelationCallBack implements RelationCallBack {

		public void processMoreToOne(Bean bean, Relation relation,
				List<Relation> relations) {
			checkMoreToOne(relations);
			String relationKeyName = relation.getRelationKeyName();
			if (StringUtil.isBlank(relationKeyName)) {
				relationKeyName = relation.getType();
			}
			Bean subBean = bean.getProperty(relationKeyName);
			if (subBean != null) {
				subBean.setProperty(relation.getMainName(),
						bean.getProperty(relation.getKeyName()));
				moreToOnecallBack(subBean);
				processRelation(subBean, relation, this);
			}

		}

		public void processOneToMore(Bean bean, Relation relation,
				List<Relation> relations) {
			checkOneToMore(relations);
			String relationKeyName = relation.getRelationKeyName();
			if (StringUtil.isBlank(relationKeyName)) {
				relationKeyName = relation.getType();
			}
			Bean[] beans = getRelationBeans(bean, relation.getCollectionMode(),
					relationKeyName);
			if (beans.length > 0) {
				for (Bean subBean : beans) {
					subBean.setProperty(relation.getMainName(),
							bean.getProperty(relation.getKeyName()));
					oneToMorecallBack(subBean);
					processRelation(subBean, relation, this);
				}
			}
		}

		abstract void moreToOnecallBack(Bean bean);

		abstract void oneToMorecallBack(Bean bean);

	}

	class InsertRelationCallBack extends CrudRelationCallBack {
		
		void moreToOnecallBack(Bean bean) {
			if (beanNotExist(bean)) {
				insertTopBean(bean);
			}
		}

		
		void oneToMorecallBack(Bean bean) {
			if (beanNotExist(bean)) {
				insertTopBean(bean);
			}
		}
	}

	class UpdateRelationCallBack extends CrudRelationCallBack {
		
		void moreToOnecallBack(Bean bean) {
			if (beanNotExist(bean)) {
				insertTopBean(bean);
			} else {
				updateTopBean(bean);
			}

		}

		
		void oneToMorecallBack(Bean bean) {
			if (beanNotExist(bean)) {
				insertTopBean(bean);
			} else {
				updateTopBean(bean);
			}

		}
	}

	class DeleteRelationCallBack extends CrudRelationCallBack {
		
		void moreToOnecallBack(Bean bean) {
			if (!beanNotExist(bean)) {
				deleteTopBean(bean);
			}

		}

		
		void oneToMorecallBack(Bean bean) {
			if (!beanNotExist(bean)) {
				deleteTopBean(bean);
			}

		}
	}

	class QueryRelationCallBack implements RelationCallBack {
		public void processMoreToOne(Bean bean, Relation relation,
				List<Relation> relations) {
			checkMoreToOne(relations);
			Bean subBean = createRelationBean(bean, relation);
			String relationKeyName = relation.getRelationKeyName();
			if (StringUtil.isBlank(relationKeyName)) {
				relationKeyName = relation.getType();
			}
			bean.setProperty(relationKeyName, subBean);// 把关联对象存储到外层bean对象中
			processRelation(subBean, relation, this);

		}

		public void processOneToMore(Bean bean, Relation relation,
				List<Relation> relations) {
			checkOneToMore(relations);
			Bean[] beans = createRelationBeans(bean, relation);// 创建一对多关联的beans
			List<Bean> subBeans = new ArrayList<Bean>();
			if (beans != null) {
				subBeans = Arrays.asList(beans);
			}
			String relationKeyName = relation.getRelationKeyName();
			if (StringUtil.isBlank(relationKeyName)) {
				relationKeyName = relation.getType();
			}
			if (subBeans.size() > 0) {
				if (relation.getCollectionMode().equals(Relation.LIST_MODE)) {
					bean.setProperty(relationKeyName, subBeans);
				} else if (relation.getCollectionMode().equals(
						Relation.SET_MODE)) {
					bean.setProperty(relationKeyName, new HashSet<Bean>(
							subBeans));
				} else if (relation.getCollectionMode().equals(
						Relation.ARRAY_MODE)) {
					bean.setProperty(relationKeyName,
							subBeans.toArray(new Bean[0]));
				}
				// bean.remove(subRelation.getKeyName());//去除原有的外键关系
				for (Bean subBean : subBeans) {
					processRelation(subBean, relation, this);
				}
			}

		}
	}

}
