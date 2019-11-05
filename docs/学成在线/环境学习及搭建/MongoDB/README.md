# MongoDB

## 在CentOS上安装MongoDB

安装步骤：
1. 创建仓库文件: ```vi /etc/yum.repos.d/mongodb-org-3.4.repo```   
2. 复制下面配置，保存退出： 
                ```[mongodb-org-3.4]       
                name=MongoDB Repository     
                baseurl=https://repo.mongodb.org/yum/redhat/$releasever/mongodb-org/3.4/x86_64/     
                gpgcheck=1      
                enabled=1       
                gpgkey=https://www.mongodb.org/static/pgp/server-3.4.asc```   
                       
3. yum安装：  ```yum install -y mongodb-org```     
4. 安装完毕后修改配置文件： ```vi /etc/mongod.conf```      
**修改配置文件的 bind_ip, 默认是 127.0.0.1 只限于本机连接。必须把这个修改为 0.0.0.0 ,否则通过别的机器是没法连接的!**        
**MongoDB默认将数据文件存储在/var/lib/mongo目录，默认日志文件在/var/log/mongodb中。可在此配置文件修改**

启动、停止、重启：
1. 启动命令： ```service mongod start```
2. 停止命令： ```service mongod stop```
3. 重启命令： ```service mongod restart```
4. 查看是否启动成功命令：  
通过查看日志文件```cat /var/log/mongodb/mongod.log```       
启动成功最后一句显示： ```[initandlisten] waiting for connections on 27017```，27017为端口号

## 本地安装 Studio 3T 工具连接数据库
1. 在服务器CentOS进入MongoDB：   ```mongo```
2. 切换为admin数据库```use admin```
3. 创建用户```db.createUser({user:"huenhui",pwd:"huenhui",roles:["root"]})```
4. 重启： ```service mongod  restart```
![Alt](./MongoDBimg/MongoDB连接ip和端口.png)      

![Alt](./MongoDBimg/MongoDB连接用户名和密码.png)

## 基础概念的理解

与关系型数据库概念进行比较，易于理解

|   SQL术语   | MongoDB术语 |                    说明                    |
| :---------: | :---------: | :----------------------------------------: |
|  database   |  database   |                   数据库                   |
|    table    | collection  |               数据库表/集合                |
|     row     |  document   |              数据记录行/文档               |
|   column    |    field    |                数据字段/域                 |
|    index    |    index    |                    索引                    |
| table joins |             |           表连接(MongoDB不支持)            |
| primary key | primary key | 主键，MongoDB自动在每个集合中添加_id的主键 |
|             |             |                                            |

## 使用GridFS进行文件存取
GridFS是MongoDB提供的用于持久化存储文件的模块。      
它将文件分块存储，文件会按照256KB的大小分割成多个块进行存储，GridFS使用两个集合 （collection）存储文件，
一个集合是chunks, 用于存储文件的二进制数据；一个集合是ﬁles，用于存储文件的元数据信息（文件名称、块大小、上传时间等信息）。     
从GridFS中读取文件要对文件的各各块进行组装、合并。
- 配置
```$xslt
@Configuration
public class MongoConfig {

    @Value("${spring.data.mongodb.database}")
    String db;

    @Bean
    public GridFSBucket getGridFSBucket(MongoClient mongoClient) {
        MongoDatabase database = mongoClient.getDatabase(db);
        GridFSBucket bucket = GridFSBuckets.create(database);
        return bucket;
    }
}
```
- 基本操作
```$xslt
    @SpringBootTest(classes = ManageCmsApplication.class)
    @RunWith(SpringRunner.class)
    public class GridFsTest {
    
        @Autowired
        GridFsTemplate gridFsTemplate;
    
        @Autowired
        GridFSBucket gridFSBucket;
    
        //存文件
        @Test
        public void testStore() throws FileNotFoundException {
            //定义file
            File file =new File("d:/test.ftl");
            //定义fileInputStream
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectId objectId = gridFsTemplate.store(fileInputStream, "fileName_test");
            System.out.println(objectId);
        }
    
        //取文件
        @Test
        public void queryFile() throws IOException {
            //根据文件id查询文件
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is("5db983fd44b9c240540b3fc1")));
    
            //打开一个下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource对象，获取流
            GridFsResource gridFsResource = new GridFsResource(gridFSFile,gridFSDownloadStream);
            //从流中取数据
            String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
            System.out.println(content);
    
        }
    
        //删除文件
        public void testDelFile() {
            gridFsTemplate.delete(Query.query(Criteria.where("_id").is("5db983fd44b9c240540b3fc1")));
        }
    }
```



