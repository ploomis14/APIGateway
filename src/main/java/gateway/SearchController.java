package gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import oauth.signpost.OAuthConsumer;
import oauth.signpost.commonshttp.CommonsHttpOAuthConsumer;
import oauth.signpost.exception.OAuthCommunicationException;
import oauth.signpost.exception.OAuthExpectationFailedException;
import oauth.signpost.exception.OAuthMessageSignerException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Establishes a gateway for the Yelp Search API
 * @author Peter Loomis
 */

@RestController
public class SearchController {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "food";
    private static final String DEFAULT_LOCATION = "Santa Barbara, CA";
    private static final String SEARCH_PATH = "/v2/search";
    /*
     * Update OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "GAoJX71ALZBhdOU2hDFcvA";
    private static final String CONSUMER_SECRET = "guheGJnpfyewETJvFuvMd-dhwRs";
    private static final String TOKEN = "au5niZlavbIaAazcHMC5A9kz8mGiCzj7";
    private static final String TOKEN_SECRET = "r1-5h1iPdhBhuD12shLJZf1LAY8";

    private static final RateLimiter rateLimiter = new RateLimiter();
    private static final CallLog termLog = new CallLog();
    private static final CallLog locationLog = new CallLog();

    /**
     * Searches for businesses by location and keyword
     * @param location // the area in which to search
     * @param term // keyword
     * @return json response
     */
    @RequestMapping("/search")
    public String search(@RequestParam(value="location", defaultValue=DEFAULT_LOCATION) String location,
                         @RequestParam(value="term", defaultValue=DEFAULT_TERM) String term) {
        String response = "";
        if (rateLimiter.limitExceeded()) {
            response = "rate limit exceeded";
        } else {
            try {
                response = getResponseContent(location, term);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
            termLog.visit(term);
            locationLog.visit(location);
        }
        return response;
    }

    /**
     * Retrieves analytics from the call log system
     * @return json containing the most common search term, the most common location, and the number of times a search
     * was performed with each parameter
     */
    @RequestMapping("/analytics")
    public ArrayList<TermCount> analytics() {
        ArrayList<TermCount> termCountList = new ArrayList<TermCount>();
        TermCount termCount;
        if ((termCount = termLog.getMostVisited()) != null) {
            termCountList.add(termCount);
        }
        if ((termCount = locationLog.getMostVisited()) != null) {
            termCountList.add(termCount);
        }
        return termCountList;
    }

    /**
     * Constructs a URI based on search terms. Creates and authenticates a request for an API resource.
     * @param location search location
     * @param term search term
     * @return json string response from the Yelp API
     * @throws IOException
     * @throws java.net.URISyntaxException
     */
    private String getResponseContent(String location, String term) throws IOException, URISyntaxException {
        URI uri = new URIBuilder()
                .setScheme("http")
                .setHost(API_HOST)
                .setPath(SEARCH_PATH)
                .setParameter("term", term)
                .setParameter("location", location)
                .build();

        // perform authentication using Yelp credentials
        OAuthConsumer consumer = new CommonsHttpOAuthConsumer(CONSUMER_KEY, CONSUMER_SECRET);
        consumer.setTokenWithSecret(TOKEN, TOKEN_SECRET);
        HttpGet request = new HttpGet(uri);
        try {
            consumer.sign(request);
        } catch (OAuthMessageSignerException e) {
            e.printStackTrace();
        } catch (OAuthExpectationFailedException e) {
            e.printStackTrace();
        } catch (OAuthCommunicationException e) {
            e.printStackTrace();
        }

        // get the response json
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(request);
        BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent(), "UTF-8"));
        String json = reader.readLine();
        return json;
    }
}