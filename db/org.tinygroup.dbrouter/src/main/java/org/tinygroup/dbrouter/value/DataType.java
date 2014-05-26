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
package org.tinygroup.dbrouter.value;

import java.math.BigDecimal;
import java.sql.Array;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.tinygroup.dbrouter.exception.DbrouterRuntimeException;

/**
 * This class contains meta data information about data types,
 * and can convert between Java objects and Values.
 */
public class DataType {

	  /**
     * This constant is used to represent the type of a ResultSet. There is no
     * equivalent java.sql.Types value, but Oracle uses it to represent a
     * ResultSet (OracleTypes.CURSOR = -10).
     */
    public static final int TYPE_RESULT_SET = -10;
    private static final List<DataType> TYPES = new ArrayList<DataType>();
    private static final List<DataType> TYPES_BY_VALUE_TYPE = new ArrayList<DataType>();
    private static final Map<String, DataType> TYPES_BY_NAME = new HashMap<String, DataType>();
    
    /**
     * The value type of this data type.
     */
    private int type;

    /**
     * The data type name.
     */
    private String name;

    /**
     * The SQL type.
     */
    private int sqlType;

    /**
     * The Java class name.
     */
    private String jdbc;

    /**
     * How closely the data type maps to the corresponding JDBC SQL type (low is
     * best).
     */
    private int sqlTypePos;

    /**
     * The maximum supported precision.
     */
    private long maxPrecision;

    /**
     * The lowest possible scale.
     */
    private int minScale;

    /**
     * The highest possible scale.
     */
    private int maxScale;

    /**
     * If this is a numeric type.
     */
    private boolean decimal;

    /**
     * The prefix required for the SQL literal representation.
     */
    private String prefix;

    /**
     * The suffix required for the SQL literal representation.
     */
    private String suffix;

    /**
     * The list of parameters used in the column definition.
     */
    private String params;

    /**
     * If this is an autoincrement type.
     */
    private boolean autoIncrement;

    /**
     * If this data type is an autoincrement type.
     */
    private boolean caseSensitive;

    /**
     * If the precision parameter is supported.
     */
    private boolean supportsPrecision;

    /**
     * If the scale parameter is supported.
     */
    private boolean supportsScale;

    /**
     * The default precision.
     */
    private long defaultPrecision;

    /**
     * The default scale.
     */
    private int defaultScale;

    /**
     * The default display size.
     */
    private int defaultDisplaySize;

