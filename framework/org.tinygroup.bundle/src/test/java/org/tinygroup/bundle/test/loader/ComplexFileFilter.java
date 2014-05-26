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
package org.tinygroup.bundle.test.loader;

import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.FileObjectFilter;
import org.tinygroup.vfs.FileObjectProcessor;

import java.util.Map;

/**
 * Created by luoguo on 14-2-26.
 */
public class ComplexFileFilter implements FileObjectFilter {
    Map<FileObjectFilter,FileObjectProcessor> filterProcessorMap;
    public boolean accept(FileObject fileObject) {
        for(FileObjectFilter filter:filterProcessorMap.keySet()){
            if(filter.accept(fileObject)){
                filterProcessorMap.get(filter).process(fileObject);
            }
        }
        return false;
    }
}
