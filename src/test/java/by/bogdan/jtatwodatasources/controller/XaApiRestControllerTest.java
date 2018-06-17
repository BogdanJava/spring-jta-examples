package by.bogdan.jtatwodatasources.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

/**
 * @author Bogdan Shishkin
 * project: jta-two-datasources
 * date/time: 17/06/2018 / 17:47
 * email: bogdanshishkin1998@gmail.com
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class XaApiRestControllerTest {

    @LocalServerPort
    private Integer port;
    private String baseUrl;

    @Autowired
    private TestRestTemplate restTemplate;

    @Before
    public void init() {
        this.baseUrl = "http://localhost:" + port;
        restTemplate.delete(baseUrl);
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void shouldAddNewRecords() {
        Map<String, String> pet = new HashMap<>();
        pet.put("name", "Masya");
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl, pet, Void.class);
        assertThat(response.getStatusCode(), is(OK));

        ResponseEntity<Collection> petsResponse = restTemplate.getForEntity(baseUrl + "/pets", Collection.class);
        assertThat(petsResponse.getBody().size(), is(1));
        assertThat(petsResponse.getStatusCode(), is(OK));

        ResponseEntity<Collection> messagesResponse = restTemplate.getForEntity(baseUrl + "/messages", Collection.class);
        assertThat(messagesResponse.getBody().size(), is(1));
        assertThat(messagesResponse.getStatusCode(), is(OK));
    }

    @Test
    public void shouldRollback() {
        Map<String, String> pet = new HashMap<>();
        pet.put("name", "Donald");
        ResponseEntity<Void> response = restTemplate.postForEntity(baseUrl + "/?rollback=true", pet, Void.class);
        assertThat(response.getStatusCode(), is(INTERNAL_SERVER_ERROR));

        ResponseEntity<Collection> petsResponse = restTemplate.getForEntity(baseUrl + "/pets", Collection.class);
        assertThat(petsResponse.getBody().size(), is(0));
        assertThat(petsResponse.getStatusCode(), is(OK));

        ResponseEntity<Collection> messagesResponse = restTemplate.getForEntity(baseUrl + "/messages", Collection.class);
        assertThat(messagesResponse.getBody().size(), is(0));
        assertThat(messagesResponse.getStatusCode(), is(OK));
    }


}
