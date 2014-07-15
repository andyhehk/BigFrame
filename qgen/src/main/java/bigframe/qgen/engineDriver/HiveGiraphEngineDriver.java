package bigframe.qgen.engineDriver;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.sql.Statement;

import org.apache.hadoop.conf.Configuration;
import org.apache.giraph.conf.GiraphConfiguration;
import org.apache.log4j.Logger;

import bigframe.bigif.BigConfConstants;
import bigframe.bigif.WorkflowInputFormat;
import bigframe.util.MapRedConfig;
import bigframe.workflows.runnable.HiveGiraphRunnable;

public class HiveGiraphEngineDriver extends EngineDriver {
	
	private GiraphConfiguration giraph_config;
	private static final Logger LOG = Logger.getLogger(HiveGiraphEngineDriver.class);
	private List<HiveGiraphRunnable> queries = new ArrayList<HiveGiraphRunnable>();
	
	private Connection connection;
	private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";

	public HiveGiraphEngineDriver(WorkflowInputFormat workIF) {
		super(workIF);
		Configuration mapred_config = MapRedConfig.getConfiguration(workIF);
		giraph_config = new GiraphConfiguration(mapred_config);
		Configuration.addDefaultResource("giraph-site.xml");
	}

	@Override
	public int numOfQueries() {
		// TODO Auto-generated method stub
		return queries.size();
	}

	@Override
	public void init() {
		try {
			Class.forName(driverName);
        } catch (ClassNotFoundException e) {
        	// TODO Auto-generated catch block
        	e.printStackTrace();
        	System.exit(1);
        }

		try {
			LOG.info("Connectiong to Hive JDBC server!!!");
			connection = DriverManager.getConnection(workIF.getHiveJDBCServer(), "", "");
			if(connection == null) {
				LOG.error("Cannot connect to JDBC server! " +
						"Make sure the HiveServer is running!");
				System.exit(1);
			}
			else
				LOG.info("Connect to Hive JDBC server Successfully!!!");
			
			
			
			String UDF_JAR = workIF.get().get(BigConfConstants.BIGFRAME_UDF_JAR);

			Statement stmt = connection.createStatement();
			stmt.execute("DELETE JAR " + UDF_JAR);
			LOG.info("Adding UDF JAR " + UDF_JAR + " to hive server");
			if(stmt.execute("ADD JAR " + UDF_JAR)) {
				LOG.info("Adding UDF JAR successful!");
				stmt.execute("create temporary function sentiment as \'bigframe.workflows.util.SenExtractorHive\'");
				stmt.execute("create temporary function isWithinDate as \'bigframe.workflows.util.WithinDateHive\'");
			}
			else {
				LOG.error("Adding UDF JAR failed!");
			}
			
			// HIVE will throw error when drop an nonexisting table
			stmt.execute("set hive.exec.drop.ignorenonexistent = true");
			
			Map<String, String> properties = workIF.get();
			
			for(Map.Entry<String, String> entry : properties.entrySet()) {
				stmt.execute("set " + entry.getKey() + "=" + entry.getValue());
			}
			
//			int dop = workIF.getSparkDoP();
//			stmt.execute("set hive.auto.convert.join=false");
//			stmt.execute("set hive.exec.parallel=true");
//			stmt.execute("set hive.exec.reducers.max="+dop);
//			stmt.execute("set mapred.job.reuse.jvm.num.tasks=10");
			
			if(!workIF.getSkipPrepareTable())
				for(HiveGiraphRunnable query : queries) {
					LOG.info("Prepare tables...");
					query.prepareHiveGiraphTables(connection);
				}
		
		} catch (SQLException e) {
			e.printStackTrace();
			System.exit(1);
		}
	}

	@Override
	public void run() {
		LOG.info("Running HiveGiraph queries!");
		for(HiveGiraphRunnable query : queries) {
			if(query.runHiveGiraph(giraph_config, connection))
				LOG.info("Query Finished");
			else
				LOG.info("Query failed");
		}

	}

	@Override
	public void cleanup() {
		for(HiveGiraphRunnable query : queries) {
			query.cleanUpHiveGiraph(connection);
		}

		if(connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void addQuery(HiveGiraphRunnable query) {
		queries.add(query);
	}
}
