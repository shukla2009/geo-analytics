# Pre-Requsite
    
    => Scalla 2.11.x
    => Java 1.8.x
    => sbt 0.13.15
    
# Note : 


This project contain 3 way to solve the geo-analytics problem

- **Only Scala [S]**

    - Pros
        
        - Data is in memory faster access
        - No other technology required
    
    - Cons
        
        - Data is in main memory so it always had limit cant handle tons of data
        - RealTime data ingestion is difficult 
        
- **Spark with Scala [SS]**

    - Pros 
        
        - Data is in memory and disk as defined by spark configuration can handle much more data then scala solution
        - No DB required
        
    - Cons
        
        - Though we can save data in memory as well as disk but again it had a limit 
        - RealTime data ingestion is difficult
        
- **Cassandra Spark and Scala [CSS]**

    - Pros 
        
        - Infinite scalable with consistent performance
        - Can handle realtime data injestion
        - No data loss 
        - No initial boot time
           
   - Cons 
       
        - Some latency due to db 
        - Initial data migration is required
        
 
# Running
    
**injestion (Optional)**

     
     set avalia.data=<data file location>
     set cassandra.pass=<cassdandra password in application.conf> 
     $/<project> sbt injest/run 
    
**clent**

     set avalia.data=<data file location>
     set cassandra.pass=<cassdandra password in application.conf> (Optional if you want to run cassandra mode) 
     $/<project> sbt client/run 
     
Query

**Find Command** 

                FIND UID1 UID2 SolutionChoice[S|SS|CSS]
        EX:   FIND  26c56675 c9d2e553 CSS