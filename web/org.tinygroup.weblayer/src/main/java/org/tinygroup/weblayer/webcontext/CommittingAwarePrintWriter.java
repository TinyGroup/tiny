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

import java.io.PrintWriter;
import java.util.Locale;

class CommittingAwarePrintWriter extends PrintWriter {
    private final HeaderCommitter committer;
    private final PrintWriter     originalWriter;

    CommittingAwarePrintWriter(HeaderCommitter committer, PrintWriter originalWriter) {
        super(originalWriter);
        this.committer = committer;
        this.originalWriter = originalWriter;
    }

    
    public void flush() {
        committer.commitHeaders();
        originalWriter.flush();
    }

    
    public void write(int c) {
        committer.commitHeaders();
        originalWriter.write(c);
    }

    
    public void write(char[] buf, int off, int len) {
        committer.commitHeaders();
        originalWriter.write(buf, off, len);
    }

    
    public void write(char[] buf) {
        committer.commitHeaders();
        originalWriter.write(buf);
    }

    
    public void write(String s, int off, int len) {
        committer.commitHeaders();
        originalWriter.write(s, off, len);
    }

    
    public void write(String s) {
        committer.commitHeaders();
        originalWriter.write(s);
    }

    
    public void print(boolean b) {
        committer.commitHeaders();
        originalWriter.print(b);
    }

    
    public void print(char c) {
        committer.commitHeaders();
        originalWriter.print(c);
    }

    
    public void print(int i) {
        committer.commitHeaders();
        originalWriter.print(i);
    }

    
    public void print(long l) {
        committer.commitHeaders();
        originalWriter.print(l);
    }

    
    public void print(float f) {
        committer.commitHeaders();
        originalWriter.print(f);
    }

    
    public void print(double d) {
        committer.commitHeaders();
        originalWriter.print(d);
    }

    
    public void print(char[] s) {
        committer.commitHeaders();
        originalWriter.print(s);
    }

    
    public void print(String s) {
        committer.commitHeaders();
        originalWriter.print(s);
    }

    
    public void print(Object obj) {
        committer.commitHeaders();
        originalWriter.print(obj);
    }

    
    public void println() {
        committer.commitHeaders();
        originalWriter.println();
    }

    
    public void println(boolean x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(char x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(int x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(long x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(float x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(double x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(char[] x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(String x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public void println(Object x) {
        committer.commitHeaders();
        originalWriter.println(x);
    }

    
    public PrintWriter printf(String format, Object... args) {
        committer.commitHeaders();
        return originalWriter.printf(format, args);
    }

    
    public PrintWriter printf(Locale l, String format, Object... args) {
        committer.commitHeaders();
        return originalWriter.printf(l, format, args);
    }

    
    public PrintWriter format(String format, Object... args) {
        committer.commitHeaders();
        return originalWriter.format(format, args);
    }

    
    public PrintWriter format(Locale l, String format, Object... args) {
        committer.commitHeaders();
        return originalWriter.format(l, format, args);
    }

    
    public PrintWriter append(CharSequence csq) {
        committer.commitHeaders();
        return originalWriter.append(csq);
    }

    
    public PrintWriter append(CharSequence csq, int start, int end) {
        committer.commitHeaders();
        return originalWriter.append(csq, start, end);
    }

    
    public PrintWriter append(char c) {
        committer.commitHeaders();
        return originalWriter.append(c);
    }

    
    public boolean checkError() {
        committer.commitHeaders();
        return originalWriter.checkError();
    }

    
    public void close() {
        originalWriter.close();
    }

    
    public String toString() {
        return originalWriter.toString();
    }
}
