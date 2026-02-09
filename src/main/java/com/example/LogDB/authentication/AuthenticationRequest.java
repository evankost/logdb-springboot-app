package com.example.LogDB.authentication;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {                                            //Class for JWT Authentication Request parameters: username & password

  private String userName;
  String password;
}
