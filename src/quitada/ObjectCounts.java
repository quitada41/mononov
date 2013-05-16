package quitada;

public class ObjectCounts {
	private long numOfInstance;
	private String className;
	
	public ObjectCounts() {}
	
	public ObjectCounts(long numOfInstance, String className) {
		this.numOfInstance = numOfInstance;
		this.className = className;
	}
	
	public long getNumOfInstance() {
		return this.numOfInstance;
	}
	
	public String getClassName() {
		return this.className;
	}
}
