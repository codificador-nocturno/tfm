/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import jm.constants.Pitches;
import jm.constants.ProgramChanges;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.Write;
import tfm.model.chords.Chord;
import tfm.model.markov.Matrix;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;
import tfm.utils.NormalDistribution;
import tfm.utils.UniformDistribution;

/**
 *
 * @author william
 */
public class Composition {

    public static void main(String[] args) throws Exception {
        new Composition().create();
    }

    private List<Matrix> matrices;
    private Phrase guitarPhrase;
    private Phrase bassPhrase;
    private Score score;
    private int TEMPO = 120;

    public Composition() throws Exception {
        matrices = new ArrayList<>();
        score = new Score();
        score.setDenominator(4);
        score.setNumerator(4);
        score.setTempo(TEMPO);

        Part guitarPart = new Part("Guitar");
        Part bassPart = new Part("Bass");

        guitarPhrase = new Phrase();
        bassPhrase = new Phrase();

        guitarPart.add(guitarPhrase);
        bassPart.add(bassPhrase);

        score.add(guitarPart);
        score.add(bassPart);

        guitarPart.setInstrument(ProgramChanges.DISTORTED_GUITAR);
        bassPart.setInstrument(ProgramChanges.ELECTRIC_BASS);

        loadMatrices();
    }

