package com.example.util.locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Created by Sanjeev on 19/10/15.
 */
@Component
public class MessageByLocaleServiceImpl implements  MessageByLocaleService {

    @Autowired
    private MessageSource messageSource;


    @Override
    public String getMessage(String id) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id,null,locale);
    }

    @Override
    public String getMessage(String id, Object [] args) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(id, args, locale);
    }


    /**
     * Sets new messageSource.
     *
     * @param messageSource New value of messageSource.
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    /**
     * Gets messageSource.
     *
     * @return Value of messageSource.
     */
    public MessageSource getMessageSource() {
        return messageSource;
    }
}
