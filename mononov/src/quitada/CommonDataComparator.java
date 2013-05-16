package quitada;

import java.util.Comparator;

public class CommonDataComparator implements Comparator<Object> {
	// sortType =0: by instance/1: by byte/2: by class name
	private int sortType;
	// isDesOrd =true: Descending order/false: Ascending order
	private boolean isDesOrd;

	public CommonDataComparator(){
		this.sortType = 0;
		this.isDesOrd = true;
	}

	public CommonDataComparator(int sortType, boolean isDesOrd){
		this.sortType = sortType;
		this.isDesOrd = isDesOrd;
	}

	@Override
	public int compare(Object o1, Object o2){

		Object to1, to2;
		if (this.isDesOrd) {
			to1 = o1;
			to2 = o2;
		} else {
			to1 = o2;
			to2 = o1;
		}

		int retVal = 0;
		long l1 = 0l;
		long l2 = 0l;
		switch (this.sortType) {
		case 0:
			l1 = ((ObjectInfo) to1).getNumOfInstance();
			l2 = ((ObjectInfo) to2).getNumOfInstance();
			retVal = (int)(l2 - l1);
			break;
		case 1:
			l1 = ((ObjectInfo) to1).getNumOfByte();
			l2 = ((ObjectInfo) to2).getNumOfByte();
			retVal = (int)(l2 - l1);
			break;
		case 2:
			String s1 = ((ObjectInfo) to1).getClassName();
			String s2 = ((ObjectInfo) to2).getClassName();
			retVal = s2.compareTo(s1);
			break;			
		}
		
		return retVal;
	}
}