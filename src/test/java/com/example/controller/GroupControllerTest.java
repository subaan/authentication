package com.example.controller;

import com.example.Application;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.model.Group;
import com.example.model.User;
import com.example.service.GroupService;
import com.example.util.infrastructure.security.TokenService;
import com.example.util.locale.MessageByLocaleService;
import com.example.vo.GroupVO;
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
import org.springframework.restdocs.snippet.Attributes;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by Abdul on 30/5/16.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class GroupControllerTest {

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets/group");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private GroupService mockGroupService;

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
     * GroupControllerTestConfig for all object mocking.
     */
    @Configuration
    public static class GroupControllerTestConfig {

        /**
         * Service GroupService.
         *
         * @return mock
         */
        @Bean
        @Primary
        public GroupService groupServiceGateway() {
            return mock(GroupService.class);
        }

    }

    /**
     * Setup mocks before each test execution.
     */
    @Before
    public void setUp() {
//        Mockito.reset(mockGroupService);
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }


    /**
     * Test the group list.
     */
//    @Test
    public void list() throws Exception {
        try {
            BDDMockito.when(mockGroupService.findAll())
                    .thenReturn(buildMockGroupList());

            //To set request header
            this.setRequestHeaders();
            //To set domain response fields
            this.setGroupListResponseFields();

            this.mockMvc.perform(
                    get("/api/group")
                    .accept(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
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
            BDDMockito.when(mockGroupService.findAll(any()))
                    .thenReturn(buildMockGroupListPage());

            //To set request header
            this.setRequestHeaders();
            //To set user response fields
            this.setGroupListResponseFields();

            this.mockMvc.perform(
                get("/api/group/list").accept(MediaType.APPLICATION_JSON)
                .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                .header("Range", "0-10").param("sortBy", "id")
            ).andExpect(status().isOk()).andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * Test the group create.
     */
    @Test
    public void create() {
        try {
            User mockUser = this.buildMockUser();
            BDDMockito.when(mockGroupService.create(any(Group.class)))
                    .thenReturn(buildMockGroup());

            JsonObject requestGroup = this.buildMockGroupRequest();

            //To set request header and fields
            this.setRequestHeaders();
            this.setGroupRequestFields();

            //To set group response fields
            this.setGroupResponseFields();

            this.mockMvc.perform(post("/api/group/create")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestGroup.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isCreated())
                    .andDo(document("index"));


        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * Test the domain update.
     */
    @Test
    public void update() {
        try {
            Group mockGroup = this.buildMockGroup();
            mockGroup.setLastModifiedDateTime(new Date());
            BDDMockito.when(mockGroupService.update(any(Group.class)))
                    .thenReturn(mockGroup);

            JsonObject requestGroup = this.buildMockGroupRequest();

            //To set request header
            this.setRequestHeaders();
            //To set group request fields
            this.setGroupRequestFields();

            //To set group response fields
            this.setGroupResponseFields();

            this.mockMvc.perform(patch("/api/group/update/"+mockGroup.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestGroup.toString())
                    .accept(MediaType.APPLICATION_JSON_VALUE))
                    .andExpect(status().isAccepted())
                    .andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * Test delete domain.
     */
    @Test
    public void remove() {
        try {
            Long id = 1L;

            //To set request header
            this.setRequestHeaders();

            this.mockMvc.perform(delete("/api/group/delete/"+id)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isNoContent()).andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * This method is used to build group mock request.
     */
    private JsonObject buildMockGroupRequest() {

        JsonArrayBuilder users = Json.createArrayBuilder().add(1).add(2);
        JsonObject requestGroups = Json.createObjectBuilder()
                .add("name", "group-X")
                .add("domainId", 1)
                .add("users", users)
                .build();

        return requestGroups;

    }

    /**
     * This method is used to set request headers.
     */
    private void setRequestHeaders() {

        this.document.snippets(requestHeaders(headerWithName(GenericConstants.AUTHENTICATION_HEADER_TOKEN)
                .description(messageByLocaleService.getMessage("auth.token.description"))));

    }

    /**
     * This method is used to set the group request fields for documentation.
     */
    private void setGroupRequestFields () {

        this.document.snippets(
                requestFields(
                        fieldWithPath("name").attributes(Attributes.key("constraints").value(messageByLocaleService.getMessage("common.constraints.not.null"))).description("Name of the Group. e.g. 'Admin' "),
                        fieldWithPath("domainId").description("The domain ID"),
                        fieldWithPath("users").description("The user ids in array list. e.g. '[1, 2]'")
                )
        );
    }

    /**
     * This method is used to set the group response fields for documentation.
     */
    private void setGroupListResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].name").description(messageByLocaleService.getMessage("group.name.description")),
                        fieldWithPath("[].users.[].id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].users.[].username").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("[].users.[].emailId").description(messageByLocaleService.getMessage("user.emailId.description")),
                        fieldWithPath("[].users.[].domain.id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].users.[].domain.aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("[].users.[].domain.organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("[].users.[].domain.emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("[].users.[].domain.billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("[].users.[].domain.streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("[].users.[].domain.city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("[].users.[].domain.state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("[].users.[].domain.country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("[].users.[].domain.zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("[].users.[].domain.phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("[].users.[].domain.status").description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("[].users.[].domain.signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("[].users.[].domain.updatedDate").type("Date").description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("[].users.[].domain.approvedDate").type("Date").description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("[].users.[].status").description(messageByLocaleService.getMessage("user.status.description")),
                        fieldWithPath("[].users.[].type").description(messageByLocaleService.getMessage("user.type.description")),
                        fieldWithPath("[].users.[].createdBy").description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("[].users.[].updatedBy").description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("[].users.[].deletedBy").description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("[].users.[].createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("[].users.[].lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("[].users.[].deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description")),
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
                        fieldWithPath("[].domain.signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("[].domain.updatedDate").type("Date").description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("[].domain.approvedDate").type("Date").description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("[].deleted").description(messageByLocaleService.getMessage("common.deleted.description")),
                        fieldWithPath("[].createdBy").description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("[].updatedBy").description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("[].deletedBy").description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("[].createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("[].lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("[].deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description"))

                )
        );
    }

    /**
     * This method is used to set the group response fields for documentation.
     */
    private void setGroupResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description("Unique identifier for the group, auto generated, cannot be edited, or modified."),
                        fieldWithPath("name").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("users.[].id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("users.[].username").description(messageByLocaleService.getMessage("user.username.description")),
                        fieldWithPath("users.[].emailId").description(messageByLocaleService.getMessage("user.emailId.description")),
                        fieldWithPath("users.[].domain.id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("users.[].domain.aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("users.[].domain.organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("users.[].domain.emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("users.[].domain.billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("users.[].domain.streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("users.[].domain.city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("users.[].domain.state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("users.[].domain.country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("users.[].domain.zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("users.[].domain.phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("users.[].domain.status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                        fieldWithPath("users.[].domain.signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("users.[].domain.updatedDate").type("Date").description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("users.[].domain.approvedDate").type("Date").description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("users.[].status").description(messageByLocaleService.getMessage("user.status.description")),
                        fieldWithPath("users.[].type").description(messageByLocaleService.getMessage("user.type.description")),
                        fieldWithPath("users.[].createdBy").description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("users.[].updatedBy").description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("users.[].deletedBy").description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("users.[].createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("users.[].lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("users.[].deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description")),
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
                        fieldWithPath("domain.signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("domain.updatedDate").type("Date").description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("domain.approvedDate").type("Date").description(messageByLocaleService.getMessage("domain.approvedDate.description")),
                        fieldWithPath("deleted").description("The soft delete value in boolean"),
                        fieldWithPath("createdBy").description(messageByLocaleService.getMessage("audit.createdBy.description")),
                        fieldWithPath("updatedBy").description(messageByLocaleService.getMessage("audit.updatedBy.description")),
                        fieldWithPath("deletedBy").description(messageByLocaleService.getMessage("audit.deletedBy.description")),
                        fieldWithPath("createdDateTime").type("Date").description(messageByLocaleService.getMessage("audit.createdDateTime.description")),
                        fieldWithPath("lastModifiedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.lastModifiedDateTime.description")),
                        fieldWithPath("deletedDateTime").type("Date").description(messageByLocaleService.getMessage("audit.deletedDateTime.description"))

                )
        );
    }

    /**
     * Build mock group.
     * @return mock group.
     */
    private Group buildMockGroup() {

        Group groupX = new Group();
        groupX.setId(1L);
        groupX.setName("GroupX");
        groupX.setDomain(this.buildMockDomain());
        groupX.setUsers(this.buildMockUserList());
        groupX.setDeleted(false);
        groupX.setCreatedDateTime(new Date());

        return groupX;

    }

    /**
     * Build mock list of group.
     * @return mock list of group.
     */
    private List<Group> buildMockGroupList() {

        List<Group> groups = new ArrayList<Group>();
        Group groupX = this.buildMockGroup();
        groups.add(groupX);
        return groups;

    }

    /**
     * Build mock pagination of group.
     * @return the group list page.
     */
    private Page<Group> buildMockGroupListPage() {

        List groupList = this.buildMockGroupList();
        Page<Group> pageUsers = new PageImpl<Group>(groupList);

        return pageUsers;
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
        userY.setCreatedBy(1L);
        users.add(userY);

        return users;
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
