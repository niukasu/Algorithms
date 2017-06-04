import edu.princeton.cs.algs4.In;
import java.lang.*;
import java.util.*;

public class BaseballElimination {
	private int numberOfTeams;
	private HashMap<String,Integer> nameToIndex;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private int[][] against;
	public BaseballElimination(String filename) {
		In readfile = new In(filename);
		numberOfTeams = readfile.readInt();
		nameToIndex = new HashMap<String, Integer>();
		wins = new int[numberOfTeams];
		losses = new int[numberOfTeams];
		remaining = new int[numberOfTeams];
		against = new int[numberOfTeams][numberOfTeams];
		for (int i = 0; i < numberOfTeams; i++) {
			String temp = readfile.readString();
			nameToIndex.put(temp, i);
			wins[i] = readfile.readInt();
			losses[i] = readfile.readInt();
			remaining[i] = readfile.readInt();
			for (int j = 0; j < numberOfTeams; j++) {
				against[i][j] = readfile.readInt();
			}
		}
	}                   
	// create a baseball division from given filename in format specified below
	//public              int numberOfTeams()                        
	// number of teams
	//public Iterable<String> teams()                                
	// all teams
	//public              int wins(String team)                      
	// number of wins for given team
	//public              int losses(String team)                    
	// number of losses for given team
	//public              int remaining(String team)                 
	// number of remaining games for given team
	//public              int against(String team1, String team2)    
	// number of remaining games between team1 and team2
	//public          boolean isEliminated(String team)              
	// is given team eliminated?
	//public Iterable<String> certificateOfElimination(String team)  
	// subset R of teams that eliminates given team; null if not eliminated

	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination(args[0]);
	    /*
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
	    */
	}
}