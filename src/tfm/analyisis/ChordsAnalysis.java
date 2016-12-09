/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.analyisis;

import java.text.DecimalFormat;
import tfm.model.chords.Chord;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import jm.constants.Durations;
import jm.midi.MidiUtil;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;
import tfm.model.nrt.Tonnetz;
import tfm.model.pcst.PCSet;

/**
 * El objetivo de esta clase es reducir la pista ritmica a sus acordes.
 *
 * @author casa
 */
public class ChordsAnalysis {

    private Score score;
    private Map<String, Chord> groups;
    private DecimalFormat df;
    private String fileName;

    public ChordsAnalysis(String fileName) {
	this.fileName = fileName;
	df = new DecimalFormat("#.###");
	groups = new HashMap<>();
    }

    public void read() {
	score = new Score();
	Read.midi(score, fileName + ".mid");
    }

    public List<Chord> scanAll() {
	List<Chord> chords = new ArrayList<>();

	Enumeration parts = score.getPartList().elements();

	while (parts.hasMoreElements()) {
	    Part nextPart = (Part) parts.nextElement();

	    Enumeration phrases = nextPart.getPhraseList().elements();

	    while (phrases.hasMoreElements()) {
		Phrase nextPhrase = (Phrase) phrases.nextElement();

		ChordsAnalysis.this.extractChords(nextPhrase);

		Enumeration notes = nextPhrase.getNoteList().elements();

		int i = 0;
		while (notes.hasMoreElements()) {
		    Note nextNote = (Note) notes.nextElement();
		    if (nextNote.getDynamic() == 127) {
			nextNote.setDynamic(126);
		    }
		    //System.out.println(nextNote);
		    i++;
		}//notes
	    }//phrases
	}//parts

	//to list
	for (String k : groups.keySet()) {
	    chords.add(groups.get(k));
	}

	Collections.sort(chords, new Comparator<Chord>() {
	    @Override
	    public int compare(Chord o1, Chord o2) {
		return o1.getStartTime().compareTo(o2.getStartTime());
	    }
	});

	return chords;
    }

    private void notate() {
	View.notate(score); //no muestra acordes :(
    }

    private void extractChords(Phrase phrase) {
	Vector notes = phrase.getNoteList();

	for (int i = 0; i < notes.size(); i++) {
	    Note n = (Note) notes.get(i);

	    if (n.isRest()) {
		continue;
	    }

	    String startTime = df.format(phrase.getNoteStartTime(i));

	    if (groups.get(startTime) == null) {
		Chord chord = new Chord();
		chord.setStartTime(startTime);
		chord.setDuration(n.getDuration());

		groups.put(startTime, chord);
	    }

	    groups.get(startTime).add(n);
	}
    }

    public List<Chord> extractChords(List<Chord> chords, int min, int max) {
	List<Chord> candidates = new ArrayList<>();

	for (Chord c : chords) {
	    if (c.size() >= min && c.size() <= max) {
		candidates.add(c);
	    }
	}

	return candidates;
    }
}
