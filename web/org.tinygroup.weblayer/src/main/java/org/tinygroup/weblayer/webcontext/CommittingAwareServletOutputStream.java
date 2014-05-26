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
package org.tinygroup.weblayer.webcontext;

import java.io.IOException;
import javax.servlet.ServletOutputStream;

class CommittingAwareServletOutputStream extends ServletOutputStream {
    private final HeaderCommitter     committer;
    private final ServletOutputStream originalStream;

    public CommittingAwareServletOutputStream(HeaderCommitter committer, ServletOutputStream originalStream) {
        this.committer = committer;
        this.originalStream = originalStream;
    }

    
    public void print(String s) throws IOException {
        committer.commitHeaders();
        originalStream.print(s);
    }

    
    public void print(boolean b) throws IOException {
        committer.commitHeaders();
        originalStream.print(b);
    }

    
    public void print(char c) throws IOException {
        committer.commitHeaders();
        originalStream.print(c);
    }

    
    public void print(int i) throws IOException {
        committer.commitHeaders();
        originalStream.print(i);
    }

    
    public void print(long l) throws IOException {
        committer.commitHeaders();
        originalStream.print(l);
    }

    
    public void print(float f) throws IOException {
        committer.commitHeaders();
        originalStream.print(f);
    }

    
    public void print(double d) throws IOException {
        committer.commitHeaders();
        originalStream.print(d);
    }

    
    public void println() throws IOException {
        committer.commitHeaders();
        originalStream.println();
    }

    
    public void println(String s) throws IOException {
        committer.commitHeaders();
        originalStream.println(s);
    }

    
    public void println(boolean b) throws IOException {
        committer.commitHeaders();
        originalStream.println(b);
    }

    
    public void println(char c) throws IOException {
        committer.commitHeaders();
        originalStream.println(c);
    }

    
    public void println(int i) throws IOException {
        committer.commitHeaders();
        originalStream.println(i);
    }

    
    public void println(long l) throws IOException {
        committer.commitHeaders();
        originalStream.println(l);
    }

    
    public void println(float f) throws IOException {
        committer.commitHeaders();
        originalStream.println(f);
    }

    
    public void println(double d) throws IOException {
        committer.commitHeaders();
        originalStream.println(d);
    }

    
    public void write(int b) throws IOException {
        committer.commitHeaders();
        originalStream.write(b);
    }

    
    public void write(byte[] b) throws IOException {
        committer.commitHeaders();
        originalStream.write(b);
    }

    
    public void write(byte[] b, int off, int len) throws IOException {
        committer.commitHeaders();
        originalStream.write(b, off, len);
    }

    
    public void flush() throws IOException {
        committer.commitHeaders();
        originalStream.flush();
    }

    
    public void close() throws IOException {
        originalStream.close();
    }

    
    public String toString() {
        return originalStream.toString();
    }
}
