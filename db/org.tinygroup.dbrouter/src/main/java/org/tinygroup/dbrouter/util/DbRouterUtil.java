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
package org.tinygroup.dbrouter.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.dbrouter.config.DataSourceConfig;
import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.jsqlparser.expression.Alias;
import org.tinygroup.jsqlparser.expression.BinaryExpression;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.JdbcParameter;
import org.tinygroup.jsqlparser.expression.LongValue;
import org.tinygroup.jsqlparser.expression.StringValue;
import org.tinygroup.jsqlparser.expression.operators.conditional.AndExpression;
import org.tinygroup.jsqlparser.expression.operators.conditional.OrExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.ExistsExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.ExpressionList;
import org.tinygroup.jsqlparser.expression.operators.relational.InExpression;
import org.tinygroup.jsqlparser.expression.operators.relational.ItemsList;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.AllColumns;
import org.tinygroup.jsqlparser.statement.select.AllTableColumns;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.Join;
import org.tinygroup.jsqlparser.statement.select.OrderByElement;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;
import org.tinygroup.jsqlparser.statement.select.SelectExpressionItem;
import org.tinygroup.jsqlparser.statement.select.SelectItem;
import org.tinygroup.jsqlparser.statement.select.SetOperationList;
import org.tinygroup.jsqlparser.statement.select.SubJoin;
import org.tinygroup.jsqlparser.statement.select.SubSelect;
import org.tinygroup.jsqlparser.statement.select.WithItem;
import org.tinygroup.jsqlparser.statement.update.Update;

/**
 * 功能说明: 工具类
 * <p/>
 * 
 * 开发人员: renhui <br>
 * 开发时间: 2013-12-17 <br>
 * <br>
 */
public  final class DbRouterUtil {

	
	private DbRouterUtil(){
		
	}
	
	/**
	 * 
	 * 替换sql语句中的表名信息，条件语句带表名，暂时不进行替换。
	 * 
	 * @param sql
	 * @param tableMapping
	 * @return
	 */
	public static String transformSqlWithTableName(String sql,
			Map<String, String> tableMapping) {
		try {
			Statement originalStatement = RouterManagerBeanFactory.getManager()
					.getSqlStatement(sql);
			Statement statement = (Statement) BeanUtil
					.deepCopy(originalStatement);
			if (statement instanceof Insert) {
				return transformInsertSql(tableMapping, statement);
			}else if (statement instanceof Delete) {
				return transformDeleteSql(tableMapping, statement);
			}else if (statement instanceof Update) {
				return transformUpdateSql(tableMapping, statement);
			}else if (statement instanceof Select) {
				return transformSelectSql(tableMapping, statement);
			}
			return sql;
		} catch (Exception e) {
			throw new DbrouterRuntimeException(e);
		}
	}

	private static String transformSelectSql(Map<String, String> tableMapping,
			Statement statement) {
		Select select = (Select) statement;
		SelectBody body = select.getSelectBody();
		transformSelectBody(body, tableMapping);
		return select.toString();
	}

	private static String transformUpdateSql(Map<String, String> tableMapping,
			Statement statement) {
		Update update = (Update) statement;
		String tableName = update.getTable().getName();
		String newTableName = tableMapping.get(tableName);
		if (!StringUtil.isBlank(newTableName)) {
			update.getTable().setName(newTableName);
		}
		Expression expression = update.getWhere();
		transformExpression(expression, tableMapping);
		return update.toString();
	}

	private static String transformDeleteSql(Map<String, String> tableMapping,
			Statement statement) {
		Delete delete = (Delete) statement;
		String tableName = delete.getTable().getName();
		String newTableName = tableMapping.get(tableName);
		if (!StringUtil.isBlank(newTableName)) {
			delete.getTable().setName(newTableName);
		}
		Expression expression = delete.getWhere();
		transformExpression(expression, tableMapping);
		return delete.toString();
	}

	private static String transformInsertSql(Map<String, String> tableMapping,
			Statement statement) {
		Insert insert = (Insert) statement;
		String tableName = insert.getTable().getName();
		String newTableName = tableMapping.get(tableName);
		if (!StringUtil.isBlank(newTableName)) {
			insert.getTable().setName(newTableName);
			List<Column> columns = insert.getColumns();
			for (Column column : columns) {// 变化新增字段信息
				Table columnTable = column.getTable();
				if (columnTable != null
						&& !StringUtil.isBlank(columnTable.getName())) {
					columnTable.setName(newTableName);
				}
			}
			
		}
		return insert.toString();
	}

