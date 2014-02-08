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

public class Utilities {
	public static <T> Object[] insertAtIndex(Object[] array, int index, T o) {
		if(array == null) {
			Object[] newAmt = new Object[index+1];
			newAmt[index] = o;
			return newAmt;
		}
		Object[] newAmt = new Object[array.length + 1];
		for (int i = 0; i < index; i++)
			newAmt[i] = array[i];
		newAmt[index] = o;
		for (int i = index+1; i < newAmt.length; i++)
			newAmt[i] = array[i-1];
		return newAmt;
	}
	
}
