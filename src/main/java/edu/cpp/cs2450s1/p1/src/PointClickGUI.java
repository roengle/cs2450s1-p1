package edu.cpp.cs2450s1.p1.src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Component;
import java.awt.Paint;
import java.awt.event.*;
import java.text.*;
import java.util.Date;
import java.util.Random;
import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import javax.swing.text.*;
/**
 *
 * @author Team SwingSharp
 */
public class PointClickGUI extends javax.swing.JFrame {
    Random r = new Random();
    String[] wordList = {"abstract", "cemetery", "nurse", "pharmacy", "climbing"};
    String[] colorList = {"RED", "YELLOW", "GREEN", "BLUE", "PURPLE"};
    boolean[] sudokuDeductible = new boolean[54];
    JTextField[] sudokuBoxes = new JTextField[54];
    int[] sudokuSolution = {3,5,1,9,2,2,9,6,8,5,7,3,1,4,7,2,9,3,8,6,1,4,2,1,2,3,6,8,5,4,9,7,5,9,6,6,7,8,1,3,4,9,8,3,4,5,2,7,6,7,4,6,8,1};
    static HashMap<String, Integer> userScoreMap = new HashMap<>();
    Integer score, sudokuScore;
    String chosenWord;
    String sudokuBlankToolTip = "Enter a number";
    static int round = 1;
    static int randomColorText, randomColor, chosenColor;
    final URL SAVES_PATH = getClass().getResource("/saves/highscores.txt");
    JPanel currentPanel;
    JPanel[] allPanels;
    

