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

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.SqlParameterValue;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.tinydb.Bean;
import org.tinygroup.tinydb.BeanDbNameConverter;
import org.tinygroup.tinydb.BeanOperatorManager;
import org.tinygroup.tinydb.config.ColumnConfiguration;
import org.tinygroup.tinydb.config.TableConfiguration;
import org.tinygroup.tinydb.exception.DBRuntimeException;
import org.tinygroup.tinydb.impl.DefaultNameConverter;
import org.tinygroup.tinydb.operator.DbBaseOperator;
import org.tinygroup.tinydb.relation.Relation;
import org.tinygroup.tinydb.util.TinyDBUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class BeanDBBaseOperator extends DBSpringBaseOperator implements DbBaseOperator {

    private static final String TINYDB_INSERT_FIELD_NOT_EXIST = "tinydb.insertFieldNotExist";

    private static final String TINYDB_TABLE_NOT_EXIST = "tinydb.tableNotExist";

    private BeanOperatorManager manager;

    private String beanType;

    private String schema;
    /**
     * 如果操作的bean是关联对象，需要进行关联操作
     */
    private Relation relation;

    private BeanDbNameConverter beanDbNameConverter = new DefaultNameConverter();

    public BeanDbNameConverter getBeanDbNameConverter() {
        return beanDbNameConverter;
    }

    public void setBeanDbNameConverter(BeanDbNameConverter beanDbNameConverter) {
        this.beanDbNameConverter = beanDbNameConverter;
    }

    public int getAutoIncreaseKey() {
        return getDialect().getNextKey();
    }

    public BeanDBBaseOperator(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    protected String getDeleteSqlByKey(String beanType) {
        String tableName = getFullTableName(beanType);
        StringBuffer sb = new StringBuffer();
        StringBuffer condition = new StringBuffer();
        sb.append("delete from ").append(tableName);
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(beanType,
                getSchema());
        String pk = table.getPrimaryKey().getColumnName();
        condition.append(pk).append("=?");
        sb.append(" where ").append(condition);
        return sb.toString();
    }

    /**
     * @param beanType
     * @param conditionColumns
     * @return
     */
    protected String getQuerySqlAndParamKeys(String beanType,
                                             List<String> conditionColumns) {
        String tableName = getFullTableName(beanType);
        if (conditionColumns == null) {
            throw new DBRuntimeException("查询配置的条件字段为空,tableName:" + tableName
                    + " Object:" + beanType);
        }
        StringBuffer sb = new StringBuffer();
        sb.append("select * from ").append(tableName);
        // 条件字段计算
        StringBuffer where = new StringBuffer();
        if (conditionColumns.size() > 0) {
            for (int i = 0; i < conditionColumns.size(); i++) {
                where.append(conditionColumns.get(i)).append("=?");
                if (i < conditionColumns.size() - 1) {
                    where.append(" and ");
                }
            }
            sb.append(" where ");
            sb.append(where);
        }
        return sb.toString();
    }

    /**
     * 获取操作的所有数据库字段名称
     *
     * @param bean
     * @return
     */
    protected List<String> getColumnNames(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        List<String> insertColumnNames = new ArrayList<String>();
        for (ColumnConfiguration column : table.getColumns()) {
            String columnsName = column.getColumnName();
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(columnsName);
            if (bean.containsKey(propertyName)) {
                insertColumnNames.add(columnsName);
            }
        }
        return insertColumnNames;
    }

    /**
     * 获取操作的所有数据库字段类型
     *
     * @param bean
     * @return
     */
    protected List<Integer> getDataTypes(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        List<Integer> dataTypes = new ArrayList<Integer>();
        for (ColumnConfiguration column : table.getColumns()) {
            String columnsName = column.getColumnName();
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(columnsName);
            if (bean.containsKey(propertyName)) {
                dataTypes.add(column.getDataType());
            }
        }
        return dataTypes;
    }

    protected SqlParameterValue[] getSqlParameterValues(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        List<ColumnConfiguration> columns = table.getColumns();
        List<SqlParameterValue> params = new ArrayList<SqlParameterValue>();
        for (ColumnConfiguration column : columns) {
            String columnsName = column.getColumnName();
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(columnsName);
            if (bean.containsKey(propertyName)) {
                params.add(createSqlParamter(bean.getProperty(propertyName),
                        column));
            }
        }
        return params.toArray(new SqlParameterValue[0]);
    }

    protected List<Integer> getDataTypes(List<ColumnConfiguration> paramsKeys) {
        List<Integer> dataTypes = new ArrayList<Integer>();
        for (ColumnConfiguration column : paramsKeys) {
            dataTypes.add(column.getDataType());
        }
        return dataTypes;
    }

    protected SqlParameterValue createSqlParamter(Object value,
                                                  ColumnConfiguration primaryColumn) {
        String columnsName = primaryColumn.getColumnName();
        String propertyName = beanDbNameConverter
                .dbFieldNameToPropertyName(columnsName);
        String scaleStr = primaryColumn.getDecimalDigits();
        int scale = 0;
        if (scaleStr != null) {
            scale = Integer.parseInt(scaleStr);
        }
        SqlParameter sqlParameter = new SqlParameter(propertyName,
                primaryColumn.getDataType(), scale);
        return new SqlParameterValue(sqlParameter, value);
    }

    /**
     * 创建新增sql
     *
     * @param bean
     * @return
     */
    protected String getInsertSql(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        if (table != null) {
            StringBuffer sb = new StringBuffer();
            StringBuffer field = new StringBuffer();
            StringBuffer values = new StringBuffer();
            sb.append("insert into " + getTableName(table) + "(");
            List<ColumnConfiguration> columns = table.getColumns();
            if (columns != null && columns.size() > 0) {
                for (ColumnConfiguration column : columns) {
                    String columnName = column.getColumnName();
                    String propertyName = beanDbNameConverter
                            .dbFieldNameToPropertyName(columnName);
                    if (bean.containsKey(propertyName) || column.isPrimaryKey()) {
                        field.append(",").append(columnName);
                        values.append(",?");
                    }

                }
                sb.append(field.substring(1)).append(")values(")
                        .append(values.substring(1)).append(")");
                return sb.toString();
            } else {
                throw new DBRuntimeException(TINYDB_INSERT_FIELD_NOT_EXIST);
            }

        }
        throw new DBRuntimeException(TINYDB_TABLE_NOT_EXIST, bean.getType(),
                getFullTableName(bean.getType()));
    }

    /**
     * @param bean
     * @param conditionColumns sql中用到的字段的列表，包括 update table set * where *两个*区域用到的所有字段
     *                         外部传进来的空列表，由此函数进行填充，字段按在sql中使用的先后顺序放入列表
     * @return
     */
    protected String getUpdateSql(Bean bean, List<String> conditionColumns) {

        if (conditionColumns == null || conditionColumns.size() == 0) {
            throw new DBRuntimeException("tinydb.updateFieldNotExist",
                    getFullTableName(bean.getType()), bean.getType());
        }

        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        if (table != null) {
            StringBuffer sb = new StringBuffer();
            String field = getUpdateFieldSegment(table, bean, conditionColumns);
            // 条件字段计算
            String condition = getConditionSegment(table, conditionColumns);
            sb.append("update ").append(getTableName(table)).append(" set ")
                    .append(field.substring(1)).append(" where ")
                    .append(condition);
            return sb.toString();
        }
        throw new DBRuntimeException(TINYDB_TABLE_NOT_EXIST, bean.getType(),
                getFullTableName(bean.getType()));
    }

    private String getUpdateFieldSegment(TableConfiguration table, Bean bean,
                                         List<String> conditionColumns) {
        StringBuffer field = new StringBuffer();
        List<ColumnConfiguration> columns = table.getColumns();
        // 更新字段计算
        for (ColumnConfiguration column : columns) {
            String columnName = column.getColumnName();
            if (!column.isPrimaryKey()) {
                // 如果说不是条件字段
                if (!conditionColumns.contains(columnName)) {
                    String propertyName = beanDbNameConverter
                            .dbFieldNameToPropertyName(columnName);
                    if (bean.containsKey(propertyName)) {
                        field.append("," + columnName + "=?");
                    }
                }
            }

        }
        return field.toString();
    }

    private String getConditionSegment(TableConfiguration table,
                                       List<String> conditionColumns) {
        // 条件字段计算
        StringBuffer condition = new StringBuffer();
        List<ColumnConfiguration> columns = table.getColumns();
        boolean first = true;
        for (ColumnConfiguration column : columns) {
            String columnName = column.getColumnName();
            if (conditionColumns.contains(columnName)) {
                if (first) {
                    first = false;
                } else {
                    condition.append(" and ");
                }
                condition.append(columnName).append("=?");
            }
        }
        return condition.toString();
    }

    protected SqlParameterValue[] getSqlParamterValue(Bean bean,
                                                      List<String> conditionColumns) {
        List<SqlParameterValue> params = new ArrayList<SqlParameterValue>();
        if (conditionColumns == null || conditionColumns.size() == 0) {
            throw new DBRuntimeException("tinydb.updateFieldNotExist",
                    getFullTableName(bean.getType()), bean.getType());
        }
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        if (table != null) {
            List<ColumnConfiguration> columns = table.getColumns();
            // 设置更新字段参数
            setUpdateParamter(bean, params, columns, conditionColumns);
            //设置条件字段参数
            setConditionParamter(bean, params, columns, conditionColumns);
            return params.toArray(new SqlParameterValue[0]);
        }
        throw new DBRuntimeException(TINYDB_TABLE_NOT_EXIST, bean.getType(),
                getFullTableName(bean.getType()));

    }

    private void setUpdateParamter(Bean bean, List<SqlParameterValue> params,
                                   List<ColumnConfiguration> columns, List<String> conditionColumns) {
        for (ColumnConfiguration column : columns) {
            String columnName = column.getColumnName();
            if (!column.isPrimaryKey()) {
                // 如果说不是条件字段
                if (!conditionColumns.contains(columnName)) {
                    String propertyName = beanDbNameConverter
                            .dbFieldNameToPropertyName(columnName);
                    if (bean.containsKey(propertyName)) {
                        params.add(createSqlParamter(
                                bean.getProperty(propertyName), column));
                    }
                }
            }
        }

    }

    private void setConditionParamter(Bean bean,
                                      List<SqlParameterValue> params, List<ColumnConfiguration> columns,
                                      List<String> conditionColumns) {
        for (ColumnConfiguration column : columns) {
            String columnName = column.getColumnName();
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(columnName);
            if (conditionColumns.contains(columnName)) {
                params.add(createSqlParamter(bean.getProperty(propertyName),
                        column));
            }
        }
    }

    protected List<List<Object>> getParamList(Bean[] beans,
                                              List<ColumnConfiguration> paramsKeys) {
        List<List<Object>> paramsList = new ArrayList<List<Object>>();
        for (Bean bean : beans) {
            List<Object> params = new ArrayList<Object>();
            for (int i = 0; i < paramsKeys.size(); i++) {
                ColumnConfiguration column = paramsKeys.get(i);
                String propertyName = beanDbNameConverter
                        .dbFieldNameToPropertyName(column.getColumnName());
                params.add(bean.get(propertyName));

            }
            paramsList.add(params);
        }
        return paramsList;
    }

    protected List<Object> getParam(Bean bean,
                                    List<ColumnConfiguration> paramsKeys) {
        List<Object> params = new ArrayList<Object>();
        for (int i = 0; i < paramsKeys.size(); i++) {
            ColumnConfiguration column = paramsKeys.get(i);
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(column.getColumnName());
            params.add(bean.get(propertyName));
        }
        return params;
    }

    protected SqlParameterValue[] createSqlParameterValue(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        if (table != null) {
            List<ColumnConfiguration> columns = table.getColumns();
            if (columns != null && columns.size() > 0) {
                List<SqlParameterValue> parameterValues = new ArrayList<SqlParameterValue>();
                boolean isIncrease = manager.getTableConfigurationContainer().isIncrease(schema);
                for (ColumnConfiguration column : columns) {
                    String columnsName = column.getColumnName();
                    String propertyName = beanDbNameConverter
                            .dbFieldNameToPropertyName(columnsName);
                    Object value = null;
                    if (column.isPrimaryKey()) {// 主键值自动生成
                        value = getPrimaryKeyValue(bean, isIncrease,
                                propertyName);
                        parameterValues.add(createSqlParamter(value, column));
                    } else {
                        if (bean.containsKey(propertyName)) {
                            value = bean.getProperty(propertyName);
                            parameterValues
                                    .add(createSqlParamter(value, column));
                        }
                    }
                }
                return parameterValues.toArray(new SqlParameterValue[0]);

            }

        }
        throw new DBRuntimeException(TINYDB_TABLE_NOT_EXIST, beanType,
                getFullTableName(beanType));

    }

    private Object getPrimaryKeyValue(Bean bean, boolean isIncrease,
                                      String propertyName) {
        Object value = null;
        if (isIncrease) {
            value = getAutoIncreaseKey();
        } else {
            value = bean.getProperty(propertyName);
            if (value == null) {
                value = UUID.randomUUID().toString().replaceAll("-", "");
            }
        }
        bean.setProperty(propertyName, value);
        return value;
    }

    protected List<SqlParameterValue[]> getInsertParams(Bean[] beans) {
        List<SqlParameterValue[]> params = new ArrayList<SqlParameterValue[]>();
        for (Bean bean : beans) {
            params.add(createSqlParameterValue(bean));
        }
        return params;
    }

    protected List<SqlParameterValue[]> getParams(Bean[] beans,
                                                  SqlParameterValue[] values) {
        List<SqlParameterValue[]> params = new ArrayList<SqlParameterValue[]>();
        params.add(values);
        for (int i = 1; i < beans.length; i++) {
            Bean bean = beans[i];
            SqlParameterValue[] param = new SqlParameterValue[values.length];
            for (int j = 0; j < values.length; j++) {
                SqlParameterValue value = values[j];
                param[j] = new SqlParameterValue(value, bean.getProperty(value
                        .getName()));
            }
            params.add(param);
        }
        return params;
    }

    protected List<List<Object>> getDeleteParams(Bean[] beans) {
        List<List<Object>> params = new ArrayList<List<Object>>();
        for (Bean bean : beans) {
            params.add(getParams(bean));
        }
        return params;
    }

    private String getFullTableName(String beanType) {
        String tempType = beanType;
        if (beanType == null) {
            tempType = this.beanType;
        }
        String tableName = beanDbNameConverter.typeNameToDbTableName(tempType);
        return getTableNameWithSchame(tableName);
    }

    private String getTableName(TableConfiguration table) {
        return getTableNameWithSchame(table.getName());
    }

    protected List<Object> getParams(Bean bean) {
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(
                bean.getType(), getSchema());
        List<Object> params = new ArrayList<Object>();
        for (ColumnConfiguration column : table.getColumns()) {
            String columnName = column.getColumnName();
            String propertyName = beanDbNameConverter
                    .dbFieldNameToPropertyName(columnName);
            if (bean.containsKey(propertyName)) {
                params.add(bean.get(propertyName));
            }
        }
        return params;
    }

    /**
     * 获取删除对象的sql语句，以及所需的参数的property列表
     *
     * @param beanType
     * @param conditionColumns
     * @return
     */
    protected String getDeleteSql(String beanType, List<String> conditionColumns) {
        if (conditionColumns == null || conditionColumns.size() == 0) {
            throw new DBRuntimeException("tinydb.deleteFieldNotExist",
                    getFullTableName(beanType), beanType);
        }
        StringBuffer sb = new StringBuffer();
        StringBuffer where = new StringBuffer();
        sb.append("delete from ").append(getFullTableName(beanType));
        boolean first = true;
        for (String columnName : conditionColumns) {
            if (first) {
                first = false;
            } else {
                where.append(" and ");
            }
            where.append(columnName).append("=?");
        }
        sb.append(" where ").append(where);
        return sb.toString();
    }

    protected String getQuerySql() {
        StringBuffer sb = new StringBuffer();
        StringBuffer where = new StringBuffer();
        // 条件字段计算
        TableConfiguration table = TinyDBUtil.getTableConfigByBean(beanType,
                schema);
        String primaryKeyName = table.getPrimaryKey().getColumnName();
        where.append(primaryKeyName).append("=?");
        sb.append("select * from ").append(getFullTableName(beanType));
        sb.append(" where ");
        sb.append(where);
        return sb.toString();
    }

    public String getBeanType() {
        return beanType;
    }

    public void setBeanType(String beanType) {
        this.beanType = beanType;
    }

    public void setSchema(String schame) {
        this.schema = schame;
    }

    public Bean createBean() {
        return new Bean(getBeanType());
    }

    protected String getTableNameWithSchame(String tableName) {
        if (schema == null || "".equals(schema)) {
            return tableName;
        }
        return schema + "." + tableName;
    }

    public String getSchema() {
        if (StringUtil.isBlank(schema)) {
            schema = manager.getMainSchema();
        }
        return schema;
    }

    public void setManager(BeanOperatorManager manager) {
        this.manager = manager;
    }

    public BeanOperatorManager getManager() {
        return manager;
    }

    public void setRelation(String id) {
        this.relation = manager.getRelationById(id);
    }

    public void setRelation(Relation relation) {
        this.relation = relation;
    }

    public Relation getRelation() {
        return relation;
    }

    protected String getPrimaryFieldName(DbBaseOperator operator,
                                         String beanType) {
        TableConfiguration configuration = manager.getTableConfiguration(beanType, operator.getSchema());
        if (configuration != null) {
            return operator.getBeanDbNameConverter().dbFieldNameToPropertyName(
                    configuration.getPrimaryKey().getColumnName());
        }
        throw new DBRuntimeException("tinydb.notExistPrimaryField",
                getFullTableName(beanType));
    }

    /**
     * 获取主键值
     *
     * @param operator
     * @param bean
     * @return
     */
    protected String getPrimaryKeyValue(DbBaseOperator operator, Bean bean) {
        Object primaryKeyValue = bean.getProperty(getPrimaryFieldName(operator,
                bean.getType()));
        if (primaryKeyValue != null) {
            return String.valueOf(primaryKeyValue);
        }
        return null;
    }

}
