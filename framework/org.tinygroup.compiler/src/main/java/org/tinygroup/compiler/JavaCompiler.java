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
import java.util.Collection;

/**
 * 定义Java编译器的接口
 * Created by luoguo on 2014/5/21.
 */
public interface JavaCompiler<S extends Source> {
    void setClassPath(String classPath);


    String getClassPath();

    /**
     * 编译一个Java源文件
     *
     * @param source
     */
    boolean compile(S source) throws CompileException;

    void setOutPrintWriter(PrintWriter outPrintWriter);

    PrintWriter getOutPrintWriter();

    void setErrPrintWriter(PrintWriter errPrintWriter);

    PrintWriter getErrPrintWriter();

    CompilationProgress getCompilationProgress();

    void setCompilationProgress(CompilationProgress compilationProgress);

    /**
     * 编译一组Java源文件
     *
     * @param sources
     * @return
     */
    boolean compile(S[] sources) throws CompileException;

    /**
     * 编译一组Java源文件
     *
     * @param sources
     * @return
     */
    boolean compile(Collection<S> sources) throws CompileException;


    /**
     * 返回是否允许调试
     *
     * @return
     */
    boolean isDebugEnabled();

    /**
     * 设置是否允许调试
     *
     * @param debugEnabled
     */
    void setDebugEnabled(boolean debugEnabled);

    String getOutputDirectory();

    void setOutputDirectory(String outputDirector);

    void setCompilerOptions(CompilerOptions compilerOptions);

    CompilerOptions getCompilerOptions();

    void setEncode(String encode);

    String getEncode();

}
