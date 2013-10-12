#!/bin/bash

MACHINEFILE=machinelist

if [[ $EUID -ne 0 ]]; then
   echo "This script must be run as root" 
   exit 1
fi

if [ -f $MACHINEFILE ]
then
	echo "Configuring each machine through ssh"
	cat $MACHINEFILE | while read line; do 
	    echo "Configuring machine: $line"
	    ssh $line "apt-get update && apt-get install openjdk-7-jdk -y && update-alternatives --set java /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java"
	done
else
	echo "No machine file found. Configuring only current"
	apt-get update && apt-get install openjdk-7-jdk -y
	update-alternatives --set java /usr/lib/jvm/java-7-openjdk-amd64/jre/bin/java
fi

exit 0
