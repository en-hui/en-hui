FROM lambdaexpression/centos7.6.1810

ENV INIT_HOME=/app/opengauss
ENV DATA_HOME=/home/omm/data
ENV LOG_HOME=/home/omm/log
ENV GAUSSLOG=/home/omm/log
ENV GAUSSHOME=${INIT_HOME}/openGauss-server/mppdb_temp_install
ENV GS_CLUSTER_NAME=GS_CLUSTER_NAME
ENV GAUSS_WARNING_TYPE=GAUSS_WARNING_TYPE
ENV LD_LIBRARY_PATH=$GAUSSHOME/lib:$LD_LIBRARY_PATH
ENV PATH=$GAUSSHOME/bin:$PATH

WORKDIR ${INIT_HOME}

RUN yum -y update && \
    yum -y install libaio-devel flex bison ncurses-devel glibc-devel patch readline-devel && \
    yum clean all && \
    rm -rf /var/cache/yum

COPY binarylibs ${INIT_HOME}/binarylibs
COPY openGauss-server ${INIT_HOME}/openGauss-server
COPY openGauss-OM ${INIT_HOME}/openGauss-OM
COPY start.sh /start.sh

RUN groupadd dbgrp &&\
    useradd omm -g dbgrp -p openGauss123 &&\
    # 分割
    cd ${INIT_HOME}/openGauss-server &&\
    sh build.sh -m debug -3rd ${INIT_HOME}/binarylibs &&\
    cd ${INIT_HOME}/openGauss-OM &&\
    chmod +x build.sh &&\
    sh build.sh -3rd ${INIT_HOME}/binarylibs &&\
    cp ${INIT_HOME}/openGauss-OM/package/openGauss-5.0.1-CentOS-64bit-om.tar.gz ${INIT_HOME}/openGauss-server/mppdb_temp_install &&\
    # 分割
    chown -R omm:dbgrp ${INIT_HOME}/openGauss-server &&\
    chown omm:dbgrp /start.sh

USER omm

RUN mkdir -p ${DATA_HOME} &&\
    mkdir -p ${LOG_HOME} &&\
    chmod +x /start.sh &&\
    gs_initdb -D ${DATA_HOME} --nodename=db1


CMD ["/start.sh"]

# CMD ["gs_ctl", "start", "-D", "/home/omm/data", "-Z", "single_node", "-l", "/home/omm/log/opengauss.log"]
# gs_ctl start -D /home/omm/data -Z single_node -l /home/omm/log/opengauss.log
# CMD ["tail", "-f", "/dev/null"]