    /**
     * If this data type should not be listed in the database meta data.
     */
    private boolean hidden;

    
    static {
        for (int i = 0; i < Value.TYPE_COUNT; i++) {
            TYPES_BY_VALUE_TYPE.add(null);
        }
        add(Value.NULL, Types.NULL, "Null",
                new DataType(),
                new String[]{"NULL"},
                // the value is always in the cache
                0
        );
        add(Value.STRING, Types.VARCHAR, "String",
                createString(true),
                new String[]{"VARCHAR", "VARCHAR2", "NVARCHAR", "NVARCHAR2", "VARCHAR_CASESENSITIVE", "CHARACTER VARYING", "TID"},
                // 24 for ValueString, 24 for String
                48
        );
        add(Value.STRING, Types.LONGVARCHAR, "String",
                createString(true),
                new String[]{"LONGVARCHAR", "LONGNVARCHAR"},
                48
        );
        add(Value.STRING_FIXED, Types.CHAR, "String",
                createString(true),
                new String[]{"CHAR", "CHARACTER", "NCHAR"},
                48
        );
        add(Value.STRING_IGNORECASE, Types.VARCHAR, "String",
                createString(false),
                new String[]{"VARCHAR_IGNORECASE"},
                48
        );
        add(Value.BOOLEAN, Types.BOOLEAN, "Boolean",
                createDecimal(1, 1, 0, 5, false, false),
                new String[]{"BOOLEAN", "BIT", "BOOL"},
                // the value is always in the cache
                0
        );
        add(Value.BYTE, Types.TINYINT, "Byte",
                createDecimal(3, 3, 0,4, false, false),
                new String[]{"TINYINT"},
                // the value is almost always in the cache
                1
        );
        add(Value.SHORT, Types.SMALLINT, "Short",
                createDecimal(5, 5, 0, 6, false, false),
                new String[]{"SMALLINT", "YEAR", "INT2"},
                // in many cases the value is in the cache
                20
        );
        add(Value.INT, Types.INTEGER, "Int",
                createDecimal(10, 10, 0,
                        11, false, false),
                new String[]{"INTEGER", "INT", "MEDIUMINT", "INT4", "SIGNED"},
                // in many cases the value is in the cache
                20
        );
        add(Value.INT, Types.INTEGER, "Int",
                createDecimal(10, 10, 0,
                        11, false, true),
                new String[]{"SERIAL"},
                20
        );
        add(Value.LONG, Types.BIGINT, "Long",
                createDecimal(19,19, 0,
                        20, false, false),
                new String[]{"BIGINT", "INT8", "LONG"},
                24
        );
        add(Value.LONG, Types.BIGINT, "Long",
                createDecimal(19, 19, 0,
                        20, false, true),
                new String[]{"IDENTITY", "BIGSERIAL"},
                24
        );
        add(Value.DECIMAL, Types.DECIMAL, "BigDecimal",
                createDecimal(Integer.MAX_VALUE, 65535,
                		32767, 65535, true, false),
                new String[]{"DECIMAL", "DEC"},
                // 40 for ValueDecimal,
                64
        );
        add(Value.DECIMAL, Types.NUMERIC, "BigDecimal",
                createDecimal(Integer.MAX_VALUE, 65535,
                		32767, 65535, true, false),
                new String[]{"NUMERIC", "NUMBER"},
                64
        );
        add(Value.FLOAT, Types.REAL, "Float",
                createDecimal(7, 7,
                        0, 15, false, false),
                new String[] {"REAL", "FLOAT4"},
                24
        );
        add(Value.DOUBLE, Types.DOUBLE, "Double",
                createDecimal(17, 17,
                        0, 24, false, false),
                new String[] { "DOUBLE", "DOUBLE PRECISION" },
                24
        );
        add(Value.DOUBLE, Types.FLOAT, "Double",
                createDecimal(17, 17,
                        0, 24, false, false),
                new String[] {"FLOAT", "FLOAT8" },
                24
        );
        add(Value.TIME, Types.TIME, "Time",
                createDate(6, "TIME", 0, 8),
                new String[]{"TIME"},
                // 24 for ValueTime, 32 for java.sql.Time
                56
        );
        add(Value.DATE, Types.DATE, "Date",
                createDate(8, "DATE", 0, 10),
                new String[]{"DATE"},
                // 24 for ValueDate, 32 for java.sql.Data
                56
        );
        add(Value.TIMESTAMP, Types.TIMESTAMP, "Timestamp",
                createDate(23, "TIMESTAMP", 10, 23),
                new String[]{"TIMESTAMP", "DATETIME", "SMALLDATETIME"},
                // 24 for ValueTimestamp, 32 for java.sql.Timestamp
                56
        );
        add(Value.BYTES, Types.VARBINARY, "Bytes",
                createString(false),
                new String[]{"VARBINARY"},
                32
        );
        add(Value.BYTES, Types.BINARY, "Bytes",
                createString(false),
                new String[]{"BINARY", "RAW", "BYTEA", "LONG RAW"},
                32
        );
        add(Value.BYTES, Types.LONGVARBINARY, "Bytes",
                createString(false),
                new String[]{"LONGVARBINARY"},
                32
        );
        add(Value.UUID, Types.BINARY, "Bytes",
                createString(false),
                new String[]{"UUID"},
                32
        );
        add(Value.JAVA_OBJECT, Types.OTHER, "Object",
                createString(false),
                new String[]{"OTHER", "OBJECT", "JAVA_OBJECT"},
                24
        );
        add(Value.BLOB, Types.BLOB, "Blob",
                createLob(),
                new String[]{"BLOB", "TINYBLOB", "MEDIUMBLOB", "LONGBLOB", "IMAGE", "OID"},
                // 80 for ValueLob, 24 for String
                104
        );
        add(Value.CLOB, Types.CLOB, "Clob",
                createLob(),
                new String[]{"CLOB", "TINYTEXT", "TEXT", "MEDIUMTEXT", "LONGTEXT", "NTEXT", "NCLOB"},
                // 80 for ValueLob, 24 for String
                104
        );
        DataType dataType = new DataType();
        dataType.prefix = "(";
        dataType.suffix = "')";
        add(Value.ARRAY, Types.ARRAY, "Array",
                dataType,
                new String[]{"ARRAY"},
                32
        );
        dataType = new DataType();
        add(Value.RESULT_SET, DataType.TYPE_RESULT_SET, "ResultSet",
                dataType,
                new String[]{"RESULT_SET"},
                400
        );
    }

