#!/bin/env bash
file=${1}
touch build/output${file}
export LD_LIBRARY_PATH=$PWD/lib:$LD_LIBRARY_PATH
if [ $file  -eq 0 ]
then
		./bin/main < ./run.in > ./build/output"$file"
		cmp -s ./run.out ./build/output"$file"
		if [ $? -eq 0 ]
		then
			echo "resultant output & expected output of run file is same."
		else
			echo "resultant output & expected output of run file is different."
		fi
else
	./bin/main < ./tests/test"$file".in > ./build/output"$file"
	cmp -s ./tests/test"$file".out ./build/output"$file"
	if [ $? -eq 0 ]
	then
		echo "resultant output & expected output of test ${file} is same."
	else
		echo "resultant output & expected output of test ${file} is different."
	fi
fi
