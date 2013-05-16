package quitada;

public class ObjectInfo {
	private long numOfInstance;
	private long numOfByte;
	private String className;
	
	public ObjectInfo() {}
	
	public ObjectInfo(long numOfInstance, long numOfByte, String className) {
		this.numOfInstance = numOfInstance;
		this.numOfByte = numOfByte;
		this.className = className;
	}
	
	public long getNumOfInstance() {
		return this.numOfInstance;
	}
	
	public long getNumOfByte() {
		return this.numOfByte;
	}
	
	public String getClassName() {
		return this.className;
	}
}
