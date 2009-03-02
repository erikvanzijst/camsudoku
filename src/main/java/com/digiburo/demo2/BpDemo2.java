package com.digiburo.demo2;

import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;

import com.digiburo.backprop1.BackProp;

/**
 * Backpropation Neural Network Demonstration (XOR problem)
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: BpDemo2.java,v 1.2 2002/02/01 06:14:07 gsc Exp $
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
 *   $Log: BpDemo2.java,v $
 *   Revision 1.2  2002/02/01 06:14:07  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/02/01 02:48:56  gsc
 *   Initial Check In
 */

public class BpDemo2 extends BackProp {

    /**
     * Constructor for new backpropagation network.
     *
     * @param input_population input node count
     * @param middle_population middle node count
     * @param output_population output node count
     * @param learning_rate 
     * @param momentum
     */
    public BpDemo2(int input_population, int middle_population, int output_population, double learning_rate, double momentum) {
	super(input_population, middle_population, output_population, learning_rate, momentum);
    }

    /**
     * Constructor for existing backpropagation network.
     *
     * @param file serialized Network memento
     */
    public BpDemo2(File file) throws IOException, FileNotFoundException, ClassNotFoundException {
	super(file);
    }

    /**
     * Classify a point based upon XOR training
     *
     * @param xx x coordinate
     * @param yy y coordinate
     * @return true, XOR true
     */
    public boolean classifier(double xx, double yy) {
	double[] input = new double[2];
	input[0] = xx;
	input[1] = yy;
	
	setInputPattern(input);
	runNetwork();
	double[] results = getOutputPattern();
	
	if (results[0] >= 0.5) {
	    return(true);
	}

	return(false);
    }
}
