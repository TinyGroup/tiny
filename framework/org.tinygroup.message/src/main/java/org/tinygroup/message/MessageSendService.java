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

/**
 * 信息服务，用于定义信息的发送与接收接口
 * Created by luoguo on 2014/4/17.
 */
public interface MessageSendService<ACCOUNT extends MessageAccount, SENDER extends MessageSender, RECEIVER extends MessageReceiver, MSG extends Message> {


    /**
     * 发送单条消息
     *
     * @param messageSender    消息发送者
     * @param messageReceivers 消息接收者
     * @param message          消息
     */
    void sendMessage(ACCOUNT messageAccount, SENDER messageSender, Collection<RECEIVER> messageReceivers, MSG message) throws MessageException;


}
