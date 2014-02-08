/*******************************************************************************
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


public interface ILexiconGraph {

	public void add(String s);
	
	public void addAll(Collection<String> c, boolean concurrently);

	public boolean contains(String s);
	
	public boolean containsAll(Collection<String> c, boolean concurrently);
	
	public void ingestLexicon(String lexiconFileName, boolean overwrite, boolean concurrently);
	
	public int size();
	
	public void destroy();

}
