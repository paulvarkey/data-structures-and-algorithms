/**
 * 
 */
package com.pvarkey.datastructures.unittests;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;


import org.junit.Test;

import com.pvarkey.datastructures.lexicongraph.HAMT;
import com.pvarkey.datastructures.lexicongraph.ILexiconGraph;
import com.pvarkey.datastructures.lexicongraph.TernarySearchTree;
import com.pvarkey.datastructures.lexicongraph.TernarySearchTree2;

/**
 * @author pvarkey
 * 
 */
public class LexiconGraphTest {

	/**
	 * Test method for {@link com.pvarkey.datastructures.lexicongraph.TernarySearchTree}.
	 */
	@Test
	public void testContainsTernarySearchTree() {
		ILexiconGraph ternarySearchTree = new TernarySearchTree();
		testContains(ternarySearchTree);
	}

	/**
	 * Test method for
	 * {@link com.pvarkey.datastructures.lexicongraph.TernarySearchTree#ingestLexicon(String, boolean)}
	 * .
	 */
	@Test
	public void testIngestLexiconTernarySearchTree() {
		TernarySearchTree ternarySearchTree = new TernarySearchTree();
		testIngestLexicon(ternarySearchTree);
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
	}
	
	
	@Test
	public void testIngestLexiconHAMT() {
		HAMT hamt = new HAMT((short)0b11111, (short)0);
		testIngestLexicon(hamt);
	}
	
	
	private <T extends ILexiconGraph> void testIngestLexicon(T concreteLexiconGraph)
	{
		String lexiconFileName = "data/Word-List.txt";
		String[] wordsInLexicon = {"AARDVARK", "REDEFINES"};
		String[] wordsNotInLexicon = {"KJAJKDBJKSDBDJHAJDASJDBHA"};

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
		
		for(String wordInLexicon : wordsInLexicon)
			assertTrue(concreteLexiconGraph.contains(wordInLexicon));
		for(String wordNotInLexicon : wordsNotInLexicon)
			assertFalse(concreteLexiconGraph.contains(wordNotInLexicon));
	}
	
	private <T extends ILexiconGraph> void testContains(T ternarySearchTree)
	{
		assertFalse(ternarySearchTree.contains(null));

		ternarySearchTree.add("Tree");
		assertTrue(ternarySearchTree.contains("Tree"));
		assertFalse(ternarySearchTree.contains("Trie"));
		assertFalse(ternarySearchTree.contains("Tried"));

		ternarySearchTree.add("Trie");
		assertTrue(ternarySearchTree.contains("Tree"));
		assertTrue(ternarySearchTree.contains("Trie"));
		assertFalse(ternarySearchTree.contains("Tried"));
		
		assertFalse(ternarySearchTree.contains("cute"));
		ternarySearchTree.add("cute");
		assertTrue(ternarySearchTree.contains("cute"));
		
		assertFalse(ternarySearchTree.contains("at"));
		ternarySearchTree.add("at");
		assertTrue(ternarySearchTree.contains("cute"));
		assertTrue(ternarySearchTree.contains("at"));
		
		assertFalse(ternarySearchTree.contains("as"));
		ternarySearchTree.add("as");
		assertTrue(ternarySearchTree.contains("as"));
		
		assertFalse(ternarySearchTree.contains("cup"));
		assertFalse(ternarySearchTree.contains("he"));
		assertFalse(ternarySearchTree.contains("us"));
		assertFalse(ternarySearchTree.contains("i"));
		
		ternarySearchTree.add("cup");
		ternarySearchTree.add("he");
		ternarySearchTree.add("us");
		ternarySearchTree.add("i");

		assertTrue(ternarySearchTree.contains("cup"));
		assertTrue(ternarySearchTree.contains("he"));
		assertTrue(ternarySearchTree.contains("us"));
		assertTrue(ternarySearchTree.contains("i"));
	}
}

