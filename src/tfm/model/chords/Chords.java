/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.chords;

import java.util.ArrayList;
import java.util.List;
import jm.constants.Durations;
import jm.midi.MidiUtil;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Write;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;

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

    public List<Chord> nodesToChords(List<PCSNode> nodes) {
        List<Chord> chords = new ArrayList<>();

        for (PCSNode n : nodes) {
            Chord chord = new Chord(n.getSet());
            chord.setDuration(Durations.QUARTER_NOTE);
            chords.add(chord);
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
            c.transpose(i);
        }
    }

    public List<Chord> convertNodesToChords(List<PCSNode> nodes) {
        List<Chord> chords = new ArrayList<>();

        for (PCSNode node : nodes) {
            chords.add(new Chord(node.getSet()));
        }

        return chords;
    }
}
