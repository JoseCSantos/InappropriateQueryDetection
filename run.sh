#!/bin/bash
if [ $# -gt 0 ]; then
  java -Xmx4G -ea -classpath "bin/" $*
else
  echo "Your command line contains no arguments"
fi
