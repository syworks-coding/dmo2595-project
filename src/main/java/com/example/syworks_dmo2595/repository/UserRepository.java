package com.example.syworks_dmo2595.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Boolean existsByLoginId(String loginId);
    Boolean existsByPassword(String password);


    User findByLoginIdAndPassword(String loginId, String password);

}
