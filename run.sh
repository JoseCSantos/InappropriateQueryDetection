#!/bin/bash
lib="lib";
jcuda_home="/home/parth/workspace/JCuda-All-0.5.5-bin-linux-x86_64";
if [ $# -gt 0 ]; then
  java -Xmx28G -Duse_cuda=true -ea -classpath bin/:$lib/trove-3.0.1.jar:$lib/jblas-1.2.3-SNAPSHOT.jar:$lib/terrier-3.5-core.jar:$lib/antlr.jar:$lib/mallet.jar:$lib/opennlp-tools-1.5.0.jar:$lib/maxent-3.0.0.jar:$lib/Nemo-20150912.jar:$lib/hadoop-0.20.2+228-core.jar:$lib/log4j-1.2.15.jar:$lib/snowball-20071024.jar:$lib/trove-2.0.2.jar:$lib/commons-logging-1.1.1.jar:$lib/cl-deep-20150204.jar:$jcuda_home/jcuda-0.5.5.jar:$jcuda_home/jcublas-0.5.5.jar:$lib/jDNN-20160210.jar $*
else
  echo "Your command line contains no arguments"
fi
