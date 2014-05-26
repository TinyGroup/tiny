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

import static org.jboss.netty.buffer.ChannelBuffers.dynamicBuffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBufferOutputStream;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Output;

public class KryoEncoder extends OneToOneEncoder{

	   private static final byte[] LENGTH_PLACEHOLDER = new byte[4];

	    private final int estimatedLength;

	    public KryoEncoder() {
	        this(512);
	    }

	   
	    public KryoEncoder(int estimatedLength) {
	        if (estimatedLength < 0) {
	            throw new IllegalArgumentException(
	                    "estimatedLength: " + estimatedLength);
	        }
	        this.estimatedLength = estimatedLength;
	    }

	    
	    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object msg) throws Exception {
	        ChannelBufferOutputStream bout =
	            new ChannelBufferOutputStream(dynamicBuffer(
	                    estimatedLength, ctx.getChannel().getConfig().getBufferFactory()));
	        bout.write(LENGTH_PLACEHOLDER);
	        Kryo kryo = new Kryo();  
			kryo.setReferences(false);
			kryo.setRegistrationRequired(false);
			kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());
	        Output output = new Output(bout);  
	        kryo.writeClassAndObject(output, msg);  
	        output.flush();  
	        output.close();
	        
	        ChannelBuffer encoded = bout.buffer();
	        encoded.setInt(0, encoded.writerIndex() - 4);
	        return encoded;
	    }

}
