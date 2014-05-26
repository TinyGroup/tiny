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
package org.tinygroup.database;

import java.util.List;

/**
 * 数据库脚本生成器
 *
 * @author luoguo
 */
public interface DatabaseSQLGenerator {

    /**
     * 返回自定义SQL的SQL语句
     *
     * @param dialectName   方言名称
     * @param packgeName    包名
     * @param customSqlName 自定义Sql名称
     * @return SQL语句
     */
    String getCustomSql(String dialectName, String packgeName,
                        String customSqlName);


    /**
     * 返回建表语句
     *
     * @param dialectName
     * @param packgeName
     * @param tableName
     * @return
     */
    String getCreateTableSql(String dialectName, String packgeName,
                             String tableName);

    /**
     * 返回视图创建SQL语句
     *
     * @param dialectName
     * @param packgeName
     * @param viewName
     * @return
     */
    String getCreateViewSql(String dialectName, String packgeName,
                            String viewName);


    /**
     * 返回索引SQL
     *
     * @param dialectName
     * @param packgeName
     * @param indexName
     * @return
     */
    String getCreateIndexSql(String dialectName, String packgeName,
                             String tableName, String indexName);


    /**
     * 返回创建存储过程SQL
     *
     * @param dialectName
     * @param packgeName
     * @param procedureName
     * @return
     */
    String getCreateProcedureSql(String dialectName, String packgeName,
                                 String procedureName);

    /**
     * 返回初始化脚本
     *
     * @param dialectName
     * @param tableName
     * @return
     */
    List<String> getInitDataSql(String dialectName, String tableName);

}
