package org.kanthaka.cassandra.bulkloader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

import org.apache.cassandra.db.marshal.AsciiType;
import org.apache.cassandra.dht.IPartitioner;
import org.apache.cassandra.dht.RandomPartitioner;
import org.apache.cassandra.io.sstable.SSTableSimpleUnsortedWriter;

import static org.apache.cassandra.utils.ByteBufferUtil.bytes;
import static org.apache.cassandra.utils.UUIDGen.decompose;

public class SSTableCreaterNew {

		static String filename;

		public static void main(String[] args) throws IOException {

//		for(int i=0;i<10;i++){
				filename = "/home/pushpalanka/cdr-3lax";
				System.out.print("Start Time:"+ System.currentTimeMillis()/1000);
				BufferedReader reader = new BufferedReader(new FileReader(
						"/home/pushpalanka/cdr-3lax"));

				String keyspace = "CDRs1M";
				File directory = new File(keyspace);
				if(!directory.exists()){
						directory.mkdir();
				}

				IPartitioner partitioner = new RandomPartitioner();

				SSTableSimpleUnsortedWriter eventWriter = new SSTableSimpleUnsortedWriter(
						directory, partitioner, keyspace, "Events", AsciiType.instance,
						null, 64);

				String line;
				int lineNumber = 1;
				CsvEntry entry = new CsvEntry();
				// There is no reason not to use the same timestamp for every column in
				// that example.
				long timestamp = System.currentTimeMillis() /1000;

				while((line = reader.readLine()) != null){
						if(entry.parse(line, lineNumber)){
								// ByteBuffer uuid = ByteBuffer.wrap(decompose(entry.key));
								// usersWriter.newRow(uuid);

								ByteBuffer uuid = ByteBuffer
										.wrap(decompose(entry.CorrelationID));

								eventWriter.newRow(uuid);
								eventWriter.addColumn(bytes("sourceAdd"),
										bytes(entry.sourceAdd), timestamp);
								eventWriter.addColumn(bytes("sourceChannelType"),
										bytes(entry.sourceChannelType), timestamp);
								eventWriter.addColumn(bytes("desAdd"), bytes(entry.desAdd),
										timestamp);
								eventWriter.addColumn(bytes("desChannelType"),
										bytes(entry.desChannelType), timestamp);
								eventWriter.addColumn(bytes("transStatus"),
										bytes(entry.transStatus), timestamp);
						}
						lineNumber++;
				}
				System.out.print("End Time:"+ System.currentTimeMillis()/1000);
				// Don't forget to close!
				eventWriter.close();

				System.exit(0);
		}

		static class CsvEntry {

				UUID CorrelationID;
				String timestamp;
				String id;
				String sourceAdd;
				String sourceChannelType;
				String desAdd;
				String desChannelType;
				String billingType;
				String transStatus;

				boolean parse(String line, int lineNumber) {
						// Ghetto csv parsing
						String[] columns = line.split(",");
//						System.out.println(columns.length);
						if(columns.length != 48){
								System.out.println(String.format(
										"Invalid input '%s' at line %d of %s", line,
										lineNumber, filename));
								return false;
						}
						try {

								CorrelationID = UUID.randomUUID();
								id = columns[0].trim();

								sourceAdd = columns[7].trim();
								sourceChannelType = columns[9].trim();
								timestamp = columns[1].trim();
								desAdd = columns[11].trim();
								desChannelType = columns[13].trim();
								billingType = columns[17].trim();
								transStatus = columns[33].trim();

//								System.out.println(CorrelationID);
//								System.out.println(id);
//
//								System.out.println(sourceAdd);
//								System.out.println(sourceChannelType);
//								System.out.println(timestamp);
//								System.out.println(desAdd);
//								System.out.println(desChannelType);
//								System.out.println(billingType);
//								System.out.println(transStatus);
								return true;
						}
						catch(NumberFormatException e) {
								System.out.println(String.format(
										"Invalid number in input '%s' at line %d of %s", line,
										lineNumber, filename));
								return false;
						}
				}
		}
}