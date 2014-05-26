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
package org.tinygroup.weblayer.servlet;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.tinygroup.fileresolver.FullContextFileRepository;
import org.tinygroup.springutil.SpringUtil;
import org.tinygroup.vfs.FileObject;
/**
 * 
 * @author luoguo
 *
 */
public class FullContextUrlRedirectServlet extends HttpServlet {
	FullContextFileRepository fullContextFileRepository;

	
	public void init() {
		fullContextFileRepository = SpringUtil.getBean(
				"fullContextFileRepository");
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -1514050556632003280L;

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String servletPath = request.getServletPath();
		if(servletPath==null||servletPath.length()==0){
			servletPath=request.getPathInfo();
		}
		FileObject fileObject = fullContextFileRepository
				.getFileObject(servletPath);
		if (fileObject != null && fileObject.isExist()) {
			response.setContentType(fullContextFileRepository
					.getFileContentType(fileObject.getExtName()));
			OutputStream outputStream = response.getOutputStream();
			byte[] buf = new byte[(int) fileObject.getSize()];
			fileObject.getInputStream().read(buf);
			outputStream.write(buf);
			outputStream.flush();
			outputStream.close();
		} else {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}


}
