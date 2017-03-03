import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by quannk on 03/03/2017.
 */
public class BaseballElimination {
	private final static byte TRIVIAL_ELIMINATED = 2;
	private final static byte NON_TRIVIAL_ELIMINATED = 3;
	private final static byte NOT_ELIMINATED = 1;
	private final static byte UNKNOWN = 0;

	private final static int SOURCE = 0;
	private final static int END = 1;
	private final static int FIRST_TEAM_VERTEX = 2;
	private final int FIRST_GAME_VERTEX;

	private final int data[][];
	private final Map<String, Integer> teamMap = new HashMap<>();
	private final String[] teamName;
	private final int N;
	private final byte[] eliminated;
	private final String maxTeam;

	/**
	 * create a baseball division from given filename in format specified below
	 *
	 * @param filename
	 */
	public BaseballElimination(String filename) {
		In in = new In(filename);
		N = in.readInt();
		data = new int[N][N + 3];
		eliminated = new byte[N];
		teamName = new String[N];
		FIRST_GAME_VERTEX = FIRST_TEAM_VERTEX + N;

		int maxWin = 0;
		String maxTeam = null;
		for (int i = 0; i < N; i++) {
			teamName[i] = in.readString();
			teamMap.put(teamName[i], i);
			data[i][N] = in.readInt(); // win
			data[i][N + 1] = in.readInt(); // lost
			data[i][N + 2] = in.readInt(); // remain
			for (int j = 0; j < N; j++) {
				data[i][j] = in.readInt();
			}
			if (maxWin < data[i][N]) {
				maxWin = data[i][N];
				maxTeam = teamName[i];
			}
		}
		this.maxTeam = maxTeam;
		// find trivial eliminated
		for (int i = 0; i < N; i++) {
			if (data[i][N] + data[i][N + 2] < maxWin) {
				eliminated[i] = TRIVIAL_ELIMINATED;
			}
		}
	}

	public static void main(String[] args) {
		BaseballElimination division = new BaseballElimination("part2-week3/baseball/teams5.txt");
		for (String team : division.teams()) {
			if (division.isEliminated(team)) {
				ArrayList<String> r = (ArrayList<String>) division.certificateOfElimination(team);
				if (r.size() > 1) {
					StdOut.print(team + " is eliminated by the subset R = { ");
					for (String t : r) {
						StdOut.print(t + " ");
					}
				} else {
					StdOut.println(team + " is trivial eliminated");
				}
				StdOut.println("}");
			} else {
				StdOut.println(team + " is not eliminated");
			}
		}
	}

	private FlowNetwork buildFlowNetwork(int team) {
		FlowNetwork G = new FlowNetwork(2 + N + N * (N - 1) / 2);
		// build edge from SOURCE to GAME and from GAME TO TEAM
		for (int i = 0; i < N - 1; i++) {
			for (int j = i + 1; j < N; j++) {
				// game between i and j
				int vertex = getGameVertex(i, j);
				G.addEdge(new FlowEdge(SOURCE, vertex, data[i][j]));
				G.addEdge(new FlowEdge(vertex, FIRST_TEAM_VERTEX + i, Double.MAX_VALUE));
				G.addEdge(new FlowEdge(vertex, FIRST_TEAM_VERTEX + j, Double.MAX_VALUE));
			}
		}

		// build edge from TEAM to END
		for (int t = 0; t < N; t++) {
			G.addEdge(new FlowEdge(t + FIRST_TEAM_VERTEX, END, data[team][N] + data[team][N + 2] - data[t][N]));
		}

		return G;
	}

	/**
	 * number of teams
	 */
	public int numberOfTeams() {
		return teamMap.size();
	}

	/**
	 * all teams
	 *
	 * @return
	 */
	public Iterable<String> teams() {
		return teamMap.keySet();
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
		Integer i = teamMap.get(team);
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
		int t = getTeam(team);
		if (eliminated[t] == UNKNOWN) {
			FlowNetwork G = buildFlowNetwork(t);
			new FordFulkerson(G, SOURCE, END);
			for (FlowEdge e : G.adj(SOURCE)) {
				if (e.flow() != e.capacity()) {
					eliminated[t] = NON_TRIVIAL_ELIMINATED;
					break;
				}
			}
			if (eliminated[t] == UNKNOWN) {
				eliminated[t] = NOT_ELIMINATED;
			}
		}
		return eliminated[t] >= TRIVIAL_ELIMINATED;
	}

	public Iterable<String> certificateOfElimination(String team) {
		int teamId = getTeam(team);
		if (eliminated[teamId] == TRIVIAL_ELIMINATED) {
			ArrayList<String> r = new ArrayList<>();
			r.add(maxTeam);
			return r;
		} else if (eliminated[teamId] == NOT_ELIMINATED) {
			return null;
		}
		FlowNetwork G = buildFlowNetwork(teamId);
		FordFulkerson ff = new FordFulkerson(G, 0, 1);
		for (FlowEdge e : G.adj(SOURCE)) {
			if (e.flow() != e.capacity()) {
				eliminated[teamId] = NON_TRIVIAL_ELIMINATED;
				break;
			}
		}
		if (eliminated[teamId] == UNKNOWN) {
			eliminated[teamId] = NOT_ELIMINATED;
			return null;
		}
		ArrayList<String> result = new ArrayList<>();
		for (int i = FIRST_TEAM_VERTEX; i < FIRST_TEAM_VERTEX + N; i++) {
			if (ff.inCut(i)) {
				result.add(teamName[i - FIRST_TEAM_VERTEX]);
			}
		}
		return result;
	}

	private int getGameVertex(int i, int j) {
		if (i > j) {
			throw new RuntimeException();
		}
		return (2 * N - 1 - i) * i / 2 + (j - i - 1) + FIRST_GAME_VERTEX;
	}
}
