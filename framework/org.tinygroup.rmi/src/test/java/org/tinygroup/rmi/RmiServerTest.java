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
package org.tinygroup.rmi;

import junit.framework.TestCase;
import org.tinygroup.rmi.impl.RmiServerLocal;
import org.tinygroup.rmi.impl.RmiServerRemote;

/**
 * Created by luoguo on 14-1-24.
 */
public class RmiServerTest extends TestCase {
    RmiServer localServer;
    RmiServer remoteServer;

    public void setUp() throws Exception {
        super.setUp();
        localServer = new RmiServerLocal();
        remoteServer = new RmiServerRemote();
    }

    public void tearDown() throws Exception {
        super.tearDown();
        remoteServer.stop();
        localServer.stop();
    }

    public void testGetRegistry() throws Exception {
        localServer.registerRemoteObject(new HelloImpl(), "hello");
        Thread.sleep(100);
        assertEquals(2, localServer.getRegistry().list().length);
        assertEquals(2, remoteServer.getRegistry().list().length);
        Hello hello = remoteServer.getRemoteObject("hello");
        String info = hello.sayHello("abc");
        assertEquals(info, "Hello,abc");

        remoteServer.registerRemoteObject(new HelloImpl(), "hello1");

        Thread.sleep(100);

        assertEquals(localServer.getRegistry().list().length, 3);
        assertEquals(remoteServer.getRegistry().list().length, 3);
        hello = localServer.getRemoteObject("hello1");
        info = hello.sayHello("def");
        assertEquals(info, "Hello,def");
    }


    public void testGetRegistry1() throws Exception {
        localServer.registerRemoteObject(new HelloImpl(), "hello");
        Thread.sleep(100);
        assertEquals(2, localServer.getRegistry().list().length);
        assertEquals(2, remoteServer.getRegistry().list().length);
        Hello hello = remoteServer.getRemoteObject("hello");
        String info = hello.sayHello("abc");
        assertEquals(info, "Hello,abc");

        remoteServer.registerRemoteObject(new HelloImpl(), "hello1");

        Thread.sleep(100);

        assertEquals(localServer.getRegistry().list().length, 3);
        assertEquals(remoteServer.getRegistry().list().length, 3);
        hello = localServer.getRemoteObject("hello1");
        info = hello.sayHello("def");
        assertEquals(info, "Hello,def");
    }

    public void testGetRegistry3() throws Exception {
        localServer.registerRemoteObject(new HelloImpl(), "hello");
        Thread.sleep(100);
        assertEquals(2, localServer.getRegistry().list().length);
        assertEquals(2, remoteServer.getRegistry().list().length);
        Hello hello = remoteServer.getRemoteObject("hello");
        String info = hello.sayHello("abc");
        assertEquals(info, "Hello,abc");

        remoteServer.registerRemoteObject(new HelloImpl(), "hello1");

        Thread.sleep(100);

        assertEquals(localServer.getRegistry().list().length, 3);
        assertEquals(remoteServer.getRegistry().list().length, 3);
        hello = localServer.getRemoteObject("hello1");
        info = hello.sayHello("def");
        assertEquals(info, "Hello,def");
    }
 /*    public void testRegisterRemoteObject() throws Exception {

    }

    public void testRegisterRemoteObject1() throws Exception {

    }

    public void testRegisterRemoteObject2() throws Exception {

    }

    public void testRegisterRemoteObject3() throws Exception {

    }

    public void testUnregisterRemoteObject() throws Exception {

    }

    public void testUnregisterRemoteObjectByType() throws Exception {

    }

    public void testUnregisterRemoteObjectByType1() throws Exception {

    }

    public void testUnregisterRemoteObject1() throws Exception {

    }

    public void testUnregisterRemoteObject2() throws Exception {

    }

    public void testGetRemoteObject() throws Exception {

    }

    public void testGetRemoteObject1() throws Exception {

    }

    public void testGetRemoteObjectList() throws Exception {

    }

    public void testGetRemoteObjectListInstanceOf() throws Exception {

    }

    public void testGetRemoteObjectList1() throws Exception {

    }

    public void testUnexportObjects() throws Exception {

    }*/
}
