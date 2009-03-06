package com.digiburo.backprop1;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Back Propagation Neural Network.  
 * Use this class as public interface to network.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: BackProp.java,v 1.4 2002/02/03 23:42:04 gsc Exp $
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
 *   $Log: BackProp.java,v $
 *   Revision 1.4  2002/02/03 23:42:04  gsc
 *   First Release
 *
 *   Revision 1.3  2002/02/02 08:27:27  gsc
 *   Work In Progress
 *
 *   Revision 1.2  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 06:34:58  gsc
 *   Work In Progress
 */

public class BackProp {

    /**
     * Index of first input node (in Nodez)
     */
    private int first_input_node_ndx;

    /**
     * Index of first middle node (in Nodez)
     */
    private int first_middle_node_ndx;

    /**
     * Index of first output node (in Nodez)
     */
    private int first_output_node_ndx;

    /**
     * All nodes in system
     */
    private AbstractNode[] nodez;

    /**
     * Reference to serialized memento.
     */
    Network network;

    /**
     * Constructor for new backpropagation network.
     *
     * @param input_population input node count
     * @param middle_population middle node count
     * @param output_population output node count
     * @param learning_rate learning rate to use during error calculations 
     * @param momentum used during weight calculations 
     */
    public BackProp(int input_population, int middle_population, int output_population, double learning_rate, double momentum) {
	network = new Network(input_population, middle_population, output_population, learning_rate, momentum);
	getLocalReferences();
    }

    /**
     * Constructor for existing backpropagation network.
     *
     * @param in containing serialized network
     */
    public BackProp(InputStream in) throws IOException, ClassNotFoundException {
        reader(in);
        getLocalReferences();
    }

    /**
     * Get local references to values contained in memento
     */
    private void getLocalReferences() {
	nodez = network.getNodes();
	first_input_node_ndx = network.getFirstInputNodeIndex();
	first_middle_node_ndx = network.getFirstMiddleNodeIndex();
	first_output_node_ndx = network.getFirstOutputNodeIndex();
    }

    //////////////////////////////////////
    // Network Operations ////////////////
    //////////////////////////////////////

    /**
     * Run the backpropagation network by propagating values
     * from front to back (input to output).  Note that
     * for backprop, only the middle and output layer support run.
     * Input neurodes must already be loaded w/pattern.
     */
    public void runNetwork() {
	int middle_node_ndx = first_middle_node_ndx;
	
	for (int ii = first_middle_node_ndx; ii < nodez.length; ii++) {
	    OutputNode on = (OutputNode) nodez[ii];
	    on.runNode();
	}
    }

    /**
     * Run the backpropagation network by propagating values
     * from front to back (input to output).  Note that
     * for backprop, only the middle and output layer support run.
     * @param pp pattern w/input for network.
     */
    public void runNetwork(Pattern pp) {
        setInputPattern(pp.getInput());
	runNetwork();
    }

    /**
     * Train by backpropagation (move backward through nodes and
     * tweak weights w/error values).  Output value (ground truth) must
     * already be loaded.
     */
    public void trainNetwork() {
	for (int ii = (nodez.length -1); ii >= first_middle_node_ndx; ii--) {
	    OutputNode on = (OutputNode) nodez[ii];
	    on.trainNode();
	}
    }

    /**
     * Train by backpropagation (move backward through nodes and
     * tweak weights w/error values).
     * @param pp pattern w/output (truth) for network to learn
     */
    public void trainNetwork(Pattern pp) {
	setOutputPattern(pp.getOutput());
	trainNetwork();
    }

    //////////////////////////////////////
    // Input Pattern /////////////////////
    //////////////////////////////////////

    /**
     * Cause the supplied pattern to be loaded into the input nodes
     *
     * @param pp test pattern
     */
    public void setInputPattern(Pattern pp) {
        setInputPattern(pp.getInput());
    }

    /**
     * Cause the supplied pattern to be loaded into the input nodes
     *
     * @param input pattern for input nodes (input layer). 
     *        Values must be in range (0.0, 1.0)
     */
    public void setInputPattern(double[] input) {
	int input_node_ndx = first_input_node_ndx;
        for (int ii = 0; ii < input.length; ii++) {
// commented out for runtime performance
//  	    if ((input[ii] < 0.0) || (input[ii] > 1.0)) {
//  		throw new IllegalArgumentException("bad input value");
//  	    }
	    
            nodez[input_node_ndx++].setNodeValue(input[ii]);
        }
    }

    //////////////////////////////////////
    // Output Pattern ////////////////////
    //////////////////////////////////////

    /**
     * Apply output pattern to output nodes
     *
     * @param pp
     */
    public void setOutputPattern(Pattern pp) {
	setOutputPattern(pp.getOutput());
    }

    /**
     * Apply output pattern to output nodes
     *
     * @param output
     */
    public void setOutputPattern(double[] output) {
	int output_node_ndx = first_output_node_ndx;
        for (int ii = 0; ii < output.length; ii++) {
            nodez[output_node_ndx++].setNodeError(output[ii]);
        }
    }

    /**
     * Return network answer from output neurodes.
     * Values are in the range of (0.0, 1.0)
     * @return network answer from output neurodes
     */
    public double[] getOutputPattern() {
	int size = nodez.length - first_output_node_ndx;
	double[] results = new double[size];

	int output_node_ndx = first_output_node_ndx;
        for (int ii = 0; ii < size; ii++) {
	    results[ii] = nodez[output_node_ndx++].getNodeValue();
	}

	return(results);
    }

    //////////////////////////////////////
    // File I/O //////////////////////////
    //////////////////////////////////////

    /**
     * Write patterns as a serialized object
     *
     * @param out to be written
     */
    public void writer(OutputStream out) throws IOException {
        ObjectOutputStream oos = new ObjectOutputStream(out);
        try {
            oos.writeObject(network);
        } finally {
            oos.close();
        }
    }

    /**
     * Read serialized pattern
     *
     * @param in to be read
     */
    public void reader(InputStream in) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(in);
        try {
            network = (Network) ois.readObject();
        } finally {
            ois.close();
        }
    }

    /**
     * Driver
     */
    public static void main(String[] args) {
	System.out.println("begin");
	
	BackProp bp = new BackProp(2, 3, 1, 1.23, 3.21);
	
	System.out.println("end");
    }
}
