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

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class CacheResponseWrapper
    extends HttpServletResponseWrapper {
  protected HttpServletResponse origResponse = null;
  protected ServletOutputStream stream = null;
  protected PrintWriter writer = null;
  protected OutputStream cache = null;

  public CacheResponseWrapper(HttpServletResponse response,
      OutputStream cache) {
    super(response);
    origResponse = response;
    this.cache = cache;
  }

  public ServletOutputStream createOutputStream()
      throws IOException {
    return (new CacheResponseStream(origResponse, cache));
  }

  public void flushBuffer() throws IOException {
    stream.flush();
  }

  public ServletOutputStream getOutputStream()
      throws IOException {
    if (writer != null) {
      throw new IllegalStateException(
        "getWriter() has already been called!");
    }

    if (stream == null)
      stream = createOutputStream();
    return (stream);
  }

  public PrintWriter getWriter() throws IOException {
    if (writer != null) {
      return (writer);
    }

    if (stream != null) {
      throw new IllegalStateException(
        "getOutputStream() has already been called!");
    }

   stream = createOutputStream();
   writer = new PrintWriter(new OutputStreamWriter(stream, "UTF-8"));
   return (writer);
  }
}
