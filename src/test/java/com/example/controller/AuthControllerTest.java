package com.example.controller;

import com.example.Application;
import com.example.WebSecurityConfig;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.User;
import com.example.util.infrastructure.AuthenticatedExternalWebService;
import com.example.util.infrastructure.security.ExternalServiceAuthenticator;
import com.example.util.infrastructure.security.TokenService;
import com.example.util.locale.MessageByLocaleService;
import com.example.util.web.ApiController;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.security.authentication.BadCredentialsException;
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
import org.springframework.restdocs.payload.JsonFieldType;

/**
 * Created by abdul on 26/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class AuthControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets/auth");

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthControllerTest.class);

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private AuthController authController;

    @Autowired
    private ExternalServiceAuthenticator mockedExternalServiceAuthenticator;

    /**
     * Locale service.
     */
    @Autowired
    private MessageByLocaleService messageByLocaleService;

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
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
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
    public void invalidAdminCredentialsReturnsUnauthorized() {

        try {
            String password = "passw0rd";
            BDDMockito.when(mockedExternalServiceAuthenticator.authenticate(anyString(), anyString(), null))
                    .thenThrow(new BadCredentialsException("Invalid Credentials"));

            JsonObject request = Json.createObjectBuilder()
                    .add("username", username)
                    .add("password", password)
                    .build();

            this.mockMvc.perform(post(ApiController.AUTHENTICATE_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(request.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isUnauthorized());

        } catch (Exception e) {
            e.printStackTrace(System.err);
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
                .description(messageByLocaleService.getMessage("auth.token.description"))));

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
                fieldWithPath("username").attributes(Attributes.key("constraints")
                        .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                        .description(messageByLocaleService.getMessage("auth.username.description")),
                fieldWithPath("password").attributes(Attributes.key("constraints")
                        .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                        .description(messageByLocaleService.getMessage("auth.password.description")),
                fieldWithPath("domainName").attributes(Attributes.key("constraints")
                        .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                        .description(messageByLocaleService.getMessage("domain.aliasName.description"))};

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
                fieldWithPath("username").attributes(Attributes.key("constraints")
                        .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                        .description(messageByLocaleService.getMessage("auth.username.description")),
                fieldWithPath("password").attributes(Attributes.key("constraints")
                        .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                        .description(messageByLocaleService.getMessage("auth.password.description"))};

        return requestFields;

    }

    /**
     * This method is used to set the domain response fields for documentation.
     */
    private void setUserResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("username").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("firstName").description(messageByLocaleService.getMessage("user.first.name.description")),
                        fieldWithPath("lastName").description(messageByLocaleService.getMessage("user.last.name.description")),
                        fieldWithPath("emailId").description(messageByLocaleService.getMessage("user.emailId.description")),
                        fieldWithPath("domain.id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("domain.aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("domain.organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("domain.emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("domain.billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("domain.streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("domain.city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("domain.state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("domain.country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("domain.zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("domain.phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("domain.status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                        fieldWithPath("domain.createdBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("domain.updatedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("domain.deletedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("domain.createdDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("domain.lastModifiedDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("domain.deletedDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.deletedDateTime.description")),
                        fieldWithPath("domain.approvedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.approvedBy.description")),
                        fieldWithPath("domain.approvedDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("status").description(messageByLocaleService.getMessage("user.status.description")),
                        fieldWithPath("type").description(messageByLocaleService.getMessage("user.type.description")),
                        fieldWithPath("createdBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("updatedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("deletedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description"))

                )
        );
    }

    /**
     * To build user mock data.
     *
     * @return the user object.
     */
    public User buildMockUser() {
        return UserControllerTest.buildMockUser();
    }

    /**
     * Build mock data.
     * @return the domain object.
     */
    private Domain buildMockDomain() {
        return DomainControllerTest.buildMockDomain();
    }

}
