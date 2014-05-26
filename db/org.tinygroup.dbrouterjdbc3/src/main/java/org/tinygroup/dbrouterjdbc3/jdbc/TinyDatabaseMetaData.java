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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import org.tinygroup.dbrouter.config.Router;
import org.tinygroup.dbrouter.config.Shard;

/**
 * 
 * 功能说明:
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-10 <br>
 * <br>
 */
public class TinyDatabaseMetaData implements DatabaseMetaData {

	private TinyConnection tinyConnection;

	private Router router;

	private Map<Shard, Connection> dataSourceConnections = new HashMap<Shard, Connection>();// 一个数据源配置对应存储一个连接

	private Collection<Connection> connections;

	public TinyDatabaseMetaData(TinyConnection tinyConnection, Router router) {
		this.tinyConnection = tinyConnection;
		this.router = router;
		dataSourceConnections = tinyConnection.getDataSourceConnections();
		connections = dataSourceConnections.values();
	}

	public boolean allProceduresAreCallable() throws SQLException {
		return false;
	}

	public boolean allTablesAreSelectable() throws SQLException {
		return false;
	}

	public String getURL() throws SQLException {
		return tinyConnection.getUrl();
	}

	public String getUserName() throws SQLException {
		return tinyConnection.getUserName();
	}

	public boolean isReadOnly() throws SQLException {
		return tinyConnection.isReadOnly();
	}

	public boolean nullsAreSortedHigh() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedLow() throws SQLException {
		return !nullsAreSortedHigh();
	}

	public boolean nullsAreSortedAtStart() throws SQLException {
		return false;
	}

	public boolean nullsAreSortedAtEnd() throws SQLException {
		return false;
	}

	public String getDatabaseProductName() throws SQLException {
		return "dbrouter";
	}

	public String getDatabaseProductVersion() throws SQLException {
		return "1.0";
	}

	public String getDriverName() throws SQLException {
		return "dbrouter JDBC Driver";
	}

	public String getDriverVersion() throws SQLException {
		return "1.0";
	}

	public int getDriverMajorVersion() {
		return 1;
	}

	public int getDriverMinorVersion() {
		return 0;
	}

	public boolean usesLocalFiles() throws SQLException {
		return false;
	}

	public boolean usesLocalFilePerTable() throws SQLException {
		return false;
	}

	public boolean supportsMixedCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesUpperCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesLowerCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesMixedCaseIdentifiers() throws SQLException {
		return false;
	}

	public boolean supportsMixedCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesUpperCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesLowerCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public boolean storesMixedCaseQuotedIdentifiers() throws SQLException {
		return false;
	}

