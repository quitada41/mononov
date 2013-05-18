package quitada;

public class MononovException extends Exception {
	/**
	 * 
	 */
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

    public String getLocalizedMessage() {
        return detailLocalizedMessage;
    }
}
