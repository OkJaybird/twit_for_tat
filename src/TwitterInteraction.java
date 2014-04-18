import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import twitter4j.PagableResponseList;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 16, 2014
 *****************************************************************/

public class TwitterInteraction {

	// per user API: don't call more than 1x per minute. This app uses app-specific API, so can call 2xs per minute.
	// can actually call up to (3xs) per minute if you hack around some things.
	public static List<String> getOrderedFollowers(String username) {
		Twitter twitter = TwitterFactory.getSingleton();
		try {
			// this list is twitter-ordered by earliest follower. why does this API reverse this list?
			// note, 'earliest follower' ordering to be taken with grain of salt. thus keep track of recency ourselves.
			PagableResponseList<User> rev = twitter.friendsFollowers().getFollowersList(username, -1, 200);
			List<String> ordered = new ArrayList<String>();
			for (int i=rev.size()-1; i>=0; i--) {
				ordered.add(rev.get(i).getScreenName());
			}
			return ordered;
		} catch (TwitterException e) {
			System.out.println("Unable to get list of followers. Will try again later.");
		}
		return null;
	}

	// same restrictions as above
	public static HashSet<String> getFollowing(String username) {
		Twitter twitter = TwitterFactory.getSingleton();
		try {
			PagableResponseList<User> following = twitter.friendsFollowers().getFriendsList(username, -1);
			HashSet<String> set = new HashSet<String>();
			for (User user : following) {
				set.add(user.getScreenName());
			}
			return set;
		} catch (TwitterException e) {
			System.out.println("Unable to get list of friends. Will try again later.");
		}
		return null;
	}

	public static void startFollowing(String username) {
		Twitter twitter = buildUserSpecificConnection();
		try {
			twitter.createFriendship(username);
			System.out.println("Now following "+username);
		} catch (TwitterException e) {
			System.out.println("Unable to follow "+username);
		}
	}

	public static void notifyNewFollower(String username) {
		Twitter twitter = buildUserSpecificConnection();
		try {
			DateFormat dateFormat = new SimpleDateFormat("MM/dd HH:mm:ss");
			Date date = new Date();
			twitter.updateStatus(IOUtil.getUsername()+"'s TwitForTat bot is now following @"+username+" at "+dateFormat.format(date));
		} catch (TwitterException e) {
			System.out.println("Unable to tweet update at "+username);
		}
	}

	public static void stopFollowing(String username) {
		Twitter twitter = buildUserSpecificConnection();
		try {
			twitter.destroyFriendship(username);
			System.out.println("No longer following "+username);
		} catch (TwitterException e) {
			e.printStackTrace();
			System.out.println("Unable to stop following "+username);
		}
	}

	private static Twitter buildUserSpecificConnection() {
		ConfigurationBuilder confbuilder  = new ConfigurationBuilder();
		confbuilder.setOAuthAccessToken(IOUtil.getAccessToken());
		confbuilder.setOAuthAccessTokenSecret(IOUtil.getAccessTokenSecret());
		confbuilder.setOAuthConsumerKey(IOUtil.getConsumerKey());
		confbuilder.setOAuthConsumerSecret(IOUtil.getConsumerSecret());
		confbuilder.setApplicationOnlyAuthEnabled(false);
		return new TwitterFactory(confbuilder.build()).getInstance();
	}


}
