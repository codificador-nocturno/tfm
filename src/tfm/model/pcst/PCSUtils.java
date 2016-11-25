/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.pcst;

import jm.music.data.Note;

/**
 *
 * @author william
 */
public class PCSUtils {

    public int getPitchClass(Note note) {
	int className = -1;

	switch (note.getNote()) {
	    case "B#":
	    case "C":
		className = 0;
		break;

	    case "C#":
	    case "Db":
		className = 1;
		break;

	    case "D":
		className = 2;
		break;

	    case "D#":
	    case "Eb":
		className = 3;
		break;

	    case "E":
	    case "Fb":
		className = 4;
		break;

	    case "E#":
	    case "F":
		className = 5;
		break;

	    case "F#":
	    case "Gb":
		className = 6;
		break;

	    case "G":
		className = 7;
		break;

	    case "G#":
	    case "Ab":
		className = 8;
		break;

	    case "A":
		className = 9;
		break;

	    case "A#":
	    case "Bb":
		className = 10;
		break;

	    case "B":
	    case "Cb":
		className = 11;
		break;

	}

	return className;
    }

}
