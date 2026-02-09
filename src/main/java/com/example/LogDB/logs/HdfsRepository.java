
package com.example.LogDB.logs;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.List;


public interface HdfsRepository extends JpaRepository<Hdfs_Logs,Integer>{       //Interface to interact with the Database relation of HDFS Logs. In addition, the Interface
                                                                                //contains the calls for the HDFS Logs related Stored Procedures.
    @Query(value = "select * from count_resources()", nativeQuery = true)
    List<Object> getResources();

    @Query(value = "select * from count_top_resources()", nativeQuery = true)
    List<Object> getTopResources();
}