    private static void add(int type, int sqlType, String jdbc, DataType dataType, String[] names, int memory) {
        for (int i = 0; i < names.length; i++) {
            DataType dt = new DataType();
            dt.type = type;
            dt.sqlType = sqlType;
            dt.jdbc = jdbc;
            dt.name = names[i];
            dt.autoIncrement = dataType.autoIncrement;
            dt.decimal = dataType.decimal;
            dt.maxPrecision = dataType.maxPrecision;
            dt.maxScale = dataType.maxScale;
            dt.minScale = dataType.minScale;
            dt.params = dataType.params;
            dt.prefix = dataType.prefix;
            dt.suffix = dataType.suffix;
            dt.supportsPrecision = dataType.supportsPrecision;
            dt.supportsScale = dataType.supportsScale;
            dt.defaultPrecision = dataType.defaultPrecision;
            dt.defaultScale = dataType.defaultScale;
            dt.defaultDisplaySize = dataType.defaultDisplaySize;
            dt.caseSensitive = dataType.caseSensitive;
            dt.hidden = i > 0;
            for (DataType t2 : TYPES) {
                if (t2.sqlType == dt.sqlType) {
                    dt.sqlTypePos++;
                }
            }
            TYPES_BY_NAME.put(dt.name, dt);
            if (TYPES_BY_VALUE_TYPE.get(type) == null) {
                TYPES_BY_VALUE_TYPE.set(type, dt);
            }
            TYPES.add(dt);
        }
    }

    private static DataType createDecimal(int maxPrecision,
            int defaultPrecision, int defaultScale, int defaultDisplaySize,
            boolean needsPrecisionAndScale, boolean autoInc) {
        DataType dataType = new DataType();
        dataType.maxPrecision = maxPrecision;
        dataType.defaultPrecision = defaultPrecision;
        dataType.defaultScale = defaultScale;
        dataType.defaultDisplaySize = defaultDisplaySize;
        if (needsPrecisionAndScale) {
            dataType.params = "PRECISION,SCALE";
            dataType.supportsPrecision = true;
            dataType.supportsScale = true;
        }
        dataType.decimal = true;
        dataType.autoIncrement = autoInc;
        return dataType;
    }

    private static DataType createDate(int precision, String prefix, int scale, int displaySize) {
        DataType dataType = new DataType();
        dataType.prefix = prefix + " '";
        dataType.suffix = "'";
        dataType.maxPrecision = precision;
        dataType.supportsScale = scale != 0;
        dataType.maxScale = scale;
        dataType.defaultPrecision = precision;
        dataType.defaultScale = scale;
        dataType.defaultDisplaySize = displaySize;
        return dataType;
    }

    private static DataType createString(boolean caseSensitive) {
        DataType dataType = new DataType();
        dataType.prefix = "'";
        dataType.suffix = "'";
        dataType.params = "LENGTH";
        dataType.caseSensitive = caseSensitive;
        dataType.supportsPrecision = true;
        dataType.maxPrecision = Integer.MAX_VALUE;
        dataType.defaultPrecision = Integer.MAX_VALUE;
        dataType.defaultDisplaySize = Integer.MAX_VALUE;
        return dataType;
    }

