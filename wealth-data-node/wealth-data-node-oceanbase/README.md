# oceanbase-cdc-demo-4.2.x

> 语言：c++   
> ide:CLion   

### CMakeLists.txt 文件介绍
``` 
# 设置所需的CMake的最低版本
CMAKE_MINIMUM_REQUIRED(VERSION 3.20)

# 定义项目的名称和支持的语言
PROJECT(oblogAgent)

# 设置C++标准为C++14
SET(CMAKE_CXX_STANDARD 14)

# 输出编译详细信息
SET(CMAKE_VERBOSE_MAKEFILE ON)

# 添加编译选项，包括线程支持、警告选项、调试选项等
ADD_COMPILE_OPTIONS("-pthread" "-W" "-Wall" "-O0" "-g" "-Wpedantic" "-Wextra" "-fexceptions" "$<$<CONFIG:DEBUG>:-O0;-g3;-ggdb>" "-march=native")

# 设置可执行文件的输出目录
SET(EXECUTABLE_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/bin)

# 设置库文件的输出目录
SET(LIBRARY_OUTPUT_PATH ${PROJECT_SOURCE_DIR}/lib)

# 设定头文件所在的目录，用于包含头文件
INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR}/include/drcmessage ${PROJECT_SOURCE_DIR}/include/oblog ${PROJECT_SOURCE_DIR}/include/kafka ${PROJECT_SOURCE_DIR}/include/rapidjson ${PROJECT_SOURCE_DIR}/include/base64 ${PROJECT_SOURCE_DIR}/include/parser ${PROJECT_SOURCE_DIR}/include/log)

# 设置库文件的链接目录，用于查找库文件
LINK_DIRECTORIES(${PROJECT_SOURCE_DIR}/lib/drcmessage/ ${PROJECT_SOURCE_DIR}/lib/oblog/ /usr/lib/x86_64-linux-gnu /usr/lib/ /usr/local/lib /usr/local)

# 创建可执行文件 oblogAgent，包括源文件 obparse.cpp、producer.cpp 和 sql_ddl_parser.cpp
ADD_EXECUTABLE(oblogAgent src/obparse.cpp src/producer.cpp src/sql_ddl_parser.cpp)

# 创建可执行文件 oblogAgentValidate，包括源文件 obparsetest.cpp
ADD_EXECUTABLE(oblogAgentValidate src/obparsetest.cpp)

# 链接库到 oblogAgent 可执行文件，包括 ssl、crypto、pthread、rdkafka++ 和 liboblog.so
TARGET_LINK_LIBRARIES(oblogAgent ssl crypto pthread rdkafka++ liboblog.so)

# 链接库到 oblogAgentValidate 可执行文件，包括 liboblog.so
TARGET_LINK_LIBRARIES(oblogAgentValidate liboblog.so)
```

### c++使用kafka案例

类库使用说明：https://github.com/confluentinc/librdkafka   
头文件下载：https://github.com/confluentinc/librdkafka/blob/master/src-cpp/rdkafkacpp.h   

> 1、mac os 上面安装库：brew install librdkafka      
> 2、在CMakeList中设置头文件路径头（头文件下载放到工程目录下）：INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR}/include)     
> 3、在CMakeList中配置库的路径：link_directories(/usr/local/lib)   
> 4、在CMakeList中链接librdkafka库：target_link_libraries(kafkaTest PRIVATE rdkafka++)     
