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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.IOUtils;
import org.tinygroup.commons.tools.StringUtil;

/**
 * 
 * 功能说明: tiny保存的文件项
 * <p>

 * 开发人员: renhui <br>
 * 开发时间: 2014-1-2 <br>
 * <br>
 */
public class TinyFileItem implements FileItem {

	private static final long serialVersionUID = -237775327152222987L;
	/**
	 * Default content charset to be used when no explicit charset parameter is
	 * provided by the sender. Media subtypes of the "text" type are defined to
	 * have a default charset value of "ISO-8859-1" when received via HTTP.
	 */
	public static final String DEFAULT_CHARSET = "ISO-8859-1";

	private String fieldName;
	private String contentType;
	private boolean isFormField;
	private String fileName;
	/** Cached contents of the file. */
	private byte[] cachedContent;
	private long size = -1;

	private DeferredByteOutputStream dbos;

	private int sizeThreshold;
	/** 用于解码字段值的字符集编码。 */
	private String charset;
	
	private FileItemStorage storage;
	/**
	 * 如果是文件则生成文件对象的唯一标识,一般在存储文件对象时进行绑定
	 */
	private String unique;

	public TinyFileItem(String fieldName, String contentType,
			boolean isFormField, String fileName,FileItemStorage storage) {
		super();
		this.fieldName = fieldName;
		this.contentType = contentType;
		this.isFormField = isFormField;
		this.fileName = fileName;
		this.storage=storage;
	}

	public InputStream getInputStream() throws IOException {
		if (!isInMemory()) {
			return new ByteArrayInputStream(dbos.getFileData());
		}

		if (cachedContent == null) {
			cachedContent = dbos.getMemoryData();
		}
		return new ByteArrayInputStream(cachedContent);

	}

	public String getContentType() {
		return contentType;
	}

	public String getName() {
		return fileName;
	}

	public boolean isInMemory() {
		return isFormField;// 普通表单字段存放在内存中
	}

	public long getSize() {
		if (size >= 0) {
			return size;
		} else if (cachedContent != null) {
			return cachedContent.length;
		} else if (dbos.isInMemory()) {
			return dbos.getMemoryData().length;
		} else {
			return dbos.getByteCount();
		}

	}

	public byte[] get() {
		if (isInMemory()) {
			if (cachedContent == null) {
				cachedContent = dbos.getMemoryData();
			}
			return cachedContent;
		}
		byte[] fileData = new byte[(int) getSize()];
		ByteArrayInputStream byteInStream = null;
		try {
			byteInStream = new ByteArrayInputStream(dbos.getFileData());
			byteInStream.read(fileData);
		} catch (IOException e) {
			fileData = null;
		} finally {
			if (byteInStream != null) {
				try {
					byteInStream.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
		return fileData;
	}

	public String getString(String encoding)
			throws UnsupportedEncodingException {
		return new String(get(), encoding);
	}

	public String getString() {
		byte[] rawdata = get();
		String charset = null;
		if (isFormField()) {
			charset = getCharset();
		}
		if (charset == null) {
			charset = DEFAULT_CHARSET;
		}
		try {
			return new String(rawdata, charset);
		} catch (UnsupportedEncodingException e) {
			try {
				return new String(rawdata, DEFAULT_CHARSET);
			} catch (UnsupportedEncodingException ee) {
				return new String(rawdata);
			}
		}
	}

	public void write(File file) throws Exception {
		// 自动创建目录
		if (file != null) {
			file.getParentFile().mkdirs();
		}

		if (isInMemory()) {
			FileOutputStream fout = null;
			try {
				fout = new FileOutputStream(file);
				fout.write(get());
			} finally {
				if (fout != null) {
					fout.close();
				}
			}
		} else {
			// Save the length of the file
			size = dbos.getByteCount();
			/*
			 * The uploaded file is being stored on disk in a temporary location
			 * so move it to the desired file.
			 */
			BufferedInputStream in = null;
			BufferedOutputStream out = null;
			try {
				in = new BufferedInputStream(new ByteArrayInputStream(
						dbos.getFileData()));
				out = new BufferedOutputStream(new FileOutputStream(file));
				IOUtils.copy(in, out);
			} finally {
				if (in != null) {
					try {
						in.close();
					} catch (IOException e) {
						// ignore
					}
				}
				if (out != null) {
					try {
						out.close();
					} catch (IOException e) {
						// ignore
					}
				}
			}
		}
	}
	/**
	 * 
	 * 调用存储接口
	 */
	public void storage(){
		if(storage!=null&&!isInMemory()){
			setUnique(storage.storage(this));
		}
	}

	public void delete() {
		cachedContent = null;
		dbos.clearData();
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String name) {
        this.fieldName=name;
	}

	public boolean isFormField() {
		return isFormField;
	}

	public void setFormField(boolean state) {
		isFormField = state;
	}

	public OutputStream getOutputStream() throws IOException {
		if (dbos == null) {
			if (isFormField()) {
				sizeThreshold = Integer.MAX_VALUE;
			}
			dbos = new DeferredByteOutputStream(sizeThreshold);
		}
		return dbos;
	}

	/** 取得当前field的字符集编码。 */
	public String getCharset() {
		return charset;
	}

	/** 设置当前field的字符集编码。 */
	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getUnique() {
		return unique;
	}

	public void setUnique(String unique) {
		this.unique = unique;
	}

	/**
	 * Returns a string representation of this object.
	 * 
	 * @return a string representation of this object.
	 */

	public String toString() {
		return StringUtil.defaultIfEmpty(getName(), getString());
	}

}
