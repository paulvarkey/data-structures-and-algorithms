package com.pvarkey.datastructures.tst;


public interface ILexiconGraph {

	public void add(String s);

	public boolean contains(String s);
	
	public void ingestLexicon(String lexiconFileName, boolean overwrite);
	
	public int size();
	
	public void destroy();

}