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
package org.tinygroup.vfs.impl;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;
import org.tinygroup.vfs.SchemaProvider;

public abstract class AbstractFileObject implements FileObject {

    private SchemaProvider schemaProvider;
    private FileObject parent;

    public FileObject getParent() {
        return parent;
    }

    public void setParent(FileObject parent) {
        this.parent = parent;
    }

    public AbstractFileObject(SchemaProvider schemaProvider) {
        this.schemaProvider = schemaProvider;
    }

    public SchemaProvider getSchemaProvider() {
        return schemaProvider;
    }

    public int hashCode() {
        return this.getAbsolutePath().hashCode();
    }


    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof FileObject) {
            FileObject fileObject = (FileObject) obj;
            return this.getAbsolutePath().equalsIgnoreCase(fileObject.getAbsolutePath());
        }
        return false;
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     * @param parentFirst         true:如果父亲和儿子都命中，则先处理父亲；false:如果父亲和儿子都命中，则先处理儿子
     */
    public void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor,
                        boolean parentFirst) {
        if (parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
        if (isFolder()) {
            for (FileObject subFileObject : getChildren()) {
                subFileObject.foreach(fileObjectFilter, fileObjectProcessor, parentFirst);
            }
        }
        if (!parentFirst && fileObjectFilter.accept(this)) {
            fileObjectProcessor.process(this);
        }
    }

    /**
     * 对文件对象及其所有子对象都通过文件对象过滤器进行过滤，如果匹配，则执行文件对外处理器，父文件先处理
     *
     * @param fileObjectFilter
     * @param fileObjectProcessor
     */
    public void foreach(FileObjectFilter fileObjectFilter, FileObjectProcessor fileObjectProcessor) {
        foreach(fileObjectFilter, fileObjectProcessor, true);
    }
}
