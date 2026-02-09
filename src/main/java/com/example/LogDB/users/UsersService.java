package com.example.LogDB.users;

import com.example.LogDB.registration.token.ConfirmToken;
import com.example.LogDB.registration.token.ConfirmTokenService;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;


@Service
@AllArgsConstructor
public class UsersService implements UserDetailsService{                        //Class for implemantation of the UserDetailsService Interface. 
    
    private final UsersRepository userepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmTokenService confirmokenservice;
   
    private final static String USER_NOT_FOUND = "User with username %s not found";



    public void showUserName(Model model){                                      //Method for displaying Username of the logged User.
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        model.addAttribute("username",currentPrincipalName);}
    
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userepository.findByUserName(username).orElseThrow(() ->{
            return new UsernameNotFoundException(String.format(USER_NOT_FOUND,username));
        });
    }
    
    
    public String signUpUser(Users user){                                       //Method for signing up a User in the Application. At first the given User is checked
                                                                                //for providing a non-existent username. If the username exitsts, the email is checked
    String token = UUID.randomUUID().toString();                                //as well. If both exist means the user is already signed in the Application : In case
    boolean userExists = userepository.findByUserName(user.getUsername()).isPresent();//the user is not enabled, a new Confirmation Token is generated for the User.If 
    boolean mailExists = userepository.findByMail(user.getMail()).isPresent();  //only the email exists, the User is prompted to provide a different email. At last, if
    if (userExists) {                                                           //neither the username nor the email exist,a new Confirmation Token is generated for the
        Users dbuser = userepository.findUser(user.getUsername());              //User,the password is encoded and both the Token and the User are stored in the Database.
        if (mailExists && dbuser.getMail().equals(user.getMail())){             //Expiration of the Registration token is set to 5 minutes. The attribute token of the 
            if (!dbuser.isEnabled()){                                           //Registration token is produced by UUID class.
                      Users existingUser =userepository.findUser(user.getUsername());
                      ConfirmToken newtoken = new ConfirmToken(token,LocalDateTime.now(),LocalDateTime.now().plusMinutes(5),null,existingUser);
                      confirmokenservice.saveToken(newtoken);
                      return token;
            }
            else return "Already Registered";
         }
         else return "Username not available, Choose Another UserName";}
     
     if (mailExists) { return "Email not available, Choose Another Email.";}
     
     String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
     user.setPassword(encodedPassword);
     userepository.save(user);
     ConfirmToken confirmtoken = new ConfirmToken(token,LocalDateTime.now(),LocalDateTime.now().plusMinutes(5),null,user);
     confirmokenservice.saveToken(confirmtoken);
     return token;    }
    
    public int enableUser(String username) {                                    //Method for altering enable status ao a User to true.
        return userepository.enableUser(username);
    }
        
    public String buildEmail(String name, String link) {                        //Method for building an email including the registration confirmation link of a User.
        return """
                <!doctype html>
                <html>
                  <head>
                    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
                    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
                    <title>Simple Transactional Email</title>
                    <style>
                     \s
                      img {
                        border: none;
                        -ms-interpolation-mode: bicubic;
                        max-width: 100%;\s
                      }
                                
                      body {
                        background-color: #f6f6f6;
                        font-family: sans-serif;
                        -webkit-font-smoothing: antialiased;
                        font-size: 14px;
                        line-height: 1.4;
                        margin: 0;
                        padding: 0;
                        -ms-text-size-adjust: 100%;
                        -webkit-text-size-adjust: 100%;\s
                      }
                                
                      table {
                        border-collapse: separate;
                        mso-table-lspace: 0pt;
                        mso-table-rspace: 0pt;
                        width: 100%; }
                        table td {
                          font-family: sans-serif;
                          font-size: 14px;
                          vertical-align: top;\s
                      }
                                
                                
                      .body {
                        background-color: #f6f6f6;
                        width: 100%;\s
                      }
                                
                        .container {
                        display: block;
                        margin: 0 auto !important;
                        max-width: 580px;
                        padding: 10px;
                        width: 580px;\s
                      }
                                
                         .content {
                        box-sizing: border-box;
                        display: block;
                        margin: 0 auto;
                        max-width: 580px;
                        padding: 10px;\s
                      }
                                
                      .title{
                		 background: #708090;
                		 color: #ffffff;
                		 text-align: center;
                		 font-size: 30px
                	  }
                                
                      .main {
                        background: #ffffff;
                        border-radius: 3px;
                        width: 100%;\s
                      }
                                
                      .wrapper {
                        box-sizing: border-box;
                        padding: 20px;\s
                      }
                                
                      .content-block {
                        padding-bottom: 10px;
                        padding-top: 10px;
                      }
                                
                      .footer {
                        clear: both;
                        margin-top: 10px;
                        text-align: center;
                        width: 100%;\s
                      }
                        .footer td,
                        .footer p,
                        .footer span,
                        .footer a {
                          color: #999999;
                          font-size: 12px;
                          text-align: center;\s
                      }
                                
                                
                      h1,
                      h2,
                      h3,
                      h4 {
                        color: #000000;
                        font-family: sans-serif;
                        font-weight: 400;
                        line-height: 1.4;
                        margin: 0;
                        margin-bottom: 30px;\s
                      }
                                
                      h1 {
                        font-size: 35px;
                        font-weight: 300;
                        text-align: center;
                        text-transform: capitalize;\s
                      }
                                
                      p,
                      ul,
                      ol {
                        font-family: sans-serif;
                        font-size: 14px;
                        font-weight: normal;
                        margin: 0;
                        margin-bottom: 15px;\s
                      }
                        p li,
                        ul li,
                        ol li {
                          list-style-position: inside;
                          margin-left: 5px;\s
                      }
                                
                      a {
                        color: #3498db;
                        text-decoration: underline;\s
                      }
                                
                                
                      .btn {
                        box-sizing: border-box;
                        width: 100%; }
                        .btn > tbody > tr > td {
                          padding-bottom: 15px; }
                        .btn table {
                          width: auto;\s
                      }
                        .btn table td {
                          background-color: #ffffff;
                          border-radius: 5px;
                          text-align: center;\s
                      }
                        .btn a {
                          background-color: #ffffff;
                          border: solid 1px #3498db;
                          border-radius: 5px;
                          box-sizing: border-box;
                          color: #3498db;
                          cursor: pointer;
                          display: inline-block;
                          font-size: 14px;
                          font-weight: bold;
                          margin: 0;
                          padding: 12px 25px;
                          text-decoration: none;
                          text-transform: capitalize;\s
                      }
                                
                      .btn-primary table td {
                        background-color: #708090;\s
                      }
                                
                      .btn-primary a {
                        background-color: #708090;
                        border-color: #708090;
                        color: #ffffff;\s
                      }
                                
                     \s
                     hr {
                        border: 0;
                        border-bottom: 1px solid #f6f6f6;
                        margin: 20px 0;\s
                      }
                                
                                
                      @media only screen and (max-width: 620px) {
                        table.body h1 {
                          font-size: 28px !important;
                          margin-bottom: 10px !important;\s
                        }
                        table.body p,
                        table.body ul,
                        table.body ol,
                        table.body td,
                        table.body span,
                        table.body a {
                          font-size: 16px !important;\s
                        }
                        table.body .wrapper,
                        table.body .article {
                          padding: 10px !important;\s
                        }
                        table.body .content {
                          padding: 0 !important;\s
                        }
                        table.body .container {
                          padding: 0 !important;
                          width: 100% !important;\s
                        }
                        table.body .main {
                          border-left-width: 0 !important;
                          border-radius: 0 !important;
                          border-right-width: 0 !important;\s
                        }
                        table.body .btn table {
                          width: 100% !important;\s
                        }
                        table.body .btn a {
                          width: 100% !important;\s
                        }
                        table.body .img-responsive {
                          height: auto !important;
                          max-width: 100% !important;
                          width: auto !important;\s
                        }
                      }
                                
                                
                      @media all {
                        .ExternalClass {
                          width: 100%;\s
                        }
                        .ExternalClass,
                        .ExternalClass p,
                        .ExternalClass span,
                        .ExternalClass font,
                        .ExternalClass td,
                        .ExternalClass div {
                          line-height: 100%;\s
                        }
                        .apple-link a {
                          color: inherit !important;
                          font-family: inherit !important;
                          font-size: inherit !important;
                          font-weight: inherit !important;
                          line-height: inherit !important;
                          text-decoration: none !important;\s
                        }
                        #MessageViewBody a {
                          color: inherit;
                          text-decoration: none;
                          font-size: inherit;
                          font-family: inherit;
                          font-weight: inherit;
                          line-height: inherit;
                        }
                        .btn-primary table td:hover {
                          background-color: #34495e !important;\s
                        }
                        .btn-primary a:hover {
                          background-color: #34495e !important;
                          border-color: #34495e !important;\s
                        }\s
                      }
                                
                    </style>
                  </head>
                  <body>
                     <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="body">
                      <tr>
                        <td>&nbsp;</td>
                        <td class="container">
                          <div class="content">
                            <table role="presentation" class="main">
                              <tr>
                                <td class="wrapper">
                                  <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                    <tr>
                                      <td>
                					    <div class="title">
                					    Confirm Your Email
                						</div>
                						<br>
                                        <p>Hello Mr/Mrs\s"""+
                name
                                        +"""
                                        </p>
                                        <p>Thank you for registering. Please click on the below link to activate your account:</p>
                                        <table role="presentation" border="0" cellpadding="0" cellspacing="0" class="btn btn-primary">
                                          <tbody>
                                            <tr>
                                              <td align="left">
                                                <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                                  <tbody>
                                                    <tr>
                                                      <td> <a href="
                                                      """+
                link
                                                      + """
                                                     " target="_blank">Activate Now</a> </td>
                                                    </tr>
                                                  </tbody>
                                                </table>
                                              </td>
                                            </tr>
                                          </tbody>
                                        </table>
                                        <p>Link will expire in 5 minutes.</p>
                                      </td>
                                    </tr>
                                  </table>
                                </td>
                              </tr>
                            </table>\s
                            <div class="footer">
                              <table role="presentation" border="0" cellpadding="0" cellspacing="0">
                                <tr>
                                  <td class="content-block">
                                    <span class="apple-link">Company Inc, Xlois 4 , Athens ZIP 14565 </span>      \s
                                  </td>
                                </tr>
                                <tr>
                                  <td class="content-block powered-by">
                                    Powered by LogDB</a>.
                                  </td>
                                </tr>
                              </table>
                            </div>
                          </div>
                        </td>
                        <td>&nbsp;</td>
                      </tr>
                    </table>
                  </body>
                </html>
                                
                """;
    }
}
