# HDFS&Kerberos

## Kerberos
krb5.conf
```
includedir /etc/krb5.conf.d/

[logging]
 default = FILE:/var/log/krb5libs.log
 kdc = FILE:/var/log/krb5kdc.log
 admin_server = FILE:/var/log/kadmind.log

[libdefaults]
 default_realm = CMBC
 dns_lookup_realm = false
 dns_lookup_kdc = false
 ticket_lifetime = 24h
 renew_lifetime = 7d
 forwardable = true
 udp_preference_limit = 1
 default_ccache_name = FILE:/tmp/krb5cc_%{uid}

[realms]
 CMBC = {
  kdc = heh-node01
  admin_server = heh-node01
 }
```

kdc.conf
``` 
[kdcdefaults]
 kdc_ports = 88
 kdc_tcp_ports = 88
 kadmind_port = 749

[realms]
 CMBC = {
  max_life = 10h 0m 0s
  max_renewable_life = 7d 0h 0m 0s
 }
```

kadm5.acl
``` 
*/admin@CMBC    *
```


## 配置HDFS
### linux添加用户   
> 本教程不用这个了，直接root操作    
> groupadd hadoop;useradd hdfs -g hadoop -p hdfs;   

### 配置HDFS相关的Kerberos账户
> 说明：Hadoop需要Kerberos来进行认证，以启动服务来说，在后面配置hadoop的时候我们会给对应服务指定一个kerberos的账户，   
> 比如namenode运行在heh-node01机器，我们可能将namenode指定给hdfs/heh-node01@CMBC 这个账户，那么想要启动namenode就必须认证这个账户才可以。   

1、在每个客户端节点创建目录：mkdir /etc/security/keytabs
2、配置heh-node01上面运行的服务对应的kerberos账户
> 执行kadmin   
> 输入密码，进入admin后台   
> 
> 创建账户    
> addprinc -randkey hdfs/heh-node01@CMBC
> 
> 防止启动或操作的过程中需要输入密码，创建免密登陆的keytab文件   
> 创建keytab   
> ktadd -k /etc/security/keytabs/hdfs.keytab hdfs/heh-node01@CMBC    
> 
> 修改权限    
> chmod 400 hdfs.keytab    

### 配置core-site.xml
```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://heh-node01:9000</value>
    <description>给出NameNode在哪里启动，ip、端口,尽量不要用localhost</description>
  </property>
  <property>
    <name>io.bile.buffer.size</name>
    <value>131072</value>
  </property>    
  <property>
    <name>hadoop.security.authorization</name>
    <value>true</value>
    <description>是否开启hadoop的安全认证</description>
  </property>
  <property>
    <name>hadoop.security.authentication</name>
    <value>kerberos</value>
    <description>使用kerberos作为hadoop的安全认证</description>
  </property>
  <property>
    <name>hadoop.security.auth_to_local</name>
    <value>
        RULE:[2:$1@$0](hdfs@.*CMBC)s/.*/root/
        DEFAULT
    </value>
    <description> 
        匹配规则，比如第一行就是表示将 hdfs/*.CMBC 的principal绑定到hdfs账户上,
        也就是想要得到一个认证后的hdfs账户，请使用Kerberos的hdfs/*@CMBC账户来认证
        本地搭建都用的root，所以hdfs改为root）
    </description>
  </property>
  <property>
      <name>hadoop.proxyuser.hdfs.hosts</name>
      <value>*</value>
  </property>
  <property>
      <name>hadoop.proxyuser.hdfs.groups</name>
      <value>*</value>
  </property>
  <property>
      <name>hadoop.proxyuser.HTTP.hosts</name>
      <value>*</value>
  </property>
  <property>
      <name>hadoop.proxyuser.HTTP.groups</name>
      <value>*</value>
  </property>
</configuration>

```

### 配置hdfs-site.xml

https://hadoop.apache.org/docs/r2.10.1/hadoop-project-dist/hadoop-common/SecureMode.html#Secure_DataNode

