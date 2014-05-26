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
package org.tinygroup.net.daemon;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

/**
 * 监护线程
 * 
 * @author luoguo
 * 
 */
public abstract class DaemonRunnable implements Runnable {
	private static final int DEFAULT_WAIT_TIME = 5000;
	private int waitTime = DEFAULT_WAIT_TIME;// 如果机器停掉，则等待这段时间之后，再做下一次连接
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	volatile boolean end = false;

	/**
	 * 停止
	 */
	public void stop() {
		this.end = true;
        logger.log(LogLevel.INFO,"开始停止运行...");
		stopAction();
        logger.log(LogLevel.INFO,"停止运行结束。");
	}

	public int getWaitTime() {
		return waitTime;
	}

	/**
	 * 设置重试等待时间
	 * 
	 * @param waitTime
	 */
	public void setWaitTime(int waitTime) {
		this.waitTime = waitTime;
	}

	/**
	 * Runnable接口实现方法
	 */
	public void run() {
		while (!end) {
			try {
                logger.log(LogLevel.INFO,"开始启动运行...");
				startAction();
                logger.log(LogLevel.INFO,"运行启动结束。");
			} catch (Exception e) {
				logger.error(e);
			} finally {
				try {
					Thread.sleep(waitTime);
				} catch (InterruptedException e) {
					logger.error(e);
				}

			}
		}
	}

	/**
	 * 启动操作
	 */
	protected abstract void startAction();

	/**
	 * 停止操作
	 */
	protected abstract void stopAction();
}
