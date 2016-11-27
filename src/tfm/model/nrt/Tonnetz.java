/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.nrt;

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

	//
	n = new PCSNode(new PCSet(0, 1, 9));
	add(n);
	n = new PCSNode(new PCSet(0, 1, 10));
	add(n);
	n = new PCSNode(new PCSet(0, 3, 7));
	add(n);
	n = new PCSNode(new PCSet(0, 3, 8));
	add(n);
	n = new PCSNode(new PCSet(0, 4, 7));
	add(n);
	n = new PCSNode(new PCSet(0, 4, 9));
	add(n);
	n = new PCSNode(new PCSet(0, 5, 8));
	add(n);
	n = new PCSNode(new PCSet(0, 5, 9));
	add(n);
	n = new PCSNode(new PCSet(0, 8, 9));
	add(n);
	n = new PCSNode(new PCSet(0, 8, 11));
	add(n);
	n = new PCSNode(new PCSet(0, 9, 10));
	add(n);
	n = new PCSNode(new PCSet(0, 9, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(1, 2, 10));
	add(n);
	n = new PCSNode(new PCSet(1, 2, 11));
	add(n);
	n = new PCSNode(new PCSet(1, 4, 8));
	add(n);
	n = new PCSNode(new PCSet(1, 4, 9));
	add(n);
	n = new PCSNode(new PCSet(1, 5, 8));
	add(n);
	n = new PCSNode(new PCSet(1, 5, 10));
	add(n);
	n = new PCSNode(new PCSet(1, 6, 9));
	add(n);
	n = new PCSNode(new PCSet(1, 6, 10));
	add(n);
	n = new PCSNode(new PCSet(1, 9, 10));
	add(n);
	n = new PCSNode(new PCSet(1, 10, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(2, 5, 9));
	add(n);
	n = new PCSNode(new PCSet(2, 5, 10));
	add(n);
	n = new PCSNode(new PCSet(2, 6, 9));
	add(n);
	n = new PCSNode(new PCSet(2, 6, 11));
	add(n);
	n = new PCSNode(new PCSet(2, 7, 10));
	add(n);
	n = new PCSNode(new PCSet(2, 7, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(3, 6, 11));
	add(n);
	n = new PCSNode(new PCSet(3, 6, 10));
	add(n);
	n = new PCSNode(new PCSet(3, 7, 10));
	add(n);
	n = new PCSNode(new PCSet(3, 8, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(4, 7, 11));
	add(n);
	n = new PCSNode(new PCSet(4, 8, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(7, 8, 10));
	add(n);
	n = new PCSNode(new PCSet(7, 8, 11));
	add(n);

	//
	n = new PCSNode(new PCSet(8, 9, 11));
	add(n);
	n = new PCSNode(new PCSet(8, 10, 11));
	add(n);

	connect();
    }

    private void add(PCSNode node) {
	boolean exists = false;
	//avoid duplicates
	for (PCSNode n : nodes) {
	    if (node.equals(n)) {
		exists = true;
		System.out.println("Warning: node " + n + " exists");
		break;
	    }
	}

	if (!exists) {
	    nodes.add(node);
	}
    }

    private void connect() {
	//connect
	getNode(11, 6, 2).setL(getNode(6, 2, 9));
    }

    public PCSNode getNode(int pitchClass1, int pitchClass2, int pitchClass3) {
	PCSNode target = new PCSNode(new PCSet(pitchClass1, pitchClass2, pitchClass3));

	PCSNode found = null;

	for (PCSNode node : nodes) {
	    if (target.equals(node)) {
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
	System.out.println("Total: " + nodes.size());
    }

    public static void main(String[] args) {
	new Tonnetz().print();
    }
}
