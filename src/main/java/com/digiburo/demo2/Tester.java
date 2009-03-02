package com.digiburo.demo2;

import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;

import java.io.FileWriter;
import java.io.BufferedWriter;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;


/**
 * Test the demo2 network.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Tester.java,v 1.3 2002/02/02 20:53:53 gsc Exp $
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
 *   $Log: Tester.java,v $
 *   Revision 1.3  2002/02/02 20:53:53  gsc
 *   More testing tweaks
 *
 *   Revision 1.2  2002/02/01 06:14:07  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/02/01 02:48:56  gsc
 *   Initial Check In
 */

public class Tester {
    public static final String NETWORK_FILENAME = "demo2.serial";
    public static final String TRUE_FILENAME = "true.txt";

    private BpDemo2 bp;
    private PatternList pl;

    /**
     * Create network
     */
    public Tester(File network) throws IOException, FileNotFoundException, ClassNotFoundException {
	bp = new BpDemo2(network);
    }

    /**
     * Generate a 2D matrix from -1 to 1 at 0.1 intervals.
     * Submit these points to the network for classification.
     */
    public void performTesting() throws Exception {
	BufferedWriter bw = new BufferedWriter(new FileWriter(TRUE_FILENAME));

	for (double xx = 0.0; xx <= 1.0; xx += 0.1) {
	    for (double yy = 0.0; yy <= 1.0; yy += 0.1) {
		if (bp.classifier(xx, yy)) {
		    bw.write(Double.toString(xx) + " ");
		    bw.write(Double.toString(yy) + " ");
		    bw.write(1); //pad
		    bw.newLine();
		}
	    }
	}

	bw.close();
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
