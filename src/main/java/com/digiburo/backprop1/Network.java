package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Memento class to capture state of network.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Network.java,v 1.2 2002/02/01 02:49:20 gsc Exp $
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
 *   $Log: Network.java,v $
 *   Revision 1.2  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 06:34:58  gsc
 *   Work In Progress
 */

public class Network implements Serializable {

    /**
     * Index of first input node (in Nodez)
     */
    private final int first_input_node_ndx;

    /**
     * Index of first middle node (in Nodez)
     */
    private final int first_middle_node_ndx;

    /**
     * Index of first output node (in Nodez)
     */
    private final int first_output_node_ndx;
    
    /**
     * learning rate
     */
    private double learning_rate;
    
    /**
     * momentum term
     */
    private double momentum;

    /**
     * Arcs connect nodes
     */
    private final Arc[] arcz;

    /**
     * Nodes contain values
     */
    private final AbstractNode[] nodez;

    /**
     * Constructor for standard 3 layer backpropagation network.
     *
     * @param input_population input node count
     * @param middle_population middle node count
     * @param output_population output node count
     * @param learning_rate 
     * @param momentum
     */
    public Network(int input_population, int middle_population, int output_population, double learning_rate, double momentum) {
	this.momentum = momentum;
	this.learning_rate = learning_rate;

	//
	// create nodez
	//
	int total_node_population = input_population + middle_population + output_population;
	nodez = new AbstractNode[total_node_population];

	int node_ndx = 0;

	//
        // define input layer
        //
        first_input_node_ndx = node_ndx;
        for (int ii = 0; ii < input_population; ii++) {
	    nodez[node_ndx++] = new InputNode();
        }

	//
        // define middle layer
        //
        first_middle_node_ndx = node_ndx;
        for (int ii = 0; ii < middle_population; ii++) {
	    nodez[node_ndx++] = new MiddleNode(learning_rate, momentum);
        }
	
        //
        // define output layer
        //
	first_output_node_ndx = node_ndx;
        for (int ii = 0; ii < output_population; ii++) {
	    nodez[node_ndx++] = new OutputNode(learning_rate, momentum);
        }

	//
        // create arcs
        //
	int total_arc_population = input_population * middle_population;
	total_arc_population += middle_population * output_population;
	arcz = new Arc[total_arc_population];

	for (int ii = 0; ii < total_arc_population; ii++) {
            arcz[ii] = new Arc();
        }

	//
	// connect nodes and arcs
	//
	int arc_ndx = 0;
	for (int ii = 0; ii < input_population; ii++) {
	    for (int jj = 0; jj < middle_population; jj++) {
		nodez[ii].connect(nodez[first_middle_node_ndx+jj], arcz[arc_ndx++]);
	    }
	}

	for (int ii = 0; ii < middle_population; ii++) {
	    for (int jj = 0; jj < output_population; jj++) {
		nodez[first_middle_node_ndx+ii].connect(nodez[first_output_node_ndx+jj], arcz[arc_ndx++]);

	    }
	}
    }

    /**
     * Return index of first input node (in Nodez)
     * @return index of first input node (in Nodez)
     */
    public int getFirstInputNodeIndex() {
	return(first_input_node_ndx);
    }

    /**
     * Return index of first middle node (in Nodez)
     * @return index of first middle node (in Nodez)
     */
    public int getFirstMiddleNodeIndex() {
	return(first_middle_node_ndx);
    }

    /**
     * Return index of first output node (in Nodez)
     * @return index of first output node (in Nodez)
     */
    public int getFirstOutputNodeIndex() {
	return(first_output_node_ndx);
    }
    
    /**
     * Return network learning rate
     * @return network learning rate
     */
    public double getLearningRate() {
	return(learning_rate);
    }
    
    /**
     * Return network momentum
     * @return network momentum
     */
    public double getMomentum() {
	return(momentum);
    }

    /**
     * Return all arcs
     * @return all arcs
     */
    public Arc[] getArcs() {
	return(arcz);
    }

    /**
     * Return all nodes
     * @return all nodes
     */
    public AbstractNode[] getNodes() {
	return(nodez);
    }

    /**
     * Write a report about node connections
     */
    public void dumpState() {
	System.out.println("Total Nodes:" + nodez.length);
	System.out.println("Input Node Index:" + first_input_node_ndx);
	System.out.println("Middle Node Index:" + first_middle_node_ndx);
	System.out.println("Output Node Index:" + first_output_node_ndx);

        for (int ii = 0; ii < nodez.length; ii++) {
	    System.out.println(ii + " " + nodez[ii]);
	}
    }

    /**
     * Write a report about arc connections
     */
    public void dumpConnection() {
	for (int ii = 0; ii < arcz.length; ii++) {
	    System.out.println(ii + " " + arcz[ii]);
	}
    }

    /**
     * Driver
     */
    public static void main(String[] args) {
	System.out.println("begin");

	Network nn = new Network(2, 3, 1, 1.23, 3.21);
	nn.dumpState();
	nn.dumpConnection();

	System.out.println("end");
    }
}
