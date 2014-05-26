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

import java.util.List;

import org.tinygroup.dbrouter.ShardRule;
import org.tinygroup.dbrouter.config.Partition;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
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

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by luoguo on 13-12-17.
 */
public abstract class ShardRuleByIdAbstract implements ShardRule {
    /**
     * 余数
     */
    @XStreamAsAttribute
    private long remainder;
    /**
     * 表名
     */
    @XStreamAsAttribute
    @XStreamAlias("table-name")
    private String tableName;
    /**
     * 主键字段
     */
    @XStreamAsAttribute
    @XStreamAlias("primary-key-field-name")
    private String primaryKeyFieldName;

    public ShardRuleByIdAbstract() {

    }

    public ShardRuleByIdAbstract(String tableName, String primaryKeyFieldName, int remainder) {
        this.tableName = tableName;
        this.primaryKeyFieldName = primaryKeyFieldName;
        this.remainder = remainder;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrimaryKeyFieldName() {
        return primaryKeyFieldName;
    }

    public void setPrimaryKeyFieldName(String primaryKeyFieldName) {
        this.primaryKeyFieldName = primaryKeyFieldName;
    }

    public long getRemainder() {
        return remainder;
    }

    public void setRemainder(int remainder) {
        this.remainder = remainder;
    }
    
    public boolean isMatch(Partition partition, String sql,
			Object... preparedParams) {
		Statement statement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		int paramIndex=0;
		if (statement instanceof Insert) {
			Insert insert = (Insert) statement;
			if (tableName.equals(insert.getTable().getName())) {
				ItemsList itemsList= insert.getItemsList();
				if(itemsList instanceof ExpressionList){
					List<Expression> expressions = ((ExpressionList)itemsList).getExpressions();
					int shardSize=partition.getShards().size();
					for (int i = 0; i < insert.getColumns().size(); i++) {
						Column column = insert.getColumns().get(i);
						Expression expression=expressions.get(i);
						if (column.getColumnName().equals(primaryKeyFieldName)) {
							  if(expression instanceof LongValue){
								  LongValue longValue=(LongValue)expression;
								  if(longValue.getValue()% shardSize == remainder){
									  return true;
								  }
							  }else if(expression instanceof JdbcParameter){
								   Long value = (Long) preparedParams[paramIndex];
									if ((value % partition.getShards().size()) == remainder) {
										return true;
									}
								  paramIndex++;
							  }
						}
					}
				}
			}
		}
		if (statement instanceof Delete) {
			Delete delete = (Delete) statement;
			if (tableName.equals(delete.getTable().getName())) {
				return getWhereExpression(0,delete.getWhere(), partition,preparedParams);
			}
		}
		if (statement instanceof Update) {
			Update update = (Update) statement;
			List<Expression> expressions = update.getExpressions();
			for (Expression expression : expressions) {
				if (expression instanceof JdbcParameter) {
					paramIndex++;
				}
			}
			if (tableName.equals(update.getTable().getName())) {
				return getWhereExpression(paramIndex,update.getWhere(), partition);
			}

		}
		if (statement instanceof Select) {
			Select select = (Select) statement;
			SelectBody body = select.getSelectBody();
			if (body instanceof PlainSelect) {
				PlainSelect plainSelect = (PlainSelect) body;
				FromItem fromItem = plainSelect.getFromItem();
				if (fromItem instanceof Table) {
					Table table = (Table) fromItem;
					if (tableName.equals(table.getName())) {
						return getWhereExpression(0,plainSelect.getWhere(),
								partition,preparedParams);
					}
				}
			}

		}

		return false;
	}
    
    private boolean getWhereExpression(int paramIndex,Expression where, Partition partition,Object... preparedParams) {
		if (where == null) {
			return true;
		}
		return getEqualsToExpression(paramIndex,where, partition,preparedParams);

	}

	private boolean getEqualsToExpression(int paramIndex,Expression where, Partition partition,Object... preparedParams) {
		if (where instanceof EqualsTo) {
			EqualsTo equalsTo = (EqualsTo) where;
			Expression leftExpression = equalsTo.getLeftExpression();
			Expression rightExpression = equalsTo.getRightExpression();
			if (leftExpression instanceof Column) {
				Column column = (Column) leftExpression;
				if (column.getColumnName().equals(primaryKeyFieldName)) {
					if(rightExpression instanceof LongValue){
						  LongValue longValue=(LongValue)rightExpression;
						  if(longValue.getValue()% partition.getShards()
									.size() == remainder){
							  return true;
						  }
					  }else if(rightExpression instanceof JdbcParameter){
						   Long value = (Long) preparedParams[paramIndex];
							if ((value % partition.getShards().size()) == remainder) {
								return true;
							}
					  }
				}
			}

		}
		return false;
	}
}
