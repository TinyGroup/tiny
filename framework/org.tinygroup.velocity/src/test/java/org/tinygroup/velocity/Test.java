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

import java.io.StringWriter;

import java.util.Properties;

import org.apache.velocity.app.Velocity;

import org.apache.velocity.VelocityContext;

import org.apache.velocity.exception.ParseErrorException;

import org.apache.velocity.exception.MethodInvocationException;

public class Test

{

	public static void main(String args[])

	{
		// first, we init the runtime engine. Defaults are fine.

		try {

			Velocity.init();

		} catch (Exception e) {
			System.out.println("Problem initializing Velocity : " + e);
			return;
		}

		// lets make a Context and put data into it

		VelocityContext context = new VelocityContext();
		context.put("name", "Velocity");
		context.put("project", "Jakarta");

		// lets render a template


        String input = "We are using $project $name to render this.";

        StringWriter out = new StringWriter();

		try {
			Velocity.evaluate(context, out, "mystring", input);
		} catch (ParseErrorException pee) {
			System.out.println("ParseErrorException : " + pee);
		} catch (MethodInvocationException mee) {
			System.out.println("MethodInvocationException : " + mee);
		} catch (Exception e) {
			System.out.println("Exception : " + e);
		}
		System.out.println(" string : " + out);

	}

}
