package tfm.analyisis;

import java.util.ArrayList;
import java.util.List;
import tfm.model.pcst.PCSet;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author william
 */
public class SetsAnalysis {

    public List<PCSet> extractSets(List<PCSet> sets, int min, int max) {
	List<PCSet> candidates = new ArrayList<>();

	for (PCSet s : sets) {
	    if (s.size() >= min && s.size() <= max) {
		candidates.add(s);
	    }
	}

	return candidates;
    }

}
