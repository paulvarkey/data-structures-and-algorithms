/*******************************************************************************
 * Licensed under the ISC License :
 * http://en.wikipedia.org/wiki/ISC_license
 * 
 * Copyright (c) 2014 pvarkey.
 * 
 * Permission to use, copy, modify, and/or distribute this software for
 * any purpose with or without fee is hereby granted, provided that the 
 * above copyright notice and this permission notice appear in all copies.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS 
 * ALL WARRANTIES WITH REGARD TO THIS SOFTWARE INCLUDING 
 * ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS. IN 
 * NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT, 
 * INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES 
 * WHATSOEVER RESULTING FROM LOSS OF USE, DATA OR PROFITS, 
 * WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR OTHER 
 * TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH 
 * THE USE OR PERFORMANCE OF THIS SOFTWARE.
 ******************************************************************************/
package com.pvarkey.datastructures.lexicongraph;

import java.util.Collection;
import java.util.HashMap;

public class TernarySearchTree2 extends AbstractLexiconGraph
		implements ILexiconGraph 
{
	
	HashMap<Integer, Byte> tstArrayAsMap = null;
	final Object locker = new Object();
	int size = 0;

	@Override
	public void add(String s) {
		if (s == null || s == "") throw new IllegalArgumentException();
		if(tstArrayAsMap == null)
		{
			tstArrayAsMap = new HashMap<Integer,Byte>(s.length());
			for(int i = 0; i < s.length(); i++)
				tstArrayAsMap.put(
						(int) (Math.pow(3, i)-1), 
						maskCharAsByte(s.charAt(i))
				);
			tstArrayAsMap.put(
					(int) (Math.pow(3, s.length()-1)-1), 
					maskCharAsByte(s.charAt(s.length()-1), true)
			);
			size++;
			return;
		}
		
		//else
		int arrIndex = 0;
		for(int i = 0; i < s.length();)
		{
			if( !tstArrayAsMap.containsKey(arrIndex) )
			{
				tstArrayAsMap.put(arrIndex, maskCharAsByte(s.charAt(i)));
				i++;
				arrIndex = arrIndex * 3 + 2;
				continue;
			}
			
			if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) == s.charAt(i))
			{
				i++;
				arrIndex = arrIndex * 3 + 2;
			}
			else if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) > s.charAt(i))
			{
				arrIndex = arrIndex * 3 + 1;
			}
			else if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) < s.charAt(i))
			{
				arrIndex = arrIndex * 3 + 3;
			}
		}
		arrIndex = (arrIndex - 2) / 3;
		tstArrayAsMap.put(arrIndex, maskCharAsByte(s.charAt(s.length()-1), true));
		size++;
	}

	@Override
	public boolean contains(String s) 
	{
		if (s == null)
			return false;
		
		if (s.isEmpty())
			return true;
		
		if(tstArrayAsMap == null || tstArrayAsMap.size() == 0)
			return false;
		
		int arrIndex = 0;
		for(int i = 0; i < s.length();)
		{
			if(!tstArrayAsMap.containsKey(arrIndex))
				return false;
			if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) == '\0')
				return false;
			if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) == s.charAt(i))
			{
				i++;
				arrIndex = arrIndex * 3 + 2;
			}
			else if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) > s.charAt(i))
			{
				arrIndex = arrIndex * 3 + 1;
			}
			else if(unmaskByteAsChar(tstArrayAsMap.get(arrIndex)) < s.charAt(i))
			{
				arrIndex = arrIndex * 3 + 3;
			}
		}
		
		arrIndex = (arrIndex - 2)/3;
		if(isWordTerminator(tstArrayAsMap.get(arrIndex)))
			return true;
		
		return false;
	}

	@Override
	public int size() {
		return size;
	}

	@Override
	public void destroy() {
		tstArrayAsMap = null;
		size = 0;
	}
	
	private char unmaskByteAsChar(byte maskedChar)
	{
		return (char) (maskedChar & (byte) 0x7F);
	}
	
	private byte maskCharAsByte(char unmaskedChar)
	{
		return maskCharAsByte(unmaskedChar, false);
	}
	
	private byte maskCharAsByte(char unmaskedChar, boolean isWordTerminator)
	{
		byte maskedChar = (byte) (unmaskedChar & (byte) 0x7F); 
		if(!isWordTerminator)
			return maskedChar;
		else
			return (byte) (maskedChar | (byte) 0x80);
	}
	
	private boolean isWordTerminator(byte maskedChar)
	{
		return (maskedChar & (byte) 0x80) >> 7 == -1;
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
