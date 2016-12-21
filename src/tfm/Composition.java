/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.ArrayList;
import java.util.List;
import jm.constants.Pitches;
import jm.constants.ProgramChanges;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.music.tools.Mod;
import jm.util.View;
import jm.util.Write;
import jm.util.Play;
import tfm.model.chords.Chord;
import tfm.model.markov.Matrix;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;
import tfm.utils.NormalDistribution;

/**
 *
 * @author william
 */
public class Composition {

    public static void main(String[] args) throws Exception {
        Composition c = new Composition();
        c.createSong();
    }

    private List<Matrix> matrices;

    public Composition() {
        matrices = new ArrayList<>();
    }

    private void createSong() throws Exception {
        Score score = new Score();
        score.setDenominator(4);
        score.setNumerator(4);

        Part guitarPart = new Part("Guitar");
        Part bassPart = new Part("Bass");

        score.add(guitarPart);
        score.add(bassPart);

        guitarPart.setInstrument(ProgramChanges.DISTORTED_GUITAR);
        bassPart.setInstrument(ProgramChanges.ELECTRIC_BASS);

        loadMatrices();

        //generate first fragment
        //chords
        List<Chord> c1 = generateChords(1, 5, 1, 2);
        List<Chord> c2 = Refs.chords.duplicate(c1);

        Refs.chords.transpose(c1, -12);
        Phrase g1 = Refs.chords.convertChordsToPhrase(c1, 120);
        guitarPart.appendPhrase(g1);
        //bass
        Phrase b1 = Refs.chords.convertChordsToPhrase(generateBass(c1), 120);
        bassPart.appendPhrase(b1);

        //copy and transpose f1
        Refs.chords.transpose(c2, 6);
        Phrase g2 = Refs.chords.convertChordsToPhrase(c2, 120);
        guitarPart.appendPhrase(g2);
        //bass
        Phrase b2 = Refs.chords.convertChordsToPhrase(generateBass(c2), 120);
        bassPart.appendPhrase(b2);

        //repeat f1 an f2 bass part only
        guitarPart.appendPhrase(g1);
        bassPart.appendPhrase(b1);
        guitarPart.appendPhrase(g2);
        bassPart.appendPhrase(b2);

        //solo
        List<Chord> c3 = generateChords(6, 60, 0.5, 1.0);
        Phrase g3 = Refs.chords.convertChordsToPhrase(c3, 180);
        guitarPart.appendPhrase(g3);
        //bass
        Phrase b3 = Refs.chords.convertChordsToPhrase(generateBass(c3), 180);
        bassPart.appendPhrase(b3);

        Write.midi(score, "instru-metal-code.mid");
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

    private List<Chord> generateChords(int matrixNumber, int length, double mean, double deviation) {
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

        return chords;
    }

    private List<Chord> generateBass(List<Chord> chords) {
        List<Chord> lowChords = new ArrayList<>();

        //copy lowest note
        for (Chord c : chords) {
            Note lower = c.getNote(0);
            Note bass = new Note();
            Chord cn = new Chord();

            if (c.getDuration() < 0.9) {
                bass.setPitch(Pitches.REST);
            } else {
                bass.setPitch(lower.getPitch());
                Mod.transpose(bass, (-2 * 12));
            }

            cn.setDuration(c.getDuration());
            cn.add(bass);

            lowChords.add(cn);
        }

        return lowChords;
    }

}
