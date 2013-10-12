#!/bin/bash

if [ $# -ne 2 ]; then
	echo "USAGE: ./init.sh <CLUSTER_SIZE> <CLUSTER_USER>"
fi

./generateMachineFile.sh $1

if [ $? -ne 0 ]; then
	echo "Error while executing generateMachineFile.sh"
	exit 1
fi

./configClusterJava.sh

if [ $? -ne 0 ]; then
	echo "Error while executing configClusterJava.sh"
	exit 1
fi

./configClusterMPj.sh $2

if [ $? -ne 0 ]; then
	echo "Error while executing configClusterMPj.sh"
	exit 1
fi

./mpjCluster.sh start

if [ $? -ne 0 ]; then
	echo "Error while executing mpjCluster.sh"
	exit 1
fi

echo "The cluster is ready! Now run AlfredMPI!"
exit 0
