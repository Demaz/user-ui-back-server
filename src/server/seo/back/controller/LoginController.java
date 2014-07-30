package server.seo.back.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;  


@RestController
@RequestMapping("/loginService")
public class LoginController {
	
	@Autowired
	private DataSource dataSource;

	private JdbcTemplate jdbcTemplate;
	
	public JdbcTemplate getJdbcTemplate() {
		
		if(jdbcTemplate == null) {
			jdbcTemplate = new JdbcTemplate(this.dataSource);
		}
		return jdbcTemplate;
	}	
	
	
	 @RequestMapping(value="/signe",method = RequestMethod.POST)
	 public String signe(HttpServletRequest request,@RequestParam("username") String username,@RequestParam("password") String password) {
		 
		 List<Map<String,Object>> rows = getJdbcTemplate().queryForList("Select uid, username,password from tuserlogin where username = ? and password = ?"
				 , new String[] {username,password} );
	  if(rows.size() > 0 && rows.get(0).get("uid") != null) {
		  Integer userUid = Integer.valueOf(rows.get(0).get("uid").toString());
		  setSignedSession(request,userUid);
		return  "true";
	  }
	  return "false";
	 }
	 
	 private void setSignedSession(HttpServletRequest request,Integer userUid) {
		request.getSession().setAttribute("userUid", userUid);
		request.getSession().setAttribute("signed", Boolean.TRUE);
	 }
	 
	 private void setSigneOutSession(HttpServletRequest request) {
		request.getSession().setAttribute("userUid", null);
		request.getSession().setAttribute("signed", Boolean.FALSE);
	 }
	 
	 @RequestMapping(value="/isSigned",method = RequestMethod.GET)
	 public String isSigned(HttpServletRequest request) {
		 Boolean signedSession  = (Boolean)request.getSession().getAttribute("signed");
		 if(signedSession != null &&  signedSession) {
			return signedSession.toString();
		 }
		return "false";
	}
	 
	 @RequestMapping(value="/signeOut",method = RequestMethod.GET)
	 public void signeOut(HttpServletRequest request) {
		 setSigneOutSession(request);
	}
  
}
