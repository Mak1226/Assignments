#!/bin/env bash
FILE=$1
CHECK=$2
IFS=$'\n'
for grp in $(cat ${FILE})
do
	read tag <<< ${grp}
	z=0
	for line in $(cat ${CHECK})
	do
		read rl <<< ${line}
		if [ "${#tag}" -eq 9 ]
		then
			if [ "$rl" = "$tag" ]
			then
				z=$((z+1))
				continue
			fi
		elif [ "${#tag}" -eq 19 ]
		then
			if [ "$rl" = "${tag:0:9}" ] || [ "$rl" = "${tag:10}" ]
			then
				z=$((z+1))
				continue
			fi
		fi
	done
	if [[ ${#tag} -eq 9 && $z -ne 1 ]]
	then
		echo "$tag"
	fi
	if  [[ ${#tag} -eq 19 && $z -ne 2 ]]
	then
		echo "$tag"
	fi
done > f.txt
