import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 17, 2014
 *****************************************************************/

public class Following {

	public static void changeFollowing(HashSet<String> toFollow, HashSet<String> following) {
		List<String> toRemove = new LinkedList<String>();
		List<String> toAdd = new LinkedList<String>();

		Iterator<String> iter = toFollow.iterator();
		while (iter.hasNext()) {
			String user = iter.next();
			if (!following.contains(user)) {
				toAdd.add(user);
			}
		}

		iter = following.iterator();
		while (iter.hasNext()) {
			String user = iter.next();
			if (!toFollow.contains(user)) {
				toRemove.add(user);
			}
		}

		followAndNotify(toAdd);
		unfollow(toRemove);
	}

	private static void followAndNotify(List<String> users) {
		for (String user : users) {
			TwitterInteraction.startFollowing(user);
			TwitterInteraction.notifyNewFollower(user);
		}
	}

	private static void unfollow(List<String> users) {
		for (String user : users) {
			TwitterInteraction.stopFollowing(user);
		}
	}


}
