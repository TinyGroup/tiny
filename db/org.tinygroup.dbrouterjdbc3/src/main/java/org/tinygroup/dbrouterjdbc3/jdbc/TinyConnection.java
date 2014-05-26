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
package org.tinygroup.dbrouterjdbc3.jdbc;

import org.enhydra.jdbc.standard.StandardXADataSource;
import org.objectweb.jotm.Jotm;
import org.tinygroup.commons.tools.StringUtil;
import org.tinygroup.dbrouter.RouterManager;
import org.tinygroup.dbrouter.config.*;
import org.tinygroup.dbrouter.factory.RouterManagerBeanFactory;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import javax.naming.NamingException;
import javax.transaction.TransactionManager;
import javax.transaction.UserTransaction;
import java.sql.*;
import java.util.*;

/**
 * 功能说明: tiny数据库连接对象
 * <p/>
 * <p/>
 * 开发人员: renhui <br>
 * 开发时间: 2013-12-11 <br>
 * <br>
 */
public class TinyConnection implements Connection {
    private boolean autoCommit = true;
    private boolean isClosed = false;
    private boolean isReadOnly = false;
    private int holdability = 1;
    private String catalog;
    private RouterManager manager = RouterManagerBeanFactory.getManager();
    private Router router;
    private List<Connection> connections = new ArrayList<Connection>();
    private Map<DataSourceConfigBean, StandardXADataSource> configDataSources = new HashMap<DataSourceConfigBean, StandardXADataSource>();
    private Map<Shard, Connection> dataSourceConnections = new HashMap<Shard, Connection>();// 一个数据源配置对应存储一个连接
    private int transactionIsolationLevel;
    private UserTransaction userTransaction;
    private Jotm jotm;
    private TransactionManager transactionManager;
    private String url;
    private Logger logger = LoggerFactory.getLogger(TinyConnection.class);

    public TinyConnection(String routerId) throws SQLException {
        router = manager.getRouter(routerId);
        this.url = "jdbc:dbrouter://" + routerId;
        List<Partition> partitions = router.getPartitions();
        try {
            jotm = new Jotm(true, false);
            userTransaction = jotm.getUserTransaction();
            transactionManager = jotm.getTransactionManager();
            for (Partition partition : partitions) {
                initPartitions(partition);
            }
        } catch (NamingException e) {
            throw new SQLException(e.getMessage());
        }
    }

    String getUrl() {
        return url;
    }

    String getUserName() {
        return router.getUserName();
    }

    private void initPartitions(Partition partition) throws SQLException {

        List<Shard> shards = partition.getShards();
        List<Shard> ableShards = new ArrayList<Shard>();
        int mode = partition.getMode();
        if (mode == Partition.MODE_SHARD) {
            for (Shard shard : shards) {
                DataSourceConfig config = router.getDataSourceConfig(shard
                        .getDataSourceId());
                try {
                    Connection connection = getConnection(shard, config);
                    shard.setConnection(this, connection);
                    connections.add(connection);
                    ableShards.add(shard);
                } catch (Exception e) {
                    logger.errorMessage(
                            "get connection error:{0},the shard:{1}", e,
                            config.getUrl(), shard.getId());
                    throw new RuntimeException(e);//分区时,如果有连接不可用,则往外抛出异常
                }
            }
        } else {
            for (Shard shard : shards) {
                DataSourceConfig config = router.getDataSourceConfig(shard
                        .getDataSourceId());
                try {
                    Connection connection = getConnection(shard, config);
                    shard.setConnection(this, connection);
                    connections.add(connection);
                    ableShards.add(shard);
                } catch (Exception e) {
                    logger.errorMessage(
                            "get connection error:{0},the shard:{1}", e,
                            config.getUrl(), shard.getId());
                    continue;//读写分离时，有连接出错，则继续获取下个shard的连接
                }
            }

        }
        for (int i = 0; i < ableShards.size(); i++) {
            Shard shard = ableShards.get(i);
            if (partition.getMode() == Partition.MODE_PRIMARY_SLAVE) {// 如果是主从模式,那么就取第一个数据源连接
                dataSourceConnections.put(shard, shard.getConnection(this));
                break;
            } else {
                if (!dataSourceConnections.containsKey(shard)) {
                    dataSourceConnections.put(shard, shard.getConnection(this));
                }
            }
        }
    }

