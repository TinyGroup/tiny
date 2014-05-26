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
package org.tinygroup.dbf;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;

/**
 * Created by luoguo on 2014/4/25.
 */
public class FoxproDBaseReader extends DbfReader {

    public static final int HEADER_LENGTH = 32;
    public static final int FIELD_LENGTH = 32;
    public static final int NAME_LENGTH = 11;
    public static final int RECORD_COUNT_POSITION = 3;
    public static final int RECORD_COUNT_LENGTH = 4;
    public static final int HEADER_LENGTH_POSITION = 7;
    public static final int HEADER_LENGTH_LENGTH = 2;
    public static final int RECORD_LENGTH_POSITION = 9;
    public static final int RECORD_LENGTH_LENGTH = 2;
    public static final int FIELD_LENGTH_POSITON = 16;
    public static final int FIELD_DECIMAL_POSITON = 17;
    public static final int DISPLACEMENT_POSITION = 12;
    public static final int DISPLACEMENT_LENGTH = 4;
    public static final int FIELD_FLAG_POSITION = 18;
    public static final int START_YEAR = 1900;
    public static final int YEAR_POS = 10000;
    public static final int MONTO_POS = 100;

    protected void readFields() throws IOException {
        ArrayList<Field> fields = new ArrayList<Field>();
        setFields(fields);
        for (int i = 0; i < (getHeader().getHeaderLength() - HEADER_LENGTH - 1) / FIELD_LENGTH; i++) {
            fields.add(readField());
        }
    }

    protected Field readField() throws IOException {
        Field field = new Field();
        ByteBuffer byteBuffer = ByteBuffer.allocate(FIELD_LENGTH);
        readByteBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        field.setName(new String(bytes, 0, NAME_LENGTH, getEncode()).trim().split("\0")[0]);
        field.setType((char) bytes[NAME_LENGTH]);
        field.setDisplacement(Util.getUnsignedInt(bytes, DISPLACEMENT_POSITION, DISPLACEMENT_LENGTH));
        field.setLength(Util.getUnsignedInt(bytes, FIELD_LENGTH_POSITON, 1));
        field.setDecimal(Util.getUnsignedInt(bytes, FIELD_DECIMAL_POSITON, 1));
        field.setFlag(bytes[FIELD_FLAG_POSITION]);
        return field;
    }

    protected void readHeader() throws IOException {
        Header header = new Header();
        setHeader(header);
        ByteBuffer byteBuffer = ByteBuffer.allocate(HEADER_LENGTH - 1);
        readByteBuffer(byteBuffer);
        byte[] bytes = byteBuffer.array();
        header.setLastUpdate((Util.getUnsignedInt(bytes, 0, 1) + START_YEAR) * YEAR_POS + Util.getUnsignedInt(bytes, 1, 1) * MONTO_POS + Util.getUnsignedInt(bytes, 2, 1));
        header.setRecordCount(Util.getUnsignedInt(bytes, RECORD_COUNT_POSITION, RECORD_COUNT_LENGTH));
        header.setHeaderLength(Util.getUnsignedInt(bytes, HEADER_LENGTH_POSITION, HEADER_LENGTH_LENGTH));
        header.setRecordLength(Util.getUnsignedInt(bytes, RECORD_LENGTH_POSITION, RECORD_LENGTH_LENGTH));
    }
}
