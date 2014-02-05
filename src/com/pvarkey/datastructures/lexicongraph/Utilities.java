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
