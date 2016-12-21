/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.utils;

import java.util.ArrayList;
import java.util.List;
import jm.constants.Durations;
import jm.music.data.Note;
import jm.music.data.Phrase;

/**
 *
 * @author william
 */
public class Utilities {

    public void printList(List<? extends Object> list) {
	if (list.isEmpty()) {
	    System.out.println("[empty]");
	    return;
	}

	System.out.println("");
	for (Object o : list) {
	    System.out.print(o + " ");
	}
	System.out.println("");
    }

    public List<String> multipleToSingleOps(List<String> ops) {
	List<String> basic=new ArrayList<>();
		
	for(String complex:ops){
	    for(char o:complex.toCharArray()){
		basic.add(String.valueOf(o));
	    }
	}
	
	return basic;
    }

    public Phrase generateSilences(Phrase phrase) {
	Phrase p=new Phrase();
	
	for(Object o:phrase.getNoteList()){
	    Note n=(Note) o;
	    
	    Note nn=new Note();
	    nn.setDuration(n.getDuration());
	    nn.setPitch(Note.REST);
	    
	    p.add(nn);
	}
	
	return p;
    }

}
