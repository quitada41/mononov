package quitada;

import java.util.Comparator;

public class InstanceAscComparator implements Comparator<Object> {
	@Override
	public int compare(Object s1, Object s2){
		long l1 = ((ObjectInfo) s1).getNumOfInstance();
		long l2 = ((ObjectInfo) s2).getNumOfInstance();
		return (int)(l1 - l2);
	}
}