    private Connection getConnection(Shard shard, DataSourceConfig config)
            throws SQLException {
        DataSourceConfigBean bean = config.getDataSourceConfigBean();
        StandardXADataSource dataSource = configDataSources.get(bean);
        if (dataSource == null) {
            dataSource = new StandardXADataSource();
            dataSource.setUrl(config.getUrl());
            dataSource.setDriverName(config.getDriver());
            dataSource.setUser(config.getUserName());
            dataSource.setPassword(config.getPassword());
            dataSource.setTransactionManager(transactionManager);
        }
        Connection connection = dataSource.getXAConnection().getConnection();
        Statement statement = connection.createStatement();
        String sql = config.getTestSql();
        if (!StringUtil.isBlank(sql)) {
            try {
                statement.execute(sql);
            } catch (SQLException e) {
                logger.errorMessage(
                        "connection:{0},执行测试语句：{1}出错,shard:{2},不可用", e,
                        config.getUrl(), sql, shard.getId());
                throw new RuntimeException(e);
            } finally {
                statement.close();
                connection.close();
            }
        }
        configDataSources.put(bean, dataSource);
        return connection;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public Collection<Connection> getDatasourceConnections() {
        return dataSourceConnections.values();
    }

    public Map<Shard, Connection> getDataSourceConnections() {
        return dataSourceConnections;
    }

    public Statement createStatement() throws SQLException {
        checkClosed();
        return new TinyStatement(router, this, ResultSet.TYPE_FORWARD_ONLY,
                ResultSet.CONCUR_READ_ONLY, false, autoCommit);
    }

    public PreparedStatement prepareStatement(String sql) throws SQLException {
        checkClosed();
        return new TinyPreparedStatement(router, this,
                ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, false,
                autoCommit, sql);
    }

    public CallableStatement prepareCall(String sql) throws SQLException {
        throw new SQLException("not support method");
    }

    /**
     * 内部管理的连接进行sql解释
     */
    public String nativeSQL(String sql) throws SQLException {
        checkClosed();
        for (Connection connection : connections) {
            connection.nativeSQL(sql);
        }
        return sql;
    }

    public synchronized void setAutoCommit(boolean autoCommit) throws SQLException {
        checkClosed();
        this.autoCommit = autoCommit;
        if (!autoCommit) {// 开启事务
            try {
                userTransaction.begin();
            } catch (Exception e) {
                logger.errorMessage("begin transaction error", e);
                throw new SQLException(e.getMessage());
            }
        }
    }

    public synchronized boolean getAutoCommit() throws SQLException {
        checkClosed();
        return autoCommit;
    }

    public synchronized void commit() throws SQLException {
        checkClosed();
        if (!autoCommit) {
            try {
                userTransaction.commit();
            } catch (Exception e) {
                logger.errorMessage("commit error", e);
                throw new SQLException(e.getMessage());
            }
            autoCommit = true;
        }
    }

    public synchronized void rollback() throws SQLException {
        checkClosed();
        if (!autoCommit) {
            try {
                userTransaction.rollback();
            } catch (Exception e) {
                logger.errorMessage("rollback  error", e);
                throw new SQLException(e.getMessage());
            }
            autoCommit = true;
        }
    }

    public void close() throws SQLException {
        boolean noError = true;
        StringBuffer buffer = new StringBuffer();
        for (Connection connection : connections) {
            try {
                connection.close();
            } catch (SQLException e) {
                buffer.append(String
                        .format("connection close error,errorcode:%s,sqlstate:%s,message:%s \n",
                                e.getErrorCode(), e.getSQLState(),
                                e.getMessage()));
                noError = false;
                logger.errorMessage("connection close error", e);
            }
        }
        //是否真正要关闭连接池中的空闲连接
        for (StandardXADataSource dataSource : configDataSources.values()) {
            dataSource.closeFreeConnection();
        }
        jotm.stop();
        isClosed = true;
        if (!noError) {
            throw new SQLException(buffer.toString());
        }
    }

    public boolean isClosed() throws SQLException {
        return isClosed;
    }

    void checkClosed() throws SQLException {
        if (isClosed) {
            throw new SQLException("connection is closed");
        }
    }

    public DatabaseMetaData getMetaData() throws SQLException {
        checkClosed();
        return new TinyDatabaseMetaData(this, router);
    }

    public void setReadOnly(boolean readOnly) throws SQLException {
        checkClosed();
        for (Connection connection : connections) {
            connection.setReadOnly(readOnly);
        }
        this.isReadOnly = readOnly;
    }

    public boolean isReadOnly() throws SQLException {
        checkClosed();
        return isReadOnly;
    }

    public void setCatalog(String catalog) throws SQLException {
        checkClosed();
        for (Connection connection : connections) {
            connection.setCatalog(catalog);
        }
        this.catalog = catalog;
    }

    public String getCatalog() throws SQLException {
        checkClosed();
        return catalog;
    }

    public void setTransactionIsolation(int level) throws SQLException {
        checkClosed();
        switch (level) {
            case Connection.TRANSACTION_READ_UNCOMMITTED:
            case Connection.TRANSACTION_READ_COMMITTED:
            case Connection.TRANSACTION_REPEATABLE_READ:
            case Connection.TRANSACTION_SERIALIZABLE:
                this.transactionIsolationLevel = level;
                break;
            default:
                throw new SQLException("not valid for the transaction level:"
                        + level);
        }
        for (Connection connection : connections) {
            connection.setTransactionIsolation(level);
        }
    }

    public int getTransactionIsolation() throws SQLException {
        checkClosed();
        return transactionIsolationLevel;
    }

    public SQLWarning getWarnings() throws SQLException {
        checkClosed();
        for (Connection connection : connections) {
            connection.getWarnings();
        }
        return null;
    }

    public void clearWarnings() throws SQLException {
        checkClosed();
        for (Connection connection : connections) {
            connection.clearWarnings();
        }
    }

    public synchronized Statement createStatement(int resultSetType, int resultSetConcurrency)
            throws SQLException {
        checkClosed();
        return new TinyStatement(router, this, resultSetType,
                resultSetConcurrency, false, autoCommit);
    }

    public synchronized PreparedStatement prepareStatement(String sql, int resultSetType,
                                                           int resultSetConcurrency) throws SQLException {
        checkClosed();
        return new TinyPreparedStatement(router, this, resultSetType,
                resultSetConcurrency, false, autoCommit, sql);
    }

    public synchronized CallableStatement prepareCall(String sql, int resultSetType,
                                                      int resultSetConcurrency) throws SQLException {
        throw new SQLException("not support method");
    }

    public Map<String, Class<?>> getTypeMap() throws SQLException {
        throw new SQLException("not support method");
    }

    public void setTypeMap(Map<String, Class<?>> map) throws SQLException {
        throw new SQLException("not support method");
    }

    public void setHoldability(int holdability) throws SQLException {
        checkClosed();
        checkHoldability(holdability);
        this.holdability = holdability;
    }

    private static void checkHoldability(int resultSetHoldability)
            throws SQLException {
        // ResultSet.HOLD_CURSORS_OVER_COMMIT
        if (resultSetHoldability != ResultSet.HOLD_CURSORS_OVER_COMMIT
                || resultSetHoldability != ResultSet.CLOSE_CURSORS_AT_COMMIT) {
            throw new SQLException("not valid value for resultSetHoldability:"
                    + resultSetHoldability);
        }
    }

    public int getHoldability() throws SQLException {
        checkClosed();
        return holdability;
    }

    public Savepoint setSavepoint() throws SQLException {
        throw new SQLException("not support method");
    }

    public Savepoint setSavepoint(String name) throws SQLException {
        throw new SQLException("not support method");
    }

    public void rollback(Savepoint savepoint) throws SQLException {
        checkClosed();
        throw new SQLException("not support method");
    }

    public void releaseSavepoint(Savepoint savepoint) throws SQLException {
        checkClosed();
        throw new SQLException("not support method");
    }

    public Statement createStatement(int resultSetType,
                                     int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        checkClosed();
        return new TinyStatement(router, this, resultSetType,
                resultSetConcurrency, false, autoCommit);
    }

    public PreparedStatement prepareStatement(String sql, int resultSetType,
                                              int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        checkClosed();
        return new TinyPreparedStatement(router, this, resultSetType,
                resultSetConcurrency, false, autoCommit, sql);
    }

    public CallableStatement prepareCall(String sql, int resultSetType,
                                         int resultSetConcurrency, int resultSetHoldability)
            throws SQLException {
        checkClosed();
        throw new SQLException("not support method");
    }

    public PreparedStatement prepareStatement(String sql, int autoGeneratedKeys)
            throws SQLException {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, int[] columnIndexes)
            throws SQLException {
        return prepareStatement(sql);
    }

    public PreparedStatement prepareStatement(String sql, String[] columnNames)
            throws SQLException {
        return prepareStatement(sql);
    }
}
