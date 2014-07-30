package server.seo.back.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.codehaus.jackson.map.ObjectMapper;

import seo.scanner.dataService.ProjetService;
import seo.scanner.domain.Projet;
import seo.scanner.domain.ProjetListUrl;
import seo.scanner.domain.UrlToCheck;
import seo.scanner.domain.User;
import server.seo.back.utils.UserSessionHelper;


@RestController
@RequestMapping("/projetService")
public class ProjetController {
	
	@Autowired
	private ProjetService projetService;

	@RequestMapping(value="/add",method = RequestMethod.POST)
	public String addProject(HttpServletRequest request) {
		Integer mapSize = 0;
		try {
			InputStream jsonStream  = request.getInputStream();
			ObjectMapper mapper = new ObjectMapper();
			Projet projet = mapper.readValue(jsonStream, Projet.class);
			projet.setUserUid(UserSessionHelper.getSessionUserUid(request));
			projet = projetService.addProjet(projet);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mapSize.toString();
	}
	
	@RequestMapping(value="/projets",method = RequestMethod.GET)
	public List<Projet> getProjetsList(HttpServletRequest request) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetsByUserUid(userUid);
	}
	
	@RequestMapping(value="/get",method = RequestMethod.POST)
	public Projet getProjet(HttpServletRequest request,@RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetbyUid(projetUid,userUid);
	}
	
	@RequestMapping(value="/urllist",method = RequestMethod.POST)
	public ProjetListUrl getProjetListUrl(HttpServletRequest request,@RequestParam("projetUid") Integer projetUid, @RequestParam("projetListUrlUid") Integer projetListUrlUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetListUrl(userUid,projetUid,projetListUrlUid);
	}
	
	@RequestMapping(value="/addUrllist",method = RequestMethod.POST)
	public ProjetListUrl addListUrl(@RequestBody ProjetListUrl projetListUrl ,HttpServletRequest request,@RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.addListUrl(projetListUrl,userUid,projetUid);
	}
	
	@RequestMapping(value="/addUpdateUrlToList",method = RequestMethod.POST)
	public UrlToCheck addUpdateUrlToList(@RequestBody UrlToCheck urlToCheck) {
		if(urlToCheck.getUid() != null) {
			return projetService.updateUrlInList(urlToCheck);
		} else {
			return projetService.addUrlToList(urlToCheck);
		}
			
	}
	
	@RequestMapping(value="/deleteUrl",method = RequestMethod.POST)
	public String deleteUrl(HttpServletRequest request,@RequestParam("uid") Integer uid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		projetService.deleteUrlInList(uid, userUid);
		return "OK";
	}
	
	
	@RequestMapping(value="/listes",method = RequestMethod.POST)
	public List<ProjetListUrl> getProjetListes(HttpServletRequest request,@RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetUrlListes(userUid, projetUid);
	}
	
	@RequestMapping(value="/users",method = RequestMethod.POST)
	public List<User> getProjetUsers(HttpServletRequest request,@RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetUsers(userUid, projetUid);
	}
	
	@RequestMapping(value="/ping",method = RequestMethod.GET)
	public void ping() {
		
	}

}
