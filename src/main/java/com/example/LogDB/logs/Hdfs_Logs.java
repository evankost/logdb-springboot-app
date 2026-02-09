

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


@Entity(name="hdfs_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "hdfs_logs",schema = "public")
public class Hdfs_Logs {                                                        //Class for mapping the HDFS Logs to the relevant relation of the Database. There is
                                                                                //one to one relation with the entries of the Logs relation.
    @Id
    @Column(
            name = "log_id",
            updatable = false,
            nullable = false
    )
    private Integer log_id;
    
    @Column(name = "destination_ip")
    private String destination_ip; 
    
    @Column(name = "size")
    private BigDecimal size;  

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(
            nullable = false,
            name = "log_id")
    private Logs log;

    public Hdfs_Logs(Integer log_id, String destination_ip, BigDecimal size) {
        this.log_id = log_id;
        this.destination_ip = destination_ip;
        this.size = size;
    }
}
