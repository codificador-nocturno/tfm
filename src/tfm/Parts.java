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
 *
 * @author casa
 */
public class Parts {

    public static void main(String[] args) {
        Parts pk = new Parts();
//
//        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_intro.mid", "KI");
//        pk.histogram();
//        
//        pk = new Parts();
//        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_ritmica.mid", "KR");
//        pk.histogram();
//        
//        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/ktulu_solo.mid", "KS");
//        pk.histogram();

//        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/orion_ritmica.mid", "OR");
//        pk.process();
//        pk.histogram();
//
//        pk = new Parts();
//        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/orion_solo.mid", "OS");
//        pk.process();
//        pk.histogram();
//
        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_intro.mid", "TI");
        pk.histogram();

        pk = new Parts();
        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_ritmica.mid", "TR");
        pk.histogram();

        pk = new Parts();
        pk.read("/home/casa/William/Dropbox/MIM/C2/TFM/MIDI/guitars/tolive_solo.mid", "TS");
        pk.histogram();
    }

    private Score score;

    public void read(String path, String title) {
        score = new Score();
        Read.midi(score, path);
        score.setTitle(title);
    }

    public void process() {
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
                    if (nextNote.getDynamic() >= 127) {
                        nextNote.setDynamic(126);
                    }
                    //nextNote.setDynamic(nextNote.getDynamic() - 1);

                }//notes
            }//phrases
        }//parts
    }

    public void histogram() {
        View.histogram(score, JMC.PITCH);
    }

}
