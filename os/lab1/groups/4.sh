#!/bin/env bash
FILE=$1
CHECK=$2
IFS=$'\n'
for line in $(cat ${CHECK})
do
	z=0
	read rl <<< ${line}
	for tag in $(cat ${FILE})
	do
		read grp <<< ${tag}
		if [ ${#grp} -eq 9 ]
		then
			if [ "$rl" = "$grp" ]
			then
				z=$((z+1))
				continue
			fi
		fi
		if [ ${#grp} -eq 19 ]
		then
			if [ "$rl" = "${grp:0:9}" ]
			then
				z=$((z+1))
				continue
			fi
			if [ "$rl" = "${grp:10}" ]
			then
				z=$((z+1))
				continue
			fi
		fi

	done
	if [ $z -eq 0 ]
	then
		echo "$rl"
	fi
done > e.txt
