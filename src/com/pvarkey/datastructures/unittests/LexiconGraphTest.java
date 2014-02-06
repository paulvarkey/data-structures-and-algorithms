/**
 * 
 */
package com.pvarkey.datastructures.unittests;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;


import org.junit.BeforeClass;
import org.junit.Test;

import com.pvarkey.datastructures.lexicongraph.CTrie;
import com.pvarkey.datastructures.lexicongraph.HAMT;
import com.pvarkey.datastructures.lexicongraph.ILexiconGraph;
import com.pvarkey.datastructures.lexicongraph.TernarySearchTree;
import com.pvarkey.datastructures.lexicongraph.TernarySearchTree2;

/**
 * @author pvarkey
 * 
 */
public class LexiconGraphTest {
	
	static int Nth = 3;
	static String lexiconFileName = "data/Word-List.txt";
	static String[] wordsInLexicon = {"AARDVARK", "REDEFINES"};
	static String[] wordsNotInLexicon = {"KJAJKDBJKSDBDJHAJDASJDBHA"};
	
	@BeforeClass 
	public static void initializeTestParametersHere() {

	}

	@Test
	public void testContainsTernarySearchTree() {
		ILexiconGraph ternarySearchTree = new TernarySearchTree();
		testContains(ternarySearchTree);
	}

	@Test
	public void testIngestLexiconTernarySearchTree() {
		TernarySearchTree ternarySearchTree = new TernarySearchTree();
		testIngestLexicon(ternarySearchTree);
		System.out.println();
	}
	
	@Test
	public void performanceTestContainsNthFromLexiconTernarySearchTree() {
		TernarySearchTree ternarySearchTree = new TernarySearchTree();
		performanceTestContainsNthFromLexicon(ternarySearchTree,Nth);
	}
	
	@Test
	public void testContainsTernarySearchTree2() {
		TernarySearchTree2 optimized2TernarySearchTree = new TernarySearchTree2();
		testContains(optimized2TernarySearchTree);
	}
	
	@Test
	public void testIngestLexiconOptimized2TernarySearchTree() {
		TernarySearchTree2 optimized2TernarySearchTree = new TernarySearchTree2();
		testIngestLexicon(optimized2TernarySearchTree);
		System.out.println();
	}
	
	@Test
	public void performanceTestContainsNthFromLexiconOptimized2TernarySearchTree() {
		TernarySearchTree2 optimized2TernarySearchTree = new TernarySearchTree2();
		performanceTestContainsNthFromLexicon(optimized2TernarySearchTree,Nth);
	}
	
	@Test
	public void testAddHAMT() {
		HAMT hamt = new HAMT((short)0b11111, (short)0);
		testAdd(hamt);
	}
	
	@Test
	public void testIngestLexiconHAMT() {
		HAMT hamt = new HAMT((short)0b11111, (short)0);
		testIngestLexicon(hamt);
		System.out.println();
	}
	
	@Test
	public void performanceTestContainsNthFromLexiconHAMT() {
		HAMT hamt = new HAMT((short)0b11111, (short)0);
		performanceTestContainsNthFromLexicon(hamt,Nth);
	}
	
	@Test
	public void testAddLexiconCTrie() {
		CTrie ctrie = new CTrie();
		testAdd(ctrie);
	}
	
	@Test
	public void testIngestLexiconCTrie() {
		CTrie ctrie = new CTrie();
		testIngestLexicon(ctrie);
		System.out.println();
	}
	
	@Test
	public void performanceTestContainsNthFromLexiconCTrie() {
		CTrie ctrie = new CTrie();
		performanceTestContainsNthFromLexicon(ctrie,Nth);
	}
	
