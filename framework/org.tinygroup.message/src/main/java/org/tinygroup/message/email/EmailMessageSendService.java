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
import org.tinygroup.message.MessageSendService;

import javax.activation.DataHandler;
import javax.activation.MimetypesFileTypeMap;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.*;
import javax.mail.util.ByteArrayDataSource;
import java.io.UnsupportedEncodingException;
import java.util.Collection;

/**
 * 邮件发送服务
 * Created by luoguo on 2014/4/17.
 */
public class EmailMessageSendService implements MessageSendService<EmailMessageAccount, EmailMessageSender, EmailMessageReceiver, EmailMessage> {
    public EmailMessageSendService() {

    }


    public void sendMessage(EmailMessageAccount messageAccount, EmailMessageSender messageSender, Collection<EmailMessageReceiver> messageReceivers, EmailMessage emailMessage) throws MessageException {
        Session session = EmailMessageUtil.getSession(messageAccount);
        javax.mail.Message message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(encode(messageSender.getAddress())));
            for (EmailMessageReceiver receiver : messageReceivers) {
                message.setRecipients(receiver.getType(),
                        InternetAddress.parse(encode(receiver.getAddress())));
            }
            message.setSubject(emailMessage.getSubject());
            Multipart multipart = new MimeMultipart();
            MimeBodyPart part = new MimeBodyPart();
            multipart.addBodyPart(part);
            part.setContent(emailMessage.getContent(), "text/html;\n\tcharset=\"UTF-8\"");
            processAccessory(emailMessage, multipart);
            message.setContent(multipart);
            Transport.send(message);
        } catch (javax.mail.MessagingException e) {
            throw new MessageException(e);
        } catch (UnsupportedEncodingException e) {
            throw new MessageException(e);
        }
    }

    private void processAccessory(EmailMessage emailMessage, Multipart multipart) throws MessagingException {
        if (emailMessage.getAccessories() != null) {
            for (EmailAccessory accessory : emailMessage.getAccessories()) {
                MimeBodyPart mimeBodyPart = new MimeBodyPart();
                ByteArrayDataSource dataSource = new ByteArrayDataSource(accessory.getContent(), MimetypesFileTypeMap.getDefaultFileTypeMap().getContentType(accessory.getFileName()));
                mimeBodyPart.setFileName(accessory.getFileName());
                mimeBodyPart.setDataHandler(new DataHandler(dataSource));
                multipart.addBodyPart(mimeBodyPart);
            }
        }
    }

    private String encode(String address) throws UnsupportedEncodingException {
        return MimeUtility.encodeText(address);
    }
}