    private static DataType createLob() {
        DataType t = createString(true);
        t.maxPrecision = Long.MAX_VALUE;
        t.defaultPrecision = Long.MAX_VALUE;
        return t;
    }
    
    /**
     * Get the list of data types.
     *
     * @return the list
     */
    public static List<DataType> getTypes() {
        return TYPES;
    }
    
    /**
     * Get the name of the Java class for the given value type.
     *
     * @param type the value type
     * @return the class name
     */
    public static String getTypeClassName(int type) {
        switch(type) {
        case Value.BOOLEAN:
            // "java.lang.Boolean";
            return Boolean.class.getName();
        case Value.BYTE:
            // "java.lang.Byte";
            return Byte.class.getName();
        case Value.SHORT:
            // "java.lang.Short";
            return Short.class.getName();
        case Value.INT:
            // "java.lang.Integer";
            return Integer.class.getName();
        case Value.LONG:
            // "java.lang.Long";
            return Long.class.getName();
        case Value.DECIMAL:
            // "java.math.BigDecimal";
            return BigDecimal.class.getName();
        case Value.TIME:
            // "java.sql.Time";
            return Time.class.getName();
        case Value.DATE:
            // "java.sql.Date";
            return Date.class.getName();
        case Value.TIMESTAMP:
            // "java.sql.Timestamp";
            return Timestamp.class.getName();
        case Value.BYTES:
        case Value.UUID:
            // "[B", not "byte[]";
            return byte[].class.getName();
        case Value.STRING:
        case Value.STRING_IGNORECASE:
        case Value.STRING_FIXED:
            // "java.lang.String";
            return String.class.getName();
        case Value.BLOB:
            // "java.sql.Blob";
            return java.sql.Blob.class.getName();
        case Value.CLOB:
            // "java.sql.Clob";
            return java.sql.Clob.class.getName();
        case Value.DOUBLE:
            // "java.lang.Double";
            return Double.class.getName();
        case Value.FLOAT:
            // "java.lang.Float";
            return Float.class.getName();
        case Value.NULL:
            return null;
        case Value.JAVA_OBJECT:
            // "java.lang.Object";
            return Object.class.getName();
        case Value.UNKNOWN:
            // anything
            return Object.class.getName();
        case Value.ARRAY:
            return Array.class.getName();
        case Value.RESULT_SET:
            return ResultSet.class.getName();
        default:
            throw new DbrouterRuntimeException("type="+type);
        }
    }

   
    /**
     * Convert a SQL type to a value type.
     *
     * @param sqlType the SQL type
     * @return the value type
     */
    public static int convertSQLTypeToValueType(int sqlType) {
        switch(sqlType) {
        case Types.CHAR:
        case Types.VARCHAR:
        case Types.LONGVARCHAR:
            return Value.STRING;
        case Types.NUMERIC:
        case Types.DECIMAL:
            return Value.DECIMAL;
        case Types.BIT:
        case Types.BOOLEAN:
            return Value.BOOLEAN;
        case Types.INTEGER:
            return Value.INT;
        case Types.SMALLINT:
            return Value.SHORT;
        case Types.TINYINT:
            return Value.BYTE;
        case Types.BIGINT:
            return Value.LONG;
        case Types.REAL:
            return Value.FLOAT;
        case Types.DOUBLE:
        case Types.FLOAT:
            return Value.DOUBLE;
        case Types.BINARY:
        case Types.VARBINARY:
        case Types.LONGVARBINARY:
            return Value.BYTES;
        case Types.OTHER:
        case Types.JAVA_OBJECT:
            return Value.JAVA_OBJECT;
        case Types.DATE:
            return Value.DATE;
        case Types.TIME:
            return Value.TIME;
        case Types.TIMESTAMP:
            return Value.TIMESTAMP;
        case Types.BLOB:
            return Value.BLOB;
        case Types.CLOB:
        case Types.NULL:
            return Value.NULL;
        case Types.ARRAY:
            return Value.ARRAY;
        case DataType.TYPE_RESULT_SET:
            return Value.RESULT_SET;
        default:
            throw new DbrouterRuntimeException(String.format("an unsupported sql type:%s", sqlType));
        }
    }

