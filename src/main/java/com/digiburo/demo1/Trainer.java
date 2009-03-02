package com.digiburo.demo1;

import java.io.File;
import java.io.IOException; 
import java.io.FileNotFoundException;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;

/**
 * Train a backpropagation network for demo1.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Trainer.java,v 1.5 2002/02/02 20:53:53 gsc Exp $
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
 *   Revision 1.5  2002/02/02 20:53:53  gsc
 *   More testing tweaks
 *
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

public class Trainer {
    private static final String TRAIN_FILENAME = "demo1.trn";
    private static final String NETWORK_FILENAME = "demo1.serial";

    private BpDemo1 bp;
    private boolean[] flags;
    private PatternList pl;

    /**
     * Create network
     */
    public Trainer() {
	//bp = new BpDemo1(2, 7, 1, 0.45, 0.9);
	bp = new BpDemo1(2, 7, 1, 0.25, 0.9);
    }

    /**
     * Load training datum
     * @param datum training file
     */
    public int loadTraining(File datum) throws IOException, FileNotFoundException, ClassNotFoundException {
	pl = new PatternList();
	pl.reader(datum);

	flags = new boolean[pl.size()];

	return(pl.size());
    }

    /**
     * Train the network on these patterns
     */
    public void performTraining() {
	int counter = 0;
	int success = 0;
	int max_success = 0;
	
	do {
	    if (success > max_success) {
		max_success = success;
	    }

	    success = 0;
	    
	    for (int ii = 0; ii < pl.size(); ii++) {
		Pattern pn = pl.get(ii);
		bp.runNetwork(pn);
		bp.trainNetwork(pn);
		
		double[] truth = pn.getOutput();		
		double[] results = bp.getOutputPattern();
		
		boolean failed = false;
		for (int jj = 0; jj < results.length; jj++) {

//  		    System.out.print(ii + " results:" + round1(results[jj]) + " truth:" + truth[jj]);
//  		    if (round1(results[jj]) == round2(truth[jj])) {
//  			System.out.println(" true");
//  		    } else {
//  			System.out.println(" false");
//  		    }

		    if (round1(results[jj]) == round2(truth[jj])) {
			flags[ii] = true;
		    } else {
			flags[ii] = false;

			failed = true;
			break;
		    }
		}
		
		if (!failed) {
		    ++success;
		} 
	    }
	    
	    if ((++counter % 10000) == 0) {
		System.out.println(counter + " success:" + success + " needed:" + pl.size() + " best run:" + max_success);

		for (int jj = 0; jj < flags.length; jj++) {
		    if (flags[jj] == false) {
			System.out.print(jj + " ");
		    }
		}
		System.out.println();

		max_success = 0;
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
	if (candidate > 0.8) {
	    return(1);
	} else if (candidate < 0.2) {
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
     * Driver.
     */
    public static void main(String args[]) throws Exception {
	System.out.println("begin");

	int population = 0;
	Trainer tr = new Trainer();

	if (args.length == 0) {
	    population = tr.loadTraining(new File(TRAIN_FILENAME));
	} else {
	    population = tr.loadTraining(new File(args[0]));
	}

	System.out.println("PatternList loaded w/" + population + " patterns");
	tr.performTraining();
	tr.saveTraining(new File(NETWORK_FILENAME));

	System.out.println("end");
    }
}
