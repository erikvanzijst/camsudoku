package com.digiburo.demo3;

import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;

import java.io.IOException; 
import java.io.FileNotFoundException;

import com.digiburo.backprop1.Pattern;
import com.digiburo.backprop1.PatternList;


/**
 * Train for demo3, digit recognition.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Trainer.java,v 1.1 2002/02/03 04:24:05 gsc Exp $
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
 *   Revision 1.1  2002/02/03 04:24:05  gsc
 *   Initial Check In
 *
 */

public class Trainer {
    public static final double one = 0.9999999999;
    public static final double zero = 0.0000000001;

    private static final String TRAIN_FILENAME = "demo3.trn";    
    private static final String NETWORK_FILENAME = "demo3.serial";

    private BpDemo3 bp;
    private PatternList pl;

    /**
     * Create network
     */
    public Trainer() {
	bp = new BpDemo3(25, 5, 10, 0.45, 0.9);
    }

    /**
     * Load training datum
     * @param datum training file
     */
    public int loadTraining(File datum) throws IOException, FileNotFoundException, ClassNotFoundException {
	pl = new PatternList();
	pl.reader(datum);
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
		bp.runNetwork(pn);
		bp.trainNetwork(pn);

		double truth[] = pn.getOutput();		
		double results[] = bp.getOutputPattern();

		boolean failed = false;

//  		System.out.print("patt " + ii + " ");
//  		double[] input = pn.getInput();
//                  for (int jj = 0; jj < input.length; jj++) {
//  		    System.out.print(round2(input[jj]) + " ");
//  		}
//  		System.out.print("// ");
//                  for (int jj = 0; jj < truth.length; jj++) {
//  		    System.out.print(round2(truth[jj]) + " ");
//  		}
//  		System.out.println();

//                  for (int jj = 0; jj < results.length; jj++) {
//  		    System.out.print(results[jj] + " ");
//  		    // System.out.print(round2(results[jj]) + " ");
//  		}
//  		System.out.println();
		
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
	if (candidate > 0.85) {
	    return(1);
	} else if (candidate < 0.15) {
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
	int population = tr.loadTraining(new File(TRAIN_FILENAME));
	System.out.println("PatternList loaded w/" + population + " patterns");
	tr.performTraining();
	tr.saveTraining(new File(NETWORK_FILENAME));

	System.out.println("end");
    }
}
