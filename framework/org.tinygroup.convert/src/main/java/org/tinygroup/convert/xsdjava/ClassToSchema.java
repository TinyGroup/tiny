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
package org.tinygroup.convert.xsdjava;

import org.tinygroup.convert.ConvertException;
import org.tinygroup.convert.Converter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.SchemaOutputResolver;
import javax.xml.transform.Result;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClassToSchema implements Converter<List<Class<?>>, List<File>> {
    private File baseFolder;

    public ClassToSchema(File baseFolder) throws ConvertException {
        this.baseFolder = baseFolder;
        if (!baseFolder.exists() && !baseFolder.mkdirs()) {
            throw new ConvertException("创建目录" + baseFolder + "失败.");
        }
    }

    public List<File> convert(List<Class<?>> inputData) throws ConvertException {
        try {
            List<File> fileList = new ArrayList<File>();
            JAXBContext context = JAXBContext.newInstance(inputData
                    .toArray(new Class[0]));
            context.generateSchema(new FileSchemaOutputResolver(fileList));
            return fileList;
        } catch (Exception e) {
            throw new ConvertException(e);
        }
    }

    class FileSchemaOutputResolver extends SchemaOutputResolver {
        private List<File> fileList;

        public FileSchemaOutputResolver(List<File> fileList) {
            this.fileList = fileList;
        }

        public Result createOutput(String namespaceUri, String suggestedFileName)
                throws IOException {
            File file = new File(baseFolder, suggestedFileName);
            fileList.add(file);
            return new StreamResult(file);
        }
    }
}
