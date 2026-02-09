
package com.example.LogDB.web;

import com.example.LogDB.logs.LogsService;
import com.example.LogDB.registration.RegistrationRequest;
import com.example.LogDB.registration.RegistrationService;
import com.example.LogDB.users.UsersRole;
import com.example.LogDB.users.UsersService;
import java.math.BigDecimal;
import java.text.ParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
public class WebController {                                                    //Class that contains the Application Services Endpoints and their corresponding Post Handlers.
                                                                                //The Log, Registration and Users Services, provide the appropriate functions for the API demands. 
    @Autowired                                                                  //The insertions and updates are excecuted via Post methods so as to display the action results.
    private final LogsService logservice;
    private final RegistrationService registrationservice;
    private final UsersService userservice;



    public WebController(LogsService logservice,
            RegistrationService registrationservice,
            UsersService userservice){
        this.logservice=logservice;
        this.registrationservice = registrationservice;
        this.userservice = userservice;
}


    
    @GetMapping({"","/","/home"})                                               //Home endpoint.
    public String homePage(Model model){
        userservice.showUserName(model);
        return "home";
    }

    @GetMapping("/login")                                                       //Default Login Page endpoint.
    public String loginPage(){
        return "login";
    }

    @GetMapping("/logout")                                                      //Default Logout Page endpoint.
    public String logoutPage(){
        return "logout";}


    @GetMapping(path = "/register/confirm")                                     //Method for redirecting Confirmation URL to login or register Page
    public String confirm(Model model,@RequestParam("token") String token) {    //according to the confirmation result.
         String response =registrationservice.confirmToken(token);
         model.addAttribute("result",response);
         if (response.startsWith("Confirmed") || response.startsWith("Email")){
            return "login";
         }
         else return "register";
    }

    @GetMapping("/register")
    public String registerPage(){                                               //Registration Page endpoint.
        return "register";
    }

    @PostMapping("/registersend")                                               //Method for handling Registration input.
    public String register(Model model,@RequestParam String firstName,
            @RequestParam String lastName,@RequestParam String userName,
            @RequestParam String password,@RequestParam String email,@RequestParam String role){

        RegistrationRequest request=new RegistrationRequest(firstName,lastName,userName,email,password,UsersRole.valueOf(role));
        String response=registrationservice.register(request);
        if (!"Register Sent".equals(response)) {
            model.addAttribute("result",response);
            return "register";}
        else return "registersent";
    }


    @GetMapping("/findbyip")                                                    //"Search Logs by IP" endpoint and Post handler.
    public String FindByIP(Model model) throws ParseException{
        userservice.showUserName(model);
        return "findbyip";
    }

    @PostMapping("/logsbyip")                                                    
    public String FindByIP(Model model,@RequestParam String searchIP) throws ParseException{
        model.addAttribute("searchip",logservice.findByIP(searchIP));
        model.addAttribute("searchIP",searchIP);
        userservice.showUserName(model);
        return "logsbyip";
    }

    @GetMapping("/logspertype")                                                 //"Search Log types in given time Range" endpoint and Post handler.
    public String logspertypePage(Model model){
        userservice.showUserName(model);
        return "logspertype";}

    @PostMapping("/typecount")
    public String typecountPage(Model model,@RequestParam String date_one,      
            @RequestParam String date_two) throws ParseException{
        String[] dateone = date_one.split("T",2);
        String[] datetwo = date_two.split("T",2);
        model.addAttribute("logspertype",logservice.getTypes(date_one,date_two));
        model.addAttribute("date_one",dateone[0]+" "+dateone[1]);
        model.addAttribute("date_two",datetwo[0]+" "+datetwo[1]);
        userservice.showUserName(model);
        return "typecount";}
    
    @GetMapping("/logsperday")                                                  //"Search Logs of Specific Type per Day for a given time range" endpoint and Post handler.
    public String logsperdatePage(Model model){
        userservice.showUserName(model);
        return "logsperday";}
    
    @PostMapping("/logcount")
    public String logcountPage(Model model,@RequestParam String type,
            @RequestParam String date_one,@RequestParam String date_two) throws ParseException{
        String[] dateone = date_one.split("T",2);
        String[] datetwo = date_two.split("T",2);
        model.addAttribute("logsperday",logservice.getLogs(type,date_one,date_two));
        model.addAttribute("type",type);
        model.addAttribute("date_one",dateone[0]+" "+dateone[1]);
        model.addAttribute("date_two",datetwo[0]+" "+datetwo[1]);
        userservice.showUserName(model);
        return "logcount";}


    @GetMapping("/commonlog")                                                   //"Search most Common log per Source IP for a specific day" endpoint and Post handler.
    public String commonlogPage(Model model){
        userservice.showUserName(model);
        return "commonlog";}

