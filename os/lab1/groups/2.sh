#!/bin/env bash
sort z.txt | uniq > a.txt
sort d.txt | uniq -d >& b.txt
