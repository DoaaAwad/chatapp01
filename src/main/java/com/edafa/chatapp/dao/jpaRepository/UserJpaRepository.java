package com.edafa.chatapp.dao.jpaRepository;


import com.edafa.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by doaa1 on 4/25/2021.
 */
public interface UserJpaRepository extends JpaRepository<User,BigDecimal> {

    List<User> searchByUserNameAndPassword(String userName, String password);

}
