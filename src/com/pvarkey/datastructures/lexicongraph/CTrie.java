package com.pvarkey.datastructures.lexicongraph;

import java.util.Arrays;
import java.util.concurrent.atomic.*;

import junit.framework.Assert;

/**
 * Reference: Prokopec, Bagwell & Odersky's papers from 2011 & 2012
 * @author pvarkey
 *
 */

public class CTrie extends AbstractLexiconGraph implements ILexiconGraph {
	
	final static int W = 5;

	AtomicReference<INode> root = new AtomicReference<INode>(null);
	volatile int size = 0;
	
	public CTrie() {
		
	}
	
	@Override
	public void add(String s) {
		INode r = root.get();
		if (r == null || isNullINode(r)) {
			SNode sSNode = new SNode(s, false);
			int maskIndex = (s.hashCode() >>> 0) & ((1 << W) - 1);
			int amtIndex = 0; // = Integer.bitCount((~0 >>> (32-maskIndex-1)) & (1 << maskIndex)) - 1;
			Object[] amt = new Object[amtIndex + 1];
			amt[amtIndex] = sSNode;
			CNode sCNode = new CNode(1 << maskIndex, amt);
			INode sINode = new INode(sCNode);
			if (!root.compareAndSet(r, sINode))
				add(s);
			else
				size++;
		}
		else {
			if(contains(s))
				return;
			else if (iadd(r, s, 0, null))
				size++;
			else
				add(s);
		}
	}
	
	private boolean iadd(INode i, String s, int level, INode parent) {
		Object mainNode = i.MainNode.get();
		
		if (mainNode == null) {
			if (parent != null)
				; // clean(parent)
			return false;
		}
		if (mainNode instanceof SNode) {
			if (((SNode) mainNode).tomb) {
				if (parent != null)
					; // clean(parent)
				return false;
			}
			else
				Assert.fail("No execution path must reach this point");
		}
		else if (mainNode instanceof CNode) {
			int maskIndex = (s.hashCode() >>> level) & ((1 << W) - 1);
			CNode mainNodeAsCNode = ((CNode) mainNode);
			int newMask = mainNodeAsCNode.mask | (1 << maskIndex);
			int amtIndex = Integer.bitCount((~0 >>> (32-maskIndex-1)) & newMask) - 1;
			SNode sSNode = new SNode(s, false);
			
			if ((mainNodeAsCNode.mask & (1 << maskIndex)) == 0) {
				Object[] amt = Utilities.insertAtIndex(mainNodeAsCNode.amt, amtIndex, sSNode);
				CNode sCNode = new CNode(newMask, amt);
				return i.MainNode.compareAndSet(mainNode, sCNode);
			}
			else if (mainNodeAsCNode.amt[amtIndex] instanceof SNode) {
				SNode sNodeAtAmtIndex = (SNode) mainNodeAsCNode.amt[amtIndex];
				if (!sNodeAtAmtIndex.tomb) {
					if (sNodeAtAmtIndex.k.equals(s))
						return false;
					else {
						// TODO: if collision at last level (i.e. level == 30), then, chain, unless full
						if (level + W >= 32) {
							if (mainNodeAsCNode.amt.length == 32) // it's full -- tough luck!
								return false; // throw RareErrorException instead?
							boolean isSet = ((mainNodeAsCNode.mask & (1 << maskIndex)) != 0);
							while(isSet) {
								maskIndex = (maskIndex + 1) % 32;
								isSet = ((mainNodeAsCNode.mask & (1 << maskIndex)) != 0);
								newMask = mainNodeAsCNode.mask | (1 << maskIndex);
								amtIndex = Integer.bitCount((~0 >>> (32-maskIndex-1)) & newMask) - 1;
								Object[] amt = Utilities.insertAtIndex(mainNodeAsCNode.amt, amtIndex, sSNode);
								CNode sCNode = new CNode(newMask, amt);
								return i.MainNode.compareAndSet(mainNode, sCNode);
							}
						}
//						int cNodeMask = 0;
//						Object[] amt = new Object[2];
						
//						maskIndex = (sNodeAtAmtIndex.k.hashCode() >>> (level + W)) & ((1 << W) - 1);
//						cNodeMask |= (1 << maskIndex);
//						amtIndex = 0; // = Integer.bitCount((~0 >>> (32-maskIndex-1)) & cNodeMask) - 1;
//						amt[amtIndex] = sNodeAtAmtIndex;
//						maskIndex = (sSNode.k.hashCode() >>> (level + W)) & ((1 << W) - 1);
//						cNodeMask |= (1 << maskIndex);
//						amtIndex = 1; // = Integer.bitCount((~0 >>> (32-maskIndex-1)) & cNodeMask) - 1;
//						amt[amtIndex] = sSNode;
//						CNode sCNode = new CNode(cNodeMask, amt);
//						INode sINode = new INode(sCNode);
//						
						
						CNode nCNode = new CNode(0, null);
						INode nINode = new INode(nCNode);
						iadd(nINode, sNodeAtAmtIndex.k, level + W, i);
						iadd(nINode, sSNode.k, level + W, i);
						
						CNode mainNodeAsCNodeCopy = new CNode(mainNodeAsCNode.mask, Arrays.copyOf(mainNodeAsCNode.amt, mainNodeAsCNode.amt.length));
						mainNodeAsCNodeCopy.amt[amtIndex] = nINode;
						return i.MainNode.compareAndSet(mainNode, mainNodeAsCNodeCopy);
					}
				}
				else
					Assert.fail("No execution path must reach this point");
			}
			else if (mainNodeAsCNode.amt[amtIndex] instanceof INode) {
				return iadd((INode) mainNodeAsCNode.amt[amtIndex], s, level + W, i);
			}
		}
		return false;
	}

