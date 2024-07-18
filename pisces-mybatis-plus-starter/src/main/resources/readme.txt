# druid 数据源配置
spring.datasource.url=jdbc:p6spy:mysql://localhost:3306/your_db?useUnicode=true&characterEncoding=utf-8&allowMultiQueries=true&useSSL=false&serverTimezone=Asia/Shanghai
spring.datasource.username=root
spring.datasource.password=password
spring.datasource.driver-class-name=com.p6spy.engine.spy.P6SpyDriver
spring.datasource.type=com.zaxxer.hikari.HikariDataSource


# --- mybatis-plus start
mybatis-plus.mapper-locations=classpath:/org/shi9/module/**/xml/*Mapper.xml
# 关闭MP3.0自带的banner
mybatis-plus.global-config.banner=false
# 主键类型  0:"数据库ID自增",1:"该类型为未设置主键类型", 2:"用户输入ID",3:"全局唯一ID (数字类型唯一ID)", 4:"全局唯一ID UUID",5:"字符串全局唯一ID (idWorker 的字符串表示)";
mybatis-plus.global-config.db-config.id-type=ASSIGN_ID
# 返回类型为Map,显示null对应的字段
mybatis-plus.configuration.call-setters-on-nulls=true
# 这个配置会将执行的sql打印出来，在开发或测试的时候可以用
mybatis-plus.configuration.log-impl=org.apache.ibatis.logging.stdout.StdOutImpl
# --- mybatis-plus end


<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="xxx.xxx.xxx.mapper.SysLogMapper">
</mapper>
