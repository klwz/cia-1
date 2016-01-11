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
package com.hack23.cia.service.impl.action.application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.databene.contiperf.PerfTest;
import org.databene.contiperf.Required;
import org.databene.contiperf.junit.ContiPerfRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import com.hack23.cia.model.internal.application.system.impl.ApplicationEventGroup;
import com.hack23.cia.model.internal.application.system.impl.ApplicationOperationType;
import com.hack23.cia.model.internal.application.system.impl.ApplicationSession;
import com.hack23.cia.model.internal.application.system.impl.ApplicationSessionType;
import com.hack23.cia.model.internal.application.system.impl.ApplicationSession_;
import com.hack23.cia.service.api.ApplicationManager;
import com.hack23.cia.service.api.action.application.CreateApplicationEventRequest;
import com.hack23.cia.service.api.action.application.CreateApplicationEventResponse;
import com.hack23.cia.service.api.action.application.CreateApplicationSessionRequest;
import com.hack23.cia.service.api.action.application.CreateApplicationSessionResponse;
import com.hack23.cia.service.api.action.common.ServiceResponse.ServiceResult;
import com.hack23.cia.service.data.api.ApplicationSessionDAO;
import com.hack23.cia.service.impl.AbstractServiceFunctionalIntegrationTest;

/**
 * The Class CreateApplicationEventServiceITest.
 */
@PerfTest(threads = 10, duration = 3000, warmUp = 1500)
@Required(max = 200,average = 10,percentile95=15,throughput=50000)
public class CreateApplicationEventServiceITest extends AbstractServiceFunctionalIntegrationTest {

	/** The i. */
	@Rule
	public ContiPerfRule i = new ContiPerfRule();


	/** The application manager. */
	@Autowired
	private ApplicationManager applicationManager;

	/** The application session dao. */
	@Autowired
	private ApplicationSessionDAO applicationSessionDAO;


	/**
	 * Service create application event request success test.
	 *
	 * @throws Exception
	 *             the exception
	 */
	@Test
	@PerfTest(threads = 4, duration = 3000, warmUp = 1500)
	@Required(max = 1000,average = 300,percentile95=350,throughput=20)
	public void serviceCreateApplicationEventRequestSuccessTest() throws Exception {
		final Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_ANONYMOUS"));
		SecurityContextHolder.getContext().setAuthentication(new AnonymousAuthenticationToken("key", "principal", authorities));


		final CreateApplicationSessionRequest createSessionRequest = createTestApplicationSession();

		final CreateApplicationEventRequest serviceRequest = new CreateApplicationEventRequest();
		serviceRequest.setSessionId(createSessionRequest.getSessionId());
		serviceRequest.setApplicationMessage("applicationMessage");

		serviceRequest.setEventGroup(ApplicationEventGroup.USER);
		serviceRequest.setApplicationOperation(ApplicationOperationType.CREATE);

		serviceRequest.setPage("Test");
		serviceRequest.setPageMode("PageMode");
		serviceRequest.setElementId("ElementId");

		serviceRequest.setActionName("Content");

		serviceRequest.setApplicationMessage("applicationMessage");
		serviceRequest.setErrorMessage("errorMessage");


		final CreateApplicationEventResponse  response = (CreateApplicationEventResponse) applicationManager.service(serviceRequest);
		assertNotNull("Expect a result",response);
		assertEquals(ServiceResult.SUCCESS,response.getResult());

		final List<ApplicationSession> findListByProperty = applicationSessionDAO.findListByProperty(ApplicationSession_.sessionId, serviceRequest.getSessionId());
		assertEquals(1, findListByProperty.size());
		final ApplicationSession applicationSession = findListByProperty.get(0);
		assertNotNull(applicationSession);

		assertEquals(1, applicationSession.getEvents().size());


	}


	/**
	 * Creates the test application session.
	 *
	 * @return the creates the application session request
	 */
	private CreateApplicationSessionRequest createTestApplicationSession() {
		final CreateApplicationSessionRequest serviceRequest = new CreateApplicationSessionRequest();
		serviceRequest.setIpInformation("8.8.8.8");
		serviceRequest.setLocale("en_US.UTF-8");
		serviceRequest.setOperatingSystem("LINUX");
		serviceRequest.setSessionId(UUID.randomUUID().toString());
		serviceRequest.setSessionType(ApplicationSessionType.ANONYMOUS);
		serviceRequest.setUserAgentInformation("Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:42.0) Gecko/20100101 Firefox/42.0");

		final CreateApplicationSessionResponse sessionResponse = (CreateApplicationSessionResponse) applicationManager
				.service(serviceRequest);
		return serviceRequest;
	}

}
