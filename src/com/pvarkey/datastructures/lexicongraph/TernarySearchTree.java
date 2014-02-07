/**
 * 
 */
package com.pvarkey.datastructures.lexicongraph;

import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author pvarkey
 *
 */
public class TernarySearchTree extends AbstractLexiconGraph implements ILexiconGraph {
	
	class Node {
		char nodeChar;
		boolean isWordTerminator;
		Node left, center, right;
		Node() {
			nodeChar = '\0';
			isWordTerminator = false;
			left = center = right = null;
		}
		Node(char c) {
			nodeChar = c;
			left = center = right = null;
		}
	}
	
	Node root = null;
	final Object locker = new Object();
	int size = 0;
	
	private Node add(String s, int pos, Node node)
    {
        if (node == null) { 
        	node = new Node(s.charAt(pos));
        }
        if (s.charAt(pos) < node.nodeChar) { node.left = add(s, pos, node.left); }
        else if (s.charAt(pos) > node.nodeChar) { node.right = add(s, pos, node.right); }
        else
        {
            if (pos + 1 == s.length())
            {
            	node.isWordTerminator = true;
            	return node;
            }
            else { 
            	node.center = add(s, pos + 1, node.center); 
            }
        }
        return node;
    }

    /* (non-Javadoc)
	 * @see com.pvarkey.datastructures.tst.LexiconGraph#add(java.lang.String)
	 */
    @Override
	public void add(String s)
    {
        if (s == null || s == "") throw new IllegalArgumentException();
        root = add(s, 0, root);
        size++;
    }
	
	/* (non-Javadoc)
	 * @see com.pvarkey.datastructures.tst.LexiconGraph#contains(java.lang.String)
	 */
	@Override
	public boolean contains (String s)
	{
		if (s == null)
			return false;
		
		if (s.isEmpty())
			return true;
		
		Node cur = root;
		
		int sIndex = 0;
		int sLen = s.length();
		while(cur != null) {
			if (s.charAt(sIndex) < cur.nodeChar)
				cur = cur.left;
			else if (s.charAt(sIndex) > cur.nodeChar)
				cur = cur.right;
			else { // (s.charAt(sIndex) == cur.nodeChar)
				if(++sIndex == sLen)
				{
					if(cur.isWordTerminator)
						return true;
					else
						return false;
				}
				cur = cur.center;
			}
		}
		
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void destroy() 
	{
		root = null;
		size = 0;
	}

	@Override
	public void addAll(Collection<String> c, boolean concurrently) {
		if (concurrently) {
			for (String word : c) {
				synchronized(locker) {
					add(word);
				}
			}
		}
		else {
			for (String word : c) {
		        add(word);
			}
		}
	}

	@Override
	public boolean containsAll(Collection<String> c, boolean concurrently) {
		if (concurrently) {
			for (String word : c) {
				synchronized(locker) {
					if(!contains(word))
						return false;
				}
			}
		}
		else {
			for (String word : c) {
	    		if (!contains(word))
	    			return false;
			}
		}
		return true;
	}
}
