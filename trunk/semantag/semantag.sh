#!/bin/sh
#
# Run a jelly script
#
# Author:   Niko Schmuck
# Author:   CF
#


CP=./target/classes

collectCP(){
  
  #for i in "$1"/*.jar
  for i in "$1"/*
  do
    if [ -d "$i" ] ; then
      # recurse into subdirs
      #echo "subdir: $i"
      collectCP $i
    elif [ `echo "$i" | sed -n /\\.jar$/p` ] ; then   
      #echo " jar: $i"
      if [ -f "$i" ] ; then
        if [ -z "$CP" ] ; then
          CP="$i"
        else
          CP="$i":"$CP"
        fi
      fi
    fi  
  done
}


LIB=lib
collectCP $LIB 

# ===============================
VM_ARGS=
JAVA=$JAVA_HOME/bin/java
# run jelly
# echo $CP
$JAVA -Djava.util.logging.config.file=src/conf/logging.properties -classpath $CP $VM_ARGS org.apache.commons.jelly.Jelly $*
