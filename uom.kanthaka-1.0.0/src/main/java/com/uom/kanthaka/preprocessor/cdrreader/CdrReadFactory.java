/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.uom.kanthaka.preprocessor.cdrreader;

import com.uom.kanthaka.preprocessor.rulereader.Rule;
import java.util.ArrayList;
import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.StackObjectPool;

/**
 *
 * @author Makumar
 */
public class CdrReadFactory  extends BasePoolableObjectFactory<CdrRead>{
    ArrayList<Rule> rules;

    
    /**
     * constructor of CdrReadFactory
     * initialize the object with business Promotions
     */
    public CdrReadFactory(ArrayList<Rule> rules) {
        this.rules = rules;
    }
    
    
    /**
     * Create poolar objecet to return
     * @return CdrRead object form pool
     */
    @Override
    public CdrRead makeObject() { 
        return new CdrRead(rules, new StackObjectPool<CdrRead>(new CdrReadFactory(rules))); 
    } 
     
    /**
     * initialize poolar objecet which returns
     * @param CdrRead object form pool need to initialize
     */
    @Override
    public void passivateObject(CdrRead cdrObject) { 
        cdrObject.initilizeFields();
    }
}
