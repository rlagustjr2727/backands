package com.web.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.web.user.User;

@Repository
public interface UserRepository extends JpaRepository<User, String>, UserRepositoryCustom {
    User findByUserId(String userId);
    boolean existsByUserId(String userId);
    boolean existsByUserNickName(String userNickName);
}
