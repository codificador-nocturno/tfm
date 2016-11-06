/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.View;

/**
 * El objetivo de esta clase es reducir la pista ritmica a sus acordes.
 *
 * @author casa
 */
public class Chords {

    public static void main(String[] args) {
        Chords c = new Chords();
        c.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/chord_test.mid", "Chord Test!");
        c.scan();
        c.countChords();
        c.printChords();
    }

    private Score score;
    private Map<Double, Chord> groups = new HashMap<>();
    private List<Chord> chords;

    public Chords() {
        chords = new ArrayList<>();
    }

    public void read(String path, String title) {
        score = new Score();
        Read.midi(score, path);
        score.setTitle(title);
    }

    public void scan() {
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
                    i++;
                }//notes
            }//phrases
        }//parts

    }

    private void notate() {
        View.notate(score); //no muestra acordes :(
    }

    private void extractChords(Phrase phrase) {
        Vector notes = phrase.getNoteList();

        int i = 0;
        for (Object o : notes) {
            Double startTime = phrase.getNoteStartTime(i);

            if (groups.get(startTime) == null) {
                Chord chord = new Chord();
                chord.setStartTime(startTime);

                groups.put(startTime, chord);
            }

            groups.get(startTime).add((Note) o);

            i++;
        }
    }

    public void printChords() {
        for (Chord c : chords) {
            System.out.println(c);
        }
    }

    private void countChords() {
        for (Double k : groups.keySet()) {
            chords.add(groups.get(k));
        }

        Collections.sort(chords, new Comparator<Chord>() {
            @Override
            public int compare(Chord o1, Chord o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });
    }

}
