/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.pcst;

import tfm.model.chords.Chord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import jm.music.data.Note;

/**
 *
 * @author william
 */
public class PCSet {

    private List<Integer> classes;

    public PCSet(Chord chord) {
	PCSUtils pcs = new PCSUtils();

	classes = new ArrayList<>();

	List<Integer> candidates = new ArrayList<>();

	for (Note note : chord.getNotes()) {
	    candidates.add(pcs.getPitchClass(note));
	}

	for (Integer cand : candidates) {
	    boolean found = false;

	    for (Integer cl : classes) {
		if (cand.equals(cl)) {
		    found = true;
		}
	    }

	    if (!found) {
		classes.add(cand);
	    }
	}

	//sort
	Collections.sort(classes);
    }

    public PCSet(int pitchClass1, int pitchClass2, int pitchClass3) {
	classes = new ArrayList<>();

	classes.add(pitchClass1);
	classes.add(pitchClass2);
	classes.add(pitchClass3);

	//sort
	Collections.sort(classes);
    }

    @Override
    public String toString() {
	String s = "[";

	boolean first = true;
	for (Integer i : classes) {
	    if (first) {
		first = false;
	    } else {
		s += " ";
	    }
	    s += i.toString();
	}

	return s + "]";
    }

    @Override
    public boolean equals(Object obj) {
	PCSet other = (PCSet) obj;

	return other.toString().equals(this.toString());
    }

    public int size() {
	return classes.size();
    }

}
