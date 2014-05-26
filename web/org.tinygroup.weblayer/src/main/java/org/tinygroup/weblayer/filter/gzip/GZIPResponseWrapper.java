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
package org.tinygroup.weblayer.filter.gzip;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.tinygroup.logger.Logger;
import org.tinygroup.logger.LoggerFactory;

public class GZIPResponseWrapper extends HttpServletResponseWrapper {
	protected ByteArrayOutputStream baos = new ByteArrayOutputStream();
	protected PrintWriter writer = null;
	
	private Logger logger=LoggerFactory.getLogger(GZIPResponseWrapper.class);

	public GZIPResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	public ServletOutputStream getOutputStream() throws IOException {
		return new GZIPResponseStream(baos);
	}

	public PrintWriter getWriter() throws IOException {
		writer = new PrintWriter(new OutputStreamWriter(baos,
				super.getCharacterEncoding()));
		return writer;
	}

	public byte[] getBufferedBytes() {
		try {
			if (writer != null)
				writer.close();
			    baos.flush();
		} catch (IOException e) {
			logger.errorMessage(e.getMessage(), e);
		}
		byte[] byteArray = baos.toByteArray();
		return byteArray;
	}

}
