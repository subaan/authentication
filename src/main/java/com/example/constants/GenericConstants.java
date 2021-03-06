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

    /** AD attribute, used in search operation */
    public static final String DIRECTORY_ATTRIBUTE_OBJECT_CLASS = "objectClass";

    /** AD attribute, used in search operation and it should be unique.  It represent account logon name of the user */
    public static final String DIRECTORY_ATTRIBUTE_SAM_ACCOUNT_NAME = "sAMAccountName";

    /** AD attribute, used in search operation. It represent first name of the user */
    public static final String DIRECTORY_ATTRIBUTE_GIVEN_NAME = "givenName";

    /** AD attribute, used in search operation. It represent last name of the user */
    public static final String DIRECTORY_ATTRIBUTE_SUR_NAME = "sn";

    /** AD attribute, used in search operation. It represent account common name */
    public static final String DIRECTORY_ATTRIBUTE_COMMON_NAME = "cn";

    /** AD attribute, used in search operation. It represent user description */
    public static final String DIRECTORY_ATTRIBUTE_DESCRIPTION = "description";

    /** AD attribute, used in search operation. It represent user password */
    public static final String DIRECTORY_ATTRIBUTE_USER_PASSWORD = "userPassword";

    /** AD attribute, used in search operation. It represent user email ID */
    public static final String DIRECTORY_ATTRIBUTE_USER_PRINCIPAL_NAME = "userPrincipalName";

    /** AD attribute, used in search operation. It represent user role list */
    public static final String DIRECTORY_ATTRIBUTE_ROLE_NAMEs = "roleNames";

    /** Attribute objectClass value. */
    public static final String DIRECTORY_ATTRIBUTE_VALUE_USER = "user";

    /** Attribute objectClass value. */
    public static final String DIRECTORY_ATTRIBUTE_VALUE_PERSON = "person";
}
