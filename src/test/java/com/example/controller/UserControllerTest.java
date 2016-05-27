package com.example.controller;

import com.example.Application;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.infrastructure.security.TokenService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;

import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Abdul on 21/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class UserControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private UserService mockUserService;

    private DomainControllerTest domainControllerTest;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Autowired
    private TokenService tokenService;


    /**
     * UsersControllerTestConfig for all object mocking.
     */
    @Configuration
    public static class UsersControllerTestConfig {

        /**
         * Service mockUserService.
         *
         * @return mock
         */
        @Bean
        @Primary
        public UserService userServiceGateway() {
            return mock(UserService.class);
        }

        @Bean
        public DomainControllerTest domainControllerTest() {
            return mock(DomainControllerTest.class);
        }
    }

    /**
     * Setup mocks before each test execution.
     */
    @Before
    public void setUp() {
        Mockito.reset(mockUserService);
        this.document = document("user-{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
        this.domainControllerTest = context.getBean(DomainControllerTest.class);
    }

    /**
     * Test the user list.
     */
    @Test
    public void list() throws Exception {
        try {
            BDDMockito.when(mockUserService.findAll())
                    .thenReturn(buildMockUserList());

            //To set request header
            this.setRequestHeaders();
            //To set user response fields
            this.setUserListResponseFields();

            this.mockMvc.perform(
                get("/api/user")
                .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                .accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk()).andDo(document("index"));;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * Test the user list by page.
     */
    @Test
    public void listByPage() throws Exception {
        try {
            BDDMockito.when(mockUserService.findAll(any()))
                    .thenReturn(buildMockUserListPage());

            //To set request header
            this.setRequestHeaders();
            //To set user response fields
            this.setUserListResponseFields();

            this.mockMvc.perform(
                    get("/api/user/list").accept(MediaType.APPLICATION_JSON)
                            .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                            .header("Range", "0-1").param("sortBy", "id")
            ).andExpect(status().isOk()).andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * Test the user details by id.
     */
    @Test
    public void getDetail() {
        try {
            Long id = 1L;
            User user = this.buildMockUser();
            BDDMockito.when(mockUserService.find(id))
                    .thenReturn(user);

            //To set request header
            this.setRequestHeaders();
            //To set user response fields
            this.setUserResponseFields();

            this.mockMvc.perform(get("/api/user/" + id).accept(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isOk()).andDo(document("index"));

        }  catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }


    /**
     * Test the user create.
     */
    @Test
    public void create() {
        try {
            User mockUser = this.buildMockUser();
            BDDMockito.when(mockUserService.create(any(User.class)))
                    .thenReturn(mockUser);

            JsonObject requestUser = this.buildMockUserRequest();

            //To set request header
            this.setRequestHeaders();
            //To set user request fields
            this.setUserRequestFields();

            this.mockMvc.perform(post("/api/user")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestUser.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    .andDo(document("index"));


        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * Test the user update.
     */
    @Test
    public void update() {
        try {
            User mockUser = this.buildMockUser();
            mockUser.setUpdatedDate(new Date());
            BDDMockito.when(mockUserService.update(any(User.class)))
                    .thenReturn(mockUser);

            JsonObject requestUser = this.buildMockUserRequest();

            //To set request header
            this.setRequestHeaders();
            //To set user request fields
            this.setUserRequestFields();

            this.mockMvc.perform(patch("/api/user/"+mockUser.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestUser.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isAccepted())
                    .andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * Test delete user.
     */
    @Test
    public void remove() {
        try {
            Long id = 1L;

            //To set request header
            this.setRequestHeaders();

            this.mockMvc.perform(delete("/api/user/"+id)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isNoContent()).andDo(document("index"));

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
     * This method is used to set the domain response fields for documentation.
     */
    private void setUserListResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description("Unique identifier for the domain, auto generated, cannot be edited, or modified."),
                        fieldWithPath("[].username").description("The user name of the user. "),
                        fieldWithPath("[].emailId").description("The unique email Id for the user. e.g 'username@example.com' "),
                        fieldWithPath("[].domain.id").description("Unique identifier for the domain, auto generated, cannot be edited, or modified."),
                        fieldWithPath("[].domain.aliasName").description("Aliasname of the domain. e.g. 'Root' "),
                        fieldWithPath("[].domain.organisationName").description("The organisation name of the domain. e.g 'Apptronix' "),
                        fieldWithPath("[].domain.emailId").description("The unique email Id for the domain. e.g 'username@example.com' "),
                        fieldWithPath("[].domain.billingEmailId").description("The billing email Id of the domain. Billing related information and alerts received by this mail e.g 'username@organisation-name.com' "),
                        fieldWithPath("[].domain.streetAddress").description("The organisation street address in detail "),
                        fieldWithPath("[].domain.city").description("The organisation city"),
                        fieldWithPath("[].domain.state").description("The organisation state "),
                        fieldWithPath("[].domain.country").description("The organisation country "),
                        fieldWithPath("[].domain.zipCode").description("The organisation zip code. e.g. '435234' "),
                        fieldWithPath("[].domain.phoneNumber").description("The organisation phone number. e.g. '9789654567'"),
                        fieldWithPath("[].domain.status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                        fieldWithPath("[].domain.signupDate").type("Date")
                                .description("The sign up date. e.g. 'MAR 11 2016 12:35:05'"),
                        fieldWithPath("[].domain.updatedDate").type("Date").description("The domain updated date. e.g. 'MAR 15 2016 12:35:05'"),
                        fieldWithPath("[].domain.approvedDate").type("Date").description("The domain approved date. e.g. 'MAR 13 2016 12:35:05'"),
                        fieldWithPath("[].status").description("The status of the user. ENABLED - The user active status, DISABLED - The user disabled status."),
                        fieldWithPath("[].type").description("The type of the user"),
                        fieldWithPath("[].createdDate").type("Date").description("The user created date."),
                        fieldWithPath("[].updatedDate").type("Date").description("The user updated date.")

                )
        );
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
     * The user request fields.
     */
    private void setUserRequestFields() {

        this.document.snippets(
                requestFields(
                        fieldWithPath("username").description("The user name of the user. "),
                        fieldWithPath("emailId").description("The unique email Id for the user. e.g 'username@example.com' "),
                        fieldWithPath("password").description("The user password."),
                        fieldWithPath("domain.aliasName").description("Aliasname of the domain. e.g. 'Root' "),
                        fieldWithPath("domain.organisationName").description("The organisation name of the domain. e.g 'Appranix' "),
                        fieldWithPath("domain.emailId").description("The unique email Id for the domain. e.g 'username@example.com' "),
                        fieldWithPath("domain.billingEmailId").description("The billing email Id of the domain. Billing related information and alerts received by this mail e.g 'username@organisation-name.com' "),
                        fieldWithPath("domain.streetAddress").description("The organisation street address in detail "),
                        fieldWithPath("domain.city").description("The organisation city"),
                        fieldWithPath("domain.state").description("The organisation state "),
                        fieldWithPath("domain.country").description("The organisation country "),
                        fieldWithPath("domain.zipCode").description("The organisation zip code. e.g. '435234' "),
                        fieldWithPath("domain.status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                        fieldWithPath("domain.phoneNumber").description("The organisation phone number. e.g. '9789654567'"),
                        fieldWithPath("status").description("The status of the user. ENABLED - The user active status, DISABLED - The user disabled status."),
                        fieldWithPath("type").description("The type of the user")
        ));

    }

    /**
     * To build the domain mock request data.
     */
    private JsonObject buildMockDomainRequest() {

        JsonObject requestDomain = Json.createObjectBuilder()
                .add("aliasName", "domain-X")
                .add("organisationName", "Organisation-X")
                .add("emailId", "username-x@domainx.com")
                .add("billingEmailId", "organisation-x@domainx.com")
                .add("streetAddress", "No.4 example street")
                .add("state", "X-State")
                .add("city", "X-City")
                .add("country", "X-country")
                .add("zipCode", "546789")
                .add("status", "ACTIVE")
                .add("phoneNumber", "9879678546")
                .build();
        return requestDomain;

    }


    /**
     * To build the user mock request data.
     */
    private JsonObject buildMockUserRequest() {

        JsonObject requestUser = Json.createObjectBuilder()
                .add("username", "username-X")
                .add("password", "passw0rd")
                .add("emailId", "username-x@example.com")
                .add("domain", this.buildMockDomainRequest())
                .add("status", "ENABLED")
                .add("type", "DOMAIN_USER")
                .build();

        return requestUser;

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
     * Build mock list of user.
     * @return mock list of user.
     */
    private List<User> buildMockUserList() {

        List<User> users = new ArrayList<User>();
        User userX = this.buildMockUser();
        users.add(userX);

        User userY = new User();
        userY.setId(3L);
        userY.setUsername("username-Y");
        userY.setEmailId("username-Y@example.com");
        userY.setDomain(this.buildMockDomain());
        userY.setStatus(User.UserStatus.ENABLED);
        userY.setType(User.UserType.DOMAIN_USER);
        userY.setCreatedDate(new Date());
        users.add(userY);

        return users;
    }

    /**
     * Build mock pagination of domain.
     * @return the domain list page.
     */
    private Page<User> buildMockUserListPage() {

        List userList = this.buildMockUserList();
        Page<User> pageUsers = new PageImpl<User>(userList);

        return pageUsers;
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
