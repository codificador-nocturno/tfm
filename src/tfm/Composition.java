/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.ArrayList;
import java.util.List;
import jm.constants.ProgramChanges;
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
        score.setNumerator(4);
        score.setDenominator(4);
        Part guitarPart = new Part();
        score.addPart(guitarPart);        
        guitarPart.setInstrument(ProgramChanges.DISTORTED_GUITAR);
        loadMatrices();

        Phrase f1 = generate(6, 26, 0.5, 1, 240);
        Mod.transpose(f1, -12);
        guitarPart.appendPhrase(f1);

        Phrase f2 = f1.copy();
        Mod.transpose(f2, +6);
        guitarPart.appendPhrase(f2);

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

    private Phrase generate(int matrixNumber, int length, double mean, double deviation, double tempo) {
        Matrix m = matrices.get(matrixNumber);
        Phrase p;
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
        p = Refs.chords.convertChordsToPhrase(chords);
        p.setTempo(tempo);

        return p;
    }

}
