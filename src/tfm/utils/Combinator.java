/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author william
 */
public class Combinator {
    //http://codereview.stackexchange.com/questions/41510/calculate-all-possible-combinations-of-given-characters

    private List<String> paths;

    List<String> ops;

    public Combinator() {
	paths = new ArrayList<>();
	ops = new ArrayList<>();
	ops.add("M");
	ops.add("L");
	ops.add("R");
	ops.add("P");
    }

    public void getCombinations(int size, String curr) {
	// If the current string has reached it's maximum length
	if (curr.length() == size) {
	    paths.add(curr);
	    // Else add each letter from the alphabet to new strings and process these new strings again
	} else {
	    for (int i = 0; i < ops.size(); i++) {
		String oldCurr = curr;
		curr += ops.get(i);
		getCombinations(size, curr);
		curr = oldCurr;
	    }
	}
    }

    /**
     * @return the paths
     */
    public List<String> getPaths() {
	return paths;
    }
}
