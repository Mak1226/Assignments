#!/bin/env bash
for i in {a..f}
do
	true > "$i.txt"
	cat "$i.txt"
done
