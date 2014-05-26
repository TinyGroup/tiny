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
package org.tinygroup.dbrouter.impl.shardrule;

import com.thoughtworks.xstream.annotations.XStreamImplicit;
import org.tinygroup.commons.tools.ArrayUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.jsqlparser.expression.*;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.EqualsTo;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;
import org.tinygroup.jsqlparser.statement.update.Update;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by luoguo on 14-1-22.
 */
public class ShardRuleByFieldValue implements ShardRule {
	@XStreamImplicit
	private List<FieldWithValues> fieldWithValues;

	public boolean isMatch(Partition partition, String sql,
			Object... preparedParams) {
		if (!CollectionUtil.isEmpty(fieldWithValues)) {
			Statement statement = RouterManagerBeanFactory.getManager()
					.getSqlStatement(sql);
			FieldValueMatch fieldValueMatch=null;
			if (statement instanceof Insert) {
				Insert insert = (Insert) statement;
				fieldValueMatch = new InsertFieldValueMatch(
						fieldWithValues, insert, preparedParams);
			} else if (statement instanceof Delete) {
				Delete delete = (Delete) statement;
				fieldValueMatch = new DeleteFieldValueMatch(
						fieldWithValues, delete, preparedParams);
			} else if (statement instanceof Update) {
				Update update = (Update) statement;
				fieldValueMatch = new UpdateFieldValueMatch(
						fieldWithValues, update, preparedParams);
			} else if (statement instanceof Select) {
				Select select = (Select) statement;
				fieldValueMatch = new SelectFieldValueMatch(
						fieldWithValues, select, preparedParams);
			} 
			if(fieldValueMatch==null){
				return false;
			}
			return fieldValueMatch.isMatch();
		}
		return true;

	}

	public String getReplacedSql(String sql) {
		return sql;
	}

	public List<FieldWithValues> getFieldWithValues() {
		if (fieldWithValues == null) {
			fieldWithValues = new ArrayList<FieldWithValues>();
		}
		return fieldWithValues;
	}

	public void setFieldWithValues(List<FieldWithValues> fieldWithValues) {
		this.fieldWithValues = fieldWithValues;
	}

	interface FieldValueMatch {
		boolean isMatch();
	}

	abstract class AbstractFieldValueMatch implements FieldValueMatch {

		protected int paramIndex;
		protected List<FieldWithValues> fields;
		protected String[] names;
		protected String[] fieldValues;
		protected String tableName;
		protected Object[] preparedParams;

		public AbstractFieldValueMatch(List<FieldWithValues> fields,
				Object... preparedParams) {
			super();
			this.paramIndex = 0;
			this.fields = fields;
			names = new String[fields.size()];
			fieldValues = new String[fields.size()];
			for (int i = 0; i < names.length; i++) {
				names[i] = fields.get(i).getName();
				fieldValues[i] = fields.get(i).getValues();
			}
			this.tableName = fields.get(0).getTableName();// 表名就直接取自第一个
			this.preparedParams = preparedParams;
		}

		protected boolean getWhereExpression(Expression where) {
			if (where == null) {// 如果没有条件字段 直接返回true
				return true;
			}
			boolean equalsTo = getEqualsToExpression(where);
			if (where instanceof AndExpression) {
				AndExpression andExpression = (AndExpression) where;
				Expression leftExpression = andExpression.getLeftExpression();
				Expression rightExpression = andExpression.getRightExpression();
				boolean leftMatch = getWhereExpression(leftExpression);
				boolean rightMatch = getWhereExpression(rightExpression);
				return leftMatch && rightMatch;
			}
			if (where instanceof OrExpression) {
				OrExpression orExpression = (OrExpression) where;
				Expression leftExpression = orExpression.getLeftExpression();
				Expression rightExpression = orExpression.getRightExpression();
				boolean leftMatch = getWhereExpression(leftExpression);
				boolean rightMatch = getWhereExpression(rightExpression);
				return leftMatch && rightMatch;
			}
			return equalsTo;

		}

