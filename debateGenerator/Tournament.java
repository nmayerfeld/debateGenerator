package debateGenerator;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.Scanner;
public class Tournament {
    private List<Round> rounds;
    private List<String> affTeams;
    private List<String> negTeams;
    private List<String> judges;
    private int numMatchups;
    private String filename;
    private int numRounds;

    public Tournament() {
        this.rounds= new ArrayList<>();
        this.numRounds=0;
        this.numMatchups=0;
        this.affTeams=new ArrayList<>();
        this.negTeams=new ArrayList<>();
        this.judges=new ArrayList<>();
    }

    /**
     * @param numRounds
     */
    public void setNumRounds(int numRounds){
        this.numRounds=numRounds;
    }

    /**
     * @return numRounds
     */
    public int getNumRounds(){
        return this.numRounds;
    }

    /**
     * @param numMatchups
     */
    public void setNumMatchups(int numMatchups){
        this.numMatchups=numMatchups;
    }

    /**
     * @return numTeams
     */
    public int getNumMatchups(){
        return this.numMatchups;
    }

    /**
     * @param teams
     */
    public void setAffTeams(ArrayList<String> teams){
        this.affTeams=teams;
    }

    /**
     * @return affTeams
     */
    public ArrayList<String> getAffTeams(){
        return (ArrayList<String>) this.affTeams;
    }

    /**
     * @param teams
     */
    public void setNegTeams(ArrayList<String> teams){
        this.negTeams=teams;
    }

    /**
     * @return negTeams
     */
    public ArrayList<String> getNegTeams(){
        return (ArrayList<String>) this.negTeams;
    }

    /**
     * @param judges
     */
    public void setJudges(ArrayList<String> judges){
        this.judges=judges;
    }

    /**
     * @return judges
     */
    public ArrayList<String> getJudges(){
        return (ArrayList<String>) this.judges;
    }

    /**
     * @param myRounds
     */
    public void setTournament(ArrayList<Round> myRounds) {
        this.rounds=myRounds;
    }

    /**
     * @param filename
     */
    public void setFilename(String filename){
        this.filename=filename;
    }

    /**
     * @return filename
     */
    public String getFilename(){
        return this.filename;
    }
    /**
     * @param numRounds
     * @return ArrayList of rounds generated
     */
    public ArrayList<Round> generateRounds(int numRounds) {
        ArrayList<Round> roundsGenerated= new ArrayList<>();
        int i=0;
        int superCounter=0;
        while(i<numRounds&&superCounter<30000) {
            boolean worked=this.createSingleRound(roundsGenerated);
            if(worked) {
                i++;
            }
            superCounter++;
        }
        return roundsGenerated;
    }

    /**
     * @param rounds
     * @return whether successful
     */
    public boolean createSingleRound(ArrayList<Round> rounds) {
        //create list with aff teams with dash to match up first so when programming more than 2 rounds the program won't get stuck
        //and be unable to place the dashed teams in later picks
        ArrayList<String> noDashAffTeams= new ArrayList<>();
        ArrayList<String> affTeamsWithDash=new ArrayList<>();
        for(String s:this.affTeams) {
            if(s.contains("-")) {
                affTeamsWithDash.add(s);
            }
            else {
                noDashAffTeams.add(s);
            }
        }
        ArrayList<String> copyOfNegTeams= new ArrayList<>();
        copyOfNegTeams.addAll(this.negTeams);
        ArrayList<String> copyOfJudges= new ArrayList<>();
        copyOfJudges.addAll(this.judges);
        Round r=new Round();
        int counter=0;
        //create the indvidual matches to add to the round
        while(copyOfNegTeams.size()>0) {
            counter++;
            //pick random aff starting with dashed teams
            String myAff=null;
            int newPickAff=0;
            if(affTeamsWithDash.size()>0) {
                newPickAff=(int)(Math.random()*affTeamsWithDash.size());
                myAff=affTeamsWithDash.get(newPickAff);
            }
            else {
                newPickAff=(int)(Math.random()*noDashAffTeams.size());
                myAff=noDashAffTeams.get(newPickAff);
            }
            // pick random neg
            int newPickNeg=(int)(Math.random()*copyOfNegTeams.size());
            String myNeg=copyOfNegTeams.get(newPickNeg);
            //making sure doesn't run endlessly
            if(counter>200) {
                return false;
            }
            //check for judge
            String myJudge=null;
            if(copyOfJudges!=null&&copyOfJudges.size()>0) {
                int newPickJudge=(int)(Math.random()*copyOfJudges.size());
                myJudge=copyOfJudges.get(newPickJudge);
            }
            Match myMatch=new Match(myAff,myNeg,myJudge);
            //check if the judge and team are from the same skwl
            String myJudgeSkwl="";
            if(myJudge!=null){
                if(myJudge.contains("-")) {
                    myJudgeSkwl=myJudge.substring(0,myJudge.length()-2);
                }
                else {
                    myJudgeSkwl=myJudge;
                }
            }
            if(myMatch.getJudge()!=null&&(myJudgeSkwl.equals(myMatch.getAffSkwl())||myJudgeSkwl.equals(myMatch.getNegSkwl()))) {
                continue;
            }
            //check if they are from the same skwl
            if(myMatch.getAffSkwl().equals(myMatch.getNegSkwl())) {
                continue;
            }
            //check if match already exists in this round
            if(r.matches.size()>0&&r.matches.contains(myMatch)) {
                continue;
            }
            //check if it exists in other rounds
            if(rounds.size()>0) {
                boolean contains=false;
                for (Round round :rounds) {
                    if(round.matches.contains(myMatch)) {
                        contains=true;
                    }
                }
                if (contains) {
                    continue;
                }
            }
            //if we've hit this point in the while loop, it doesn't already exist, so it should be added
            r.addMatch(myMatch);
            if(affTeamsWithDash.contains(myAff)) {
                affTeamsWithDash.remove(myAff);
            }
            else {
                noDashAffTeams.remove(myAff);
            }
            copyOfNegTeams.remove(myNeg);
            if(myJudge!=null) {
                copyOfJudges.remove(myJudge);
            }
        }
        if(!rounds.contains(r)) {
            rounds.add(r);
            return true;
        }
        else {
            return false;
        }
    }
	
    @Override
    public String toString() {
        String result="";
        for (Round r: rounds) {
            result+=r.toString();
            result+="\n\n\n\n\n";
        }
        return result;
    }

    /**
     * print the formatted string version of the tournament
     */
    public void printPretty() {
        System.out.println(this.createPrettyString());
    }

    /**
     * @return a formatted string of the tournament matchups
     */
	public String createPrettyString(){
        String s= String.format("%-15s","AFF's:");
        s+= String.format("%-15s","NEG's:");
        s+=String.format ("%-15s %n","JUDGES:");
        int roundNumber=1;
        for(Round r:this.rounds) {
            s+="Round Number: "+String.valueOf(roundNumber)+"\n";
            for(Match m: r.matches) {
                s+=String.format("%-15s",m.getAff());
                s+=String.format("%-15s",m.getNeg());
                s+=String.format("%-15s %n",m.getJudge());
            }
            s+="\n";
            roundNumber++;
        }
        return s;
    }
    public void printTournament() {
        System.out.println(this.toString());
    }
}
