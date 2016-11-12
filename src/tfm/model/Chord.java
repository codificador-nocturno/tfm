/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jm.constants.Scales;
import jm.music.data.Note;

/**
 *
 * @author casa
 */
public class Chord {

    private List<Note> notes;

    private Double startTime;
    
    private Double duration;

    public Chord() {
        notes = new ArrayList<>();
    }

    public void add(Note note) {
        //dont add duplicates
        for(Note n:notes){
            if(n.getPitch()==note.getPitch()){
                return;
            }
        }
        notes.add(note);
        sort();
    }

    private void sort() {
        Collections.sort(notes, new Comparator<Note>() {
            @Override
            public int compare(Note n1, Note n2) {
                return Integer.valueOf(n1.getPitch()).compareTo(Integer.valueOf(n2.getPitch()));
            }
        });
    }

    @Override
    public String toString() {
        String s = "[";

        boolean first = true;
        for (Note n : notes) {
            if (first) {
                first = false;
            } else {
                s += " ";
            }
            s += n.getNote();
        }

        return s + "]";
    }

    /**
     * @return the startTime
     */
    public Double getStartTime() {
        return startTime;
    }

    /**
     * @param startTime the startTime to set
     */
    public void setStartTime(Double startTime) {
        this.startTime = startTime;
    }

    public Chord duplicate() {
        Chord newC = new Chord();

        for (Note n : notes) {
            newC.add(n.copy());
        }

        return newC;
    }

    @Override
    public boolean equals(Object obj) {
        if (!obj.getClass().equals(Chord.class)) {
            return false;
        }

        if (obj.toString().equals(toString())) {
            return true;
        }

        return false;
    }

    public int size() {
        return notes.size();
    }

    public int[] getPitchesArray() {
        int[] pitches = new int[notes.size()];

        int i = 0;
        for (Note n : notes) {
            pitches[i] = n.getPitch();
            i++;
        }

        return pitches;
    }

    /**
     * @return the duration
     */
    public Double getDuration() {
        return duration;
    }

    /**
     * @param duration the duration to set
     */
    public void setDuration(Double duration) {
        this.duration = duration;
    }

}
