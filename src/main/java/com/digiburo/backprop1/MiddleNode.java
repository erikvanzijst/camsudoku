package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Backpropagation middle node.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: MiddleNode.java,v 1.2 2002/02/01 02:49:20 gsc Exp $
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
 *   $Log: MiddleNode.java,v $
 *   Revision 1.2  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 06:34:58  gsc
 *   Work In Progress
 */

public class MiddleNode extends OutputNode implements Serializable {

    /**
     * @param learning_rate
     * @param momentum
     */
    public MiddleNode(double learning_rate, double momentum) {
	super(learning_rate, momentum);
    }

    /**
     * Compute error term as sum of weighted outputs.
     */
    public double computeError() {
        double total = 0.0;

        int limit = output_arcs.size(); 

        for (int ii = 0; ii < limit; ii++) {
	    Arc arc = (Arc) output_arcs.get(ii);
            total += arc.getWeightedOutputError();
        }

        double result = value * (1.0 - value) * total;

        return(result);
    }

    /**
     * Return description of object
     * @return description of object
     */
    public String toString() {
	return(toString("MiddleNode:"));
    }
}
