package quitada;

import java.util.Comparator;

public class ClassAscComparator implements Comparator<Object> {
	@Override
	public int compare(Object o1, Object o2){
		String s1 = ((ObjectInfo) o1).getClassName();
		String s2 = ((ObjectInfo) o2).getClassName();
		return s1.compareTo(s2);
	}
}