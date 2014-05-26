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
package org.tinygroup.xmlparser.node;

import org.tinygroup.commons.file.IOUtils;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.FileInputStream;

/**
 * Created by luoguo on 14-3-5.
 */
public class Test {
    public static void main(String[] args) throws Exception {
        XmlNode root = new XmlStringParser().parse(IOUtils.readFromInputStream(new FileInputStream("E:\\test\\MyJavaBean.xml"), "UTF-8")).getRoot();
        StringBuffer stringBuffer=new StringBuffer("class ");
        stringBuffer.append(root.getNodeName()).append("{\n");
        for(XmlNode subNode:root.getSubNodes()){
            stringBuffer.append("\tString ").append(subNode.getNodeName()).append(";\n");
            //如果要生成get set，下面继续append
        }
        stringBuffer.append("}\n");
        System.out.println(stringBuffer);
    }
}
