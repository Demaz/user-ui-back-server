package server.seo.back.utils;

import javax.servlet.http.HttpServletRequest;
import server.seo.back.constants.Constants;

public class UserSessionHelper {
	
	public static Integer getSessionUserUid(HttpServletRequest request) {
		Integer userUid = (Integer) request.getSession().getAttribute(Constants.USER_UID_SESSION_KEY);
		if(userUid != null){
			return userUid;
		}
		return null;
	}

}
