cmake_minimum_required(VERSION 3.20)
project(oceanbase_cdc_demo_4_2_x)

set(CMAKE_CXX_STANDARD 14)

# 设置库文件的链接目录，用于查找库文件
link_directories(/usr/local/lib)

# 创建可执行文件，包括源文件 KafkaUtil.cpp
add_executable(kafkaTest  src/KafkaUtil.cpp)

# 打印变量，调试使用
message("PROJECT_SOURCE_DIR = ${PROJECT_SOURCE_DIR}")

# 设定头文件所在的目录，用于包含头文件
INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR}/include)
# 链接库到可执行文件(rdkafka++在link_directories设置的路径下面)
target_link_libraries(kafkaTest PRIVATE rdkafka++)
