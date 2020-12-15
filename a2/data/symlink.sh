#!/bin/bash

ampl_dir=~/Downloads/ampl_linux-intel64/

echo Creating symlinks from "$ampl_dir"

for i in $PWD/*
do
    if test -d "$i" 
    then
       ln -fs "$ampl_dir/ampl" "$i/ampl"
       ln -fs "$ampl_dir/cplex" "$i/cplex"
       ln -fs "$ampl_dir/libcplex12100.so" "$i/libcplex12100.so"
       echo -e 'Done: \t' "$i"
    fi
done
