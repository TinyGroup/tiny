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
package org.tinygroup.dbrouterjdbc3.sqlprocessor;

import org.tinygroup.commons.beanutil.BeanUtil;
import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.dbrouter.StatementProcessor;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.dbrouter.util.DbRouterUtil;
import org.tinygroup.dbrouter.util.OrderByProcessor;
import org.tinygroup.dbrouter.util.OrderByProcessor.OrderByValues;
import org.tinygroup.dbrouterjdbc3.jdbc.TinyResultSetSimple;
import org.tinygroup.jsqlparser.expression.Alias;
import org.tinygroup.jsqlparser.expression.Expression;
import org.tinygroup.jsqlparser.expression.Function;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.select.*;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;

/**
 * Created by luoguo on 13-12-19.
 */
public class SqlProcessorFunction implements StatementProcessor {
    private static final String COUNT_ALIAS_NAME = "dbRouterCount";// 在处理avg聚合函数时增加的count函数唯一别名标识
    private static final String COUNT = "count";
    private static final String MAX = "max";
    private static final String AVG = "avg";
    private static final String MIN = "min";
    private static final String SUM = "sum";

    public SqlProcessorFunction() {
    }

    public boolean isMatch(String sql) {
        Statement statement = RouterManagerBeanFactory.getManager().getSqlStatement(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody body = select.getSelectBody();
            if (body instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) body;
                for (SelectItem selectItem : plainSelect.getSelectItems()) {
                    if (selectItem instanceof SelectExpressionItem) {
                        SelectExpressionItem item = (SelectExpressionItem) selectItem;
                        Expression expression = item.getExpression();
                        if (expression instanceof Function) {
                            Function function = (Function) item.getExpression();
                            if (function != null) {
                                return true;
                            }
                        }
                    }
                }
            }

        }
        return false;
    }

    public String getSql(String sql) {
        Statement originalStatement = RouterManagerBeanFactory.getManager().getSqlStatement(sql);
        try {
            Statement statement = (Statement) BeanUtil.deepCopy(originalStatement);
            if (statement instanceof Select) {
                Select select = (Select) statement;
                SelectBody body = select.getSelectBody();
                if (body instanceof PlainSelect) {
                    PlainSelect plainSelect = (PlainSelect) body;
                    List<SelectItem> selectItems = plainSelect.getSelectItems();
                    SelectExpressionItem avgItem = getSelectFunctionItem(plainSelect, AVG);
                    if (avgItem != null) {
                        SelectExpressionItem countItem = new SelectExpressionItem();
                        Function count = new Function();
                        count.setName(COUNT);
                        count.setAllColumns(true);
                        countItem.setExpression(count);
                        countItem.setAlias(new Alias(COUNT_ALIAS_NAME));
                        selectItems.add(countItem);
                        return plainSelect.toString();
                    }
                    DbRouterUtil.checkOrderByAndGroupbyItem(plainSelect);
                }
                return select.toString();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return sql;
    }

    private SelectExpressionItem getSelectFunctionItem(PlainSelect plainSelect,
                                                       String functionName) {
        for (SelectItem selectItem : plainSelect.getSelectItems()) {
            if (selectItem instanceof SelectExpressionItem) {
                SelectExpressionItem item = (SelectExpressionItem) selectItem;
                Expression expression = item.getExpression();
                if (expression instanceof Function) {
                    Function function = (Function) item.getExpression();
                    if (function.getName().equalsIgnoreCase(functionName)) {
                        return item;
                    }
                }
            }
        }
        return null;
    }

    public ResultSet combineResult(String sql, List<ResultSet> results) throws SQLException {

        if (CollectionUtil.isEmpty(results)) {
            return null;
        }
        Statement statement = RouterManagerBeanFactory.getManager().getSqlStatement(sql);
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody body = select.getSelectBody();
            if (body instanceof PlainSelect) {
                PlainSelect plainSelect = (PlainSelect) body;
                GroupKey groupKey = getGroupKey(plainSelect);
                List<SelectItem> selectItems = plainSelect.getSelectItems();
                List<SelectItemMemory> items = getSelectItems(selectItems);
                TinyResultSetSimple simple = new TinyResultSetSimple();
                Columns columns = new Columns(results.get(0), items);
                columns.addColumns(simple);
                RowDatas rowDatas = new RowDatas(groupKey, results, items, plainSelect);
                rowDatas.addRowDatas(simple);
                return simple;
            }
        }
        return null;

    }

    private List<SelectItemMemory> getSelectItems(List<SelectItem> selectItems) {
        List<SelectItemMemory> items = new ArrayList<SelectItemMemory>();
        for (int i = 0; i < selectItems.size(); i++) {
            SelectItem selectItem = selectItems.get(i);
            if (selectItem instanceof SelectExpressionItem) {
                SelectExpressionItem item = (SelectExpressionItem) selectItem;
                Expression expression = item.getExpression();
                String alias = null;
                if (item.getAlias() != null) {
                	alias=item.getAlias().getName();
                }
                if (expression instanceof Function) {
                    Function function = (Function) expression;
                    if (alias == null) {
                        alias = function.getName();
                    }
                    SelectItemMemory memory = new SelectItemMemory(i, alias, function.getName());
                    items.add(memory);
                } else if (expression instanceof Column) {
                    Column column = (Column) expression;
                    if (alias == null) {
                        alias = column.getFullyQualifiedName();
                    }
                    SelectItemMemory memory = new SelectItemMemory(i, alias);
                    items.add(memory);
                }
            }
        }
        return items;
    }

    private GroupKey getGroupKey(PlainSelect plainSelect) {
        GroupKey groupKey = null;
        List<Expression> groupExpressions = plainSelect.getGroupByColumnReferences();
        if (!CollectionUtil.isEmpty(groupExpressions)) {
            List<Column> groupColumns = new ArrayList<Column>();
            for (Expression expression : groupExpressions) {
                Column column = (Column) expression;
                groupColumns.add(column);
                // selectColumns.add(column);
            }
            groupKey = new GroupKey(groupColumns);
        } else {
            groupKey = new GroupKey();
        }
        return groupKey;
    }

    class GroupKey {

        private List<String> keys = new ArrayList<String>();
        private List<Column> groupColumns = new ArrayList<Column>();

        public GroupKey(List<Column> groupColumns) {
            super();
            this.groupColumns = groupColumns;
            for (Column column : groupColumns) {
                keys.add(column.getColumnName());
            }
        }

        public GroupKey() {

        }

        public List<String> getKeys() {
            return keys;
        }

        public List<Column> getGroupColumns() {
            return groupColumns;
        }

    }

    class GroupData {

        private List<Object> values = new ArrayList<Object>();

        public GroupData(ResultSet rs, GroupKey gropeKey) throws SQLException {
            if (gropeKey != null) {
                List<String> keys = gropeKey.getKeys();
                for (int i = 0; i < keys.size(); i++) {
                    values.add(rs.getObject(keys.get(i)));
                }
            }

        }


        public int hashCode() {
            if (values == null)
                return 0;
            int result = 1;
            for (Object element : values) {
                int elementHash = element.hashCode();
                result = 31 * result + elementHash;
            }
            return result;
        }


        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj instanceof GroupData) {
                GroupData data = (GroupData) obj;
                List<Object> dataValues = data.getValues();
                if (values.size() != dataValues.size()) {
                    return false;
                }
                for (int i = 0; i < values.size(); i++) {
                    if (!values.get(i).equals(dataValues.get(i))) {// 暂时只比较两个对象的equals方法
                        return false;
                    }
                }
            }
            return true;
        }

        public List<Object> getValues() {
            return values;
        }

    }

    /**
     * 功能说明: 合并后resultset的列信息列表
     * <p/>
     * <p/>
     * 开发人员: renhui <br>
     * 开发时间: 2013-12-21 <br>
     * <br>
     */
    class Columns {

        private List<Integer> columnIndexs;
        private ResultSetMetaData metaData;

        public Columns(ResultSet resultSet, List<SelectItemMemory> items) throws SQLException {
            this.metaData = resultSet.getMetaData();
            columnIndexs = new ArrayList<Integer>();
            if (!CollectionUtil.isEmpty(items)) {
                for (int i = 0; i < items.size(); i++) {
                    columnIndexs.add(i + 1);
                }
            }
        }

        void addColumns(TinyResultSetSimple simple) throws SQLException {
            for (Integer index : columnIndexs) {
                simple.addColumn(index, metaData.getColumnName(index), metaData.getColumnType(index), metaData.getPrecision(index), metaData.getScale(index));
            }
        }

        // void

    }

    /**
     * 功能说明:代表一行数据
     * <p/>
     * <p/>
     * 开发人员: renhui <br>
     * 开发时间: 2013-12-21 <br>
     * <br>
     */
    class RowData {
    	private ResultSet resultSet;
    	private Object[] values;
    	private Map<Integer, SelectItemMemory> aggrerateItems = new HashMap<Integer, SelectItemMemory>();
    	private Long addedCount = 0l;
    	private int resultRowIndex;

        public RowData(int resultRowIndex, ResultSet resultSet, List<SelectItemMemory> items)
                throws SQLException {
            this.resultSet = resultSet;
            this.resultRowIndex = resultRowIndex;
            if (!CollectionUtil.isEmpty(items)) {
                values = new Object[items.size()];
                for (int i = 0; i < items.size(); i++) {
                    SelectItemMemory item = items.get(i);
                    if (item.isFunction()) {
                        if (item.getItemName().equals(COUNT_ALIAS_NAME)) {
                            addedCount = resultSet.getLong(i + 1);
                        }
                        aggrerateItems.put(i, item);
                    }
                    values[i] = resultSet.getObject(i + 1);
                }
            }
        }

        public Map<Integer, SelectItemMemory> getAggregateItems() {
            return aggrerateItems;
        }

        public Object[] getVaules() {
            return values;
        }

        public Long getCount(int index) throws SQLException {
            resultSet.absolute(resultRowIndex);
            return resultSet.getLong(index + 1);
        }

        public Long getAddedCount() throws SQLException {
            return addedCount;
        }

        public Double getSum(Integer index) throws SQLException {
            resultSet.absolute(resultRowIndex);
            return resultSet.getDouble(index + 1);
        }

        public Double getMax(Integer index) throws SQLException {
            resultSet.absolute(resultRowIndex);
            return resultSet.getDouble(index + 1);
        }

        public double getMin(Integer index) throws SQLException {
            resultSet.absolute(resultRowIndex);
            return resultSet.getDouble(index + 1);
        }

        public double getAvg(Integer index) throws SQLException {
            resultSet.absolute(resultRowIndex);
            return resultSet.getDouble(index + 1);
        }

    }

    class RowDatas {
    	private Map<GroupData, List<RowData>> rowDatas = new HashMap<GroupData, List<RowData>>();
        private OrderByProcessor orderByProcessor;

        public RowDatas(GroupKey gropeKey, List<ResultSet> results, List<SelectItemMemory> items,
                        PlainSelect plainSelect) throws SQLException {
            if (!CollectionUtil.isEmpty(results)) {
                orderByProcessor = new OrderByProcessor(plainSelect, results.get(0));
                for (ResultSet resultSet : results) {
                    int resultRowIndex = 0;
                    while (resultSet.next()) {
                        GroupData data = new GroupData(resultSet, gropeKey);
                        List<RowData> datas = rowDatas.get(data);
                        if (datas == null) {
                            datas = new ArrayList<RowData>();
                            rowDatas.put(data, datas);
                        }
                        datas.add(new RowData(++resultRowIndex, resultSet, items));
                    }
                }
            }

        }

        public void addRowDatas(TinyResultSetSimple simple) throws SQLException {
            List<OrderByValues> orderByValueList = new ArrayList<OrderByValues>();
            for (Entry<GroupData, List<RowData>> entry : rowDatas.entrySet()) {
                List<RowData> rowDatas = entry.getValue();
                Object[] value = createRowValues(rowDatas);
                orderByValueList.add(orderByProcessor.new OrderByValues(value));

            }
            if (orderByProcessor.getSortOrder() != null) {
                Collections.sort(orderByValueList, orderByProcessor.getSortOrder());
            }
            for (OrderByValues orderByValues : orderByValueList) {
                simple.addRow(orderByValues.getValues());
            }
        }

        private Object[] createRowValues(List<RowData> rowDatas) throws SQLException {
            if (rowDatas.size() > 0) {
                RowData firstRow = rowDatas.get(0);
                Object[] values = firstRow.getVaules();
                Map<Integer, SelectItemMemory> aggregateItems = firstRow.getAggregateItems();
                for (Integer index : aggregateItems.keySet()) {
                    SelectItemMemory item = aggregateItems.get(index);
                    String functionName = item.getFunctionName();
                    if (functionName.equalsIgnoreCase(AVG)) {
                        values[index] = processRowDataWithAvg(index, rowDatas);
                    } else if (functionName.equalsIgnoreCase(COUNT)) {
                        values[index] = processRowDataWithCount(index, rowDatas);
                    } else if (functionName.equalsIgnoreCase(SUM)) {
                        values[index] = processRowDataWithSum(index, rowDatas);
                    } else if (functionName.equalsIgnoreCase(MAX)) {
                        values[index] = processRowDataWithMax(index, rowDatas);
                    } else if (functionName.equalsIgnoreCase(MIN)) {
                        values[index] = processRowDataWithMin(index, rowDatas);
                    }
                }
                return values;
            }
            return null;
        }

        protected Double processRowDataWithAvg(Integer index, List<RowData> rowDatas)
                throws SQLException {
            if (rowDatas.size() > 0) {
                double sum = 0d;
                Long count = 0l;
                for (RowData rowData : rowDatas) {
                    Long addedCount = rowData.getAddedCount();
                    sum += rowData.getAvg(index) * addedCount;
                    count += addedCount;
                }
                return sum / count;
            }
            return null;
        }

        protected Double processRowDataWithMax(Integer index, List<RowData> rowDatas)
                throws SQLException {
            if (rowDatas.size() > 0) {
                Double max = rowDatas.get(0).getMax(index);
                for (int i = 1; i < rowDatas.size(); i++) {
                    double maxValue = rowDatas.get(i).getMax(index);
                    max = max > maxValue ? max : maxValue;
                }
                return max;
            }
            return null;
        }

        protected Double processRowDataWithMin(Integer index, List<RowData> rowDatas)
                throws SQLException {
            if (rowDatas.size() > 0) {
                Double min = rowDatas.get(0).getMax(index);
                for (int i = 1; i < rowDatas.size(); i++) {
                    double maxValue = rowDatas.get(i).getMin(index);
                    min = min < maxValue ? min : maxValue;
                }
                return min;
            }
            return null;
        }

        protected Double processRowDataWithSum(Integer index, List<RowData> rowDatas)
                throws SQLException {
            Double sum = 0d;
            for (RowData rowData : rowDatas) {
                sum += rowData.getSum(index);
            }
            return sum;
        }

        protected Long processRowDataWithCount(int index, List<RowData> rowDatas)
                throws SQLException {
            Long totalCount = 0l;
            for (RowData rowData : rowDatas) {
                totalCount += rowData.getCount(index);
            }
            return totalCount;
        }

    }

    /**
     * 功能说明: 存储查询语句选择项
     * <p/>
     * <p/>
     * 开发人员: renhui <br>
     * 开发时间: 2013-12-22 <br>
     * <br>
     */
    class SelectItemMemory {
        private int index;// 选择项序号 例如0、1、2....
        private String itemName;// 选择项名称，可能是别名
        private String functionName;// 选择项是分组函数，分组函数名称

        public SelectItemMemory(int index, String itemName) {
            super();
            this.index = index;
            this.itemName = itemName;
        }

        public SelectItemMemory(int index, String itemName, String functionName) {
            this(index, itemName);
            this.functionName = functionName;
        }

        public boolean isFunction() {
            return functionName != null;
        }

        public int getIndex() {
            return index;
        }

        public String getItemName() {
            return itemName;
        }

        public String getFunctionName() {
            return functionName;
        }

    }

}
