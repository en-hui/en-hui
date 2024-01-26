# 使用docker安装opengauss

暂不可用 

## Dockerfile
```编译安装
FROM lambdaexpression/centos7.6.1810

WORKDIR /app/opengauss

# 安装构建依赖、下载、编译并安装 GCC 7.3.0 和 CMake 3.16
RUN yum groupinstall -y "Development Tools" && \
    yum install -y wget && \
    cd /usr/src && \
    wget http://www.netgull.com/gcc/releases/gcc-7.3.0/gcc-7.3.0.tar.gz && \
    tar -xf gcc-7.3.0.tar.gz && \
    cd gcc-7.3.0 && \
    ./contrib/download_prerequisites && \
    ./configure --disable-multilib --enable-languages=c,c++ && \
    make -j$(nproc) && \
    make install && \
    cd /usr/src && \
    wget https://github.com/Kitware/CMake/releases/download/v3.16.0/cmake-3.16.0.tar.gz && \
    tar -zxvf cmake-3.16.0.tar.gz && \
    cd cmake-3.16.0 && \
    ./bootstrap && make -j$(nproc) && make install && \
    cd / && \
    yum clean all && \
    rm -rf /usr/src/gcc-7.3.0 /usr/src/cmake-3.16.0 /usr/src/gcc-7.3.0.tar.gz /usr/src/cmake-3.16.0.tar.gz

RUN wget https://opengauss.obs.cn-south-1.myhuaweicloud.com/1.1.0/openGauss-third_party_binarylibs.tar.gz

EXPOSE 5432

CMD ["/usr/sbin/sshd", "-D"]
```

```gpt参考 
FROM ubuntu:20.04

ENV OPENGAUSS_VERSION 5.0.0

RUN apt-get update && apt-get install -y \
    wget \
    libaio1 \
    libnuma1 \
    && rm -rf /var/lib/apt/lists/*


RUN wget https://opengauss.obs.cn-south-1.myhuaweicloud.com/${OPENGAUSS_VERSION}/openGauss-${OPENGAUSS_VERSION}-Ubuntu20.04-x86_64.tar.gz \
    && tar -zxvf openGauss-${OPENGAUSS_VERSION}-Ubuntu20.04-x86_64.tar.gz \
    && ./openGauss-${OPENGAUSS_VERSION}-Ubuntu20.04-x86_64/install.sh


RUN echo "listen_addresses = '*'" >> /yourDataDir/postgresql.conf \
    && echo "host all all 0.0.0.0/0 trust" >> /yourDataDir/pg_hba.conf


CMD ["gs_ctl", "start", "-D", "/yourDataDir", "-M", "primary"]
```
