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
import tfm.probability.NormalDistribution;

/**
 *
 * @author william
 */
public class Composition {

    public static void main(String[] args) throws Exception {
        Composition c = new Composition();
        c.create();
        c.write("experimento-3");
    }

    private List<Matrix> matrices;
    private Phrase guitarPhrase;
    private Phrase bassPhrase;
    private Score score;
    //drums
    private Part drumsPart;
    private Phrase hiHatPhrase;
    private Phrase snarePhrase;
    private Phrase kickPhrase;

    private int TEMPO = 120;
    private double[] gallopTimes = {0.5, 0.25, 0.25, -0.5, 0.25, 0.25, 0.5, 0.25, 0.25, -0.5, 0.25, 0.25};
    private double[] doubletimes = {0.25, 0.25, -1.5, 0.25, 0.25, -1.5};
    private double[] sixteenTimes = {0.25, 0.25, 0.25, 0.25, -0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, 0.25, -0.25, 0.25, 0.25, 0.25};
    private double[] soloTimes = {0.25, -0.25, -0.25, -0.25, -0.25, -0.25, -0.25, -0.25, 0.25, -0.25, -0.25, -0.25, -0.25, -0.25, -0.25, -0.25};
    private double[] rockTimes = {1, -1, 1, -1};

    private double[] durationArray;

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

        drumsPart = new Part("Drums", 0, 9); // 9 = MIDI channel 10
        score.addPart(drumsPart);

        hiHatPhrase = new Phrase();
        snarePhrase = new Phrase();
        kickPhrase = new Phrase();

        drumsPart.add(hiHatPhrase);
        drumsPart.add(snarePhrase);
        drumsPart.add(kickPhrase);

