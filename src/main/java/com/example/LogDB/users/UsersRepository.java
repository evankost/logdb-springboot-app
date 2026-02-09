
package com.example.LogDB.users;


import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional (readOnly = true) 
public interface UsersRepository extends JpaRepository<Users, Long> {           //Interface for communication with the Database.
    
    Optional<Users> findByUserName(String username);                            //Here we provide the corresponding attributes of our Users class to the
                                                                                //default query methods provided by JpaRepository.
    Optional<Users> findByMail(String email);

    @Transactional                                                              //Custom method for finding Users by username, when we are sure that the
    @Query("SELECT a FROM Users a " +                                           //user exists.
            "WHERE a.userName = ?1")
    Users findUser(String username);
    
    @Transactional                                                              //Method for enabling a User by updating the enable attribute to true.
    @Modifying
    @Query("UPDATE Users a " +
            "SET a.enabled = TRUE WHERE a.userName = ?1")
    int enableUser(String username);
    
    
}