    /**
     * Creates new form PointClickFrame
     */
    public PointClickGUI() {
        
        initComponents();
        
        currentPanel = StartPanel;

        DisplayPanel.setVisible(false);
        PlayPanel.setVisible(false);
        PlayPanel2.setVisible(false);
        PlayPanel3.setVisible(false);
        HighscorePanel.setVisible(false);
        CreditsPanel.setVisible(false);
        EndPanel.setVisible(false);
        randomTest.setVisible(false);
        NewHSPanel.setVisible(false);
        F1Panel.setVisible(false);
        
        allPanels = new JPanel[]{DisplayPanel, PlayPanel, PlayPanel2, 
                                    PlayPanel3, HighscorePanel, CreditsPanel, 
                                    EndPanel, NewHSPanel};
        
        
        //Set System Timer
        Timer dateTimer = new Timer(1000, new ActionListener(){ //Updates every second
            public void actionPerformed(ActionEvent e)
            {
                Date date = new Date();                                 
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, y | HH:mm:ss");
                systemTimeText.setText(sdf.format(date));
                systemTimeText2.setText(sdf.format(date));
                systemTimeText3.setText(sdf.format(date));
            }
        });
        dateTimer.start();
        
        /* F1 key and Esc key functionality */
        //Create action for F1 key press
        Action f1MenuAction = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                currentPanel.setVisible(false);
                F1Panel.setVisible(true);
            }
        };
        //Create action for ESCAPE key press
        Action escAction = new AbstractAction(){
            @Override
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        };
        //Put the proper keystroke-identifier and identifier-action pairs 
        //in the input and action maps respectively.
        for(JPanel p : allPanels){
            p.getInputMap(2).put(KeyStroke.getKeyStroke("F1"), "f1_menu");
            p.getInputMap(2).put(KeyStroke.getKeyStroke("ESCAPE"), "esc");
            
            p.getActionMap().put("f1_menu", f1MenuAction);
            p.getActionMap().put("esc", escAction);
        }
        
        
        //Set Screen Change Timer
        Timer timer = new Timer(3000, new ActionListener(){           //Switches screen after 3 seconds
            public void actionPerformed(ActionEvent e)
            {
                StartPanel.setVisible(false);
                DisplayPanel.setVisible(true);
                currentPanel = DisplayPanel;
            }
        });
        timer.setRepeats(false);   //Runs once
        timer.start();
        
        for(int i = 0; i < sudokuDeductible.length; i++)                          //set all boxes deductible
            sudokuDeductible[i] = true;
        sudokuBoxes = new JTextField[]{sudokuBox2,sudokuBox3,sudokuBox5,sudokuBox7,sudokuBox8,
            sudokuBox10,sudokuBox11,sudokuBox12,sudokuBox13,sudokuBox14,sudokuBox15,sudokuBox17,sudokuBox18,
            sudokuBox19,sudokuBox21,sudokuBox22,sudokuBox23,sudokuBox24,sudokuBox27,
            sudokuBox29,sudokuBox31,sudokuBox33,sudokuBox36,
            sudokuBox37,sudokuBox38,sudokuBox39,sudokuBox40,sudokuBox42,sudokuBox43,sudokuBox44,sudokuBox45,
            sudokuBox46,sudokuBox49,sudokuBox51,sudokuBox53,
            sudokuBox55,sudokuBox58,sudokuBox59,sudokuBox60,sudokuBox61,sudokuBox63,
            sudokuBox64,sudokuBox65,sudokuBox67,sudokuBox68,sudokuBox69,sudokuBox70,sudokuBox71,sudokuBox72,
            sudokuBox74,sudokuBox75,sudokuBox77,sudokuBox79,sudokuBox80};
        
        //Alerts user if anything other than 0-9 is pressed or already has a number
        for(JTextField field : sudokuBoxes){
            field.addKeyListener(new KeyAdapter() {
                public void keyTyped(KeyEvent e) {
                    char c = e.getKeyChar();
                    if (!((c >= '0') && (c <= '9') || (c == KeyEvent.VK_BACK_SPACE) || (c == KeyEvent.VK_DELETE)) || field.getText().length() > 0) {
                        getToolkit().beep();
                        e.consume();
                    }
                }
            });     
        }

    }


        
    /**
     * Is called when the user guesses incorrectly. Deducts 10 from the user's score and adds one limb
     * onto the hangman picture. After doing this, the methods checks if the user has guessed incorrectly 6 times
     * (by checking if the score equals 40). If so, the end game event occurs.
     */
    private void penaliseScore() {
        //Decrement score by 10
        score -= 10;
        //Add image based on score
        switch(score){
            case 90:
                headImage.setVisible(true);
                break;
            case 80:
                bodyImage.setVisible(true);
                break;
            case 70:
                leftArmImage.setVisible(true);
                break;
            case 60:
                rightArmImage.setVisible(true);
                break;
            case 50:
                leftLegImage.setVisible(true);
                break;
            case 40:
                rightLegImage.setVisible(true);
                break;  
        }
        
        //Set the score text box with the new score value
        txtScore.setText(String.format("Score: %d", score));
        //Check if there are six incorrect guesses
        if(score <= 40){
            //Trigger end game event
            endGame();
        }
    }
    
    /**
     * Ends the game. Is called when (1) the user has guessed incorrectly 6 times,
     * (2) the user guesses all letters in the word, or (3) the user clicks on the "skip"
     * button. 
     */
    private void endGame(){    //Game: Hangman
        PlayPanel.setVisible(false);
        PlayPanel2.setVisible(true);
        currentPanel = PlayPanel2;
        
        //Reset buttons
        JButton[] buttons = new JButton[]{AButton,BButton,CButton,DButton,EButton,FButton,GButton,HButton,IButton, 
            JButton,KButton,LButton,MButton,NButton,OButton,PButton,QButton,RButton,
            SButton,TButton,UButton,VButton,WButton,XButton,YButton,ZButton};
        
        for(JButton b : buttons){
            b.setEnabled(true);
        }
        
        txtScore2.setText(String.format("Score: %d", score));
    }
    
    private void endGame2(){    //Game: Color Buttons
        PlayPanel2.setVisible(false);
        PlayPanel3.setVisible(true);
        currentPanel = PlayPanel3;
        
        txtScore4.setText(String.format("Total Score: %d", score+sudokuScore));
    }
    
    
    private void endGame3(){    //Game: Sudoku
        txtEndScore.setText(String.format("%d",score));
        
        //Reset textfields
        JTextField[] sudokuBoxes = new JTextField[]{sudokuBox2,sudokuBox3,sudokuBox5,sudokuBox7,sudokuBox8,
            sudokuBox10,sudokuBox11,sudokuBox12,sudokuBox13,sudokuBox14,sudokuBox15,sudokuBox17,sudokuBox18,
            sudokuBox19,sudokuBox21,sudokuBox22,sudokuBox23,sudokuBox24,sudokuBox27,
            sudokuBox29,sudokuBox31,sudokuBox33,sudokuBox36,
            sudokuBox37,sudokuBox38,sudokuBox39,sudokuBox40,sudokuBox42,sudokuBox43,sudokuBox44,sudokuBox45,
            sudokuBox46,sudokuBox49,sudokuBox51,sudokuBox53,
            sudokuBox55,sudokuBox58,sudokuBox59,sudokuBox60,sudokuBox61,sudokuBox63,
            sudokuBox64,sudokuBox65,sudokuBox67,sudokuBox68,sudokuBox69,sudokuBox70,sudokuBox71,sudokuBox72,
            sudokuBox74,sudokuBox75,sudokuBox77,sudokuBox79,sudokuBox80};
        for(int i = 0; i < sudokuBoxes.length; i++)
            sudokuBoxes[i].setText("");
        
        for(int i = 0; i < sudokuDeductible.length; i++)                          //set all boxes deductible
            sudokuDeductible[i] = true;
        
        updateHighScores();
    }
    
    /**
     * A method used for all the A-Z game buttons to prevent repeating code. Performs the following
     * functions:
     * 
     * 1)Disables the button
     * 2)Checks if the letter is in the current word
     *  2a)If it is in the word, fills in the letter(s) in the word.
     *      2aa)Checks if the word is complete. If so, call endGame.
     *  2b)If it isn't in the word, calls penaliseScore
     * 
     * @param evt the ActionEvent that comes from the button
     */
    private void genericGameBtnPressed(ActionEvent evt){    //Game: Hangman
        //Get the JButton object that was pressed
        JButton btnPressed = ((JButton)evt.getSource());
        //Disable the JButton
        btnPressed.setEnabled(false);

        //Get the letter in lowercase(our buttons have uppercase letters)
        String btnLetter = btnPressed.getText().toLowerCase();
        //Check if letter is in current word
        if(this.chosenWord.contains(btnLetter)){
            //Replace every blank containing btnLetter
            for(int i = 0; i < chosenWord.length(); i++)
                if(chosenWord.charAt(i) == btnLetter.charAt(0))
                    blankLabel.setText(blankLabel.getText().substring(0,i*2) + btnLetter + blankLabel.getText().substring(i*2+1));
            //Checks for any remaining blanks. If none, game is complete.
            for(int i = 0; i < blankLabel.getText().length(); i++)  
                if(!blankLabel.getText().contains("_"))
                    endGame();
        }
        else
            penaliseScore();     
    }
    
    private void genericGameBtnPressed2(ActionEvent evt){   //Game: Color Buttons
        if(chosenColor == randomColor){
            score += 100;
            txtScore2.setText(String.format("Score: %d", score));
        }
        else{
            score += 0;
        }
        
        randomizeText();
        changeButtonLocation();
        round++;
        
        if(round >= 5){
            endGame2();
        }
    }
    
    /**
     * A function to handle the event for when any of the color buttons are hovered over(mouse in). Changes the
     * icon of the respective button into the hovered version.
     * 
     * @param evt the source of the MouseEvent that calls the mouse in
     * @param color a string (all uppercase) the represents the color of the button
     */
    private void genericGameBtnMouseIn(MouseEvent evt, String color){
        //Get source button
        JButton source = ((JButton)evt.getSource());
        switch(color){
            case "RED":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/red_hovered.png")));
                break;
            case "GREEN":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/green_hovered.png")));
                break;
            case "BLUE":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/blue_hovered.png")));
                break;
            case "YELLOW":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/yellow_hovered.png")));
                break;
            case "PURPLE":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/purple_hovered.png")));
                break;
        }
    }
    
    /**
     * A function to handle the event for when any of the color buttons are however out of (mouse out).
     * Changes the icon of the respective button into the unhovered version.
     * 
     * @param evt the source of the MouseEvent that calls the mouse out
     * @param color a string (all uppercase) that represents the color of the button
     */
    private void genericGameBtnMouseOut(MouseEvent evt, String color){
        //Get source button
        JButton source = ((JButton)evt.getSource());
        switch(color){
            case "RED":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/red_unhovered.png")));
                break;
            case "GREEN":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/green_unhovered.png")));
                break;
            case "BLUE":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/blue_unhovered.png")));
                break;
            case "YELLOW":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/yellow_unhovered.png")));
                break;
            case "PURPLE":
                source.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/purple_unhovered.png")));
                break;
        }
    }
    
    private void randomizeText(){   //Game: Color Buttons
        randomColorText = r.nextInt(5)+1; //from 1-5
        randomColor = r.nextInt(5)+1; //from 1-5
        
        //Color order: colorList = {"RED", "YELLOW", "GREEN", "BLUE", "PURPLE"};
        colorText.setText(colorList[randomColorText-1]);

        switch(randomColor){
            case 1:
                colorText.setForeground(Color.RED);
                break;
            case 2:
                colorText.setForeground(Color.YELLOW);
                break;
            case 3:
                colorText.setForeground(Color.GREEN);
                break;
            case 4:
                colorText.setForeground(Color.BLUE);
                break;
            case 5:
                colorText.setForeground(new Color(128, 0, 128));    //Purple
                break;
        }
    }
    
    private void changeButtonLocation(){   //Game: Color Buttons   
        redButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
        yellowButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
        greenButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
        blueButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
        purpleButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
        
        //check purpleButton overlapping
        for(int tries = 0; tries <= 50; tries++)
            if(purpleButton.getBounds().intersects(blueButton.getBounds()) ||
            purpleButton.getBounds().intersects(greenButton.getBounds()) ||
            purpleButton.getBounds().intersects(yellowButton.getBounds()) ||
            purpleButton.getBounds().intersects(redButton.getBounds()) ||
            purpleButton.getBounds().intersects(colorText.getBounds()) ||
            purpleButton.getBounds().intersects(jPanel3.getBounds()))
                purpleButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
            else
                break;
        
        //check blueButton overlapping
        for(int tries = 0; tries <= 50; tries++)
            if(blueButton.getBounds().intersects(purpleButton.getBounds()) ||
            blueButton.getBounds().intersects(greenButton.getBounds()) ||
            blueButton.getBounds().intersects(yellowButton.getBounds()) ||
            blueButton.getBounds().intersects(redButton.getBounds()) ||
            blueButton.getBounds().intersects(colorText.getBounds()) ||
            blueButton.getBounds().intersects(jPanel3.getBounds()))
                blueButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
            else
                break;
        
        //check greenButton overlapping
        for(int tries = 0; tries <= 50; tries++)
            if(greenButton.getBounds().intersects(purpleButton.getBounds()) ||
            greenButton.getBounds().intersects(blueButton.getBounds()) ||
            greenButton.getBounds().intersects(yellowButton.getBounds()) ||
            greenButton.getBounds().intersects(redButton.getBounds()) ||
            greenButton.getBounds().intersects(colorText.getBounds()) ||
            greenButton.getBounds().intersects(jPanel3.getBounds()))
                greenButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
            else
                break;  
        
        //check yellowButton overlapping
        for(int tries = 0; tries <= 50; tries++)
            if(yellowButton.getBounds().intersects(purpleButton.getBounds()) ||
            yellowButton.getBounds().intersects(blueButton.getBounds()) ||
            yellowButton.getBounds().intersects(greenButton.getBounds()) ||
            yellowButton.getBounds().intersects(redButton.getBounds()) ||
            yellowButton.getBounds().intersects(colorText.getBounds()) ||
            yellowButton.getBounds().intersects(jPanel3.getBounds()))
                yellowButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
            else
                break; 
        
        //check redButton overlapping
        for(int tries = 0; tries <= 50; tries++)
            if(redButton.getBounds().intersects(purpleButton.getBounds()) ||
            redButton.getBounds().intersects(blueButton.getBounds()) ||
            redButton.getBounds().intersects(greenButton.getBounds()) ||
            redButton.getBounds().intersects(yellowButton.getBounds()) ||
            redButton.getBounds().intersects(colorText.getBounds()) ||
            redButton.getBounds().intersects(jPanel3.getBounds()))
                redButton.setBounds(r.nextInt(500), r.nextInt(300), 100, 100);
            else
                break;  
        
        revalidate();
        repaint();
    }
    
    
    private void sudokuSubmit(){        //Game: Sudoku
        int tempScore = score;
        boolean sudokuComplete = true;
        for(int i = 0; i < sudokuBoxes.length; i++){                                        //check all JTextFields
                if(sudokuBoxes[i].getText().equals("")){                                    //check for blanks
                    sudokuError();
                    sudokuComplete = false;
                    break;
                }
                else if(Integer.parseInt(sudokuBoxes[i].getText()) != sudokuSolution[i]){   //compare with solution
                        if(sudokuDeductible[i] == true){                                    //if wrong and unchecked
                                sudokuScore -= 10;                                          //deduct score
                                sudokuDeductible[i] = false;                                //box no longer deductible
                        }
                        sudokuComplete = false;                                             //sudoku unfinished
                }
        }
        if(sudokuComplete == true){
                score += sudokuScore;
                endGame3();
        }
    
        txtScore3.setText(String.format("Sudoku Score: %d", sudokuScore));
        tempScore += sudokuScore;
        txtScore4.setText(String.format("Total Score: %d", tempScore));
    }
    
    private void sudokuError(){
        JLabel sudokuErrorMsg = new JLabel("Sudoku not completed yet");

        JPanel sudokuErrorPanel = new JPanel();
        sudokuErrorPanel.add(sudokuErrorMsg);

        getToolkit().beep();
        JOptionPane.showMessageDialog(null, sudokuErrorPanel);
    }
    
    private void sudokuQuit(){
        sudokuScore = 0;
        endGame3();
    }
    
    /**
     * Checks to see if the user's end score is a new high score. If so, goes to a new 
     * panel where they can input their high score.
     */
    private void updateHighScores(){
        //Keep track of our high score file
        boolean fileEmpty = false;
        //Try-catch for file reading incase I/OException thrown
        try{
            //Construct a buffered reader
            BufferedReader br = new BufferedReader(new FileReader(new File(SAVES_PATH.toURI())));
            //Set the first line of the buffered reader
            String inLine = br.readLine();
            //Reset our userScoreMap
            userScoreMap = new HashMap<>();
            //If our file is empty, update our boolean value
            if(inLine == null){
                //Text file is empty, no scores are inputted
                fileEmpty = true;
            }else{
                //Text file isn't empty
                while(inLine != null){
                    //Read file and update our userscore map as needed
                    String[] strSplit = inLine.split(" ");
                    String user = strSplit[0];
                    Integer score = Integer.parseInt(strSplit[1]);
                    userScoreMap.put(user, score);
                    inLine = br.readLine();
                }
            }
            //Close our buffered reader
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //Move values into an array
        int[] valueArr = new int[userScoreMap.size()];
        Iterator it = userScoreMap.keySet().iterator();
        int valueArrIndex = 0;
        while(it.hasNext()){
            String key = (String)it.next();
            Integer value = userScoreMap.get(key);
            valueArr[valueArrIndex] = value;
            valueArrIndex++;
        }
        //Sort array
        Arrays.sort(valueArr);
        //If our file is empty, tread it as a new high score
        if(fileEmpty){
            //File is empty, so there will definitely be a new high score
            newHSScoreLabel.setText(Integer.toString(score));
            initialEntryTextField.setEnabled(true);
            initialEntryTextField.setVisible(true);
            newHSButtonOk.setEnabled(false);
            PlayPanel3.setVisible(false);
            NewHSPanel.setVisible(true);
            currentPanel = NewHSPanel;
        }else{
            //Our file isn't empty, so use our pre-exisiting new high score logic.
            if(score > valueArr[0] || valueArr.length < 5){
                //Our score is above the lowest high score or there aren't 5 highscores yet
                newHSScoreLabel.setText(Integer.toString(score));
                initialEntryTextField.setEnabled(true);
                initialEntryTextField.setVisible(true);
                newHSButtonOk.setEnabled(false);
                PlayPanel3.setVisible(false);
                NewHSPanel.setVisible(true);
                currentPanel = NewHSPanel;
            }else{
                //Is not a new high score
                EndPanel.setVisible(true);
                currentPanel = EndPanel;
            }
        }   
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        randomTest = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel9 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        StartPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        DisplayPanel = new javax.swing.JPanel();
        PlayButton = new javax.swing.JButton();
        HighscoresButton = new javax.swing.JButton();
        CreditsButton = new javax.swing.JButton();
        DisplayIcon = new javax.swing.JLabel();
        PlayPanel = new javax.swing.JPanel();
        platformImage = new javax.swing.JLabel();
        blankLabel = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        BButton = new javax.swing.JButton();
        EButton = new javax.swing.JButton();
        FButton = new javax.swing.JButton();
        QButton = new javax.swing.JButton();
        ZButton = new javax.swing.JButton();
        PButton = new javax.swing.JButton();
        XButton = new javax.swing.JButton();
        CButton = new javax.swing.JButton();
        LButton = new javax.swing.JButton();
        TButton = new javax.swing.JButton();
        NButton = new javax.swing.JButton();
        KButton = new javax.swing.JButton();
        AButton = new javax.swing.JButton();
        GButton = new javax.swing.JButton();
        MButton = new javax.swing.JButton();
        SButton = new javax.swing.JButton();
        VButton = new javax.swing.JButton();
        JButton = new javax.swing.JButton();
        OButton = new javax.swing.JButton();
        IButton = new javax.swing.JButton();
        WButton = new javax.swing.JButton();
        YButton = new javax.swing.JButton();
        HButton = new javax.swing.JButton();
        RButton = new javax.swing.JButton();
        DButton = new javax.swing.JButton();
        UButton = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        systemTimeText = new javax.swing.JTextField();
        skipButton = new javax.swing.JButton();
        txtScore = new javax.swing.JTextField();
        bodyImage = new javax.swing.JLabel();
        leftArmImage = new javax.swing.JLabel();
        rightArmImage = new javax.swing.JLabel();
        leftLegImage = new javax.swing.JLabel();
        rightLegImage = new javax.swing.JLabel();
        headImage = new javax.swing.JLabel();
        PlayPanel2 = new javax.swing.JPanel();
        colorText = new javax.swing.JLabel();
        redButton = new javax.swing.JButton();
        purpleButton = new javax.swing.JButton();
        blueButton = new javax.swing.JButton();
        greenButton = new javax.swing.JButton();
        yellowButton = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        systemTimeText2 = new javax.swing.JTextField();
        txtScore2 = new javax.swing.JTextField();
        PlayPanel3 = new javax.swing.JPanel();
        sudokuPanel = new javax.swing.JPanel();
        jPanel10 = new javax.swing.JPanel();
        sudokuBox1 = new javax.swing.JLabel();
        sudokuBox2 = new javax.swing.JTextField();
        sudokuBox3 = new javax.swing.JTextField();
        sudokuBox10 = new javax.swing.JTextField();
        sudokuBox11 = new javax.swing.JTextField();
        sudokuBox12 = new javax.swing.JTextField();
        sudokuBox19 = new javax.swing.JTextField();
        sudokuBox20 = new javax.swing.JLabel();
        sudokuBox21 = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        sudokuBox4 = new javax.swing.JLabel();
        sudokuBox5 = new javax.swing.JTextField();
        sudokuBox6 = new javax.swing.JLabel();
        sudokuBox13 = new javax.swing.JTextField();
        sudokuBox14 = new javax.swing.JTextField();
        sudokuBox15 = new javax.swing.JTextField();
        sudokuBox22 = new javax.swing.JTextField();
        sudokuBox23 = new javax.swing.JTextField();
        sudokuBox24 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        sudokuBox7 = new javax.swing.JTextField();
        sudokuBox8 = new javax.swing.JTextField();
        sudokuBox9 = new javax.swing.JLabel();
        sudokuBox16 = new javax.swing.JLabel();
        sudokuBox17 = new javax.swing.JTextField();
        sudokuBox18 = new javax.swing.JTextField();
        sudokuBox25 = new javax.swing.JLabel();
        sudokuBox26 = new javax.swing.JLabel();
        sudokuBox27 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        sudokuBox28 = new javax.swing.JLabel();
        sudokuBox29 = new javax.swing.JTextField();
        sudokuBox30 = new javax.swing.JLabel();
        sudokuBox37 = new javax.swing.JTextField();
        sudokuBox38 = new javax.swing.JTextField();
        sudokuBox39 = new javax.swing.JTextField();
        sudokuBox46 = new javax.swing.JTextField();
        sudokuBox47 = new javax.swing.JLabel();
        sudokuBox48 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        sudokuBox31 = new javax.swing.JTextField();
        sudokuBox32 = new javax.swing.JLabel();
        sudokuBox33 = new javax.swing.JTextField();
        sudokuBox40 = new javax.swing.JTextField();
        sudokuBox41 = new javax.swing.JLabel();
        sudokuBox42 = new javax.swing.JTextField();
        sudokuBox49 = new javax.swing.JTextField();
        sudokuBox50 = new javax.swing.JLabel();
        sudokuBox51 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        sudokuBox34 = new javax.swing.JLabel();
        sudokuBox35 = new javax.swing.JLabel();
        sudokuBox36 = new javax.swing.JTextField();
        sudokuBox43 = new javax.swing.JTextField();
        sudokuBox44 = new javax.swing.JTextField();
        sudokuBox45 = new javax.swing.JTextField();
        sudokuBox52 = new javax.swing.JLabel();
        sudokuBox53 = new javax.swing.JTextField();
        sudokuBox54 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        sudokuBox55 = new javax.swing.JTextField();
        sudokuBox56 = new javax.swing.JLabel();
        sudokuBox57 = new javax.swing.JLabel();
        sudokuBox64 = new javax.swing.JTextField();
        sudokuBox65 = new javax.swing.JTextField();
        sudokuBox66 = new javax.swing.JLabel();
        sudokuBox73 = new javax.swing.JLabel();
        sudokuBox74 = new javax.swing.JTextField();
        sudokuBox75 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        sudokuBox58 = new javax.swing.JTextField();
        sudokuBox59 = new javax.swing.JTextField();
        sudokuBox60 = new javax.swing.JTextField();
        sudokuBox67 = new javax.swing.JTextField();
        sudokuBox68 = new javax.swing.JTextField();
        sudokuBox69 = new javax.swing.JTextField();
        sudokuBox76 = new javax.swing.JLabel();
        sudokuBox77 = new javax.swing.JTextField();
        sudokuBox78 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        sudokuBox61 = new javax.swing.JTextField();
        sudokuBox62 = new javax.swing.JLabel();
        sudokuBox63 = new javax.swing.JTextField();
        sudokuBox70 = new javax.swing.JTextField();
        sudokuBox71 = new javax.swing.JTextField();
        sudokuBox72 = new javax.swing.JTextField();
        sudokuBox79 = new javax.swing.JTextField();
        sudokuBox80 = new javax.swing.JTextField();
        sudokuBox81 = new javax.swing.JLabel();
        sudokuSubmitButton = new javax.swing.JButton();
        sudokuQuitButton = new javax.swing.JButton();
        jPanel5 = new javax.swing.JPanel();
        systemTimeText3 = new javax.swing.JTextField();
        txtScore3 = new javax.swing.JTextField();
        txtScore4 = new javax.swing.JTextField();
        HighscorePanel = new javax.swing.JPanel();
        HSLabel = new javax.swing.JLabel();
        HSBackButton = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        ScoresLabel = new javax.swing.JLabel();
        CreditsPanel = new javax.swing.JPanel();
        CreditsBackButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        EndPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        txtEndScore = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        NewHSPanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        newHSScoreLabel = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        initialEntryTextField = new javax.swing.JTextField();
        newHSButtonOk = new javax.swing.JButton();
        F1Panel = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        F1BackButton = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();

        randomTest.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        randomTest.setText("Random word test");
        randomTest.setEnabled(false);
        randomTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomTestActionPerformed(evt);
            }
        });

        jButton8.setText("jButton8");

        jButton7.setText("jButton7");

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane2.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        StartPanel.setMaximumSize(new java.awt.Dimension(600, 400));
        StartPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Point and Click Game");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("By:  Team SwingSharp");

        javax.swing.GroupLayout StartPanelLayout = new javax.swing.GroupLayout(StartPanel);
        StartPanel.setLayout(StartPanelLayout);
        StartPanelLayout.setHorizontalGroup(
            StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StartPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        StartPanelLayout.setVerticalGroup(
            StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StartPanelLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(jLabel2)
                .addContainerGap(98, Short.MAX_VALUE))
        );

        DisplayPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        PlayButton.setText("Play");
        PlayButton.setToolTipText("Click to start game");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        HighscoresButton.setText("Highscores");
        HighscoresButton.setToolTipText("Click to view highscores");
        HighscoresButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HighscoresButtonActionPerformed(evt);
            }
        });

        CreditsButton.setText("Credits");
        CreditsButton.setToolTipText("Click to view credits");
        CreditsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreditsButtonActionPerformed(evt);
            }
        });

        DisplayIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DisplayIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon.jpg"))); // NOI18N
        DisplayIcon.setToolTipText("SwingSharp's logo");
        DisplayIcon.setMaximumSize(new java.awt.Dimension(190, 184));
        DisplayIcon.setMinimumSize(new java.awt.Dimension(190, 184));
        DisplayIcon.setPreferredSize(new java.awt.Dimension(190, 184));

        javax.swing.GroupLayout DisplayPanelLayout = new javax.swing.GroupLayout(DisplayPanel);
        DisplayPanel.setLayout(DisplayPanelLayout);
        DisplayPanelLayout.setHorizontalGroup(
            DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DisplayPanelLayout.createSequentialGroup()
                .addContainerGap(206, Short.MAX_VALUE)
                .addGroup(DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(PlayButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(HighscoresButton)
                    .addComponent(CreditsButton, javax.swing.GroupLayout.PREFERRED_SIZE, 84, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(DisplayIcon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(204, Short.MAX_VALUE))
        );
        DisplayPanelLayout.setVerticalGroup(
            DisplayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, DisplayPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(DisplayIcon, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(36, 36, 36)
                .addComponent(PlayButton)
                .addGap(18, 18, 18)
                .addComponent(HighscoresButton)
                .addGap(18, 18, 18)
                .addComponent(CreditsButton)
                .addGap(63, 63, 63))
        );

        platformImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/Hangman_Base.png"))); // NOI18N

        blankLabel.setFont(new java.awt.Font("Calibri", 0, 36)); // NOI18N
        blankLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        blankLabel.setText("_ _ _ _ _ _");

        BButton.setText("B");
        BButton.setToolTipText("Choose B as your guess");
        BButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BButtonActionPerformed(evt);
            }
        });

        EButton.setText("E");
        EButton.setToolTipText("Choose E as your guess");
        EButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EButtonActionPerformed(evt);
            }
        });

        FButton.setText("F");
        FButton.setToolTipText("Choose F as your guess");
        FButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FButtonActionPerformed(evt);
            }
        });

        QButton.setText("Q");
        QButton.setToolTipText("Choose Q as your guess");
        QButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QButtonActionPerformed(evt);
            }
        });

        ZButton.setText("Z");
        ZButton.setToolTipText("Choose Z as your guess");
        ZButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZButtonActionPerformed(evt);
            }
        });

        PButton.setText("P");
        PButton.setToolTipText("Choose P as your guess");
        PButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PButtonActionPerformed(evt);
            }
        });

        XButton.setText("X");
        XButton.setToolTipText("Choose X as your guess");
        XButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XButtonActionPerformed(evt);
            }
        });

        CButton.setText("C");
        CButton.setToolTipText("Choose C as your guess");
        CButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CButtonActionPerformed(evt);
            }
        });

        LButton.setText("L");
        LButton.setToolTipText("Choose L as your guess");
        LButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LButtonActionPerformed(evt);
            }
        });

        TButton.setText("T");
        TButton.setToolTipText("Choose T as your guess");
        TButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TButtonActionPerformed(evt);
            }
        });

        NButton.setText("N");
        NButton.setToolTipText("Choose N as your guess");
        NButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NButtonActionPerformed(evt);
            }
        });

        KButton.setText("K");
        KButton.setToolTipText("Choose K as your guess");
        KButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KButtonActionPerformed(evt);
            }
        });

        AButton.setText("A");
        AButton.setToolTipText("Choose A as your guess");
        AButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AButtonActionPerformed(evt);
            }
        });

        GButton.setText("G");
        GButton.setToolTipText("Choose G as your guess");
        GButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GButtonActionPerformed(evt);
            }
        });

        MButton.setText("M");
        MButton.setToolTipText("Choose M as your guess");
        MButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MButtonActionPerformed(evt);
            }
        });

        SButton.setText("S");
        SButton.setToolTipText("Choose S as your guess");
        SButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SButtonActionPerformed(evt);
            }
        });

        VButton.setText("V");
        VButton.setToolTipText("Choose V as your guess");
        VButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VButtonActionPerformed(evt);
            }
        });

        JButton.setText("J");
        JButton.setToolTipText("Choose J as your guess");
        JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonActionPerformed(evt);
            }
        });

        OButton.setText("O");
        OButton.setToolTipText("Choose O as your guess");
        OButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OButtonActionPerformed(evt);
            }
        });

        IButton.setText("I");
        IButton.setToolTipText("Choose I as your guess");
        IButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IButtonActionPerformed(evt);
            }
        });

        WButton.setText("W");
        WButton.setToolTipText("Choose W as your guess");
        WButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WButtonActionPerformed(evt);
            }
        });

        YButton.setText("Y");
        YButton.setToolTipText("Choose Y as your guess");
        YButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YButtonActionPerformed(evt);
            }
        });

        HButton.setText("H");
        HButton.setToolTipText("Choose H as your guess");
        HButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HButtonActionPerformed(evt);
            }
        });

        RButton.setText("R");
        RButton.setToolTipText("Choose R as your guess");
        RButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RButtonActionPerformed(evt);
            }
        });

        DButton.setText("D");
        DButton.setToolTipText("Choose D as your guess");
        DButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DButtonActionPerformed(evt);
            }
        });

        UButton.setText("U");
        UButton.setToolTipText("Choose U as your guess");
        UButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                UButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(NButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(OButton, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(QButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(RButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(SButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(TButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(UButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(VButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(WButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(XButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(YButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ZButton))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(AButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(BButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(CButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(DButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(FButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(GButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(HButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(JButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(KButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MButton)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(AButton)
                    .addComponent(BButton)
                    .addComponent(CButton)
                    .addComponent(DButton)
                    .addComponent(EButton)
                    .addComponent(FButton)
                    .addComponent(GButton)
                    .addComponent(HButton)
                    .addComponent(IButton)
                    .addComponent(JButton)
                    .addComponent(KButton)
                    .addComponent(LButton)
                    .addComponent(MButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NButton)
                    .addComponent(OButton)
                    .addComponent(PButton)
                    .addComponent(QButton)
                    .addComponent(RButton)
                    .addComponent(SButton)
                    .addComponent(TButton)
                    .addComponent(UButton)
                    .addComponent(VButton)
                    .addComponent(WButton)
                    .addComponent(XButton)
                    .addComponent(YButton)
                    .addComponent(ZButton))
                .addContainerGap())
        );

        systemTimeText.setEditable(false);
        systemTimeText.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        systemTimeText.setText("Feb 8, 2021, | 13:28:07");
        systemTimeText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemTimeTextActionPerformed(evt);
            }
        });

        skipButton.setText("Skip");
        skipButton.setToolTipText("Click Skip to move on to next game");
        skipButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                skipButtonActionPerformed(evt);
            }
        });

        txtScore.setEditable(false);
        txtScore.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtScore.setText("Score:");
        txtScore.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtScoreActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtScore)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel2Layout.createSequentialGroup()
                        .addGap(92, 92, 92)
                        .addComponent(skipButton))
                    .addComponent(systemTimeText, javax.swing.GroupLayout.Alignment.LEADING))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(systemTimeText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtScore, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(skipButton)
                .addContainerGap())
        );

        bodyImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/body.PNG"))); // NOI18N

        leftArmImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/left_arm.PNG"))); // NOI18N

        rightArmImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/right_arm.PNG"))); // NOI18N

        leftLegImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/left_leg.PNG"))); // NOI18N

        rightLegImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/right_leg.PNG"))); // NOI18N

        headImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/face.PNG"))); // NOI18N

        javax.swing.GroupLayout PlayPanelLayout = new javax.swing.GroupLayout(PlayPanel);
        PlayPanel.setLayout(PlayPanelLayout);
        PlayPanelLayout.setHorizontalGroup(
            PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayPanelLayout.createSequentialGroup()
                .addGap(150, 150, 150)
                .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayPanelLayout.createSequentialGroup()
                        .addGap(180, 180, 180)
                        .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(rightArmImage, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(leftArmImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(leftLegImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(rightLegImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(headImage))
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(bodyImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(26, 26, 26)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(platformImage)))
            .addGroup(PlayPanelLayout.createSequentialGroup()
                .addGap(196, 196, 196)
                .addComponent(blankLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        );
        PlayPanelLayout.setVerticalGroup(
            PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(PlayPanelLayout.createSequentialGroup()
                .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(PlayPanelLayout.createSequentialGroup()
                        .addGap(100, 100, 100)
                        .addGroup(PlayPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(30, 30, 30)
                                .addComponent(rightArmImage, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(leftArmImage))
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(leftLegImage))
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(50, 50, 50)
                                .addComponent(rightLegImage))
                            .addComponent(headImage)
                            .addGroup(PlayPanelLayout.createSequentialGroup()
                                .addGap(20, 20, 20)
                                .addComponent(bodyImage, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(PlayPanelLayout.createSequentialGroup()
                        .addGap(70, 70, 70)
                        .addComponent(platformImage, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addComponent(blankLabel)
                .addGap(11, 11, 11)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PlayPanel2.setPreferredSize(new java.awt.Dimension(600, 400));
        PlayPanel2.setLayout(null);

        colorText.setFont(new java.awt.Font("Courier New", 1, 24)); // NOI18N
        colorText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colorText.setText("Color");
        PlayPanel2.add(colorText);
        colorText.setBounds(240, 40, 110, 28);

        redButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/red_unhovered.png"))); // NOI18N
        redButton.setToolTipText("Red Button");
        redButton.setBorderPainted(false);
        redButton.setContentAreaFilled(false);
        redButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                redButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                redButtonMouseExited(evt);
            }
        });
        redButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                redButtonActionPerformed(evt);
            }
        });
        PlayPanel2.add(redButton);
        redButton.setBounds(40, 60, 100, 100);

        purpleButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/purple_unhovered.png"))); // NOI18N
        purpleButton.setToolTipText("Purple Button");
        purpleButton.setBorderPainted(false);
        purpleButton.setContentAreaFilled(false);
        purpleButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                purpleButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                purpleButtonMouseExited(evt);
            }
        });
        purpleButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                purpleButtonActionPerformed(evt);
            }
        });
        PlayPanel2.add(purpleButton);
        purpleButton.setBounds(440, 70, 100, 100);

        blueButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/blue_unhovered.png"))); // NOI18N
        blueButton.setBorderPainted(false);
        blueButton.setContentAreaFilled(false);
        blueButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                blueButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                blueButtonMouseExited(evt);
            }
        });
        blueButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                blueButtonActionPerformed(evt);
            }
        });
        PlayPanel2.add(blueButton);
        blueButton.setBounds(440, 230, 100, 100);

        greenButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/green_unhovered.png"))); // NOI18N
        greenButton.setBorderPainted(false);
        greenButton.setContentAreaFilled(false);
        greenButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                greenButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                greenButtonMouseExited(evt);
            }
        });
        greenButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                greenButtonActionPerformed(evt);
            }
        });
        PlayPanel2.add(greenButton);
        greenButton.setBounds(240, 230, 100, 100);

        yellowButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/buttons/yellow_unhovered.png"))); // NOI18N
        yellowButton.setToolTipText("Yellow Button");
        yellowButton.setBorderPainted(false);
        yellowButton.setContentAreaFilled(false);
        yellowButton.setMaximumSize(new java.awt.Dimension(100, 100));
        yellowButton.setMinimumSize(new java.awt.Dimension(100, 100));
        yellowButton.setPreferredSize(new java.awt.Dimension(100, 100));
        yellowButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                yellowButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                yellowButtonMouseExited(evt);
            }
        });
        yellowButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                yellowButtonActionPerformed(evt);
            }
        });
        PlayPanel2.add(yellowButton);
        yellowButton.setBounds(50, 230, 100, 100);

        systemTimeText2.setEditable(false);
        systemTimeText2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        systemTimeText2.setText("Feb 8, 2021, | 13:28:07");
        systemTimeText2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemTimeText2ActionPerformed(evt);
            }
        });

        txtScore2.setEditable(false);
        txtScore2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtScore2.setText("Score:");
        txtScore2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtScore2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtScore2)
                    .addComponent(systemTimeText2, javax.swing.GroupLayout.DEFAULT_SIZE, 143, Short.MAX_VALUE))
                .addGap(0, 7, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(systemTimeText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtScore2)
                .addContainerGap())
        );

        PlayPanel2.add(jPanel3);
        jPanel3.setBounds(450, 0, 150, 60);

        PlayPanel3.setPreferredSize(new java.awt.Dimension(600, 400));
        PlayPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        sudokuPanel.setPreferredSize(new java.awt.Dimension(325, 325));
        sudokuPanel.setLayout(new java.awt.GridLayout(3, 3));

        jPanel10.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel10.setLayout(new java.awt.GridLayout(3, 9));

        sudokuBox1.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox1.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox1.setText("8");
        sudokuBox1.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox1.setOpaque(true);
        jPanel10.add(sudokuBox1);

        sudokuBox2.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox2.setToolTipText(sudokuBlankToolTip);
        sudokuBox2.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox2ActionPerformed(evt);
            }
        });
        sudokuBox2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                sudokuBox2KeyTyped(evt);
            }
        });
        jPanel10.add(sudokuBox2);

        sudokuBox3.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox3.setToolTipText(sudokuBlankToolTip);
        sudokuBox3.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel10.add(sudokuBox3);

        sudokuBox10.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox10.setToolTipText(sudokuBlankToolTip);
        sudokuBox10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox10ActionPerformed(evt);
            }
        });
        jPanel10.add(sudokuBox10);

        sudokuBox11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox11.setToolTipText(sudokuBlankToolTip);
        sudokuBox11.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel10.add(sudokuBox11);

        sudokuBox12.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox12.setToolTipText(sudokuBlankToolTip);
        sudokuBox12.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel10.add(sudokuBox12);

        sudokuBox19.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox19.setToolTipText(sudokuBlankToolTip);
        sudokuBox19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox19ActionPerformed(evt);
            }
        });
        jPanel10.add(sudokuBox19);

        sudokuBox20.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox20.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox20.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox20.setText("1");
        sudokuBox20.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox20.setOpaque(true);
        jPanel10.add(sudokuBox20);

        sudokuBox21.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox21.setToolTipText(sudokuBlankToolTip);
        sudokuBox21.setAlignmentX(0.0F);
        sudokuBox21.setAlignmentY(0.0F);
        sudokuBox21.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox21.setMinimumSize(new java.awt.Dimension(26, 26));
        sudokuBox21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox21ActionPerformed(evt);
            }
        });
        jPanel10.add(sudokuBox21);

        sudokuPanel.add(jPanel10);

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel4.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox4.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox4.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox4.setText("4");
        sudokuBox4.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        sudokuBox4.setOpaque(true);
        jPanel4.add(sudokuBox4);

        sudokuBox5.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox5.setToolTipText(sudokuBlankToolTip);
        sudokuBox5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox5ActionPerformed(evt);
            }
        });
        jPanel4.add(sudokuBox5);

        sudokuBox6.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox6.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox6.setText("6");
        sudokuBox6.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox6.setOpaque(true);
        jPanel4.add(sudokuBox6);

        sudokuBox13.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox13.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox13.setToolTipText(sudokuBlankToolTip);
        sudokuBox13.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel4.add(sudokuBox13);

        sudokuBox14.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox14.setToolTipText(sudokuBlankToolTip);
        sudokuBox14.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox14ActionPerformed(evt);
            }
        });
        jPanel4.add(sudokuBox14);

        sudokuBox15.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox15.setToolTipText(sudokuBlankToolTip);
        sudokuBox15.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel4.add(sudokuBox15);

        sudokuBox22.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox22.setToolTipText(sudokuBlankToolTip);
        sudokuBox22.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox22ActionPerformed(evt);
            }
        });
        jPanel4.add(sudokuBox22);

        sudokuBox23.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox23.setToolTipText(sudokuBlankToolTip);
        sudokuBox23.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel4.add(sudokuBox23);

        sudokuBox24.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox24.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox24.setToolTipText(sudokuBlankToolTip);
        sudokuBox24.setAlignmentX(0.0F);
        sudokuBox24.setAlignmentY(0.0F);
        sudokuBox24.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox24.setMinimumSize(new java.awt.Dimension(26, 26));
        sudokuBox24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox24ActionPerformed(evt);
            }
        });
        jPanel4.add(sudokuBox24);

        sudokuPanel.add(jPanel4);

        jPanel7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel7.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox7.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox7.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox7.setToolTipText(sudokuBlankToolTip);
        sudokuBox7.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox7ActionPerformed(evt);
            }
        });
        jPanel7.add(sudokuBox7);

        sudokuBox8.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox8.setToolTipText(sudokuBlankToolTip);
        sudokuBox8.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox8ActionPerformed(evt);
            }
        });
        jPanel7.add(sudokuBox8);

        sudokuBox9.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox9.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox9.setText("7");
        sudokuBox9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox9.setOpaque(true);
        jPanel7.add(sudokuBox9);

        sudokuBox16.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox16.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox16.setText("4");
        sudokuBox16.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox16.setOpaque(true);
        jPanel7.add(sudokuBox16);

        sudokuBox17.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox17.setToolTipText(sudokuBlankToolTip);
        sudokuBox17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox17ActionPerformed(evt);
            }
        });
        jPanel7.add(sudokuBox17);

        sudokuBox18.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox18.setToolTipText(sudokuBlankToolTip);
        sudokuBox18.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel7.add(sudokuBox18);

        sudokuBox25.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox25.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox25.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox25.setText("6");
        sudokuBox25.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox25.setOpaque(true);
        jPanel7.add(sudokuBox25);

        sudokuBox26.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox26.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox26.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox26.setText("5");
        sudokuBox26.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox26.setOpaque(true);
        jPanel7.add(sudokuBox26);

        sudokuBox27.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox27.setToolTipText(sudokuBlankToolTip);
        sudokuBox27.setAlignmentX(0.0F);
        sudokuBox27.setAlignmentY(0.0F);
        sudokuBox27.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox27.setMinimumSize(new java.awt.Dimension(26, 26));
        sudokuBox27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox27ActionPerformed(evt);
            }
        });
        jPanel7.add(sudokuBox27);

        sudokuPanel.add(jPanel7);

        jPanel8.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel8.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox28.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox28.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox28.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox28.setText("5");
        sudokuBox28.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox28.setOpaque(true);
        jPanel8.add(sudokuBox28);

        sudokuBox29.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox29.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox29.setToolTipText(sudokuBlankToolTip);
        sudokuBox29.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox29ActionPerformed(evt);
            }
        });
        jPanel8.add(sudokuBox29);

        sudokuBox30.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox30.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox30.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox30.setText("9");
        sudokuBox30.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox30.setOpaque(true);
        jPanel8.add(sudokuBox30);

        sudokuBox37.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox37.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox37.setToolTipText(sudokuBlankToolTip);
        sudokuBox37.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel8.add(sudokuBox37);

        sudokuBox38.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox38.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox38.setToolTipText(sudokuBlankToolTip);
        sudokuBox38.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel8.add(sudokuBox38);

        sudokuBox39.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox39.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox39.setToolTipText(sudokuBlankToolTip);
        sudokuBox39.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel8.add(sudokuBox39);

        sudokuBox46.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox46.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox46.setToolTipText(sudokuBlankToolTip);
        sudokuBox46.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox46ActionPerformed(evt);
            }
        });
        jPanel8.add(sudokuBox46);

        sudokuBox47.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox47.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox47.setText("4");
        sudokuBox47.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox47.setOpaque(true);
        jPanel8.add(sudokuBox47);

        sudokuBox48.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox48.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox48.setText("8");
        sudokuBox48.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox48.setOpaque(true);
        jPanel8.add(sudokuBox48);

        sudokuPanel.add(jPanel8);

        jPanel14.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel14.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox31.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox31.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox31.setToolTipText(sudokuBlankToolTip);
        sudokuBox31.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox31ActionPerformed(evt);
            }
        });
        jPanel14.add(sudokuBox31);

        sudokuBox32.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox32.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox32.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox32.setText("3");
        sudokuBox32.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox32.setOpaque(true);
        jPanel14.add(sudokuBox32);

        sudokuBox33.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox33.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox33.setToolTipText(sudokuBlankToolTip);
        sudokuBox33.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel14.add(sudokuBox33);

        sudokuBox40.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox40.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox40.setToolTipText(sudokuBlankToolTip);
        sudokuBox40.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox40ActionPerformed(evt);
            }
        });
        jPanel14.add(sudokuBox40);

        sudokuBox41.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox41.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox41.setText("7");
        sudokuBox41.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox41.setOpaque(true);
        jPanel14.add(sudokuBox41);

        sudokuBox42.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox42.setToolTipText(sudokuBlankToolTip);
        sudokuBox42.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel14.add(sudokuBox42);

        sudokuBox49.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox49.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox49.setToolTipText(sudokuBlankToolTip);
        sudokuBox49.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox49.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox49ActionPerformed(evt);
            }
        });
        jPanel14.add(sudokuBox49);

        sudokuBox50.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox50.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox50.setText("2");
        sudokuBox50.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox50.setOpaque(true);
        jPanel14.add(sudokuBox50);

        sudokuBox51.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox51.setToolTipText(sudokuBlankToolTip);
        sudokuBox51.setAlignmentX(0.0F);
        sudokuBox51.setAlignmentY(0.0F);
        sudokuBox51.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox51.setMinimumSize(new java.awt.Dimension(26, 26));
        sudokuBox51.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox51ActionPerformed(evt);
            }
        });
        jPanel14.add(sudokuBox51);

        sudokuPanel.add(jPanel14);

        jPanel11.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel11.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox34.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox34.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox34.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox34.setText("7");
        sudokuBox34.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox34.setOpaque(true);
        jPanel11.add(sudokuBox34);

        sudokuBox35.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox35.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox35.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox35.setText("8");
        sudokuBox35.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox35.setOpaque(true);
        jPanel11.add(sudokuBox35);

        sudokuBox36.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox36.setToolTipText(sudokuBlankToolTip);
        sudokuBox36.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(sudokuBox36);

        sudokuBox43.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox43.setToolTipText(sudokuBlankToolTip);
        sudokuBox43.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(sudokuBox43);

        sudokuBox44.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox44.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox44.setToolTipText(sudokuBlankToolTip);
        sudokuBox44.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel11.add(sudokuBox44);

        sudokuBox45.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox45.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox45.setToolTipText(sudokuBlankToolTip);
        sudokuBox45.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(sudokuBox45);

        sudokuBox52.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox52.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox52.setText("1");
        sudokuBox52.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox52.setOpaque(true);
        jPanel11.add(sudokuBox52);

        sudokuBox53.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox53.setToolTipText(sudokuBlankToolTip);
        sudokuBox53.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel11.add(sudokuBox53);

        sudokuBox54.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox54.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox54.setText("3");
        sudokuBox54.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox54.setOpaque(true);
        jPanel11.add(sudokuBox54);

        sudokuPanel.add(jPanel11);

        jPanel13.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel13.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox55.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox55.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox55.setToolTipText(sudokuBlankToolTip);
        sudokuBox55.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox55.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox55ActionPerformed(evt);
            }
        });
        jPanel13.add(sudokuBox55);

        sudokuBox56.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox56.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox56.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox56.setText("5");
        sudokuBox56.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox56.setOpaque(true);
        jPanel13.add(sudokuBox56);

        sudokuBox57.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox57.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox57.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox57.setText("2");
        sudokuBox57.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox57.setOpaque(true);
        jPanel13.add(sudokuBox57);

        sudokuBox64.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox64.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox64.setToolTipText(sudokuBlankToolTip);
        sudokuBox64.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel13.add(sudokuBox64);

        sudokuBox65.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox65.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox65.setToolTipText(sudokuBlankToolTip);
        sudokuBox65.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel13.add(sudokuBox65);

        sudokuBox66.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox66.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox66.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox66.setText("1");
        sudokuBox66.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox66.setOpaque(true);
        jPanel13.add(sudokuBox66);

        sudokuBox73.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox73.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox73.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox73.setText("3");
        sudokuBox73.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox73.setOpaque(true);
        jPanel13.add(sudokuBox73);

        sudokuBox74.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox74.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox74.setToolTipText(sudokuBlankToolTip);
        sudokuBox74.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel13.add(sudokuBox74);

        sudokuBox75.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox75.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox75.setToolTipText(sudokuBlankToolTip);
        sudokuBox75.setAlignmentX(0.0F);
        sudokuBox75.setAlignmentY(0.0F);
        sudokuBox75.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox75.setMinimumSize(new java.awt.Dimension(26, 26));
        sudokuBox75.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox75ActionPerformed(evt);
            }
        });
        jPanel13.add(sudokuBox75);

        sudokuPanel.add(jPanel13);

        jPanel12.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel12.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox58.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox58.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox58.setToolTipText(sudokuBlankToolTip);
        sudokuBox58.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox58ActionPerformed(evt);
            }
        });
        jPanel12.add(sudokuBox58);

        sudokuBox59.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox59.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox59.setToolTipText(sudokuBlankToolTip);
        sudokuBox59.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox59.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox59ActionPerformed(evt);
            }
        });
        jPanel12.add(sudokuBox59);

        sudokuBox60.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox60.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox60.setToolTipText(sudokuBlankToolTip);
        sudokuBox60.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(sudokuBox60);

        sudokuBox67.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox67.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox67.setToolTipText(sudokuBlankToolTip);
        sudokuBox67.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(sudokuBox67);

        sudokuBox68.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox68.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox68.setToolTipText(sudokuBlankToolTip);
        sudokuBox68.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel12.add(sudokuBox68);

        sudokuBox69.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox69.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox69.setToolTipText(sudokuBlankToolTip);
        sudokuBox69.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(sudokuBox69);

        sudokuBox76.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox76.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox76.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox76.setText("9");
        sudokuBox76.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox76.setOpaque(true);
        jPanel12.add(sudokuBox76);

        sudokuBox77.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox77.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox77.setToolTipText(sudokuBlankToolTip);
        sudokuBox77.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel12.add(sudokuBox77);

        sudokuBox78.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox78.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox78.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox78.setText("2");
        sudokuBox78.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox78.setOpaque(true);
        jPanel12.add(sudokuBox78);

        sudokuPanel.add(jPanel12);

        jPanel15.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jPanel15.setLayout(new java.awt.GridLayout(3, 3));

        sudokuBox61.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox61.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox61.setToolTipText(sudokuBlankToolTip);
        sudokuBox61.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox61.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox61ActionPerformed(evt);
            }
        });
        jPanel15.add(sudokuBox61);

        sudokuBox62.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox62.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox62.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox62.setText("9");
        sudokuBox62.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        sudokuBox62.setOpaque(true);
        jPanel15.add(sudokuBox62);

        sudokuBox63.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox63.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox63.setToolTipText(sudokuBlankToolTip);
        sudokuBox63.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(sudokuBox63);

        sudokuBox70.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox70.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox70.setToolTipText(sudokuBlankToolTip);
        sudokuBox70.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(sudokuBox70);

        sudokuBox71.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox71.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox71.setToolTipText(sudokuBlankToolTip);
        sudokuBox71.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel15.add(sudokuBox71);

        sudokuBox72.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox72.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox72.setToolTipText(sudokuBlankToolTip);
        sudokuBox72.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(sudokuBox72);

        sudokuBox79.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox79.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox79.setToolTipText(sudokuBlankToolTip);
        sudokuBox79.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox79.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuBox79ActionPerformed(evt);
            }
        });
        jPanel15.add(sudokuBox79);

        sudokuBox80.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        sudokuBox80.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        sudokuBox80.setToolTipText(sudokuBlankToolTip);
        sudokuBox80.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel15.add(sudokuBox80);

        sudokuBox81.setBackground(new java.awt.Color(255, 255, 255));
        sudokuBox81.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        sudokuBox81.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        sudokuBox81.setText("5");
        sudokuBox81.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        sudokuBox81.setOpaque(true);
        jPanel15.add(sudokuBox81);

        sudokuPanel.add(jPanel15);

        PlayPanel3.add(sudokuPanel, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 42, -1, -1));

        sudokuSubmitButton.setText("Submit");
        sudokuSubmitButton.setToolTipText("Click Submit when Sudoku is complete ");
        sudokuSubmitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuSubmitButtonActionPerformed(evt);
            }
        });
        PlayPanel3.add(sudokuSubmitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(21, 356, -1, -1));

        sudokuQuitButton.setText("Quit");
        sudokuQuitButton.setToolTipText("Click Quit to exit Sudoku game");
        sudokuQuitButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sudokuQuitButtonActionPerformed(evt);
            }
        });
        PlayPanel3.add(sudokuQuitButton, new org.netbeans.lib.awtextra.AbsoluteConstraints(511, 352, -1, -1));

        systemTimeText3.setEditable(false);
        systemTimeText3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        systemTimeText3.setText("Feb 8, 2021, | 13:28:07");
        systemTimeText3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemTimeText3ActionPerformed(evt);
            }
        });

        txtScore3.setEditable(false);
        txtScore3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtScore3.setText("Sudoku Score: 540");
        txtScore3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtScore3ActionPerformed(evt);
            }
        });

        txtScore4.setEditable(false);
        txtScore4.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtScore4.setText("Total Score: ");
        txtScore4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtScore4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(txtScore3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(systemTimeText3, javax.swing.GroupLayout.Alignment.TRAILING)
            .addComponent(txtScore4)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(systemTimeText3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtScore3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtScore4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        PlayPanel3.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(470, 0, 130, -1));

        HSLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        HSLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HSLabel.setText("Highscores");

        HSBackButton.setText("Back");
        HSBackButton.setToolTipText("Click to go back to display page");
        HSBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HSBackButtonActionPerformed(evt);
            }
        });

        jScrollPane1.setBorder(null);

        ScoresLabel.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        ScoresLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ScoresLabel.setText("<html>ABC.........00000<br/>ABC.........00000<br/>ABC.........00000<br/>ABC.........00000<br/>ABC.........00000</html>");
        ScoresLabel.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jScrollPane1.setViewportView(ScoresLabel);

        javax.swing.GroupLayout HighscorePanelLayout = new javax.swing.GroupLayout(HighscorePanel);
        HighscorePanel.setLayout(HighscorePanelLayout);
        HighscorePanelLayout.setHorizontalGroup(
            HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HighscorePanelLayout.createSequentialGroup()
                .addGroup(HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(HighscorePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(HSBackButton))
                    .addGroup(HighscorePanelLayout.createSequentialGroup()
                        .addGap(224, 224, 224)
                        .addGroup(HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(HSLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 149, Short.MAX_VALUE)
                            .addComponent(jScrollPane1))))
                .addContainerGap(227, Short.MAX_VALUE))
        );
        HighscorePanelLayout.setVerticalGroup(
            HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HighscorePanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(HSLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 194, Short.MAX_VALUE)
                .addGap(21, 21, 21)
                .addComponent(HSBackButton)
                .addContainerGap())
        );

        CreditsBackButton.setText("Back");
        CreditsBackButton.setToolTipText("Click to go back to display page");
        CreditsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreditsBackButtonActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel3.setText("Credits");
        jLabel3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel4.setText("Robert Berger Engle, Bronco #014011517");

        jLabel5.setText("Cynthia Luong, Bronco #011673490");

        jLabel6.setText("Kenneth Shuto, Bronco #012585989");

        jLabel7.setText("Rida Siddiqui, Bronco #014147900");

        javax.swing.GroupLayout CreditsPanelLayout = new javax.swing.GroupLayout(CreditsPanel);
        CreditsPanel.setLayout(CreditsPanelLayout);
        CreditsPanelLayout.setHorizontalGroup(
            CreditsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreditsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(CreditsBackButton)
                .addContainerGap(526, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CreditsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(CreditsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addGap(191, 191, 191))
        );
        CreditsPanelLayout.setVerticalGroup(
            CreditsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreditsPanelLayout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addGap(55, 55, 55)
                .addComponent(jLabel4)
                .addGap(31, 31, 31)
                .addComponent(jLabel5)
                .addGap(33, 33, 33)
                .addComponent(jLabel6)
                .addGap(28, 28, 28)
                .addComponent(jLabel7)
                .addGap(62, 62, 62)
                .addComponent(CreditsBackButton)
                .addContainerGap())
        );

        EndPanel.setMinimumSize(new java.awt.Dimension(600, 400));

        jTextField1.setEditable(false);
        jTextField1.setFont(new java.awt.Font("Courier New", 1, 36)); // NOI18N
        jTextField1.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField1.setText("Game Over!");
        jTextField1.setBorder(null);

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Courier New", 2, 14)); // NOI18N
        jTextField2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField2.setText("Please play again!");
        jTextField2.setBorder(null);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });

        txtEndScore.setEditable(false);
        txtEndScore.setFont(new java.awt.Font("Courier New", 1, 18)); // NOI18N
        txtEndScore.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txtEndScore.setBorder(null);

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Courier New", 2, 14)); // NOI18N
        jTextField3.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField3.setText("Your Final Score:");
        jTextField3.setBorder(null);

        jButton1.setText("End");
        jButton1.setToolTipText("Click to end game");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EndBtnActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EndPanelLayout = new javax.swing.GroupLayout(EndPanel);
        EndPanel.setLayout(EndPanelLayout);
        EndPanelLayout.setHorizontalGroup(
            EndPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EndPanelLayout.createSequentialGroup()
                .addGap(178, 178, 178)
                .addGroup(EndPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(EndPanelLayout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 164, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(EndPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtEndScore)
                        .addComponent(jTextField2)
                        .addComponent(jTextField1)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 233, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(189, Short.MAX_VALUE))
        );
        EndPanelLayout.setVerticalGroup(
            EndPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EndPanelLayout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(txtEndScore, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addContainerGap(91, Short.MAX_VALUE))
        );

        NewHSPanel.setMaximumSize(new java.awt.Dimension(600, 400));
        NewHSPanel.setMinimumSize(new java.awt.Dimension(600, 400));

        jLabel8.setFont(new java.awt.Font("sansserif", 1, 24)); // NOI18N
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("New High Score!");

        jLabel9.setFont(new java.awt.Font("Courier New", 1, 12)); // NOI18N
        jLabel9.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel9.setText("Your Score:");

        newHSScoreLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);

        jLabel10.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Please Enter Three Initials Below:");

        initialEntryTextField.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        initialEntryTextField.setToolTipText("Enter initials here");
        initialEntryTextField.setEnabled(false);
        initialEntryTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                initialEntryTextFieldActionPerformed(evt);
            }
        });
        initialEntryTextField.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                initialEntryTextFieldKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                initialEntryTextFieldKeyReleased(evt);
            }
        });

        newHSButtonOk.setText("Ok");
        newHSButtonOk.setToolTipText("Click when done inputting initials");
        newHSButtonOk.setEnabled(false);
        newHSButtonOk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newHSButtonOkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout NewHSPanelLayout = new javax.swing.GroupLayout(NewHSPanel);
        NewHSPanel.setLayout(NewHSPanelLayout);
        NewHSPanelLayout.setHorizontalGroup(
            NewHSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewHSPanelLayout.createSequentialGroup()
                .addGap(191, 191, 191)
                .addGroup(NewHSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(initialEntryTextField)
                    .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel9, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newHSScoreLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel10, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newHSButtonOk, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(180, Short.MAX_VALUE))
        );
        NewHSPanelLayout.setVerticalGroup(
            NewHSPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(NewHSPanelLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 64, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel9, javax.swing.GroupLayout.PREFERRED_SIZE, 47, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(newHSScoreLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(initialEntryTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(newHSButtonOk)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        F1Panel.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        F1Panel.setEnabled(false);
        F1Panel.setMaximumSize(new java.awt.Dimension(600, 400));
        F1Panel.setPreferredSize(new java.awt.Dimension(600, 400));

        jLabel11.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("F1 Menu");
        jLabel11.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Robert Berger Engle, Bronco #014011517");

        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Cynthia Luong, Bronco #011673490");

        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Kenneth Shuto, Bronco #012585989");

        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("Rida Siddiqui, Bronco #014147900");

        F1BackButton.setText("Back");
        F1BackButton.setToolTipText("Click to go back to the current panel");
        F1BackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                F1BackButtonActionPerformed(evt);
            }
        });

        jLabel16.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("Project Name: Point and Click Game v.1.2");

        jLabel17.setFont(new java.awt.Font("sansserif", 1, 14)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("Term: Spring 2021");

        javax.swing.GroupLayout F1PanelLayout = new javax.swing.GroupLayout(F1Panel);
        F1Panel.setLayout(F1PanelLayout);
        F1PanelLayout.setHorizontalGroup(
            F1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(F1PanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(F1BackButton)
                .addContainerGap(539, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, F1PanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(F1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel11, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel12)
                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 238, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel15, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 292, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        F1PanelLayout.setVerticalGroup(
            F1PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(F1PanelLayout.createSequentialGroup()
                .addContainerGap(63, Short.MAX_VALUE)
                .addComponent(jLabel11)
                .addGap(18, 18, 18)
                .addComponent(jLabel16, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel17, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23)
                .addComponent(jLabel12)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel13)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel15)
                .addGap(62, 62, 62)
                .addComponent(F1BackButton)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 612, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(CreditsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(HighscorePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(DisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(EndPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(NewHSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(F1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, 418, Short.MAX_VALUE)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(CreditsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(HighscorePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(DisplayPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(EndPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(6, 6, 6)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(NewHSPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(PlayPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(F1Panel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void PlayButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PlayButtonActionPerformed
        //------------HANGMAN CODE--------------//
        //Change window visibility
        DisplayPanel.setVisible(false);
        PlayPanel.setVisible(true);
        currentPanel = PlayPanel;
        
        headImage.setVisible(false);
        bodyImage.setVisible(false);
        leftArmImage.setVisible(false);
        rightArmImage.setVisible(false);
        leftLegImage.setVisible(false);
        rightLegImage.setVisible(false);
        
        //Pick random word from the set ("abstract", "cemetery", "nurse", "pharmacy", "climbing")
        int random = r.nextInt(wordList.length);
        chosenWord = wordList[random];
        //Set our boxes chosen word
        //TODO: This is for testing. The user won't actually know the word.
        randomTest.setText(chosenWord);
        
        //Set blanks based on length of word
        String blankText = "";
        for(int i = 0; i < chosenWord.length(); i++)
            blankText += "_ ";
        blankText = blankText.substring(0,blankText.length()-1);    //remove last space
        blankLabel.setText(blankText);
        
        //Set starting score
        this.score = 100;
        
        //Set score textbox
        txtScore.setText(String.format("Score: %d", (int)this.score));
        
        
        //----------COLOR BUTTONS CODE-----------//
        round = 0;
        
        randomizeText();
        changeButtonLocation();
        
        //---------SUDOKU CODE-----------------//
        sudokuScore = 540;
        txtScore3.setText(String.format("Sudoku Score: %d", sudokuScore));
        
        
    }//GEN-LAST:event_PlayButtonActionPerformed

    private void CreditsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreditsButtonActionPerformed
        DisplayPanel.setVisible(false);
        CreditsPanel.setVisible(true);
        currentPanel = CreditsPanel;
    }//GEN-LAST:event_CreditsButtonActionPerformed

    private void HSBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HSBackButtonActionPerformed
        HighscorePanel.setVisible(false);
        DisplayPanel.setVisible(true);
        currentPanel = DisplayPanel;
    }//GEN-LAST:event_HSBackButtonActionPerformed

    private void HighscoresButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HighscoresButtonActionPerformed
        //Populate hashmap
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File(SAVES_PATH.toURI())));
            String inLine = "";
            userScoreMap = new HashMap<>();
            while((inLine = br.readLine()) != null){
                String[] strSplit = inLine.split(" ");
                String user = strSplit[0];
                Integer score = Integer.parseInt(strSplit[1]);
                userScoreMap.put(user, score);
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //Move values into an array
        int[] valueArr = new int[userScoreMap.size()];
        Iterator it = userScoreMap.keySet().iterator();
        int valueArrIndex = 0;
        while(it.hasNext()){
            String key = (String)it.next();
            Integer value = userScoreMap.get(key);
            valueArr[valueArrIndex] = value;
            valueArrIndex++;
        }
        //Sort array
        Arrays.sort(valueArr);
        //Loop through array,setting text, looking up key for value, so names are sorted by values
        //Make buffer to build our string
        StringBuilder buf = new StringBuilder();
        buf.append("<html>");
        for(int i = valueArr.length - 1; i >= 0; i--){
            it = userScoreMap.keySet().iterator();
            while(it.hasNext()){
                String user = (String)it.next();
                int value = userScoreMap.get(user);
                if(value == valueArr[i]){
                    buf.append(String.format("%s...........%d<br/>", user, value));
                }
            }
        }
        buf.append("</html>");
        
        //Set text on high score panel to buffer
        ScoresLabel.setText(buf.toString());
        
        //Display high scores panel
        DisplayPanel.setVisible(false);
        HighscorePanel.setVisible(true);
        currentPanel = HighscorePanel;
    }//GEN-LAST:event_HighscoresButtonActionPerformed

    private void CreditsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreditsBackButtonActionPerformed
        CreditsPanel.setVisible(false);
        DisplayPanel.setVisible(true);
        currentPanel = DisplayPanel;
    }//GEN-LAST:event_CreditsBackButtonActionPerformed

    private void systemTimeTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemTimeTextActionPerformed
        
    }//GEN-LAST:event_systemTimeTextActionPerformed

    private void txtScoreActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtScoreActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtScoreActionPerformed

    /* SKIP BUTTON PRESSED */
    private void skipButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_skipButtonActionPerformed
        score = 0;
        //TODO: Think of additional things to implement here.
        endGame();
    }//GEN-LAST:event_skipButtonActionPerformed

    private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField2ActionPerformed

    private void EndBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EndBtnActionPerformed
        EndPanel.setVisible(false);
        DisplayPanel.setVisible(true);
        currentPanel = DisplayPanel;
    }//GEN-LAST:event_EndBtnActionPerformed

    private void randomTestActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_randomTestActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_randomTestActionPerformed

    private void UButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_UButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_UButtonActionPerformed

    private void DButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_DButtonActionPerformed

    private void RButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_RButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_RButtonActionPerformed

    private void HButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_HButtonActionPerformed

    private void YButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_YButtonActionPerformed

    private void WButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_WButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_WButtonActionPerformed

    private void IButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_IButtonActionPerformed

    private void OButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_OButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_OButtonActionPerformed

    private void JButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_JButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_JButtonActionPerformed

    private void VButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_VButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_VButtonActionPerformed

    private void SButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_SButtonActionPerformed

    private void MButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_MButtonActionPerformed

    private void GButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_GButtonActionPerformed

    private void AButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_AButtonActionPerformed

    private void KButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_KButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_KButtonActionPerformed

    private void NButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_NButtonActionPerformed

    private void TButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_TButtonActionPerformed

    private void LButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_LButtonActionPerformed

    private void CButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_CButtonActionPerformed

    private void XButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_XButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_XButtonActionPerformed

    private void PButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_PButtonActionPerformed

    private void ZButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ZButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_ZButtonActionPerformed

    private void QButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_QButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_QButtonActionPerformed

    private void FButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_FButtonActionPerformed

    private void EButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_EButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_EButtonActionPerformed

    private void BButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_BButtonActionPerformed
        genericGameBtnPressed(evt);
    }//GEN-LAST:event_BButtonActionPerformed

    private void redButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_redButtonActionPerformed
        chosenColor = 1;
        genericGameBtnPressed2(evt);
    }//GEN-LAST:event_redButtonActionPerformed

    private void purpleButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_purpleButtonActionPerformed
        chosenColor = 5;
        genericGameBtnPressed2(evt);
    }//GEN-LAST:event_purpleButtonActionPerformed

    private void blueButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_blueButtonActionPerformed
        chosenColor = 4;
        genericGameBtnPressed2(evt);
    }//GEN-LAST:event_blueButtonActionPerformed

    private void greenButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_greenButtonActionPerformed
        chosenColor = 3;
        genericGameBtnPressed2(evt);
    }//GEN-LAST:event_greenButtonActionPerformed

    private void yellowButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_yellowButtonActionPerformed
        chosenColor = 2;
        genericGameBtnPressed2(evt);
    }//GEN-LAST:event_yellowButtonActionPerformed

    private void systemTimeText2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemTimeText2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_systemTimeText2ActionPerformed

    private void txtScore2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtScore2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtScore2ActionPerformed

    private void greenButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_greenButtonMouseEntered
        genericGameBtnMouseIn(evt, "GREEN");
    }//GEN-LAST:event_greenButtonMouseEntered

    private void redButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redButtonMouseEntered
        genericGameBtnMouseIn(evt, "RED");
    }//GEN-LAST:event_redButtonMouseEntered

    private void redButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redButtonMouseExited
        genericGameBtnMouseOut(evt, "RED");
    }//GEN-LAST:event_redButtonMouseExited

    private void yellowButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yellowButtonMouseEntered
        genericGameBtnMouseIn(evt, "YELLOW");
    }//GEN-LAST:event_yellowButtonMouseEntered

    private void yellowButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yellowButtonMouseExited
        genericGameBtnMouseOut(evt, "YELLOW");
    }//GEN-LAST:event_yellowButtonMouseExited

    private void greenButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_greenButtonMouseExited
        genericGameBtnMouseOut(evt, "GREEN");
    }//GEN-LAST:event_greenButtonMouseExited

    private void blueButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blueButtonMouseEntered
        genericGameBtnMouseIn(evt, "BLUE");
    }//GEN-LAST:event_blueButtonMouseEntered

    private void blueButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blueButtonMouseExited
        genericGameBtnMouseOut(evt, "BLUE");
    }//GEN-LAST:event_blueButtonMouseExited

    private void purpleButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purpleButtonMouseEntered
        genericGameBtnMouseIn(evt, "PURPLE");
    }//GEN-LAST:event_purpleButtonMouseEntered

    private void purpleButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_purpleButtonMouseExited
        genericGameBtnMouseOut(evt, "PURPLE");
    }//GEN-LAST:event_purpleButtonMouseExited

    private void initialEntryTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_initialEntryTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_initialEntryTextFieldActionPerformed

    private void newHSButtonOkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newHSButtonOkActionPerformed
        //Save highscores
        //Populate hashmap
        userScoreMap = new HashMap<>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(new File(SAVES_PATH.toURI())));
            String inLine = "";
            while((inLine = br.readLine()) != null){
                String[] strSplit = inLine.split(" ");
                String user = strSplit[0];
                Integer score = Integer.parseInt(strSplit[1]);
                userScoreMap.put(user, score);;
            }
            br.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        //Move values into an array
        int[] valueArr = new int[userScoreMap.size()];
        Iterator it = userScoreMap.keySet().iterator();
        int valueArrIndex = 0;
        while(it.hasNext()){
            String key = (String)it.next();
            Integer value = userScoreMap.get(key);
            valueArr[valueArrIndex] = value;
            valueArrIndex++;
        }
        if(valueArr.length < 5){
            int[] newArr = new int[valueArr.length + 1];
            System.arraycopy(valueArr, 0, newArr, 0, valueArr.length);
            newArr[valueArr.length] = score;
            valueArr = newArr;
        }else{
            for(int i = 0; i < valueArr.length; i++){
                if(valueArr[i] < score){
                    valueArr[i] = score;
                    it = userScoreMap.keySet().iterator();
                    while(it.hasNext()){
                        String user = (String)it.next();
                        int value = userScoreMap.get(user);
                        if(value == valueArr[i]){
                            userScoreMap.remove(user);
                        }
                    }
                    break;
                }
            }
        }
        //Update our text file
        String newUserEntry = initialEntryTextField.getText();
        userScoreMap.put(newUserEntry, score);
        try{
            FileWriter fw0b = new FileWriter(new File(SAVES_PATH.toURI()), false);
            PrintWriter pw0b = new PrintWriter(fw0b, false);
            pw0b.flush();
            pw0b.close();
            fw0b.close();
            BufferedWriter bw = new BufferedWriter(new FileWriter(new File(SAVES_PATH.toURI())));
            StringBuilder sb = new StringBuilder();

                it = userScoreMap.keySet().iterator();
                while(it.hasNext()){
                    String user = (String)it.next();
                    int value = userScoreMap.get(user);
                    sb.append(String.format("%s %d\n", user, value));
                }
            
            
            bw.write(sb.toString());
            bw.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        
        //Set panel visibility
        txtEndScore.setText(Integer.toString(score));
        NewHSPanel.setVisible(false);
        initialEntryTextField.setVisible(false);
        EndPanel.setVisible(true);
        currentPanel = EndPanel;
    }//GEN-LAST:event_newHSButtonOkActionPerformed

    private void initialEntryTextFieldKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_initialEntryTextFieldKeyPressed
        String intials = initialEntryTextField.getText();
        if(intials.length() == 3){
            newHSButtonOk.setEnabled(true);
        }else{
            newHSButtonOk.setEnabled(false);
        }
    }//GEN-LAST:event_initialEntryTextFieldKeyPressed

    private void initialEntryTextFieldKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_initialEntryTextFieldKeyReleased
        String intials = initialEntryTextField.getText();
        if(intials.length() == 3){
            newHSButtonOk.setEnabled(true);
        }else{
            newHSButtonOk.setEnabled(false);
        }
    }//GEN-LAST:event_initialEntryTextFieldKeyReleased

    private void sudokuBox24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox24ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox24ActionPerformed

    private void sudokuBox22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox22ActionPerformed

    private void sudokuBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox5ActionPerformed

    private void sudokuBox2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox2ActionPerformed

    private void sudokuBox19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox19ActionPerformed

    private void sudokuBox21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox21ActionPerformed

    private void sudokuBox7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox7ActionPerformed

    private void sudokuBox8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox8ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox8ActionPerformed

    private void sudokuBox27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox27ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox27ActionPerformed

    private void sudokuBox29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox29ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox29ActionPerformed

    private void sudokuBox46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox46ActionPerformed

    private void sudokuBox31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox31ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox31ActionPerformed

    private void sudokuBox49ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox49ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox49ActionPerformed

    private void sudokuBox51ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox51ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox51ActionPerformed

    private void sudokuBox55ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox55ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox55ActionPerformed

    private void sudokuBox75ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox75ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox75ActionPerformed

    private void sudokuBox58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox58ActionPerformed

    private void sudokuBox59ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox59ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox59ActionPerformed

    private void sudokuBox61ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox61ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox61ActionPerformed

    private void sudokuBox79ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox79ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox79ActionPerformed

    private void sudokuBox10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox10ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox10ActionPerformed

    private void sudokuBox14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox14ActionPerformed

    private void sudokuBox17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox17ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox17ActionPerformed

    private void sudokuBox40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuBox40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox40ActionPerformed

    private void systemTimeText3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemTimeText3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_systemTimeText3ActionPerformed

    private void txtScore3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtScore3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtScore3ActionPerformed

    private void txtScore4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtScore4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtScore4ActionPerformed

    private void sudokuSubmitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuSubmitButtonActionPerformed
        sudokuSubmit();
    }//GEN-LAST:event_sudokuSubmitButtonActionPerformed

    private void sudokuQuitButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sudokuQuitButtonActionPerformed
        sudokuQuit();
    }//GEN-LAST:event_sudokuQuitButtonActionPerformed

    private void sudokuBox2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_sudokuBox2KeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_sudokuBox2KeyTyped

    private void F1BackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_F1BackButtonActionPerformed
        // TODO add your handling code here:
        F1Panel.setVisible(false);
        currentPanel.setVisible(true);
    }//GEN-LAST:event_F1BackButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(PointClickGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PointClickGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PointClickGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PointClickGUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new PointClickGUI().setVisible(true);
            }
        });

        
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AButton;
    private javax.swing.JButton BButton;
    private javax.swing.JButton CButton;
    private javax.swing.JButton CreditsBackButton;
    private javax.swing.JButton CreditsButton;
    private javax.swing.JPanel CreditsPanel;
    private javax.swing.JButton DButton;
    private javax.swing.JLabel DisplayIcon;
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JButton EButton;
    private javax.swing.JPanel EndPanel;
    private javax.swing.JButton F1BackButton;
    private javax.swing.JPanel F1Panel;
    private javax.swing.JButton FButton;
    private javax.swing.JButton GButton;
    private javax.swing.JButton HButton;
    private javax.swing.JButton HSBackButton;
    private javax.swing.JLabel HSLabel;
    private javax.swing.JPanel HighscorePanel;
    private javax.swing.JButton HighscoresButton;
    private javax.swing.JButton IButton;
    private javax.swing.JButton JButton;
    private javax.swing.JButton KButton;
    private javax.swing.JButton LButton;
    private javax.swing.JButton MButton;
    private javax.swing.JButton NButton;
    private javax.swing.JPanel NewHSPanel;
    private javax.swing.JButton OButton;
    private javax.swing.JButton PButton;
    private javax.swing.JButton PlayButton;
    private javax.swing.JPanel PlayPanel;
    private javax.swing.JPanel PlayPanel2;
    private javax.swing.JPanel PlayPanel3;
    private javax.swing.JButton QButton;
    private javax.swing.JButton RButton;
    private javax.swing.JButton SButton;
    private javax.swing.JLabel ScoresLabel;
    private javax.swing.JPanel StartPanel;
    private javax.swing.JButton TButton;
    private javax.swing.JButton UButton;
    private javax.swing.JButton VButton;
    private javax.swing.JButton WButton;
    private javax.swing.JButton XButton;
    private javax.swing.JButton YButton;
    private javax.swing.JButton ZButton;
    private javax.swing.JLabel blankLabel;
    private javax.swing.JButton blueButton;
    private javax.swing.JLabel bodyImage;
    private javax.swing.JLabel colorText;
    private javax.swing.JButton greenButton;
    private javax.swing.JLabel headImage;
    private javax.swing.JTextField initialEntryTextField;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel leftArmImage;
    private javax.swing.JLabel leftLegImage;
    private javax.swing.JButton newHSButtonOk;
    private javax.swing.JLabel newHSScoreLabel;
    private javax.swing.JLabel platformImage;
    private javax.swing.JButton purpleButton;
    private javax.swing.JTextField randomTest;
    private javax.swing.JButton redButton;
    private javax.swing.JLabel rightArmImage;
    private javax.swing.JLabel rightLegImage;
    private javax.swing.JButton skipButton;
    private javax.swing.JLabel sudokuBox1;
    private javax.swing.JTextField sudokuBox10;
    private javax.swing.JTextField sudokuBox11;
    private javax.swing.JTextField sudokuBox12;
    private javax.swing.JTextField sudokuBox13;
    private javax.swing.JTextField sudokuBox14;
    private javax.swing.JTextField sudokuBox15;
    private javax.swing.JLabel sudokuBox16;
    private javax.swing.JTextField sudokuBox17;
    private javax.swing.JTextField sudokuBox18;
    private javax.swing.JTextField sudokuBox19;
    private javax.swing.JTextField sudokuBox2;
    private javax.swing.JLabel sudokuBox20;
    private javax.swing.JTextField sudokuBox21;
    private javax.swing.JTextField sudokuBox22;
    private javax.swing.JTextField sudokuBox23;
    private javax.swing.JTextField sudokuBox24;
    private javax.swing.JLabel sudokuBox25;
    private javax.swing.JLabel sudokuBox26;
    private javax.swing.JTextField sudokuBox27;
    private javax.swing.JLabel sudokuBox28;
    private javax.swing.JTextField sudokuBox29;
    private javax.swing.JTextField sudokuBox3;
    private javax.swing.JLabel sudokuBox30;
    private javax.swing.JTextField sudokuBox31;
    private javax.swing.JLabel sudokuBox32;
    private javax.swing.JTextField sudokuBox33;
    private javax.swing.JLabel sudokuBox34;
    private javax.swing.JLabel sudokuBox35;
    private javax.swing.JTextField sudokuBox36;
    private javax.swing.JTextField sudokuBox37;
    private javax.swing.JTextField sudokuBox38;
    private javax.swing.JTextField sudokuBox39;
    private javax.swing.JLabel sudokuBox4;
    private javax.swing.JTextField sudokuBox40;
    private javax.swing.JLabel sudokuBox41;
    private javax.swing.JTextField sudokuBox42;
    private javax.swing.JTextField sudokuBox43;
    private javax.swing.JTextField sudokuBox44;
    private javax.swing.JTextField sudokuBox45;
    private javax.swing.JTextField sudokuBox46;
    private javax.swing.JLabel sudokuBox47;
    private javax.swing.JLabel sudokuBox48;
    private javax.swing.JTextField sudokuBox49;
    private javax.swing.JTextField sudokuBox5;
    private javax.swing.JLabel sudokuBox50;
    private javax.swing.JTextField sudokuBox51;
    private javax.swing.JLabel sudokuBox52;
    private javax.swing.JTextField sudokuBox53;
    private javax.swing.JLabel sudokuBox54;
    private javax.swing.JTextField sudokuBox55;
    private javax.swing.JLabel sudokuBox56;
    private javax.swing.JLabel sudokuBox57;
    private javax.swing.JTextField sudokuBox58;
    private javax.swing.JTextField sudokuBox59;
    private javax.swing.JLabel sudokuBox6;
    private javax.swing.JTextField sudokuBox60;
    private javax.swing.JTextField sudokuBox61;
    private javax.swing.JLabel sudokuBox62;
    private javax.swing.JTextField sudokuBox63;
    private javax.swing.JTextField sudokuBox64;
    private javax.swing.JTextField sudokuBox65;
    private javax.swing.JLabel sudokuBox66;
    private javax.swing.JTextField sudokuBox67;
    private javax.swing.JTextField sudokuBox68;
    private javax.swing.JTextField sudokuBox69;
    private javax.swing.JTextField sudokuBox7;
    private javax.swing.JTextField sudokuBox70;
    private javax.swing.JTextField sudokuBox71;
    private javax.swing.JTextField sudokuBox72;
    private javax.swing.JLabel sudokuBox73;
    private javax.swing.JTextField sudokuBox74;
    private javax.swing.JTextField sudokuBox75;
    private javax.swing.JLabel sudokuBox76;
    private javax.swing.JTextField sudokuBox77;
    private javax.swing.JLabel sudokuBox78;
    private javax.swing.JTextField sudokuBox79;
    private javax.swing.JTextField sudokuBox8;
    private javax.swing.JTextField sudokuBox80;
    private javax.swing.JLabel sudokuBox81;
    private javax.swing.JLabel sudokuBox9;
    private javax.swing.JPanel sudokuPanel;
    private javax.swing.JButton sudokuQuitButton;
    private javax.swing.JButton sudokuSubmitButton;
    private javax.swing.JTextField systemTimeText;
    private javax.swing.JTextField systemTimeText2;
    private javax.swing.JTextField systemTimeText3;
    private javax.swing.JTextField txtEndScore;
    private javax.swing.JTextField txtScore;
    private javax.swing.JTextField txtScore2;
    private javax.swing.JTextField txtScore3;
    private javax.swing.JTextField txtScore4;
    private javax.swing.JButton yellowButton;
    // End of variables declaration//GEN-END:variables
}
