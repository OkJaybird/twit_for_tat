import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import twitter4j.TwitterException;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 16, 2014
 *****************************************************************/

public class Monitor {

	public static void main(String[] args) {
		new Monitor();
	}

	FollowSlots followSlots;

	public Monitor() {
		try {
			IOUtil.initAppwideAuth();
		} catch (TwitterException | IOException e1) {
			e1.printStackTrace();
			System.out.println("Error setting App-wide access token for a greater API call limit. Exiting.");
			return;
		}
		try {
			IOUtil.loadProperties();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to read valid properties. Exiting.");
			return;
		}

		followSlots = new FollowSlots();
		run();
	}

	private void run() {
		while (true) {
			List<String> followers = TwitterInteraction.getOrderedFollowers(IOUtil.getUsername());
			HashSet<String> following = TwitterInteraction.getFollowing(IOUtil.getUsername());

			if (followers != null && following != null) {
				followSlots.recomputeQueue(followers);
				HashSet<String> toFollow = followSlots.getFollowingSet();
				Following.changeFollowing(toFollow, following);
			}

			IOUtil.printAPICallLimit();
			try {Thread.sleep(31000);} catch (InterruptedException e) {}
		}
	}

}
