/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.List;
import tfm.model.chords.Chord;
import tfm.model.markov.Matrix;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class Composition {

    public static void main(String[] args) throws Exception {
        //read serialized matrices
        Matrix m = Refs.matrices.readFromDisk("The_Call_Of_Ktulu_guitar_2_single");
        //m.print();
        List<String> single = m.generateSteps(3);
        //Refs.utilities.printList(single);
        //from ops create tonnetz node list
        List<PCSNode> nodes = Refs.tonnetz.generateNodes(single);
        Refs.utilities.printList(nodes);
        //from nodes create list of chords
        List<Chord> chords = Refs.chords.convertNodesToChords(nodes);
        Refs.utilities.printList(chords);
        Refs.chords.writeChordsToMidi(chords, "The_Call_Of_Ktulu_guitar_2_single_algorithmic", 120);

        m = Refs.matrices.readFromDisk("The_Call_Of_Ktulu_guitar_2_multiple");
        //m.print();
        List<String> multiple = m.generateSteps(3);
        //Refs.utilities.printList(multiple);
        //from ops create tonnetz node list
        nodes = Refs.tonnetz.generateNodes(multiple);
        Refs.utilities.printList(nodes);
        chords = Refs.chords.convertNodesToChords(nodes);
        Refs.utilities.printList(chords);
        Refs.chords.writeChordsToMidi(chords, "The_Call_Of_Ktulu_guitar_2_multiple_algorihtmic", 120);

        m = Refs.matrices.readFromDisk("The_Call_Of_Ktulu_guitar_2_classes");
        //m.print();
        List<PCSet> sets = m.generateSets(10);
        Refs.utilities.printList(sets);
        chords = Refs.chords.convertSetsToChords(sets);
        Refs.utilities.printList(chords);
        Refs.chords.writeChordsToMidi(chords, "The_Call_Of_Ktulu_guitar_2_classes_algoritmic", 120);
    }

}
