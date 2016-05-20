package com.example.controller;

/**
 * Created by Abdul on 18/5/16.
 */
import com.example.Application;
import com.example.model.Domain;
import com.example.model.Person;
import com.example.repository.PersonRepository;
import com.example.service.DomainService;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.mockito.BDDMockito;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Matchers.any;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class DomainControllerTest {
    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("build/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private DomainService mockDomainService;

    @Autowired
    private ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;


    /**
     * DomainControllerTestConfig for all object mocking.
     */
    @Configuration
    public static class CloudControllerTestConfig {

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
        this.document = document("domain-{method-name}", preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
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

            this.document.snippets(
                    responseFields(
                            fieldWithPath("[].id").description("Unique identifier for the domain, auto generated, cannot be edited, or modified."),
                            fieldWithPath("[].aliasName").description("Aliasname of the domain. e.g. 'Root' "),
                            fieldWithPath("[].organisationName").description("The organisation name of the domain. e.g 'Apptronix' "),
                            fieldWithPath("[].emailId").description("The unique email Id for the domain. e.g 'username@example.com' "),
                            fieldWithPath("[].billingEmailId").description("The billing email Id of the domain. Billing related information and alerts received by this mail e.g 'username@organisation-name.com' "),
                            fieldWithPath("[].streetAddress").description("The organisation street address in detail "),
                            fieldWithPath("[].city").description("The organisation city"),
                            fieldWithPath("[].state").description("The organisation state "),
                            fieldWithPath("[].country").description("The organisation country "),
                            fieldWithPath("[].zipCode").description("The organisation zip code. e.g. '435234' "),
                            fieldWithPath("[].phoneNumber").description("The organisation phone number. e.g. '9789654567'"),
                            fieldWithPath("[].status").description("The status of domain. APPROVAL_PENDING - Initial state when done signup, ACTIVE - The domain active state," +
                                    " SUSPENDED - The domain suspended state, CLOSED - The domain dead state.'"),
                            fieldWithPath("[].signupDate").type("Date")
                                    .description("The sign up date. e.g. 'MAR 11 2016 12:35:05'"),
                            fieldWithPath("[].updatedDate").type("Date").description("The domain updated date. e.g. 'MAR 15 2016 12:35:05'"),
                            fieldWithPath("[].approvedDate").type("Date").description("The domain approved date. e.g. 'MAR 13 2016 12:35:05'")
                    )
            );

            this.mockMvc.perform(
                    get("/domain").accept(MediaType.APPLICATION_JSON)
            ).andExpect(status().isOk()).andDo(document("index"));;

        } catch (Exception e) {
            e.printStackTrace(System.err);
            Assert.fail("Unexpected Exception");
        }

    }

    /**
     * Build mock list of domain.
     * @return mock list of domain.
     */
    private List<Domain> buildMockDomainList() {
        List<Domain> domains = new ArrayList<Domain>();
        Domain domainX = new Domain();
        domainX.setId(1L);
        domainX.setAliasName("Domain-X");
        domainX.setOrganisationName("Organisation-X");
        domainX.setEmailId("username-x@domainx.com");
        domainX.setBillingEmailId("organisation-x@domainx.com");
        domainX.setStreetAddress("No.4 example street");
        domainX.setState("X-State");
        domainX.setCity("X-City");
        domainX.setCountry("X-Contry");
        domainX.setZipCode("546789");
        domainX.setPhoneNumber("9879678546");
        domainX.setStatus(Domain.DomainStatus.ACTIVE);
        domainX.setSignupDate(new Date());
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
        domainY.setCountry("Y-Contry");
        domainY.setZipCode("546889");
        domainY.setPhoneNumber("9879238546");
        domainY.setStatus(Domain.DomainStatus.APPROVAL_PENDING);
        domainY.setSignupDate(new Date());
        domains.add(domainY);

//        Page<Domain> page = new PageImpl<Domain>(domains);
        return domains;
    }

}
