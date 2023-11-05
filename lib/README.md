# README

## 安装依赖
将jar安装到本地maven仓库
``` 
-- tdsql mysql
mvn install:install-file -Dfile=/Users/liuhe/IdeaProjects/wealth/wealth-data-node/lib/tdsql-mysql-connector-java8-1.5.0.jar -DgroupId=com.tencentcloud.tdsql -DartifactId=tdsql-mysql-connector-java8 -Dversion=1.5.0 -Dpackaging=jar

-- iceberg flink
mvn install:install-file -Dfile=/Users/liuhe/IdeaProjects/wealth/wealth-data-node/lib/iceberg-flink-runtime-1.16-1.3.0.jar -DgroupId=org.apache.iceberg -DartifactId=iceberg-flink-runtime-1.16 -Dversion=1.3.0 -Dpackaging=jar
```