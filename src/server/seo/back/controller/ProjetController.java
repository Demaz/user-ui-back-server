package server.seo.back.controller;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import org.codehaus.jackson.map.ObjectMapper;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.SAXException;

import seo.scanner.dataService.ProjetService;
import seo.scanner.domain.Alertes;
import seo.scanner.domain.Event;
import seo.scanner.domain.Parameters;
import seo.scanner.domain.Projet;
import seo.scanner.domain.ProjetListUrl;
import seo.scanner.domain.UrlCheckResult;
import seo.scanner.domain.UrlToCheck;
import seo.scanner.domain.User;
import seo.scanner.domain.Useragent;
import seo.scanner.sitemap.SitemapService;
import server.seo.back.utils.UserSessionHelper;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

@RestController
@RequestMapping("/projetService")
public class ProjetController {

	@Autowired
	private ProjetService projetService;

	@Autowired
	private SitemapService sitemapService;

	@RequestMapping(value = "/addUrllist", method = RequestMethod.POST)
	public ProjetListUrl addListUrl(@RequestBody ProjetListUrl projetListUrl, HttpServletRequest request) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.addListUrl(projetListUrl, userUid);
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addProject(HttpServletRequest request) {
		Integer mapSize = 0;
		try {
			InputStream jsonStream = request.getInputStream();
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

	@RequestMapping(value = "/useragents", method = RequestMethod.GET)
	public List<Useragent> addProjetUser() {
		return projetService.getUseragents();
	}

	@RequestMapping(value = "/addProjetUser", method = RequestMethod.POST)
	public Integer addProjetUser(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid,
			@RequestParam("userUid") Integer otherUserUid) {
		return projetService.addProjetUser(otherUserUid, projetUid);
	}

	@RequestMapping(value = "/addUpdateUrlToList", method = RequestMethod.POST)
	public UrlToCheck addUpdateUrlToList(@RequestBody UrlToCheck urlToCheck) {
		if (urlToCheck.getUid() != null) {
			return projetService.updateUrlInList(urlToCheck);
		} else {
			return projetService.addUrlToList(urlToCheck);
		}

	}

	@RequestMapping(value = "/deleteUrl", method = RequestMethod.POST)
	public String deleteUrl(HttpServletRequest request, @RequestParam("uid") Integer uid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		projetService.deleteUrlInList(uid, userUid);
		return "OK";
	}

	@RequestMapping(value = "/alertes/get", method = RequestMethod.POST)
	public Alertes getAlertes(@RequestParam("projetListUrlUid") Integer projetListUrlUid) {
		return projetService.getAlertes(projetListUrlUid);
	}

	@RequestMapping(value = "/comptesUsers", method = RequestMethod.POST)
	public List<User> getComptesUsers(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getComptesUsers(projetUid, userUid);
	}

	@RequestMapping(value = "/reports", method = RequestMethod.POST)
	public List<UrlCheckResult> getLastEvent(HttpServletRequest request,
			@RequestParam("projetListUrlUid") Integer projetListUrlUid,
			@RequestParam("eventStartUid") Integer eventStartUid, @RequestParam("eventEndUid") Integer eventEndUid) {
		return projetService.getCrawResults(eventStartUid, eventEndUid, projetListUrlUid);
	}

	@RequestMapping(value = "/parameters/get", method = RequestMethod.POST)
	public Parameters getParameters(@RequestParam("projetListUrlUid") Integer projetListUrlUid) {
		return projetService.getParameters(projetListUrlUid);
	}

	@RequestMapping(value = "/get", method = RequestMethod.POST)
	public Projet getProjet(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetbyUid(projetUid, userUid);
	}

	@RequestMapping(value = "/listes", method = RequestMethod.POST)
	public List<ProjetListUrl> getProjetListes(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetUrlListes(userUid, projetUid);
	}

	@RequestMapping(value = "/urllist", method = RequestMethod.POST)
	public ProjetListUrl getProjetListUrl(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid,
			@RequestParam("projetListUrlUid") Integer projetListUrlUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetListUrl(userUid, projetUid, projetListUrlUid, true, false);
	}

	@RequestMapping(value = "/projets", method = RequestMethod.GET)
	public List<Projet> getProjetsList(HttpServletRequest request) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetsByUserUid(userUid);
	}

	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public List<User> getProjetUsers(HttpServletRequest request, @RequestParam("projetUid") Integer projetUid) {
		Integer userUid = UserSessionHelper.getSessionUserUid(request);
		return projetService.getProjetUsers(userUid, projetUid);
	}

	@RequestMapping(value = "/events", method = RequestMethod.POST)
	public List<Event> getReportResults(HttpServletRequest request,
			@RequestParam("projetListUrlUid") Integer projetListUrlUid) {
		return projetService.getLastEventDoneOfUrlList(projetListUrlUid);
	}

	@RequestMapping(value = "/ping", method = RequestMethod.GET)
	public void ping() {

	}

	@RequestMapping(value = "/alertes/save", method = RequestMethod.POST)
	public Alertes saveAlertes(@RequestBody Alertes alertes) {
		return projetService.saveAlertes(alertes);
	}

	@RequestMapping(value = "/listes/submit", method = RequestMethod.POST)
	public Event saveAlertes(@RequestBody Event event) {
		return projetService.submitListe(event);
	}

	@RequestMapping(value = "/parameters/save", method = RequestMethod.POST)
	public Parameters saveParameters(@RequestBody Parameters parameters) {
		return projetService.saveParameters(parameters);
	}

	@RequestMapping(value = "/listes/sitemap/save", method = RequestMethod.POST)
	public Boolean saveSitemap(@RequestParam("projetListUrlUid") Integer projetListUrlUid,
			@RequestParam("sitemapUrl") String sitemapUrl) throws MalformedURLException, JAXBException,
			ParserConfigurationException, SAXException, IOException, DocumentException {
		URL sitemap = new URL(sitemapUrl);
		List<UrlToCheck> urlToChecks = sitemapService.getUrlFromSitemap(sitemap);
		projetService.cleanProjetListUrl(projetListUrlUid);
		projetService.addUrlsToList(urlToChecks, projetListUrlUid);
		return true;
	}

	@RequestMapping(value = "/listes/upload/csv", method = RequestMethod.POST)
	public List<UrlToCheck> uploadCsv(@RequestParam("projetListUrlUid") Integer projetListUrlUid,
			@RequestParam("file") MultipartFile file) throws IllegalStateException, IOException {
		if (!file.isEmpty()) {
			File convFile = new File(file.getOriginalFilename());
			file.transferTo(convFile);

			FileReader fileReader = new FileReader(convFile);
			char delimiter = ';';
			char quote = '"';
			char comment = '#';

			CSVStrategy strategy = new CSVStrategy(delimiter, quote, comment, true, true);
			ValueProcessorProvider vpp = new ValueProcessorProvider();
			CSVReader<UrlToCheck> csvPersonReader = new CSVReaderBuilder<UrlToCheck>(fileReader).strategy(strategy)
					.entryParser(new AnnotationEntryParser<UrlToCheck>(UrlToCheck.class, vpp)).build();

			List<UrlToCheck> urlToChecks = csvPersonReader.readAll();
			projetService.cleanProjetListUrl(projetListUrlUid);
			projetService.addUrlsToList(urlToChecks, projetListUrlUid);
			return urlToChecks;
		} else {
			return new ArrayList<UrlToCheck>();
		}

	}

}
