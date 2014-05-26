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
package org.tinygroup.threadgroup;

import org.tinygroup.logger.LogLevel;
import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * 多线程处理器
 *
 * @author luoguo
 */
public final class MultiThreadProcessor {
    private static Logger logger = LoggerFactory.getLogger(MultiThreadProcessor.class);
    private CountDownLatch downLatch = null;
    private List<Processor> processors = new ArrayList<Processor>();
    private String name;

    public MultiThreadProcessor(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void addProcessor(Processor[] processors) {
        for (Processor processor : processors) {
            addProcessor(processor);
        }
    }

    public void addProcessor(Processor processor) {
        processors.add(processor);
        processor.setMultiThreadProcess(this);
    }

    public void addProcessor(Collection<Processor> processorCollection) {
        for (Processor processor : processorCollection) {
            addProcessor(processor);
        }
    }

    public void threadDone() {
        downLatch.countDown();
    }

    public void start() {
        this.downLatch = new CountDownLatch(processors.size());
        logger.logMessage(LogLevel.DEBUG, "线程组<{}>运行开始,线程数{}...", name, processors.size());
        long start = System.currentTimeMillis();

        for (Processor processor : processors) {
            Thread thread = new Thread(processor);
            thread.setName(name + "-" + processor.getName());
            thread.start();
        }

        try {
            downLatch.await(); // 等待所有工作线程完成.
        } catch (InterruptedException e) {
            logger.errorMessage("缓程组<{}运行出现问题：{}>", e, name, e.getMessage());
        }
        long end = System.currentTimeMillis();
        logger.logMessage(LogLevel.DEBUG, "线程组<{}>运行结束, 用时:{}ms", name, (end - start));
    }

}
