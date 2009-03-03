package com.digiburo.backprop1;

import java.util.Random;

/**
 * Math utilities
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: MathSupport.java,v 1.2 2002/01/21 10:27:55 gsc Exp $
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
 *   $Log: MathSupport.java,v $
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:28:59  gsc
 *   Initial Check In
 */

public class MathSupport {

    /**
     * Reference for instance of random
     */
    private static Random random = new Random();
    
    /**
     * Return a random number within the specified range
     *
     * @param lower range
     * @param upper range
     * @return a random number within the specified range
     */
    public static double boundedRandom(double lower, double upper) {
	double range = upper - lower;
	double result = random.nextDouble() * range + lower;
	
	return(result);
    }
    
    /**
     * Test driver
     */
    public static void main(String args[]) {
	for (int ii = 0; ii < 100; ii++) {
	    System.out.println(ii + ", " + boundedRandom(-1.0, 1.0));
	}
    }
}

