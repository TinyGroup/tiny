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
package org.tinygroup.tinyioc;

import org.tinygroup.tinyioc.impl.BeanContainerImpl;

/**
 * Created by luoguo on 13-12-27.
 */
public class TestRequest {
    static Pet2[] Pet2s = new Pet2[2];
    static BeanContainer container = new BeanContainerImpl();

    public static void main(String[] args) throws InterruptedException {
        container.registerClass(Pet2.class);
        for (int i = 0; i < 2; i++) {
            NewThread thread = new NewThread(i);
            thread.start();
            ;
        }
        Thread.sleep(100);
        System.out.println(Pet2s[0] == Pet2s[1]);
    }

    static class NewThread extends Thread {
        private final int index;

        public NewThread(int index) {
            this.index = index;
        }

        public void run() {
            Pet2 Pet2 = container.getBeanByName("Pet2");
            Pet2 Pet21 = container.getBeanByName("Pet2");
            System.out.println(Pet2 == Pet21);
            Pet2s[index] = Pet2;
        }
    }
}
