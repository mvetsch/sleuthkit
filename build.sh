#!/bin/bash

cd bindings/java
make
ant clean
ant


cd -
./bootstrap
./configure --prefix=/root/tsk --with-libewf=/root/tsk
make
make install


cp -v /root/tsk/share/java/Tsk_DataModel.jar /root/tsk/bindings/java/dist/Tsk_DataModel_PostgreSQL.jar 
cp -v /root/tsk/share/java/Tsk_DataModel.jar /root/tsk/bindings/java/dist/Tsk_DataModel.jar 
