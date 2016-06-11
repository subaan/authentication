package com.example.util.activedirectory;

import com.example.constants.GenericConstants;
import com.example.model.DirectoryConfig;
import com.example.model.User;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import java.io.UnsupportedEncodingException;

/**
 * Created by Abdul on 9/6/16.
 */
public class DirectoryUtil {


    /**
     * This method is used to configure the LDAP for connection
     *
     * @param directoryConfig the directoryConfig object
     * @return the ldapTemplate
     */
    public static LdapTemplate ldapTemplate(DirectoryConfig directoryConfig) {

        return new LdapTemplate(contextSource(directoryConfig));
    }

    /**
     * This method is used to set ldap configuration detail.
     *
     * @param directoryConfig the directoryConfig object
     * @return the contextSource
     */
    private static LdapContextSource contextSource(DirectoryConfig directoryConfig) {
        LdapContextSource contextSource = new LdapContextSource();
        if(directoryConfig != null) {
            contextSource.setUrl(directoryConfig.getUrl());
            contextSource.setBase(directoryConfig.getBaseDN());
            contextSource.setUserDn(directoryConfig.getUserDN());
            contextSource.setPassword(directoryConfig.getPassword());
        } else {
            contextSource.setUrl("");
        }
        contextSource.afterPropertiesSet();
        return contextSource;
    }

    public static String getFilter(String searchValue, String searchBy) {

        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter(GenericConstants.DIRECTORY_ATTRIBUTE_OBJECT_CLASS,
                GenericConstants.DIRECTORY_ATTRIBUTE_VALUE_USER));
        filter.and(new EqualsFilter(GenericConstants.DIRECTORY_ATTRIBUTE_OBJECT_CLASS,
                GenericConstants.DIRECTORY_ATTRIBUTE_VALUE_PERSON));
//        Object ob = ldapTemplate.lookup("sAMAccountName=" + username);
//        User user = (User) ldapTemplate.lookup("cn=" + username, new UserContextMapper());
        if(searchValue != null && !searchValue.isEmpty()) {
            if(searchBy.equals("username")) {
                filter.and(new EqualsFilter(GenericConstants.DIRECTORY_ATTRIBUTE_SAM_ACCOUNT_NAME, searchValue));
            } else if(searchBy.equals("emailId")) {
                filter.and(new EqualsFilter(GenericConstants.DIRECTORY_ATTRIBUTE_USER_PRINCIPAL_NAME, searchValue));
            }
        }
        return filter.toString();
    }

    /**
     * This method is used to unicode the password for AD.
     *
     * @param password the password
     * @return the unicode password in byte.
     */
    public static byte[] createUnicodePassword(String password) {
        return toUnicodeBytes(doubleQuoteString(password));
    }

    /**
     * This method is used to unicode the password for AD.
     *
     * @param string the password string value
     * @return the unicode value
     */
    private static byte[] toUnicodeBytes(String string) {
        byte[] unicodeBytes = null;
        try {
            byte[] unicodeBytesWithQuotes = string.getBytes("Unicode");
            unicodeBytes = new byte[unicodeBytesWithQuotes.length - 2];
            System.arraycopy(
                    unicodeBytesWithQuotes,
                    2,
                    unicodeBytes,
                    0,
                    unicodeBytesWithQuotes.length - 2);
        } catch (UnsupportedEncodingException e) {
            // This should never happen.
            e.printStackTrace();
        }
        return unicodeBytes;
    }

    /**
     * This method is used to build user attribute values.
     * @param user the user object
     * @return the Attribute
     * @throws UnsupportedEncodingException throw unsupported encoding exception
     */
    public static Attributes buildAttributes(User user) throws UnsupportedEncodingException {
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_OBJECT_CLASS,
                GenericConstants.DIRECTORY_ATTRIBUTE_VALUE_PERSON);
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_OBJECT_CLASS,
                GenericConstants.DIRECTORY_ATTRIBUTE_VALUE_USER);
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_GIVEN_NAME, user.getFirstName() );
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_SUR_NAME, user.getLastName() );
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_SAM_ACCOUNT_NAME, user.getUsername() );
        // PASSWORD stuff
//        userAttributes.put("unicodepwd", this.encodePassword(user.getPassword()) );
//        userAttributes.put( "userAccountControl", "512" ); //'512'- enabled account, 546- Disabled, Password Not Required
        userAttributes.put(GenericConstants.DIRECTORY_ATTRIBUTE_USER_PASSWORD, DirectoryUtil.createUnicodePassword(user.getPassword()));
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_USER_PRINCIPAL_NAME, user.getEmailId() );
        userAttributes.put( GenericConstants.DIRECTORY_ATTRIBUTE_DESCRIPTION, "Created via application" );


        return userAttributes;
    }


    /**
     * This method is used to set the AD attribute values.
     * @param ctx
     * @return the user object
     */
    public static Object setAttributes(Object ctx) {

        DirContextAdapter context = (DirContextAdapter)ctx;
        User user = new User();
        user.setUsername(context.getStringAttribute(GenericConstants.DIRECTORY_ATTRIBUTE_SAM_ACCOUNT_NAME));
        user.setFirstName(context.getStringAttribute(GenericConstants.DIRECTORY_ATTRIBUTE_GIVEN_NAME));
        user.setLastName(context.getStringAttribute(GenericConstants.DIRECTORY_ATTRIBUTE_SUR_NAME));
//            user.setFullName(context.getStringAttribute("cn"));
//            p.setDescription(context.getStringAttribute("description"));
//            p.setRoleNames(context.getStringAttributes("roleNames"));
        return user;
    }

    public static LdapName userToDistinguishedName( User user ) throws InvalidNameException {

        LdapName ldapName = new LdapName(GenericConstants.DIRECTORY_ATTRIBUTE_COMMON_NAME +"="
                +user.getFirstName() + " " + user.getLastName());
        return ldapName;
    }

    /**
     * The password has converted into double quoted String.
     *
     * @param password the password
     * @return the password string
     */
    private static String doubleQuoteString(String password) {
        StringBuffer sb = new StringBuffer();
        sb.append("\"");
        sb.append(password);
        sb.append("\"");
        return sb.toString();
    }

    private byte[] encodePassword(String password) throws UnsupportedEncodingException {
        String newQuotedPassword = "" + password + "";

//        return newQuotedPassword.getBytes("UTF-16LE");
//        return DatatypeConverter.printBase64Binary(('"' + password + '"').getBytes("UTF-16LE"));
        return DirectoryUtil.createUnicodePassword(password);
    }


}