    @PostMapping("/ipcount")
    public String ipcountPage(Model model,@RequestParam String date) throws ParseException{
        model.addAttribute("ipperday",logservice.logIp(date));
        model.addAttribute("date",date);
        userservice.showUserName(model);
        return "ipcount";}

   @GetMapping("/topblocks")                                                    //"Search actions-per-day top-5 Block IDs for a specific date range " endpoint and Post handler.
    public String topblocksPage(Model model){
        userservice.showUserName(model);
        return "topblocks";}


    @PostMapping("/blockcount")
    public String blockcountPage(Model model,@RequestParam String date_one,@RequestParam String date_two) throws ParseException{
        model.addAttribute("blockperday",logservice.blockCount(date_one,date_two));
        model.addAttribute("date_one",date_one);
        model.addAttribute("date_two",date_two);
        userservice.showUserName(model);
        return "blockcount";}


    @GetMapping("/referers")
    public String referersPage(Model model) throws ParseException {             //"Referers tha led to more than one resources" endpoint.
        model.addAttribute("referers",logservice.getResources());
        userservice.showUserName(model);
        return "referers";}

    @GetMapping("/commonresource")                                              //"Search the second most common resource" endpoint.
    public String resourcePage(Model model) throws ParseException {
        model.addAttribute("referers",logservice.getTopResources());
        userservice.showUserName(model);
        return "commonresource";}

    @GetMapping("/logsize")                                                     //"Search Access Logs with size less than a given number " endpoint and Post handler.
    public String logsizePage(Model model){
        userservice.showUserName(model);
        return "logsize";}

    @PostMapping("/sizedlogs")
    public String blockcountPage(Model model,@RequestParam Long size) throws ParseException{
        model.addAttribute("sizedlogs",logservice.getSizedLogs(size));
        model.addAttribute("size",size.toString());
        userservice.showUserName(model);
        return "sizedlogs";}

    @GetMapping("/dayreplicatedserved")                                         //"Search Block IDs that were replicated and served the same day" endpoint.
    public String dayreplicatedservedPage(Model model) throws ParseException {
        model.addAttribute("dreplicatedserved",logservice.dayReplicatedServed());
        userservice.showUserName(model);
        return "dayreplicatedserved";}

    @GetMapping("/hourreplicatedserved")                                        //"Search Block IDs that were replicated and served the same day and hour" endpoint.
    public String houreplicatedservedPage(Model model) throws ParseException {  
        model.addAttribute("hreplicatedserved",logservice.hourReplicatedServed());
        userservice.showUserName(model);
        return "hourreplicatedserved";}


    @GetMapping("/firefoxversion")                                              //"Search Access Logs that specified a particular version of Firefox as their browser" endpoint.
    public String firefoxversionPage(Model model) throws ParseException {
        model.addAttribute("firefox",logservice.getBrowser());
        userservice.showUserName(model);
        return "firefoxversion";}

    @GetMapping("/searchonehttp")                                               //"Search IPs that isssued a particular HTTP method on a specific time range " endpoint and Post handler.
    public String searchOneHttp(Model model){
        userservice.showUserName(model);
        return "searchonehttp";}

    @PostMapping("/httpmethodone")
    public String httpmethodonePage(Model model,@RequestParam String method,
            @RequestParam String date_one,@RequestParam String date_two) throws ParseException {
        String[] dateone = date_one.split("T",2);
        String[] datetwo = date_two.split("T",2);
        model.addAttribute("methodone",logservice.httpMethodOne(method,date_one,date_two));
        model.addAttribute("date_one",dateone[0]+" "+dateone[1]);
        model.addAttribute("date_two",datetwo[0]+" "+datetwo[1]);
        model.addAttribute("method",method);
        userservice.showUserName(model);
        return "httpmethodone";}


    @GetMapping("/searchtwohttp")                                               //"Search IPs that isssued two particular HTTP methods on a specific time range " endpoint and Post handler.
    public String searchtwoHttp(Model model){
        userservice.showUserName(model);
        return "searchtwohttp";}

    @PostMapping("/httpmethodtwo")
    public String httpmethodtwoPage(Model model,@RequestParam String method_one,
            @RequestParam String method_two,@RequestParam String date_one,
            @RequestParam String date_two) throws ParseException {
        String[] dateone = date_one.split("T",2);
        String[] datetwo = date_two.split("T",2);
        model.addAttribute("methodtwo",logservice.httpMethodTwo(method_one,method_two,date_one,date_two));
        model.addAttribute("date_one",dateone[0]+" "+dateone[1]);
        model.addAttribute("date_two",datetwo[0]+" "+datetwo[1]);
        model.addAttribute("method_one",method_one);
        model.addAttribute("method_two",method_two);
        userservice.showUserName(model);
        return "httpmethodtwo";}

    @GetMapping("/searchfourhttp")                                              //"Search IPs that isssued amy four distinct HTTP methods on a specific time range " endpoint and Post handler.
    public String searchfourHttp(Model model){
        userservice.showUserName(model);
        return "searchfourhttp";}

