package quitada;

/**
 * Exception class used with Mononov system
 * 
 * @author quitada <a href="http://d.hatena.ne.jp/quitada/"
 *         target="_top">http://d.hatena.ne.jp/quitada/</a>
 */
public class MononovException extends Exception {

	private static final long serialVersionUID = -7163351588273785360L;

	private String detailLocalizedMessage;
	
    public MononovException() {
        super();
    }
    
	public MononovException(String message) {
		super(message);
		detailLocalizedMessage = message;
	}
	
	public MononovException(String message, String localizedMessage) {
		super(message);
		detailLocalizedMessage = localizedMessage;
	}
	
    public MononovException(String message, Throwable cause) {
        super(message, cause);
		detailLocalizedMessage = message;
    }
    
    public MononovException(String message, String localizedMessage, Throwable cause) {
        super(message, cause);
		detailLocalizedMessage = localizedMessage;
    }

    public MononovException(Throwable cause) {
        super(cause);
    }

	/**
	 * Get localized message according to locale. Current available localized messages are based on the following language.
	 * <ul>
	 * <li>English</li>
	 * <li>Japanese</li>
	 * </ul>
	 * 
	 * @since 0.4.5
	 *
	 * @return localized message           
	 */
    public String getLocalizedMessage() {
        return detailLocalizedMessage;
    }
}
