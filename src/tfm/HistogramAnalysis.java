/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.Enumeration;
import jm.music.data.Note;
import jm.music.data.Part;
import jm.music.data.Phrase;
import jm.music.data.Score;
import jm.util.Read;
import jm.util.View;

/**
 * El objetivo de esta clase es generar los histogramas de tono y ritmo de cada
 * parte de cada pieza.
 *
 * @author casa
 */
public class HistogramAnalysis {

    private Score score;

    public HistogramAnalysis(String path, String title) {
        score = new Score();
        Read.midi(score, path + ".mid");
        score.setTitle(title);
        fix();
    }

    private void fix() {
        Enumeration parts = score.getPartList().elements();

        while (parts.hasMoreElements()) {
            Part nextPart = (Part) parts.nextElement();

            Enumeration phrases = nextPart.getPhraseList().elements();
            while (phrases.hasMoreElements()) {
                Phrase nextPhrase = (Phrase) phrases.nextElement();

                Enumeration notes = nextPhrase.getNoteList().elements();
                while (notes.hasMoreElements()) {
                    Note nextNote = (Note) notes.nextElement();
                    //System.out.println(nextNote);
                    if (nextNote.getDynamic() >= 127) {//fixes an array out of bounds
                        nextNote.setDynamic(126);
                    }
                }//notes
            }//phrases
        }//parts
    }

    public void histogram() {
        View.histogram(score);
    }

    public static void main(String[] args) {
        HistogramAnalysis h = new HistogramAnalysis("Metallica_Orion_guitars", "Orion");
        h.histogram();

        h = new HistogramAnalysis("Metallica_The_Call_Of_Ktulu_guitars","Ktulu");
        h.histogram();

        h = new HistogramAnalysis("Metallica_To_Live_Is_To_Die_guitars", "ToLive");
        h.histogram();
    }

}
