package com.authentication;

import com.core.Constants;

import java.util.HashMap;

/**
 * Класс, отвечающий за аутентификацию пользователей
 * @author Dmitry
 */
public class Authenticator {
    private String userId;

    public String getStatus(){
        if (userId == null){
            return Constants.LOGIN_MESSAGE;
        }
        return "";
    }

    public String authenticate(String input){
        userId = input;
        return Constants.SUCCESSFULL_AUTH_MSG;
    }

    public boolean gotCredentials(){
        return userId != null;
    }

    public String getUserId(){
        return userId;
    }
}
