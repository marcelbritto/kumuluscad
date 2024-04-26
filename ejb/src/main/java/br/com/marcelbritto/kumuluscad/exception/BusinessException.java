package br.com.marcelbritto.kumuluscad.exception;

import javax.ejb.ApplicationException;

/**
 * @author marcelbritto
 *
 */
@ApplicationException(rollback=true, inherited=false)
public class BusinessException extends Exception {
 	
	static final long serialVersionUID = 45332465;
	Throwable previousException;
	
	
	public BusinessException()	{
	}

	public BusinessException(String message) {
		super(message);
	}

	public BusinessException(String message, Exception previousException) {
		super(message);
		this.previousException = previousException;
	}

	public BusinessException(Exception previousException) {
		this.previousException = previousException;
	}

	public Throwable getInternalException()	{
		return previousException;
	}

}
