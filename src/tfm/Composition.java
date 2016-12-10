/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm;

import java.util.List;
import tfm.model.markov.Matrix;
import tfm.model.nrt.PCSNode;
import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class Composition {

    public static void main(String[] args) throws Exception {
        //read serialized matrices
        Matrix m = Refs.matrices.readFromDisk("Ejemplo_analisis_acordes_single");
        m.print();
        List<String> single = m.generateOperations(3);
        Refs.utilities.printList(single);
        //from ops create tonnetz node list
        List<PCSNode> nodes = Refs.tonnetz.generateNodes(single);
        Refs.utilities.printList(nodes);

        m = Refs.matrices.readFromDisk("Ejemplo_analisis_acordes_multiple");
        m.print();
        List<String> multiple = m.generateOperations(3);
        Refs.utilities.printList(multiple);
        //from ops create tonnetz node list
        nodes = Refs.tonnetz.generateNodes(multiple);
        Refs.utilities.printList(nodes);

        //m = Refs.matrices.readFromDisk("Orion_guitar_2_classes");
        //m.print();
        //List<PCSet> sets = m.generateSets(3);
        //Refs.utilities.printList(sets);
    }

}
