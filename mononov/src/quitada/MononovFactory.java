package quitada;

/**
 * Factory class used with Mononov API
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 */
public class MononovFactory extends MononovCommons {
	private int sortType = 0;
	private boolean isDesOrd = true;
	private String before = null;
	private String after = null;

	/**
	 * Create Mononov object to run Mononov system through API
	 * 
	 * @since 0.4.5
	 *
	 * @return Mononov object           
	 */
	public Mononov create() {
		Mononov mnv = new Mononov(sortType, isDesOrd, before, after);
		return mnv;
	}

	/**
	 * Set sort type
	 * 
	 * @since 0.4.5
	 * @param sortType
	 *            Type of sorting:
	 *            "instance" or "i" - by instance
	 *            "byte" or "b" - by byte
	 *            "class" or "c" - by class name
	 *
	 * @return MononovFactroy object           
	 */
	public MononovFactory setSortType(String sortType) throws MononovException {
		if (sortType.equals(BYT_SUBOPS) || sortType.equals(BYT_SU_SUBOPS)) {
			this.sortType = 1;
		} else if (sortType.equals(CLS_SUBOPS) || sortType.equals(CLS_SU_SUBOPS)) {
			this.sortType = 2;
		} else if (!sortType.equals(CNT_SUBOPS) && !sortType.equals(CNT_SU_SUBOPS)) {
			//invalid_arg_mes
			String message = getNonLocalizedExMeg("invalid_arg_mes") + ": " + sortType;
			String localizedMessage = getI18nMessages("invalid_arg_mes") + ": " + sortType;
			throw new MononovException(message, localizedMessage);
		}
		return this;
	}

	/**
	 * Set sort order
	 * 
	 * @since 0.4.5
	 * @param sortOrder
	 *            Type of sort order:
	 *            "des" or "d" - Sort in descending order
	 *            "asc" or "a" - Sort in ascending order
	 *
	 * @return MononovFactroy object           
	 */
	public MononovFactory setSortOrder(String sortOrder) throws MononovException {
		if (sortOrder.equals(ASC_SUBOPS) || sortOrder.equals(ASC_SU_SUBOPS)) {
			this.isDesOrd = false;
		} else if (!sortOrder.equals(DES_SUBOPS) && !sortOrder.equals(DES_SU_SUBOPS)) {
			//invalid_arg_mes
			String message = getNonLocalizedExMeg("invalid_arg_mes") + ": " + sortOrder;
			String localizedMessage = getI18nMessages("invalid_arg_mes") + ": " + sortOrder;
			throw new MononovException(message, localizedMessage);
		}
		return this;
	}
	
	/**
	 * Set path name to histogram file before memory leak
	 * 
	 * @since 0.4.5
	 * @param before
	 *
	 * @return MononovFactroy object           
	 */
	public MononovFactory setPathBefore(String before) {
		this.before = before;
		return this;
	}
	
	/**
	 * Set path name to histogram file after memory leak
	 * 
	 * @since 0.4.5
	 * @param after
	 *
	 * @return MononovFactroy object           
	 */
	public MononovFactory setPathAfter(String after) {
		this.after = after;
		return this;
	}
}
