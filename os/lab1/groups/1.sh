#!/bin/env bash
FILE=$1
#IFS=$'\n'
list ()
{
	if [ -n "$1" ]
	then
		echo "$1"
	fi
} >> d.txt
c=0
for line in $(cat ${FILE})
do
	IFS=','
	if [ $c -gt 0 ]
	then
		read t u s1 s2 <<< ${line}
		s1=${s1:1:-1}
		s2=${s2:1:-1}
		list "$s1"
		list "$s2"

		if [ "$s1" \< "$s2" ]
		then
			if [[ -n "$s1" && -n "$s2" ]]
			then
				echo "$s1"_"$s2"
			else
				echo "$s1$s2"
			fi
		elif [ "$s1" \> "$s2" ]
		then
			if [[ -n "$s1" && -n "$s2" ]]
			then
				echo "$s2"_"$s1"
			else
				echo "$s2$s1"
			fi
		fi
	fi
	c=$((c+1))
done > z.txt
