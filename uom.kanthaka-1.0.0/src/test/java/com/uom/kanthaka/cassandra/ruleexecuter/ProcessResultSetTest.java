package com.uom.kanthaka.cassandra.ruleexecuter;

import java.util.ArrayList;
import java.util.HashSet;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.uom.kanthaka.preprocessor.rulereader.Rule;

import static junit.framework.Assert.assertEquals;

public class ProcessResultSetTest {

    private ProcessResultSet processResultSet;
    private  QueryRunUtill queryRunUtill;
    private ArrayList<ArrayList<HashSet<String>>> resultset;
    Rule rule;
    @Before
    public void setUp() throws Exception {
     processResultSet=new ProcessResultSet();
        rule=new Rule();
        resultset=new ArrayList<ArrayList<HashSet<String>>>();
        queryRunUtill=new QueryRunUtill(rule);

        // creating the resultset
        HashSet<String> toORlist1=new HashSet<String>();
        toORlist1.add("1234");
        toORlist1.add("1235");

        HashSet<String> toORlist2=new HashSet<String>();
        toORlist2.add("1234");
        toORlist2.add("1238");

        HashSet<String> toORlist3=new HashSet<String>();
        toORlist3.add("1234");
        toORlist3.add("1236");

        HashSet<String> toORlist4=new HashSet<String>();
        toORlist4.add("1234");
        toORlist4.add("1237");

        ArrayList<HashSet<String>> ANDlist1=new ArrayList<HashSet<String>>();
        ANDlist1.add(toORlist1);
        ANDlist1.add(toORlist2);

        ArrayList<HashSet<String>> ANDlist2=new ArrayList<HashSet<String>>();
        ANDlist2.add(toORlist3);
        ANDlist2.add(toORlist4);

        resultset.add(ANDlist1);
        resultset.add(ANDlist2);

        rule.setCounterResultSet(resultset);

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void testCompareResultSet() throws Exception {

        HashSet<String> expected=new HashSet<String>();
        expected.add("1234");
        HashSet<String> returnSet=processResultSet.compareResultSet(rule,queryRunUtill);
        assertEquals(expected, returnSet);
    }

    @Test
    public void testUnion() throws Exception {

    }


    @Test
    public void testIntersection() throws Exception {

    }

    @Test
    public void testDifference() throws Exception {

    }

    @Test
    public void testSymDifference() throws Exception {

    }
}
