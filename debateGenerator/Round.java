package debateGenerator;
import java.util.*;

public class Round {
    protected List<Match> matches;
	
    public Round() {
        this.matches= new ArrayList<>();
    }
   
    /**
     * @param aff
     * @param neg
     * @param judge
     */
    public void setMatch(String aff, String neg, String judge) {
        matches.add(new Match(aff,neg,judge));
    }
	
   /**
     * @param aff
     * @param neg
     */
    public void setMatch(String aff, String neg) {
        matches.add(new Match (aff,neg));
    }
	
   /**
     * @param m
     */
    public void addMatch(Match m) {
        matches.add(m);
    }

    @Override
    public String toString() {
        String result="";
        for(Match m:matches) {
            result+=m.toString();
            result+="\n";
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        //see if it's the same object
        if(this == obj) {
            return true;
        }
        //see if it's null
        if(obj == null) {
            return false;
        }
        //see if they're from the same class
        if(getClass()!=obj.getClass()) {
            return false;
        }
        Round other=(Round)obj;
        //check if they contain any of the same match
        for (int i=0;i<other.matches.size();i++) {
            if(this.matches.contains(other.matches.get(i))) {
                return true;
            }
        }
        return false;
    }
	
    @Override
    public int hashCode() {
		/*see comments in the match class about the difficulty of writing a functioning hashcode method due to the different possibilities for equality
		here, I limited the options slightly by using the hashcode method for the size of matches*/
        Integer round=(Integer)(this.matches.size());
        return round.hashCode();
    }
}
