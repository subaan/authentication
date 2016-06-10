package com.example.service;

import com.example.model.DirectoryConfig;
import com.example.model.User;
import com.example.repository.DirectoryConfigRepository;
import com.example.util.activedirectory.DirectoryUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.ContextMapper;
import org.springframework.ldap.core.DirContextAdapter;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.AndFilter;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.directory.Attributes;
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

    @Autowired
    private DirectoryConfigRepository directoryConfigRepository;

    @Override
    public Iterable<User> findAll(Long domainId) {
        List<User> search = ldapTemplate(domainId).search("", DirectoryUtil.getFilter("", ""), new UserContextMapper());
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
    public User findByUsername(String username, Long domainId) {
        User user = (User) ldapTemplate(domainId).searchForObject("", DirectoryUtil.
                getFilter(username, "username"), new UserContextMapper());
        LOGGER.info("LDAP user: {}",user.getFirstName());
        LOGGER.info("LDAP user: {}",user.getLastName());
        LOGGER.info("LDAP user: {}",user.getUsername());
        return user;
    }

    @Override
    public void create(User user, Long domainId) throws InvalidNameException, UnsupportedEncodingException {

        LOGGER.info("-------------- New user creation ------------");

        DirContextAdapter dirContextAdapter = new DirContextAdapter();

        //Create new user in Ad
        LdapName newUserDN = DirectoryUtil.userToDistinguishedName( user );
        Attributes userAttributes = DirectoryUtil.buildAttributes(user);
        ldapTemplate(domainId).bind(newUserDN, null, userAttributes);

        LOGGER.info("-------------- New user create success ------------");
    }

    @Override
    public void authenticate() throws InvalidNameException {

        String username = "balaji";
        AndFilter filter = new AndFilter();
        filter.and(new EqualsFilter("objectclass", "person")).and(
                new EqualsFilter("objectclass", "user")).and(
                new EqualsFilter("sAMAccountName", username));

//        LdapName ldapName = new LdapName("cn="+username);

        boolean value = ldapTemplate(1L).authenticate("", filter
                .toString(), "l3tm3in!@#123");

        LOGGER.info("authenticated: {}", value);
    }

    @Override
    public LdapTemplate ldapTemplate(Long domainId) {
        DirectoryConfig directoryConfig = directoryConfigRepository.findByDomainId(domainId);
        return DirectoryUtil.ldapTemplate(directoryConfig);
    }

    private static class UserContextMapper implements ContextMapper {
        public Object mapFromContext(Object ctx) {
            return DirectoryUtil.setAttributes(ctx);
        }
    }
}