	@Override
	public boolean contains(String s) {	
		INode r = root.get();
		
		if (r == null) 
			return false;
		else if (isNullINode(r)) {
			root.compareAndSet(r, null);
			return contains(s);
		}
		else {
			boolean result = false;
			try {
				result = icontains(r, s, 0, null);
			} catch (RequireRestartException e) {
				return contains(s);
			}
			return result;
		}
	}
	
	private boolean icontains(INode i, String s, int level, INode parent) 
			throws RequireRestartException	{
		
		Object mainNode = i.MainNode.get();
		
		if (mainNode == null) {
			if (parent != null)
				; // clean(parent)
			throw new RequireRestartException("MainNode of current INode is null pointer");
		}
		else if (mainNode instanceof SNode) {
			if (((SNode) mainNode).tomb) {
				if (parent != null)
					; // clean(parent)
				throw new RequireRestartException("MainNode of current INode is tombed SNode");
			}
			else
				Assert.fail("No execution path must reach this point");
		}
		else if (mainNode instanceof CNode) {
			// if last level, then it may have chained elements (from collisions), 
			// therefore, search whole array
			if (level + W >= 32) {
				for (Object sNode : ((CNode) mainNode).amt) {
					if(((SNode) sNode).k.equals(s))
						return true;
				}
				return false;
			}
			
			int maskIndex = (s.hashCode() >>> level) & ((1 << W) - 1);
			
			CNode mainNodeAsCNode = ((CNode) mainNode);
			
			if ((mainNodeAsCNode.mask & (1 << maskIndex)) == 0)
				return false;
						
			int amtIndex = Integer.bitCount((~0 >>> (32-maskIndex-1)) & mainNodeAsCNode.mask) - 1;
			
			if (mainNodeAsCNode.amt[amtIndex] instanceof SNode) {
				SNode sNodeAtAmtIndex = (SNode) mainNodeAsCNode.amt[amtIndex];
				if (!sNodeAtAmtIndex.tomb) {
					if (sNodeAtAmtIndex.k.equals(s))
						return true;
					else
						return false;
				}
				else
					Assert.fail("No execution path must reach this point");
			}
			else if (mainNodeAsCNode.amt[amtIndex] instanceof INode) {
				return icontains((INode) mainNodeAsCNode.amt[amtIndex], s, level + W, i);
			}
		}
		Assert.fail("No execution path must reach this point");
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void destroy() {
		root.set(null);
		size = 0;
	}
	
	
	class INode {
		AtomicReference<Object> MainNode = new AtomicReference<Object>(null);
		public INode(Object m) { MainNode.set(m); }
	}
	
	class SNode {
		String k = null;
		boolean tomb = false;
		public SNode(String s, boolean t) { k = s; tomb = t; }
	}
	
	class CNode {
		int mask = 0;
		Object[] amt = null;
		public CNode(int m, Object[] amtArray) { mask = m; amt = amtArray; }
	}
	
	private boolean isNullINode(INode inode)
	{
		if (inode == null) throw new NullPointerException();
		
		if (inode.MainNode.get() == null) return true;
		
		return false;
	}
	
	class RequireRestartException extends Exception {
		public RequireRestartException(String string) {
			super(string);
		}
	}

}
