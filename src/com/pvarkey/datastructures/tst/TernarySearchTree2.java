package com.pvarkey.datastructures.tst;

import java.util.HashMap;

public class TernarySearchTree2 extends AbstractLexiconGraph
		implements ILexiconGraph 
{
	
	HashMap<Integer, Byte> tstArrayAsMap = null;
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

}
