cmake_minimum_required(VERSION 3.26)
project(oceanbase_cdc_demo_4_2_x)

set(CMAKE_CXX_STANDARD 14)

add_executable(main  main.cpp)

# 创建可执行文件，包括源文件 KafkaUtil.cpp
add_executable(kafkaTest  src/KafkaUtil.cpp)

# 设定头文件所在的目录，用于包含头文件
INCLUDE_DIRECTORIES(${PROJECT_SOURCE_DIR}/include)
# 链接库到可执行文件
#find_package(RdKafka REQUIRED)
target_link_libraries(kafkaTest PRIVATE rdkafka++)
