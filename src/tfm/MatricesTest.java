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
import jm.util.Write;
import tfm.model.chords.Chord;
import tfm.model.markov.Matrix;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;

/**
 *
 * @author casa
 */
public class MatricesTest {

    public static void main(String[] args) throws Exception {
        new MatricesTest().matrices();
    }

    private List<Matrix> matrices;

    public MatricesTest() throws Exception {
        matrices = new ArrayList<>();
        loadMatrices();
    }

    private void matrices() {
        for (int i = 1; i < matrices.size(); i++) {
            List<Chord> chords = generateChords(i);
            Score s = new Score();
            s.setTempo(120);
            Part part = new Part();
            part.setInstrument(ProgramChanges.DISTORTED_GUITAR);
            Phrase phrase = new Phrase();
            s.addPart(part);
            part.addPhrase(phrase);
            phrase.addNoteList(Refs.chords.convertChordsToPhrase(chords).getNoteList(), true);
            Write.midi(s, "matrix_" + i + ".mid");
        }
    }

    private List<Chord> generateChords(int matrixNumber) {
        Matrix m = matrices.get(matrixNumber);
        List<Chord> chords;

        if (m.getName().contains("_classes")) {
            //single pitch class
            //generate sets
            List<PCSet> sets = m.generateSets(32);
            chords = Refs.chords.convertSetsToChords(sets);
        } else {
            //operations
            List<String> operations = m.generateSteps(31);
            //from ops createSong tonnetz node list
            List<PCSNode> nodes = Refs.tonnetz.generateNodes(operations);
            //from nodes createSong list of chords
            chords = Refs.chords.convertNodesToChords(nodes);
        }

        Refs.chords.applyDuration(chords, 1.0);

        return chords;
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

}
