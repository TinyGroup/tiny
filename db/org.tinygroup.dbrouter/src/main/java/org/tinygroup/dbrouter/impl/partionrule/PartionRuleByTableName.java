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
package org.tinygroup.dbrouter.impl.partionrule;

import java.util.List;

import org.tinygroup.dbrouter.PartitionRule;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.jsqlparser.schema.Table;
import org.tinygroup.jsqlparser.statement.Statement;
import org.tinygroup.jsqlparser.statement.delete.Delete;
import org.tinygroup.jsqlparser.statement.insert.Insert;
import org.tinygroup.jsqlparser.statement.select.FromItem;
import org.tinygroup.jsqlparser.statement.select.Join;
import org.tinygroup.jsqlparser.statement.select.PlainSelect;
import org.tinygroup.jsqlparser.statement.select.Select;
import org.tinygroup.jsqlparser.statement.select.SelectBody;
import org.tinygroup.jsqlparser.statement.select.SetOperationList;
import org.tinygroup.jsqlparser.statement.select.SubSelect;
import org.tinygroup.jsqlparser.statement.select.WithItem;
import org.tinygroup.jsqlparser.statement.update.Update;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * 分区规则实现类
 * Created by luoguo on 13-12-16.
 */
public class PartionRuleByTableName implements PartitionRule {
    /**
     * 分区的表名
     */
	@XStreamAlias("table-name")
	@XStreamAsAttribute
    private String tableName;

    public PartionRuleByTableName() {

    }

    /**
     * 构造函数
     *
     * @param tableName
     */
    public PartionRuleByTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * 查看是否匹配
     *
     * @param sql
     * @return
     */
    public boolean isMatch(String sql) {
        Statement statement = RouterManagerBeanFactory.getManager().getSqlStatement(sql);
        if (statement instanceof Delete) {
            Delete delete = (Delete) statement;
            if (delete.getTable().getName().equals(tableName)) {
                return true;
            }
        }
        if (statement instanceof Insert) {
            Insert insert = (Insert) statement;
            if (insert.getTable().getName().equals(tableName)) {
                return true;
            }
        }
        if (statement instanceof Update) {
            Update update = (Update) statement;
            if (update.getTable().getName().equals(tableName)) {
                return true;
            }
        }
        if (statement instanceof Select) {
            Select select = (Select) statement;
            SelectBody body = select.getSelectBody();
            if (body instanceof PlainSelect) {
                return plainSelectSqlMatch((PlainSelect)body);
            }
            if (body instanceof SetOperationList) {
                SetOperationList operationList = (SetOperationList) body;
                List<PlainSelect> plainSelects = operationList.getPlainSelects();
                for (PlainSelect plainSelect : plainSelects) {
                    if (plainSelectSqlMatch(plainSelect)) {
                        return true;
                    }
                }
            }
            if (body instanceof WithItem) {
                WithItem withItem = (WithItem) body;
                PlainSelect plainSelect = (PlainSelect) withItem.getSelectBody();
                return plainSelectSqlMatch(plainSelect);
            }
        }

        return false;
    }

    /**
     * 普通查询语句匹配
     *
     * @param plainSelect
     * @return
     */
    private boolean plainSelectSqlMatch(PlainSelect plainSelect) {
    	FromItem fromItem=plainSelect.getFromItem();
    	if(fromItem instanceof Table){
    		 Table table = (Table)fromItem;
    	        if (table.getName().equals(tableName)) {
    	            return true;
    	        }
    	        List<Join> joins = plainSelect.getJoins();
    	        if (joins != null) {
    	            for (Join join : joins) {
    	            	FromItem rightItem=join.getRightItem();
    	            	if(rightItem instanceof Table){
    	            		Table joinTable = (Table) rightItem;
        	                if (joinTable.getName().equals(tableName)) {
        	                    return true;
        	                }
    	            	}
    	            }
    	        }
    	}else if(fromItem instanceof SubSelect){
    		SubSelect subSelect=(SubSelect)fromItem;
    		return plainSelectSqlMatch((PlainSelect)subSelect.getSelectBody());
    	}
        return false;
    }

}
