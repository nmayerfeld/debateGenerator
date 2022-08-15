package debateGenerator;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class GUIForDebateGenerator {
    private Tournament t;
    private int numRounds;
    private int numMatchups;
    private String[] affTeams;
    private String[] negTeams;
    private String[] judges;
    private boolean cancel=false;

    public static void main (String[] args) {
        Tournament t = new Tournament();
        GUIForDebateGenerator GUIFDG=new GUIForDebateGenerator(t);
    }

    /**
     * @param t
     */
    public GUIForDebateGenerator(Tournament t) {
        this.t=t;
        /*
           GUIs lead from one to the next when button is clicked to solve the problem of the program moving on before the button has been clicked
           The path is as follows
           1) a getNum GUI to get the number of rounds
           2) a getNum GUI to get the number of matches
           3) a getList GUI to get the list of aff teams
           4) a YesNo GUI to see if neg teams are the same
            4B) if necessary, a getList GUI to get the neg teams
           5) a YesNo GUI to see if user wants to input judges
            5B) if necessary, a getList GUI to get the judges
           6) a Filename GUI to get the filename (which also created the tournament and saves it to a txt file)
           7) a Display GUI to display the resulting schedule immediately to the user
         */
        GUIGetNum gGN=new GUIGetNum("Please enter the number of rounds: ","rounds",t);
    }

    //inner class creating a gui that ask for a number for something (used for numRounds then numMatches)
    class GUIGetNum implements ActionListener {
        private int num;
        private JButton enter;
        private JFrame myFrame;
        private JLabel myLabel;
        private JTextField j;
        private Tournament t;
        private String fieldAccepting;

        /**
         * @param str
         * @param fieldAccepting
         * @param t
         */
        public GUIGetNum(String str, String fieldAccepting,Tournament t) {
            this.fieldAccepting=fieldAccepting;
            this.t=t;
            myFrame = new JFrame("Welcome to the debate generator.");
            myFrame.setSize(600,400);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myLabel= new JLabel(str, JLabel.CENTER);
            myLabel.setBounds(0,40,600,25);
            myFrame.add(myLabel);
            j=new JTextField();
            j.setBounds(280,100,40,25);
            myFrame.add(j);
            JPanel panel=new JPanel();
            panel.setLayout(null);
            this.enter=new JButton("Enter");
            this.enter.setBounds(220,180,160,25);
            enter.addActionListener(this);
            panel.add(enter);
            myFrame.add(panel);
            myFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            try{
                this.num=Integer.parseInt(j.getText());
                myFrame.dispose();
                if(this.fieldAccepting.equals("rounds")){
                    t.setNumRounds(this.num);
                    GUIGetNum g2=new GUIGetNum("Please enter the number of teams: ", "matchups", this.t);
                }
                else{ // "matchups"
                    myFrame.dispose();
                    t.setNumMatchups(this.num);
                    GUIGetList gGL=new GUIGetList("Please enter the names of the Affirmative teams, one per line: ", "If there are multiple teams from the same school, please use a dash (eg MTA-A, MTA-B, etc...)","affirmative",this.t,this.num);
                }
            } catch (NumberFormatException ex) {
                myFrame.dispose();
                String str="You have failed to enter a number.  Please enter the number of "+this.fieldAccepting+": ";
                new GUIGetNum(str, this.fieldAccepting,this.t);
            }
        }
    }

    //class creating a gui that takes in a list typed by the user (used for aff teams and neg teams and judges if necessary)
    class GUIGetList implements ActionListener{
        private List<String> list;
        private List<JTextField> listOfTextFields;
        private String myString;
        private int numOfField;
        private JButton enter;
        private JFrame myFrame;
        private JLabel myLabel;
        private JLabel instructions;
        private Tournament t;
        private String fieldAccepting;

        /**
         * @param str
         * @param directions
         * @param fieldAccepting
         * @param t
         * @param numOfField
         */
        public GUIGetList(String str, String directions,String fieldAccepting,Tournament t, int numOfField) {
            this.fieldAccepting=fieldAccepting;
            this.t=t;
            list=new ArrayList<>();
            listOfTextFields=new ArrayList<>();
            myFrame = new JFrame("Welcome to the debate generator.");
            int height = (65*numOfField)+260;
            myFrame.setSize(650,height);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myLabel= new JLabel(str, JLabel.CENTER);
            myLabel.setBounds(0,40,650,25);
            myFrame.add(myLabel);
            instructions=new JLabel(directions,JLabel.CENTER);
            instructions.setBounds(0,100,650,25);
            myFrame.add(instructions);
            int currentBoundHeight=160;
            for (int i=0;i<numOfField;i++) {
                JTextField jTF=new JTextField(JTextField.CENTER);
                jTF.setBounds(250,currentBoundHeight,150,25);
                currentBoundHeight+=65;
                myFrame.add(jTF);
                this.listOfTextFields.add(jTF);
            }
            JPanel panel2=new JPanel();
            panel2.setLayout(null);
            this.enter=new JButton("Enter");
            this.enter.setBounds(245,currentBoundHeight+40,160,25);
            enter.addActionListener(this);
            panel2.add(enter);
            myFrame.add(panel2);
            myFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            for(JTextField j: this.listOfTextFields) {
                this.list.add(j.getText().toUpperCase());
            }
            myFrame.dispose();
            if(this.fieldAccepting.equals("affirmative")){
                this.t.setAffTeams((ArrayList<String>) this.list);
                GUIInputYesNo gYNNeg=new GUIInputYesNo("Are the negative team names the same as the affirmative team names? ","negative",this.t);
            }
            else if (this.fieldAccepting.equals("negative")){
                this.t.setNegTeams((ArrayList<String>) this.list);
                GUIInputYesNo gYNJudges=new GUIInputYesNo("Would you like to add Judges?","judges",this.t);
            }
            else{ //judges
                this.t.setJudges((ArrayList<String>) this.list);
                GUIInputFileName gFN=new GUIInputFileName(this.t);
            }
        }
    }

    //class creating a gui to take a yes or no from the user in response to a question (used to see if neg teams are same as aff and if user wants to input judges)
    class GUIInputYesNo implements ActionListener{
        private JButton yes;
        private JButton no;
        private JFrame myFrame;
        private JLabel myLabel;
        private JTextField j;
        private Tournament t;
        private String fieldRegarding;

        /**
         * @param str
         * @param fieldRegarding
         * @param t
         */
        public GUIInputYesNo(String str, String fieldRegarding,Tournament t) {
            this.fieldRegarding=fieldRegarding;
            this.t=t;
            myFrame = new JFrame("Welcome to the debate generator.");
            myFrame.setSize(600,200);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myLabel= new JLabel(str, JLabel.CENTER);
            myLabel.setBounds(0,40,600,25);
            myFrame.add(myLabel);
            JPanel panel=new JPanel();
            panel.setLayout(null);
            this.yes=new JButton("Yes");
            this.yes.setBounds(92,100,162,25);
            yes.addActionListener(this);
            yes.setActionCommand("Yes");
            panel.add(yes);
            this.no=new JButton("No");
            this.no.setBounds(346,100,162,25);
            no.addActionListener(this);
            no.setActionCommand("No");
            panel.add(no);
            myFrame.add(panel);
            myFrame.setVisible(true);
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            String command=e.getActionCommand();
            myFrame.dispose();
            if(fieldRegarding.equals("negative")){
                if(command.equals("No")){
                    //allow user to input negative teams
                    GUIGetList gLNeg=new GUIGetList("Please enter the negative teams: ","If there are multiple teams from the same school, please use a dash (eg MTA-A, MTA-B, etc...)","negative",this.t,t.getNumMatchups());
                }
                else{
                    //automatically input negative teams
                    t.setNegTeams(t.getAffTeams());
                    //ask user whether they want to input judges
                    GUIInputYesNo gYNJudges=new GUIInputYesNo("Would you like to add Judges?","judges",this.t);
                }
            }
            else{ //fieldRegarding.equals("judges")
                if(command.equals("Yes")){
                    myFrame.dispose();
                    GUIGetList gLJudges=new GUIGetList("Please enter the last names of the judges: ", "If multiple judges have the same last name, please enter the first initial (eg J. Schwartz)","judges",this.t,t.getNumMatchups());
                }
                else{ //answer was no
                    GUIInputFileName gFN=new GUIInputFileName(this.t);
                }
            }
        }
    }

    //class saving the file name that the user wants to save result to, implements the logic to create tournament and saves it in txt file
    class GUIInputFileName implements ActionListener {
        private String Filename;
        private JButton enter;
        private JFrame myFrame;
        private JLabel myLabel;
        private JTextField j;
        private Tournament t;

        /**
         * @param t
         */
        public GUIInputFileName(Tournament t) {
            this.t=t;
            myFrame = new JFrame("Welcome to the debate generator.");
            myFrame.setSize(800,400);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            myLabel= new JLabel("Please input the filename.  It will be saved as <replace with your input>.txt in the current directory", JLabel.CENTER);
            myLabel.setBounds(0,40,800,25);
            myFrame.add(myLabel);
            JLabel l= new JLabel("valid filenames only please", JLabel.CENTER);
            l.setBounds(0,105,800,25);
            myFrame.add(l);
            j=new JTextField();
            j.setBounds(380,170,40,25);
            myFrame.add(j);
            JPanel panel=new JPanel();
            panel.setLayout(null);
            this.enter=new JButton("Enter");
            this.enter.setBounds(320,235,160,25);
            enter.addActionListener(this);
            panel.add(enter);
            myFrame.add(panel);
            myFrame.setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            myFrame.dispose();
            t.setTournament(t.generateRounds(t.getNumRounds()));
            String result=t.createPrettyString();
            t.setFilename(j.getText()+".txt");
            File f=new File(t.getFilename());
            try {
                Files.writeString(Path.of(t.getFilename()),result);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            GUIDisplayResult gDR=new GUIDisplayResult(result);
        }
    }

    //class creating a gui that displays the resulting tournament schedule
    class GUIDisplayResult{
        private JFrame myFrame;
        private JTextArea j;

        /**
         * @param s
         */
        public GUIDisplayResult(String s) {
            myFrame = new JFrame("Here are your results");
            myFrame.setSize(600, 600);
            myFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            j = new JTextArea(s);
            j.setBounds(5, 5, 595, 595);
            myFrame.add(j);
            myFrame.setVisible(true);
        }
    }
}
