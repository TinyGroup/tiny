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

import org.tinygroup.message.Message;

import java.util.Collection;
import java.util.Date;

/**
 * Created by luoguo on 2014/4/18.
 */
public class EmailReceiveMessage implements Message {
    private  EmailMessage message;
    private  Collection<EmailMessageSender> messageSenders;
    private Collection<EmailMessageReceiver> messageReceivers;
    private Date sendDate;
    private Date receiveDate;

    public Collection<EmailMessageSender> getMessageSenders() {
        return messageSenders;
    }

    public void setMessageSenders(Collection<EmailMessageSender> messageSenders) {
        this.messageSenders = messageSenders;
    }

    public EmailMessage getMessage() {
        return message;
    }

    public void setMessage(EmailMessage message) {
        this.message = message;
    }

    public Collection<EmailMessageReceiver> getMessageReceivers() {
        return messageReceivers;
    }

    public void setMessageReceivers(Collection<EmailMessageReceiver> messageReceivers) {
        this.messageReceivers = messageReceivers;
    }

    public Date getSendDate() {
        return sendDate;
    }

    public void setSendDate(Date sendDate) {
        this.sendDate = sendDate;
    }

    public Date getReceiveDate() {
        return receiveDate;
    }

    public void setReceiveDate(Date receiveDate) {
        this.receiveDate = receiveDate;
    }
}
