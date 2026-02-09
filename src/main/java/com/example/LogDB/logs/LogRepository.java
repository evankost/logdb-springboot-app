
package com.example.LogDB.logs;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;




public interface LogRepository extends JpaRepository<Logs,Integer>{             //Interface to interact with the Database relation of HDFS Logs. In addition, the Interface
                                                                                //contains the calls for the (Common Atribbute) Logs related Stored Procedures.In additition,
                                                                                //it contains the calls for the calls tha involve two or three Log oriented Relations.
    @Query(value = "select * from count_types(?1,?2)", nativeQuery = true)
    List<Object> getTypes(Timestamp sDate,Timestamp eDate);
    
    @Query(value = "select * from count_type(?1,?2,?3)", nativeQuery = true)
    List<Object> getLogs(String type,Timestamp sDate,Timestamp eDate);

    @Query(value = "select * from count_source_ip(?1)", nativeQuery = true)
    List<Object> logIp(Date date);

    @Query(value = "select * from count_blocks(?1,?2)", nativeQuery = true)
    List<Object> blockCount(Date sDate,Date eDate);

    @Query(value = "select * from day_replicated_served()", nativeQuery = true)
    List<Object>dayReplicatedServed();

    @Query(value = "select * from hour_replicated_served()", nativeQuery = true)
    List<Object>hourReplicatedServed();

    @Query(value = "select * from search_source_one_method(?1,?2,?3)", nativeQuery = true)
    public List<Object> httpMethodOne(String method,Timestamp date_one,Timestamp date_two);

    @Query(value = "select * from search_source_two_method(?1,?2,?3,?4)", nativeQuery = true)
    public List<Object> httpMethodTwo(String method_one,String method_two,Timestamp date_one,Timestamp date_two);

    @Query(value = "select * from search_source_four_method(?1,?2)", nativeQuery = true)
    public List<Object> httpMethodFour(Timestamp date_one,Timestamp date_two);

    @Query(value = "select * from search_ip(?1)", nativeQuery = true)
    public List<Object> findByIP(String searchIP);

    @Query(value="select pg_sequence_last_value('public.logs_log_id_seq')")
    Integer getLastKey();
    
    @Query(value = "select * from get_search_web(?1)", nativeQuery = true)
    public List<Object> getSearchWeb(Integer LogId);
    
    @Query(value = "select * from get_search_hdfs(?1)", nativeQuery = true)
    public List<Object> getSearchHDFS(Integer LogId);
}