        loadMatrices();
    }

    private void create() throws Exception {
        durationArray = rockTimes;
        //apertura
        add(generateChords(19, durationArray.length, 0), true, false, false, 2);
        //intro 1
        add(generateChords(6, durationArray.length, 0), 4);
        //intro 2
        add(generateChords(19, durationArray.length, 0), 4);
        //intro 3
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);

        //A
        durationArray = gallopTimes;
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_single")) {
                List<Chord> chords = generateChords(i, durationArray.length, -1);
                add(chords, 4);
            }
        }

        //Puente A1
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);
        //Puente A2
        durationArray = doubletimes;
        add(generateChords(6, durationArray.length, 0), 4);
        //Puente A3
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);

        //A'
        durationArray = gallopTimes;
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_multiple")) {
                List<Chord> chords = generateChords(i, durationArray.length, -1);
                add(chords, 2);
                List<Chord> copy = Refs.chords.duplicate(chords);
                Refs.chords.transpose(copy, +1);
                add(copy, 1);
                add(chords, true, false, false, 1);
            }
        }

        //Puente A'1
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);
        durationArray = soloTimes;
        //Solo 1
        add(generateChords(6, durationArray.length, +1), 4);
        //Solo 2
        add(generateChords(6, durationArray.length, +1), 4);
        durationArray = doubletimes;
        //Puente A'2
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);
        //Puente A'3
        add(generateChords(19, durationArray.length, 0), false, true, false, 4);

        //B
        durationArray = sixteenTimes;
        int[] multiple = {2, 4, 7, 9, 11, 13, 15, 17, 20};
        int bMatrix = new Random().nextInt(multiple.length);
        for (int i = 0; i < 48; i++) {
            add(generateChords(multiple[bMatrix], durationArray.length, -1), 1);
        }

        //Puente B1
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);
        //Puente B2
        durationArray = doubletimes;
        add(generateChords(19, durationArray.length, 0), 4);
        //Puente B3
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);

        //A
        durationArray = gallopTimes;
        for (int i = 1; i < matrices.size(); i++) {
            if (matrices.get(i).getName().endsWith("_single")) {
                List<Chord> chords = generateChords(i, durationArray.length, -1);
                add(chords, 2);
            }
        }

        //Outro 1
        add(generateChords(19, durationArray.length, 0), false, false, true, 2);
        durationArray = rockTimes;
        //Outro 2
        add(generateChords(6, durationArray.length, 0), 4);
        //Outro 3
        add(generateChords(19, durationArray.length, 0), 4);
        //Cierre
        add(generateChords(19, durationArray.length, 0), true, false, false, 2);
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

    private List<Chord> generateChords(int matrixNumber, int length, double duration, int transposition) {
        List<Chord> chords = gen(matrixNumber, length, transposition);
        Refs.chords.applyDuration(chords, duration);
        return chords;
    }

    private List<Chord> generateChords(int matrixNumber, int length, double mean, double deviation, int transposition) {
        List<Chord> chords = gen(matrixNumber, length, transposition);
        Refs.chords.applyDuration(chords, new NormalDistribution(mean, deviation));
        return chords;
    }

    private List<Chord> generateChords(int matrixNumber, int length, int transposition) {
        List<Chord> chords = gen(matrixNumber, length, transposition);
        Refs.chords.applyDuration(chords, durationArray);
        return chords;
    }

    private List<Chord> gen(int matrixNumber, int length, int transposition) {
        Matrix m = matrices.get(matrixNumber);
        List<Chord> chords;

        if (m.getName().contains("_classes")) {
            //single pitch class
            //generate sets
            List<PCSet> sets = m.generateSets(length);
            chords = Refs.chords.convertSetsToChords(sets);
        } else {
            //operations
            List<String> operations = m.generateSteps(length - 1);
            //from ops createSong tonnetz node list
            List<PCSNode> nodes = Refs.tonnetz.generateNodes(operations);
            //from nodes createSong list of chords
            chords = Refs.chords.convertNodesToChords(nodes);
        }

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

            //if (c.getDuration() <= 0.25) {
            Mod.transpose(bass, (-2 * 12));
//	    } else {
//		bass.setPitch(Pitches.REST);
//	    }

            cn.setDuration(c.getDuration());
            cn.add(bass);

            lowChords.add(cn);
        }

        return lowChords;
    }

    private void generateDrums() {
        generateHat();
        generateKick();
        generateSnare();
    }

    private void generateHat() {
        // make hats
        for (int i = 0; i < 4; i++) {
            Note note = new Note(42, 1);
            hiHatPhrase.addNote(note);
        }
    }

    private void generateKick() {
        // make bass drum
        for (int j = 0; j < durationArray.length; j++) {
            if (durationArray[j] < 0.0) {
                kickPhrase.addNote(new Note(Pitches.REST, Math.abs(durationArray[j])));
            } else {
                kickPhrase.addNote(new Note(36, durationArray[j]));
            }
        }
    }

    private void generateSnare() {
        Note rest = new Note(Pitches.REST, 1);
        snarePhrase.addNote(rest);
        Note note = new Note(38, 1);
        snarePhrase.addNote(note);
        snarePhrase.addNote(rest);
        snarePhrase.addNote(note);
    }

    private void add(List<Chord> chords, int times) {
        for (int i = 0; i < times; i++) {
            //chords
            guitarPhrase.addNoteList(Refs.chords.convertChordsToPhrase(chords).getNoteList(), true);
            //bass
            bassPhrase.addNoteList(Refs.chords.convertChordsToPhrase(generateBass(chords)).getNoteList(), true);
            //drums
            generateDrums();
        }
    }

    private void write(String name) {
        Write.midi(score, name + ".mid");
    }

    private void add(List<Chord> chords, boolean guitar, boolean bass, boolean drums, int times) {

        for (int i = 0; i < times; i++) {
            //chords
            if (guitar && !bass && !drums) {
                guitarPhrase.addNoteList(Refs.chords.convertChordsToPhrase(chords).getNoteList(), true);
                bassPhrase.addNote(new Note(Pitches.REST, 4.0));
                hiHatPhrase.addNote(new Note(Pitches.REST, 4.0));
                snarePhrase.addNote(new Note(Pitches.REST, 4.0));
                kickPhrase.addNote(new Note(Pitches.REST, 4.0));
            }

            if (bass && !guitar && !drums) {
                guitarPhrase.addNote(new Note(Pitches.REST, 4.0));
                bassPhrase.addNoteList(Refs.chords.convertChordsToPhrase(generateBass(chords)).getNoteList(), true);
                hiHatPhrase.addNote(new Note(Pitches.REST, 4.0));
                snarePhrase.addNote(new Note(Pitches.REST, 4.0));
                kickPhrase.addNote(new Note(Pitches.REST, 4.0));
            }

            if (drums && !guitar && !bass) {
                guitarPhrase.addNote(new Note(Pitches.REST, 4.0));
                bassPhrase.addNote(new Note(Pitches.REST, 4.0));
                generateDrums();
            }
        }
    }
}
