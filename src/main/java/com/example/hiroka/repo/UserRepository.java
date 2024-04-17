package com.example.hiroka.repo;


import com.example.hiroka.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
   Optional<User> findByEmail(String email);
   @Query("select u from User u " +
           "where lower(u.email) like lower(concat('%', :searchTerm, '%')) " +
           "or lower(u.username) like lower(concat('%', :searchTerm, '%'))")
   List<User> search(@Param("searchTerm") String searchTerm);
}
