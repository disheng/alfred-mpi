#!/bin/bash

HOMEDIR=/home/`whoami`
MACHINEFILENAME=machinelist
MACHINEFILE=$HOMEDIR/$MACHINEFILENAME

MPJ_HOME=$HOMEDIR/mpj-v0_38
MPJ_BIN=$MPJ_HOME/bin
OMPI_LIB=$OMPI_PATH/lib

JAR_FILE=alfred-mpi-0.0.1-SNAPSHOT-mpi.jar
OMPI_MASTER=1
OMPI_SLAVES=2
OMPI_PROC_COUNT=$(($OMPI_MASTER+$OMPI_SLAVES))
JAVA_MEMORY_OPTIONS="-XX:MaxPermSize=128m -Xmx2G"

export $MPJ_HOME
export PATH=$PATH:$MPJ_BIN

if [ -f $MACHINEFILE ]
then
	echo "Starting on multiple machines"
	mpjrun.sh -np $OMPI_PROC_COUNT $JAVA_MEMORY_OPTIONS -dev niodev -machinesfile $MACHINEFILE -jar $JAR_FILE $@
else
	echo "Starting on multicore"
	mpjrun.sh -np $OMPI_PROC_COUNT $JAVA_MEMORY_OPTIONS -jar $JAR_FILE $@
fi