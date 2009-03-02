package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Arcs provide a weighted connection between nodes.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: Arc.java,v 1.5 2002/02/01 03:48:11 gsc Exp $
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
 *   $Log: Arc.java,v $
 *   Revision 1.5  2002/02/01 03:48:11  gsc
 *   Work In Progress
 *
 *   Revision 1.4  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.3  2002/01/22 06:34:58  gsc
 *   Work In Progress
 *
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:40:46  gsc
 *   Initial Check In
 */

public class Arc extends ArcNodeParent implements Serializable {

    /**
     * weights are initialized to a random value
     */    
    private double weight = MathSupport.boundedRandom(-1.0, 1.0);
    
    /**
     * Previous weight change
     */
    private double delta;
    
    /**
     * AbstractNode which arc is coming from
     */
    private AbstractNode in;
    
    /**
     * AbstractNode which arc is going to
     */
    private AbstractNode out;

    /**
     * Define input node
     * @param arg input node
     */
    public void setInputNode(AbstractNode arg) {
        in = arg;
    }
    
    /**
     * Define output node
     * @param arg output node
     */
    public void setOutputNode(AbstractNode arg) {
        out = arg;
    }

    /**
     * Return the node value of input arc
     * @return node value of input arc
     */
    public double getInputValue() {
        return(in.getNodeValue());
    }

    /**
     * Return the product of a input node value and arc weight
     * @return product of input node value and arc weight
     */
    public double getWeightedInputValue() {
	return(in.getNodeValue() * weight);
    }
    
    /**
     * Return the product of a output node error and arc weight
     * @return the product of a output node error and arc weight
     */
    public double getWeightedOutputError() {
	return(out.getNodeError() * weight);
    }
    
    /**
     * Update link weight by adding new value to current weight
     * @param arg new value added to current weight
     */
    public void updateWeight(double arg) {
    	OutputNode on = (OutputNode) out;
  	weight += arg + on.getMomentum() * delta;
    	delta = arg;

	//System.out.println("arc weight:" + weight + " delta:" + delta);
    }
    
    /**
     * Return description of object
     * @return description of object
     */
    public String toString() {
	return("Arc:" + id + " in:" + in.getId() + " out:" + out.getId());
    }
}
