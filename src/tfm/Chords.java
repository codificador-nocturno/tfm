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
import javax.sound.midi.MidiChannel;
import jm.constants.Durations;
import jm.midi.MidiUtil;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.View;
import jm.util.Write;

/**
 * El objetivo de esta clase es reducir la pista ritmica a sus acordes.
 *
 * @author casa
 */
public class Chords {

    public static void main(String[] args) {
        Chords c = new Chords();
        c.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_ritmica.mid", "Chord Test!");
        c.scan();
        c.processChords();
        //c.printOriginalChords();
        c.printProcessedChords();
        //c.writeOriginalChords();
        c.writeProcessedChords();
    }

    private Score score;
    private Map<Double, Chord> groups = new HashMap<>();
    private List<Chord> originalChords;
    private List<Chord> processedChords;

    public Chords() {
        originalChords = new ArrayList<>();
        processedChords = new ArrayList<>();
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
            Note n = (Note) o;
            if (n.isRest()) {
                continue;
            }

            Double startTime = phrase.getNoteStartTime(i);

            if (groups.get(startTime) == null) {
                Chord chord = new Chord();
                chord.setStartTime(startTime);
                chord.setDuration(n.getDuration());

                groups.put(startTime, chord);
            }

            groups.get(startTime).add(n);

            i++;
        }
    }

    private void processChords() {
        for (Double k : groups.keySet()) {
            originalChords.add(groups.get(k));
        }

        Collections.sort(originalChords, new Comparator<Chord>() {
            @Override
            public int compare(Chord o1, Chord o2) {
                return o1.getStartTime().compareTo(o2.getStartTime());
            }
        });

        //remove duplicates and set whole note
        for (int i = 0; i < originalChords.size(); i++) {
            if (originalChords.get(i).isSingleNote()) {
                continue;
            }

            Chord newChord = originalChords.get(i).duplicate();
            newChord.setDuration(originalChords.get(i).getDuration());

            //copy
            if (i == 0) {
                processedChords.add(newChord);
            } else if (!originalChords.get(i - 1).equals(newChord)) {
                processedChords.add(newChord);
            }
        }
    }

    public void printOriginalChords() {
        print(originalChords);
    }

    public void printProcessedChords() {
        print(processedChords);
    }

    private void print(List<Chord> chords) {
        for (Chord c : chords) {
            System.out.println(c);
        }
    }

    private void writeOriginalChords() {
        Score s = new Score();
        s.setTempo(score.getTempo());
        Part p = new Part();
        p.setInstrument(MidiUtil.DISTORTED_GUITAR);
        Phrase ph = new Phrase();
        s.add(p);
        p.add(ph);

        for (Chord c : originalChords) {
            ph.addChord(c.getPitchesArray(), c.getDuration());
        }

        Write.midi(s, "/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/chord_test_original.mid");
    }

    private void writeProcessedChords() {
        Score s = new Score();
        s.setTempo(score.getTempo());
        Part p = new Part();
        p.setInstrument(MidiUtil.DISTORTED_GUITAR);
        Phrase ph = new Phrase();
        s.add(p);
        p.add(ph);

        for (Chord c : processedChords) {
            ph.addChord(c.getPitchesArray(), c.getDuration());
        }

        Write.midi(s, "/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/chord_test_processed.mid");
    }

}
