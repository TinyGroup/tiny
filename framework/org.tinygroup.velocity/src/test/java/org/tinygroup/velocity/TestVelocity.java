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
package org.tinygroup.velocity;

import java.io.File;
import java.io.StringWriter;

import org.tinygroup.context.Context;
import org.tinygroup.context.impl.ContextImpl;
import org.tinygroup.fileresolver.impl.FullContextFileRepositoryImpl;
import org.tinygroup.velocity.impl.VelocityHelperImpl;
import org.tinygroup.vfs.FileObject;
import org.tinygroup.vfs.VFS;

public class TestVelocity {

	public static void main(String[] args) throws Exception {
		VelocityHelperImpl velocityHelper = new VelocityHelperImpl();
		FullContextFileRepositoryImpl fullContextFileRepository = new FullContextFileRepositoryImpl();
		File baseFile = new File("src/test/resources");
		fullContextFileRepository.addFileObject("/aa/bb/aa.view", VFS
				.resolveFile(baseFile+"test/aa/bb/aa.view"));
		fullContextFileRepository.addFileObject(
				"/aa/bb/default.view",
				VFS.resolveFile(baseFile+
						"test/aa/bb/default.view"));
		fullContextFileRepository.addFileObject(
				"/aa/default.view",
				VFS.resolveFile(baseFile+
						"test/aa/default.view"));
		fullContextFileRepository
				.addFileObject(
						"/default.view",
						VFS.resolveFile(baseFile+
								"test/default.view"));
		fullContextFileRepository.addFileObject(
				"/aa/bb/default.layout",
				VFS.resolveFile(baseFile+
						"test/aa/bb/default.layout"));
		fullContextFileRepository.addFileObject(
				"/aa/bb/bb.view",
				VFS.resolveFile(baseFile+
						"test/aa/bb/bb.view"));
		fullContextFileRepository.addFileObject(
				"/aa/cc/bb.view",
				VFS.resolveFile(baseFile+
						"test/aa/cc/bb.view"));
		fullContextFileRepository.addFileObject(
				"/aa/bb/aa.layout",
				VFS.resolveFile(baseFile+
						"test/aa/bb/aa.layout"));
		fullContextFileRepository.addFileObject(
				"/aa/default.layout",
				VFS.resolveFile(baseFile+
						"test/aa/default.layout"));
		fullContextFileRepository.addFileObject(
				"/ttt.view",
				VFS.resolveFile(baseFile+
						"test/ttt.view"));
		fullContextFileRepository.addFileObject(
				"/default.layout",
				VFS.resolveFile(baseFile+
						"test/default.layout"));
		velocityHelper.setFullContextFileRepository(fullContextFileRepository);
		velocityHelper.addMacroFile(VFS.resolveFile(baseFile+
				"test/default.component"));
		Context context = new ContextImpl();
		context.put("name", "罗果");
		StringWriter writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/aa/bb/aa.view");
		System.out.println(writer);
		writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/aa/bb/bb.view");
		System.out.println(writer);
		writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/aa/cc/bb.view");
		System.out.println(writer);
		System.out.println("=============================");
		writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/ttt.view");
		System.out.println(writer);

		FileObject resolveFile = VFS.resolveFile(baseFile+
				"test/ttt.component");
		velocityHelper.addMacroFile(resolveFile);
		writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/ttt.view");
		System.out.println(writer);
		System.out.println("begin sleep.");
		Thread.sleep(10000);
		System.out.println("end sleep.");
		resolveFile = VFS.resolveFile(baseFile+
				"test/ttt.component");
		velocityHelper.removeMacroFile(resolveFile);
		velocityHelper.addMacroFile(resolveFile);
		writer = new StringWriter();
		velocityHelper.processTempleateWithLayout(context, writer,
				"/ttt.view");
		System.out.println(writer);
	}
}
