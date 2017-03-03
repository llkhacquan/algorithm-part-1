import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by quannk on 03/03/2017.
 */
public class BaseballElimination {
	private final int data[][];
	private final Map<String, Integer> teamName = new HashMap<>();
	private final int N;
	private final boolean[] eliminated;

	/**
	 * create a baseball division from given filename in format specified below
	 *
	 * @param filename
	 */
	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		data = new int[N][N + 3];
		eliminated = new boolean[N];

		int maxWin = 0;
		for (int i = 0; i < N; i++) {
			teamName.put(in.readString(), i);
			data[i][N] = in.readInt(); // win
			data[i][N + 1] = in.readInt(); // win
			data[i][N + 2] = in.readInt(); // win
			for (int j = 0; j < N; j++) {
				data[i][j] = in.readInt();
			}
			if (maxWin < data[i][N]) {
				maxWin = data[i][N];
			}
		}

		// find trivial eliminated
		for (int i = 0; i < N; i++) {
			if (data[i][N] + data[i][2] < maxWin) {
				eliminated[i] = true;
			}
		}
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team)) {
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}

	/**
	 * number of teams
	 */
	public int numberOfTeams() {
		return teamName.size();
	}

	/**
	 * all teams
	 *
	 * @return
	 */
	public Iterable<String> teams() {
		return teamName.keySet();
	}

	/**
	 * number of wins for given team
	 *
	 * @param team
	 * @return
	 */
	public int wins(String team) {
		return data[getTeam(team)][N];
	}

	private int getTeam(String team) {
		Integer i = teamName.get(team);
		if (i != null) {
			return i;
		} else {
			throw new IllegalArgumentException();
		}
	}

	/**
	 * number of losses for given team
	 */
	public int losses(String team) {
		return data[getTeam(team)][N + 1];
	}

	/**
	 * number of remaining games for given team
	 *
	 * @param team
	 * @return
	 */
	public int remaining(String team) {
		return data[getTeam(team)][N + 2];
	}

	/**
	 * number of remaining games between team1 and team2
	 *
	 * @param team1
	 * @param team2
	 * @return
	 */
	public int against(String team1, String team2) {
		return data[getTeam(team1)][getTeam(team2)];
	}

	/**
	 * is given team eliminated?
	 *
	 * @param team
	 * @return
	 */
	public boolean isEliminated(String team) {
		return eliminated[getTeam(team)];
	}

	public Iterable<String> certificateOfElimination(String team) {
		return null;
	}
}
