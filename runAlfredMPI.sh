#!/bin/sh

OMPI_PATH=/home/furio/ompi-1.7
OMPI_BIN=$OMPI_PATH/bin
OMPI_LIB=$OMPI_PATH/lib

JAR_FILE=alfred-mpi-0.0.1-SNAPSHOT-mpi.jar
OMPI_PROC_COUNT=5

export PATH=$OMPI_BIN:$PATH
export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:$OMPI_LIB

mpirun -n $OMPI_PROC_COUNT java -d64 -Djava.library.path=$OMPI_LIB -jar $JAR_FILE $@
