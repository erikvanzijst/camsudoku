package com.digiburo.backprop1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.io.IOException; 
import java.io.FileNotFoundException;

import java.util.ArrayList;

/**
 * Container for a collection of patterns.  Also supports file I/O.
 *
 * @author G.S. Cole (gsc@acm.org)
 * @version $Id: PatternList.java,v 1.2 2002/01/21 10:27:55 gsc Exp $
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
 *   $Log: PatternList.java,v $
 *   Revision 1.2  2002/01/21 10:27:55  gsc
 *   Work In Progress
 *
 *   Revision 1.1  2002/01/21 02:56:10  gsc
 *   Initial Check In
 */

public class PatternList {

    /**
     * Container for Patterns
     */
    ArrayList al = new ArrayList();

    /**
     * Add a new element to the list
     * @param pp pattern to add to list
     */
    public void add(Pattern pp) {
	al.add(pp);
    }

    /**
     * Add a new element to the list
     * @param input input pattern
     * @param output output pattern
     */
    public void add(double[] input, double[] output) {
	al.add(new Pattern(input, output));
    }
    
    /**
     * Return the specified Pattern
     * @param index into pattern list, zero is first
     * @return the specified Pattern
     */
    public Pattern get(int index) {
	return((Pattern) al.get(index));
    }

    /**
     * Return the population of elements contained in list
     * @return the population of elements contained in list
     */
    public int size() {
	return(al.size());
    }

    /**
     * Write patterns as a serialized object
     *
     * @param file to be written
     */
    public void writer(File file) throws IOException, FileNotFoundException {
	ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file));
	oos.writeObject(al);
	oos.close();
    }
    
    /**
     * Read serialized pattern
     *
     * @param file to be read
     */
    public void reader(File file) throws IOException, FileNotFoundException, ClassNotFoundException {
	ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file));
	al = (ArrayList) ois.readObject();
	ois.close();
    }
}
