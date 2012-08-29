package com.uom.kanthaka.cassandra.updater;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: amila
 * Date: 8/28/12
 * Time: 5:42 PM
 * To change this template use File | Settings | File Templates.
 */
public class TableCreaterTest {

    private TableCreater tableCreater;
    @Before
    public void setUp() throws Exception {
        tableCreater=new TableCreater();
    }

    @After
    public void tearDown() throws Exception {

    }



    @Test
    public void testCreateKeyspace() throws Exception {
         tableCreater.createKeyspace();
    }

    @Test
    public void testCreateTable() throws Exception {
      tableCreater.createTable("testColumnfamily");
    }


    @Test
    public void testIndexColumn() throws Exception {
        tableCreater.createKeyspace();
        tableCreater.createTable("testColumnfamily");
        tableCreater.indexColumn("testColumn","testColumnfamily");

    }
}