	private static void transformExpression(Expression expression,
			Map<String, String> tableMapping) {
		if (expression == null) {
			return;
		}
		if (expression instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) expression;
			transformSubSelect(subSelect, tableMapping);
		}else if (expression instanceof AndExpression) {
			AndExpression andExpression = (AndExpression) expression;
			Expression rightExpression = andExpression.getRightExpression();
			transformExpression(rightExpression, tableMapping);
		}else if(expression instanceof OrExpression){
			OrExpression orExpression = (OrExpression) expression;
			Expression rightExpression = orExpression.getRightExpression();
			transformExpression(rightExpression, tableMapping);
		}else if(expression instanceof InExpression){
			InExpression inExpression=(InExpression)expression;
			ItemsList rightItemsList=inExpression.getRightItemsList();
			if(rightItemsList instanceof SubSelect){
				SubSelect subSelect = (SubSelect) rightItemsList;
				transformSubSelect(subSelect, tableMapping);
			}
			
		}else if(expression instanceof ExistsExpression){
			ExistsExpression existsExpression=(ExistsExpression)expression;
			transformExpression(existsExpression.getRightExpression(), tableMapping);
			
		}else if(expression instanceof BinaryExpression){
			BinaryExpression binaryExpression=(BinaryExpression)expression;
			transformExpression(binaryExpression.getRightExpression(), tableMapping);
		}
		
	}

	private static void transformSubSelect(SubSelect select,
			Map<String, String> tableMapping) {
		SelectBody body = select.getSelectBody();
		transformSelectBody(body, tableMapping);
	}

	private static void transformSelectBody(SelectBody body,
			Map<String, String> tableMapping) {
		if (body instanceof PlainSelect) {
			transfromPlainSelect((PlainSelect) body, tableMapping);
		}
		if (body instanceof SetOperationList) {
			SetOperationList operationList = (SetOperationList) body;
			List<PlainSelect> plainSelects = operationList.getPlainSelects();
			for (PlainSelect plainSelect : plainSelects) {
				transfromPlainSelect(plainSelect, tableMapping);
			}
		}
		if (body instanceof WithItem) {
			WithItem withItem = (WithItem) body;
			SelectBody selectBody = withItem.getSelectBody();
			transformSelectBody(selectBody, tableMapping);
		}
	}

	private static void transfromPlainSelect(PlainSelect plainSelect,
			Map<String, String> tableMapping) {
		checkOrderByAndGroupbyItem(plainSelect);
		FromItem fromItem = plainSelect.getFromItem();
		if (fromItem != null) {
			transfromFromItem(fromItem, tableMapping);
		}
		List<Join> joins = plainSelect.getJoins();
		if (!CollectionUtil.isEmpty(joins)) {
			for (Join join : joins) {
				FromItem rightItem = join.getRightItem();
				transfromFromItem(rightItem, tableMapping);
			}
		}
		Expression where = plainSelect.getWhere();
		if (where != null) {
			transformExpression(where, tableMapping);
		}
	}

	private static void transfromFromItem(FromItem fromItem,
			Map<String, String> tableMapping) {
		if (fromItem instanceof Table) {
			Table table = (Table) fromItem;
			String tableName = table.getName();
			String newTableName = tableMapping.get(tableName);
			if (!StringUtil.isBlank(newTableName)) {
				table.setName(newTableName);
			}
		} else if (fromItem instanceof SubSelect) {
			SubSelect subSelect = (SubSelect) fromItem;
			transformSubSelect(subSelect, tableMapping);
		} else if (fromItem instanceof SubJoin) {
			SubJoin join = (SubJoin) fromItem;
			FromItem left = join.getLeft();
			transfromFromItem(left, tableMapping);
			Join right = join.getJoin();
			transfromFromItem(right.getRightItem(), tableMapping);
		}
	}

	public static int getSqlParamSize(String sql) {
		int paramSize = 0;
		int len = sql.length() + 1;
		char[] command = new char[len];
		len--;
		sql.getChars(0, len, command, 0);
		for (int i = 0; i < len; i++) {
			char c = command[i];
			if (c == '?') {
				paramSize++;
			}
		}
		return paramSize;
	}

	/**
	 * 框架内部会对sql进行处理： 如果是insert语句，会检测主键字段是否有传值。如果插入语句没有传主键字段，将会自动赋值。
	 * 
	 * @param sql
	 * @param router
	 * @param metaData
	 * @return
	 */
	public static String transformInsertSql(String sql, Router router,
			Map<String, String> tableMapping, DatabaseMetaData metaData) {
		Statement originalStatement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		ResultSet rs = null;
		ResultSet typeRs = null;
		try {
			Statement statement = (Statement) BeanUtil
					.deepCopy(originalStatement);
			if (statement instanceof Insert) {
				Insert insert = (Insert) statement;
				Table table = insert.getTable();
				String tableName = table.getName();
				String realTableName = getRealTableName(tableMapping, tableName);
				rs = metaData.getPrimaryKeys(null, null, realTableName);
				if (rs.next()) {
					String primaryKey = rs.getString("COLUMN_NAME");
					typeRs = metaData.getColumns(null, null, realTableName,
							primaryKey);
					if (typeRs.next()) {
						int dataType = typeRs.getInt("DATA_TYPE");
						List<Column> columns = insert.getColumns();
						boolean exist = primaryKeyInColumns(primaryKey, columns);
						if (!exist) {
							Column primaryColumn = new Column(table, primaryKey);
							columns.add(primaryColumn);
							ItemsList itemsList = insert.getItemsList();
							if (itemsList instanceof ExpressionList) {
								addPrimaryKeyExpression(router, tableName,
										dataType, itemsList);
							}
							return insert.toString();
						}
					}
				}

			}
		} catch (Exception e) {
			throw new DbrouterRuntimeException(e);
		} finally {
			try {
				if (rs != null) {
					rs.close();
				}
				if (typeRs != null) {
					typeRs.close();
				}
			} catch (SQLException e) {
				throw new DbrouterRuntimeException(e);
			}

		}
		return sql;
	}

	private static String getRealTableName(Map<String, String> tableMapping,
			String tableName) {
		String realTableName = tableName;// 真正在数据库存在的表名
		if (tableMapping != null&&tableMapping.containsKey(tableName)) {
				realTableName = tableMapping.get(tableName);
		}
		return realTableName;
	}
   /**
    * 新增sql语句增加主键字段信息
    * @param router
    * @param tableName
    * @param dataType
    * @param itemsList
    */
	private static void addPrimaryKeyExpression(Router router,
			String tableName, int dataType, ItemsList itemsList) {
		List<Expression> expressions = ((ExpressionList) itemsList)
				.getExpressions();
		Expression expression = null;
		switch (dataType) {
		case Types.CHAR:
		case Types.VARCHAR:
		case Types.LONGVARCHAR:
			String value = RouterManagerBeanFactory
					.getManager().getPrimaryKey(router,
							tableName);
			StringValue stringValue = new StringValue(value);
			expression = stringValue;
		    break;
		case Types.NUMERIC:
		case Types.DECIMAL:
		case Types.INTEGER:
		case Types.SMALLINT:
		case Types.TINYINT:
		case Types.BIGINT:
			Object values = RouterManagerBeanFactory
					.getManager().getPrimaryKey(router,
							tableName);
			expression = new LongValue(values + "");
		    break;
		default:
		}
		expressions.add(expression);
	}

	private static boolean primaryKeyInColumns(String primaryKey,
			List<Column> columns) {
		boolean exist = false;
		if (!CollectionUtil.isEmpty(columns)) {
			for (Column column : columns) {
				if (column.getColumnName().equals(primaryKey)) {
					exist = true;
					break;
				}
			}
		}
		return exist;
	}

	/**
	 * 检测查询语句选择项是否包含orderby字段，不存在则创建orderby字段
	 * 
	 * @param plainSelect
	 */
	public static void checkOrderByAndGroupbyItem(PlainSelect plainSelect) {
		List<Column> orderByColumns = getOrderByColumns(plainSelect);
		List<Column> groupByColumns = getGroupByColumns(plainSelect);
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		for (Column groupByColumn : groupByColumns) {
			checkItem(selectItems, groupByColumn);
		}
		for (Column orderByColumn : orderByColumns) {
			checkItem(selectItems, orderByColumn);
		}
	}

	public static void checkItem(List<SelectItem> selectItems,
			Column checkColumn) {
		String groupByName = checkColumn.getFullyQualifiedName();
		boolean isExist = false;
		for (SelectItem selectItem : selectItems) {
			if (selectItem instanceof AllColumns) {
				isExist = true;
				break;
			}
			if (selectItem instanceof AllTableColumns) {
				isExist = true;
				break;
			}
			SelectExpressionItem item = (SelectExpressionItem) selectItem;
			Alias alias = item.getAlias();
			if (alias != null && groupByName.equals(alias.getName())) {
				isExist = true;
				break;
			}
			Expression expression = item.getExpression();
			if (expression instanceof Column) {
				Column column = (Column) expression;
				if (groupByName.equals(column.getFullyQualifiedName())
						|| groupByName.equals(column.getColumnName())) {
					isExist = true;
					break;
				}
			}
		}
		if (!isExist) {
			SelectExpressionItem item = new SelectExpressionItem();
			Column column = new Column(checkColumn.getTable(),
					checkColumn.getColumnName());
			item.setExpression(column);
			selectItems.add(item);
		}

	}

	/**
	 * 获取查询语句orderby字段的位置
	 * 
	 * @param plainSelect
	 * @param resultSet
	 * @return
	 * @throws SQLException
	 */
	public static int[] getOrderByIndexs(PlainSelect plainSelect,
			ResultSet resultSet) throws SQLException {

		List<Column> orderByColumns = getOrderByColumns(plainSelect);
		if (CollectionUtil.isEmpty(orderByColumns)) {
			return new int[0];
		}
		List<SelectItem> selectItems = plainSelect.getSelectItems();
		int[] indexs = new int[orderByColumns.size()];
		for (int i = 0; i < orderByColumns.size(); i++) {
			Column orderByColumn = orderByColumns.get(i);
			String orderByName = orderByColumn.getFullyQualifiedName();
			for (int j = 0; j < selectItems.size(); j++) {
				SelectItem selectItem = selectItems.get(j);
				if (selectItem instanceof AllColumns
						|| selectItem instanceof AllTableColumns) {
					indexs[i] = resultSet.findColumn(orderByName) - 1;
					break;
				}
				SelectExpressionItem item = (SelectExpressionItem) selectItem;
				Alias alias = item.getAlias();
				if (alias != null && orderByName.equals(alias.getName())) {
					indexs[i] = j;
					break;
				}
				Expression expression = item.getExpression();
				if (expression instanceof Column) {
					Column column = (Column) expression;
					if (orderByName.equals(column.getFullyQualifiedName())
							|| orderByName.equals(column.getColumnName())) {
						indexs[i] = j;
					}
				}
			}
		}
		return indexs;
	}

	public static List<Column> getOrderByColumns(PlainSelect plainSelect) {
		List<Column> orderByColumns = new ArrayList<Column>();
		List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
		if (!CollectionUtil.isEmpty(orderByElements)) {
			for (OrderByElement orderByElement : orderByElements) {
				Column column = (Column) orderByElement.getExpression();
				orderByColumns.add(column);
			}
		}
		return orderByColumns;
	}

	public static List<Column> getGroupByColumns(PlainSelect plainSelect) {
		List<Column> groupByColumns = new ArrayList<Column>();
		List<Expression> groupExpressions = plainSelect
				.getGroupByColumnReferences();
		if (!CollectionUtil.isEmpty(groupExpressions)) {
			for (Expression expression : groupExpressions) {
				Column column = (Column) expression;
				groupByColumns.add(column);
			}
		}
		return groupByColumns;
	}

	public static String getSelectTableName(String sql) {
		Statement statement = RouterManagerBeanFactory.getManager()
				.getSqlStatement(sql);
		if (statement instanceof Select) {
			Select select = (Select) statement;
			SelectBody body = select.getSelectBody();
			if (body instanceof PlainSelect) {
				return getTableNameWithPlainSelect((PlainSelect) body);
			}
		}
		throw new DbrouterRuntimeException("must be a query sql");

	}

	private static String getTableNameWithPlainSelect(PlainSelect plainSelect) {
		FromItem fromItem = plainSelect.getFromItem();
		if (fromItem instanceof Table) {
			Table table = (Table) fromItem;
			return table.getName();
		}
		return null;
	}

	public static Connection createConnection(DataSourceConfig config)
			throws SQLException {
			try {
				Class.forName(config.getDriver());
			} catch (ClassNotFoundException e) {
				throw new DbrouterRuntimeException(e);
			}
			Connection connection = DriverManager
					.getConnection(config.getUrl(), config.getUserName(),
							config.getPassword());
			connection.setAutoCommit(true);
			return connection;
	}

	/**
	 * 
	 * 检查参数指定的字段名称在列集合元素的位置，返回-1说明columnName不存在columns中
	 * 
	 * @param columnName
	 * @param columns
	 * @return
	 */
	public static int checkColumnIndex(String columnName, List<Column> columns) {
		if (!CollectionUtil.isEmpty(columns)) {
			for (int i = 0; i < columns.size(); i++) {
				Column column = columns.get(i);
				String name = column.getColumnName();
				int index = name.indexOf('.');
				if (index != -1) {
					name = name.substring(index + 1);
				}
				if (name.equals(columnName)) {
					return i + 1;
				}
			}
		}
		return -1;
	}

	/**
	 * 
	 * 检查insert语句中列号为columnIndex的jdbcParamter的位置，例如 insert into aaa(id,name,age)
	 * values(11,?,?) columnIndex:<=1----return -1 columnIndex:2----return 0;
	 * columnIndex:3----return 1; columnIndex:>3-----return -1;
	 * 
	 * @param columnIndex
	 * @param expressions
	 * @return
	 */
	public static int checkParamIndex(int columnIndex,
			List<Expression> expressions) {
		if (CollectionUtil.isEmpty(expressions)
				|| columnIndex > expressions.size() || columnIndex < 1) {
			return -1;
		}
		int paramIndex = -1;
		for (int i = 0; i < columnIndex; i++) {
			Expression expression = expressions.get(i);
			if (expression instanceof JdbcParameter) {
				paramIndex++;
			}
		}
		return paramIndex;
	}

}
