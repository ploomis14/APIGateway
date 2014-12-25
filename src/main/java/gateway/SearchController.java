package gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
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
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Establishes a gateway for the Yelp Search API
 *
 * @author Peter Loomis
 */

@RestController
public class SearchController {

    private static final String API_HOST = "api.yelp.com";
    private static final String DEFAULT_TERM = "food";
    private static final String DEFAULT_LOCATION = "Santa Barbara, CA";
    private static final String PATH = "/v2/search";
    /*
     * Update OAuth credentials below from the Yelp Developers API site:
     * http://www.yelp.com/developers/getting_started/api_access
     */
    private static final String CONSUMER_KEY = "GAoJX71ALZBhdOU2hDFcvA";
    private static final String CONSUMER_SECRET = "guheGJnpfyewETJvFuvMd-dhwRs";
    private static final String TOKEN = "au5niZlavbIaAazcHMC5A9kz8mGiCzj7";
    private static final String TOKEN_SECRET = "r1-5h1iPdhBhuD12shLJZf1LAY8";

    private static final RateLimiter rateLimiter = new RateLimiter();

    /**
     * Constructs a URI based on the search terms, performs authentication, and sends an HTTP request to the Yelp API
     * Search results are cached to improve performance.
     *
     * @param location search location
     * @param term search term
     * @return json string response from the Yelp API
     * @throws IOException
     */
    @Cacheable("searchResultsCache")
    @RequestMapping("/search")
    public String search(@RequestParam(value="location", defaultValue=DEFAULT_LOCATION) String location,
                         @RequestParam(value="term", defaultValue=DEFAULT_TERM) String term) throws IOException {
        if (rateLimiter.limitExceeded()) {
            return "rate limit exceeded";
        } else {
            URI uri = null;
            try {
                uri = new URIBuilder()
                        .setScheme("http")
                        .setHost(API_HOST)
                        .setPath(PATH)
                        .setParameter("term", term)
                        .setParameter("location", location)
                        .build();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }

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
}