```xml
<?xml version="1.0" encoding="UTF-8"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<configuration>
    <property>
        <name>dfs.replication</name>
        <!-- 伪分布式，副本数量设置为了1 -->
        <value>1</value>
    </property>
    <property>
        <name>dfs.namenode.name.dir</name>
        <!-- NameNode 数据存放路径 -->
        <value>/var/bigdata/hadoop/local/dfs/name</value>
    </property>
    <property>
        <name>dfs.datanode.data.dir</name>
        <!-- NameNode 数据存放路径 -->
        <value>/var/bigdata/hadoop/local/dfs/data</value>
    </property>
    <property>
        <name>dfs.namenode.secondary.http-address</name>
        <!-- SecondaryNode 在哪里启动,不设置也会在本机启动 -->
        <value>heh-node01:50090</value>
    </property>
    <property>
        <name>dfs.namenode.checkpoint.dir</name>
        <!-- SecondaryNode 数据存放路径 -->
        <value>/var/bigdata/hadoop/local/dfs/secondary</value>
    </property>

    <property>
        <name>dfs.block.access.token.enable</name>
        <value>true</value>
    </property>

    <!-- namenode security config-->
    <property>
        <name>dfs.namenode.keytab.file</name>
        <value>/etc/security/keytabs/hdfs.keytab</value>
        <description>keytab 免密</description>
    </property>
    <property>
        <name>dfs.namenode.kerberos.principal</name>
        <value>hdfs/_HOST@CMBC</value>
        <description>namenode对应的kerberos账户，_HOST会自动转换为主机名</description>
    </property>
    <property>
        <name>dfs.namenode.kerberos.internal.spnego.principal</name>
        <value>hdfs/_HOST@CMBC</value>
    </property>
    <!-- secondary namenode security config-->
    <property>
        <name>dfs.secondary.namenode.kerberos.principal</name>
        <value>hdfs/_HOST@CMBC</value>
    </property>
    <property>
        <name>dfs.secondary.namenode.keytab.file</name>
        <value>/etc/security/keytabs/hdfs.keytab</value>
    </property>
    <property>
        <name>dfs.secondary.namenode.kerberos.internal.spnego.principal</name>
        <value>hdfs/_HOST@CMBC</value>
    </property>
    <!-- datanode security config-->
    <property>
        <name>dfs.datanode.keytab.file</name>
        <value>/etc/security/keytabs/hdfs.keytab</value>
    </property>
    <property>
        <name>dfs.datanode.kerberos.principal</name>
        <value>hdfs/_HOST@CMBC</value>
    </property>


    <property>
        <name>dfs.datanode.data.dir.perm</name>
        <value>700</value>
    </property>
    
    <property>
        <name>dfs.web.authentication.kerberos.principal</name>
        <value>hdfs/_HOST@CMBC</value>
    </property>
    <property>
        <name>dfs.web.authentication.kerberos.keytab</name>
        <value>/etc/security/keytabs/hdfs.keytab</value>
    </property>
    <property>
        <name>dfs.permissions.supergroup</name>
        <value>root</value>
    </property>

    <property>
        <name>dfs.data.transfer.protection</name>
        <value>integrity</value>
    </property>
    <property>
        <name>dfs.http.policy</name>
        <value>HTTPS_ONLY</value>
    </property>
    <property>
        <name>dfs.https.port</name>
        <value>50470</value>
        <description>访问地址：https://heh-node01:50470/dfshealth.html#tab-overview</description>
    </property>
</configuration>
```

### https 证书配置（密码全用123456）
> mkdir /etc/security/cdh.https   
> cd /etc/security/cdh.https    
> 
> openssl req -new -x509 -keyout bd_ca_key -out bd_ca_cert -days 9999 -subj '/C=CN/ST=beijing/L=beijing/0=test/OU=test/CN=test'    
> (输入密码和确认密码123456，此命令成功后输出bd_ca_key和bd_ca_cert两个文件)      
>  
> 
> cd /etc/security/cdh.https    
> 
> --1.输入密码后，此命令成功输出keystore文件   
> keytool -keystore keystore -alias localhost -validity 9999 -genkey -keyalg RSA -keysize 2048 -dname "CN=test, OU=test, O=test, L=beijing, ST=beijing, C=CN"   
> 
> --2.输入密码后，提示是否信任证书，输入yes，此命令成功后输出truststore文件    
> keytool -keystore truststore -alias CARoot -import -file bd_ca_cert    
> 
> --3.输入密码后，此命令成功后输出cert文件   
> keytool -certreq -alias localhost -keystore keystore -file cert    
> 
> --4.此命令成功后输出cert_signed文件   
> openssl x509 -req -CA bd_ca_cert -CAkey bd_ca_key -in cert -out cert_signed -days 9999 -CAcreateserial -passin pass:123456   
> 
> --5.输入命令后，提示是否信任证书，输入yes，此命令成功后更新keystore文件   
>  keytool -keystore keystore -alias CARoot -import -file bd_ca_cert  
> 
> --6.输入密码   
> keytool -keystore keystore -alias localhost -import -file cert_signed   
>  
> 最终得到的文件：    
> -rw-r--r-- 1 root root 1257 Mar  8 14:03 bd_ca_cert     
> -rw-r--r-- 1 root root   17 Mar  8 14:15 bd_ca_cert.srl     
> -rw-r--r-- 1 root root 1834 Mar  8 14:03 bd_ca_key     
> -rw-r--r-- 1 root root 1081 Mar  8 14:14 cert     
> -rw-r--r-- 1 root root 1159 Mar  8 14:15 cert_signed     
> -rw-r--r-- 1 root root 3986 Mar  8 14:15 keystore     
> -rw-r--r-- 1 root root  950 Mar  8 14:14 truststore     

