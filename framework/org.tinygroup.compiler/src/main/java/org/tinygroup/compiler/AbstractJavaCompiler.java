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
package org.tinygroup.compiler;

import org.eclipse.jdt.core.compiler.CompilationProgress;
import org.eclipse.jdt.internal.compiler.impl.CompilerOptions;

import java.io.PrintWriter;

public abstract class AbstractJavaCompiler<S extends Source> implements JavaCompiler<S> {

    private boolean debugEnabled;
    private String outputDirectory;
    private CompilerOptions compilerOptions;

    private String classPath;
    private PrintWriter outPrintWriter;
    private PrintWriter errPrintWriter;
    private CompilationProgress compilationProgress;
    private String encode = "UTF-8";

    public String getEncode() {
        return encode;
    }

    public void setEncode(String encode) {
        this.encode = encode;
    }

    public CompilationProgress getCompilationProgress() {
        return compilationProgress;
    }

    public void setCompilationProgress(CompilationProgress compilationProgress) {
        this.compilationProgress = compilationProgress;
    }

    public PrintWriter getOutPrintWriter() {
        return outPrintWriter;
    }

    public void setOutPrintWriter(PrintWriter outPrintWriter) {
        this.outPrintWriter = outPrintWriter;
    }

    public PrintWriter getErrPrintWriter() {
        return errPrintWriter;
    }

    public void setErrPrintWriter(PrintWriter errPrintWriter) {
        this.errPrintWriter = errPrintWriter;
    }

    public String getClassPath() {
        return classPath;
    }

    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    public CompilerOptions getCompilerOptions() {
        return compilerOptions;
    }

    public void setCompilerOptions(CompilerOptions compilerOptions) {
        this.compilerOptions = compilerOptions;
    }


    public boolean isDebugEnabled() {
        return debugEnabled;
    }

    public void setDebugEnabled(boolean debugEnabled) {
        this.debugEnabled = debugEnabled;
    }

    public String getOutputDirectory() {
        return outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }
}
