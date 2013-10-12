#!/bin/bash

MPJ_BOOT=`which mpjboot`
MPJ_HALT=`which mpjhalt`
MACHINEFILENAME=machinelist
SCRIPT_NAME=`basename $0`

if [ -z "$MPJ_BOOT" ] || [ -z "$MPJ_HALT" ]
then
	echo "mpjboot or mpjhalt executable not found."
	exit 1
fi

if ! [ -f $MACHINEFILENAME ]
then
        echo "$MACHINEFILENAME not found."
        exit 1
fi

parse_script_option_list () {
	case "$1" in
		start)
			echo "Starting MPJ cluster"
			$MPJ_BOOT $MACHINEFILENAME
		;;
		stop)
			echo "Stopping MPJ cluster"
			$MPJ_HALT $MACHINEFILENAME
		;;
		*)
            cat << EOF >&2
Usage: $SCRIPT_NAME {start|stop|restart|force-reload|status}
EOF
            exit 1
            ;;
    esac
}

parse_script_option_list $@

exit 0
