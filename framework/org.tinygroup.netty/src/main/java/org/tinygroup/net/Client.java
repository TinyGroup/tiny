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
package org.tinygroup.net;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.*;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;
import org.tinygroup.net.exception.InterruptedRuntimeException;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 客户端抽象类
 *
 * @author luoguo
 */
public abstract class Client implements Netty {

    private final String host;
    private final int port;
    volatile private boolean ready = false;
    protected ChannelHandlerContext context;
    ClientBootstrap bootstrap;
    ExecutorService pool1;
    ExecutorService pool2;
    ChannelFuture connectFuture;
    protected int timeout = 5000;// ms
    volatile private String status = "";
    private static final String RUNNING = "running";
    static String CONNECINGT = "connecting";
    static String CLOSED = "closed";
    static String FIRED_EXCEPTION = "fired_exception";
    private byte[] lock = new byte[0];

    /**
     * 停止方法
     */
    public void stop() {
        if (bootstrap != null)
            bootstrap.shutdown();
    }

    /**
     * 设置超时时间
     *
     * @param timeout
     */
    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public boolean isClosed() {
        return CLOSED.equals(status);
    }

    /**
     * 发送对象
     *
     * @param object
     * @return
     */
    public <T> T sendObject(Object object) {
        synchronized (status) {
            if (status.equals(CLOSED)) {
                status = FIRED_EXCEPTION;
                throw new InterruptedRuntimeException("网络连接已经关闭！");
            }
            if (!isReady()) {
                throw new RuntimeException("客户端没有就绪，不能发送报文！");
            }
        }
        return (T) sendObjectLocal(object);
    }

    public abstract <T> T sendObjectLocal(Object object);

    /**
     * 启动方法
     */
    public void run() {
        // 创建线程池
        status = CONNECINGT;
        pool1 = Executors.newCachedThreadPool();
        pool2 = Executors.newCachedThreadPool();
        bootstrap = new ClientBootstrap(new NioClientSocketChannelFactory(pool1, pool2));

        // 设置pipeline工厂.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() throws Exception {
                return Channels.pipeline(getEncoder(), getDecoder(), getHandler());
            }
        });
        connectFuture = bootstrap.connect(new InetSocketAddress(host, port));
        status = RUNNING;

        connectFuture.awaitUninterruptibly().getChannel();

        // 等待运行，直接连接关闭或出错
        connectFuture.getChannel().getCloseFuture().awaitUninterruptibly();
        synchronized (lock) {
            status = CLOSED;
            ready = false;
        }

        // 停止线程池并退出
        bootstrap.releaseExternalResources();
    }

    /**
     * @param ready
     */
    void setReady(boolean ready) {
        this.ready = ready;
    }

    public boolean isReady() {
        return ready;
    }

    /**
     * 设置通道上下文
     *
     * @param ctx
     */
    void setChannelHandlerContext(ChannelHandlerContext ctx) {
        this.context = ctx;

    }

}