    /**
     * Get the value type for the given Java class.
     *
     * @param x the Java class
     * @return the value type
     */
    public static int getTypeFromClass(Class <?> x) {
        if (x == null || Void.TYPE == x) {
            return Value.NULL;
        }
        if (x.isPrimitive()) {
            x = getNonPrimitiveClass(x);
        }
        if (String.class == x) {
            return Value.STRING;
        } else if (Integer.class == x) {
            return Value.INT;
        } else if (Long.class == x) {
            return Value.LONG;
        } else if (Boolean.class == x) {
            return Value.BOOLEAN;
        } else if (Double.class == x) {
            return Value.DOUBLE;
        } else if (Byte.class == x) {
            return Value.BYTE;
        } else if (Short.class == x) {
            return Value.SHORT;
        } else if (Character.class == x) {
            throw new DbrouterRuntimeException("char (not supported)");
        } else if (Float.class == x) {
            return Value.FLOAT;
        } else if (byte[].class == x) {
            return Value.BYTES;
        } else if (UUID.class == x) {
            return Value.UUID;
        } else if (Void.class == x) {
            return Value.NULL;
        } else if (BigDecimal.class.isAssignableFrom(x)) {
            return Value.DECIMAL;
        } else if (ResultSet.class.isAssignableFrom(x)) {
            return Value.RESULT_SET;
        } else if (Value.ValueBlob.class.isAssignableFrom(x)) {
            return Value.BLOB;
        } else if (Value.ValueClob.class.isAssignableFrom(x)) {
            return Value.CLOB;
        } else if (Date.class.isAssignableFrom(x)) {
            return Value.DATE;
        } else if (Time.class.isAssignableFrom(x)) {
            return Value.TIME;
        } else if (Timestamp.class.isAssignableFrom(x)) {
            return Value.TIMESTAMP;
        } else if (java.util.Date.class.isAssignableFrom(x)) {
            return Value.TIMESTAMP;
        } else if (java.io.Reader.class.isAssignableFrom(x)) {
            return Value.CLOB;
        } else if (java.sql.Clob.class.isAssignableFrom(x)) {
            return Value.CLOB;
        } else if (java.io.InputStream.class.isAssignableFrom(x)) {
            return Value.BLOB;
        } else if (java.sql.Blob.class.isAssignableFrom(x)) {
            return Value.BLOB;
        } else if (Object[].class.isAssignableFrom(x)) {
            // this includes String[] and so on
            return Value.ARRAY;
        } else {
            return Value.JAVA_OBJECT;
        }
    }
    
    /**
     * Convert primitive class names to java.lang.* class names.
     *
     * @param clazz the class (for example: int)
     * @return the non-primitive class (for example: java.lang.Integer)
     */
    public static Class<?> getNonPrimitiveClass(Class<?> clazz) {
        if (!clazz.isPrimitive()) {
            return clazz;
        } else if (clazz == boolean.class) {
            return Boolean.class;
        } else if (clazz == byte.class) {
            return Byte.class;
        } else if (clazz == char.class) {
            return Character.class;
        } else if (clazz == double.class) {
            return Double.class;
        } else if (clazz == float.class) {
            return Float.class;
        } else if (clazz == int.class) {
            return Integer.class;
        } else if (clazz == long.class) {
            return Long.class;
        } else if (clazz == short.class) {
            return Short.class;
        } else if (clazz == void.class) {
            return Void.class;
        }
        return clazz;
    }




    /**
     * Check if the given value type is a large object (BLOB or CLOB).
     *
     * @param type the value type
     * @return true if the value type is a lob type
     */
    public static boolean isLargeObject(int type) {
        if (type == Value.BLOB || type == Value.CLOB) {
            return true;
        }
        return false;
    }