		private boolean getEqualsToExpression(Expression where) {
			boolean match = false;
			if (where instanceof EqualsTo) {
				EqualsTo equalsTo = (EqualsTo) where;
				Expression leftExpression = equalsTo.getLeftExpression();
				Expression rightExpression = equalsTo.getRightExpression();
				if (leftExpression instanceof Column) {
					Column column = (Column) leftExpression;
					String columnName = column.getColumnName();
					int index = columnName.indexOf('.');
					if (index != -1) {
						columnName = columnName.substring(index + 1);
					}
					int indexInArray = ArrayUtil
							.arrayIndexOf(names, columnName);
					if (indexInArray != -1) {// sql存在定义的条件字段
						String[] params = fieldValues[indexInArray].split(",");
						if (rightExpression instanceof StringValue) {
							StringValue stringValue = (StringValue) rightExpression;
							match = ArrayUtil.arrayContains(params,
									stringValue.getValue());
						} else if (rightExpression instanceof LongValue) {
							LongValue longValue = (LongValue) rightExpression;
							match = ArrayUtil.arrayContains(params,
									longValue.getStringValue());
						} else if (rightExpression instanceof DoubleValue) {
							DoubleValue doubleValue = (DoubleValue) rightExpression;
							match = ArrayUtil.arrayContains(params,
									doubleValue.toString());
						} else if (rightExpression instanceof JdbcParameter) {
							Object value = preparedParams[paramIndex];
							match = ArrayUtil.arrayContains(params, value);
							paramIndex++;
						}
					} else {// 如果是可变参数，则变化参数位置
						if (rightExpression instanceof JdbcParameter) {
							paramIndex++;
						}
						match = true;// 不是匹配条件字段，返回true
					}
				}

			}
			return match;
		}

		public int getParamIndex() {
			return paramIndex;
		}

	}

	class InsertFieldValueMatch extends AbstractFieldValueMatch {
		private Insert insert;

		public InsertFieldValueMatch(List<FieldWithValues> fields,
				Insert insert, Object... preparedParams) {
			super(fields, preparedParams);
			this.insert = insert;
		}

		public boolean isMatch() {
			boolean match = false;
			if (tableName.equals(insert.getTable().getName())) {
				for (int i = 0; i < names.length; i++) {
					int index = DbRouterUtil.checkColumnIndex(names[i],
							insert.getColumns());
					String[] params = fieldValues[i].split(",");
					if (index != -1) {
						ItemsList itemsList = insert.getItemsList();
						if (itemsList instanceof ExpressionList) {
							List<Expression> expressions = ((ExpressionList) itemsList)
									.getExpressions();
							Expression expression = expressions.get(index - 1);
							if (expression instanceof StringValue) {
								StringValue stringValue = (StringValue) expression;
								match = ArrayUtil.arrayContains(params,
										stringValue.getValue());
							} else if (expression instanceof LongValue) {
								LongValue longValue = (LongValue) expression;
								match = ArrayUtil.arrayContains(params,
										longValue.getStringValue());
							} else if (expression instanceof DoubleValue) {
								DoubleValue doubleValue = (DoubleValue) expression;
								match = ArrayUtil.arrayContains(params,
										doubleValue.toString());
							} else if (expression instanceof JdbcParameter) {
								int paramIndex = DbRouterUtil.checkParamIndex(
										index, expressions);
								Object value = preparedParams[paramIndex];
								match = ArrayUtil.arrayContains(params, value);
							}
						}
					}
					if(!match){//如果有一个新增字段条件符合，就返回false
						return false;
					}
				}

			}
			return match;
		}

	}

	class DeleteFieldValueMatch extends AbstractFieldValueMatch {

		private Delete delete;

		public DeleteFieldValueMatch(List<FieldWithValues> fields,
				Delete delete, Object... preparedParams) {
			super(fields, preparedParams);
			this.delete = delete;
		}

		public boolean isMatch() {
			String deleteTableName = delete.getTable().getName();
			if (tableName.equals(deleteTableName)) {
				return getWhereExpression(delete.getWhere());
			}
			return false;
		}

	}

	class UpdateFieldValueMatch extends AbstractFieldValueMatch {

		private Update update;

		public UpdateFieldValueMatch(List<FieldWithValues> fields,
				Update update, Object... preparedParams) {
			super(fields, preparedParams);
			this.update = update;
			List<Expression> expressions = update.getExpressions();
			for (Expression expression : expressions) {
				if (expression instanceof JdbcParameter) {
					this.paramIndex++;
				}
			}
		}

		public boolean isMatch() {
			if (tableName.equals(update.getTable().getName())) {
				Expression where = update.getWhere();
				return getWhereExpression(where);
			}
			return false;
		}

	}

	class SelectFieldValueMatch extends AbstractFieldValueMatch {

		private Select select;

		public SelectFieldValueMatch(List<FieldWithValues> fields,
				Select select, Object... preparedParams) {
			super(fields, preparedParams);
			this.select = select;
		}

		public boolean isMatch() {
			SelectBody body = select.getSelectBody();
			if (body instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) body;
				FromItem fromItem = plainSelect.getFromItem();
				if (fromItem instanceof Table) {
					Table table = (Table) fromItem;
					if (tableName.equals(table.getName())) {
						return getWhereExpression(plainSelect.getWhere());
					}
				}
			}
			return false;
		}

	}

}
