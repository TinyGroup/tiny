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
package org.tinygroup.tinypc;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

/**
 * 对象存储,用于进行对象与文件的互相转换
 * 这里主要用于进行工作序列化
 * Created by luoguo on 14-1-14.
 */
public interface ObjectStorage {
    /**
     * 返回文件根路径
     *
     * @return
     */
    String getRootFolder();

    /**
     * 载入所有对象
     *
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    List<Serializable> loadObjects() throws IOException, ClassNotFoundException;

    /**
     * 载入指定类型的对象
     *
     * @param typeName
     * @param <T>
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    <T> List<T> loadObjects(String typeName) throws IOException, ClassNotFoundException;

    /**
     * 保存普通对象
     *
     * @param object
     * @throws IOException
     */
    void saveObject(Serializable object) throws IOException;

    /**
     * 存储某种类型的文件
     *
     * @param object
     * @param typeName
     * @throws IOException
     */
    void saveObject(Serializable object, String typeName) throws IOException;

    /**
     * 清除某个对象
     *
     * @param object
     * @throws IOException
     */
    void clearObject(Serializable object) throws IOException;

    /**
     * 清除所有对象，会清除子目录
     *
     * @throws IOException
     */
    void clearObjects() throws IOException;

    /**
     * 清除某种类型所有对象
     *
     * @param typeName
     * @throws IOException
     */
    void clearObjects(String typeName) throws IOException;
}
