#!/bin/bash

ampl_dir=~/Downloads/ampl_linux-intel64/

echo Creating symlinks from "$ampl_dir"

for i in $PWD/*
do
    if test -d "$i" 
    then
       ln "$ampl_dir/ampl" "$i/ampl"
       ln "$ampl_dir/cplex" "$i/cplex"
       echo -e 'Done: \t' "$i"
    fi
done