### 配置ssl-server.xml和ssl-client.xml
> cd /opt/bigdata/hadoop-2.10.1/etc/hadoop    
> 有两个样例文件：ssl-client.xml.example ssl-server.xml.example    
>
> cp ssl-server.xml.example ssl-server.xml
> cp ssl-client.xml.example ssl-client.xml 

ssl-server.xml

```xml
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<configuration>

<property>
  <name>ssl.server.truststore.location</name>
  <value>/etc/security/cdh.https/truststore</value>
  <description>Truststore to be used by NN and DN. Must be specified.
  </description>
</property>

<property>
  <name>ssl.server.truststore.password</name>
  <value>123456</value>
  <description>Optional. Default value is "".
  </description>
</property>

<property>
  <name>ssl.server.truststore.type</name>
  <value>jks</value>
  <description>Optional. The keystore file format, default value is "jks".
  </description>
</property>

<property>
  <name>ssl.server.truststore.reload.interval</name>
  <value>10000</value>
  <description>Truststore reload check interval, in milliseconds.
  Default value is 10000 (10 seconds).
  </description>
</property>

<property>
  <name>ssl.server.keystore.location</name>
  <value>/etc/security/cdh.https/keystore</value>
  <description>Keystore to be used by NN and DN. Must be specified.
  </description>
</property>

<property>
  <name>ssl.server.keystore.password</name>
  <value>123456</value>
  <description>Must be specified.
  </description>
</property>

<property>
  <name>ssl.server.keystore.keypassword</name>
  <value>123456</value>
  <description>Must be specified.
  </description>
</property>

<property>
  <name>ssl.server.keystore.type</name>
  <value>jks</value>
  <description>Optional. The keystore file format, default value is "jks".
  </description>
</property>

<property>
  <name>ssl.server.exclude.cipher.list</name>
  <value>TLS_ECDHE_RSA_WITH_RC4_128_SHA,SSL_DHE_RSA_EXPORT_WITH_DES40_CBC_SHA,
  SSL_RSA_WITH_DES_CBC_SHA,SSL_DHE_RSA_WITH_DES_CBC_SHA,
  SSL_RSA_EXPORT_WITH_RC4_40_MD5,SSL_RSA_EXPORT_WITH_DES40_CBC_SHA,
  SSL_RSA_WITH_RC4_128_MD5</value>
  <description>Optional. The weak security cipher suites that you want excluded
  from SSL communication.</description>
</property>

</configuration>
```

ssl-client.xml
```xml
<?xml version="1.0"?>
<?xml-stylesheet type="text/xsl" href="configuration.xsl"?>
<!--
   Licensed to the Apache Software Foundation (ASF) under one or more
   contributor license agreements.  See the NOTICE file distributed with
   this work for additional information regarding copyright ownership.
   The ASF licenses this file to You under the Apache License, Version 2.0
   (the "License"); you may not use this file except in compliance with
   the License.  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
-->
<configuration>

<property>
  <name>ssl.client.truststore.location</name>
  <value>/etc/security/cdh.https/truststore</value>
  <description>Truststore to be used by clients like distcp. Must be
  specified.
  </description>
</property>

<property>
  <name>ssl.client.truststore.password</name>
  <value>123456</value>
  <description>Optional. Default value is "".
  </description>
</property>

<property>
  <name>ssl.client.truststore.type</name>
  <value>jks</value>
  <description>Optional. The keystore file format, default value is "jks".
  </description>
</property>

<property>
  <name>ssl.client.truststore.reload.interval</name>
  <value>10000</value>
  <description>Truststore reload check interval, in milliseconds.
  Default value is 10000 (10 seconds).
  </description>
</property>

<property>
  <name>ssl.client.keystore.location</name>
  <value>/etc/security/cdh.https/keystore</value>
  <description>Keystore to be used by clients like distcp. Must be
  specified.
  </description>
</property>

<property>
  <name>ssl.client.keystore.password</name>
  <value>123456</value>
  <description>Optional. Default value is "".
  </description>
</property>

<property>
  <name>ssl.client.keystore.keypassword</name>
  <value>123456</value>
  <description>Optional. Default value is "".
  </description>
</property>

<property>
  <name>ssl.client.keystore.type</name>
  <value>jks</value>
  <description>Optional. The keystore file format, default value is "jks".
  </description>
</property>

</configuration>
```

## 启动hdfs服务 
kinit hdfs/heh-node01@CMBC —— 需要输入密码    
kinit -kt /etc/security/keytabs/hdfs.keytab hdfs/heh-node01@CMBC —— 不需要密码

> hadoop-daemon.sh start namenode —— tail -f /opt/bigdata/hadoop-2.10.1/logs/hadoop-root-namenode-heh-node01.log    
> hadoop-daemon.sh start secondarynamenode —— tail -f hadoop-root-secondarynamenode-heh-node01.log    
> hadoop-daemon.sh start datanode —— tail -f hadoop-root-datanode-heh-node01.log


