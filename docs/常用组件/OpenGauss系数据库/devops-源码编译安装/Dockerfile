FROM lambdaexpression/centos7.6.1810

ENV INIT_HOME=/app/opengauss \
    DATA_HOME=/home/omm/data \
    LOG_HOME=/home/omm/log

WORKDIR ${INIT_HOME}

COPY binarylibs ${INIT_HOME}/binarylibs
COPY openGauss-server ${INIT_HOME}/openGauss-server
COPY openGauss-OM ${INIT_HOME}/openGauss-OM

RUN groupadd dbgrp &&\
    useradd omm -g dbgrp -p openGauss123 &&\
    yum -y install -y libaio-devel &&\
    yum -y install -y flex &&\
    yum -y install -y bison &&\
    yum -y install -y ncurses-devel &&\
    yum -y install -y glibc-devel &&\
    yum -y install -y patch &&\
    yum -y install -y readline-devel &&\
    # 分割
    cd ${INIT_HOME}/openGauss-server &&\
    sh build.sh -m debug -3rd ${INIT_HOME}/binarylibs &&\
    cd ${INIT_HOME}/openGauss-OM &&\
    chmod +x build.sh &&\
    sh build.sh -3rd ${INIT_HOME}/binarylibs &&\
    cp ${INIT_HOME}/openGauss-OM/package/openGauss-5.0.1-CentOS-64bit-om.tar.gz ${INIT_HOME}/openGauss-server/mppdb_temp_install/ &&\
    # 分割
    chown -R omm:dbgrp ${INIT_HOME}/openGauss-server

USER omm

RUN mkdir -p ${DATA_HOME} &&\
    mkdir -p ${LOG_HOME} &&\
    export GAUSSHOME=${INIT_HOME}/openGauss-server/mppdb_temp_install &&\
    export LD_LIBRARY_PATH=$GAUSSHOME/lib:$LD_LIBRARY_PATH &&\
    export PATH=$GAUSSHOME/bin:$PATH &&\
    gs_initdb -D ${DATA_HOME} --nodename=db1

EXPOSE 5432
WORKDIR ${DATA_HOME}


CMD ["gs_ctl", "start", "-D", "${DATA_HOME}", "-Z", "single_node", "-l", "${LOG_HOME}/opengauss.log"]

#CMD ["tail", "-f", "/dev/null"]
