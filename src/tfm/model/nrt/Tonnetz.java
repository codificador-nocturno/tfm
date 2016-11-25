/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.nrt;

import static java.lang.reflect.Array.set;
import java.util.ArrayList;
import java.util.List;
import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class Tonnetz {

    private List<PCSNode> nodes;

    public Tonnetz() {
	nodes = new ArrayList<>();

	//create
	PCSNode n;
	n = new PCSNode(new PCSet(11, 6, 2));
	add(n);
	n = new PCSNode(new PCSet(6, 2, 9));
	add(n);
	n = new PCSNode(new PCSet(6, 1, 9));
	add(n);
	n = new PCSNode(new PCSet(1, 9, 4));
	add(n);
	n = new PCSNode(new PCSet(1, 8, 4));
	add(n);
	n = new PCSNode(new PCSet(8, 4, 11));
	add(n);
	n = new PCSNode(new PCSet(8, 3, 11));
	add(n);
	n = new PCSNode(new PCSet(3, 11, 6));
	add(n);
	n = new PCSNode(new PCSet(3, 10, 6));
	add(n);
	n = new PCSNode(new PCSet(10, 6, 1));
	add(n);
	//
	n = new PCSNode(new PCSet(2, 10, 5));
	add(n);
	n = new PCSNode(new PCSet(2, 9, 5));
	add(n);
	n = new PCSNode(new PCSet(9, 5, 0));
	add(n);
	n = new PCSNode(new PCSet(9, 4, 0));
	add(n);
	n = new PCSNode(new PCSet(4, 0, 7));
	add(n);
	n = new PCSNode(new PCSet(4, 11, 7));
	add(n);
	n = new PCSNode(new PCSet(11, 7, 2));
	add(n);
	//
	n = new PCSNode(new PCSet(10, 5, 1));
	add(n);
	n = new PCSNode(new PCSet(5, 1, 8));
	add(n);
	n = new PCSNode(new PCSet(5, 0, 8));
	add(n);
	n = new PCSNode(new PCSet(0, 8, 3));
	add(n);
	n = new PCSNode(new PCSet(0, 7, 3));
	add(n);
	n = new PCSNode(new PCSet(7, 3, 10));
	add(n);
	n = new PCSNode(new PCSet(7, 2, 10));
	add(n);
	//

	//connect
	getNode(11, 6, 2).setL(getNode(6, 2, 9));
    }

    private void add(PCSNode node) {
	boolean exists = false;
	//avoid duplicates
	for (PCSNode n : nodes) {
	    if (node.equals(n)) {
		exists = true;
		System.out.println("Warning: node " + n + "exists");
		break;
	    }
	}

	if (!exists) {
	    nodes.add(node);
	}
    }

    public PCSNode getNode(int pitchClass1, int pitchClass2, int pitchClass3) {
	String label = "[" + String.valueOf(pitchClass1) + " " + String.valueOf(pitchClass2) + " " + String.valueOf(pitchClass3) + "]";

	PCSNode found = null;

	for (PCSNode node : nodes) {
	    if (label.equals(node.getSet().toString())) {
		found = node;
		break;
	    }
	}

	return found;
    }

    public void print() {
	for (PCSNode node : nodes) {
	    System.out.println(node.getSet());
	}
    }

}
