package com.digiburo.demo1;

import java.io.File;

import java.io.FileWriter;
import java.io.BufferedWriter;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;

/**
 * Read training datum, write output suitable for gnuplot
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: DatumConverter1.java,v 1.4 2002/02/02 08:27:27 gsc Exp $
 */

/*
 * Development Environment:
 *   Linux 2.2.12-20 (Red Hat 6.1)
 *   Java Developers Kit 1.2.2-RC2-K
 *
 * Legalise:  
 *   Copyright (C) 2001 Digital Burro, INC.
 *
 * Maintenance History:
 *   $Log: DatumConverter1.java,v $
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

public class DatumConverter1 {
    private static final String NEG_FILENAME = "below.txt";
    private static final String POS_FILENAME = "above.txt";
    private static final String TRAIN_FILENAME = "demo1.trn";

    /**
     *
     */
    private PatternList pl;

    /**
     *
     */
    public void patternReader(String file_name) throws Exception {
	pl = new PatternList();
	pl.reader(new File(file_name));

  	int limit = pl.size();

	// write above answers

	BufferedWriter bw = new BufferedWriter(new FileWriter(POS_FILENAME));

	for (int ii = 0; ii < limit; ii++) {
	    Pattern pp = pl.get(ii);
	    double[] output = pp.getOutput();
	    if (output[0] > 0.5) {
		writeData(bw, pp);
	    }
  	}

	bw.close();

	// write below answers

	bw = new BufferedWriter(new FileWriter(NEG_FILENAME));

	for (int ii = 0; ii < limit; ii++) {
	    Pattern pp = pl.get(ii);
	    double[] output = pp.getOutput();
	    if (output[0] < 0.5) {
		writeData(bw, pp);
	    }
	}

	bw.close();
    }

    /**
     *
     */
    public void writeData(BufferedWriter bw, Pattern pp) throws Exception {
	double[] input = pp.getInput();
	double[] output = pp.getOutput();

	if (input == null) {
	    System.out.println("input null");
	}

	if (output == null) {
	    System.out.println("output null");
	}

	for (int ii = 0; ii < input.length; ii++) {
	    bw.write(Double.toString(input[ii]) + " ");
	}

	for (int ii = 0; ii < output.length; ii++) {
	    bw.write(Double.toString(output[ii]) + " ");
	}

	bw.newLine();
    }

    /**
     * Driver
     */
    public static void main(String args[]) throws Exception {
	System.out.println("begin");

	DatumConverter1 dc1 = new DatumConverter1();

	if (args.length == 0) {
	    dc1.patternReader(TRAIN_FILENAME);
	} else {
	    dc1.patternReader(args[0]);
	}

	System.out.println("end");
    }
}
