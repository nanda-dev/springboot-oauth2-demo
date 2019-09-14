package com.ng.oauth2.demo;

import static org.junit.Assert.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ng.oauth2.demo.config.OAuth2AuthorizationServer;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		SpringOauth2DemoApplication.class, 
		OAuth2AuthorizationServer.class}, 
	webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class SpringOauth2DemoApplicationTests {

	//OAuth2AuthorizationServer.class added to the @SpringBootTest(classes = {}) list helped resolve the Autowire error (couldn't find Bean)
	@Autowired private TokenStore tokenStore; 
    
    @Autowired private MockMvc mockMvc;	
		
	@Test
	public void contextLoads() {
	}	

    @Test
    public void whenTokenRetrievedAndValidated_thenSuccess() throws Exception {
        final String tokenValue = obtainAccessToken("user", "pwd");
        final OAuth2Authentication auth = tokenStore.readAuthentication(tokenValue);
        log.info("tokenValue={}", tokenValue);
        log.info("auth={}", auth);
        log.info("auth.isAuth={}", auth.isAuthenticated());
        assertTrue(auth.isAuthenticated());//no NPE now, weird!
        log.info("auth.details={}", auth.getDetails());//this shows 'null'...
        //System.out.println(tokenValue);
        //System.out.println(auth);
        //assertTrue(auth.isAuthenticated()); //Throws NPE at the moment...
        System.out.println(auth.getDetails());

		/*
		 * Map<String, Object> details = (Map<String, Object>) auth.getDetails();
		 * assertTrue(details.containsKey("organization"));
		 * System.out.println(details.get("organization"));
		 */
    }

    private String obtainAccessToken(String clientId, String username, String password) {
        final Map<String, String> params = new HashMap<String, String>();
        params.put("grant_type", "password");
        params.put("client_id", clientId);
        params.put("username", username);
        params.put("password", password);
        final Response response = RestAssured.given().auth().preemptive()
        		.basic(clientId, "secret")
        		.and().with()
        		.params(params)
        		.when()
        		//.post("http://localhost:8080/oauth/token");
        		.post("/oauth/token");
        return response.jsonPath().getString("access_token");
    }
    
    private String obtainAccessToken(String username, String password) throws Exception {

    	//Using hard-coded values for now...
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", "admin");
        params.add("password", "pwd");
        //params.add("scope", "openid");

        //String base64ClientCredentials = new String(Base64.encodeBase64("user:password".getBytes()));
        String base64ClientCredentials = new String(Base64.encodeBase64("client:secret".getBytes()));

        ResultActions result
              = mockMvc.perform(post("/oauth/token")
              .header("Authorization","Basic " + base64ClientCredentials)
              .accept("application/json;charset=UTF-8")
              .contentType(MediaType.APPLICATION_FORM_URLENCODED)
              .params(params))              
              .andExpect(status().isOk());

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
     }

}
