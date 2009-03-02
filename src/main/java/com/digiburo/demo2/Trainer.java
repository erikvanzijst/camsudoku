package com.digiburo.demo2;

import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;


/**
 * Train for demo2, XOR pattern.
 * Since there are only four patterns, I generate the training
 * patterns manually.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Trainer.java,v 1.4 2002/02/03 20:31:41 gsc Exp $
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
 *   $Log: Trainer.java,v $
 *   Revision 1.4  2002/02/03 20:31:41  gsc
 *   Format tweaks
 *
 *   Revision 1.3  2002/02/02 20:53:53  gsc
 *   More testing tweaks
 *
 *   Revision 1.2  2002/02/01 06:14:07  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/02/01 02:48:56  gsc
 *   Initial Check In
 */

public class Trainer {
    public static final double ONE = 0.9999999999;
    public static final double ZERO = 0.0000000001;
    
    private static final String NETWORK_FILENAME = "demo2.serial";

    private BpDemo2 bp;
    private PatternList pl;

    /**
     * Create network
     */
    public Trainer() {
	bp = new BpDemo2(2, 3, 1, 0.25, 0.5);
    }

    /**
     * Create training datum
     */
    public int loadTraining() throws IOException, FileNotFoundException, ClassNotFoundException {
	pl = new PatternList();

	double[] input = new double[2];
	double[] output = new double[1];

	input[0] = ZERO;
	input[1] = ZERO;
	output[0] = ZERO;
	pl.add(input, output);

	input = new double[2];
	output = new double[1];

	input[0] = ZERO;
	input[1] = ONE;
	output[0] = ONE;
	pl.add(input, output);

	input = new double[2];
	output = new double[1];

	input[0] = ONE;
	input[1] = ZERO;
	output[0] = ONE;
	pl.add(input, output);

	input = new double[2];
	output = new double[1];

	input[0] = ONE;
	input[1] = ONE;
	output[0] = ZERO;
	pl.add(input, output);

	return(pl.size());
    }

    /**
     * Train the network on these patterns
     */
    public void performTraining() {
	int counter = 0;
	int success = 0;

	do {
	    success = 0;

	    for (int ii = 0; ii < pl.size(); ii++) {
		Pattern pn = pl.get(ii);
		//		System.out.println(pn);
		bp.runNetwork(pn);
		bp.trainNetwork(pn);

		double truth[] = pn.getOutput();		
		double results[] = bp.getOutputPattern();

		boolean failed = false;
                for (int jj = 0; jj < results.length; jj++) {
                    if (round1(results[jj]) != round2(truth[jj])) {
                        failed = true;
                    }
                }
                
                if (!failed) {
                    ++success;
                } 
            }

	    if ((++counter % 100) == 0) {
                System.out.println(counter + " success:" + success + " needed:" + pl.size());
            }
        } while (success < pl.size());
	
	System.out.println("Training complete in " + counter + " cycles");
    }

    /**
     * Map an answer from the network to a value suitable for truth comparison
     * @param candidate value from network
     * @return value for comparison w/truth
     */
    private int round1(double candidate) {
	if (candidate > 0.95) {
	    return(1);
	} else if (candidate < 0.05) {
	    return(0);
	}
	
	return(-1);
    }

    /**
     * Map a truth value to a value suitable for comparison
     * @param candidate value from truth pattern
     * @return value for comparison w/truth
     */
    private int round2(double candidate) {
	if (candidate > 0.5) {
	    return(1);
	}

	return(0);
    }

    /**
     * Save this network for later use.
     * @param datum file to save as
     */
    public void saveTraining(File datum) throws IOException, FileNotFoundException {
        bp.writer(datum);
    }

    /**
     *
     */
    public static void main(String args[]) throws Exception {
	System.out.println("begin");

	Trainer tr = new Trainer();
	int population = tr.loadTraining();
	System.out.println("PatternList loaded w/" + population + " patterns");
	tr.performTraining();
	tr.saveTraining(new File(NETWORK_FILENAME));

	System.out.println("end");
    }
}
