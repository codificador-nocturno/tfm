/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.chords;

import java.util.ArrayList;
import java.util.List;
import jm.midi.MidiUtil;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Write;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;
import tfm.utils.ProbDistribution;

/**
 *
 * @author casa
 */
public class Chords {

    public List<Chord> convertSetsToChords(List<PCSet> sets) {
        List<Chord> chords = new ArrayList<>();

        for (PCSet set : sets) {
            Chord c = new Chord(set);
            chords.add(c);
        }

        return chords;
    }

    public void writeChordsToMidi(List<Chord> chords, String fileName, int tempo) {
        if (chords.isEmpty()) {
            return;
        }

        Score s = new Score();
        s.setTempo(tempo);
        s.setTitle(fileName);

        Part p = new Part();
        p.setInstrument(MidiUtil.DISTORTED_GUITAR);
        Phrase ph = new Phrase();
        s.add(p);
        p.add(ph);
        for (Chord c : chords) {
            ph.addChord(c.getPitchesArray(), c.getDuration());
        }

        Write.midi(s, fileName + ".mid");
    }

    public void transpose(List<Chord> chords, int i) {
        for (Chord c : chords) {
            c.transpose(i * 12);
        }
    }

    public List<Chord> convertNodesToChords(List<PCSNode> nodes) {
        List<Chord> chords = new ArrayList<>();

        for (PCSNode node : nodes) {
            chords.add(new Chord(node.getSet()));
        }

        return chords;
    }

    public Phrase convertChordsToPhrase(List<Chord> chords) {
        Phrase phrase = new Phrase();

        for (Chord c : chords) {
            phrase.addChord(c.getPitchesArray(), c.getDuration());
        }

        return phrase;
    }

    public void applyDuration(List<Chord> chords, ProbDistribution pd) {
        for (Chord c : chords) {
            c.setDuration(pd.getNext());
        }
    }

    public List<Chord> duplicate(List<Chord> chords) {
        List<Chord> copy = new ArrayList<>();

        for (Chord c : chords) {
            copy.add(c.duplicate());
        }

        return copy;
    }

    public void applyDuration(List<Chord> chords, double d) {
        for (Chord c : chords) {
            c.setDuration(d);
        }
    }
}
