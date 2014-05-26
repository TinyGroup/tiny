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

import org.tinygroup.commons.tools.CollectionUtil;
import org.tinygroup.jsqlparser.schema.Column;
import org.tinygroup.jsqlparser.statement.select.OrderByElement;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * 排序处理类
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-12-23 <br>
 * <br>
 */
public class OrderByProcessor {

    private List<OrderByColumn> orderByList = new ArrayList<OrderByColumn>();
    private OrderByValues valueCache;
    private boolean[] orderTypes;
    private int[] orderByIndexs;
    private boolean hasOrderBy;
    private SortOrder sortOrder;

    public OrderByProcessor(PlainSelect plainSelect, ResultSet resultSet) throws SQLException {
        List<OrderByElement> orderByElements = plainSelect.getOrderByElements();
        if (!CollectionUtil.isEmpty(orderByElements)) {
            for (OrderByElement orderByElement : orderByElements) {
                Column column = (Column) orderByElement.getExpression();
                orderByList.add(new OrderByColumn(column, orderByElement.isAsc()));
            }
            orderTypes = new boolean[orderByList.size()];
            for (int i = 0; i < orderTypes.length; i++) {
                orderTypes[i] = orderByList.get(i).isAsc();
            }
            orderByIndexs = DbRouterUtil.getOrderByIndexs(plainSelect, resultSet);
            hasOrderBy = true;
            sortOrder = new SortOrder(orderTypes, orderByIndexs);
        }

    }

    /**
     * 是否存在排序字段
     *
     * @return
     */
    public boolean isHasOrderBy() {
        return hasOrderBy;
    }

    public SortOrder getSortOrder() {
        return sortOrder;
    }

    public boolean[] getOrderTypes() {
        return orderTypes;
    }

    public int[] getOrderByIndexs() {
        return orderByIndexs;
    }

    public void setValues(Object[] values) {
        this.valueCache = new OrderByValues(values);
    }

    public void setValues(ResultSet resultSet) throws SQLException {
        this.valueCache = new OrderByValues(resultSet);
    }

    public OrderByValues getValueCache() {
        return valueCache;
    }

    public void setValueCache(OrderByValues valueCache) {
        this.valueCache = valueCache;
    }

    public void clearValueCache() {
        this.valueCache = null;
    }

    /**
     * 排序列
     */
    public class OrderByColumn {
        private Column column;
        private boolean asc = true;

        public OrderByColumn(Column column, boolean asc) {
            super();
            this.column = column;
            this.asc = asc;
        }

        public String getColumnName() {
            return column.getColumnName();
        }

        public boolean isAsc() {
            return asc;
        }

    }

    public class OrderByValues {
        private Object[] values;
        private ResultSet resultSet;

        public OrderByValues(Object[] values) {
        	this.values=new Object[values.length];
        	System.arraycopy(values, 0, this.values, 0, values.length);
        }

        public OrderByValues(ResultSet resultSet) throws SQLException {
            this.resultSet = resultSet;
            if (orderByList.size() > 0) {
                values = new Object[orderByList.size()];
                for (int i = 0; i < orderByList.size(); i++) {
                    OrderByColumn orderBy = orderByList.get(i);
                    Object value = resultSet.getObject(orderBy.getColumnName());
                    values[i] = value;
                }
            }

        }

        public Object[] getValues() {
            return values;
        }

        public void setValues(Object[] values) {
        	this.values=new Object[values.length];
        	System.arraycopy(values, 0, this.values, 0, values.length);
        }

        public ResultSet getResultSet() {
            return resultSet;
        }

        public void clearValueCache() {
            values = null;
        }
        
        public boolean isCurrentResult(ResultSet rs){
        	return rs.equals(resultSet);
        }

    }


}
