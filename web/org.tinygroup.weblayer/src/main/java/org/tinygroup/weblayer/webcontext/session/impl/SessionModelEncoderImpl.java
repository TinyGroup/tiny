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

import static org.tinygroup.commons.tools.BasicConstant.*;
import static org.tinygroup.commons.tools.ObjectUtil.*;
import static org.tinygroup.commons.tools.StringUtil.*;

import org.tinygroup.weblayer.webcontext.session.SessionModel.Factory;
import java.text.MessageFormat;


import org.tinygroup.weblayer.webcontext.session.SessionModel;
import org.tinygroup.weblayer.webcontext.session.SessionModelEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <code>SessionModelEncoder</code>的默认实现：将model内容保存成字符串。
 *
 * @author renhui
 */
public class SessionModelEncoderImpl implements SessionModelEncoder {
    private static final Logger log     = LoggerFactory.getLogger(SessionModelEncoderImpl.class);
    private static final String pattern = "'{'id:\"{0}\",ct:{1,number,#},ac:{2,number,#},mx:{3,number,#}'}'";

    public Object encode(SessionModel model) {
        Object[] args = { defaultIfNull(model.getSessionID(), EMPTY_STRING), //
                          model.getCreationTime(), //
                          model.getLastAccessedTime(), //
                          model.getMaxInactiveInterval() //
        };

        String data = new MessageFormat(pattern).format(args);

        if (log.isDebugEnabled()) {
            log.debug("Stored session model data: {}", data);
        }

        return data;
    }

    public SessionModel decode(Object data, Factory factory) {
        SessionModel model = null;

        if (data instanceof String) {
            log.trace("Trying to parse session model data: {}", data);

            try {
                Object[] values = new MessageFormat(pattern).parse((String) data);

                String sessionID = trimToNull((String) values[0]);
                long creationTime = (Long) values[1];
                long lastAccessedTime = (Long) values[2];
                int maxInactiveInterval = ((Long) values[3]).intValue();

                model = factory.newInstance(sessionID, creationTime, lastAccessedTime, maxInactiveInterval);
            } catch (Exception e) {
                if (log.isDebugEnabled()) {
                    log.debug("Could not parse session model data: " + data, e);
                }
            }
        }

        return model;
    }

}
