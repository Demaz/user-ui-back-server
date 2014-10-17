package seo.scanner.test;

import java.net.MalformedURLException;
import java.net.URL;

import org.dom4j.DocumentException;
import org.junit.Test;

import seo.scanner.sitemap.SitemapService;

public class sitemapServiceTest {

	SitemapService sitemapService = new SitemapService();
	
	@Test
	public void testParsingSitemapXml() throws MalformedURLException, DocumentException {
		
		URL sitemapUrl = new URL("http://terrassement-hainaut-djs.com/sitemap.xml");
		sitemapService.getUrlFromSitemap(sitemapUrl);
	}
	
}
