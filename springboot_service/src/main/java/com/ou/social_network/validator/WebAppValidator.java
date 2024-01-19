package com.ou.social_network.validator;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ou.social_network.pojo.Account;
import com.ou.social_network.pojo.AuthRequest;
import com.ou.social_network.pojo.Comment;
import com.ou.social_network.pojo.Post;
import com.ou.social_network.pojo.User;
import com.ou.social_network.pojo.UserStudent;

import jakarta.validation.ConstraintViolation;
import lombok.Setter;

@Component
@Setter
public class WebAppValidator implements Validator {
    @Autowired
    private jakarta.validation.Validator beanValidators;

    @Autowired
    private PassValidator passValidator;

    private Set<Validator> springValidators = new HashSet<>();

    @Override
    public boolean supports(Class<?> clazz) {
        return Account.class.isAssignableFrom(clazz) ||
                User.class.isAssignableFrom(clazz) ||
                UserStudent.class.isAssignableFrom(clazz) ||
                AuthRequest.class.isAssignableFrom(clazz) ||
                Comment.class.isAssignableFrom(clazz) ||
                Post.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        springValidators.add(passValidator);
        Set<ConstraintViolation<Object>> constraintViolations = beanValidators.validate(target);
        constraintViolations.forEach(cs -> {
            errors.reject(
                    cs.getMessageTemplate(),
                    cs.getMessage());
        });
        springValidators.forEach(sv -> {
            sv.validate(target, errors);
        });
    }

}
