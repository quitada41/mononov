package quitada;

import java.util.Comparator;

public class DataComparator implements Comparator<Object> {
	@Override
	public int compare(Object s1, Object s2){
		long l1 = ((ObjectCounts) s1).getNumOfInstance();
		long l2 = ((ObjectCounts) s2).getNumOfInstance();
		return (int)(l2 - l1);
	}
}