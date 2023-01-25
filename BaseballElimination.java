import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class BaseballElimination {
    private int teamNum;
    private Team[] teams;
    private HashMap<String, Team> nameToTeam;

    private int flow;

    private class Team {
        public String name;
        public int id;
        public int wins;
        public int loses;
        public int remainings;
        public int[] remainingAgainst;

    }

    public BaseballElimination(
            String filename)                    // create a baseball division from given filename in format specified below
    {
        In in = new In(filename);
        teamNum = in.readInt();
        teams = new Team[teamNum];
        nameToTeam = new HashMap<String, Team>();
        for (int i = 0; i < teamNum; i++) {
            teams[i] = new Team();
            teams[i].name = in.readString();
            teams[i].id = i;
            teams[i].wins = in.readInt();
            teams[i].loses = in.readInt();
            teams[i].remainings = in.readInt();
            teams[i].remainingAgainst = new int[teamNum];
            for (int j = 0; j < teamNum; j++) {
                teams[i].remainingAgainst[j] = in.readInt();
            }
            nameToTeam.put(teams[i].name, teams[i]);
        }
    }


    public int numberOfTeams()                        // number of teams
    {
        return teamNum;
    }

    public Iterable<String> teams()                                // all teams
    {
        return nameToTeam.keySet();
    }

    private void validate(String arg) {
        if (arg == null || !nameToTeam.containsKey(arg))
            throw new IllegalArgumentException();
    }

    public int wins(String team)                      // number of wins for given team
    {
        validate(team);
        return nameToTeam.get(team).wins;
    }

    public int losses(String team)                    // number of losses for given team
    {
        validate(team);
        return nameToTeam.get(team).loses;
    }

    public int remaining(String team)                 // number of remaining games for given team
    {
        validate(team);
        return nameToTeam.get(team).remainings;
    }

    public int against(String team1,
                       String team2)    // number of remaining games between team1 and team2
    {
        validate(team1);
        validate(team2);
        return nameToTeam.get(team1).remainingAgainst[nameToTeam.get(team2).id];
    }

    private FordFulkerson getMaxFlow(String team) {
        // 待评估的球队 x 的编号
        int x = nameToTeam.get(team).id;
        // 计算顶点个数
        int numberOfGameVertices = teamNum * (teamNum - 1) / 2;
        int numberOfVertices = numberOfGameVertices + teamNum + 2; // 加上起点和终点
        // 构建网络流
        FlowNetwork flowNetwork = new FlowNetwork(numberOfVertices);
        int s = 0;
        int t = numberOfVertices - 1;
        int index = 1;
        flow = 0;
        for (int i = 0; i < teamNum; i++) {
            if (i == x) {
                continue;
            }
            for (int j = i + 1; j < teamNum; j++) {
                if (j == x) {
                    continue;
                }
                // 首先将起点 s 与所有可能发生的比赛（game vertices）连接
                // 起点是 s，game vertices 的编号从 1 开始，限制容量为 remainingAgainst[i][j]
                flowNetwork.addEdge(new FlowEdge(s, index, teams[i].remainingAgainst[j]));
                // 然后将 team vertices 与 game vertices 连接，容量为无穷大
                // 比赛涉及到的双方是 i 和 j，因此代表它们的 team vertices 就是 i(j) + numberOfGameVertices + 1
                flowNetwork.addEdge(new FlowEdge(index, i + numberOfGameVertices + 1,
                                                 Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(index, j + numberOfGameVertices + 1,
                                                 Double.POSITIVE_INFINITY));
                index++;
                // 累加来自起点的所有流量
                flow += teams[i].remainingAgainst[j];
            }
            // 最后，我们需要将 team vertices 与终点 t 连接，容量限制为 w[x] + r[x] - w[i]
            int wX = nameToTeam.get(team).wins;
            int rX = nameToTeam.get(team).remainings;
            int wI = teams[i].wins;
            // 容量必须是非负数，所以如果这里相减得到负数，则意味着可以提前结束判断，认为 x 已经被淘汰
            // 注意如果将 capacity 设置为 Math.max(0, wX + rX - wI) 会导致有些被 eliminate 的球队无法被正确淘汰
            if (wX + rX - wI < 0) {
                return null;
            }
            else {
                flowNetwork.addEdge(new FlowEdge(i + numberOfGameVertices + 1, t, wX + rX - wI));
            }
        }

        return new FordFulkerson(flowNetwork, s, t);
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        validate(team);
        FordFulkerson maxFlow = getMaxFlow(team);
        if (maxFlow == null) {
            return true;
        }
        else {
            // 球队 x 没有被淘汰，当且仅当最大流的值大于等于从 s 出发的所有流量
            return flow > maxFlow.value();
        }
    }

    // subset R of teams that eliminates given team; null if not eliminated
    public Iterable<String> certificateOfElimination(String team) {
        validate(team);
        if (!isEliminated(team)) {
            return null;
        }
        ArrayList<String> list = new ArrayList<>();
        int x = nameToTeam.get(team).id;
        int numberOfGameVertices = teamNum * (teamNum - 1) / 2;
        FordFulkerson maxFlow = getMaxFlow(team);
        for (int index = 0; index < teamNum; index++) {
            if (index == x) {
                continue;
            }
            if (maxFlow == null) {
                int wX = nameToTeam.get(team).wins;
                int rX = nameToTeam.get(team).remainings;
                int wI = teams[index].wins;
                if (wX + rX - wI < 0) {
                    list.add(teams[index].name);
                }
            }
            else if (maxFlow.inCut(index + numberOfGameVertices + 1)) {
                list.add(teams[index].name);
            }
        }
        return list;
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
            }
            else {
                StdOut.println(team + " is not eliminated");
            }
        }
    }
}
