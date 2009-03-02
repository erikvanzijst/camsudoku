package com.digiburo.backprop1;

/**
 * This class generates a globally unique identifier.
 * All elements (i.e. nodes and links) get a globally unique identifier.
 * used for debugging/validation.  Yes, I know about Object.hashCode().
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: GenerateId.java,v 1.2 2002/01/21 10:27:55 gsc Exp $
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
 *   $Log: GenerateId.java,v $
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:26:08  gsc
 *   Initial Check In
 */

public class GenerateId {

    /**
     * Next unique identifier
     */
    private static int next_id;

    /**
     * Return the globally unique identifier.
     * @return unique identifier
     */
    public static synchronized int getId() {
	return(next_id++);
    }
}
