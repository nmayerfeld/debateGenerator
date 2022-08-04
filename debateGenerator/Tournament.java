package debateGenerator;
import java.util.*;
import java.util.Scanner;
public class Tournament
{
    private List<Round> rounds;
    private List<String> affTeams;
    private List<String> negTeams;
    private List<String> judges;
    private int numRounds;
    public static void main (String[] args)
    {
		/*steps
		1) SET NUMBER OF ROUNDS
		2)SET NUM OF MATCHES IN EACH ROUND
		3) RUN THROUGH USER INPUT
		4) CREATE A MATCH BY RANDOMLY PICKING AN AFF AND A NEG AND JUDGE, CHECK IF THEY ARE THE SAME AS ANOTHER MATCH IN THAT ROUND, IF NOT, ADD TO THE ROUND
				REMOVE THEM FROM THE ORIGINAL LISTS
		5) LOOP THAT AS MANY TIMES AS ROUNDS NEEDED (EACH ADDITIONAL ROUND CHECKING THAT IT ISN'T THE SAME AS PREVIOUS ONES)
		6) PRINT
		*/
        // create tournament
        Tournament tournament= new Tournament(0);
        //set numRounds
        tournament.setNumRounds();
        //fill lists with teams and judges
        //check if any of the exceptions are thrown due to wrong number of negative teams/judges inputted and print out corrrect messages
        try{
            tournament.createListsOfTeams();
        }catch(TooFewTeamsException e){
            System.out.println("THE NUMBER OF AFFIRMATIVE TEAMS MUST BE EQUAL TO THE NUMBER OF NEGATIVE TEAMS. YOU ENTERED: "+tournament.affTeams.size()+" AFFIRMATIVE TEAMS AND "+tournament.negTeams.size()+" NEGATIVE TEAMS.  PLEASE RETRY");
            return;
        }catch(WrongNumberOfJudgesException s){
            System.out.println("THE NUMBER OF JUDGES MUST BE EQUAL TO THE NUMBER OF MATCHES. YOU ENTERED: "+tournament.affTeams.size()+" MATCHES AND "+tournament.judges.size()+" JUDGES.  PLEASE RETRY");
            return;
        }
        //generate rounds
        tournament.setTournament(tournament.generateRounds(tournament.numRounds));
        tournament.printPretty();
    }
    public Tournament(int numRounds)
    {
        this.rounds= new ArrayList<>();
        this.numRounds=numRounds;
        this.affTeams=new ArrayList<>();
        this.negTeams=new ArrayList<>();
        this.judges=new ArrayList<>();
    }
    public void setTournament(ArrayList<Round> myRounds)
    {
        this.rounds=myRounds;
    }
    public ArrayList<Round> generateRounds(int numRounds)
    {
        ArrayList<Round> roundsGenerated= new ArrayList<>();
        int i=0;
        int superCounter=0;
        while(i<numRounds&&superCounter<30000)
        {
            boolean worked=this.createSingleRound(roundsGenerated);
            if(worked)
            {
                i++;
            }
            superCounter++;
        }
        return roundsGenerated;
    }
    public boolean createSingleRound(ArrayList<Round> rounds)
    {
        //create list with aff teams with dash to match up first so when programming more than 2 rounds the program won't get stuck
        //and be unable to place the dashed teams in later picks
        ArrayList<String> noDashAffTeams= new ArrayList<>();
        ArrayList<String> affTeamsWithDash=new ArrayList<>();
        for(String s:this.affTeams)
        {
            if(s.contains("-"))
            {
                affTeamsWithDash.add(s);
            }
            else
            {
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
        while(copyOfNegTeams.size()>0)
        {
            counter++;
            //pick random aff starting with dashed teams
            String myAff=null;
            int newPickAff=0;
            if(affTeamsWithDash.size()>0)
            {
                newPickAff=(int)(Math.random()*affTeamsWithDash.size());
                myAff=affTeamsWithDash.get(newPickAff);
            }
            else
            {
                newPickAff=(int)(Math.random()*noDashAffTeams.size());
                myAff=noDashAffTeams.get(newPickAff);
            }
            // pick random neg
            int newPickNeg=(int)(Math.random()*copyOfNegTeams.size());
            String myNeg=copyOfNegTeams.get(newPickNeg);
            //making sure doesn't run endlessly
            if(counter>200)
            {
                return false;
            }
            //check for judge
            String myJudge=null;
            if(copyOfJudges!=null&&copyOfJudges.size()>0)
            {
                int newPickJudge=(int)(Math.random()*copyOfJudges.size());
                myJudge=copyOfJudges.get(newPickJudge);
            }
            Match myMatch=new Match(myAff,myNeg,myJudge);
            //check if the judge and team are from the same skwl
            String myJudgeSkwl="";
            if(myJudge.contains("-"))
            {
                myJudgeSkwl=myJudge.substring(0,myJudge.length()-2);
            }
            else
            {
                myJudgeSkwl=myJudge;
            }
            if(myMatch.getJudge()!=null&&(myJudgeSkwl.equals(myMatch.getAffSkwl())||myJudgeSkwl.equals(myMatch.getNegSkwl())))
            {
                continue;
            }
            //check if they are from the same skwl
            if(myMatch.getAffSkwl().equals(myMatch.getNegSkwl()))
            {
                continue;
            }
            //check if match already exists in this round
            if(r.matches.size()>0&&r.matches.contains(myMatch))
            {
                continue;
            }
            //check if it exists in other rounds
            if(rounds.size()>0)
            {
                boolean contains=false;
                for (Round round :rounds)
                {
                    if(round.matches.contains(myMatch))
                    {
                        contains=true;
                    }
                }
                if (contains)
                {
                    continue;
                }
            }
            //if we've hit this point in the while loop, it doesn't already exist, so it should be added
            r.addMatch(myMatch);
            if(affTeamsWithDash.contains(myAff))
            {
                affTeamsWithDash.remove(myAff);
            }
            else
            {
                noDashAffTeams.remove(myAff);
            }
            copyOfNegTeams.remove(myNeg);
            if(myJudge!=null)
            {
                copyOfJudges.remove(myJudge);
            }
        }
        if(!rounds.contains(r))
        {
            rounds.add(r);
            return true;
        }
        else
        {
            return false;
        }
    }
    public void setNumRounds()
    {
        boolean myChecker=true;
        while(myChecker)
        {
            System.out.print("Enter the number of rounds to create: ");
            Scanner myScanner= new Scanner(System.in);
            if(myScanner.hasNextInt())
            {
                this.numRounds=myScanner.nextInt();
                myChecker=false;
            }
            else
            {
                System.out.println("You entered the wrong type of input.  Please enter a number: ");
            }
        }
    }
    public void createListsOfTeams() throws TooFewTeamsException, WrongNumberOfJudgesException
    {
        readAffTeams();
        //make sure they enter the same number of teams and judges
        readNegTeams(); //will thrown an illegal argument exception if wrong number
        readJudges();  //will throw an illegal state exception if wrong number
    }
    public void readAffTeams()
    {
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\nPlease enter all of the affirmative teams in this tournament");
        System.out.println("If there are muliple affirmative teams from the same school, please enter them as SCHOOLNAME-LETTER");
        System.out.println("For example, MTA-A, MTA-B.....");
        Boolean run=true;
        while(run)
        {
            System.out.println("Please enter the next affirmative team name.  If there are no more, please enter the word DONE and press enter: ");
            String aff=myScanner.nextLine().toUpperCase();
            if (aff.equals("DONE"))
            {
                run=false;
            }
            else
            {
                this.affTeams.add(aff);
            }
        }
    }
    public void readNegTeams() throws TooFewTeamsException
    {
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\n Are the negative team names the same as the affirmative team names? If so, please type SAME.  If not, please type DIFFERENT");
        String answer=myScanner.nextLine().toUpperCase();
        if(answer.equals("SAME"))
        {
            this.negTeams.addAll(this.affTeams);
        }
        else if(answer.equals("DIFFERENT"))
        {
            Boolean run=true;
            while(run)
            {
                System.out.println("Please enter the next negative team name.  If there are no more, please enter the word DONE as shown");
                String aff=myScanner.nextLine().toUpperCase();
                if (aff.equals("DONE"))
                {
                    run=false;
                }
                else
                {
                    this.negTeams.add(aff);
                }
            }
        }
        //check to make sure there are the same number of AFF teams as NEG
        if(this.affTeams.size()!=this.negTeams.size())
        {
            throw new TooFewTeamsException();
        }
    }
    public void readJudges() throws WrongNumberOfJudgesException
    {
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\nPlease enter all of the judges in this tournament");
        System.out.println("If there are muliple judges with the same last name, please include a first initial");
        System.out.println("If there are muliple judges from the same skwl, please enter the school name followed by a dash and a number, ex: mta-1, mta-2,...");
        System.out.println("If you don't want to set the judges, enter the word NONE and press enter: ");
        Boolean run=true;
        while(run)
        {
            System.out.println("Please enter the next judge and press enter: ");
            System.out.println("If there are no more, please enter enter the word DONE and press enter");
            String judge=myScanner.nextLine().toUpperCase();
            if (judge.equals("NONE")||judge.equals("DONE"))
            {
                run=false;
            }
            else
            {
                this.judges.add(judge);
            }
        }
        if(this.judges.size()!=this.negTeams.size()&&this.judges.size()!=0)
        {
            throw new WrongNumberOfJudgesException();
        }
    }
    //public Round generateRound()
    public String toString()
    {
        String result="";
        for (Round r: rounds)
        {
            result+=r.toString();
            result+="\n\n\n\n\n";
        }
        return result;
    }
    public void printPretty()
    {
        System.out.println("\n\n");
        System.out.printf ("%-15s", "AFF's:");
        System.out.printf ("%-15s", "NEG's:");
        System.out.printf ("%-15s %n", "JUDGES:");
        System.out.println("\n");
        int roundNumber=1;
        for(Round r:this.rounds)
        {
            System.out.println("Round Number: "+roundNumber);
            for(Match m: r.matches)
            {
                System.out.printf ("%-15s", m.getAff());
                System.out.printf ("%-15s", m.getNeg());
                System.out.printf ("%-15s %n", m.getJudge());
            }
            System.out.println("\n\n\n");
            roundNumber++;
        }
    }
    public void printTournament()
    {
        System.out.println(this.toString());
    }
}