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

    public CdrReadFactory(ArrayList<Rule> rules) {
        this.rules = rules;
    }
    
    @Override
    public CdrRead makeObject() { 
        return new CdrRead(rules, new StackObjectPool<CdrRead>(new CdrReadFactory(rules))); 
    } 
     
    // when an object is returned to the pool,  
    // we'll clear it out 
    @Override
    public void passivateObject(CdrRead cdrObject) { 
        cdrObject.initilizeFields();
    }
}
