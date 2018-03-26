package test;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.client.RestTemplate;

import asw.Application;
import asw.webService.IncidenceData;

@SuppressWarnings("deprecation")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest({ "server.port=0" })
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MainTest {

	@Value("${local.server.port}")
	private int port;

	private URL base;
	private RestTemplate template;

	private IncidenceData incidenceData1;
	private IncidenceData incidenceData2;

	@Before
	public void setUp() throws Exception {
		this.base = new URL("http://localhost:" + port + "/");
		template = new TestRestTemplate();

		String username = "maria@gmail.com";
		String password = "123123";

		String name = "FUGA GAS";
		String description = "Fuga de gas cocina";
		String location = "gijon";
		String tags = "bombona,gas";
		String additionalInformation = "Butano";
		String properties = "bombona:butano";
		String state = "OPEN";
		String notification = "yes";
		String expireAt = "tomorrowland";
		String assignedTo = "x";

		incidenceData1 = new IncidenceData(username, password, name, description, location, tags, additionalInformation,
				properties, state, notification, expireAt, assignedTo);

		username = "paco@gmail.com";
		password = "123123";

		name = "Incendio";
		description = "Incendio muy grande";
		location = "Salinas";
		tags = "asd:edf";
		state = "OPEN";
		notification = "yes";
		expireAt = "today";
		assignedTo = "x";

		incidenceData2 = new IncidenceData(username, password, name, description, location, tags, additionalInformation,
				properties, state, notification, expireAt, assignedTo);

	}

	@Test
	public void T1emptyIncidentName() {
		ResponseEntity<String> response = template.getForEntity(base.toString(), String.class);
		String incidenceURI = base.toString() + "/postIncident";
		String emptyName = "{\"reason\": \"Incidence name is required\"}";

		incidenceData1.setName("");
		incidenceData2.setName("");

		response = template.postForEntity(incidenceURI, incidenceData1, String.class);
		assertThat(response.getBody(), equalTo(emptyName));

		response = template.postForEntity(incidenceURI, incidenceData2, String.class);
		assertThat(response.getBody(), equalTo(emptyName));

	}

}
