package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Generic pattern container.  
 * Input/output patterns are represented as array of double which map 
 * directly to the backprop input/output neurodes.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Pattern.java,v 1.2 2002/01/21 10:27:55 gsc Exp $
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
 *   $Log: Pattern.java,v $
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:56:10  gsc
 *   Initial Check In
 */

public class Pattern implements Serializable {

    /**
     * Input pattern
     */
    private double[] input;
    
    /**
     * Output pattern
     */
    private double[] output;

    /**
     * @param input input pattern
     * @param output output pattern
     */
    public Pattern(double[] input, double[] output) {
	this.input = (double[]) input.clone();
	this.output = (double[]) output.clone();
    }
    
    /**
     * @return input pattern
     */
    public double[] getInput() {
	return(input);
    }

    /**
     * @return output pattern
     */
    public double[] getOutput() {
	return(output);
    }

    /**
     * Return object contents as a String
     * @return object contents as a String
     */
    public String toString() {
	String result = "input ";
	if (input == null) {
	    result += " null ";
	} else {
	    for (int ii = 0; ii < input.length; ii++) {
		result += input[ii] + " ";
	    }
	}

	result += " output ";
	if (output == null) {
	    result += " null ";
	} else {
	    for (int ii = 0; ii < output.length; ii++) {
		result += output[ii] + " ";
	    }
	    
	}

	return(result);
    }

    /**
     * Write pattern to stdout
     */
    public void dumpPattern() {
	System.out.println("output pattern");
	for (int ii = 0; ii < output.length; ii++) {
	    System.out.println(ii + " " + output[ii]);
	}
	
	System.out.println("input pattern");
	for (int ii = 0; ii < input.length; ii++) {
      	    System.out.println(ii + " " + input[ii]);
	}
    }
}