	private <T extends ILexiconGraph> void testIngestLexicon(T concreteLexiconGraph)
	{
		for(String wordInLexicon : wordsInLexicon)
			assertFalse(concreteLexiconGraph.contains(wordInLexicon));

		File lexicon = new File(lexiconFileName);
		if (!lexicon.isFile() || !lexicon.canRead()) {
			try {
				concreteLexiconGraph.ingestLexicon(lexiconFileName, true);
				fail("ternarySearchTree.ingestLexicon() did not throw expected exception -- IllegalArgumentException, for non-existent or unreadable file!");
			} catch (IllegalArgumentException expectedException) {
				System.out.println(lexiconFileName + " does not exist or could not be read!");
				return;
			}
		}
		// else
		concreteLexiconGraph.ingestLexicon(lexiconFileName, true);
		
		for(String wordInLexicon : wordsInLexicon) {
			assertTrue(concreteLexiconGraph.contains(wordInLexicon));
		}
		for(String wordNotInLexicon : wordsNotInLexicon) {
			assertFalse(concreteLexiconGraph.contains(wordNotInLexicon));
		}
	}
	
	private <T extends ILexiconGraph> void performanceTestContainsNthFromLexicon(T concreteLexiconGraph, int N)
	{			
		if(lexiconFileName == null || lexiconFileName.isEmpty())
			throw new IllegalArgumentException("Lexicon filename cannot be null or empty!");
	
		File lexicon = new File(lexiconFileName);
		if (!lexicon.isFile() || !lexicon.canRead())
			throw new IllegalArgumentException("Lexicon file does not exist or is unreadable!");
		
		// first, ingest lexicon
		concreteLexiconGraph.ingestLexicon(lexiconFileName, true);
		
		long start = System.nanoTime(); 
		
		try(BufferedReader br = new BufferedReader(new FileReader(lexicon))) {
			br.readLine(); // skip first line -- it contains the count of the number of words
			int i = 0;
		    for(String line; (line = br.readLine()) != null; ) {
		    	i = (++i)%N;
		    	if (i == 0)
		    		assertTrue(concreteLexiconGraph.contains(line.trim()));
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		
		System.out.println(concreteLexiconGraph.getClass().getSimpleName() + " took "  + elapsedTimeInSec + " seconds for " + N + "th word containment test from lexicon.");
	}
	
	private <T extends ILexiconGraph> void testContains(T lexiconGraph)
	{
		assertFalse(lexiconGraph.contains(null));

		lexiconGraph.add("Tree");
		assertTrue(lexiconGraph.contains("Tree"));
		assertFalse(lexiconGraph.contains("Trie"));
		assertFalse(lexiconGraph.contains("Tried"));

		lexiconGraph.add("Trie");
		assertTrue(lexiconGraph.contains("Tree"));
		assertTrue(lexiconGraph.contains("Trie"));
		assertFalse(lexiconGraph.contains("Tried"));
		
		assertFalse(lexiconGraph.contains("cute"));
		lexiconGraph.add("cute");
		assertTrue(lexiconGraph.contains("cute"));
		
		assertFalse(lexiconGraph.contains("at"));
		lexiconGraph.add("at");
		assertTrue(lexiconGraph.contains("cute"));
		assertTrue(lexiconGraph.contains("at"));
		
		assertFalse(lexiconGraph.contains("as"));
		lexiconGraph.add("as");
		assertTrue(lexiconGraph.contains("as"));
		
		assertFalse(lexiconGraph.contains("cup"));
		assertFalse(lexiconGraph.contains("he"));
		assertFalse(lexiconGraph.contains("us"));
		assertFalse(lexiconGraph.contains("i"));
		
		lexiconGraph.add("cup");
		lexiconGraph.add("he");
		lexiconGraph.add("us");
		lexiconGraph.add("i");

		assertTrue(lexiconGraph.contains("cup"));
		assertTrue(lexiconGraph.contains("he"));
		assertTrue(lexiconGraph.contains("us"));
		assertTrue(lexiconGraph.contains("i"));
	}
	
	private <T extends ILexiconGraph> void testAdd(T lexiconGraph)
	{
		assertTrue(lexiconGraph.size() == 0);
		assertFalse(lexiconGraph.contains("first"));
		
		lexiconGraph.add("first");
		assertTrue(lexiconGraph.size() == 1);
		assertTrue(lexiconGraph.contains("first"));
		
		lexiconGraph.add("first");
		assertTrue(lexiconGraph.size() == 1);
		assertTrue(lexiconGraph.contains("first"));
		
		lexiconGraph.add("second");
		assertTrue(lexiconGraph.size() == 2);
		assertTrue(lexiconGraph.contains("second"));
	}
}

