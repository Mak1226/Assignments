#!/bin/env bash
var=$1
if [ -n "$1" ]
then
	cd "$1"
fi
touch list.txt
for line in *
do
	if [ -f "$line" ] && [ "$line" != "list.txt" ]
	then
		if [ -L "$line" ]
		then
			continue
		else
			echo "$line"
		fi
	fi
done > list.txt
total=$(wc -l < list.txt)
for (( i=1; i<$total; i++ ))
do
	file=$(head -n $i list.txt | tail -1)
	if [ -f "$file" ]
	then
		if [ -L "$file" ]
		then
			continue
		fi
		for (( j=((i+1)); j<=$total; j++ ))
		do
			copy=$(head -n $j list.txt | tail -1)
			if [ -f "$copy" ]
			then
				if [ -L "$copy" ]
				then
					continue
				fi
				cmp -s "$file" "$copy"
				if [ $? -eq 0 ]
				then
					rm "$copy"
					ln -s "$file" "$copy"
				fi
			fi
		done
	fi
done
rm list.txt
