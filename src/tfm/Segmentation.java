/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.HashMap;
import java.util.Map;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;

/**
 *
 * @author william
 */
public class Segmentation {

    public static void main(String[] args) {
	Segmentation s = new Segmentation();
	s.process("Metallica_Orion_guitar_1.mid");
    }

    //extract parts
    //find repetitions
    //write midis
    private Map<String, Integer> partNumbers = new HashMap<>();

    public void process(String fileName) {
	Score fullScore = new Score();
	Read.midi(fullScore, fileName);

	//each part in
	for (Part part : fullScore.getPartArray()) {
	    //each phrase
	    for (Phrase phrase : part.getPhraseArray()) {
		//each note
		for(Note note: phrase.getNoteArray()){
		    
		}
		
	    }
	}
    }

    private Integer getPartNumber(String fileName) {
	if (partNumbers.get(fileName) == null) {
	    partNumbers.put(fileName, 1);
	}

	Integer p = partNumbers.get(fileName);

	partNumbers.put(fileName, p + 1);

	return p;
    }

}
