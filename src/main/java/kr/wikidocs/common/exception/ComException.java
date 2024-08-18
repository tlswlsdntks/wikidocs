package kr.wikidocs.common.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ComException extends Exception {

	private final static long serialVersionUID = 1L;
	protected int code = 0;
	protected String message = null;
	
	/**
	 * 코드 설정
	 * @param code 결과코드
	 * @return
	 */
	public void setCode(int code) {
		this.code = code;
	}
	
	/**
	 * 메세지 설정
	 * @param message 결과메세지
	 * @return
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * 에러코드 반환
	 * @return
	 */
	public int getCode() {
		return this.code;
	}
	
	/**
	 * 에러메시지 반환
	 * @return
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * 코드,메시지 생성
	 * @param wrappedException
	 * @throws ComException
	 */
	public void setTEDException(Throwable wrappedException) throws ComException {
		
		//wrappedException.printStackTrace();
		
		if(wrappedException instanceof ComException){
			throw (ComException)wrappedException;
		}
		else{
			throw new ComException(wrappedException, -1, "[RuntimeException] Cause:\n" + wrappedException.getMessage());
		}
	}

	/**
	 * ComException 생성자
	 * @throws ComException
	 */
	public ComException(Throwable wrappedException) throws ComException {
		this.setTEDException(wrappedException);
	}
	
	/**
	 * ComException 생성자
	 * @param message 메세지내용
	 */
	public ComException(String message) {
		//this(null, "90", "[ServiceException] Cause:\n" + message);
		this(null, -1, message);
	}
	
	/**
	 * ComException 생성자
	 * @param code 결과코드
	 * @param message 결과메세지
	 */
	public ComException(int code, String message) {
		this(null, code, message);
	}

	/**
	 * ComException 생성자
	 * @param wrappedException 예외객체
	 * @param code 결과코드
	 * @param message 결과메세지
	 */
	public ComException(Throwable wrappedException, int code, String message) {
		super(wrappedException);
		this.code = code;
		this.message = message;
		this.printStackTrace();
	}
}
