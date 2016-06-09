package com.example.util.activedirectory;

import com.example.model.User;
import org.springframework.ldap.core.DirContextAdapter;

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
        userAttributes.put( "objectclass", "person" );
        userAttributes.put( "objectclass", "user" );
        userAttributes.put( "givenName", user.getFirstName() );
        userAttributes.put( "sn", user.getLastName() );
        userAttributes.put( "sAMAccountName", user.getUsername() );
        // PASSWORD stuff
//        userAttributes.put("unicodepwd", this.encodePassword(user.getPassword()) );
        userAttributes.put("userPassword", DirectoryUtil.createUnicodePassword(user.getPassword()));

        userAttributes.put( "userPrincipalName", user.getEmailId() );
//        userAttributes.put( "userAccountControl", "512" ); //'512'- enabled account, 546- Disabled, Password Not Required
        userAttributes.put( "description", "Created via application" );


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
        user.setUsername(context.getStringAttribute("sAMAccountName"));
        user.setFirstName(context.getStringAttribute("givenName"));
        user.setLastName(context.getStringAttribute("sn"));
//            user.setFullName(context.getStringAttribute("cn"));
//            p.setDescription(context.getStringAttribute("description"));
//            p.setRoleNames(context.getStringAttributes("roleNames"));
        return user;
    }

    public static LdapName userToDistinguishedName( User user ) throws InvalidNameException {

        LdapName ldapName = new LdapName("cn="+user.getFirstName() + " " + user.getLastName());
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
