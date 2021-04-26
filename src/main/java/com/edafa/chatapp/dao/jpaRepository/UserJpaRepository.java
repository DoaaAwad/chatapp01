package com.edafa.chatapp.dao.jpaRepository;


import com.edafa.chatapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * Created by doaa1 on 4/25/2021.
 */
@Repository
public interface UserJpaRepository extends JpaRepository<User,BigDecimal> {

    @Query("SELECT u FROM User u WHERE u.userName = ?1 AND u.password = ?2")
    User searchByUserNameAndPassword(String userName, String password);

}
