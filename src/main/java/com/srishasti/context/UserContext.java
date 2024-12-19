package com.srishasti.context;

import org.springframework.stereotype.Component;

@Component
public class UserContext {

    private static final ThreadLocal<Integer> userId  = new ThreadLocal<>();
    public static Integer getUserId(){
        return userId.get();
    }
    public static void setUserId(int id){
        userId.set(id);
    }
    public static void clear(){
        userId.remove();
    }

}
