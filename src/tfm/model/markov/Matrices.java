/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.markov;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 *
 * @author casa
 */
public class Matrices {

    public Matrix readFromDisk(String name) throws Exception {
	FileInputStream fileIn = new FileInputStream(name + ".ser");
	ObjectInputStream in = new ObjectInputStream(fileIn);

	Matrix m = (Matrix) in.readObject();
	in.close();
	fileIn.close();

	return m;
    }

    public void writeToDisk(Matrix m) throws Exception {
	FileOutputStream fileOut = new FileOutputStream(m.getName() + ".ser");
	ObjectOutputStream out = new ObjectOutputStream(fileOut);
	out.writeObject(m);
	out.close();
	fileOut.close();
    }

}
