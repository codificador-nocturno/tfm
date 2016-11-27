/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

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
    private Map<String, Chord> groups = new HashMap<>();
    private Tonnetz tonnetz;
    private DecimalFormat df;
    private String fileName;

    public ChordsAnalysis(String fileName) {
        this.fileName = fileName;
        df = new DecimalFormat("#.###");
        //tonnetz = new Tonnetz();
        //tonnetz.print();
        //System.out.println(tonnetz.getNode(11, 6, 2).getL().getL());
    }

    public void read() {
        score = new Score();
        Read.midi(score, fileName + ".mid");
    }

    public List<Chord> scanChords() {
        List<Chord> chords = new ArrayList<>();

        Enumeration parts = score.getPartList().elements();

        while (parts.hasMoreElements()) {
            Part nextPart = (Part) parts.nextElement();

            Enumeration phrases = nextPart.getPhraseList().elements();

            while (phrases.hasMoreElements()) {
                Phrase nextPhrase = (Phrase) phrases.nextElement();

                extractChords(nextPhrase);

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

    private List<Chord> reduceChords(List<Chord> chords, int size) {
        List<Chord> candidates = new ArrayList<>();
        List<Chord> reducedChords = new ArrayList<>();

        for (int i = 0; i < chords.size(); i++) {
            Chord c = chords.get(i);

            //0 for all
            if (size == 0) {
                candidates.add(c);
            } else if (c.size() == size) {
                candidates.add(c);
            }
        }

        if (candidates.size() > 0) {
            reducedChords.add(candidates.get(0));

            for (Chord c : candidates) {
                Chord last = reducedChords.get(reducedChords.size() - 1);
                if (!c.equals(last)) {
                    reducedChords.add(c);
                }
            }
        }

        return reducedChords;
    }

    private List<PCSet> convertToSets(List<Chord> chords) {
        List<PCSet> sets = new ArrayList<>();

        for (Chord c : chords) {
            sets.add(new PCSet(c));
        }

        return sets;
    }

    private void print(List<Chord> chords) {
        System.out.println("");
        for (Chord c : chords) {
            System.out.print(c);
        }
        System.out.println("");
    }

    private void writeChordsToMidi(List<Chord> chords, String name, int size) {
        if (chords.isEmpty()) {
            return;
        }

        Score s = new Score();
        s.setTempo(score.getTempo());
        s.setTitle(fileName + " " + name);

        Part p = new Part();
        p.setInstrument(MidiUtil.DISTORTED_GUITAR);
        Phrase ph = new Phrase();
        s.add(p);
        p.add(ph);
        for (Chord c : chords) {
            ph.addChord(c.getPitchesArray(), c.getDuration());
        }

        Write.midi(s, fileName + "_" + name + "_" + size + ".mid");
    }

    private void printSets(List<PCSet> sets) {
        System.out.println("");
        for (PCSet p : sets) {
            System.out.print(p);
        }
        System.out.println("");
    }

    public static void main(String[] args) {
        String[] files = {
            //                        "Orion_guitar_1",
            //                        "Orion_guitar_2",
            //                        "Orion_guitar_3",
            //                        "The_Call_Of_Ktulu_guitar_1",
            //                        "The_Call_Of_Ktulu_guitar_2",
            //                        "The_Call_Of_Ktulu_guitar_3",
            //                        "The_Call_Of_Ktulu_guitar_4",
            //                        "The_Call_Of_Ktulu_guitar_5",
            //                        "To_Live_Is_To_Die_guitar_1",
            //                        "To_Live_Is_To_Die_guitar_2",
            //                        "To_Live_Is_To_Die_guitar_3",
            //                        "To_Live_Is_To_Die_guitar_4",
            //                        "To_Live_Is_To_Die_guitar_5",
            //                        "To_Live_Is_To_Die_guitar_6",
            //                        "To_Live_Is_To_Die_guitar_7"
            //            "Ejemplo_analisis"
            "Orion_guitars",
            "The_Call_Of_Ktulu_guitars",
            "To_Live_Is_To_Die_guitars"
        };

        for (String fName : files) {
            ChordsAnalysis c = new ChordsAnalysis(fName);
            c.read();
            List<Chord> chords = c.scanChords();

            for (int size = 3; size <= 3; size++) {
                //List<Chord> reduced = c.reduceChords(chords, size);
                c.print(chords);
                //c.print(reduced);
                List<PCSet> sets = c.convertToSets(chords);
                c.printSets(sets);

                //c.writeChordsToMidi(reduced, "reduction", size);
            }
        }

        //c.writeOriginalChords();
        //c.writeProcessedChords();
    }

}
