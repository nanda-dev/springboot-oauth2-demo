package com.ng.oauth2.demo.resource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.apache.commons.codec.binary.Base64;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import com.ng.oauth2.demo.SpringOauth2DemoApplication;
import com.ng.oauth2.demo.config.OAuth2AuthorizationServer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(classes = {
		SpringOauth2DemoApplication.class, 
		OAuth2AuthorizationServer.class}, 
	webEnvironment = WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RestResourceTest {
	
	@Mock
	PasswordEncoder passwordEncoder;
	
	@InjectMocks
	RestResource restResource;
	
	@Autowired
    private MockMvc mockMvc;
	
	private String adminToken;
	private String userToken;
	
	@Before
	public void init() {
		try {
			adminToken = obtainAccessToken("admin", "pwd");
			userToken = obtainAccessToken("user", "pwd");
			log.info("token obtained: {}--{}", adminToken, userToken);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Test
	//@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void getAdminUnauthenticated() throws Exception {
		log.info("getAdminUnauthenticated starts");
		
		//mvc.perform(get("/api/employees"))
			      //.andExpect(status().isOk());
		ResultActions result
        = mockMvc.perform(get("/admin")
	        //.header("Authorization","Basic " + adminToken)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().is(401));
	}
	
	@Test
	//@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void getAdminAuthenticated() throws Exception {
		log.info("getAdminAuthenticated starts");
		
		//mvc.perform(get("/api/employees"))
			      //.andExpect(status().isOk());
		ResultActions result
        = mockMvc.perform(get("/admin")
	        .header("Authorization","Bearer " + adminToken)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk());
	}
	
	@Test
	//@Test(expected = AuthenticationCredentialsNotFoundException.class)
	public void getUserAuthenticated() throws Exception {
		log.info("getUserAuthenticated starts");
		
		//mvc.perform(get("/api/employees"))
			      //.andExpect(status().isOk());
		ResultActions result
        = mockMvc.perform(get("/api/user")
	        .header("Authorization","Bearer " + userToken)
	        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
        .andExpect(status().isOk());
	}
	
	private String obtainAccessToken(String username, String password) throws Exception {

    	//Using hard-coded values for now...
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);
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
