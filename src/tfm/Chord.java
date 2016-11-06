/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import jm.music.data.Note;

/**
 *
 * @author casa
 */
public class Chord {

    private List<Note> notes;

    private Double startTime;

    public Chord() {
        notes = new ArrayList<>();
    }

    public void add(Note note) {
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

        for (Note n : notes) {
            s += n.getNote() + " ";
        }

        return s + "] "+startTime;
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
}
