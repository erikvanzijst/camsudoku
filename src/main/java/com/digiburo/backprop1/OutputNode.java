package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Backpropagation output node.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: OutputNode.java,v 1.3 2002/02/03 23:42:04 gsc Exp $
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
 *   $Log: OutputNode.java,v $
 *   Revision 1.3  2002/02/03 23:42:04  gsc
 *   First Release
 *
 *   Revision 1.2  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 06:34:58  gsc
 *   Work In Progress
 */

public class OutputNode extends AbstractNode implements Serializable {

    /**
     * learning rate is used to help compute error term.
     */    
    private double learning_rate;
    
    /**
     * momentum term is used to compute weight in Arc
     */    
    private double momentum;

    /**
     * @param learning_rate
     * @param momentum
     */
    public OutputNode(double learning_rate, double momentum) {
	this.learning_rate = learning_rate;
	this.momentum = momentum;
    }

    /**
     * Return learning rate
     * @return learning rate
     */    
    public double getLearningRate() {
        return(learning_rate);
    }
    
    /**
     * Return momentum term
     * @return momentum term
     */
    public double getMomentum() {
        return(momentum);
    }
    
    /**
     * Update node value by summing weighted inputs 
     */
    public void runNode() {
	double total = 0.0;

	int limit = input_arcs.size();

	for (int ii = 0; ii < limit; ii++) {
	    Arc arc = (Arc) input_arcs.get(ii);
	    total += arc.getWeightedInputValue();
	}

	value = transferFunction(total);
    }

    /**
     * Update input weights based on error
     */    
    public void trainNode() {
	error = computeError();

	int limit = input_arcs.size();

	for (int ii = 0; ii < limit; ii++) {
	    Arc arc = (Arc) input_arcs.get(ii);
	    double delta = learning_rate * error * arc.getInputValue();
	    arc.updateWeight(delta);
	}
    }

    /**
     * Return sigmoid transfer value, result 0.0 < value < 1.0
     * @return sigmoid transfer value, result 0.0 < value < 1.0
     */
    public double transferFunction(double value) {
        return(1.0/(1.0 + Math.exp(-value)));
    }

    /**
     * Compute output node error using the derivative of the
     * sigmoid transfer function.
     *
     * @return output node error
     */
    public double computeError() {
	return(value * (1.0 - value) * (error - value));
    }

    /**
     * Return description of object
     * @return description of object
     */    
    public String toString() {
	return(toString("OutputNode:"));
    }

    /**
     * Return description of object
     * @return description of object
     */    
    public String toString(String prefix) {
	String result = prefix + super.toString() + " learning rate:" + learning_rate + " momentum:" + momentum;

	return(result);
    }

    /**
     * Test driver
     */
    public static void main(String[] args) {
	OutputNode on = new OutputNode(1.0, 2.0);

	for (int ii = -100; ii < 100; ii++) {
	    double arg = ii;
	    arg/=10.0;
	    System.out.println(arg + " " + on.transferFunction(arg));
	}
    }
}
