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
package org.tinygroup.dbf;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

/**
 * Created by luoguo on 2014/4/26.
 */
public interface Reader {

    void setFileChannel(FileChannel fileChannel);

    String getEncode();

    Header getHeader();

    List<Field> getFields();

    boolean isRecordRemoved();

    void moveBeforeFirst() throws IOException;

    void absolute(int position) throws IOException;

    void close() throws IOException;

    boolean hasNext();

    void next() throws IOException;
}
