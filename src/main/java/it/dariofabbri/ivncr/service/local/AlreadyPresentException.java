package it.dariofabbri.ivncr.service.local;

public class AlreadyPresentException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public AlreadyPresentException() {
		super();
	}

	public AlreadyPresentException(String message) {
		super(message);
	}

	public AlreadyPresentException(String message, Throwable t) {
		super(message, t);
	}
}
