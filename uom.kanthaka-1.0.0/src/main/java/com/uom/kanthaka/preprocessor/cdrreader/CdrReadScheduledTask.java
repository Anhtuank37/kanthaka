/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

//import DBUpdater.CounterCreater;
import com.uom.kanthaka.cassandra.updater.CassandraUpdater;
import com.uom.kanthaka.cassandra.updater.TableCreater;
import com.uom.kanthaka.preprocessor.Constant;
import org.apache.commons.pool.ObjectPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Timer;
import java.util.TimerTask;

//import org.apache.log4j.Logger;
/**
 * @author Makumar
 */
public class CdrReadScheduledTask extends TimerTask {

//    ArrayList<Rule> businessRules;
    CassandraUpdater couUpdater;
    TableCreater tableCreater;
    private ObjectPool<CdrRead> pool;
//    CdrRead cdr;
    final Logger logger = LoggerFactory.getLogger(CdrReadScheduledTask.class);

    /**
     * Constructor of CdrReadScheduledTask
     * @param
     * @return
     */
    public CdrReadScheduledTask() {
    }

    public CdrReadScheduledTask(ObjectPool<CdrRead> pool) {
        this.pool = pool;
//        this.businessRules = businessRules;
//        cdr = new CdrRead(businessRules);
    }

    /**
     * run method for the class
     */
    @Override
    public void run() {
        CdrRead newCdrObject = null;
        File files[];
        files = new File(Constant.CDR_URL).listFiles();

        try {
            if (files.length > 0) {
                for (int i = 0; i < files.length; i++) {
                    newCdrObject = pool.borrowObject();
                    File file = new File(files[i].toString());
                    newCdrObject.setFile(file);
                    newCdrObject.start();
                }
            } else {
//                System.out.println("--- No files ---");
                logger.info("--- No files ---");
            }
        } catch (Exception e) {
//            e.printStackTrace();
//        }

            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        }
    }

//    /**
//     * Read CDR files according to the given business rules
//     * @param file
//     */
//    public void readCdr(File file) {
//        CdrRead newCdrObject = null;
//        try {
//            BufferedReader bufReader = new BufferedReader(new FileReader(file));
//            newCdrObject = pool.borrowObject();
//            String tempRec;
//
//            while ((tempRec = bufReader.readLine()) != null) {
//                newCdrObject.setRecord(tempRec);
//                newCdrObject.start();
////                newCdrObject.readCdrFile(tempRec);
//            }
////  *****************************************************************          
//            printRuleRecordMaps();
////  *****************************************************************          
//            bufReader.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != newCdrObject) {
//                    pool.returnObject(newCdrObject);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
//    public void printRuleRecordMaps() {
//        CdrRead newCdrObject = null;
//        try {
//            newCdrObject = pool.borrowObject();
//            ArrayList<Rule> businessRules = newCdrObject.getRules();
//            for (int i = 0; i < businessRules.size(); i++) {
//                Rule businessRule = businessRules.get(i);
//                System.out.println("Rule Name : " + businessRule.getRuleName());
////            logger.debug("Rule Name : {}.", businessRule.getRuleName());
//
//                for (int j = 0; j < businessRule.getConditionFields().size(); j++) {
//                    ArrayList<ConditionField> temp = businessRule.getConditionFields().get(j);
//                    for (int k = 0; k < temp.size(); k++) {
//                        System.out.println(temp.get(k).printDetails());
////                    logger.debug("Checking Conditions : {}.", temp.get(k).printDetails());
//                    }
//                }
//                ArrayList<RecordMap> mapList = businessRule.getRecordMaps();
//                for (int j = 0; j < mapList.size(); j++) {
//                    RecordMap record = mapList.get(j);
//                    System.out.println(record.getType() + "  -  " + record.getDataMap());
////                logger.debug("{}. - {}.", record.getType(), record.getDataMap());
//                }
//                System.out.println("");
////            logger.debug("");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (null != newCdrObject) {
//                    pool.returnObject(newCdrObject);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
    public CdrRead getCdrRead() {
        CdrRead newCdrObject = null;
        try {
            newCdrObject = pool.borrowObject();
            return newCdrObject;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                if (null != newCdrObject) {
                    pool.returnObject(newCdrObject);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
