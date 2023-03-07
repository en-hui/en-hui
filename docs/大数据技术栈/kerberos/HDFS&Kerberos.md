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



## 配置HDFS
### linux添加用户   
> groupadd hadoop;useradd hdfs -g hadoop -p hdfs;   

### 配置HDFS相关的Kerberos账户
> 说明：Hadoop需要Kerberos来进行认证，以启动服务来说，在后面配置hadoop的时候我们会给对应服务指定一个kerberos的账户，   
> 比如namenode运行在node01机器，我们可能将namenode指定给nn/node01@CMBC 这个账户，那么想要启动namenode就必须认证这个账户才可以。   

1、在每个客户端节点创建目录：mkdir /etc/security/keytabs
2、配置node01上面运行的服务对应的kerberos账户
> 执行kadmin   
> 输入密码，进入admin后台   
> 
> 创建账户    
> addprinc -randkey hdfs/node01@CMBC
> 
> 防止启动或操作的过程中需要输入密码，创建免密登陆的keytab文件   
> 创建keytab   
> ktadd -k /etc/security/keytabs/hdfs.keytab hdfs/node01@CMBC    
> 
> 修改权限    
> chmod 400 hdfs.keytab    

### 配置core-site.xml
``` 
<configuration>
  <property>
    <name>fs.defaultFS</name>
    <value>hdfs://heh-node01:9000</value>
    <description>给出NameNode在哪里启动，ip、端口,尽量不要用localhost</description>
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
    </value>
    <description>
        匹配规则，比如第一行就是表示将 hdfs/*.CMBC 的principal绑定到hdfs账户上,
        也就是想要得到一个认证后的hdfs账户，请使用Kerberos的hdfs/*@CMBC账户来认证
        本地搭建都用的root，所以hdfs改为root）
    </description>
  </property>
</configuration>
```

### 配置hdfs-site.xml
``` 
  <property>
    <name>dfs.block.access.token.enable</name>
    <value>true</value>
  </property>
  <property>
    <name>dfs.namenode.kerberos.principal</name>
    <value>hdfs/_HOST@CMBC</value>
    <description>namenode对应的kerberos账户，_HOST会自动转换为主机名</description>
  </property>
  <property>
    <name>dfs.namenode.keytab.file</name>
    <value>/etc/security/keytabs/hdfs.keytab</value>
    <description>
        因为使用-randkey 创建的用户，密码随机不知道，
        所以用免密登陆的keytab文件，指定namenode需要用的keytab文件在哪里
    </description>
  </property>
  
  <property>
    <name>dfs.secondary.namenode.kerberos.principal</name>
    <value>hdfs/_HOST@CMBC</value>
    <description>secondary namenode 使用的账户</description>
  </property>
  <property>
    <name>dfs.secondary.namenode.keytab.file</name>
    <value>/etc/security/keytabs/hdfs.keytab</value>
  </property>
  
  <property>
    <name>dfs.datanode.kerberos.principal</name>
    <value>hdfs/_HOST@CMBC</value>
    <description>datanode 使用的账户</description>
  </property>
  <property>
    <name>dfs.datanode.keytab.file</name>
    <value>/etc/security/keytabs/hdfs.keytab</value>
  </property>
```