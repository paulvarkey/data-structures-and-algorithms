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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;

public abstract class AbstractLexiconGraph implements ILexiconGraph 
{
	
	public void ingestLexicon(String lexiconFileName, boolean overwrite, boolean concurrently)
	{		
		if(lexiconFileName == null || lexiconFileName.isEmpty())
			throw new IllegalArgumentException("Lexicon filename cannot be null or empty!");
	
		File lexicon = new File(lexiconFileName);
		if (!lexicon.isFile() || !lexicon.canRead())
			throw new IllegalArgumentException("Lexicon file does not exist or is unreadable!");
			
		if (overwrite)
			destroy();
		
		HashSet<String> words = new HashSet<String>(178691);
		
		try(BufferedReader br = new BufferedReader(new FileReader(lexicon))) {
			br.readLine(); // skip first line -- it contains the count of the number of words
		    for(String line; (line = br.readLine()) != null; ) {
		    	words.add(line.trim());
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		long start = System.nanoTime(); 
		
		addAll(words, concurrently);
		
		double elapsedTimeInSec = (System.nanoTime() - start) * 1.0e-9;
		
		System.out.println(this.getClass().getSimpleName() + " took "  + elapsedTimeInSec + " seconds to ingest " + size() + " lexicon entries.");
	}
	
	
}
