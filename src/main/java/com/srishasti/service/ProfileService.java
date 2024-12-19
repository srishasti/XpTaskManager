package com.srishasti.service;

import com.srishasti.context.UserContext;
import com.srishasti.model.User;
import com.srishasti.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProfileService {

    @Autowired
    private UserRepo userRepo;

    private int xpThreshold = 200;
    private int healthThreshold = 100;

    public void changeXp(int xp){

        if(xp<0) throw new IllegalArgumentException();

        int userId = UserContext.getUserId();
        User user = userRepo.findById(userId).orElse(null);

        if(user.getXp()+xp > xpThreshold){
            user.setLevel(user.getLevel()+1);
            user.setXp((user.getXp()+xp)-xpThreshold);
        }
        else user.setXp(user.getXp()+xp);
        userRepo.save(user);
    }

    public void changeHealth(int userId, int health){

        if(health < 0) throw new IllegalArgumentException();

        User user = userRepo.findById(userId).orElse(null);
        if(user == null) throw new IllegalArgumentException();

        if(user.getHealth() < health){
            if(user.getLevel()>1){
                user.setLevel(user.getLevel()-1);
                user.setHealth(healthThreshold -  (health - user.getHealth()));
            }
            else user.setHealth(0);
        }
        else user.setHealth(user.getHealth()-health);
        userRepo.save(user);
    }

}
