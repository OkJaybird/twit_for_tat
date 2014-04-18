import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;

import twitter4j.RateLimitStatus;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.OAuth2Token;
import twitter4j.conf.ConfigurationBuilder;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 16, 2014
 *****************************************************************/

public class IOUtil {

	private static final String PROP_FILE_NAME = "twit_for_tat.properties";
	private static Properties prop;
	private static Properties t4jProp;


	public static void printAPICallLimit() {
		try {
			Twitter twitter = TwitterFactory.getSingleton();
			RateLimitStatus rateLimitStatus = twitter.getRateLimitStatus().get("/followers/list");
			System.out.println("API Call Limit: "+rateLimitStatus);
		} catch (Exception e) {}
	}

	public static List<String> loadLineDelimUsers(String filename) throws IOException {
		File file = new File(filename);
		List<String> lines = FileUtils.readLines(file, "UTF-8");
		List<String> names = new ArrayList<String>();
		for (String line : lines) {
			String stripped = line.replaceAll("\\s+","");
			if (!stripped.equals("")) {
				names.add(stripped);
			}
		}
		return names;
	}

	public static Properties loadProperties() throws IOException {
		prop = new Properties();
		FileReader reader = new FileReader(PROP_FILE_NAME);
		prop.load(reader);

		if (!prop.containsKey("username")) {
			throw new IOException("Found the file, but it didn't contain the username key/val pair.");
		}

		return prop;
	}

	public static void initAppwideAuth() throws TwitterException, IOException {
		t4jProp = new Properties();
		t4jProp.load(new FileReader("twitter4j.properties"));
		if (!t4jProp.containsKey("enableApplicationOnlyAuth")) {
			System.out.println("This is the first time this app is running. It will configure its twitter4j.properties properties file for App-wide authentication, increasing the API call limit per duration...");
			ConfigurationBuilder builder = new ConfigurationBuilder();
			builder.setApplicationOnlyAuthEnabled(true);
			Twitter twitter = new TwitterFactory(builder.build()).getInstance();
			OAuth2Token token = twitter.getOAuth2Token();
			t4jProp.put("oauth2.accessToken", token.getAccessToken());
			t4jProp.put("enableApplicationOnlyAuth", "true");
			t4jProp.store(new FileWriter("twitter4j.properties"), "configured for app-wide auth");
			System.out.println("... twitter4j.properties reconfigured properly. Run the app again to start.");
			System.exit(0);
		}
	}

	public static String getUsername() {
		return prop.getProperty("username");
	}

	public static String getAccessToken() {
		return t4jProp.getProperty("oauth.accessToken");
	}

	public static String getAccessTokenSecret() {
		return t4jProp.getProperty("oauth.accessTokenSecret");
	}

	public static String getConsumerKey() {
		return t4jProp.getProperty("oauth.consumerKey");
	}

	public static String getConsumerSecret() {
		return t4jProp.getProperty("oauth.consumerSecret");
	}

}
