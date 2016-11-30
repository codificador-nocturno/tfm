/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package tfm.model.nrt;

import tfm.model.pcst.PCSet;

/**
 *
 * @author william
 */
public class PCSNode {

    private PCSet set;
    private PCSNode L;
    private PCSNode P;
    private PCSNode R;
    private PCSNode I;

    public PCSNode(PCSet set) {
	this.set = set;
    }

    /**
     * @return the L
     */
    public PCSNode getL() {
	return L;
    }

    /**
     * @return the P
     */
    public PCSNode getP() {
	return P;
    }

    /**
     * @return the R
     */
    public PCSNode getR() {
	return R;
    }

    /**
     * @param L the L to set
     */
    public PCSNode setL(PCSNode node) {
	L = node;
	return this;
    }

    /**
     * @param P the P to set
     */
    public PCSNode setP(PCSNode node) {
	P = node;
	return this;
    }

    /**
     * @param R the R to set
     */
    public PCSNode setR(PCSNode node) {
	R = node;
	return this;
    }

    @Override
    public boolean equals(Object obj) {
	return set.equals(((PCSNode) obj).set);
    }

    /**
     * @return the set
     */
    public PCSet getSet() {
	return set;
    }

    /**
     * @return the I
     */
    public PCSNode getI() {
	return this;
    }

    @Override
    public String toString() {
	return set.toString();
    }

}
