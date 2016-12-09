/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import tfm.utils.Utilities;
import tfm.analyisis.SetsAnalysis;
import tfm.analyisis.ChordsAnalysis;
import java.util.List;
import tfm.model.chords.Chord;
import tfm.model.chords.Chords;
import tfm.model.markov.Matrix;
import tfm.model.nrt.Tonnetz;
import tfm.model.pcst.PCSet;
import tfm.model.pcst.PCSets;

/**
 *
 * @author william
 */
public class Main {

    public static Utilities utilities;
    public static PCSets pcsets;
    public static Chords chords;
    public static Tonnetz tonnetz;

    public static void main(String[] args) {
        Main.utilities = new Utilities();
        Main.tonnetz = new Tonnetz();
        Main.chords = new Chords();
        Main.pcsets = new PCSets();

        Main m = new Main();

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
        //"Ejemplo_analisis_acordes"
        //"Orion_guitars",
        //"The_Call_Of_Ktulu_guitars",
        //"To_Live_Is_To_Die_guitars"
        };

        for (String fName : files) {
            SetsAnalysis sa = new SetsAnalysis();
            ChordsAnalysis ca = new ChordsAnalysis(fName);
            ca.read();
            List<Chord> allChords = ca.scanAll();
            m.singleNotes(allChords, sa);
            m.chords(allChords, sa);
        }

        //m.histograms();
    }

    private void singleNotes(List<Chord> chords, SetsAnalysis sa) {
        //to sets
        List<PCSet> sets = Main.pcsets.convertChordsToSets(chords);

        //single notes
        List<PCSet> single = sa.extractSets(sets, 1, 1);

        if (single.size() > 0) {
            Main.utilities.print(single);

            //extract markov matrix
            Matrix m = new Matrix();
            m.loadSets(single);
        }
    }

    private void chords(List<Chord> chords, SetsAnalysis sa) {

        //Main.utils.print(chords);
        //convert to sets
        List<PCSet> sets = Main.pcsets.convertChordsToSets(chords);
        //Main.utils.print(sets);

        //reduce sets
        List<PCSet> groups = sa.extractSets(sets, 2, 6);
        //Main.utils.print(groups);

        //fix sets
        List<PCSet> fixed = Main.pcsets.setsToTriads(groups, new Tonnetz());
        //Main.utils.print(fixed);

        //find transformations
        List<String> ops = Main.tonnetz.findTransformations(fixed);

        if (ops.size() > 0) {
            Main.utilities.print(fixed);
            Main.utilities.print(ops);

            //extract markov matrix
            Matrix m = new Matrix();
            m.loadOperations(ops);
        }

        /*
	    List<Chord> newChords = Main.utils.convertSetsToChords(fixed, -7);
	    Main.utils.writeChordsToMidi(newChords, fName, "from_sets", 128);
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
