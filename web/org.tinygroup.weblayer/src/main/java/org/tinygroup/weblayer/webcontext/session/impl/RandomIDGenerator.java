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
package org.tinygroup.weblayer.webcontext.session.impl;

import org.apache.commons.codec.binary.Base64;
import org.tinygroup.commons.tools.ToStringBuilder;
import org.tinygroup.support.BeanSupport;
import org.tinygroup.weblayer.webcontext.session.SessionIDGenerator;

import java.security.SecureRandom;
import java.util.Random;

import static org.tinygroup.commons.tools.Assert.unreachableCode;
import static org.tinygroup.commons.tools.ObjectUtil.defaultIfNull;

/**
 * 用随机数生成session ID的机制。
 *
 * @author renhui
 */
public class RandomIDGenerator extends BeanSupport implements SessionIDGenerator {
    private Integer length;
    private Random  rnd;

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    
    protected void init() {
        length = defaultIfNull(length, SESSION_ID_LENGTH_DEFAULT);

        try {
            rnd = new SecureRandom();
        } catch (Exception e) {
            rnd = new Random();
        }
    }

    public String generateSessionID() {
        assertInitialized();

        byte[] bytes = new byte[(length + 3) / 4 * 3];

        rnd.nextBytes(bytes);

        byte[] b64Encoded = Base64.encodeBase64(bytes);
        StringBuilder buf = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            char ch = (char) b64Encoded[i];

            // 替换掉/和+，因为这两个字符在url中有特殊用处。
            switch (ch) {
                case '/':
                    ch = '$';
                    break;

                case '+':
                    ch = '-';
                    break;

                case '=':
                    unreachableCode();
            }

            buf.append(ch);
        }

        return buf.toString();
    }

    
    public String toString() {
        return new ToStringBuilder().append("RandomSessionIDGenerator[length=").append(length).append("]").toString();
    }
}
