package com.pvarkey.datastructures.lexicongraph;

public class HAMT extends AbstractLexiconGraph implements ILexiconGraph {
	
	final static int W = 5;
	
	short indexExtractorCode = 0b11111;
	short indexExtractorShift = 0;
	int mask = 0;
	Object[] amt = null;
	int lexiconSize = 0;
	
	public HAMT(short _indexExtractorCode, short _indexExtractorShift)
	{
		this.indexExtractorCode = _indexExtractorCode;
		this.indexExtractorShift = _indexExtractorShift;
	}

	@Override
	public void add(String s) {
			
		if (s == null || s == "") throw new IllegalArgumentException();
		
		int maskIndex = (s.hashCode() & (indexExtractorCode << indexExtractorShift)) >>> indexExtractorShift;
		// the following from Prokopec, et al. (2011) is more efficient
		// int maskIndex = (s.hashCode() >>> indexExtractorShift) & indexExtractorCode;
		
		// is mask set at index?
		boolean isSet = ((mask & (1 << maskIndex)) != 0);
		
		// if there is collision with *another* string, 
		// chain until next free slot OR sub-HAMT OR non-colliding string
		while(isSet && (amt[getAmtIndex(maskIndex)] instanceof String)
					&& (amt[getAmtIndex(maskIndex)].hashCode() == s.hashCode())) {
			// if current string is already present, return
			if (s.equals((String)amt[getAmtIndex(maskIndex)])) 
				return;
			maskIndex = (maskIndex + 1) % 32;
			isSet = ((mask & (1 << maskIndex)) != 0);
		}
		
		// set mask at index and proceed
		mask = mask | (1 << maskIndex);
		
		if(!isSet)
			amt = Utilities.insertAtIndex(amt, getAmtIndex(maskIndex), s);
		else
		{
			if (amt[getAmtIndex(maskIndex)] instanceof String)
			{
				HAMT subHAMT = new HAMT(indexExtractorCode, (short) (indexExtractorShift + W));
				subHAMT.add((String)amt[getAmtIndex(maskIndex)]);
				subHAMT.add(s);
				amt[getAmtIndex(maskIndex)] = subHAMT;
			}
			else // (amt[getAmtIndex(maskIndex)] instanceof HAMT)
			{
				HAMT subHAMT = (HAMT)amt[getAmtIndex(maskIndex)];
				subHAMT.add(s);
			}
		}
		
		lexiconSize++;
	}

	@Override
	public boolean contains(String s) {
		if (s == null)
			return false;
		
		if (s.isEmpty())
			return true;
		
		int maskIndex = (s.hashCode() & (indexExtractorCode << indexExtractorShift)) >>> indexExtractorShift;
		
		// is mask set at index?
		boolean isSet = ((mask & (1 << maskIndex)) != 0);
					
		if(!isSet)
			return false;
		
		while(isSet) {				
			if((amt[getAmtIndex(maskIndex)] instanceof HAMT))
				return ((HAMT) amt[getAmtIndex(maskIndex)]).contains(s);
			
			if(s.equals(amt[getAmtIndex(maskIndex)]))
				return true;
			
			if (amt[getAmtIndex(maskIndex)].hashCode() != s.hashCode())
				return false;
			
			// in case of collision,
			maskIndex = (maskIndex + 1) % 32;
			isSet = ((mask & (1 << maskIndex)) != 0);
		}
		
		return false;
	}

	@Override
	public int size() {
		return lexiconSize;
	}

	@Override
	public void destroy() {
		indexExtractorCode = 0b11111;
		mask = 0;
		amt = null;
		lexiconSize = 0;
	}
	
	private int getAmtIndex(int maskIndex) {
		int amtIndex = -1;
		for(int i = maskIndex; i >= 0; i--)
			if ((mask & (1 << i)) != 0)
				amtIndex++;
		return amtIndex;
		// the following from Prokopec, et al. (2011) may be more efficient?
		// return Integer.bitCount(((1 << (maskIndex + 1)) - 1) & mask) - 1;
	}

}
