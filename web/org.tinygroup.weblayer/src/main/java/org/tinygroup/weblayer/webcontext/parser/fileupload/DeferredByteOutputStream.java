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
package org.tinygroup.weblayer.webcontext.parser.fileupload;

import java.io.IOException;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.commons.io.output.ThresholdingOutputStream;
import org.tinygroup.commons.io.ByteArrayInputStream;

/**
 * 
 * 功能说明:对普通字段与表单字段延迟输出
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-2 <br>
 * <br>
 */
public class DeferredByteOutputStream extends ThresholdingOutputStream {

	// ----------------------------------------------------------- Data members

	/**
	 * The output stream to which data will be written prior to the theshold
	 * being reached.
	 */
	private ByteArrayOutputStream memoryOutputStream;//存储普通字段

	/**
	 * The output stream to which data will be written at any given time. This
	 * will always be one of <code>memoryOutputStream</code> or
	 * <code>diskOutputStream</code>.
	 */
	private ByteArrayOutputStream currentOutputStream;//存储文件字节流

	/**
	 * True when close() has been called successfully.
	 */
	private boolean closed = false;

	// ----------------------------------------------------------- Constructors

	/**
	 * Constructs an instance of this class which will trigger an event at the
	 * specified threshold, and save data to a file beyond that point.
	 * 
	 * @param threshold
	 *            The number of bytes at which to trigger an event.
	 * @param outputFile
	 *            The file to which data is saved beyond the threshold.
	 */
	public DeferredByteOutputStream(int threshold) {
		super(threshold);
		memoryOutputStream = new ByteArrayOutputStream();
		currentOutputStream = memoryOutputStream;
	}

	// --------------------------------------- ThresholdingOutputStream methods

	/**
	 * Returns the current output stream. This may be memory based or disk
	 * based, depending on the current state with respect to the threshold.
	 * 
	 * @return The underlying output stream.
	 * 
	 * @exception IOException
	 *                if an error occurs.
	 */

	protected OutputStream getStream() throws IOException {
		return currentOutputStream;
	}

	/**
	 * Switches the underlying output stream from a memory based stream to one
	 * that is backed by disk. This is the point at which we realise that too
	 * much data is being written to keep in memory, so we elect to switch to
	 * disk-based storage.
	 * 
	 * @exception IOException
	 *                if an error occurs.
	 */

	protected void thresholdReached() throws IOException {
	    ByteArrayOutputStream totalStream=new ByteArrayOutputStream();
		memoryOutputStream.writeTo(totalStream);
		currentOutputStream = totalStream;
		memoryOutputStream = null;
	}

	// --------------------------------------------------------- Public methods

	/**
	 * Determines whether or not the data for this output stream has been
	 * retained in memory.
	 * 
	 * @return {@code true} if the data is available in memory; {@code false}
	 *         otherwise.
	 */
	public boolean isInMemory() {
		return !isThresholdExceeded();
	}

	/**
	 * Returns the data for this output stream as an array of bytes, assuming
	 * that the data has been retained in memory. If the data was written to
	 * mongo, this method returns {@code null}.
	 * 
	 * @return The data for this output stream, or {@code null} if no such data
	 *         is available.
	 */
	public byte[] getMemoryData() {
		if (memoryOutputStream != null) {
			return memoryOutputStream.toByteArray();
		}
		return null;
	}
	/**
	 * 
	 * 返回文件字节流
	 * @return
	 */
	public byte[] getFileData() {
		if (currentOutputStream != null) {
			return currentOutputStream.toByteArray();
		}
		return null;
	}



	/**
	 * Closes underlying output stream, and mark this as closed
	 * 
	 * @exception IOException
	 *                if an error occurs.
	 */

	public void close() throws IOException {
		super.close();
		closed = true;
	}

	/**
	 * Writes the data from this output stream to the specified output stream,
	 * after it has been closed.
	 * 
	 * @param out
	 *            output stream to write to.
	 * @exception IOException
	 *                if this stream is not yet closed or an error occurs.
	 */
	public void writeTo(OutputStream out) throws IOException {
		// we may only need to check if this is closed if we are working with a
		// file
		// but we should force the habit of closing wether we are working with
		// a file or memory.
		if (!closed) {
			throw new IOException("Stream not closed");
		}

		if (isInMemory()) {
			memoryOutputStream.writeTo(out);
		} else {
			ByteArrayInputStream  byteInputStream = new ByteArrayInputStream(currentOutputStream.toByteArray());
			try {
				IOUtils.copy(byteInputStream, out);
			} finally {
				IOUtils.closeQuietly(byteInputStream);
			}
		}
	}
	
	public void clearData(){
		memoryOutputStream=null;
		currentOutputStream=null;
	}
}
