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
package org.tinygroup.weblayer.webcontext.session.encrypter.impl;

import static org.tinygroup.commons.tools.Assert.assertNotNull;
import static org.tinygroup.commons.tools.Assert.assertTrue;
import static org.tinygroup.commons.tools.StringUtil.trimToNull;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.tinygroup.weblayer.webcontext.session.encrypter.AbstractJceEncrypter;

public class AesEncrypter extends AbstractJceEncrypter {
    public final static String ALG_NAME         = "AES";
    public final static int    DEFAULT_KEY_SIZE = 128;
    private String        key;
    private int           keySize;
    private SecretKeySpec keySpec;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = trimToNull(key);
    }

    public int getKeySize() {
        return keySize <= 0 ? DEFAULT_KEY_SIZE : keySize;
    }

    public void setKeySize(int keySize) {
        this.keySize = keySize;
    }

    
    protected void init() throws Exception {
        assertNotNull(key, "no key");

        byte[] raw = key.getBytes("UTF-8");
        int keySize = getKeySize();
        int actualKeySize = raw.length * 8;

        assertTrue(keySize == actualKeySize, "Illegal key: expected size=%d, actual size is %d", keySize, actualKeySize);

        keySpec = new SecretKeySpec(raw, ALG_NAME);
    }

    
    protected Cipher createCipher(int mode) throws Exception {
        Cipher cipher = Cipher.getInstance(ALG_NAME);
        cipher.init(mode, keySpec);
        return cipher;
    }

    
    public String toString() {
        return "AES(keySize=" + getKeySize() + ")";
    }


}
