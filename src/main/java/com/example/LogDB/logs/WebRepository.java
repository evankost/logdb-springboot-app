
package com.example.LogDB.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface WebRepository extends JpaRepository<Web_Logs,Integer>{         //Interface to interact with the Database relation of Access Logs. In addition, the Interface
                                                                                //contains the calls for the Access Logs related Stored Procedures.
    @Query(value = "select * from logs_length(?1)", nativeQuery = true)
    List<Object> getSizedLogs(Long N);

    @Query(value = "select * from show_browser()", nativeQuery = true)
    List<Object> getBrowser();
    
}
