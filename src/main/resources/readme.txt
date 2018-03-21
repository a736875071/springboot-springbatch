spring batch 默认项目启动job,如果不写将默认启动全部job,不想默认启动可以在配置中添加
spring.batch.job.names=xxx,没有的job,这样项目启动时将不会执行job


spring.batch.job.names = job1,job2 #启动时要执行的Job，默认执行全部Job

spring.batch.job.enabled=true #是否自动执行定义的Job，默认是

spring.batch.initializer.enabled=true #是否初始化Spring Batch的数据库，默认为是

spring.batch.schema=

spring.batch.table-prefix= #设置SpringBatch的数据库表的前缀
