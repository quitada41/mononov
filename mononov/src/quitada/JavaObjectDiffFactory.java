package quitada;

public class JavaObjectDiffFactory {
	private int sortType = 0;
	private boolean isDesOrd = true;
	private int tabLength = 8;
	private int resultFormat = 0; // this will be removed in the near future. 
	private String before = null;
	private String after = null;
	
	public JavaObjectDiff create() {
		return new JavaObjectDiff();
	}

}
