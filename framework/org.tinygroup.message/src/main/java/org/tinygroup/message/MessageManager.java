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
public interface MessageManager<ACCOUNT extends MessageAccount, SENDER extends MessageSender, RECEIVER extends MessageReceiver, MSG extends Message> {
    void setMessageAccount(ACCOUNT messageAccount);

    void setMessageReceiveService(MessageReceiveService<ACCOUNT, MSG> messageReceiveService);

    void setMessageSendService(MessageSendService<ACCOUNT, SENDER, RECEIVER, MSG> messageSendService);

    /**
     * 设置信息接收处理器列表
     *
     * @param messageReceiveProcessors
     */
    void setMessageReceiveProcessors(List<MessageReceiveProcessor<MSG>> messageReceiveProcessors);

    /**
     * 添加信息接收处理器
     *
     * @param messageReceiveProcessor
     */
    void addMessageReceiveProcessor(MessageReceiveProcessor<MSG> messageReceiveProcessor);

    /**
     * 设置信息发送处理器列表
     *
     * @param messageSendProcessors
     */
    void setMessageSendProcessors(List<MessageSendProcessor<SENDER, RECEIVER, MSG>> messageSendProcessors);

    /**
     * 添加信息发送处理器
     *
     * @param messageSendProcessor
     */
    void addMessageSendProcessor(MessageSendProcessor<SENDER, RECEIVER, MSG> messageSendProcessor);

    Collection<MSG> getMessages() throws MessageException;

    void sendMessage(SENDER messageSender, Collection<RECEIVER> messageReceivers, MSG message) throws MessageException;

}
