package org.kanthaka.cassandra.flowtest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.management.JMX;
import javax.management.MBeanServerConnection;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;

import org.apache.cassandra.service.StorageServiceMBean;


public class BulkDataLoader {

	private JMXConnector connector;
	private StorageServiceMBean storageBean;
	
	
	
	public BulkDataLoader(String host, int port) throws Exception {
		connect(host, port);
	}

	
		
	void runLoader(String[] path,BulkDataLoader bulkDataLoader) throws Exception  {

		
		if (path.length == 0) {
			throw new IllegalArgumentException("usage: paths to bulk files");
		}

		
		for (String arg : path) {
			System.out.println("done");
			System.out.println(arg);
			bulkDataLoader.bulkLoad(arg);
		}
		bulkDataLoader.close();

	}

	private void connect(String host, int port) throws IOException,
			MalformedObjectNameException {
		JMXServiceURL jmxUrl = new JMXServiceURL(String.format(
				"service:jmx:rmi:///jndi/rmi://%s:%d/jmxrmi", host, port));
		Map<String, Object> env = new HashMap<String, Object>();
		connector = JMXConnectorFactory.connect(jmxUrl, env);
		MBeanServerConnection mbeanServerConn = connector
				.getMBeanServerConnection();
		ObjectName name = new ObjectName(
				"org.apache.cassandra.db:type=StorageService");
		storageBean = JMX.newMBeanProxy(mbeanServerConn, name,
				StorageServiceMBean.class);
	}

	public void close() throws IOException {
		connector.close();
	}

	public void bulkLoad(String path) {
		storageBean.bulkLoad(path);
	}

}
