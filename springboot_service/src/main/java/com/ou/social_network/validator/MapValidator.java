package com.ou.social_network.validator;

import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ou.social_network.pojo.Account;
import com.ou.social_network.pojo.User;
import com.ou.social_network.pojo.UserStudent;

@Component
public class MapValidator implements Validator{
    @Autowired
    private WebAppValidator webAppValidator;

    @Override
    public boolean supports(Class<?> clazz) {
        return Map.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        @SuppressWarnings("unchecked")
        Map<String, Object> requestParams = (Map<String, Object>) target;
        ObjectMapper mapper = new ObjectMapper();
        Account account = mapper.convertValue(requestParams.get("account"), Account.class);
        User user = mapper.convertValue(requestParams.get("user"), User.class);
        UserStudent userStudent = mapper.convertValue(requestParams.get("userStudent"), UserStudent.class);
        System.out.println("[DEBUG] - START VALIDATE IN THE MAP VALIDATOR");
        if(account != null){
            webAppValidator.validate(account, errors);
        }
        
        if(user != null){
            webAppValidator.validate(user, errors);
        }
        
        if(userStudent != null){
            webAppValidator.validate(userStudent, errors);
        }
    }
    
}
