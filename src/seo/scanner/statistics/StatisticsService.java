package seo.scanner.statistics;

import java.util.List;

import seo.scanner.domain.Statistics;
import seo.scanner.domain.UrlCheckResult;
import seo.scanner.domain.UrlToCheck;

public class StatisticsService {

	public Statistics computeStatistics(List<UrlCheckResult> urlCheckResults) {

		Statistics statistics = new Statistics();
		if (urlCheckResults.size() > 100) {
			statistics.setUrlCheckResults(urlCheckResults.subList(0, 100));
		} else {
			statistics.setUrlCheckResults(urlCheckResults);
		}
		for (UrlToCheck urlToCheck : urlCheckResults) {
			String responseCode = urlToCheck.getRedirectionUrlCode1();
			if (statistics.getResponseStatusStats().containsKey(responseCode)) {
				statistics.getResponseStatusStats().put(responseCode,
						statistics.getResponseStatusStats().get(responseCode) + 1);
			} else {
				statistics.getResponseStatusStats().put(responseCode, 1);
			}

		}

		return statistics;
	}

}
