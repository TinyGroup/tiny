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
package org.tinygroup.message;

import java.util.Collection;
import java.util.List;

/**
 * Created by luoguo on 2014/4/24.
 */
public class MessageManagerDefault<Account extends MessageAccount, Sender extends MessageSender, Receiver extends MessageReceiver, Msg extends Message>
        implements MessageManager<Account, Sender, Receiver, Msg> {

    private Account messageAccount;
    private MessageReceiveService<Account, Msg> messageReceiveService;
    private MessageSendService<Account, Sender, Receiver, Msg> messageSendService;
    private List<MessageReceiveProcessor<Msg>> messageReceiveProcessors;
    private List<MessageSendProcessor<Sender, Receiver, Msg>> messageSendProcessors;

    public void setMessageAccount(Account messageAccount) {
        this.messageAccount = messageAccount;
    }

    public void setMessageReceiveService(MessageReceiveService<Account, Msg> messageReceiveService) {
        this.messageReceiveService = messageReceiveService;
    }

    public void setMessageSendService(MessageSendService<Account, Sender, Receiver, Msg> messageSendService) {
        this.messageSendService = messageSendService;
    }

    public void setMessageReceiveProcessors(List<MessageReceiveProcessor<Msg>> messageReceiveProcessors) {
        this.messageReceiveProcessors = messageReceiveProcessors;
    }

    public void addMessageReceiveProcessor(MessageReceiveProcessor<Msg> messageReceiveProcessor) {

    }

    public void setMessageSendProcessors(List<MessageSendProcessor<Sender, Receiver, Msg>> messageSendProcessors) {
        this.messageSendProcessors = messageSendProcessors;
    }

    public void addMessageSendProcessor(MessageSendProcessor<Sender, Receiver, Msg> messageSendProcessor) {

    }

    public Collection<Msg> getMessages() throws MessageException {
        Collection<Msg> messages = messageReceiveService.getMessages(messageAccount);
        if (messageReceiveProcessors != null && messageReceiveProcessors.size() > 0) {
            for (Message message : messages) {
                for (MessageReceiveProcessor processor : messageReceiveProcessors) {
                    if (processor.isMatch(message)) {
                        processor.processMessage(message);
                    }
                }
            }
        }
        return messages;
    }

    public void sendMessage(Sender messageSender, Collection<Receiver> messageReceivers, Msg message) throws MessageException {
        messageSendService.sendMessage(messageAccount, messageSender, messageReceivers, message);
        if (messageSendProcessors != null && messageSendProcessors.size() > 0) {
            for (Receiver receiver : messageReceivers) {
                for (MessageSendProcessor<Sender, Receiver, Msg> msgMessageSendProcessor : messageSendProcessors) {
                    if (msgMessageSendProcessor.isMatch(messageSender, receiver, message)) {
                        msgMessageSendProcessor.processMessage(messageSender, receiver, message);
                    }
                }
            }
        }
    }
}
