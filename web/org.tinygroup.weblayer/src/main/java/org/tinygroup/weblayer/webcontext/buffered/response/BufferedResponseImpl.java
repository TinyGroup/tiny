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
package org.tinygroup.weblayer.webcontext.buffered.response;

import static org.tinygroup.commons.tools.BasicConstant.EMPTY_BYTE_ARRAY;
import static org.tinygroup.commons.tools.BasicConstant.EMPTY_STRING;
import static org.tinygroup.commons.tools.CollectionUtil.createLinkedList;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.EmptyStackException;
import java.util.LinkedList;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.commons.io.ByteArray;
import org.tinygroup.commons.io.ByteArrayInputStream;
import org.tinygroup.commons.io.ByteArrayOutputStream;
import org.tinygroup.commons.io.StreamUtil;
import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.weblayer.AbstractTinyFilter;
import org.tinygroup.weblayer.WebContext;
import org.tinygroup.weblayer.webcontext.AbstractResponseWrapper;

/**
 * 包裹<code>HttpServletResponse</code>，使之输出到内存中。
 *
 * @author renhui
 */
public class BufferedResponseImpl extends AbstractResponseWrapper {
    private static final Logger  logger       =  LoggerFactory.getLogger(AbstractTinyFilter.class);
    private              boolean buffering = true;
    private Stack<ByteArrayOutputStream> bytesStack;
    private Stack<StringWriter>          charsStack;
    private ServletOutputStream          stream;
    private PrintWriter                  streamAdapter;
    private PrintWriter                  writer;
    private ServletOutputStream          writerAdapter;

    /**
     * 创建一个<code>BufferedResponseImpl</code>。
     *
     * @param requestContext response所在的request context
     * @param response       原始的response
     */
    public BufferedResponseImpl(WebContext webContext, HttpServletResponse response) {
        super(webContext, response);
    }

    /**
     * 取得输出流。
     *
     * @return response的输出流
     * @throws IOException 输入输出失败
     */
    
    public ServletOutputStream getOutputStream() throws IOException {
        if (stream != null) {
            return stream;
        }

        if (writer != null) {
            // 如果getWriter方法已经被调用，则将writer转换成OutputStream
            // 这样做会增加少量额外的内存开销，但标准的servlet engine不会遇到这种情形，
            // 只有少数servlet engine需要这种做法（resin）。
            if (writerAdapter != null) {
                return writerAdapter;
            } else {
            	logger.logMessage(LogLevel.DEBUG, "Attampt to getOutputStream after calling getWriter.  This may cause unnecessary system cost.");
                writerAdapter = new WriterOutputStream(writer, getCharacterEncoding());
                return writerAdapter;
            }
        }

        if (buffering) {
            // 注意，servletStream一旦创建，就不改变，
            // 如果需要改变，只需要改变其下面的bytes流即可。
            if (bytesStack == null) {
                bytesStack = new Stack<ByteArrayOutputStream>();
            }

            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bytesStack.push(bytes);
            stream = new BufferedServletOutputStream(bytes);

            logger.logMessage(LogLevel.DEBUG,"Created new byte buffer");
        } else {
            stream = super.getOutputStream();
        }

        return stream;
    }

    /**
     * 取得输出字符流。
     *
     * @return response的输出字符流
     * @throws IOException 输入输出失败
     */
    
    public PrintWriter getWriter() throws IOException {
        if (writer != null) {
            return writer;
        }

        if (stream != null) {
            // 如果getOutputStream方法已经被调用，则将stream转换成PrintWriter。
            // 这样做会增加少量额外的内存开销，但标准的servlet engine不会遇到这种情形，
            // 只有少数servlet engine需要这种做法（resin）。
            if (streamAdapter != null) {
                return streamAdapter;
            } else {
                logger.logMessage(LogLevel.DEBUG,"Attampt to getWriter after calling getOutputStream.  This may cause unnecessary system cost.");
                streamAdapter = new PrintWriter(new OutputStreamWriter(stream, getCharacterEncoding()), true);
                return streamAdapter;
            }
        }

        if (buffering) {
            // 注意，servletWriter一旦创建，就不改变，
            // 如果需要改变，只需要改变其下面的chars流即可。
            if (charsStack == null) {
                charsStack = new Stack<StringWriter>();
            }

            StringWriter chars = new StringWriter();

            charsStack.push(chars);
            writer = new BufferedServletWriter(chars);

            logger.logMessage(LogLevel.DEBUG,"Created new character buffer");
        } else {
            writer = super.getWriter();
        }

        return writer;
    }

