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
package org.tinygroup.velocity;

import java.io.Writer;

import org.apache.velocity.context.InternalContextAdapter;
import org.tinygroup.context.Context;
import org.tinygroup.velocity.config.VelocityContextConfig;
import org.tinygroup.vfs.FileObject;

/**
 * Velocity接口类
 *
 * @author luoguo
 */
public interface VelocityHelper {
    String XSTEAM_PACKAGE_NAME = "velocity";

    /**
     * 设置velocity中的初始化Bean
     *
     * @param velocityContextConfig
     */
    void setVelocityContextConfig(VelocityContextConfig velocityContextConfig);

    /**
     * 添加组件
     *
     * @param macroFile
     */
    void addMacroFile(FileObject macroFile);

    /**
     * 删除组件
     *
     * @param macroFile
     */
    void removeMacroFile(FileObject macroFile);

    /**
     * @param context
     * @param writer
     * @param path
     * @throws Exception
     */
    void processTempleate(Context context, Writer writer, String path) throws Exception;

    void processTempleate(InternalContextAdapter context, Writer writer, String path)
            throws Exception;

    void processBigpipeTempleate(InternalContextAdapter context, Writer writer, String path)
            throws Exception;

    void processTempleateWithLayout(Context context, Writer writer, String path) throws Exception;

    void evaluteString(Context context, Writer writer, String string);
}