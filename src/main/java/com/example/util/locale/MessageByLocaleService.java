package com.example.util.locale;

/**
 * Created by Sanjeev on 19/10/15.
 */
public interface MessageByLocaleService {

    /**
     * getMessage.
     *
     * This method gets the respective string from the local message,
     * @param id id
     * @return Vale of the respective key from the configured properties file.
     */
    String getMessage(String id);


    /**
     * This method gets the respective string from the local message with args passed if any.
     * @param id key
     * @param args args of any.
     * @return message,
     */
    String getMessage(String id, Object[] args);
}
