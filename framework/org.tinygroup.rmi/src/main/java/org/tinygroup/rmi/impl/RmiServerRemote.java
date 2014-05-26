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
package org.tinygroup.rmi.impl;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;
import org.tinygroup.rmi.RmiServer;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 远程RMI服务器 Created by luoguo on 14-1-10.
 */
public class RmiServerRemote implements RmiServer {
    private int port = DEFAULT_RMI_PORT;
    private String hostName = "localhost";
    private Registry registry = null;
    private RmiServer server = null;
    private Map<String, Remote> objectMap = new HashMap<String, Remote>();
    private static transient Logger logger = LoggerFactory.getLogger(RmiServerRemote.class);
    RemoteRmiHeartBeatChecker heartBeatChecker = new RemoteRmiHeartBeatChecker();

    public RmiServerRemote() throws RemoteException {
        this("localhost", RmiServer.DEFAULT_RMI_PORT);
    }

    public RmiServerRemote(int port) throws RemoteException {
        this("localhost", port);
    }

    public RmiServerRemote(String hostName) throws RemoteException {
        this(hostName, RmiServer.DEFAULT_RMI_PORT);
    }

    public RmiServerRemote(String hostName, int port) throws RemoteException {
//        System.setProperty("java.rmi.server.useLocalHostname", "true");
        System.setProperty("java.rmi.server.hostname ", hostName);
        this.hostName = hostName;
        this.port = port;
        registry = getRegistry();
        new Thread(heartBeatChecker).start();
    }

    public synchronized Registry getRegistry() throws RemoteException {
        try {
            registry = LocateRegistry.getRegistry(hostName, port);
            server = (RmiServer) registry.lookup("RmiServer");
            return registry;
        } catch (NotBoundException e) {
            throw new RemoteException("找不到RmiServer远程对象！", e);
        }
    }


    public void registerRemoteObject(Remote object, Class type, String id) throws RemoteException {
        objectMap.put(RmiUtil.getName(type.getName(), id), object);
        server.registerRemoteObject(object, type, id);
    }

    public void registerRemoteObject(Remote object, String type, String id) throws RemoteException {
        objectMap.put(RmiUtil.getName(type, id), object);
        server.registerRemoteObject(object, type, id);
    }

    public void registerRemoteObject(Remote object, String name) throws RemoteException {
        objectMap.put(name, object);
        server.registerRemoteObject(object, name);
    }

    public void registerRemoteObject(Remote object, Class type) throws RemoteException {
        objectMap.put(type.getName(), object);
        server.registerRemoteObject(object, type);
    }


    public void unregisterRemoteObject(String name) throws RemoteException {
        objectMap.remove(name);
        server.unregisterRemoteObject(name);
    }

    public void unregisterRemoteObjectByType(Class type) throws RemoteException {
        objectMap.remove(type.getName());
        server.unregisterRemoteObjectByType(type);
    }

    public void unregisterRemoteObjectByType(String type) throws RemoteException {
        objectMap.remove(type);
        server.unregisterRemoteObjectByType(type);
    }

    public void unregisterRemoteObject(String type, String id) throws RemoteException {
        objectMap.remove(RmiUtil.getName(type, id));
        server.unregisterRemoteObject(type, id);
    }

    public void unregisterRemoteObject(Class type, String id) throws RemoteException {
        objectMap.remove(RmiUtil.getName(type.getName(), id));
        server.unregisterRemoteObject(type, id);
    }

    public <T> T getRemoteObject(String name) throws RemoteException {
        return (T) server.getRemoteObject(name);
    }

    public <T> T getRemoteObject(Class<T> type) throws RemoteException {
        return (T) server.getRemoteObject(type);
    }

    public <T> List<T> getRemoteObjectList(Class<T> type) throws RemoteException {
        return (List<T>) server.getRemoteObjectList(type);
    }

    public <T> List<T> getRemoteObjectListInstanceOf(Class<T> type) throws RemoteException {
        return (List<T>) server.getRemoteObjectListInstanceOf(type);
    }

    public <T> List<T> getRemoteObjectList(String typeName) throws RemoteException {
        return (List<T>) server.getRemoteObjectList(typeName);
    }

    public void unexportObjects() throws RemoteException {
        for (Map.Entry<String, Remote> entry : objectMap.entrySet()) {
            server.unregisterRemoteObject(entry.getKey());
            objectMap.remove(entry);
        }
    }


    public void unregisterRemoteObject(Remote object) throws RemoteException {
        for (Map.Entry<String, Remote> entry : objectMap.entrySet()) {
            if (entry.getValue().equals(object)) {
                server.unregisterRemoteObject(entry.getKey());
                objectMap.remove(entry);
            }
        }
    }

    protected void registerAllRemoteObject() {
        for (Map.Entry<String, Remote> entry : objectMap.entrySet()) {
            try {
                logger.logMessage(LogLevel.DEBUG, "向远端服务器渡口对象:{}开始...", entry.getKey());
                registerRemoteObject(entry.getValue(), entry.getKey());
                logger.logMessage(LogLevel.DEBUG, "向远端服务器渡口对象:{}结束。", entry.getKey());
            } catch (RemoteException e) {
                logger.errorMessage("向远端服务器渡口对象:{}时发生异常。", e, entry.getKey());
            }
        }
    }

    public void stop() throws RemoteException {
        unexportObjects();
        heartBeatChecker.setStop(true);
        try {
            Thread.sleep(20);
        } catch (InterruptedException e) {
            //DoNothing
        }
        registry = null;
    }

    public class RemoteRmiHeartBeatChecker implements Runnable {
        private int interval = 5000;
        private volatile boolean stop = false;

        public RemoteRmiHeartBeatChecker() {
        }

        public void setStop(boolean stop) {
            this.stop = stop;
        }

        public void run() {
            boolean isDown = false;
            while (!stop && registry != null) {
                try {
                    getRegistry().list();//检查registry是否可用
                    //如果可用
                    if (isDown) {
                        registerAllRemoteObject();
                    }
                } catch (RemoteException e) {
                    isDown = true;
                }
                try {
                    Thread.sleep(interval);
                } catch (InterruptedException e) {
                    //DoNothing
                }
            }
        }


    }

}
