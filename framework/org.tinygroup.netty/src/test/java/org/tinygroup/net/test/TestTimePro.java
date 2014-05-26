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
package org.tinygroup.net.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import org.jboss.netty.handler.codec.serialization.ClassResolvers;
import org.objenesis.strategy.StdInstantiatorStrategy;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;

public class TestTimePro {
	public static void main(String[] args) throws Exception {
		int time = 10000;
		
		long time32 = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			ObjectOutputStream oout = new CompactObjectOutputStream(
					new FileOutputStream("object.bin"));
			oout.writeObject(Simple.getSimple());
			oout.flush();
		}
		long time4 = System.currentTimeMillis();
		System.out.println(time4 - time32 + ":Object write");
		//===================================================
		long time42 = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			CompactObjectInputStream in = new CompactObjectInputStream(
					new FileInputStream("object.bin"),
					ClassResolvers.cacheDisabled(null));
			in.readObject();
			in.close();
		}
		long time5 = System.currentTimeMillis();
		System.out.println(time5 - time42 + ":Object read");
		//===================================================
		Kryo kryo = new Kryo();
		kryo.setReferences(false);
		kryo.setRegistrationRequired(false);
		kryo.setInstantiatorStrategy(new StdInstantiatorStrategy());

		
		//===================================================
		long time6 = System.currentTimeMillis();
		for (int i = 0; i < time; i++) {
			Output output = new Output(1024000);
			FileOutputStream fileOutputStream = new FileOutputStream("kryo.bin");
			output.setOutputStream(fileOutputStream);
			kryo.writeClassAndObject(output, Simple.getSimple());
			output.flush();
		}
		long time7 = System.currentTimeMillis();
		System.out.println(time7 - time6 + ":kryo write");
		//===================================================
		long time72 = System.currentTimeMillis();
		Input in = new Input(1024000);
		for (int i = 0; i < time; i++) {
			FileInputStream fileInputStream = new FileInputStream("kryo.bin");
			in.setInputStream(fileInputStream);
			kryo.readClassAndObject(in);
			in.close();
		}
		long time8 = System.currentTimeMillis();
		System.out.println(time8 - time72 + ":kryo read");
	}
}
