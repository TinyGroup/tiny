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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by luoguo on 2014/4/25.
 */
public abstract class DbfReader implements Reader {
    public static final int HEADER_END_CHAR = 13;
    private byte type;
    private String encode = "GBK";
    private FileChannel fileChannel;
    private Header header;
    private List<Field> fields;
    private boolean recordRemoved;
    private int position = 0;

    public byte getType() {
        return type;
    }

    public void setFields(List<Field> fields) {
        this.fields = fields;
    }

    public void setHeader(Header header) {
        this.header = header;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public String getEncode() {
        return encode;
    }

    public Header getHeader() {
        return header;
    }

    public List<Field> getFields() {
        return fields;
    }


    public boolean isRecordRemoved() {
        return recordRemoved;
    }

    public static Reader parse(String dbfFile, String encode) throws IOException, IllegalAccessException, InstantiationException {
        return parse(new File(dbfFile), encode);
    }

    public static Reader parse(String dbfFile) throws IOException, IllegalAccessException, InstantiationException {
        return parse(new File(dbfFile), "GBK");
    }

    public static Reader parse(File dbfFile) throws IOException, IllegalAccessException, InstantiationException {
        return parse(dbfFile, "GBK");
    }

    public static Reader parse(File dbfFile, String encode) throws IOException, IllegalAccessException, InstantiationException {
        RandomAccessFile aFile = new RandomAccessFile(dbfFile, "r");
        FileChannel fileChannel = aFile.getChannel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        fileChannel.read(byteBuffer);
        DbfReader reader = new FoxproDBaseReader();
        reader.setType(byteBuffer.array()[0]);
        reader.setFileChannel(fileChannel);
        reader.readHeader();
        reader.readFields();
        reader.skipHeaderTerminator();
        return reader;
    }


    public void setFileChannel(FileChannel fileChannel) {
        this.fileChannel = fileChannel;
    }


    protected abstract void readFields() throws IOException;

    public void moveBeforeFirst() throws IOException {
        position = 0;
        fileChannel.position(header.getHeaderLength());
    }

    /**
     * @param position 从1开始
     * @throws java.io.IOException
     */
    public void absolute(int position) throws IOException {
        checkPosition(position);
        this.position = position;
        fileChannel.position(header.getHeaderLength() + (position - 1) * header.getRecordLength());
    }

    private void checkPosition(int position) throws IOException {
        if (position >= header.getRecordCount()) {
            throw new IOException("期望记录行数为" + (this.position + 1) + "，超过实际记录行数：" + header.getRecordCount() + "。");
        }
    }

    protected abstract Field readField() throws IOException;

    protected abstract void readHeader() throws IOException;


    private void skipHeaderTerminator() throws IOException {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        readByteBuffer(byteBuffer);
        if (byteBuffer.array()[0] != HEADER_END_CHAR) {
            throw new IOException("头结束符期望是13，但实际是：" + byteBuffer.array()[0]);
        }
    }

    public void close() throws IOException {
        fileChannel.close();
    }

    public void next() throws IOException {
        checkPosition(position);
        ByteBuffer byteBuffer = ByteBuffer.allocate(1);
        readByteBuffer(byteBuffer);
        this.recordRemoved = (byteBuffer.array()[0] == '*');
        for (Field field : fields) {
            if (field.getType() == 'M' || field.getType() == 'B' || field.getType() == 'G') {
                throw new IOException("Not Support type Memo");
            }
            read(field);
        }
        position++;
    }

    public boolean hasNext() {
        return position < header.getRecordCount();
    }

    private void read(Field field) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(field.getLength());
        readByteBuffer(buffer);
        field.setStringValue(new String(buffer.array(), encode).trim());
        field.setBuffer(buffer);
    }

    protected void readByteBuffer(ByteBuffer byteBuffer) throws IOException {
        fileChannel.read(byteBuffer);
    }
}