    /**
     * 设置content长度。该调用只在<code>setBuffering(false)</code>时有效。
     *
     * @param length content长度
     */
    
    public void setContentLength(int length) {
        if (!buffering) {
            super.setContentLength(length);
        }
    }

    /**
     * 冲洗buffer。
     *
     * @throws IOException 如果失败
     */
    
    public void flushBuffer() throws IOException {
        if (buffering) {
            flushBufferAdapter();

            if (writer != null) {
                writer.flush();
            } else if (stream != null) {
                stream.flush();
            }
        } else {
            super.flushBuffer();
        }
    }

    /**
     * 清除所有buffers，常用于显示出错信息。
     *
     * @throws IllegalStateException 如果response已经commit
     */
    
    public void resetBuffer() {
        if (buffering) {
            flushBufferAdapter();

            if (stream != null) {
                bytesStack.clear();
                bytesStack.add(new ByteArrayOutputStream());
                ((BufferedServletOutputStream) stream).updateOutputStream(bytesStack.peek());
            }

            if (writer != null) {
                charsStack.clear();
                charsStack.add(new StringWriter());
                ((BufferedServletWriter) writer).updateWriter(charsStack.peek());
            }
        }

        super.resetBuffer();
    }

    /**
     * 设置是否将所有信息保存在内存中。
     *
     * @return 如果是，则返回<code>true</code>
     */
    public boolean isBuffering() {
        return buffering;
    }

    /**
     * 设置buffer模式，如果设置成<code>true</code>，表示将所有信息保存在内存中，否则直接输出到原始response中。
     * <p>
     * 此方法必须在<code>getOutputStream</code>和<code>getWriter</code>方法之前执行，否则将抛出
     * <code>IllegalStateException</code>。
     * </p>
     *
     * @param buffering 是否buffer内容
     * @throws IllegalStateException <code>getOutputStream</code>或
     *                               <code>getWriter</code>方法已经被执行
     */
    public void setBuffering(boolean buffering) {
        if (stream == null && writer == null) {
            if (this.buffering != buffering) {
                this.buffering = buffering;
                logger.logMessage(LogLevel.DEBUG,"Set buffering " + (buffering ? "on" : "off"));
            }
        } else {
            if (this.buffering != buffering) {
                throw new IllegalStateException(
                        "Unable to change the buffering mode since the getOutputStream() or getWriter() method has been called");
            }
        }
    }

    /**
     * 创建新的buffer，保存老的buffer。
     *
     * @throws IllegalStateException 如果不在buffer模式，或<code>getWriter</code>
     *                               方法曾被调用，或<code>getOutputStream</code>方法从未被调用
     */
    public void pushBuffer() {
        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required to pushBuffer");
        }

        if (stream == null && writer == null) {
            throw new IllegalStateException("getOutputStream() or getWriter() method has not been called yet");
        }

        flushBufferAdapter();

