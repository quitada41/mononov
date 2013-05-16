package quitada;

import java.util.Comparator;

public class ByteDesComparator implements Comparator<Object> {
	@Override
	public int compare(Object s1, Object s2){
		long l1 = ((ObjectInfo) s1).getNumOfByte();
		long l2 = ((ObjectInfo) s2).getNumOfByte();
		return (int)(l2 - l1);
	}
}