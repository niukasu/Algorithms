import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.StdOut;
import java.lang.*;
import java.util.*;

public class BaseballElimination {
    private int numberOfTeams;
    private HashMap<String, Integer> nameToIndex;
    private String[] indexToName;
    private int[] wins;
    private int[] losses;
    private int[] remaining;
    private int[][] against;
    public BaseballElimination(String filename) {
        In readfile = new In(filename);
        numberOfTeams = readfile.readInt();
        nameToIndex = new HashMap<String, Integer>();
        indexToName = new String[numberOfTeams];
        wins = new int[numberOfTeams];
        losses = new int[numberOfTeams];
        remaining = new int[numberOfTeams];
        against = new int[numberOfTeams][numberOfTeams];
        for (int i = 0; i < numberOfTeams; i++) {
            String temp = readfile.readString();
            nameToIndex.put(temp, i);
            indexToName[i] = temp;
            wins[i] = readfile.readInt();
            losses[i] = readfile.readInt();
            remaining[i] = readfile.readInt();
            for (int j = 0; j < numberOfTeams; j++) {
                against[i][j] = readfile.readInt();
            }
        }
    }                   
    // create a baseball division from given filename in format specified below
    public              int numberOfTeams() {
        return numberOfTeams;
    }                       
    // number of teams
    public Iterable<String> teams() {
        return nameToIndex.keySet();
    }                               
    // all teams
    public              int wins(String team) {
        if (!nameToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return wins[nameToIndex.get(team)];
    }                     
    // number of wins for given team
    public              int losses(String team) {
        if (!nameToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return losses[nameToIndex.get(team)];
    }                   
    // number of losses for given team
    public              int remaining(String team) {
        if (!nameToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException();
        return remaining[nameToIndex.get(team)];
    }                
    // number of remaining games for given team
    public              int against(String team1, String team2) {
        if (!nameToIndex.containsKey(team1)) throw new java.lang.IllegalArgumentException();
        if (!nameToIndex.containsKey(team2)) throw new java.lang.IllegalArgumentException();
        return against[nameToIndex.get(team1)][nameToIndex.get(team2)];
    }   
    // number of remaining games between team1 and team2
    public          boolean isEliminated(String team) {
        if (!nameToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException();
        FlowNetwork flowNetwork = new FlowNetwork(numberOfTeams * numberOfTeams + numberOfTeams + 2);
        int check = nameToIndex.get(team);
        int teamIndex = numberOfTeams * numberOfTeams;
        int source = teamIndex + numberOfTeams;
        int sink = teamIndex + numberOfTeams + 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == check) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == check) continue;
                flowNetwork.addEdge(new FlowEdge(source, i * numberOfTeams + j, against[i][j]));
                flowNetwork.addEdge(new FlowEdge(i * numberOfTeams + j, teamIndex + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(i * numberOfTeams + j, teamIndex + j, Double.POSITIVE_INFINITY));
            }
            int temp = wins[check] + remaining[check] - wins[i];
            if (temp < 0) return true;
            flowNetwork.addEdge(new FlowEdge(teamIndex + i, sink, temp));
        }
        FordFulkerson minCut = new FordFulkerson(flowNetwork, source, sink);
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == check) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == check) continue;
                if (minCut.inCut(i * numberOfTeams + j)) return true;
            }
        }
        return false;
    }             
    // is given team eliminated?
    public Iterable<String> certificateOfElimination(String team) {
        if (!nameToIndex.containsKey(team)) throw new java.lang.IllegalArgumentException();
        Set<String> result = new HashSet<String>();
        FlowNetwork flowNetwork = new FlowNetwork(numberOfTeams * numberOfTeams + numberOfTeams + 2);
        int check = nameToIndex.get(team);
        int teamIndex = numberOfTeams * numberOfTeams;
        int source = teamIndex + numberOfTeams;
        int sink = teamIndex + numberOfTeams + 1;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == check) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == check) continue;
                flowNetwork.addEdge(new FlowEdge(source, i * numberOfTeams + j, against[i][j]));
                flowNetwork.addEdge(new FlowEdge(i * numberOfTeams + j, teamIndex + i, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(i * numberOfTeams + j, teamIndex + j, Double.POSITIVE_INFINITY));
            }
            int temp = wins[check] + remaining[check] - wins[i];
            if (temp < 0) {
                result.add(indexToName[i]);
                return result;
            }
            flowNetwork.addEdge(new FlowEdge(teamIndex + i, sink, temp));
        }
        FordFulkerson minCut = new FordFulkerson(flowNetwork, source, sink);

        boolean t = false;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == check) continue;
            for (int j = i + 1; j < numberOfTeams; j++) {
                if (j == check) continue;
                if (minCut.inCut(i * numberOfTeams + j)) t = true;
            }
        }
        if (!t) return null;
        for (int i = 0; i < numberOfTeams; i++) {
            if (i == check) continue;
            if (minCut.inCut(teamIndex + i)) result.add(indexToName[i]);
        }
        return result;
    } 
    // subset R of teams that eliminates given team; null if not eliminated

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