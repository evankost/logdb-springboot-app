
package com.example.LogDB.logs;




import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class LogsService {                                                      //Class for handling the API demands of the Application, by implementing the appropriate Data
                                                                                //transformations and calling the corresponding Database related methods. Basic tranformations
                @Autowired                                                      //are String to date-timestamp and Integer to BigDecimal casts, so as the Data to be compliant
                private LogRepository logrepository;                            //with the DataBase data types. Insertions and Updates follow the Databse Relations structure, 
                                                                                //for example before inserting an access log, its total size is computed. Finally,the deletions
                @Autowired                                                      //are excecuted in the Logs relation and are cascaded by log_id to the other two relations.
                private HdfsRepository hdfslogrepository;
                
                @Autowired
                private WebRepository weblogrepository;


                public List<Object> getTypes(String date_one,String date_two) 
                        throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Timestamp sDate = new java.sql.Timestamp(dateStart.getTime());
                    java.sql.Timestamp eDate = new java.sql.Timestamp(dateEnd.getTime());
                    return logrepository.getTypes(sDate,eDate);
                }

                public List<Object> getLogs(String type,String date_one,String date_two) 
                        throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Timestamp sDate = new java.sql.Timestamp(dateStart.getTime());
                    java.sql.Timestamp eDate = new java.sql.Timestamp(dateEnd.getTime());
                    return logrepository.getLogs(type,sDate,eDate);
                }

                public List<Object> logIp(String date) throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateStart = dateFormat.parse(date);
                    java.sql.Date parsedate = new java.sql.Date(dateStart.getTime());
                    return logrepository.logIp(parsedate);
                }

                public List<Object> blockCount(String date_one,String date_two) 
                        throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Date sDate = new java.sql.Date(dateStart.getTime());
                    java.sql.Date eDate = new java.sql.Date(dateEnd.getTime());
                    return logrepository.blockCount(sDate,eDate);
                }

                public List<Object> getResources() throws ParseException{
                    return hdfslogrepository.getResources();
                }

                public List<Object> getTopResources() throws ParseException{
                    return hdfslogrepository.getTopResources();
                }

                public List<Object> getSizedLogs(Long N) throws ParseException{
                    return weblogrepository.getSizedLogs(N);
                }

                public List<Object> dayReplicatedServed() throws ParseException{
                    return logrepository.dayReplicatedServed();
                }

                public List<Object> hourReplicatedServed() throws ParseException{
                    return logrepository.hourReplicatedServed();
                }

                public List<Object> getBrowser() throws ParseException{
                    return weblogrepository.getBrowser();
                }

                public List<Object> httpMethodOne(
                        String method,String date_one,String date_two) throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Timestamp sDate = new java.sql.Timestamp(dateStart.getTime());
                    java.sql.Timestamp eDate = new java.sql.Timestamp(dateEnd.getTime());
                    return logrepository.httpMethodOne(method,sDate,eDate);
                }

                public List<Object> httpMethodTwo(
                        String method_one,String method_two,String date_one,String date_two) 
                        throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Timestamp sDate = new java.sql.Timestamp(dateStart.getTime());
                    java.sql.Timestamp eDate = new java.sql.Timestamp(dateEnd.getTime());
                    return logrepository.httpMethodTwo(method_one,method_two,sDate,eDate);
                }

                public List<Object> httpMethodFour(
                        String date_one,String date_two) throws ParseException{
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date dateStart = dateFormat.parse(date_one);
                    java.util.Date dateEnd = dateFormat.parse(date_two);
                    java.sql.Timestamp sDate = new java.sql.Timestamp(dateStart.getTime());
                    java.sql.Timestamp eDate = new java.sql.Timestamp(dateEnd.getTime());
                    return logrepository.httpMethodFour(sDate,eDate);
                }


                public List<Object> findByIP(String searchIP) throws ParseException{
                    return logrepository.findByIP(searchIP);
                }

                public String deleteLog(Integer LogId){

                    if (!logrepository.existsById(LogId)){return "Log Not Found! Provide Another Log ID.";}
                    else {
                        logrepository.deleteById(LogId);
                        return "LoG wtih ID " + LogId.toString() + " was successfully Deleted. Delete Another?";
                    }
                }

                public String insertWebLog(String time_date,String source_ip,
                        String log_type,String specific_type,String username, String user_id,
                        BigDecimal http_status,BigDecimal response_size,String referers,
                        String resources,String agents){
                    try {
                        Logs newLog =new Logs(time_date,source_ip,log_type,specific_type,null);
                        logrepository.save(newLog);

                        BigDecimal size= BigDecimal.valueOf(time_date.length() + source_ip.length() + log_type.length() +
                                specific_type.length()+username.length()+user_id.length()+http_status.toString().length()+
                                response_size.toString().length()+referers.length()+resources.length()+agents.length()+9);

                        Integer web_id = logrepository.getLastKey();
                        Web_Logs newWebLog=new Web_Logs(web_id,size,username,user_id,http_status,response_size,referers,resources,agents);
                        weblogrepository.save(newWebLog);
                        return "Log Stored with ID " + web_id + ". Do you Want to Store Another";
                        } catch (ParseException e) {
                        return "Log Could Not Be Stored, Please Provide Correct Values";
                    }
                }

                
                public String insertHDFSLog(String time_date,String source_ip,String log_type,
                        String specific_type,BigDecimal blockid, String destination_ip,
                               BigDecimal size){
                    try {
                        
                        Logs newLog=new Logs(time_date,source_ip,log_type,specific_type,blockid);
                        logrepository.save(newLog);
                        Integer hdfs_id = logrepository.getLastKey();
                        Hdfs_Logs newHdfsLog = new Hdfs_Logs(hdfs_id,destination_ip,size);
                        hdfslogrepository.save(newHdfsLog);

                        return "Log Stored with ID " + hdfs_id + ". Do you Want to Store Another";
                    } catch (ParseException e) {
                        return "Log Could Not Be Stored, Please Provide Correct Values";
                    }

                }

                public String getSearchType(Integer LogId) {
                    if (!logrepository.existsById(LogId)){return "Log Not Found! Provide Another Log ID.";}
                    return logrepository.findById(LogId).get().getLog_type();
                }


                public List<Object> getSearchWeb(Integer LogId){
                       return logrepository.getSearchWeb(LogId);
                };
                
                public List<Object> getSearchHDFS(Integer LogId){
                       return logrepository.getSearchHDFS(LogId);
                };


        public String updateWebLog(Integer log_id,String time_date,String source_ip,String log_type,
                String specific_type,String username,String user_id,BigDecimal http_status,
                                   BigDecimal response_size,String referers,String resources,String agents){
            try {
                BigDecimal size= BigDecimal.valueOf(time_date.length() + source_ip.length()
                        + log_type.length() + specific_type.length()+username.length()
                        +user_id.length()+http_status.toString().length()+
                        response_size.toString().length()+referers.length()+resources.length()+agents.length()+9);
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                java.util.Date parsedDate = dateFormat.parse(time_date);
                Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                Logs updateLog=logrepository.getReferenceById(log_id);
                updateLog.setLog_type(log_type);
                updateLog.setSource_ip(source_ip);
                updateLog.setTime_date(timestamp);
                updateLog.setSpecific_type(specific_type);
                logrepository.save(updateLog);

                Web_Logs updateWebLog = weblogrepository.getReferenceById(log_id);
                updateWebLog.setSize(size);
                updateWebLog.setAgent(agents);
                updateWebLog.setReferers(referers);
                updateWebLog.setResources(resources);
                updateWebLog.setHttp_status(http_status);
                updateWebLog.setResponse_size(response_size);
                updateWebLog.setUsername(username);
                updateWebLog.setUser_id(user_id);
                weblogrepository.save(updateWebLog);

                return "Log with ID " + log_id + " was Succesfully Updated. Do you Want to Update Another ?";
            } catch (ParseException e) {
                return "Log Could Not Be Updtaed, Please Provide Correct Values.";
            }
        }

                public String updateHDFSLog(Integer log_id,String time_date,String source_ip,
                        String log_type,String specific_type,BigDecimal blockid, String destination_ip,
                                BigDecimal size){
                    try {

                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
                    java.util.Date parsedDate = dateFormat.parse(time_date);
                    Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
                    Logs updateLog=logrepository.getReferenceById(log_id);
                    updateLog.setLog_type(log_type);
                    updateLog.setBlockid(blockid);
                    updateLog.setSource_ip(source_ip);
                    updateLog.setTime_date(timestamp);
                    updateLog.setSpecific_type(specific_type);
                    logrepository.save(updateLog);

                    Hdfs_Logs updateHdfsLog = hdfslogrepository.getReferenceById(log_id);
                    updateHdfsLog.setDestination_ip(destination_ip);
                    updateHdfsLog.setSize(size);
                    hdfslogrepository.save(updateHdfsLog);

                    return "Log with ID " + log_id + " was Succesfully Updated. Do you Want to Update Another?";
                    } catch (ParseException e) {
                    return "Log Could Not Be Updtaed, Please Provide Correct Values.";
                    }
                }
}
