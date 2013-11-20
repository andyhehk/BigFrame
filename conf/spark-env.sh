#!/usr/bin/env bash

######################### SPARK RELATED ##########################

# Path of Spark installation home. 
export SPARK_HOME="/home/shlee0605/spark-0.8.0"

# Path of Scala installation home.
export SCALA_HOME="/home/shlee0605/scala-2.9.3"

# Spark memory parameters, defaults will be used if unspecified
#export SPARK_MEM=g
# export SPARK_WORKER_MEMORY=6g

# Spark connection string, available in Spark master's webUI
export SPARK_CONNECTION_STRING="spark://ubuntu:7077"

# The Spark Home Directory
SPARK_HOME=$SPARK_HOME

# The Shark Home
SHARK_HOME=$SHARK_HOME
BIGFRAME_OPTS="${BIGFRAME_OPTS} -Dbigframe.shark.home=${SHARK_HOME}"

# Local directory for Spark scratch space
SPARK_LOCAL_DIR="/tmp/spark_local"

# The Spark Master Address
SPARK_MASTER=$SPARK_CONNECTION_STRING
BIGFRAME_OPTS="${BIGFRAME_OPTS} -Dbigframe.spark.master=${SPARK_MASTER}"

# Global Output Path
export OUTPUT_PATH="hdfs://localhost:9000/test_output"

