package org.tatemichi.api.commandparser.exception;

/**
 * 解析の過程で生じたエラー情報を保持するExceptionクラス
 * @author sf0273
 *
 */
public class InvalidCommandParameterException extends Exception {

	private ErrorCode code;
	private Object[] options;

	public InvalidCommandParameterException() {

	}

	public InvalidCommandParameterException(ErrorCode code) {
		this.setCode(code);
	}

	public InvalidCommandParameterException(ErrorCode code, Object... options) {
		this.setCode(code);
		this.options = options;
	}

	public InvalidCommandParameterException(ErrorCode code, Throwable e) {
		super(e);
		this.setCode(code);
	}

	public ErrorCode getCode() {
		return code;
	}

	public void setCode(ErrorCode code) {
		this.code = code;
	}

	public String createErrorMessage() {

		String message = "no message.";

		if (code == null) {
			return message;
		}

		message = code.getMessage();

		for (Object obj : options) {
			if (obj instanceof Throwable) {
				message += "[" + ((Throwable)obj).getMessage() + "]";
				continue;
			}
			message += "[" + obj.toString() + "]";
		}

		return message;
	}
}
