package quitada;

/**
 * Factory class used with Mononov API
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 */
public class MononovFactory {
	private int sortType = 0;
	private boolean isDesOrd = true;
	private int tabLength = 8;
	//private int resultFormat = 0; // this will be removed in the near future. 
	private String before = null;
	private String after = null;

	/**
	 * Create Mononov object to run Mononov system through API
	 * 
	 * @since 0.4.5
	 *
	 * @return number of instances           
	 */
	public Mononov create() {
		Mononov mnv = new Mononov(sortType, isDesOrd, tabLength, before, after);
		return mnv;
	}

}
