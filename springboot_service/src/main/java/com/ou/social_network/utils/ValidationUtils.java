package com.ou.social_network.utils;

import org.springframework.validation.BindingResult;

public class ValidationUtils {
    public static String getInvalidMessage(BindingResult bindingResult){
        return bindingResult.getAllErrors()
                        .get(0)
                        .getDefaultMessage();
    }    
}
