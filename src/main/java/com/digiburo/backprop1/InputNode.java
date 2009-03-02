package com.digiburo.backprop1;

import java.io.Serializable;

/**
 * Input nodes simply contain the submitted pattern.
 * Note that input values should be scaled in range (0.0, 1.0)
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: InputNode.java,v 1.3 2002/02/03 23:42:04 gsc Exp $
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
 *   $Log: InputNode.java,v $
 *   Revision 1.3  2002/02/03 23:42:04  gsc
 *   First Release
 *
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 08:49:21  gsc
 *   Initial Check In
 */

public class InputNode extends AbstractNode implements Serializable {

    /**
     *
     */
    public String toString() {
	return("InputNode:" + super.toString());
    }
}
