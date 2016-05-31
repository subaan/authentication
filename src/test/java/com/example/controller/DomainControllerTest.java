package com.example.controller;

/**
 * Created by Abdul on 18/5/16.
 */
import com.example.Application;
import com.example.constants.GenericConstants;
import com.example.model.Domain;
import com.example.service.DomainService;
import com.example.util.infrastructure.security.TokenService;
import com.example.util.locale.MessageByLocaleService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
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

import org.mockito.BDDMockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DomainControllerTest {
    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets/domain");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private DomainService mockDomainService;

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
     * DomainControllerTestConfig for all object mocking.
     */
    @Configuration
    public static class DomainControllerTestConfig {

        /**
         * Service DomainService.
         *
         * @return mock
         */
        @Bean
        @Primary
        public DomainService domainServiceGateway() {
            return mock(DomainService.class);
        }
    }

    /**
     * Setup mocks before each test execution.
     */
    @Before
    public void setUp() {
        Mockito.reset(mockDomainService);
        this.document = document("{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.context)
                .apply(documentationConfiguration(this.restDocumentation))
                .alwaysDo(this.document)
                .build();
    }

    /**
     * Test the domain list.
     */
    @Test
    public void list() throws Exception {
        try {
            BDDMockito.when(mockDomainService.findAll())
                    .thenReturn(buildMockDomainList());

            //To set request header
            this.setRequestHeaders();
            //To set domain response fields
            this.setDomainListResponseFields();

            this.mockMvc.perform(
                    get("/api/domain")
                    .accept(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
            ).andExpect(status().isOk()).andDo(document("index"));;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * Test the domain list by page.
     */
    @Test
    public void listByPage() throws Exception {
        try {
            BDDMockito.when(mockDomainService.findAll(any()))
                    .thenReturn(buildMockDomainListPage());

            //To set request header
            this.setRequestHeaders();
            //To set domain response fields
            this.setDomainListResponseFields();

            this.mockMvc.perform(
                    get("/api/domain/list")
                    .accept(MediaType.APPLICATION_JSON)
                     .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .header("Range", "0-1").param("sortBy", "id")
            ).andExpect(status().isOk()).andDo(document("index"));

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }


    /**
     * Test the domain details by id.
     */
    @Test
    public void getDetail() {
        try {
            Long id = 1L;
            Domain domain = this.buildMockDomain();
            BDDMockito.when(mockDomainService.find(id))
                    .thenReturn(domain);

            //To set request header
            this.setRequestHeaders();
            //To set domain response fields
            this.setDomainResponseFields();

            this.mockMvc.perform(get("/api/domain/" + id)
                    .accept(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken()))
                    .andExpect(status().isOk()).andDo(document("index"));

        }  catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }
    }

    /**
     * Test the domain create.
     */
    @Test
    public void create() {
        try {
            Domain mockDomain = this.buildMockDomain();
            BDDMockito.when(mockDomainService.create(any(Domain.class)))
                    .thenReturn(mockDomain);

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

            //To set request header
            this.setRequestHeaders();
            //To set domain request fields
            this.setDomainRequestFields();

            this.mockMvc.perform(post("/api/domain")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestDomain.toString())
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
            Domain mockDomain = this.buildMockDomain();
            mockDomain.setUpdatedDate(new Date());
            BDDMockito.when(mockDomainService.update(any(Domain.class)))
                    .thenReturn(mockDomain);

            JsonObject requestDomain = Json.createObjectBuilder()
                    .add("aliasName", "domain-X")
                    .add("organisationName", "Organisation-X")
                    .add("emailId", "username-x@domainx.com")
                    .add("billingEmailId", "organisation-x@domainx.com")
                    .add("streetAddress", "No.4 example street")
                    .add("city", "X-City")
                    .add("state", "X-State")
                    .add("country", "X-country")
                    .add("zipCode", "546789")
                    .add("phoneNumber", "9879678546")
                    .add("status", "ACTIVE")
                    .build();

            //To set request header
            this.setRequestHeaders();
            //To set domain request fields
            this.setDomainRequestFields();

            this.mockMvc.perform(patch("/api/domain/"+mockDomain.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .header(GenericConstants.AUTHENTICATION_HEADER_TOKEN, tokenService.generateNewToken())
                    .content(requestDomain.toString())
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

            this.mockMvc.perform(delete("/api/domain/"+id)
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
     * This method is used to set the domain response fields for documentation.
     */
    private void setDomainListResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("[].id")
                            .description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("[].aliasName")
                            .description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("[].organisationName")
                            .description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("[].emailId")
                            .description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("[].billingEmailId")
                            .description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("[].streetAddress")
                            .description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("[].city")
                            .description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("[].state")
                            .description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("[].country")
                            .description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("[].zipCode")
                            .description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("[].phoneNumber")
                            .description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("[].status")
                            .description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("[].signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("[].updatedDate").type("Date")
                            .description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("[].approvedDate").type("Date")
                            .description(messageByLocaleService.getMessage("domain.approvedDate.description"))
                )
        );
    }

    /**
     * This method is used to set the domain request fields for documentation.
     */
    private void setDomainResponseFields () {

        this.document.snippets(
                responseFields(
                        fieldWithPath("id").description(messageByLocaleService.getMessage("common.id.description")),
                        fieldWithPath("aliasName").description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description")),
                        fieldWithPath("status").description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("signupDate").type("Date")
                                .description(messageByLocaleService.getMessage("domain.signupDate.description")),
                        fieldWithPath("updatedDate").type("Date").description(messageByLocaleService.getMessage("domain.updatedDate.description")),
                        fieldWithPath("approvedDate").type("Date").description(messageByLocaleService.getMessage("domain.approvedDate.description"))
                )
        );
    }

    /**
     * This method is used to set the domain request fields for documentation.
     */
    private void setDomainRequestFields () {

        this.document.snippets(
                requestFields(
                        fieldWithPath("aliasName").attributes(Attributes.key("constraints")
                                .value(messageByLocaleService.getMessage("common.constraints.not.null")))
                                .description(messageByLocaleService.getMessage("domain.aliasName.description")),
                        fieldWithPath("organisationName").description(messageByLocaleService.getMessage("domain.organisationName.description")),
                        fieldWithPath("emailId").description(messageByLocaleService.getMessage("domain.emailId.description")),
                        fieldWithPath("billingEmailId").description(messageByLocaleService.getMessage("domain.billingEmailId.description")),
                        fieldWithPath("streetAddress").description(messageByLocaleService.getMessage("domain.streetAddress.description")),
                        fieldWithPath("state").description(messageByLocaleService.getMessage("domain.state.description")),
                        fieldWithPath("city").description(messageByLocaleService.getMessage("domain.city.description")),
                        fieldWithPath("country").description(messageByLocaleService.getMessage("domain.country.description")),
                        fieldWithPath("zipCode").description(messageByLocaleService.getMessage("domain.zipCode.description")),
                        fieldWithPath("status").description(messageByLocaleService.getMessage("domain.status.description")),
                        fieldWithPath("phoneNumber").description(messageByLocaleService.getMessage("domain.phoneNumber.description"))
                )
        );
    }

    /**
     * Build mock data.
     * @return the domain object.
     */
    public Domain buildMockDomain() {

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

    /**
     * Build mock list of domain.
     * @return mock list of domain.
     */
    private List<Domain> buildMockDomainList() {
        List<Domain> domains = new ArrayList<Domain>();
        Domain domainX = this.buildMockDomain();
        domains.add(domainX);

        Domain domainY = new Domain();
        domainY.setId(2L);
        domainY.setAliasName("Domain-Y");
        domainY.setOrganisationName("Organisation-Y");
        domainY.setEmailId("username-x@domainY.com");
        domainY.setBillingEmailId("organisation-x@domainY.com");
        domainY.setStreetAddress("No.4 example street");
        domainY.setState("Y-State");
        domainY.setCity("Y-City");
        domainY.setCountry("Y-country");
        domainY.setZipCode("546889");
        domainY.setPhoneNumber("9879238546");
        domainY.setStatus(Domain.DomainStatus.APPROVAL_PENDING);
        domainY.setSignupDate(new Date());
        domains.add(domainY);

        return domains;
    }

    /**
     * Build mock pagination of domain.
     * @return the domain list page.
     */
    private Page<Domain> buildMockDomainListPage() {

        List domainList = this.buildMockDomainList();
        Page<Domain> pageDoamins = new PageImpl<Domain>(domainList);

        return pageDoamins;
    }

}