    @PostMapping("/httpmethodfour")
    public String httpmethodfourPage(Model model,@RequestParam String date_one,
            @RequestParam String date_two) throws ParseException {
        String[] dateone = date_one.split("T",2);
        String[] datetwo = date_two.split("T",2);
        model.addAttribute("methodfour",logservice.httpMethodFour(date_one,date_two));
        model.addAttribute("date_one",dateone[0]+" "+dateone[1]);
        model.addAttribute("date_two",datetwo[0]+" "+datetwo[1]);
        userservice.showUserName(model);
        return "httpmethodfour";}

    
    @GetMapping("/inserthdfslog")                                               //"Insert HDFS Log" endpoint and Post handler.
    public String insertHdfsLog(Model model){
        String result = " ";
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "inserthdfslog";
    }

    @PostMapping("/inserthdfslog")
    public String insertHdfsResultLog(Model model, @RequestParam String time_date, 
            @RequestParam String source_ip, @RequestParam String log_type,
            @RequestParam String specific_type, @RequestParam BigDecimal blockid, 
            @RequestParam String destination_ip,@RequestParam BigDecimal size)
    {
        String result=logservice.insertHDFSLog(time_date,source_ip,log_type,
                specific_type,blockid,destination_ip,size);
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "inserthdfslog";
    }

    @GetMapping("/insertweblog")                                                //"Insert Access Log" endpoint and Post handler.
    public String insertWebLog(Model model){
        userservice.showUserName(model);
        String result = " ";
        model.addAttribute("result",result);
        return "insertweblog";
    }

    @PostMapping("/insertweblog")
    public String insertWebResultLog(Model model,@RequestParam String time_date,
            @RequestParam String source_ip,@RequestParam String log_type,
            @RequestParam String specific_type,@RequestParam String username,
            @RequestParam String user_id,@RequestParam BigDecimal http_status,
            @RequestParam BigDecimal response_size,@RequestParam String referers,
            @RequestParam String resources,@RequestParam String agents)
    {
        String result=logservice.insertWebLog(time_date,source_ip,log_type,
                specific_type,username,user_id,http_status,response_size,referers,
                resources,agents);
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "insertweblog";
    }

    @GetMapping("/modifylog")                                                   //"Modify Logs" endpoints and Post handlers. After the User selects the ID of the Log
    public String upDateLog(Model model){                                       //for modification, the type-corresponding endpoint and handler is provided.
        String result = "Log ID can be found in \"Find Logs\" Page";
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "modifylog";
    }

    @PostMapping("/modifylog")
    public String searchUpdateLog(Model model,@RequestParam Integer LogId){
        userservice.showUserName(model);
        String type=logservice.getSearchType(LogId);
        if (type.startsWith("HTTP")){
            model.addAttribute("result",logservice.getSearchWeb(LogId));
            model.addAttribute("log_id",LogId);
            return "modifyweblog";}
        else if (type.startsWith("HDFS")){
            model.addAttribute("result",logservice.getSearchHDFS(LogId));
            model.addAttribute("log_id",LogId);
            return "modifyhdfslog";}
        else {
            model.addAttribute("result",type);
            return "modifylog";
        }
    }

    @PostMapping("/updatehdfslog")

        public String  updateHdfslog(Model model,@RequestParam String log_id, 
                @RequestParam String time_date, @RequestParam String source_ip,
                @RequestParam String log_type,@RequestParam String specific_type,
                @RequestParam BigDecimal blockid, @RequestParam String destination_ip,
                @RequestParam BigDecimal size){
        String result=logservice.updateHDFSLog(Integer.valueOf(log_id),time_date,
                source_ip,log_type,specific_type,blockid,destination_ip,size);
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "modifylog";
    }

    @PostMapping("/updateweblog")

    public String  updateweblog(Model model,@RequestParam String log_id,
            @RequestParam String time_date,@RequestParam String source_ip,
            @RequestParam String log_type,@RequestParam String specific_type,
            @RequestParam String username,@RequestParam String user_id,
            @RequestParam BigDecimal http_status,@RequestParam BigDecimal response_size,
            @RequestParam String referers,@RequestParam String resources,@RequestParam String agents
    ){
        String result=logservice.updateWebLog(Integer.valueOf(log_id),time_date,
                source_ip,log_type,specific_type,username,user_id,http_status,response_size,
                referers,resources,agents);
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "modifylog";
    }

    @GetMapping("/deletelog")                                                   //"Delete Log" endpoint and Post handler.
    public String deleteLog(Model model){
        String result = "Log ID can be found in \"Find Logs\" Page";
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "deletelog";
    }

    @PostMapping("/deletelog")
    public String deleteResultLog(Model model,@RequestParam Integer LogId){
        String result=logservice.deleteLog(LogId);
        model.addAttribute("result",result);
        userservice.showUserName(model);
        return "deletelog";
    }
}
