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
package org.tinygroup.message.email;

import org.tinygroup.message.MessageException;

import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeUtility;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by luoguo on 2014/4/18.
 */
public final class EmailMessageUtil {
    private EmailMessageUtil() {

    }

    public static Session getSession(final EmailMessageAccount messageAccount) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", messageAccount.isAuth());
        props.put("mail.smtp.starttls.enable", messageAccount.isStartTlsEnable());
        props.put("mail.smtp.host", messageAccount.getHost());
        if (messageAccount.getPort() != null) {
            props.put("mail.smtp.port", messageAccount.getPort());
        }
        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(messageAccount.getUsername(), messageAccount.getPassword());
                    }
                }
        );
        return session;
    }

    public static byte[] decode(byte[] b) throws MessageException {
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(b);
            InputStream b64is = null;
            b64is = MimeUtility.decode(bais, "base64");
            byte[] tmp = new byte[b.length];
            int n = b64is.read(tmp);
            byte[] res = new byte[n];
            System.arraycopy(tmp, 0, res, 0, n);
            return res;
        } catch (Exception e) {
            throw new MessageException(e);
        }
    }
}
