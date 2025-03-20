package com.revature.repos.interfaces;

import com.revature.models.User;

public interface UserDAO extends GeneralDAO<User>{
    User getUserByEmail(String email);

}
