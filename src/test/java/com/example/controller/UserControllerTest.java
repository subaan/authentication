package com.example.controller;

import com.example.Application;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.User;
import com.example.service.UserService;
import com.example.util.infrastructure.security.TokenService;
import com.example.util.locale.MessageByLocaleService;
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
import org.springframework.restdocs.payload.JsonFieldType;
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
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets/user");

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
     * Locale service.
     */
    @Autowired
    private MessageByLocaleService messageByLocaleService;


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
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
        this.domainControllerTest = context.getBean(DomainControllerTest.class);
    }

    /**
     * Test the user list.
     */
//    @Test
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
            mockUser.setLastModifiedDateTime(new Date());
            mockUser.setUpdatedBy(1L);
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
                .description(messageByLocaleService.getMessage("auth.token.description"))));

    }


    /**
     * This method is used to set the user response fields for documentation.
     */
    private void setUserListResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].username").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("[].emailId").description(messageByLocaleService.getMessage("user.emailId.description")),
                        fieldWithPath("[].domain.id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].domain.aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("[].domain.organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("[].domain.emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("[].domain.billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("[].domain.streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("[].domain.city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("[].domain.state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("[].domain.country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("[].domain.zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("[].domain.phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("[].domain.status").description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("[].domain.createdBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("[].domain.updatedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("[].domain.deletedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("[].domain.createdDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("[].domain.lastModifiedDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("[].domain.deletedDateTime").type("Date")
                                .description(messageByLocaleService.getMessage("audit.deletedDateTime.description")),
                        fieldWithPath("[].domain.approvedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.approvedBy.description")),
                        fieldWithPath("[].domain.approvedDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("[].status").description(messageByLocaleService.getMessage("user.status.description")),
                        fieldWithPath("[].type").description(messageByLocaleService.getMessage("user.type.description")),
                        fieldWithPath("[].createdBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("[].updatedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("[].deletedBy").type(JsonFieldType.NUMBER)
                                .description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("[].createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("[].lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("[].deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description"))

                )
        );
    }

    /**
     * This method is used to set the user response fields for documentation.
     */
    private void setUserResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("username").description(messageByLocaleService.getMessage("user.username.description")),
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
                        fieldWithPath("domain.status").description(messageByLocaleService.getMessage("domain.status.description")),
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
     * The user request fields.
     */
    private void setUserRequestFields() {

        this.document.snippets(
                requestFields(
                        fieldWithPath("username").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("emailId").description(messageByLocaleService.getMessage("user.emailId.description")),
                        fieldWithPath("password").description("The user password."),
                        fieldWithPath("domain.aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("domain.organisationName").description("The organisation name of the domain. e.g 'Appranix' "),
                        fieldWithPath("domain.emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("domain.billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("domain.streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("domain.city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("domain.state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("domain.country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("domain.zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("domain.status").description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("domain.phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("status").description(messageByLocaleService.getMessage("user.status.description")),
                        fieldWithPath("type").description(messageByLocaleService.getMessage("user.type.description"))
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
        userX.setCreatedDateTime(new Date());
        userX.setCreatedBy(1L);

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
        userY.setCreatedDateTime(new Date());
        userX.setCreatedBy(1L);
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
        domain.setCreatedDateTime(new Date());

        return domain;
    }

}
