# Job Decider


SELECT * from test.BATCH_JOB_EXECUTION bje ;


```
JOB_EXECUTION_ID|VERSION|JOB_INSTANCE_ID|CREATE_TIME                  |START_TIME                   |END_TIME                     |STATUS   |EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |JOB_CONFIGURATION_LOCATION|
----------------+-------+---------------+-----------------------------+-----------------------------+-----------------------------+---------+---------+------------+-----------------------------+--------------------------+
               1|      2|              1|2024-09-11 17:56:44.147000000|2024-09-11 17:56:44.182000000|2024-09-11 17:56:44.240000000|COMPLETED|COMPLETED|            |2024-09-11 17:56:44.240000000|                          |
```

SELECT * from test.BATCH_STEP_EXECUTION bse ;


```
STEP_EXECUTION_ID|VERSION|STEP_NAME|JOB_EXECUTION_ID|START_TIME                   |END_TIME                     |STATUS   |COMMIT_COUNT|READ_COUNT|FILTER_COUNT|WRITE_COUNT|READ_SKIP_COUNT|WRITE_SKIP_COUNT|PROCESS_SKIP_COUNT|ROLLBACK_COUNT|EXIT_CODE|EXIT_MESSAGE|LAST_UPDATED                 |
-----------------+-------+---------+----------------+-----------------------------+-----------------------------+---------+------------+----------+------------+-----------+---------------+----------------+------------------+--------------+---------+------------+-----------------------------+
                1|      3|step1    |               1|2024-09-11 17:56:44.202000000|2024-09-11 17:56:44.216000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-11 17:56:44.216000000|
                2|      3|step2    |               1|2024-09-11 17:56:44.228000000|2024-09-11 17:56:44.235000000|COMPLETED|           1|         0|           0|          0|              0|               0|                 0|             0|COMPLETED|            |2024-09-11 17:56:44.236000000|
```