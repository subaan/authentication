package com.example.controller;

import com.example.Application;
import com.example.WebSecurityConfig;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.User;
import com.example.util.infrastructure.AuthenticatedExternalWebService;
import com.example.util.infrastructure.security.ExternalServiceAuthenticator;
import com.example.util.infrastructure.security.TokenService;
import com.example.util.web.ApiController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.json.Json;
import javax.json.JsonObject;

import java.util.Date;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;

/**
 * Created by abdul on 26/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AuthControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthController authController;

    @Autowired
    ExternalServiceAuthenticator mockedExternalServiceAuthenticator;

    /** Admin username. */
    @Value("${backend.admin.username}")
    private String username;

    /** Admin password. */
    @Value("${backend.admin.password}")
    private String password;

    /**
     * AuthenticationControllerTestConfig for all object mocking.
     */
    @Configuration
    public static class AuthControllerTestConfig {

        /**
         * Authenticator someExternalServiceAuthenticator.
         *
         * @return mock
         */
        @Bean
        public ExternalServiceAuthenticator someExternalServiceAuthenticator() {
            return mock(ExternalServiceAuthenticator.class);
        }

        /**
         * Token manager.
         *
         * @return mock
         */
        @Bean
        public AuthController authController() {
            return mock(AuthController.class);
        }
    }

    /**
     * Setup mocks before each test execution.
     */
    @Before
    public void setUp() {
//        Mockito.reset(mockedExternalServiceAuthenticator);
        this.document = document("auth-{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }

    @Test
    public void loginByAdmin() {

        try {

            AuthenticatedExternalWebService authenticationWithToken = new AuthenticatedExternalWebService(username, null,
                    AuthorityUtils.commaSeparatedStringToAuthorityList("ROLE_DOMAIN_USER"));

//            BDDMockito.when(status().isOk()).
//                    thenReturn(header().string(GenericConstants.AUTHENTICATION_HEADER_TOKEN, token));

            JsonObject request = Json.createObjectBuilder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            System.out.println("request :  "+request);
            //To set request fields
            this.setAdminRequestFields();

            this.mockMvc.perform(post(ApiController.AUTHENTICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk()).andDo(document("index"))
                    .andReturn().getResponse().setHeader(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken());

//                    .andDo(document("headers", responseHeaders(headerWithName(GenericConstants.AUTHENTICATION_HEADER_TOKEN)
//                            .description("Authentication token"))));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    @Test
    public void loginByUser() {

        try {

            String domainName = "Root";
            JsonObject request = Json.createObjectBuilder()
                    .add("username", username)
                    .add("password", password)
                    .add("domainName", domainName)
                    .build();

            System.out.println("request :  " + request);
            //To set request fields
            this.setUserRequestFields();

            this.mockMvc.perform(post(ApiController.AUTHENTICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isOk()).andDo(document("index"))
                    .andReturn().getResponse().setHeader(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken());

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    @Test
    public void whoAmI() {

        try {

            BDDMockito.when(authController.getCurrentUser(any())).thenReturn(this.buildMockUser());

            //To set request header and response fields
            this.setRequestHeaders();
            this.setUserResponseFields();

            this.mockMvc.perform(get("/api/auth/whoami")
                    .accept(MediaType.APPLICATION_JSON_VALUE)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isOk()).andDo(document("index"));


        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    @Test
    public void logout() {

        try {

            //To set request header fields
            this.setRequestHeaders();

            this.mockMvc.perform(post(ApiController.LOGOUT_URL)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isOk()).andDo(document("index"));


        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * This method is used to set request headers.
     */
    private void setRequestHeaders() {

        this.document.snippets(requestHeaders(headerWithName(GenericConstants.AUTHENTICATION_HEADER_TOKEN)
                .description("The authentication unique token.")));

    }

    /**
     * This method is used to set the auth request fields for documentation.
     */
    private void setAdminRequestFields() {

        this.document.snippets(
                requestFields(
                        this.setCommonRequestFields()
                )
        );

    }

    /**
     * This method is used to set the auth request fields for documentation.
     */
    private void setUserRequestFields() {

        FieldDescriptor[] requestFields = new FieldDescriptor[] {
                fieldWithPath("username").attributes(Attributes.key("constraints").value("Must not be null. Must not be empty"))
                        .description("The username of the user. e.g. 'abdul' "),
                fieldWithPath("password").attributes(Attributes.key("constraints").value("Must not be null. Must not be empty"))
                        .description("The user credential"),
                fieldWithPath("domainName").attributes(Attributes.key("constraints").value("Must not be null. Must not be empty"))
                        .description("The domain name. e.g. 'Root' ")};

        this.document.snippets(
                requestFields(
                        requestFields
                )
        );

    }

    /**
     * This method set common request field details.
     */
    private FieldDescriptor[] setCommonRequestFields() {

        FieldDescriptor[] requestFields = new FieldDescriptor[] {
                fieldWithPath("username").attributes(Attributes.key("constraints").value("Must not be null. Must not be empty"))
                        .description("The username of the user. e.g. 'abdul' "),
                fieldWithPath("password").attributes(Attributes.key("constraints").value("Must not be null. Must not be empty"))
                        .description("The user credential")};

        return requestFields;

    }

    /**
     * This method is used to set the domain response fields for documentation.
     */
    private void setUserResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Unique identifier for the domain, auto generated, cannot be edited, or modified."),
                        fieldWithPath("username").description("The user name of the user. "),
                        fieldWithPath("emailId").description("The unique email Id for the user. e.g 'username@example.com' "),
                        fieldWithPath("domain.id").description("Unique identifier for the domain, auto generated, cannot be edited, or modified."),
                        fieldWithPath("domain.aliasName").description("Aliasname of the domain. e.g. 'Root' "),
                        fieldWithPath("domain.organisationName").description("The organisation name of the domain. e.g 'Apptronix' "),
                        fieldWithPath("domain.emailId").description("The unique email Id for the domain. e.g 'username@example.com' "),
                        fieldWithPath("domain.billingEmailId").description("The billing email Id of the domain. Billing related information and alerts received by this mail e.g 'username@organisation-name.com' "),
                        fieldWithPath("domain.streetAddress").description("The organisation street address in detail "),
                        fieldWithPath("domain.city").description("The organisation city"),
                        fieldWithPath("domain.state").description("The organisation state "),
                        fieldWithPath("domain.country").description("The organisation country "),
                        fieldWithPath("domain.zipCode").description("The organisation zip code. e.g. '435234' "),
                        fieldWithPath("domain.phoneNumber").description("The organisation phone number. e.g. '9789654567'"),
                        fieldWithPath("domain.status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                        fieldWithPath("domain.signupDate").type("Date")
                                .description("The sign up date. e.g. 'MAR 11 2016 12:35:05'"),
                        fieldWithPath("domain.updatedDate").type("Date").description("The domain updated date. e.g. 'MAR 15 2016 12:35:05'"),
                        fieldWithPath("domain.approvedDate").type("Date").description("The domain approved date. e.g. 'MAR 13 2016 12:35:05'"),
                        fieldWithPath("status").description("The status of the user. ENABLED - The user active status, DISABLED - The user disabled status."),
                        fieldWithPath("type").description("The type of the user"),
                        fieldWithPath("createdDate").type("Date").description("The user created date."),
                        fieldWithPath("updatedDate").type("Date").description("The user updated date.")

                )
        );
    }

    /**
     * To build user mock data.
     *
     * @return the user object.
     */
    public User buildMockUser() {

        User userX = new User();
        userX.setId(1L);
        userX.setUsername("username-x");
        userX.setEmailId("username-x@example.com");
        userX.setDomain(this.buildMockDomain());
        userX.setStatus(User.UserStatus.ENABLED);
        userX.setType(User.UserType.DOMAIN_ADMIN);
        userX.setCreatedDate(new Date());

        return userX;
    }

    /**
     * Build mock data.
     * @return the domain object.
     */
    private Domain buildMockDomain() {

        Domain domain = new Domain();
        domain.setId(1L);
        domain.setAliasName("Domain-X");
        domain.setOrganisationName("Organisation-X");
        domain.setEmailId("username-x@domainx.com");
        domain.setBillingEmailId("organisation-x@domainx.com");
        domain.setStreetAddress("No.4 example street");
        domain.setState("X-State");
        domain.setCity("X-City");
        domain.setCountry("X-country");
        domain.setZipCode("546789");
        domain.setPhoneNumber("9879678546");
        domain.setStatus(Domain.DomainStatus.ACTIVE);
        domain.setSignupDate(new Date());

        return domain;
    }

}
