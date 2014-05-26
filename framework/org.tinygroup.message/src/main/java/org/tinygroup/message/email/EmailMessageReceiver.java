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

import org.tinygroup.message.MessageReceiver;

import javax.mail.Message;

/**
 * 邮件接收者
 * Created by luoguo on 2014/4/17.
 */
public class EmailMessageReceiver implements MessageReceiver {
    private Message.RecipientType type = Message.RecipientType.TO;
    //邮箱地址
    private String email;
    //显示名
    private  String displayName;

    public String getDisplayName() {
        return displayName;
    }

    public String getAddress() {
        if (displayName == null) {
            return email;
        }
        return displayName + "<" + email + ">";
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Message.RecipientType getType() {
        return type;
    }

    public void setType(Message.RecipientType type) {
        this.type = type;
    }
}
