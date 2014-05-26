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
package org.tinygroup.fileresolver;

import org.tinygroup.config.Configuration;
import org.tinygroup.vfs.FileObject;

import java.util.List;

/**
 * 文件查找器
 *
 * @author luoguo
 */
public interface FileResolver extends Configuration {

    String BEAN_NAME = "fileResolver";

    FileObject getClassesPath();

    /**
     * 返回所有的文件处理器
     *
     * @return
     */
    List<FileProcessor> getFileProcessorList();

    /**
     * 手动添加路径
     *
     * @param path
     */
    void addManualClassPath(String path);


    /**
     * 手工添加扫描的匹配列表，如果有包含列表，则按包含列表
     *
     * @param pattern
     */
    void addIncludePathPattern(String pattern);

    List<String> getManualClassPaths();

    /**
     * 增加文件处理器
     *
     * @param fileProcessor
     */
    void addFileProcessor(FileProcessor fileProcessor);

    /**
     * 开始找文件
     */
    void resolve();

    /**
     *
     */
    void refresh();

    /**
     * 获取文件搜索器扫描的所有路径
     *
     * @return
     */
    List<String> getAllScanningPath();

    /**
     * 获取文件处理的线程数目
     *
     * @return
     */
    int getFileProcessorThreadNum();

    /**
     * 设置文件处理的线程数目
     *
     * @param threadNum
     */
    void setFileProcessorThreadNum(int threadNum);
}
