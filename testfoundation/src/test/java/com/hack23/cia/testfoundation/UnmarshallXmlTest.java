/*
 * Copyright 2010 James Pether Sörling
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 *	$Id$
 *  $HeadURL$
*/
package com.hack23.cia.testfoundation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.junit.Test;
import org.springframework.oxm.xstream.XStreamMarshaller;

import com.thoughtworks.xstream.XStream;

/**
 * The Class UnmarshallXmlTest.
 */
public class UnmarshallXmlTest extends AbstractUnmarshallXmlTest<String> {

	/**
	 * Unmarshall test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	public void unmarshallTest() throws Exception {
		assertNull(getDatabaseConnection());

		final String content = "abcdefg";
		final String xmlContent = "<string>" + content + "</string>";
		File temp;
		temp = File.createTempFile("UnmarshallXmlTestTempFile", ".xml");

		final BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
		bw.write(xmlContent);
		bw.close();

		final XStream xstream = new XStream();
		xstream.alias("TestXml", TestXml.class);

		assertEquals(content, unmarshallXml(new XStreamMarshaller(), temp.getPath()));
	}
}
