#!/bin/sh
#
# Run a jelly script
#
# Author:   Niko Schmuck
# Version:  $Id: jelly.sh,v 1.1 2004/08/24 00:12:26 niko_schmuck Exp $
#

CP=./target/classes

collectCP(){
  for i in "$1"/*.jar
  do
    if [ -f "$i" ] ; then
        if [ -z "$CP" ] ; then
      CP="$i"
        else
      CP="$i":"$CP"
        fi
    fi
  done
}


LIB=lib
collectCP $LIB 
collectCP $LIB/tm4j 
collectCP $LIB/jena 

# ===============================
VM_ARGS=
JAVA=$JAVA_HOME/bin/java
# run jelly
# echo $CP
$JAVA -Djava.util.logging.config.file=logging.properties -classpath $CP $VM_ARGS org.apache.commons.jelly.Jelly $*
