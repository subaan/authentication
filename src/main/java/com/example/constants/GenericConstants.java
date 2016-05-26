package com.example.constants;

/**
 * All the common constants for the application will go here.
 *
 */
public class GenericConstants {

    /**
     * Makes sure that utility classes (classes that contain only static methods or fields in their API)
     * do not have a public constructor.
     */
    protected GenericConstants() {
        throw new UnsupportedOperationException();
    }

    /** Constant used to strip range value from UI for pagination. */
    public static final String RANGE_PREFIX = "items=";

    /** Constant used to set content range response for pagination. */
    public static final String CONTENT_RANGE_HEADER = "Content-Range";

    /** Constant used to set content type header. */
    public static final String CONTENT_TYPE_HEADER = "Content-Type";

    /** Constant used to set as a content type header. */
    public static final String CONTENT_TYPE_JSON = "application/json";

    /** Constant used as header in authentication */
    public static final String AUTHENTICATION_HEADER_USERNAME = "X-Auth-Username";

    /** Constant used as header in authentication */
    public static final String AUTHENTICATION_HEADER_PASSWORD = "X-Auth-Password";

    /** onstant used as authentication header */
    public static final String AUTHENTICATION_HEADER_TOKEN = "X-Auth-Token";

    /** Constant used as form data as in authentication. */
    public static final String DOMAIN_NAME = "domainName";

    /** Constant used as form data as in authentication. */
    public static final String USERNAME = "username";

    /** Constant used as form data as in authentication. */
    public static final String PASSWORD = "password";
}