    private void create() throws Exception {
        //intro
        add(generateChords(6, 16, 0.5, 1.0, 0), 1);
        add(generateChords(19, 16, 0.5, 1.0, 0), 1);

        //A
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_single")) {
                List<Chord> chords = generateChords(i, 5, 0.25, 0.5, -1);
                add(chords, 4);
                List<Chord> copy = Refs.chords.duplicate(chords);
                Refs.chords.transpose(copy, +1);
                add(copy, 4);
            }
        }

        //bridge
        add(generateChords(6, 32, 0.25, 0.5, 0), 1);

        //A'
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_multiple")) {
                List<Chord> chords = generateChords(i, 5, 0.25, 0.5, -1);
                add(chords, 4);
                List<Chord> copy = Refs.chords.duplicate(chords);
                Refs.chords.transpose(copy, +1);
                add(copy, 4);
            }
        }

        //solo
        add(generateChords(6, 64, 0.125, 0.25, +1), 1);
        add(generateChords(19, 64, 0.125, 0.25, +1), 1);

        //B
        int[] multiple = {2, 4, 7, 9, 11, 13, 15, 17, 20};
        int bMatrix = new Random().nextInt(multiple.length);
        add(generateChords(multiple[bMatrix], 511, 0.25, 0.5, -1), 1);

        //bridge
        add(generateChords(19, 32, 0.25, 0.5, 0), 1);

        //A
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_single")) {
                List<Chord> chords = generateChords(i, 5, 0.25, 0.5, -1);
                add(chords, 4);
                List<Chord> copy = Refs.chords.duplicate(chords);
                Refs.chords.transpose(copy, +1);
                add(copy, 4);
            }
        }

        //outro
        add(generateChords(6, 16, 0.5, 1.0, 0), 1);
        add(generateChords(19, 16, 0.5, 1.0, 0), 1);
        
        //end
        add(generateChords(6, 2, 4.0, 4.5, 0), 1);
        add(generateChords(19, 2, 4.0, 4.5, 0), 1);
        
        

        //generateDrums();

        write("instru-metal-code-2");
    }

    private void loadMatrices() throws Exception {
        String[] names = {
            "Orion_guitar_1_single",
            "Orion_guitar_2_multiple",
            "Orion_guitar_2_single",
            "Orion_guitar_3_multiple",
            "Orion_guitar_3_single",
            "The_Call_Of_Ktulu_guitar_2_classes",
            "The_Call_Of_Ktulu_guitar_2_multiple",
            "The_Call_Of_Ktulu_guitar_2_single",
            "The_Call_Of_Ktulu_guitar_4_multiple",
            "The_Call_Of_Ktulu_guitar_4_single",
            "The_Call_Of_Ktulu_guitar_5_multiple",
            "The_Call_Of_Ktulu_guitar_5_single",
            "To_Live_Is_To_Die_guitar_1_multiple",
            "To_Live_Is_To_Die_guitar_1_single",
            "To_Live_Is_To_Die_guitar_2_multiple",
            "To_Live_Is_To_Die_guitar_2_single",
            "To_Live_Is_To_Die_guitar_3_multiple",
            "To_Live_Is_To_Die_guitar_3_single",
            "To_Live_Is_To_Die_guitar_4_classes",
            "To_Live_Is_To_Die_guitar_6_multiple",
            "To_Live_Is_To_Die_guitar_6_single"
        };

        matrices.add(null);//indices from 1 :P

        for (String n : names) {
            //read serialized matrix
            matrices.add(Refs.matrices.readFromDisk(n));
        }
    }

    private List<Chord> generateChords(int matrixNumber, int length, double mean, double deviation, int transposition) {
        Matrix m = matrices.get(matrixNumber);
        List<Chord> chords;

        if (m.getName().contains("_classes")) {
            //single pitch class
            //generate sets
            List<PCSet> sets = m.generateSets(length);
            chords = Refs.chords.convertSetsToChords(sets);
        } else {
            //operations
            List<String> operations = m.generateSteps(length);
            //from ops createSong tonnetz node list
            List<PCSNode> nodes = Refs.tonnetz.generateNodes(operations);
            //from nodes createSong list of chords
            chords = Refs.chords.convertNodesToChords(nodes);
        }

        Refs.chords.applyDuration(chords, new NormalDistribution(mean, deviation));
        Refs.chords.transpose(chords, transposition);

        return chords;
    }

    private List<Chord> generateBass(List<Chord> chords) {
        List<Chord> lowChords = new ArrayList<>();

        //copy lowest note
        for (Chord c : chords) {
            Note lower = c.getNote(0);
            Note bass = new Note(lower.getNote());
            Chord cn = new Chord();

            if (c.getDuration() >= 0.5) {
                Mod.transpose(bass, (-2 * 12));
            } else {
                bass.setPitch(Pitches.REST);
            }

            cn.setDuration(c.getDuration());
            cn.add(bass);

            lowChords.add(cn);
        }

        return lowChords;
    }

    private void generateDrums() {
        //drums
        Part drumsPart = new Part("Drums", 0, 9); // 9 = MIDI channel 10

        int beats = (int) Math.ceil(guitarPhrase.getEndTime() / 4);

        drumsPart.add(generateHat(beats));
        drumsPart.add(generateKick(beats));
        drumsPart.add(generateSnare(beats));

        score.addPart(drumsPart);
    }

    private Phrase generateHat(int length) {
        Phrase phrase = new Phrase(0.0);

        // make hats
        for (int i = 0; i < length * 4; i++) {
            Note note = new Note(42, 1);
            phrase.addNote(note);
        }

        return phrase;
    }

    private Phrase generateKick(int length) {
        // make bass drum
        Phrase phrase = new Phrase(0.0);

        for (int i = 0; i < length; i++) {
            Note note = new Note(36, 1);
            phrase.addNote(note);
            Note rest = new Note(Pitches.REST, 1);
            phrase.addNote(rest);
            phrase.addNote(note);
            phrase.addNote(rest);
        }

        return phrase;
    }

    private Phrase generateSnare(int length) {
        // make snare drum
        Phrase phrase = new Phrase(0.0);

        for (int i = 0; i < length; i++) {
            Note rest = new Note(Pitches.REST, 1);
            phrase.addNote(rest);
            Note note = new Note(38, 1);
            phrase.addNote(note);
            phrase.addNote(rest);
            phrase.addNote(note);            
        }

        return phrase;
    }

    private void add(List<Chord> chords, int times) {
        for (int i = 0; i < times; i++) {
            //chords
            guitarPhrase.addNoteList(Refs.chords.convertChordsToPhrase(chords).getNoteList(), true);
            //bass
            bassPhrase.addNoteList(Refs.chords.convertChordsToPhrase(generateBass(chords)).getNoteList(), true);
        }
    }

    private void write(String name) {
        Write.midi(score, name + ".mid");
    }
}
