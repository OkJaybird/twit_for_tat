import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/*****************************************************************
	Jay Waldron
	jaywaldron@gmail.com
	Apr 16, 2014
 *****************************************************************/

public class FollowingPrefs {

	private final String YES_FOLLOW_FILENAME = "yes_follow.txt";
	private final String NO_FOLLOW_FILENAME = "no_follow.txt";

	private List<String> yes_follow = new LinkedList<String>();
	private List<String> no_follow = new LinkedList<String>();

	public FollowingPrefs() {
		loadYes();
		loadNo();
	}

	public List<String> getYesFollows() {
		return yes_follow;
	}

	public List<String> getNoFollows() {
		return no_follow;
	}

	private void loadNo() {
		List<String> users;
		try {
			users = IOUtil.loadLineDelimUsers(NO_FOLLOW_FILENAME);
		} catch (IOException e) {
			System.out.println("Unable to load no_follow.txt. Defaulting to no blocked follows.");
			return;
		}
		for (String user : users) {
			no_follow.add(user);
		}
	}

	private void loadYes() {
		List<String> users;
		try {
			users = IOUtil.loadLineDelimUsers(YES_FOLLOW_FILENAME);
		} catch (IOException e) {
			System.out.println("Unable to load yes_follow.txt. Defaulting to no required follows.");
			return;
		}
		for (String user : users) {
			yes_follow.add(user);
			if (yes_follow.size() >= 5) {
				return;
			}
		}
	}

}
