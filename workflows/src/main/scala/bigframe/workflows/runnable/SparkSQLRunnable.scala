package bigframe.workflows.runnable

import org.apache.spark.sql.hive.HiveContext
import bigframe.workflows.events.BigFrameListenerBus

/**
* Implement this interface such that a query can be run on hive.
*
* @author nikhil
*
*/
trait SparkSQLRunnable {

/*
* Prepeare the basic tables before run the Hive query
*/
def prepareHiveTables(hc:HiveContext): Unit

/**
* Run the benchmark query
*/
def runSparkSQL(hc:HiveContext, eventBus:BigFrameListenerBus): Boolean

def cleanUpHive(hc:HiveContext): Unit
}
