package com.digiburo.demo1;

import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.BufferedWriter;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;


/**
 * Test the demo1 network.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Tester.java,v 1.4 2002/02/02 08:27:27 gsc Exp $
 */

/*
 * Development Environment:
 *   Linux 2.2.14-5.0 (Red Hat 6.2)
 *   Java Developers Kit 1.3.1
 *
 * Legalise:  
nn *   Copyright (C) 2002 Digital Burro, INC.
 *
 * Maintenance History:
 *   $Log: Tester.java,v $
 *   Revision 1.4  2002/02/02 08:27:27  gsc
 *   Work In Progress
 *
 *   Revision 1.3  2002/02/01 05:09:59  gsc
 *   Tweaks from Unit Testing
 *
 *   Revision 1.2  2002/02/01 02:48:08  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 08:19:35  gsc
 *   Initial Check In
 */

public class Tester {
    private static final String NEG_FILENAME = "negative.txt";
    private static final String POS_FILENAME = "positive.txt";
    private static final String NETWORK_FILENAME = "demo1.serial";

    private BpDemo1 bp;
    private PatternList pl;

    /**
     * Create network
     */
    public Tester(File network) throws IOException, FileNotFoundException, ClassNotFoundException {
	bp = new BpDemo1(network);
    }

    /**
     * Generate a 2D matrix from -1 to 1 at 0.1 intervals.
     * Submit these points to the network for classification.
     */
    public void performTesting() throws Exception {
	// write positive answers
	
	BufferedWriter bw = new BufferedWriter(new FileWriter(POS_FILENAME));
	
	for (double xx = 0.0; xx <= 1.0; xx += 0.1) {
	    for (double yy = 0.0; yy <= 1.0; yy += 0.1) {
		if (bp.classifier(xx, yy) > 0) {
		    writeData(bw, xx, yy, 1);
		}
	    }
	}
	
	bw.close();
	
	// write negative answers
	
	bw = new BufferedWriter(new FileWriter(NEG_FILENAME));
	
	for (double xx = 0.0; xx <= 1.0; xx += 0.1) {
	    for (double yy = 0.0; yy <= 1.0; yy += 0.1) {
		if (bp.classifier(xx, yy) < 0) {
		    writeData(bw, xx, yy, -1);
		}
	    }
	}
	
	bw.close();
    }


    /**
     *
     */
    public void writeData(BufferedWriter bw, double xx, double yy, int flag) throws Exception {
	bw.write(Double.toString(xx) + " ");
	bw.write(Double.toString(yy) + " ");
	bw.write(Integer.toString(flag));
	bw.newLine();
    }

    /**
     * Driver
     */
    public static void main(String args[]) throws Exception {
	System.out.println("begin");

	Tester tr = null;

	if (args.length != 1) {
	    tr = new Tester(new File(NETWORK_FILENAME));
	} else {
	    tr = new Tester(new File(args[0]));
	}

	tr.performTesting();

	System.out.println("end");
    }
}
