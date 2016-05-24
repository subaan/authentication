package com.example.util.web;

/**
 * Generic controller for reusing constants, defining path etc..
 *
 */
public interface ApiController {

    //Generic constants
    /** Create.*/
    String PATH_ID = "id";

    //Swagger constants
    /** Create.*/
    String SW_METHOD_CREATE = "create";

    /** Read.*/
    String SW_METHOD_READ = "read";

    /** Update.*/
    String SW_METHOD_UPDATE = "update";

    /** Delete.*/
    String SW_METHOD_DELETE = "delete";


    //Paging constants
    /** Create.*/
    String RANGE = "Range";

    //Application paths

    /** API path. */
    String API_PATH = "/api";

    /** Authentication URL path. */
    String AUTHENTICATE_URL = "/authenticate";

    /** Stuff URL path. */
    String STUFF_URL = API_PATH + "/stuff";

    // Spring Boot Actuator services path. username and password required in header .
    /** Auto config endpoint.*/
    String DOMAIN_ENDPOINT = "/autoconfig";
    /** Beans endpoint. */
    String USER_ENDPOINT = "/beans";
    /** Config groups endpoint. */
    String CONFIGPROPS_ENDPOINT = "/configprops";
    /**Environment endpoint. */
    String ENV_ENDPOINT = "/env";
    /** Mappings endpoint. */
    String MAPPINGS_ENDPOINT = "/mappings";
    /** Metrics endpoint. */
    String METRICS_ENDPOINT = "/metrics";
    /** Shutdown endpoint. */
    String SHUTDOWN_ENDPOINT = "/shutdown";

}
