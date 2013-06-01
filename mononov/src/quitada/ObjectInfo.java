package quitada;

/**
 * Object information used with Mononov system
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 */
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

	/**
	 * Get number of instances managed by Mononov system
	 * 
	 * @since 0.1
	 *
	 * @return number of instances           
	 */
	public long getNumOfInstance() {
		return this.numOfInstance;
	}

	/**
	 * Get number of bytes managed by Mononov system
	 * 
	 * @since 0.1
	 *
	 * @return number of bytes           
	 */
	public long getNumOfByte() {
		return this.numOfByte;
	}

	/**
	 * Get class name of this object managed by Mononov system
	 * 
	 * @since 0.1
	 *
	 * @return class name           
	 */
	public String getClassName() {
		return this.className;
	}
}
