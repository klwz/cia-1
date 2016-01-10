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
package com.hack23.cia.service.api.action.admin;

import com.hack23.cia.service.api.action.common.ServiceRequest;


/**
 * The Class UpdateApplicationConfigurationRequest.
 */
public final class UpdateApplicationConfigurationRequest implements ServiceRequest {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

    /** The session id. */
    private String sessionId;

	/** The application configuration id. */
	protected Long applicationConfigurationId;

    /** The config title. */
    protected String configTitle;

    /** The config description. */
    protected String configDescription;

    /** The component title. */
    protected String componentTitle;

    /** The component description. */
    protected String componentDescription;

    /** The property value. */
    protected String propertyValue;

	/**
	 * Gets the session id.
	 *
	 * @return the session id
	 */
	public String getSessionId() {
		return sessionId;
	}

	/**
	 * Sets the session id.
	 *
	 * @param sessionId
	 *            the new session id
	 */
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	/**
	 * Gets the application configuration id.
	 *
	 * @return the application configuration id
	 */
	public Long getApplicationConfigurationId() {
		return applicationConfigurationId;
	}

	/**
	 * Sets the application configuration id.
	 *
	 * @param applicationConfigurationId
	 *            the new application configuration id
	 */
	public void setApplicationConfigurationId(Long applicationConfigurationId) {
		this.applicationConfigurationId = applicationConfigurationId;
	}

	/**
	 * Gets the config title.
	 *
	 * @return the config title
	 */
	public String getConfigTitle() {
		return configTitle;
	}

	/**
	 * Sets the config title.
	 *
	 * @param configTitle
	 *            the new config title
	 */
	public void setConfigTitle(String configTitle) {
		this.configTitle = configTitle;
	}

	/**
	 * Gets the config description.
	 *
	 * @return the config description
	 */
	public String getConfigDescription() {
		return configDescription;
	}

	/**
	 * Sets the config description.
	 *
	 * @param configDescription
	 *            the new config description
	 */
	public void setConfigDescription(String configDescription) {
		this.configDescription = configDescription;
	}

	/**
	 * Gets the component title.
	 *
	 * @return the component title
	 */
	public String getComponentTitle() {
		return componentTitle;
	}

	/**
	 * Sets the component title.
	 *
	 * @param componentTitle
	 *            the new component title
	 */
	public void setComponentTitle(String componentTitle) {
		this.componentTitle = componentTitle;
	}

	/**
	 * Gets the component description.
	 *
	 * @return the component description
	 */
	public String getComponentDescription() {
		return componentDescription;
	}

	/**
	 * Sets the component description.
	 *
	 * @param componentDescription
	 *            the new component description
	 */
	public void setComponentDescription(String componentDescription) {
		this.componentDescription = componentDescription;
	}

	/**
	 * Gets the property value.
	 *
	 * @return the property value
	 */
	public String getPropertyValue() {
		return propertyValue;
	}

	/**
	 * Sets the property value.
	 *
	 * @param propertyValue
	 *            the new property value
	 */
	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

}