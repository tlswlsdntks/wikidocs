package kr.wikidocs.common.session;

import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserSession{

	private HttpSession session = null;

	public UserSession(){
		this(false);
	}

	/**
	 * 세션을 가져온다.
	 * @param isNew true인경우 세션을 새로 생성한다.
	 */
	public UserSession(boolean isNew){
		try{
			if(isNew){
				this.session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession(true);
			}else{
				this.session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
			}
		}catch(Exception e){
			log.error(e.getMessage());
		}
	}

	/**
	 * 로그인여부
	 */
	public boolean isLogin(){
		if(null == this.session.getAttribute("isLogin")){
			return false;
		}else if("false".equals(String.valueOf(this.session.getAttribute("isLogin")))){
			return false;
		}else{
			return true;
		}
	}

	/**
	 * 로그인시간
	 * @return
	 */
	public String getLoginTime(){
		return ((String)this.session.getAttribute("loginTime"));
	}

	/**
	 * 로그인ID
	 * @return
	 */
	public String getLoginId(){
		return (String)this.session.getAttribute("loginId");
	}

	/**
	 * 사용자ID
	 * @return
	 */
	public String getUserId(){
		return (String)this.session.getAttribute("userId");
	}

	/**
	 * 사용자이름
	 * @return
	 */
	public String getUserNm(){
		return (String)this.session.getAttribute("userNm");
	}

	/**
	 * 이메일
	 * @return
	 */
	public String getUserEmail(){
		return (String)this.session.getAttribute("userEmail");
	}

	/**
	 * 사용자 휴대전화번호
	 * @return
	 */
	public String getUserTelno(){
		return (String)this.session.getAttribute("userTelno");
	}

	/**
	 * 사용자상태
	 * @return
	 */
	public String getUserStatus(){
		return (String)this.session.getAttribute("userStatus");
	}

	/**
	 * 세션을 사용해야할경우 여기 담아서 사용
	 */
	public void setEtc(String etc){
		this.session.setAttribute("etc", etc);
	}

	/**
	 * 세션을 사용해야할경우 여기 담아서 사용
	 */
	public String getEtc(){
		return (String)this.session.getAttribute("etc");
	}

	/**
	 * 이전 URL
	 * @return
	 */
	public void setPreUrl(String preUrl){
		this.session.setAttribute("preUrl", preUrl);
	}

	/**
	 * 이전 URL
	 * @return
	 */
	public String getPreUrl(){
		return (String)this.session.getAttribute("preUrl");
	}

	/**
	 * 세션 아이디를 가져옵니다
	 * @return
	 */
	public String getSessionId(){
		return session.getId();
	}

	/**
	 * 세션 소멸
	 */
	public void removeSession(){
		this.session.invalidate();
	}
}