        // 向stream或writer stack中压入新的buffer。
        if (stream != null) {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();

            bytesStack.push(bytes);

            ((BufferedServletOutputStream) stream).updateOutputStream(bytesStack.peek());

            logger.logMessage(LogLevel.DEBUG,"Pushed new byte buffer (stack size is " + bytesStack.size() + ")");
        } else {
            StringWriter chars = new StringWriter();

            charsStack.push(chars);

            ((BufferedServletWriter) writer).updateWriter(charsStack.peek());

            logger.logMessage(LogLevel.DEBUG,"Pushed new character buffer (stack size is " + charsStack.size() + ")");
        }
    }

    /**
     * 弹出最近的buffer，如果堆栈中只有一个buffer，则弹出后再创建一个新的。
     *
     * @return 最近的buffer内容，如果<code>getOutputStream</code>方法从未被调用，则返回空的byte array
     * @throws IllegalStateException 如果不在buffer模式，或<code>getWriter</code>方法曾被调用
     */
    public ByteArray popByteBuffer() {
        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required to popByteBuffer");
        }

        if (writer != null) {
            throw new IllegalStateException("Unable to popByteBuffer() since the getWriter() method has been called");
        }

        if (stream == null) {
            return new ByteArray(EMPTY_BYTE_ARRAY, 0, 0);
        } else {
            flushBufferAdapter();

            ByteArrayOutputStream block = bytesStack.pop();

            if (bytesStack.size() == 0) {
                bytesStack.push(new ByteArrayOutputStream());
            }

            ((BufferedServletOutputStream) stream).updateOutputStream(bytesStack.peek());

            logger.logMessage(LogLevel.DEBUG,"Popped the last byte buffer (stack size is " + bytesStack.size() + ")");

            return block.toByteArray();
        }
    }
    
    /**
     * 返回最上层的ByteArray
     *
     * @return 最近的buffer内容，如果<code>getOutputStream</code>方法从未被调用，则返回空的byte array
     * @throws IllegalStateException 如果不在buffer模式，或<code>getWriter</code>方法曾被调用
     */
    public ByteArray peekByteBuffer() {
        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required to popByteBuffer");
        }

        if (writer != null) {
            throw new IllegalStateException("Unable to popByteBuffer() since the getWriter() method has been called");
        }

        if (stream == null) {
            return new ByteArray(EMPTY_BYTE_ARRAY, 0, 0);
        } else {
            flushBufferAdapter();

            ByteArrayOutputStream block = bytesStack.peek();

            return block.toByteArray();
        }
    }

    /**
     * 弹出最近的buffer，如果堆栈中只有一个buffer，则弹出后再创建一个新的。
     *
     * @return 最近的buffer内容，如果<code>getWriter</code>方法从未被调用，则返回空的字符串
     * @throws IllegalStateException 如果不在buffer模式，或<code>getOutputStream</code>
     *                               方法曾被调用
     */
    public String popCharBuffer() {
        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required to popCharBuffer");
        }

        if (stream != null) {
            throw new IllegalStateException(
                    "Unable to popCharBuffer() since the getOutputStream() method has been called");
        }

        if (writer == null) {
            return EMPTY_STRING;
        } else {
            flushBufferAdapter();

            StringWriter block = charsStack.pop();

            if (charsStack.size() == 0) {
                charsStack.push(new StringWriter());
            }

            ((BufferedServletWriter) writer).updateWriter(charsStack.peek());

            logger.logMessage(LogLevel.DEBUG,"Popped the last character buffer (stack size is " + charsStack.size() + ")");

            return block.toString();
        }
    }
    
    /**
     * 返回最上层的buffer
     *
     * @return 最近的buffer内容，如果<code>getWriter</code>方法从未被调用，则返回空的字符串
     * @throws IllegalStateException 如果不在buffer模式，或<code>getOutputStream</code>
     *                               方法曾被调用
     */
    public String peekCharBuffer() {
        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required to popCharBuffer");
        }

        if (stream != null) {
            throw new IllegalStateException(
                    "Unable to popCharBuffer() since the getOutputStream() method has been called");
        }

        if (writer == null) {
            return EMPTY_STRING;
        } else {
            flushBufferAdapter();

            StringWriter block = charsStack.peek();

            return block.toString();
        }
    }

    /**
     * 将buffer中的内容提交到真正的servlet输出流中。
     * <p>
     * 如果从来没有执行过<code>getOutputStream</code>或<code>getWriter</code>
     * 方法，则该方法不做任何事情。
     * </p>
     *
     * @throws IOException           如果输入输出失败
     * @throws IllegalStateException 如果不是在buffer模式，或buffer栈中不止一个buffer
     */
    public void commitBuffer() throws IOException {
        if (stream == null && writer == null) {
            return;
        }

        if (!buffering) {
            throw new IllegalStateException("Buffering mode is required for commitBuffer");
        }

        // 输出bytes
        if (stream != null) {
            if (bytesStack.size() > 1) {
                throw new IllegalStateException("More than 1 byte-buffers in the stack");
            }

            flushBufferAdapter();

            OutputStream ostream = super.getOutputStream();
            ByteArray bytes = popByteBuffer();

            bytes.writeTo(ostream);
            ostream.flush();
            logger.logMessage(LogLevel.DEBUG,"Committed buffered bytes to the Servlet output stream");
        }

        // 输出chars
        if (writer != null) {
            if (charsStack.size() > 1) {
                throw new IllegalStateException("More than 1 char-buffers in the stack");
            }

            flushBufferAdapter();

            PrintWriter writer = super.getWriter();
            String chars = popCharBuffer();

            writer.write(chars);
            writer.flush();
            logger.logMessage(LogLevel.DEBUG,"Committed buffered characters to the Servlet writer");
        }
    }

    /** 冲洗buffer adapter，确保adapter中的信息被写入buffer中。 */
    private void flushBufferAdapter() {
        if (streamAdapter != null) {
            streamAdapter.flush();
        }

        if (writerAdapter != null) {
            try {
                writerAdapter.flush();
            } catch (IOException e) {
            }
        }
    }

    /**
     * 从<code>LinkedList</code>继承的stack，避免<code>java.util.Stack</code>
     * 中synchronized的代价。
     */
    private static class Stack<T> {
        private final LinkedList<T> list = createLinkedList();

        public T peek() {
            if (list.isEmpty()) {
                throw new EmptyStackException();
            }

            return list.getLast();
        }

        public void push(T object) {
            list.addLast(object);
        }

        public T pop() {
            if (list.isEmpty()) {
                throw new EmptyStackException();
            }

            return list.removeLast();
        }

        public int size() {
            return list.size();
        }

        public boolean add(T o) {
            return list.add(o);
        }

        public void clear() {
            list.clear();
        }
    }

    /** 代表一个将内容保存在内存中的<code>ServletOutputStream</code>。 */
    private static class BufferedServletOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream bytes;

        public BufferedServletOutputStream(ByteArrayOutputStream bytes) {
            this.bytes = bytes;
        }

        public void updateOutputStream(ByteArrayOutputStream bytes) {
            this.bytes = bytes;
        }

        
        public void write(int b) throws IOException {
            bytes.write((byte) b);
        }

        
        public void write(byte[] b) throws IOException {
            write(b, 0, b.length);
        }

        
        public void write(byte[] b, int off, int len) throws IOException {
            bytes.write(b, off, len);
        }

        
        public void flush() throws IOException {
            bytes.flush();
        }

        
        public void close() throws IOException {
            bytes.flush();
            bytes.close();
        }
    }

    /** 代表一个将内容保存在内存中的<code>PrintWriter</code>。 */
    private static class BufferedServletWriter extends PrintWriter {
        public BufferedServletWriter(StringWriter chars) {
            super(chars);
        }

        public void updateWriter(StringWriter chars) {
            this.out = chars;
        }
    }

    /** 将<code>Writer</code>适配到<code>ServletOutputStream</code>。 */
    private static class WriterOutputStream extends ServletOutputStream {
        private ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        private Writer writer;
        private String charset;

        public WriterOutputStream(Writer writer, String charset) {
            this.writer = writer;
            this.charset = defaultIfNull(charset, "ISO-8859-1");
        }

        
        public void write(int b) throws IOException {
            buffer.write((byte) b);
        }

        
        public void write(byte[] b) throws IOException {
            buffer.write(b, 0, b.length);
        }

        
        public void write(byte[] b, int off, int len) throws IOException {
            buffer.write(b, off, len);
        }

        
        public void flush() throws IOException {
            ByteArray bytes = buffer.toByteArray();

            if (bytes.getLength() > 0) {
                ByteArrayInputStream inputBytes = new ByteArrayInputStream(bytes.getRawBytes(), bytes.getOffset(),
                                                                           bytes.getLength());
                InputStreamReader reader = new InputStreamReader(inputBytes, charset);

                StreamUtil.io(reader, writer, true, false);
                writer.flush();

                buffer.reset();
            }
        }

        
        public void close() throws IOException {
            this.flush();
        }
    }
}
