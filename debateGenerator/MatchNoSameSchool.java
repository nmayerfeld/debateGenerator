package debateGenerator;
//doesn't allow same school matches
public class MatchNoSameSchool implements Match {
    private String aff;
    private String neg;
    private String judge;
    protected String affSkwl;
    protected String negSkwl;
	
    /**
     * Creates a new Match instance
     * @param aff team
     * @param neg team
     */
    public MatchNoSameSchool (String aff,String neg) {
        this.aff=aff;
        this.neg=neg;
        if(this.aff.contains("-")) {
            this.affSkwl=this.aff.substring(0,this.aff.indexOf("-"));
        }
        else {
            this.affSkwl=this.aff;
        }
        if(this.neg.contains("-")) {
            this.negSkwl=this.neg.substring(0,this.neg.indexOf("-"));
        }
        else {
            this.negSkwl=this.neg;
        }
    }
	
    /**
     * Creates a new Match instance with a judge
     * @param aff team
     * @param neg team
     * @param judge
     */
    public MatchNoSameSchool (String aff,String neg, String judge) {
        this(aff,neg);
        this.judge=judge;
    }
	
    /**
     * @return negative school
     */
    public String getNegSkwl() {
        return this.negSkwl;
    }
	
    /**
     * @return affirmative school
     */
    public String getAffSkwl() {
        return this.affSkwl;
    }
	
    /**
     * @return affirmative
     */
    public String getAff() {
        return this.aff;
    }
	
    /**
     * @return negative 
     */
    public String getNeg() {
        return this.neg;
    }
	
    /**
     * @return judge
     */
    public String getJudge() {
        return this.judge;
    }
	
    @Override
    public String toString() {
        String result=this.aff+"\t"+this.neg;
        if(this.judge!=null) {
            result+="\t";
            result+=this.judge;
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
        Match other=(Match) obj;
        //if same aff team plays the same school twice
        if(this.aff.equals(other.getAff())&&this.negSkwl.equals(other.getNegSkwl())) {
            return true;
        }
        //if same neg team plays the other school twice
        else if(this.neg.equals(other.getNeg())&&this.affSkwl.equals(other.getAffSkwl())) {
            return true;
        }
        else if(this.judge!=null&&other.getJudge()!=null&&this.judge.equals(other.getJudge())&&(this.aff.equals(other.getAff())||this.neg.equals(other.getNeg()))) {
            return true;
        }
        else {
            return false;
        }
    }
	
    @Override
    public int hashCode() {
		/*there's no way that I can think of to write this method, because of how equality here works- matches are equal if either they have the same aff and neg teams,
		or if the same judge is judging the same aff or neg team in both matches, so I can't find a way to account for both of those options of equality in this method.
		as such, I did the following, and the contains method will just take a little longer, but it will still be better overall than using arrays and skipping the hashcode altogether*/
        return 1;
    }
}
