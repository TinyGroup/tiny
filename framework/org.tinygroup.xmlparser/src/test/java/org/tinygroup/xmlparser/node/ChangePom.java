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
import org.tinygroup.xmlparser.XmlDocument;
import org.tinygroup.xmlparser.formatter.XmlFormater;
import org.tinygroup.xmlparser.parser.XmlStringParser;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by luoguo on 14-1-17.
 */
public class ChangePom {
    public static void main(String[] args) throws Throwable {
        File file1 = new File("D:\\SVN\\tinyorg-code\\trunk\\Sources\\");
        processFolder(file1);
    }

    private static void processFolder(File file1) throws Exception {
        File[] files = file1.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                processFolder(file);
            }
            if (file.getName().equals("pom.xml")) {
                processPomFile(file);
            }
        }
    }

    private static void processPomFile(File file) throws Exception {
        System.out.println("processing:" + file.getAbsolutePath());
        XmlStringParser parser = new XmlStringParser();
        XmlDocument doc = parser.parse(IOUtils.readFromInputStream(new FileInputStream(file), "utf-8"));
        XmlNode dependencies = doc.getRoot().getSubNode("dependencies");
        XmlNode projectArtifactId = doc.getRoot().getSubNode("artifactId");
        //projectArtifactId.setContent("org.tinygroup" + projectArtifactId.getContent().trim());
        if (dependencies != null) {
            List<XmlNode> dependencyList = dependencies.getSubNodes("dependency");
            if (dependencyList != null) {
                for (XmlNode node : dependencyList) {
                    XmlNode groupId = node.getSubNode("groupId");
                    if (groupId.getContent().trim().equals("org.tinygroup")) {
                        XmlNode artifactId = node.getSubNode("artifactId");
                        if (artifactId.getContent().indexOf("org.tinygroup") < 0) {
                            artifactId.setContent("org.tinygroup." + artifactId.getContent().trim());
                        }
                    }
                }
            }
        }

        XmlFormater formater = new XmlFormater();
        IOUtils.writeToOutputStream(new FileOutputStream(file), formater.format(doc), "UTF-8");
    }
}
