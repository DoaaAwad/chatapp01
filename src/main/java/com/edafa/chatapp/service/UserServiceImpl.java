package com.edafa.chatapp.service;

import com.edafa.chatapp.dao.jpaRepository.UserJpaRepository;
import com.edafa.chatapp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by doaa1 on 4/25/2021.
 */
@Service
public class UserServiceImpl implements IUserService {
    @Autowired
    UserJpaRepository userJpaRepository;

    @Override
    public User searchByUserNameAndPassword(String userName, String password) {
        return userJpaRepository.searchByUserNameAndPassword(userName,password);
    }
}
