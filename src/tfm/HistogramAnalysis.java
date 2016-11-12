/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.Enumeration;
import jm.JMC;
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

    public static void main(String[] args) {
        HistogramAnalysis h = new HistogramAnalysis();
        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_intro.mid", "KI");
        h.histogram();

        h = new HistogramAnalysis();
        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_ritmica.mid", "KR");
        h.histogram();

        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_solo.mid", "KS");
        h.histogram();

        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/orion_ritmica.mid", "OR");
        h.fix();
        h.histogram();

        h = new HistogramAnalysis();
        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/orion_solo.mid", "OS");
        h.fix();
        h.histogram();

        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_intro.mid", "TI");
        h.histogram();

        h = new HistogramAnalysis();
        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_ritmica.mid", "TR");
        h.histogram();

        h = new HistogramAnalysis();
        h.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_solo.mid", "TS");
        h.histogram();
    }

    private Score score;

    public void read(String path, String title) {
        score = new Score();
        Read.midi(score, path);
        score.setTitle(title);
    }

    public void fix() {
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
        View.histogram(score, JMC.PITCH);
    }

}
