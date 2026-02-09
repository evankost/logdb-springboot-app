

package com.example.LogDB.logs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;
import static jakarta.persistence.GenerationType.SEQUENCE;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;




@Entity(name="logs")
@Getter
@Setter
@NoArgsConstructor
@Table(name = "logs",schema = "public")
public class Logs {                                                             //Class for mapping the common attributes of Access and HDFS Logs to the relevant relation of 
                                                                                //the Database. The blockid is included so as to avoid natural joins in search queries.
    @Id
    @SequenceGenerator(
            name = "logs_log_id_seq",
            sequenceName = "logs_log_id_seq",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "logs_log_id_seq"
    )
    @Column(
            name = "log_id",
            updatable = false
    )
    private Integer log_id;
    
    @Column(
            name = "time_date",
            nullable = false)    
    private Timestamp time_date;
    
    @Column(name = "source_ip") 
    private String source_ip;
    
    @Column(
            name = "log_type",
            nullable = false)      
    private String log_type;
    
     @Column(
            name = "specific_type",
            nullable = false)     
    private String specific_type;
     
    @Column(name = "blockid")       
    private BigDecimal blockid;

    public Logs(String date,String source_ip,String type,String stype,BigDecimal bid) throws ParseException{
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
        java.util.Date parsedDate = dateFormat.parse(date);
        Timestamp timestamp = new java.sql.Timestamp(parsedDate.getTime());
        this.time_date=timestamp;
        this.source_ip=source_ip;
        this.log_type=type;
        this.specific_type=stype;
        this.blockid=bid;
    }    
}
