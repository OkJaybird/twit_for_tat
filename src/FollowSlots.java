import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.io.FileUtils;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 16, 2014
 *****************************************************************/

public class FollowSlots {

	private final int SLOTS = 5;
	private final String QUEUE_FILE_PATH = "queue.txt";
	private HashSet<String> following = new HashSet<String>();
	private List<String> followQueue;

	public FollowSlots() {
		followQueue = loadQueueFile();
	}

	public void recomputeQueue(List<String> currentFollowers) {
		updateAndComputeNewQueueOrder(currentFollowers);
		saveQueueFile();
		computeSlots(followQueue);
	}

	private void updateAndComputeNewQueueOrder(List<String> currentFollowers) {
		for (String currentFollower : currentFollowers) {
			if (!followQueue.contains(currentFollower)) {
				followQueue.add(currentFollower);
			}
		}
		List<String> newFollowQueue = new ArrayList<String>();
		for (String queuedFollower : followQueue) {
			if (currentFollowers.contains(queuedFollower)) {
				newFollowQueue.add(queuedFollower);
			}
		}
		followQueue = newFollowQueue;
	}

	private void computeSlots(List<String> followers) {
		following = new HashSet<String>();
		FollowingPrefs followingPrefs = new FollowingPrefs();

		// the required follows
		for (String user : followingPrefs.getYesFollows()) {
			if (following.size() >= SLOTS) {
				break;
			}
			following.add(user);
		}

		// the other followers
		for (String follower : followers) {
			if (following.size() >= SLOTS) {
				break;
			}
			if (!followingPrefs.getNoFollows().contains(follower)) {
				following.add(follower);
			}
		}
	}

	private void saveQueueFile() {
		StringBuilder sb = new StringBuilder();
		for (String follower : followQueue) {
			sb.append(follower);
			sb.append("\n");
		}
		try {
			FileUtils.writeStringToFile(new File(QUEUE_FILE_PATH), sb.toString()); // clobers existing file
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("Unable to save queue file. Ensure valid permissions.");
		}
	}

	private List<String> loadQueueFile() {
		File queueFile = new File(QUEUE_FILE_PATH);
		if (queueFile.exists()) {
			try {
				return IOUtil.loadLineDelimUsers(QUEUE_FILE_PATH);
			} catch (IOException e) {}
		}
		return new ArrayList<String>();
	}

	public HashSet<String> getFollowingSet() {
		return following;
	}

}
