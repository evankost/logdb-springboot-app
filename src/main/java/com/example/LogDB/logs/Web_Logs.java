

package com.example.LogDB.logs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity(name="web_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "web_logs",schema = "public")
public class Web_Logs {                                                         //Class for mapping the Access Logs to the relevant relation of the Database. There is
                                                                                //one to one relation with the entries of the Logs relation.
    @Id
    @Column(
            name = "log_id",
            updatable = false,
            nullable = false
    )
    private Integer log_id;
    
    @Column(name = "size")    
    private BigDecimal size;
    
    @Column(name = "username")
    private String username;
    
    @Column(name = " user_id")
    private String user_id;
    
    @Column(
            name = "http_status",
            nullable = false)
    private BigDecimal http_status;
    
    @Column(
            name = "response_size",
            nullable = false)
    private BigDecimal response_size;
    
    @Column(name = "referers")
    private String referers;

    @Column(name = "resources")
    private String resources;
    
    @Column(name = "agent")
    private String agent;       

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(
            name = "log_id",
            nullable = false)
    private Logs log;

    public Web_Logs(Integer log_id, BigDecimal size, String username, String user_id, BigDecimal http_status, BigDecimal response_size, String referers, String resources, String agent) {
        this.log_id = log_id;
        this.size = size;
        this.username = username;
        this.user_id = user_id;
        this.http_status = http_status;
        this.response_size = response_size;
        this.referers = referers;
        this.resources = resources;
        this.agent = agent;
    }
    
    
}
