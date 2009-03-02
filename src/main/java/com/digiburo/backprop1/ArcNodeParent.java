package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Common support for arcs and nodes.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: ArcNodeParent.java,v 1.2 2002/01/21 10:27:55 gsc Exp $
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
 *   $Log: ArcNodeParent.java,v $
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:30:15  gsc
 *   Initial Check In
 */

public abstract class ArcNodeParent implements Serializable {

    /**
     * globally unique identifier for this object
     */
    protected final int id = GenerateId.getId();
    
    /**
     * Return object id
     *
     * @return object id
     */
    public int getId() {
	return(id);
    }
}
