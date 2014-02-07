package com.pvarkey.datastructures.lexicongraph;

import java.util.Collection;


public interface ILexiconGraph {

	public void add(String s);
	
	public void addAll(Collection<String> c, boolean concurrently);

	public boolean contains(String s);
	
	public boolean containsAll(Collection<String> c, boolean concurrently);
	
	public void ingestLexicon(String lexiconFileName, boolean overwrite, boolean concurrently);
	
	public int size();
	
	public void destroy();

}