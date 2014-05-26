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
package org.tinygroup.jsqlparser;

/**
 * An exception class with stack trace informations
 */
public class JSQLParserException extends Exception {

	/* The serial class version */
	private static final long serialVersionUID = -1099039459759769980L;
	private Throwable cause = null;

	public JSQLParserException() {
		super();
	}

	public JSQLParserException(String arg0) {
		super(arg0);
	}

	public JSQLParserException(Throwable arg0) {
		this.cause = arg0;
	}

	public JSQLParserException(String arg0, Throwable arg1) {
		super(arg0);
		this.cause = arg1;
	}


	public Throwable getCause() {
		return cause;
	}


	public void printStackTrace() {
		printStackTrace(System.err);
	}


	public void printStackTrace(java.io.PrintWriter pw) {
		super.printStackTrace(pw);
		if (cause != null) {
			pw.println("Caused by:");
			cause.printStackTrace(pw);
		}
	}


	public void printStackTrace(java.io.PrintStream ps) {
		super.printStackTrace(ps);
		if (cause != null) {
			ps.println("Caused by:");
			cause.printStackTrace(ps);
		}
	}
}
