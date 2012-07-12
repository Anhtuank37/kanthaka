package org.kanthaka.cassandra.bulkloader;

/**
 * Disclaimer:
 * This file is an example on how to use the Cassandra SSTableSimpleUnsortedWriter class to create
 * sstables from a csv input file.
 * While this has been tested to work, this program is provided "as is" with no guarantee. Moreover,
 * it's primary aim is toward simplicity rather than completness. In partical, don't use this as an
 * example to parse csv files at home.
 */

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

public class DataImportExample {

		static String fileName;

		public static void main(String[] args) throws IOException {
//				if(args.length == 0){
//						System.out.println("Expecting <csv_file> as argument");
//						System.exit(1);
//				}
//				fileName = args[0];
//
//				BufferedReader reader = new BufferedReader(new FileReader(fileName));

				BufferedReader reader = new BufferedReader(new FileReader
						("/home/pushpalanka/Installations/FYP/cdr_simulator/cdr_001.csv"));

				String keySpace = "Demo";
				File directory = new File(keySpace);
				if(!directory.exists()){
						directory.mkdir();
				}

				IPartitioner partitioner = new RandomPartitioner();
				SSTableSimpleUnsortedWriter loginWriter = new SSTableSimpleUnsortedWriter(
						directory,
						partitioner, keySpace,
						"Events",
						AsciiType.instance,
						null,
						64);

				SSTableSimpleUnsortedWriter usersWriter = new SSTableSimpleUnsortedWriter(
						directory,
						partitioner, keySpace,
						"Users",
						AsciiType.instance,
						null,
						64);

				String line;
				int lineNumber = 1;
				CsvEntry entry = new CsvEntry();
				// There is no reason not to use the same timestamp for every column in that example.
				long timestamp = System.currentTimeMillis() * 1000;
				while((line = reader.readLine()) != null){
						if(entry.parse(line, lineNumber)){
								ByteBuffer uuid = ByteBuffer.wrap(decompose(entry.key));
								usersWriter.newRow(uuid);
								usersWriter.addColumn(bytes("firstName"), bytes(entry.firstName), timestamp);
								usersWriter.addColumn(bytes("lastName"), bytes(entry.lastName), timestamp);
								usersWriter.addColumn(bytes("password"), bytes(entry.password), timestamp);
								usersWriter.addColumn(bytes("age"), bytes(entry.age), timestamp);
								usersWriter.addColumn(bytes("email"), bytes(entry.email), timestamp);

								loginWriter.newRow(bytes(entry.email));
								loginWriter.addColumn(bytes("password"), bytes(entry.password), timestamp);
								loginWriter.addColumn(bytes("uuid"), uuid, timestamp);
						}
						lineNumber++;
				}
				// Don't forget to close!
				usersWriter.close();
				loginWriter.close();
				System.exit(0);
		}

		static class CsvEntry {

				UUID key;
				String firstName;
				String lastName;
				String password;
				long age;
				String email;

				boolean parse(String line, int lineNumber) {
						// Ghetto csv parsing
						String[] columns = line.split(",");
						if(columns.length != 48){
								System.out.println(String.format("Invalid input '%s' at line %d of %s", line, lineNumber, fileName));
								return false;
						}
						try {
								key = UUID.fromString(columns[0].trim());
								firstName = columns[1].trim();
								lastName = columns[2].trim();
								password = columns[3].trim();
								age = Long.parseLong(columns[4].trim());
								email = columns[5].trim();
								return true;
						}
						catch(NumberFormatException e) {
								System.out.println(String.format("Invalid number in input '%s' at line %d of %s", line, lineNumber,
										fileName));
								return false;
						}
				}
		}
}