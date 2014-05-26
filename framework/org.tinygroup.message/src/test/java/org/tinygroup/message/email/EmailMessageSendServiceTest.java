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

import org.tinygroup.message.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by luoguo on 2014/4/21.
 */
public class EmailMessageSendServiceTest {
    public static void main(String[] args) throws IOException, MessageException {
        MessageManager<EmailMessageAccount, EmailMessageSender, EmailMessageReceiver, EmailMessage> messageManager = new MessageManagerDefault();
        EmailMessageAccount account = new EmailMessageAccount();
        account.setHost("127.0.0.1");
        account.setUsername("luoguo@tinygroup.org");
        account.setPassword("123456");
        EmailMessageSendService sendService = new EmailMessageSendService();
        EmailMessageSender messageSender = new EmailMessageSender();
        messageSender.setDisplayName("罗果");
        messageSender.setEmail("luoguo@tinygroup.org");
        EmailMessageReceiver messageReceiver = new EmailMessageReceiver();
        messageReceiver.setDisplayName("罗果");
        messageReceiver.setEmail("luog@tinygroup.org");
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setSubject("test1111111");
        emailMessage.setContent("中华人民共和国");
        EmailAccessory accessory = new EmailAccessory(new File("D:/RUNNING.txt"));
        emailMessage.getAccessories().add(accessory);
        messageManager.setMessageAccount(account);
        MessageReceiveService messageReceiveService = new EmailMessageReceiveService();
        MessageSendService messageSendService = new EmailMessageSendService();
        messageManager.setMessageReceiveService(messageReceiveService);
        messageManager.setMessageSendService(messageSendService);
        Collection<EmailMessageReceiver> receivers = new ArrayList<EmailMessageReceiver>();
        receivers.add(messageReceiver);
        messageManager.sendMessage(messageSender, receivers, emailMessage);
    }


}
