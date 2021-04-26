package com.edafa.chatapp.service;

import com.edafa.chatapp.entity.User;

/**
 * Created by doaa1 on 4/25/2021.
 */
public interface IUserService {
    User searchByUserNameAndPassword(String userName, String password);
}
