/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import tfm.analyisis.SetsAnalysis;
import tfm.analyisis.ChordsAnalysis;
import java.util.List;
import tfm.model.chords.Chord;
import tfm.model.markov.Matrix;
import tfm.model.nrt.Tonnetz;
import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class Analysis {

    public static void main(String[] args) throws Exception {
        Analysis m = new Analysis();

        String[] files = {
            "Orion_guitar_1",
            "Orion_guitar_2",
            "Orion_guitar_3",
            "The_Call_Of_Ktulu_guitar_1",
            "The_Call_Of_Ktulu_guitar_2",
            "The_Call_Of_Ktulu_guitar_3",
            "The_Call_Of_Ktulu_guitar_4",
            "The_Call_Of_Ktulu_guitar_5",
            "To_Live_Is_To_Die_guitar_1",
            "To_Live_Is_To_Die_guitar_2",
            "To_Live_Is_To_Die_guitar_3",
            "To_Live_Is_To_Die_guitar_4",
            "To_Live_Is_To_Die_guitar_5",
            "To_Live_Is_To_Die_guitar_6",
            "To_Live_Is_To_Die_guitar_7"
        };

        for (String fName : files) {
            SetsAnalysis sa = new SetsAnalysis();
            ChordsAnalysis ca = new ChordsAnalysis(fName);
            ca.read();
            List<Chord> allChords = ca.scanAll();
            m.singleNotes(fName, allChords, sa);
            m.chords(fName, allChords, sa);
        }

        //m.histograms();
    }

    private void singleNotes(String name, List<Chord> chords, SetsAnalysis sa) throws Exception {
        //to sets
        List<PCSet> sets = Refs.pcsets.convertChordsToSets(chords);

        //single set
        List<PCSet> single = sa.extractSets(sets, 1, 1);

        if (single.size() > 0) {
            //Main.utilities.printList(single);

            //extract markov matrix
            Matrix m = new Matrix(name + "_classes");
            m.loadClasses(Refs.pcsets.setsToStringList(single));
            if (m.check()) {
                m.print();
                Refs.matrices.writeToDisk(m);
            }
        }
    }

    private void chords(String name, List<Chord> chords, SetsAnalysis sa) throws Exception {

        //Main.utils.printList(chords);
        //convert to sets
        List<PCSet> sets = Refs.pcsets.convertChordsToSets(chords);
        //Main.utils.printList(sets);

        //reduce sets
        List<PCSet> groups = sa.extractSets(sets, 2, 6);
        //Main.utils.printList(groups);

        //fix sets
        List<PCSet> fixed = Refs.pcsets.setsToTriads(groups, new Tonnetz());
        //Main.utils.printList(fixed);

        //find transformations
        List<String> ops = Refs.tonnetz.findTransformations(fixed);

        if (ops.size() > 0) {
            //Main.utilities.printList(fixed);
            Refs.utilities.printList(ops);

            //compund op matrix
            Matrix m = new Matrix(name + "_multiple");
            m.loadMultipleOp(ops);
            if (m.check()) {
                m.print();
                Refs.matrices.writeToDisk(m);
            }

            //single op matrix
            m = new Matrix(name + "_single");
            m.loadSingleOp(Refs.utilities.multipleToSingleOps(ops));
            if (m.check()) {
                m.print();
                Refs.matrices.writeToDisk(m);
            }
        }

        /*
	    List<Chord> newChords = Analysis.utils.convertSetsToChords(fixed, -7);
	    Analysis.utils.writeChordsToMidi(newChords, fName, "from_sets", 128);
         */
    }

//    private void histograms() {
//	String[] hFiles = {
//	    "Orion_guitars",
//	    "The_Call_Of_Ktulu_guitars",
//	    "To_Live_Is_To_Die_guitars"
//	};
//
//	HistogramAnalysis h = new HistogramAnalysis(file, file);
//	h.histogram();
//
//    }
}
