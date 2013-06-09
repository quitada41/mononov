package quitada;

/**
 * Mononov class to use Mononov system through API
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 */
public class Mononov {
	private int sortType;
	private boolean isDesOrd;
	private String before;
	private String after;

	public Mononov (int sortType, boolean isDesOrd, String before, String after) {
		this.sortType = sortType;
		this.isDesOrd = isDesOrd;
		this.before = before;
		this.after = after;
	}

	/**
	 * Merge two java histogram files extracted with the following jmap command
	 * and sort result based on type of sorting in descending or ascending
	 * order.
	 * <table border=1 align=center>
	 * <tr>
	 * <th align=left>jmap -histo:live <i>[target java pid]</i></th>
	 * </tr>
	 * </table>
	 * 
	 * @since 0.4.5
	 *
	 * @return Array of ObjectInfo objects merged and sorted according to condition
	 *            
	 */
	public ObjectInfo[] runMerge() throws MononovException {
		return new JavaObjectDiff().runMerge(sortType, isDesOrd, before, after);
	}
}
