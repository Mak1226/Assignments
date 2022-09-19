#1/bin/env bash
FILE=$1
CHECK=$2
IFS=$'\n'
for line in $(cat ${CHECK})
do
	read rl <<< ${line}
	for tag in $(cat ${FILE})
	do
		read grp <<< ${tag}
		if [ ${#grp} -eq 19 ]
		then
			if [ "$rl" = "${grp:0:9}" ] || [ "$rl" -eq "${grp:10}" ]
			then
				continue
			else
				echo "$grp"
			fi
		elif [ ${#grp} -eq 9 ]
		then
			if [ "$rl" = "$grp" ]
			then
				continue
			else
				echo "$grp"
			fi
		fi
	done
done > c.txt
