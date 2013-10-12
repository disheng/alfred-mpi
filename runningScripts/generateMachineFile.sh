#!/bin/bash

if [ "$#" == "0" ]; then
    echo "No arguments provided"
    exit 1
fi

inputnumber=$1
re='^[0-9]+$'
MACHINEFILENAME=machinelist

if ! [[ $inputnumber =~ $re ]] ; then
	echo "Not a number: $inputnumber"
	exit 1
fi

if ! [[ $inputnumber -gt 0 ]] ; then
	echo "$inputnumber must be greater than 0"
	exit 1
fi

if ! [[ $inputnumber -lt 1000 ]] ; then
	echo "$inputnumber must be less than 1000"
	exit 1
fi

if [[ $inputnumber -eq 0 ]] ; then
	echo "master"$'\r' > $MACHINEFILENAME
else
	echo "master"$'\r' > $MACHINEFILENAME
	for i in `seq 1 $(($inputnumber-1))`
	do
		echo `printf "node%03d \n" $i` >> $MACHINEFILENAME
	done
fi

echo "Done generating $MACHINEFILENAME for $inputnumber hosts"

exit 0
