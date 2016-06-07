package com.example.service;

import com.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.DistinguishedName;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Abdul on 3/6/16.
 */
@Service
public class ActiveDirectoryServiceImpl implements ActiveDirectoryService {

    /** Logger constant. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ActiveDirectoryServiceImpl.class);

    private final LdapTemplate ldapTemplate;

    @Value("${ldap.contextSource.base}")
    private String BASE_DN;

    @Autowired
    public ActiveDirectoryServiceImpl(LdapTemplate ldapTemplate) { this.ldapTemplate = ldapTemplate; }

    @Override
    public Iterable<User> findAll() {
        List<User> search = ldapTemplate.search("", "(objectClass=person)", new UserContextMapper());
        for(int i=0; i< search.size(); i++) {

            LOGGER.info("-------------- search ------------");
            LOGGER.info("LDAP first name: {}",search.get(i).getFirstName());
            LOGGER.info("LDAP last name: {}",search.get(i).getLastName());
            LOGGER.info("LDAP username: {}",search.get(i).getUsername());
        }
        LOGGER.info("search : {}", search);
        return  search;
    }

    @Override
    public User findByUsername(String username) {

        Object ob =ldapTemplate.lookup("cn=" + username);
        LOGGER.info("ob:"+ob.toString());
        User user = (User) ldapTemplate.lookup("cn=" + username, new UserContextMapper());
        LOGGER.info("LDAP user: {}",user.getFirstName());
        LOGGER.info("LDAP user: {}",user.getLastName());
        LOGGER.info("LDAP user: {}",user.getUsername());
        return user;
    }

    private Attributes buildAttributes(User user) throws UnsupportedEncodingException {
        Attributes userAttributes = new BasicAttributes();
        userAttributes.put( "objectclass", "person" );
        userAttributes.put( "objectclass", "user" );
        userAttributes.put( "givenName", user.getFirstName() );
        userAttributes.put( "sn", user.getLastName() );
        userAttributes.put( "sAMAccountName", user.getUsername() );
        // PASSWORD stuff
        userAttributes.put("unicodepwd", this.encodePassword(user.getPassword()) );

        userAttributes.put( "userPrincipalName", user.getEmailId() );
        userAttributes.put( "userAccountControl", "512" );
        userAttributes.put( "description", "Created via application" );


        return userAttributes;
    }

    private byte[] encodePassword(String password) throws UnsupportedEncodingException {
        String newQuotedPassword = "" + password + "";
        return newQuotedPassword.getBytes("UTF-16LE");
    }

    private LdapName userToDistinguishedName( User user ) throws InvalidNameException {

        LdapName ldapName = new LdapName("cn=user_"+user.getId());
        ldapName.add("ou=kaveri");
        return ldapName;
    }



    private static class UserContextMapper implements ContextMapper {
        public Object mapFromContext(Object ctx) {
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
    }
}
