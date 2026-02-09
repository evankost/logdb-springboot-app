
package com.example.LogDB.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


@Repository
@Transactional(readOnly = true)
public interface ConfirmTokenRepository extends JpaRepository<ConfirmToken,Long>{//Interface to ineract with the Database relation of Registration tokens. 

    @Transactional                                                              //Method for retrieving a Registration token by the token attribute (could also use the findByToken 
    @Query("SELECT c FROM ConfirmToken c " +                                    //method of JpaRepository).
            "WHERE c.token = ?1")
    Optional<ConfirmToken> findToken(String token);

                       
    @Transactional                                                              //Method for updating the confirmation Date of a Registration token.
    @Modifying
    @Query("UPDATE ConfirmToken c " +
            "SET c.confirmed = ?2 " +
            "WHERE c.token = ?1")
    int updateConfirmed(String token,
                          LocalDateTime confirmedAt);
    
}
