package edu.cpp.cs2450s1.p1.src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Paint;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.Timer;
import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
/**
 *
 * @author Team SwingSharp
 */
public class PointClickGUI extends javax.swing.JFrame {
    Random r = new Random();
    String[] wordList = {"abstract", "cemetery", "nurse", "pharmacy", "climbing"};
    String[] colorList = {"RED", "YELLOW", "GREEN", "BLUE", "PURPLE"};
    static HashMap<String, Integer> userScoreMap = new HashMap<>();
    Integer score;
    String chosenWord;
    static int round = 1;
    static int randomColorText, randomColor, chosenColor;
    final URL SAVES_PATH = getClass().getResource("/saves/highscores.txt");
    

    /**
     * Creates new form PointClickFrame
     */
    public PointClickGUI() {
        
        initComponents();

        DisplayPanel.setVisible(false);
        PlayPanel.setVisible(false);
        PlayPanel2.setVisible(false);
        HighscorePanel.setVisible(false);
        CreditsPanel.setVisible(false);
        EndPanel.setVisible(false);
        randomTest.setVisible(false);
        
        
        
        //Set System Timer
        Timer dateTimer = new Timer(1000, new ActionListener(){ //Updates every second
            public void actionPerformed(ActionEvent e)
            {
                Date date = new Date();                                 
                SimpleDateFormat sdf = new SimpleDateFormat("MMM d, y | HH:mm:ss");
                systemTimeText.setText(sdf.format(date));
                systemTimeText2.setText(sdf.format(date));
            }
        });
        dateTimer.start();
        
        //Set Screen Change Timer
        Timer timer = new Timer(3000, new ActionListener(){     //Switches screen after 3 seconds
            public void actionPerformed(ActionEvent e)
            {
                StartPanel.setVisible(false);
                DisplayPanel.setVisible(true);
            }
        });
        timer.setRepeats(false);   //Runs once
        timer.start();
        
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
        txtEndScore.setText(String.format("%d",score));
        PlayPanel2.setVisible(false);
        EndPanel.setVisible(true);
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
            updateHighScores();
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
    
    private void updateHighScores(){    //Game: Color Buttons
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
        //Lowest score is in valueArr[0]
        if(score > valueArr[0] || valueArr.length < 5){
            newHSScoreLabel.setText(Integer.toString(score));
            initialEntryTextField.setEnabled(true);
            initialEntryTextField.setVisible(true);
            newHSButtonOk.setEnabled(false);
            PlayPanel2.setVisible(false);
            NewHSPanel.setVisible(true);
        }else{
            endGame2();
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
        jPanel5 = new javax.swing.JPanel();
        jPanel6 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel11 = new javax.swing.JLabel();
        jTextField14 = new javax.swing.JTextField();
        jTextField15 = new javax.swing.JTextField();
        jTextField16 = new javax.swing.JTextField();
        jTextField17 = new javax.swing.JTextField();
        jTextField18 = new javax.swing.JTextField();
        jTextField19 = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        jTextField21 = new javax.swing.JTextField();
        jPanel7 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jTextField5 = new javax.swing.JTextField();
        jLabel14 = new javax.swing.JLabel();
        jTextField10 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jTextField8 = new javax.swing.JTextField();
        jTextField9 = new javax.swing.JTextField();
        jTextField11 = new javax.swing.JTextField();
        jTextField12 = new javax.swing.JTextField();
        jPanel10 = new javax.swing.JPanel();
        jTextField22 = new javax.swing.JTextField();
        jTextField23 = new javax.swing.JTextField();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jTextField26 = new javax.swing.JTextField();
        jTextField27 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        jTextField30 = new javax.swing.JTextField();
        jPanel8 = new javax.swing.JPanel();
        jLabel19 = new javax.swing.JLabel();
        jTextField32 = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        jTextField34 = new javax.swing.JTextField();
        jTextField35 = new javax.swing.JTextField();
        jTextField36 = new javax.swing.JTextField();
        jTextField37 = new javax.swing.JTextField();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jPanel14 = new javax.swing.JPanel();
        jTextField40 = new javax.swing.JTextField();
        jLabel40 = new javax.swing.JLabel();
        jTextField42 = new javax.swing.JTextField();
        jTextField43 = new javax.swing.JTextField();
        jLabel41 = new javax.swing.JLabel();
        jTextField45 = new javax.swing.JTextField();
        jTextField46 = new javax.swing.JTextField();
        jLabel42 = new javax.swing.JLabel();
        jTextField48 = new javax.swing.JTextField();
        jPanel11 = new javax.swing.JPanel();
        jLabel43 = new javax.swing.JLabel();
        jLabel44 = new javax.swing.JLabel();
        jTextField51 = new javax.swing.JTextField();
        jTextField52 = new javax.swing.JTextField();
        jTextField53 = new javax.swing.JTextField();
        jTextField54 = new javax.swing.JTextField();
        jLabel46 = new javax.swing.JLabel();
        jTextField56 = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        jPanel13 = new javax.swing.JPanel();
        jTextField58 = new javax.swing.JTextField();
        jLabel47 = new javax.swing.JLabel();
        jLabel48 = new javax.swing.JLabel();
        jTextField61 = new javax.swing.JTextField();
        jTextField62 = new javax.swing.JTextField();
        jLabel49 = new javax.swing.JLabel();
        jLabel50 = new javax.swing.JLabel();
        jTextField65 = new javax.swing.JTextField();
        jTextField66 = new javax.swing.JTextField();
        jPanel12 = new javax.swing.JPanel();
        jTextField67 = new javax.swing.JTextField();
        jTextField68 = new javax.swing.JTextField();
        jTextField69 = new javax.swing.JTextField();
        jTextField70 = new javax.swing.JTextField();
        jTextField71 = new javax.swing.JTextField();
        jTextField72 = new javax.swing.JTextField();
        jLabel51 = new javax.swing.JLabel();
        jTextField74 = new javax.swing.JTextField();
        jLabel52 = new javax.swing.JLabel();
        jPanel15 = new javax.swing.JPanel();
        jTextField76 = new javax.swing.JTextField();
        jLabel53 = new javax.swing.JLabel();
        jTextField78 = new javax.swing.JTextField();
        jTextField79 = new javax.swing.JTextField();
        jTextField80 = new javax.swing.JTextField();
        jTextField81 = new javax.swing.JTextField();
        jTextField82 = new javax.swing.JTextField();
        jTextField83 = new javax.swing.JTextField();
        jLabel54 = new javax.swing.JLabel();

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
                .addContainerGap(593, Short.MAX_VALUE)
                .addGroup(StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(143, 143, 143))
        );
        StartPanelLayout.setVerticalGroup(
            StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StartPanelLayout.createSequentialGroup()
                .addGap(103, 103, 103)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(89, 89, 89)
                .addComponent(jLabel2)
                .addContainerGap(122, Short.MAX_VALUE))
        );

        DisplayPanel.setPreferredSize(new java.awt.Dimension(600, 400));

        PlayButton.setText("Play");
        PlayButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PlayButtonActionPerformed(evt);
            }
        });

        HighscoresButton.setText("Highscores");
        HighscoresButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HighscoresButtonActionPerformed(evt);
            }
        });

        CreditsButton.setText("Credits");
        CreditsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreditsButtonActionPerformed(evt);
            }
        });

        DisplayIcon.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        DisplayIcon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images/icon.jpg"))); // NOI18N
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
        BButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                BButtonActionPerformed(evt);
            }
        });

        EButton.setText("E");
        EButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                EButtonActionPerformed(evt);
            }
        });

        FButton.setText("F");
        FButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FButtonActionPerformed(evt);
            }
        });

        QButton.setText("Q");
        QButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                QButtonActionPerformed(evt);
            }
        });

        ZButton.setText("Z");
        ZButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ZButtonActionPerformed(evt);
            }
        });

        PButton.setText("P");
        PButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PButtonActionPerformed(evt);
            }
        });

        XButton.setText("X");
        XButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                XButtonActionPerformed(evt);
            }
        });

        CButton.setText("C");
        CButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CButtonActionPerformed(evt);
            }
        });

        LButton.setText("L");
        LButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LButtonActionPerformed(evt);
            }
        });

        TButton.setText("T");
        TButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TButtonActionPerformed(evt);
            }
        });

        NButton.setText("N");
        NButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NButtonActionPerformed(evt);
            }
        });

        KButton.setText("K");
        KButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                KButtonActionPerformed(evt);
            }
        });

        AButton.setText("A");
        AButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AButtonActionPerformed(evt);
            }
        });

        GButton.setText("G");
        GButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GButtonActionPerformed(evt);
            }
        });

        MButton.setText("M");
        MButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MButtonActionPerformed(evt);
            }
        });

        SButton.setText("S");
        SButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SButtonActionPerformed(evt);
            }
        });

        VButton.setText("V");
        VButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                VButtonActionPerformed(evt);
            }
        });

        JButton.setText("J");
        JButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                JButtonActionPerformed(evt);
            }
        });

        OButton.setText("O");
        OButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                OButtonActionPerformed(evt);
            }
        });

        IButton.setText("I");
        IButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IButtonActionPerformed(evt);
            }
        });

        WButton.setText("W");
        WButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                WButtonActionPerformed(evt);
            }
        });

        YButton.setText("Y");
        YButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YButtonActionPerformed(evt);
            }
        });

        HButton.setText("H");
        HButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HButtonActionPerformed(evt);
            }
        });

        RButton.setText("R");
        RButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                RButtonActionPerformed(evt);
            }
        });

        DButton.setText("D");
        DButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DButtonActionPerformed(evt);
            }
        });

        UButton.setText("U");
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

        HSLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        HSLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HSLabel.setText("Highscores");

        HSBackButton.setText("Back");
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
                .addContainerGap(97, Short.MAX_VALUE))
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

        jPanel6.setPreferredSize(new java.awt.Dimension(350, 350));
        jPanel6.setLayout(new java.awt.GridLayout(3, 3));

        jPanel4.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel4.setLayout(new java.awt.GridLayout(3, 3));

        jLabel11.setBackground(new java.awt.Color(255, 255, 255));
        jLabel11.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("8");
        jLabel11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel11.setOpaque(true);
        jPanel4.add(jLabel11);

        jTextField14.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField14.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jTextField14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField14ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField14);

        jTextField15.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField15.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel4.add(jTextField15);

        jTextField16.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField16.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField16.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jTextField16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField16ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField16);

        jTextField17.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField17.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField17.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel4.add(jTextField17);

        jTextField18.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField18.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField18.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel4.add(jTextField18);

        jTextField19.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField19.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField19ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField19);

        jLabel12.setBackground(new java.awt.Color(255, 255, 255));
        jLabel12.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("1");
        jLabel12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel12.setOpaque(true);
        jPanel4.add(jLabel12);

        jTextField21.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField21.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField21.setAlignmentX(0.0F);
        jTextField21.setAlignmentY(0.0F);
        jTextField21.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField21.setMinimumSize(new java.awt.Dimension(26, 26));
        jTextField21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField21ActionPerformed(evt);
            }
        });
        jPanel4.add(jTextField21);

        jPanel6.add(jPanel4);

        jPanel7.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel7.setLayout(new java.awt.GridLayout(3, 3));

        jLabel13.setBackground(new java.awt.Color(255, 255, 255));
        jLabel13.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("4");
        jLabel13.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel13.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jLabel13.setOpaque(true);
        jPanel7.add(jLabel13);

        jTextField5.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField5.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField5.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField5ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField5);

        jLabel14.setBackground(new java.awt.Color(255, 255, 255));
        jLabel14.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("6");
        jLabel14.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel14.setOpaque(true);
        jPanel7.add(jLabel14);

        jTextField10.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField10.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField10.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel7.add(jTextField10);

        jTextField6.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField6.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField6.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField6ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField6);

        jTextField8.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField8.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField8.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel7.add(jTextField8);

        jTextField9.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField9.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField9.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField9ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField9);

        jTextField11.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField11.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField11.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel7.add(jTextField11);

        jTextField12.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField12.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField12.setAlignmentX(0.0F);
        jTextField12.setAlignmentY(0.0F);
        jTextField12.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField12.setMinimumSize(new java.awt.Dimension(26, 26));
        jTextField12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField12ActionPerformed(evt);
            }
        });
        jPanel7.add(jTextField12);

        jPanel6.add(jPanel7);

        jPanel10.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel10.setLayout(new java.awt.GridLayout(3, 3));

        jTextField22.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField22.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField22.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField22ActionPerformed(evt);
            }
        });
        jPanel10.add(jTextField22);

        jTextField23.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField23.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField23.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jTextField23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField23ActionPerformed(evt);
            }
        });
        jPanel10.add(jTextField23);

        jLabel15.setBackground(new java.awt.Color(255, 255, 255));
        jLabel15.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel15.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel15.setText("7");
        jLabel15.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel15.setOpaque(true);
        jPanel10.add(jLabel15);

        jLabel16.setBackground(new java.awt.Color(255, 255, 255));
        jLabel16.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel16.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel16.setText("4");
        jLabel16.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jLabel16.setOpaque(true);
        jPanel10.add(jLabel16);

        jTextField26.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField26.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField26.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jTextField26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField26ActionPerformed(evt);
            }
        });
        jPanel10.add(jTextField26);

        jTextField27.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField27.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField27.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel10.add(jTextField27);

        jLabel17.setBackground(new java.awt.Color(255, 255, 255));
        jLabel17.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel17.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel17.setText("6");
        jLabel17.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel17.setOpaque(true);
        jPanel10.add(jLabel17);

        jLabel18.setBackground(new java.awt.Color(255, 255, 255));
        jLabel18.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel18.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel18.setText("5");
        jLabel18.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel18.setOpaque(true);
        jPanel10.add(jLabel18);

        jTextField30.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField30.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField30.setAlignmentX(0.0F);
        jTextField30.setAlignmentY(0.0F);
        jTextField30.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField30.setMinimumSize(new java.awt.Dimension(26, 26));
        jTextField30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField30ActionPerformed(evt);
            }
        });
        jPanel10.add(jTextField30);

        jPanel6.add(jPanel10);

        jPanel8.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel8.setLayout(new java.awt.GridLayout(3, 3));

        jLabel19.setBackground(new java.awt.Color(255, 255, 255));
        jLabel19.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel19.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel19.setText("5");
        jLabel19.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel19.setOpaque(true);
        jPanel8.add(jLabel19);

        jTextField32.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField32.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField32.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jTextField32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField32ActionPerformed(evt);
            }
        });
        jPanel8.add(jTextField32);

        jLabel37.setBackground(new java.awt.Color(255, 255, 255));
        jLabel37.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel37.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel37.setText("9");
        jLabel37.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel37.setOpaque(true);
        jPanel8.add(jLabel37);

        jTextField34.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField34.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField34.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel8.add(jTextField34);

        jTextField35.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField35.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField35.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel8.add(jTextField35);

        jTextField36.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField36.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField36.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel8.add(jTextField36);

        jTextField37.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField37.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField37.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField37.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField37ActionPerformed(evt);
            }
        });
        jPanel8.add(jTextField37);

        jLabel38.setBackground(new java.awt.Color(255, 255, 255));
        jLabel38.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel38.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel38.setText("4");
        jLabel38.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel38.setOpaque(true);
        jPanel8.add(jLabel38);

        jLabel39.setBackground(new java.awt.Color(255, 255, 255));
        jLabel39.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel39.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel39.setText("8");
        jLabel39.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel39.setOpaque(true);
        jPanel8.add(jLabel39);

        jPanel6.add(jPanel8);

        jPanel14.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel14.setLayout(new java.awt.GridLayout(3, 3));

        jTextField40.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField40.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField40.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField40.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField40ActionPerformed(evt);
            }
        });
        jPanel14.add(jTextField40);

        jLabel40.setBackground(new java.awt.Color(255, 255, 255));
        jLabel40.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel40.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel40.setText("3");
        jLabel40.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel40.setOpaque(true);
        jPanel14.add(jLabel40);

        jTextField42.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField42.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField42.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel14.add(jTextField42);

        jTextField43.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField43.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField43.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jTextField43.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField43ActionPerformed(evt);
            }
        });
        jPanel14.add(jTextField43);

        jLabel41.setBackground(new java.awt.Color(255, 255, 255));
        jLabel41.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel41.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel41.setText("7");
        jLabel41.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jLabel41.setOpaque(true);
        jPanel14.add(jLabel41);

        jTextField45.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField45.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField45.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel14.add(jTextField45);

        jTextField46.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField46.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField46.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField46.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField46ActionPerformed(evt);
            }
        });
        jPanel14.add(jTextField46);

        jLabel42.setBackground(new java.awt.Color(255, 255, 255));
        jLabel42.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel42.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel42.setText("2");
        jLabel42.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel42.setOpaque(true);
        jPanel14.add(jLabel42);

        jTextField48.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField48.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField48.setAlignmentX(0.0F);
        jTextField48.setAlignmentY(0.0F);
        jTextField48.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField48.setMinimumSize(new java.awt.Dimension(26, 26));
        jTextField48.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField48ActionPerformed(evt);
            }
        });
        jPanel14.add(jTextField48);

        jPanel6.add(jPanel14);

        jPanel11.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel11.setLayout(new java.awt.GridLayout(3, 3));

        jLabel43.setBackground(new java.awt.Color(255, 255, 255));
        jLabel43.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel43.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel43.setText("7");
        jLabel43.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel43.setOpaque(true);
        jPanel11.add(jLabel43);

        jLabel44.setBackground(new java.awt.Color(255, 255, 255));
        jLabel44.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel44.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel44.setText("8");
        jLabel44.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel44.setOpaque(true);
        jPanel11.add(jLabel44);

        jTextField51.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField51.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField51.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(jTextField51);

        jTextField52.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField52.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField52.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(jTextField52);

        jTextField53.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField53.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField53.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel11.add(jTextField53);

        jTextField54.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField54.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField54.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel11.add(jTextField54);

        jLabel46.setBackground(new java.awt.Color(255, 255, 255));
        jLabel46.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel46.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel46.setText("1");
        jLabel46.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel46.setOpaque(true);
        jPanel11.add(jLabel46);

        jTextField56.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField56.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField56.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel11.add(jTextField56);

        jLabel45.setBackground(new java.awt.Color(255, 255, 255));
        jLabel45.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel45.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel45.setText("3");
        jLabel45.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel45.setOpaque(true);
        jPanel11.add(jLabel45);

        jPanel6.add(jPanel11);

        jPanel13.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel13.setLayout(new java.awt.GridLayout(3, 3));

        jTextField58.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField58.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField58.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField58.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField58ActionPerformed(evt);
            }
        });
        jPanel13.add(jTextField58);

        jLabel47.setBackground(new java.awt.Color(255, 255, 255));
        jLabel47.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel47.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel47.setText("5");
        jLabel47.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel47.setOpaque(true);
        jPanel13.add(jLabel47);

        jLabel48.setBackground(new java.awt.Color(255, 255, 255));
        jLabel48.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel48.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel48.setText("2");
        jLabel48.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel48.setOpaque(true);
        jPanel13.add(jLabel48);

        jTextField61.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField61.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField61.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel13.add(jTextField61);

        jTextField62.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField62.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField62.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel13.add(jTextField62);

        jLabel49.setBackground(new java.awt.Color(255, 255, 255));
        jLabel49.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel49.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel49.setText("1");
        jLabel49.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jLabel49.setOpaque(true);
        jPanel13.add(jLabel49);

        jLabel50.setBackground(new java.awt.Color(255, 255, 255));
        jLabel50.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel50.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel50.setText("3");
        jLabel50.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel50.setOpaque(true);
        jPanel13.add(jLabel50);

        jTextField65.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField65.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField65.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel13.add(jTextField65);

        jTextField66.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField66.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField66.setAlignmentX(0.0F);
        jTextField66.setAlignmentY(0.0F);
        jTextField66.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField66.setMinimumSize(new java.awt.Dimension(26, 26));
        jTextField66.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField66ActionPerformed(evt);
            }
        });
        jPanel13.add(jTextField66);

        jPanel6.add(jPanel13);

        jPanel12.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel12.setLayout(new java.awt.GridLayout(3, 3));

        jTextField67.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField67.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField67.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField67.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField67ActionPerformed(evt);
            }
        });
        jPanel12.add(jTextField67);

        jTextField68.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField68.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField68.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jTextField68.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField68ActionPerformed(evt);
            }
        });
        jPanel12.add(jTextField68);

        jTextField69.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField69.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField69.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(jTextField69);

        jTextField70.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField70.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField70.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(jTextField70);

        jTextField71.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField71.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField71.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel12.add(jTextField71);

        jTextField72.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField72.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField72.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel12.add(jTextField72);

        jLabel51.setBackground(new java.awt.Color(255, 255, 255));
        jLabel51.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel51.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel51.setText("9");
        jLabel51.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel51.setOpaque(true);
        jPanel12.add(jLabel51);

        jTextField74.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField74.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField74.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel12.add(jTextField74);

        jLabel52.setBackground(new java.awt.Color(255, 255, 255));
        jLabel52.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel52.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel52.setText("2");
        jLabel52.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel52.setOpaque(true);
        jPanel12.add(jLabel52);

        jPanel6.add(jPanel12);

        jPanel15.setBorder(new javax.swing.border.MatteBorder(null));
        jPanel15.setLayout(new java.awt.GridLayout(3, 3));

        jTextField76.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField76.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField76.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField76.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField76ActionPerformed(evt);
            }
        });
        jPanel15.add(jTextField76);

        jLabel53.setBackground(new java.awt.Color(255, 255, 255));
        jLabel53.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel53.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel53.setText("9");
        jLabel53.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jLabel53.setOpaque(true);
        jPanel15.add(jLabel53);

        jTextField78.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField78.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField78.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(jTextField78);

        jTextField79.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField79.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField79.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(jTextField79);

        jTextField80.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField80.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField80.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 1, 1, 1, new java.awt.Color(102, 102, 102)));
        jPanel15.add(jTextField80);

        jTextField81.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField81.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField81.setBorder(javax.swing.BorderFactory.createMatteBorder(1, 0, 1, 0, new java.awt.Color(102, 102, 102)));
        jPanel15.add(jTextField81);

        jTextField82.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField82.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField82.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jTextField82.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField82ActionPerformed(evt);
            }
        });
        jPanel15.add(jTextField82);

        jTextField83.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jTextField83.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        jTextField83.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 1, 0, 1, new java.awt.Color(102, 102, 102)));
        jPanel15.add(jTextField83);

        jLabel54.setBackground(new java.awt.Color(255, 255, 255));
        jLabel54.setFont(new java.awt.Font("Bodoni 72", 1, 28)); // NOI18N
        jLabel54.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel54.setText("5");
        jLabel54.setBorder(javax.swing.BorderFactory.createMatteBorder(0, 0, 0, 0, new java.awt.Color(102, 102, 102)));
        jLabel54.setOpaque(true);
        jPanel15.add(jLabel54);

        jPanel6.add(jPanel15);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(125, 125, 125)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(125, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(StartPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        
        
        
    }//GEN-LAST:event_PlayButtonActionPerformed

    private void CreditsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreditsButtonActionPerformed
        DisplayPanel.setVisible(false);
        CreditsPanel.setVisible(true);
    }//GEN-LAST:event_CreditsButtonActionPerformed

    private void HSBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_HSBackButtonActionPerformed
        HighscorePanel.setVisible(false);
        DisplayPanel.setVisible(true);
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
    }//GEN-LAST:event_HighscoresButtonActionPerformed

    private void CreditsBackButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CreditsBackButtonActionPerformed
        CreditsPanel.setVisible(false);
        DisplayPanel.setVisible(true);
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
        EndPanel.setVisible(true);
        initialEntryTextField.setVisible(false);
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

    private void jTextField12ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField12ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField12ActionPerformed

    private void jTextField9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField9ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField9ActionPerformed

    private void jTextField5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField5ActionPerformed

    private void jTextField14ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField14ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField14ActionPerformed

    private void jTextField19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField19ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField19ActionPerformed

    private void jTextField21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField21ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField21ActionPerformed

    private void jTextField22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField22ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField22ActionPerformed

    private void jTextField23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField23ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField23ActionPerformed

    private void jTextField30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField30ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField30ActionPerformed

    private void jTextField32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField32ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField32ActionPerformed

    private void jTextField37ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField37ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField37ActionPerformed

    private void jTextField40ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField40ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField40ActionPerformed

    private void jTextField46ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField46ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField46ActionPerformed

    private void jTextField48ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField48ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField48ActionPerformed

    private void jTextField58ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField58ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField58ActionPerformed

    private void jTextField66ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField66ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField66ActionPerformed

    private void jTextField67ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField67ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField67ActionPerformed

    private void jTextField68ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField68ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField68ActionPerformed

    private void jTextField76ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField76ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField76ActionPerformed

    private void jTextField82ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField82ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField82ActionPerformed

    private void jTextField16ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField16ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField16ActionPerformed

    private void jTextField6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField6ActionPerformed

    private void jTextField26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField26ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField26ActionPerformed

    private void jTextField43ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField43ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField43ActionPerformed

    
    
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
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel48;
    private javax.swing.JLabel jLabel49;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel50;
    private javax.swing.JLabel jLabel51;
    private javax.swing.JLabel jLabel52;
    private javax.swing.JLabel jLabel53;
    private javax.swing.JLabel jLabel54;
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
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField10;
    private javax.swing.JTextField jTextField11;
    private javax.swing.JTextField jTextField12;
    private javax.swing.JTextField jTextField14;
    private javax.swing.JTextField jTextField15;
    private javax.swing.JTextField jTextField16;
    private javax.swing.JTextField jTextField17;
    private javax.swing.JTextField jTextField18;
    private javax.swing.JTextField jTextField19;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField21;
    private javax.swing.JTextField jTextField22;
    private javax.swing.JTextField jTextField23;
    private javax.swing.JTextField jTextField26;
    private javax.swing.JTextField jTextField27;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField30;
    private javax.swing.JTextField jTextField32;
    private javax.swing.JTextField jTextField34;
    private javax.swing.JTextField jTextField35;
    private javax.swing.JTextField jTextField36;
    private javax.swing.JTextField jTextField37;
    private javax.swing.JTextField jTextField40;
    private javax.swing.JTextField jTextField42;
    private javax.swing.JTextField jTextField43;
    private javax.swing.JTextField jTextField45;
    private javax.swing.JTextField jTextField46;
    private javax.swing.JTextField jTextField48;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField51;
    private javax.swing.JTextField jTextField52;
    private javax.swing.JTextField jTextField53;
    private javax.swing.JTextField jTextField54;
    private javax.swing.JTextField jTextField56;
    private javax.swing.JTextField jTextField58;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField61;
    private javax.swing.JTextField jTextField62;
    private javax.swing.JTextField jTextField65;
    private javax.swing.JTextField jTextField66;
    private javax.swing.JTextField jTextField67;
    private javax.swing.JTextField jTextField68;
    private javax.swing.JTextField jTextField69;
    private javax.swing.JTextField jTextField70;
    private javax.swing.JTextField jTextField71;
    private javax.swing.JTextField jTextField72;
    private javax.swing.JTextField jTextField74;
    private javax.swing.JTextField jTextField76;
    private javax.swing.JTextField jTextField78;
    private javax.swing.JTextField jTextField79;
    private javax.swing.JTextField jTextField8;
    private javax.swing.JTextField jTextField80;
    private javax.swing.JTextField jTextField81;
    private javax.swing.JTextField jTextField82;
    private javax.swing.JTextField jTextField83;
    private javax.swing.JTextField jTextField9;
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
    private javax.swing.JTextField systemTimeText;
    private javax.swing.JTextField systemTimeText2;
    private javax.swing.JTextField txtEndScore;
    private javax.swing.JTextField txtScore;
    private javax.swing.JTextField txtScore2;
    private javax.swing.JButton yellowButton;
    // End of variables declaration//GEN-END:variables
}
