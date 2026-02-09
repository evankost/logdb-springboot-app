package com.example.LogDB.jwt.token;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Integer> {  //Interface to ineract with the Database relation of JWT tokens.

  @Query(value = """
      select t from JwtToken t inner join Users u
      on t.user.user_id = u.user_id
      where u.user_id = :user_id and (t.expired = false or t.revoked = false)
      """)
  List<JwtToken> findAllValidTokenByUser(Integer user_id);                      //Method that retrives a List of valid JWT tokens of a specific User.
  
  Optional<JwtToken> findByToken(String token);                                 //Method that retrieves one specific JWT token.
}