    /**
     * Check if the given value type is a String (VARCHAR,...).
     *
     * @param type the value type
     * @return true if the value type is a String type
     */
    public static boolean isStringType(int type) {
        if (type == Value.STRING || type == Value.STRING_FIXED || type == Value.STRING_IGNORECASE) {
            return true;
        }
        return false;
    }

    /**
     * Check if the given value type supports the add operation.
     *
     * @param type the value type
     * @return true if add is supported
     */
    public static boolean supportsAdd(int type) {
        switch (type) {
        case Value.BYTE:
        case Value.DECIMAL:
        case Value.DOUBLE:
        case Value.FLOAT:
        case Value.INT:
        case Value.LONG:
        case Value.SHORT:
            return true;
        default:
            return false;
        }
    }

    /**
     * Get the data type that will not overflow when calling 'add' 2 billion times.
     *
     * @param type the value type
     * @return the data type that supports adding
     */
    public static int getAddProofType(int type) {
        switch (type) {
        case Value.BYTE:
            return Value.LONG;
        case Value.FLOAT:
            return Value.DOUBLE;
        case Value.INT:
            return Value.LONG;
        case Value.LONG:
            return Value.DECIMAL;
        case Value.SHORT:
            return Value.LONG;
        default:
            return type;
        }
    }

    /**
     * Get the default value in the form of a Java object for the given Java class.
     *
     * @param clazz the Java class
     * @return the default object
     */
    public static Object getDefaultForPrimitiveType(Class<?> clazz) {
        if (clazz == Boolean.TYPE) {
            return Boolean.FALSE;
        } else if (clazz == Byte.TYPE) {
            return Byte.valueOf((byte) 0);
        } else if (clazz == Character.TYPE) {
            return Character.valueOf((char) 0);
        } else if (clazz == Short.TYPE) {
            return Short.valueOf((short) 0);
        } else if (clazz == Integer.TYPE) {
            return Integer.valueOf(0);
        } else if (clazz == Long.TYPE) {
            return Long.valueOf(0);
        } else if (clazz == Float.TYPE) {
            return Float.valueOf(0);
        } else if (clazz == Double.TYPE) {
            return Double.valueOf(0);
        }
        throw new DbrouterRuntimeException("primitive=" + clazz.toString());
    }
    /**
     * Get the data type object for the given value type.
     *
     * @param type the value type
     * @return the data type object
     */
    public static DataType getDataType(int type) {
        if (type == Value.UNKNOWN) {
            throw new DbrouterRuntimeException("unsupported data type");
        }
        DataType dt = TYPES_BY_VALUE_TYPE.get(type);
        if (dt == null) {
            dt = TYPES_BY_VALUE_TYPE.get(Value.NULL);
        }
        return dt;
    }
    /**
     * Get a data type object from a type name.
     *
     * @param s the type name
     * @return the data type object
     */
    public static DataType getTypeByName(String s) {
        return TYPES_BY_NAME.get(s);
    }

	public int getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public int getSqlType() {
		return sqlType;
	}

	public String getJdbc() {
		return jdbc;
	}

	public int getSqlTypePos() {
		return sqlTypePos;
	}

	public long getMaxPrecision() {
		return maxPrecision;
	}

	public int getMinScale() {
		return minScale;
	}

	public int getMaxScale() {
		return maxScale;
	}

	public boolean isDecimal() {
		return decimal;
	}

	public String getPrefix() {
		return prefix;
	}

	public String getSuffix() {
		return suffix;
	}

	public String getParams() {
		return params;
	}

	public boolean isAutoIncrement() {
		return autoIncrement;
	}

	public boolean isCaseSensitive() {
		return caseSensitive;
	}

	public boolean isSupportsPrecision() {
		return supportsPrecision;
	}

	public boolean isSupportsScale() {
		return supportsScale;
	}

	public long getDefaultPrecision() {
		return defaultPrecision;
	}

	public int getDefaultScale() {
		return defaultScale;
	}

	public int getDefaultDisplaySize() {
		return defaultDisplaySize;
	}

	public boolean isHidden() {
		return hidden;
	}
    
}
