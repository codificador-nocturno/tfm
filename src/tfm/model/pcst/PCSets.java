/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.pcst;

import java.util.ArrayList;
import java.util.List;
import jm.music.data.Note;
import tfm.model.chords.Chord;
import tfm.model.nrt.PCSNode;
import tfm.model.nrt.Tonnetz;
import tfm.Refs;
import tfm.model.markov.Matrix;


/**
 *
 * @author casa
 */
public class PCSets {

    public  boolean contains(PCSet set1, PCSet set2) {
        if (set1.size() >= set2.size()) {
            for (Integer c : set2.getClasses()) {
                if (!set1.getClasses().contains(c)) {
                    return false;
                }
            }
            return true;
        } else {
            for (Integer c : set1.getClasses()) {
                if (!set2.getClasses().contains(c)) {
                    return false;
                }
            }
            return true;
        }
    }

    public  List<PCSet> convertChordsToSets(List<Chord> chords) {
        List<PCSet> sets = new ArrayList<>();

        for (Chord c : chords) {
            sets.add(new PCSet(c));
        }

        return sets;
    }

    public  int getPitchClass(Note note) {

        switch (note.getNote()) {
            case "B#":
            case "C":
                return 0;

            case "C#":
            case "Db":
                return 1;

            case "D":
                return 2;

            case "D#":
            case "Eb":
                return 3;

            case "E":
            case "Fb":
                return 4;

            case "E#":
            case "F":
                return 5;

            case "F#":
            case "Gb":
                return 6;

            case "G":
                return 7;

            case "G#":
            case "Ab":
                return 8;

            case "A":
                return 9;

            case "A#":
            case "Bb":
                return 10;

            case "B":
            case "Cb":
                return 11;

            default:
                System.out.println("Warning: pitch class set not found for " + note.getNote());
                return -1;

        }
    }

    public  List<PCSet> setsToTriads(List<PCSet> sets, Tonnetz tonnetz) {
        List<PCSet> candidates = new ArrayList<>();

        for (PCSet set : sets) {
            if (set.size() == 1) {
                //System.out.println("ignored size 1");
            } else if (set.size() == 3) {
                candidates.add(set);
            } else if (set.size() == 2) {
                for (int i = 0; i < 12; i++) {
                    set.getClasses().add(i);
                    if (tonnetz.getNode(set.getClasses().get(0), set.getClasses().get(1), set.getClasses().get(2)) != null) {
                        set.sort();
                        candidates.add(set);
                        break;
                    } else {
                        set.getClasses().remove(2);
                    }
                }
            } else {
                for (PCSNode n : tonnetz.getNodes()) {
                    if (Refs.pcsets.contains(set, n.getSet())) {
                        candidates.add(n.getSet());
                        break;
                    }
                }
            }

        }

        //check
        List<PCSet> fixed = new ArrayList<>();
        for (PCSet set : candidates) {
            if (tonnetz.getNode(set.getClasses().get(0), set.getClasses().get(1), set.getClasses().get(2)) != null) {
                fixed.add(set);
            }
        }
        return fixed;
    }
    
    public  String getNote(Integer pitchClass) {
	switch (pitchClass) {
	    case 0:
		return "C";

	    case 1:
		return "C#";

	    case 2:
		return "D";

	    case 3:
		return "Eb";

	    case 4:
		return "E";

	    case 5:
		return "F";

	    case 6:
		return "F#";

	    case 7:
		return "G";

	    case 8:
		return "Ab";

	    case 9:
		return "A";

	    case 10:
		return "Bb";

	    case 11:
		return "B";

	    default:
		System.out.println("Warning: note not found for pitch class" + pitchClass);
		return "";

	}
    }

    public List<String> setsToStringList(List<PCSet> sets) {
	List<String> strings=new ArrayList<>();
	
	for(PCSet p:sets){
	    strings.add(p.toString());
	}
	
	return strings;
    }    
}
