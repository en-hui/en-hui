# kerberos

- [HDFS集成Kerberos](大数据技术栈/kerberos/HDFS&Kerberos.md)

test/admin@EXAMPLE.COM   
各部分含义   
account / instance @ 域   

## 服务端安装
```text
# 下载安装krb5服务端
yum install -y krb5-server krb5-workstation
# 三个配置文件，基本使用默认
vim /etc/krb5.conf    # 配置域，默认域等
vim /var/kerberos/krb5kdc/kdc.conf   # 默认
vim /var/kerberos/krb5kdc/kadm5.acl  # 默认，即符合/admin@EXAMPLE的用户有*权限
# 初始化数据库
kdb5_util create -s -r EXAMPLE.COM
输入密码：EXAMPLE   然后会生成几个文件：principal  principal.kadm5  principal.kadm5.lock  principal.ok

# 从服务端进入管理后台,不用输入密码（只有服务端可以这样进入）
kadmin.local

# 查看所有的princ
listprincs

# 创建用户:密码都设置为EXAMPLE
addprinc root/admin@EXAMPLE.COM
addprinc test/admin@EXAMPLE.COM

# 启动服务
service krb5kdc restart
service kadmin restart
# 上面的不行试试这个
systemctl restart krb5kdc.service
systemctl restart kadmin.service

# 设置开机自启
chkconfig krb5kdc on
chkconfig kadmin on
```
### 配置文件内容
/etc/krb5.conf
```text
# Configuration snippets may be placed in this directory as well
includedir /etc/krb5.conf.d/

[logging]
 default = FILE:/var/log/krb5libs.log
 kdc = FILE:/var/log/krb5kdc.log
 admin_server = FILE:/var/log/kadmind.log

[libdefaults]
 dns_lookup_realm = false
 ticket_lifetime = 24h
 renew_lifetime = 7d
 forwardable = true
 rdns = false
 pkinit_anchors = FILE:/etc/pki/tls/certs/ca-bundle.crt
# default_realm = EXAMPLE.COM
 default_ccache_name = KEYRING:persistent:%{uid}
 default_realm = EXAMPLE.COM

[realms]
# 只能大写
EXAMPLE.COM = {
 kdc = heh-node01
 admin_server = heh-node01
}

[domain_realm]
# 哪些主机名会被认为到什么域下
.example.com = EXAMPLE.COM
example.com = EXAMPLE.COM
```

/var/kerberos/krb5kdc/kdc.conf
```text
[kdcdefaults]
 kdc_ports = 88
 kdc_tcp_ports = 88

[realms]
 EXAMPLE.COM = {
  #master_key_type = aes256-cts
  acl_file = /var/kerberos/krb5kdc/kadm5.acl
  dict_file = /usr/share/dict/words
  admin_keytab = /var/kerberos/krb5kdc/kadm5.keytab
  supported_enctypes = aes256-cts:normal aes128-cts:normal des3-hmac-sha1:normal arcfour-hmac:normal camellia256-cts:normal camellia128-cts:normal des-hmac-sha1:normal des-cbc-md5:normal des-cbc-crc:normal
 }
```

/var/kerberos/krb5kdc/kadm5.acl
```text
*/admin@EXAMPLE.COM     *
```
## 客户端安装
```text
客户端：
yum -y install krb5-libs krb5-workstation

把服务端的/etc/krb5.conf 复制到客户端的同目录

# 尝试用 新建的test登陆
# kinit hdfs/admin@CMBC —— 需要输入密码    
# kinit -kt xxx.keytab test/hdfs@CMBC —— 不需要密码
kinit test/admin@EXAMPLE
# 输入密码(没有任何输出说明是正常的)：EXAMPLE

# 显示当前登录用户信息
klist

# 登陆kadmin管理后台，需要输入密码
kadmin

# 登陆成功后，和服务端一样，可以用一些命令了
listprincs

# 销毁登录
kdestroy
```
