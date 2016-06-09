package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import com.example.util.activedirectory.DirectoryUtil;
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
import javax.xml.bind.DatatypeConverter;

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

    @Override
    public void create(User user) throws InvalidNameException, UnsupportedEncodingException {

        LOGGER.info("-------------- New user creation ------------");

        DirContextAdapter dirContextAdapter = new DirContextAdapter();

        //Create new user in Ad
        LdapName newUserDN = DirectoryUtil.userToDistinguishedName( user );
        Attributes userAttributes = DirectoryUtil.buildAttributes(user);
        ldapTemplate.bind(newUserDN, null, userAttributes);

        LOGGER.info("-------------- New user create success ------------");
    }

    private static class UserContextMapper implements ContextMapper {
        public Object mapFromContext(Object ctx) {
            return DirectoryUtil.setAttributes(ctx);
        }
    }
}