	public String getIdentifierQuoteString() throws SQLException {
		return "\"";
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public String getSQLKeywords() throws SQLException {
		String[] allKeywords = { "ACCESSIBLE", "ADD", "ALL", "ALTER",
				"ANALYZE", "AND", "AS", "ASC", "ASENSITIVE", "BEFORE",
				"BETWEEN", "BIGINT", "BINARY", "BLOB", "BOTH", "BY", "CALL",
				"CASCADE", "CASE", "CHANGE", "CHAR", "CHARACTER", "CHECK",
				"COLLATE", "COLUMN", "CONDITION", "CONNECTION", "CONSTRAINT",
				"CONTINUE", "CONVERT", "CREATE", "CROSS", "CURRENT_DATE",
				"CURRENT_TIME", "CURRENT_TIMESTAMP", "CURRENT_USER", "CURSOR",
				"DATABASE", "DATABASES", "DAY_HOUR", "DAY_MICROSECOND",
				"DAY_MINUTE", "DAY_SECOND", "DEC", "DECIMAL", "DECLARE",
				"DEFAULT", "DELAYED", "DELETE", "DESC", "DESCRIBE",
				"DETERMINISTIC", "DISTINCT", "DISTINCTROW", "DIV", "DOUBLE",
				"DROP", "DUAL", "EACH", "ELSE", "ELSEIF", "ENCLOSED",
				"ESCAPED", "EXISTS", "EXIT", "EXPLAIN", "FALSE", "FETCH",
				"FLOAT", "FLOAT4", "FLOAT8", "FOR", "FORCE", "FOREIGN", "FROM",
				"FULLTEXT", "GRANT", "GROUP", "HAVING", "HIGH_PRIORITY",
				"HOUR_MICROSECOND", "HOUR_MINUTE", "HOUR_SECOND", "IF",
				"IGNORE", "IN", "INDEX", "INFILE", "INNER", "INOUT",
				"INSENSITIVE", "INSERT", "INT", "INT1", "INT2", "INT3", "INT4",
				"INT8", "INTEGER", "INTERVAL", "INTO", "IS", "ITERATE", "JOIN",
				"KEY", "KEYS", "KILL", "LEADING", "LEAVE", "LEFT", "LIKE",
				"LIMIT", "LINEAR", "LINES", "LOAD", "LOCALTIME",
				"LOCALTIMESTAMP", "LOCK", "LONG", "LONGBLOB", "LONGTEXT",
				"LOOP", "LOW_PRIORITY", "MATCH", "MEDIUMBLOB", "MEDIUMINT",
				"MEDIUMTEXT", "MIDDLEINT", "MINUTE_MICROSECOND",
				"MINUTE_SECOND", "MOD", "MODIFIES", "NATURAL", "NOT",
				"NO_WRITE_TO_BINLOG", "NULL", "NUMERIC", "ON", "OPTIMIZE",
				"OPTION", "OPTIONALLY", "OR", "ORDER", "OUT", "OUTER",
				"OUTFILE", "PRECISION", "PRIMARY", "PROCEDURE", "PURGE",
				"RANGE", "READ", "READS", "READ_ONLY", "READ_WRITE", "REAL",
				"REFERENCES", "REGEXP", "RELEASE", "RENAME", "REPEAT",
				"REPLACE", "REQUIRE", "RESTRICT", "RETURN", "REVOKE", "RIGHT",
				"RLIKE", "SCHEMA", "SCHEMAS", "SECOND_MICROSECOND", "SELECT",
				"SENSITIVE", "SEPARATOR", "SET", "SHOW", "SMALLINT", "SPATIAL",
				"SPECIFIC", "SQL", "SQLEXCEPTION", "SQLSTATE", "SQLWARNING",
				"SQL_BIG_RESULT", "SQL_CALC_FOUND_ROWS", "SQL_SMALL_RESULT",
				"SSL", "STARTING", "STRAIGHT_JOIN", "TABLE", "TERMINATED",
				"THEN", "TINYBLOB", "TINYINT", "TINYTEXT", "TO", "TRAILING",
				"TRIGGER", "TRUE", "UNDO", "UNION", "UNIQUE", "UNLOCK",
				"UNSIGNED", "UPDATE", "USAGE", "USE", "USING", "UTC_DATE",
				"UTC_TIME", "UTC_TIMESTAMP", "VALUES", "VARBINARY", "VARCHAR",
				"VARCHARACTER", "VARYING", "WHEN", "WHERE", "WHILE", "WITH",
				"WRITE", "X509", "XOR", "YEAR_MONTH", "ZEROFILL", "MINUS",
				"ROWNUM", "SYSDATE", "SYSTIME", "SYSTIMESTAMP", "TODAY" };
		String[] sql92Keywords = { "ABSOLUTE", "EXEC", "OVERLAPS", "ACTION",
				"EXECUTE", "PAD", "ADA", "EXISTS", "PARTIAL", "ADD",
				"EXTERNAL", "PASCAL", "ALL", "EXTRACT", "POSITION", "ALLOCATE",
				"FALSE", "PRECISION", "ALTER", "FETCH", "PREPARE", "AND",
				"FIRST", "PRESERVE", "ANY", "FLOAT", "PRIMARY", "ARE", "FOR",
				"PRIOR", "AS", "FOREIGN", "PRIVILEGES", "ASC", "FORTRAN",
				"PROCEDURE", "ASSERTION", "FOUND", "PUBLIC", "AT", "FROM",
				"READ", "AUTHORIZATION", "FULL", "REAL", "AVG", "GET",
				"REFERENCES", "BEGIN", "GLOBAL", "RELATIVE", "BETWEEN", "GO",
				"RESTRICT", "BIT", "GOTO", "REVOKE", "BIT_LENGTH", "GRANT",
				"RIGHT", "BOTH", "GROUP", "ROLLBACK", "BY", "HAVING", "ROWS",
				"CASCADE", "HOUR", "SCHEMA", "CASCADED", "IDENTITY", "SCROLL",
				"CASE", "IMMEDIATE", "SECOND", "CAST", "IN", "SECTION",
				"CATALOG", "INCLUDE", "SELECT", "CHAR", "INDEX", "SESSION",
				"CHAR_LENGTH", "INDICATOR", "SESSION_USER", "CHARACTER",
				"INITIALLY", "SET", "CHARACTER_LENGTH", "INNER", "SIZE",
				"CHECK", "INPUT", "SMALLINT", "CLOSE", "INSENSITIVE", "SOME",
				"COALESCE", "INSERT", "SPACE", "COLLATE", "INT", "SQL",
				"COLLATION", "INTEGER", "SQLCA", "COLUMN", "INTERSECT",
				"SQLCODE", "COMMIT", "INTERVAL", "SQLERROR", "CONNECT", "INTO",
				"SQLSTATE", "CONNECTION", "IS", "SQLWARNING", "CONSTRAINT",
				"ISOLATION", "SUBSTRING", "CONSTRAINTS", "JOIN", "SUM",
				"CONTINUE", "KEY", "SYSTEM_USER", "CONVERT", "LANGUAGE",
				"TABLE", "CORRESPONDING", "LAST", "TEMPORARY", "COUNT",
				"LEADING", "THEN", "CREATE", "LEFT", "TIME", "CROSS", "LEVEL",
				"TIMESTAMP", "CURRENT", "LIKE", "TIMEZONE_HOUR",
				"CURRENT_DATE", "LOCAL", "TIMEZONE_MINUTE", "CURRENT_TIME",
				"LOWER", "TO", "CURRENT_TIMESTAMP", "MATCH", "TRAILING",
				"CURRENT_USER", "MAX", "TRANSACTION", "CURSOR", "MIN",
				"TRANSLATE", "DATE", "MINUTE", "TRANSLATION", "DAY", "MODULE",
				"TRIM", "DEALLOCATE", "MONTH", "TRUE", "DEC", "NAMES", "UNION",
				"DECIMAL", "NATIONAL", "UNIQUE", "DECLARE", "NATURAL",
				"UNKNOWN", "DEFAULT", "NCHAR", "UPDATE", "DEFERRABLE", "NEXT",
				"UPPER", "DEFERRED", "NO", "USAGE", "DELETE", "NONE", "USER",
				"DESC", "NOT", "USING", "DESCRIBE", "NULL", "VALUE",
				"DESCRIPTOR", "NULLIF", "VALUES", "DIAGNOSTICS", "NUMERIC",
				"VARCHAR", "DISCONNECT", "OCTET_LENGTH", "VARYING", "DISTINCT",
				"OF", "VIEW", "DOMAIN", "ON", "WHEN", "DOUBLE", "ONLY",
				"WHENEVER", "DROP", "OPEN", "WHERE", "ELSE", "OPTION", "WITH",
				"END", "OR", "WORK", "END-EXEC", "ORDER", "WRITE", "ESCAPE",
				"OUTER", "YEAR", "EXCEPT", "OUTPUT", "ZONE", "EXCEPTION" };
		TreeMap mySQLKeywordMap = new TreeMap();
		for (int i = 0; i < allKeywords.length; ++i) {
			mySQLKeywordMap.put(allKeywords[i], null);
		}
		HashMap sql92KeywordMap = new HashMap(sql92Keywords.length);
		for (int i = 0; i < sql92Keywords.length; ++i) {
			sql92KeywordMap.put(sql92Keywords[i], null);
		}
		Iterator it = sql92KeywordMap.keySet().iterator();
		while (it.hasNext()) {
			mySQLKeywordMap.remove(it.next());
		}
		StringBuffer keywordBuf = new StringBuffer();
		it = mySQLKeywordMap.keySet().iterator();
		if (it.hasNext()) {
			keywordBuf.append(it.next().toString());
		}
		while (it.hasNext()) {
			keywordBuf.append(",");
			keywordBuf.append(it.next().toString());
		}
		return keywordBuf.toString();
	}

	public String getNumericFunctions() throws SQLException {
		return "ABS,ACOS,ASIN,ATAN,ATAN2,BIT_COUNT,CEILING,COS,COT,DEGREES,EXP,FLOOR,LOG,LOG10,MAX,MIN,MOD,PI,POW,POWER,RADIANS,RAND,ROUND,SIN,SQRT,TAN,TRUNCATE";
	}

	public String getStringFunctions() throws SQLException {
		return "ASCII,BIN,BIT_LENGTH,CHAR,CHARACTER_LENGTH,CHAR_LENGTH,CONCAT,CONCAT_WS,CONV,ELT,EXPORT_SET,FIELD,FIND_IN_SET,HEX,INSERT,INSTR,LCASE,LEFT,LENGTH,LOAD_FILE,LOCATE,LOCATE,LOWER,LPAD,LTRIM,MAKE_SET,MATCH,MID,OCT,OCTET_LENGTH,ORD,POSITION,QUOTE,REPEAT,REPLACE,REVERSE,RIGHT,RPAD,RTRIM,SOUNDEX,SPACE,STRCMP,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING,SUBSTRING_INDEX,TRIM,UCASE,UPPER";
	}

	public String getSystemFunctions() throws SQLException {
		return "DATABASE,USER,SYSTEM_USER,SESSION_USER,PASSWORD,ENCRYPT,LAST_INSERT_ID,VERSION";
	}

	public String getTimeDateFunctions() throws SQLException {
		return "DAYOFWEEK,WEEKDAY,DAYOFMONTH,DAYOFYEAR,MONTH,DAYNAME,MONTHNAME,QUARTER,WEEK,YEAR,HOUR,MINUTE,SECOND,PERIOD_ADD,PERIOD_DIFF,TO_DAYS,FROM_DAYS,DATE_FORMAT,TIME_FORMAT,CURDATE,CURRENT_DATE,CURTIME,CURRENT_TIME,NOW,SYSDATE,CURRENT_TIMESTAMP,UNIX_TIMESTAMP,FROM_UNIXTIME,SEC_TO_TIME,TIME_TO_SEC";
	}

	public String getSearchStringEscape() throws SQLException {
		return "\\";
	}

	public String getExtraNameCharacters() throws SQLException {
		return "";
	}

	public boolean supportsAlterTableWithAddColumn() throws SQLException {
		return true;
	}

	public boolean supportsAlterTableWithDropColumn() throws SQLException {
		return true;
	}

	public boolean supportsColumnAliasing() throws SQLException {
		return true;
	}

	public boolean nullPlusNonNullIsNull() throws SQLException {
		return true;
	}

	public boolean supportsConvert() throws SQLException {
		return true;
	}

	public boolean supportsConvert(int fromType, int toType)
			throws SQLException {
		return true;
	}

	public boolean supportsTableCorrelationNames() throws SQLException {
		return true;
	}

	public boolean supportsDifferentTableCorrelationNames() throws SQLException {
		return false;
	}

	public boolean supportsExpressionsInOrderBy() throws SQLException {
		return true;
	}

	public boolean supportsOrderByUnrelated() throws SQLException {
		return true;
	}

	public boolean supportsGroupBy() throws SQLException {
		return true;
	}

	public boolean supportsGroupByUnrelated() throws SQLException {
		return true;
	}

	public boolean supportsGroupByBeyondSelect() throws SQLException {
		return true;
	}

	public boolean supportsLikeEscapeClause() throws SQLException {
		return true;
	}

	public boolean supportsMultipleResultSets() throws SQLException {
		return false;
	}

	public boolean supportsMultipleTransactions() throws SQLException {
		return true;
	}

	public boolean supportsNonNullableColumns() throws SQLException {
		return true;
	}

	public boolean supportsMinimumSQLGrammar() throws SQLException {
		return true;
	}

	public boolean supportsCoreSQLGrammar() throws SQLException {
		return true;
	}

	public boolean supportsExtendedSQLGrammar() throws SQLException {
		return false;
	}

	public boolean supportsANSI92EntryLevelSQL() throws SQLException {
		return true;
	}

	public boolean supportsANSI92IntermediateSQL() throws SQLException {
		return false;
	}

	public boolean supportsANSI92FullSQL() throws SQLException {
		return false;
	}

	public boolean supportsIntegrityEnhancementFacility() throws SQLException {
		return true;
	}

	public boolean supportsOuterJoins() throws SQLException {
		return true;
	}

	public boolean supportsFullOuterJoins() throws SQLException {
		return true;
	}

	public boolean supportsLimitedOuterJoins() throws SQLException {
		return true;
	}

	public String getSchemaTerm() throws SQLException {
		return "schema";
	}

	public String getProcedureTerm() throws SQLException {
		return "procedure";
	}

	public String getCatalogTerm() throws SQLException {
		return "catalog";
	}

	public boolean isCatalogAtStart() throws SQLException {
		return true;
	}

	public String getCatalogSeparator() throws SQLException {
		return ".";
	}

	public boolean supportsSchemasInDataManipulation() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInProcedureCalls() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInTableDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInIndexDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsSchemasInPrivilegeDefinitions() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInDataManipulation() throws SQLException {
		return true;
	}

	public boolean supportsCatalogsInProcedureCalls() throws SQLException {
		return false;
	}

	public boolean supportsCatalogsInTableDefinitions() throws SQLException {
		return true;
	}

	public boolean supportsCatalogsInIndexDefinitions() throws SQLException {
		return true;
	}

	public boolean supportsCatalogsInPrivilegeDefinitions() throws SQLException {
		return true;
	}

	public boolean supportsPositionedDelete() throws SQLException {
		return true;
	}

	public boolean supportsPositionedUpdate() throws SQLException {
		return true;
	}

	public boolean supportsSelectForUpdate() throws SQLException {
		return true;
	}

	public boolean supportsStoredProcedures() throws SQLException {
		return false;
	}

	public boolean supportsSubqueriesInComparisons() throws SQLException {
		return true;
	}

	public boolean supportsSubqueriesInExists() throws SQLException {
		return true;
	}

	public boolean supportsSubqueriesInIns() throws SQLException {
		return true;
	}

	public boolean supportsSubqueriesInQuantifieds() throws SQLException {
		return true;
	}

	public boolean supportsCorrelatedSubqueries() throws SQLException {
		return true;
	}

	public boolean supportsUnion() throws SQLException {
		return true;
	}

	public boolean supportsUnionAll() throws SQLException {
		return true;
	}

	public boolean supportsOpenCursorsAcrossCommit() throws SQLException {
		return false;
	}

	public boolean supportsOpenCursorsAcrossRollback() throws SQLException {
		return false;
	}

	public boolean supportsOpenStatementsAcrossCommit() throws SQLException {
		return true;
	}

	public boolean supportsOpenStatementsAcrossRollback() throws SQLException {
		return true;
	}

	public int getMaxBinaryLiteralLength() throws SQLException {
		return 16777208;
	}

	public int getMaxCharLiteralLength() throws SQLException {
		return 16777208;
	}

	public int getMaxColumnNameLength() throws SQLException {
		return 64;
	}

	public int getMaxColumnsInGroupBy() throws SQLException {
		return 64;
	}

	public int getMaxColumnsInIndex() throws SQLException {
		return 16;
	}

	public int getMaxColumnsInOrderBy() throws SQLException {
		return 64;
	}

	public int getMaxColumnsInSelect() throws SQLException {
		return 256;
	}

	public int getMaxColumnsInTable() throws SQLException {
		return 512;
	}

	public int getMaxConnections() throws SQLException {
		return 0;
	}

	public int getMaxCursorNameLength() throws SQLException {
		return 64;
	}

	public int getMaxIndexLength() throws SQLException {
		return 256;
	}

	public int getMaxSchemaNameLength() throws SQLException {
		return 0;
	}

	public int getMaxProcedureNameLength() throws SQLException {
		return 0;
	}

	public int getMaxCatalogNameLength() throws SQLException {
		return 32;
	}

	public int getMaxRowSize() throws SQLException {
		return 2147483639;
	}

	public boolean doesMaxRowSizeIncludeBlobs() throws SQLException {
		return false;
	}

	public int getMaxStatementLength() throws SQLException {
		return 0;
	}

	public int getMaxStatements() throws SQLException {
		return 0;
	}

	public int getMaxTableNameLength() throws SQLException {
		return 64;
	}

	public int getMaxTablesInSelect() throws SQLException {
		return 256;
	}

	public int getMaxUserNameLength() throws SQLException {
		return 16;
	}

	public int getDefaultTransactionIsolation() throws SQLException {
		return Connection.TRANSACTION_READ_COMMITTED;
	}

	public boolean supportsTransactions() throws SQLException {
		return true;
	}

	public boolean supportsTransactionIsolationLevel(int level)
			throws SQLException {
		return true;
	}

	public boolean supportsDataDefinitionAndDataManipulationTransactions()
			throws SQLException {
		return false;
	}

	public boolean supportsDataManipulationTransactionsOnly()
			throws SQLException {
		return true;
	}

	public boolean dataDefinitionCausesTransactionCommit() throws SQLException {
		return true;
	}

	public boolean dataDefinitionIgnoredInTransactions() throws SQLException {
		return false;
	}

	public ResultSet getProcedures(String catalog, String schemaPattern,
			String procedureNamePattern) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : dataSourceConnections.values()) {
			resultSets.add(connection.getMetaData().getProcedures(catalog,
					schemaPattern, procedureNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getProcedureColumns(String catalog, String schemaPattern,
			String procedureNamePattern, String columnNamePattern)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getProcedureColumns(
					catalog, schemaPattern, procedureNamePattern,
					columnNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getTables(String catalog, String schemaPattern,
			String tableNamePattern, String[] types) throws SQLException {
		ResultFilter filter=new ResultFilter(catalog, schemaPattern, tableNamePattern, types, dataSourceConnections);
		return filter.filter();
	}

	public ResultSet getSchemas() throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getSchemas());
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getCatalogs() throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getCatalogs());
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getTableTypes() throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getTableTypes());
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getColumns(String catalog, String schemaPattern,
			String tableNamePattern, String columnNamePattern)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getColumns(catalog,
					schemaPattern, tableNamePattern, columnNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getColumnPrivileges(String catalog, String schema,
			String table, String columnNamePattern) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getColumnPrivileges(
					catalog, schema, table, columnNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getTablePrivileges(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getTablePrivileges(catalog,
					schemaPattern, tableNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getBestRowIdentifier(String catalog, String schema,
			String table, int scope, boolean nullable) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getBestRowIdentifier(
					catalog, schema, table, scope, nullable));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getVersionColumns(String catalog, String schema,
			String table) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getVersionColumns(catalog,
					schema, table));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getPrimaryKeys(String catalog, String schema, String table)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getPrimaryKeys(catalog,
					schema, table));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getImportedKeys(String catalog, String schema, String table)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getImportedKeys(catalog,
					schema, table));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getExportedKeys(String catalog, String schema, String table)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getExportedKeys(catalog,
					schema, table));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getCrossReference(String primaryCatalog,
			String primarySchema, String primaryTable, String foreignCatalog,
			String foreignSchema, String foreignTable) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getCrossReference(
					primaryCatalog, primarySchema, primaryTable,
					foreignCatalog, foreignSchema, foreignTable));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getTypeInfo() throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getTypeInfo());
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getIndexInfo(String catalog, String schema, String table,
			boolean unique, boolean approximate) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getExportedKeys(catalog,
					schema, table));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public boolean supportsResultSetType(int type) throws SQLException {
		return type != ResultSet.TYPE_SCROLL_SENSITIVE;
	}

	public boolean supportsResultSetConcurrency(int type, int concurrency)
			throws SQLException {
		return false;
	}

	public boolean ownUpdatesAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean ownDeletesAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean ownInsertsAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean othersUpdatesAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean othersDeletesAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean othersInsertsAreVisible(int type) throws SQLException {
		return false;
	}

	public boolean updatesAreDetected(int type) throws SQLException {
		return false;
	}

	public boolean deletesAreDetected(int type) throws SQLException {
		return false;
	}

	public boolean insertsAreDetected(int type) throws SQLException {
		return false;
	}

	public boolean supportsBatchUpdates() throws SQLException {
		return true;
	}

	public ResultSet getUDTs(String catalog, String schemaPattern,
			String typeNamePattern, int[] types) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getUDTs(catalog,
					schemaPattern, typeNamePattern, types));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public Connection getConnection() throws SQLException {
		return tinyConnection;
	}

	public boolean supportsSavepoints() throws SQLException {
		return true;
	}

	public boolean supportsNamedParameters() throws SQLException {
		return false;
	}

	public boolean supportsMultipleOpenResults() throws SQLException {
		return true;
	}

	public boolean supportsGetGeneratedKeys() throws SQLException {
		return true;
	}

	public ResultSet getSuperTypes(String catalog, String schemaPattern,
			String typeNamePattern) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getSuperTypes(catalog,
					schemaPattern, typeNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getSuperTables(String catalog, String schemaPattern,
			String tableNamePattern) throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getSuperTables(catalog,
					schemaPattern, tableNamePattern));
		}
		return new TinyResultSetCombine(resultSets);
	}

	public ResultSet getAttributes(String catalog, String schemaPattern,
			String typeNamePattern, String attributeNamePattern)
			throws SQLException {
		List<ResultSet> resultSets = new ArrayList<ResultSet>();
		for (Connection connection : connections) {
			resultSets.add(connection.getMetaData().getAttributes(catalog,
					schemaPattern, typeNamePattern, attributeNamePattern));
		}
		return new TinyResultSetCombine(resultSets);

	}

	public boolean supportsResultSetHoldability(int holdability)
			throws SQLException {
		return holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	public int getResultSetHoldability() throws SQLException {
		return ResultSet.CLOSE_CURSORS_AT_COMMIT;
	}

	public int getDatabaseMajorVersion() throws SQLException {
		return 1;
	}

	public int getDatabaseMinorVersion() throws SQLException {
		return 3;
	}

	public int getJDBCMajorVersion() throws SQLException {
		return 3;
	}

	public int getJDBCMinorVersion() throws SQLException {
		return 0;
	}

	public int getSQLStateType() throws SQLException {
		return 2;
	}

	public boolean locatorsUpdateCopy() throws SQLException {
		return false;
	}

	public boolean supportsStatementPooling() throws SQLException {
		return false;
	}

	class ResultFilter{
		private List<ResultSet> resultSets;
		private Map<String, String> tableMapping=new HashMap<String, String>();
		private Set<String> tables=new HashSet<String>();
		private TinyResultSetSimple simple;
		public ResultFilter(String catalog, String schemaPattern,
				String tableNamePattern, String[] types,Map<Shard, Connection> dataSourceConnections) throws SQLException {
			super();
			resultSets=new ArrayList<ResultSet>();
			for (Shard shard : dataSourceConnections.keySet()) {
				resultSets.add(dataSourceConnections.get(shard).getMetaData().getTables(catalog, schemaPattern, tableNamePattern, types));
				Map<String, String> mapping=shard.getTableMappingMap();
				if(mapping!=null){
					for (String key : mapping.keySet()) {
						tableMapping.put(mapping.get(key), key);
					}
				}
			}
			simple=new TinyResultSetSimple(resultSets);
		}
		
		public ResultSet filter() throws SQLException{
			while (simple.next()) {
				String tableName = simple.getString("TABLE_NAME");
				String catalog=simple.getString("TABLE_CAT");
				if(catalog==null){
					catalog="";
				}
				String schema=simple.getString("TABLE_SCHEM");
				if(schema==null){
					schema="";
				}
				String fullTableName=catalog+schema+tableName;
				if(!tables.contains(fullTableName)){
					String updateTableName=getUpdateTableName(tableName,tableMapping);
					if(updateTableName!=null){
						String fullUpdateTableName=catalog+schema+updateTableName;
						if(!tables.contains(fullUpdateTableName)){
							simple.updateRow("TABLE_NAME", updateTableName);
							tables.add(fullUpdateTableName);
						}else{//删除重复的
							simple.removeRow();
						}
					}
					tables.add(fullTableName);
				}else{//删除重复的
					simple.removeRow();
				}
				
			}
			simple.beforeFirst();//重新定位到第一条前
			return simple;
			
		}
		
		private String getUpdateTableName(String tableName,
				Map<String, String> tableMapping) {
			return tableMapping.get(tableName);
		}
		
		
	}
	
}
