package com.catamco.IT342.Repository;

import com.catamco.IT342.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AuthRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email_address = :email")
    Optional<User> findByEmailAddress(@Param("email") String email);

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM User u WHERE u.email_address = :email")
    boolean existsByEmailAddress(@Param("email") String email);
}