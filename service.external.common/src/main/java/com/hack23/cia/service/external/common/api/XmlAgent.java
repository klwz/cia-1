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
package com.hack23.cia.service.external.common.api;

import org.springframework.oxm.Unmarshaller;

/**
 * The Interface XmlAgent.
 */
public interface XmlAgent {

	/**
	 * Unmarshall xml.
	 *
	 * @param unmarshaller
	 *            the unmarshaller
	 * @param accessUrl
	 *            the access url
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	Object unmarshallXml(Unmarshaller unmarshaller, String accessUrl)
			throws Exception;

	/**
	 * Unmarshall xml.
	 *
	 * @param unmarshaller
	 *            the unmarshaller
	 * @param accessUrl
	 *            the access url
	 * @param nameSpace
	 *            the name space
	 * @param replace
	 *            the replace
	 * @param with
	 *            the with
	 * @return the object
	 * @throws Exception
	 *             the exception
	 */
	Object unmarshallXml(Unmarshaller unmarshaller, String accessUrl,
			String nameSpace, String replace, String with) throws Exception;

	/**
	 * Retrive content.
	 *
	 * @param accessUrl
	 *            the access url
	 * @return the string
	 * @throws Exception
	 *             the exception
	 */
	String retriveContent(String accessUrl) throws Exception;

}
