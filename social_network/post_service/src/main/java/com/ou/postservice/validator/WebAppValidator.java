package com.ou.postservice.validator;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.ou.postservice.pojo.Comment;
import com.ou.postservice.pojo.Post;
import com.ou.postservice.pojo.Reaction;
import com.ou.postservice.pojo.User;
import lombok.Setter;

@Component
@Setter
public class WebAppValidator implements Validator {


    @Override
    public boolean supports(Class<?> clazz) {
        return Reaction.class.isAssignableFrom(clazz) ||
                User.class.isAssignableFrom(clazz) ||
                Post.class.isAssignableFrom(clazz) ||
                Comment.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'validate'");
    }

}
