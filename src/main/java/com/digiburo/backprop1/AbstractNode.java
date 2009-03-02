package com.digiburo.backprop1;

import java.io.Serializable;

import java.util.ArrayList;

/**
 * Parent for all node types.  Nodes are connected by arcs.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: AbstractNode.java,v 1.3 2002/02/02 08:27:27 gsc Exp $
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
 *   $Log: AbstractNode.java,v $
 *   Revision 1.3  2002/02/02 08:27:27  gsc
 *   Work In Progress
 *
 *   Revision 1.2  2002/02/01 02:49:20  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/22 06:34:58  gsc
 *   Work In Progress
 */

public abstract class AbstractNode extends ArcNodeParent implements Serializable {

    /**
     * Error for this node
     */    
    double error;

    /**
     * Value for this node
     */    
    double value;

    /**
     * input arcs
     */
    ArrayList input_arcs;
    
    /**
     * output arcs
     */
    ArrayList output_arcs;

    /**
     *
     */
    public AbstractNode() {
	input_arcs = new ArrayList();
	output_arcs = new ArrayList();
    }
    
    /**
     * Return node error term
     * @return node error term
     */
    public double getNodeError() {
        return(error);
    }
    
    /**
     * Define node error term
     * @param arg new error term
     */
    public void setNodeError(double arg) {
        error = arg;
    }
    
    /**
     * Return node value
     * @return node value
     */
    public double getNodeValue() {
        return(value);
    }
    
    /**
     * Define node value
     * @param arg new node value
     */
    public void setNodeValue(double arg) {
        value = arg;
    }

    /**
     * Connect to another node via an arc
     * @param destination node
     * @param arc to connect with
     */    
    public void connect(AbstractNode destination, Arc arc) {
	output_arcs.add(arc);
	
        destination.input_arcs.add(arc);
        
	arc.setInputNode(this);
	arc.setOutputNode(destination);
    }

    /**
     * Return description of object
     * @return description of object
     */
    public String toString() {
	return(id + " error:" + error + " value:" + value);
    }
}
