package com.digiburo.demo3;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException; 
import java.io.FileNotFoundException;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;
import com.digiburo.backprop1.BackProp;

/**
 * Backpropation Neural Network Demonstration (digit recognition)
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: BpDemo3.java,v 1.1 2002/02/03 04:24:05 gsc Exp $
 */

/*
 * Development Environment:
 *   Linux 2.2.14-5.0 (Red Hat 6.2)
 *   Java Developers Kit 1.3.1
 *
 * Legalise:  
 *   Copyright (C) 2002 Digital Burro, INC.
 *
 * Maintenance History:
 *   $Log: BpDemo3.java,v $
 *   Revision 1.1  2002/02/03 04:24:05  gsc
 *   Initial Check In
 *
 */

public class BpDemo3 extends BackProp {
    
    /**
     * Constructor for new backpropagation network.
     *
     * @param input_population input node count
     * @param middle_population middle node count
     * @param output_population output node count
     * @param learning_rate 
     * @param momentum
     */
    public BpDemo3(int input_population, int middle_population, int output_population, double learning_rate, double momentum) {
	super(input_population, middle_population, output_population, learning_rate, momentum);
    }

    /**
     * Constructor for existing backpropagation network.
     *
     * @param file serialized Network memento
     */
    public BpDemo3(File file) throws IOException, FileNotFoundException, ClassNotFoundException {
	super(file);
    }
}
