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
package org.tinygroup.net.coder.kryo;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferInputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldBasedFrameDecoder;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;

public class KryoDecoder extends LengthFieldBasedFrameDecoder {

	private static final int MB = 1048576;

	public KryoDecoder() {
		this(MB);
	}

	public KryoDecoder(int maxObjectSize) {
		super(maxObjectSize, 0, 4, 0, 4);
	}

	
	protected Object decode(ChannelHandlerContext ctx, Channel channel,
			ChannelBuffer buffer) throws Exception {
		ChannelBuffer frame = (ChannelBuffer) super
				.decode(ctx, channel, buffer);
		if (frame == null) {
			return null;
		}
		  Kryo kryo = new Kryo();  
			kryo.setReferences(false);
			kryo.setRegistrationRequired(false);
			kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
        Input input = null;  
        try {  
            input = new Input(new ChannelBufferInputStream(frame));  
            return kryo.readClassAndObject(input);  
        } finally {  
            input.close();  
        }  

	}

	protected ChannelBuffer extractFrame(ChannelBuffer buffer, int index,
			int length) {
		return buffer.slice(index, length);
	}

}

