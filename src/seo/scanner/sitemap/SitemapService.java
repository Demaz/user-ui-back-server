package seo.scanner.sitemap;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.XPath;
import org.dom4j.io.SAXReader;
import org.springframework.cglib.core.CollectionUtils;
import org.springframework.cglib.core.Transformer;

import seo.scanner.domain.UrlToCheck;
import seo.scanner.dto.TUrl;
import seo.scanner.dto.Urlset;

public class SitemapService {
	
private String sitemapPackage =  "seo.scanner.dto";
	
	
	Transformer tUrllistTransformer = new Transformer() {
		
		public Object transform(Object obj) {
			UrlToCheck urlToCheck =  new UrlToCheck(((Node)obj).selectSingleNode("loc").getText());
			urlToCheck.setRedirectionUrl1(urlToCheck.getUrl());
			urlToCheck.setRedirectionUrlCode1("200");
			return urlToCheck;
		}
	};
	
	
	
	public List<UrlToCheck> getUrlFromSitemap(URL sitemapUrl) throws DocumentException   {
				
		List<UrlToCheck> urlToCheckList = new ArrayList<UrlToCheck>();
		SAXReader reader = new SAXReader();
        Document document = reader.read(sitemapUrl);
        Element root = document.getRootElement();
     // iterate through child elements of root
        for ( Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            String name = element.getName();
            
            Iterator locIt = element.elementIterator("loc");
            Element  urle = (Element) locIt.next();
            String url = urle.getText();
            
            UrlToCheck urlToCheck =  new UrlToCheck(url);
			urlToCheck.setRedirectionUrl1(url);
			urlToCheck.setRedirectionUrlCode1("200");
			urlToCheckList.add(urlToCheck);
            
        }
        
		return urlToCheckList;
		
	}
	

}
