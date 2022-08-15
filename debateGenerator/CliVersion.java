package debateGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CliVersion {
    private Tournament t;

    public static void main(String[] args){
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
        Tournament tournament= new Tournament();
        //create CliVersion
        CliVersion c=new CliVersion(tournament);
        //set numRounds
        c.setNumRounds(tournament);
        //fill lists with teams and judges
        //check if any of the exceptions are thrown due to wrong number of negative teams/judges inputted and print out corrrect messages
        try{
            c.createListsOfTeams(tournament);
        }catch(TooFewTeamsException e){
            System.out.println("THE NUMBER OF AFFIRMATIVE TEAMS MUST BE EQUAL TO THE NUMBER OF NEGATIVE TEAMS. YOU ENTERED: "+tournament.getAffTeams().size()+" AFFIRMATIVE TEAMS AND "+tournament.getNegTeams().size()+" NEGATIVE TEAMS.  PLEASE RETRY");
            return;
        }catch(WrongNumberOfJudgesException s){
            System.out.println("THE NUMBER OF JUDGES MUST BE EQUAL TO THE NUMBER OF MATCHES. YOU ENTERED: "+tournament.getAffTeams().size()+" MATCHES AND "+tournament.getJudges().size()+" JUDGES.  PLEASE RETRY");
            return;
        }
        //generate rounds
        tournament.setTournament(tournament.generateRounds(tournament.getNumRounds()));
        tournament.printPretty();
    }

    /**
     * @param t
     */
    public CliVersion(Tournament t){
        this.t=t;
    }

    /**
     * takes user input to set the number of rounds desired
     */
    public void setNumRounds(Tournament tournament) {
        boolean myChecker=true;
        while(myChecker) {
            System.out.print("Enter the number of rounds to create: ");
            Scanner myScanner= new Scanner(System.in);
            if(myScanner.hasNextInt()) {
                tournament.setNumRounds(myScanner.nextInt());
                myChecker=false;
            }
            else {
                System.out.println("You entered the wrong type of input.  Please enter a number: ");
            }
        }
    }

    /**
     * @throws TooFewTeamsException
     * @throws WrongNumberOfJudgesException
     */
    public void createListsOfTeams(Tournament t) throws TooFewTeamsException, WrongNumberOfJudgesException {
        readAffTeams(t);
        //make sure they enter the same number of teams and judges
        readNegTeams(t); //will thrown an illegal argument exception if wrong number
        readJudges(t);  //will throw an illegal state exception if wrong number
    }

    /**
     * uses scanner to get Affirmative teams from the user
     */
    public void readAffTeams(Tournament t) {
        List<String> affTeams=new ArrayList<>();
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\nPlease enter all of the affirmative teams in this tournament");
        System.out.println("If there are muliple affirmative teams from the same school, please enter them as SCHOOLNAME-LETTER");
        System.out.println("For example, MTA-A, MTA-B.....");
        Boolean run=true;
        while(run) {
            System.out.println("Please enter the next affirmative team name.  If there are no more, please enter the word DONE and press enter: ");
            String aff=myScanner.nextLine().toUpperCase();
            if (aff.equals("DONE")) {
                run=false;
            }
            else {
                affTeams.add(aff);
            }
        }
        t.setAffTeams((ArrayList<String>) affTeams);
    }

    /**
     * uses scanner to get the negative teams from the user
     * @throws TooFewTeamsException
     */
    public void readNegTeams(Tournament t) throws TooFewTeamsException {
        List<String> negTeams=new ArrayList<>();
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\n Are the negative team names the same as the affirmative team names? If so, please type SAME.  If not, please type DIFFERENT");
        String answer=myScanner.nextLine().toUpperCase();
        if(answer.equals("SAME")) {
            t.setNegTeams(t.getAffTeams());
        }
        else if(answer.equals("DIFFERENT")) {
            Boolean run=true;
            while(run) {
                System.out.println("Please enter the next negative team name.  If there are no more, please enter the word DONE as shown");
                String aff=myScanner.nextLine().toUpperCase();
                if (aff.equals("DONE")) {
                    run=false;
                }
                else {
                    negTeams.add(aff);
                }
            }
            t.setNegTeams((ArrayList<String>) negTeams);
        }
        //check to make sure there are the same number of AFF teams as NEG
        if(t.getAffTeams().size()!=t.getNegTeams().size()) {
            throw new TooFewTeamsException();
        }
    }

    /**
     * uses scanner to get the list of judges from the user
     * @throws WrongNumberOfJudgesException
     */
    public void readJudges(Tournament t) throws WrongNumberOfJudgesException {
        List<String> judges=new ArrayList<>();
        Scanner myScanner=new Scanner(System.in);
        System.out.println("\n\n\nPlease enter all of the judges in this tournament");
        System.out.println("If there are muliple judges with the same last name, please include a first initial");
        System.out.println("If there are muliple judges from the same skwl, please enter the school name followed by a dash and a number, ex: mta-1, mta-2,...");
        System.out.println("If you don't want to set the judges, enter the word NONE and press enter: ");
        Boolean run=true;
        while(run) {
            System.out.println("Please enter the next judge and press enter: ");
            System.out.println("If there are no more, please enter enter the word DONE and press enter");
            String judge=myScanner.nextLine().toUpperCase();
            if (judge.equals("NONE")||judge.equals("DONE")) {
                run=false;
            }
            else {
                judges.add(judge);
            }
        }
        t.setJudges((ArrayList<String>) judges);
        if(judges.size()!=t.getAffTeams().size()&&judges.size()!=0) {
            throw new WrongNumberOfJudgesException();
        }
    }
}
