package edu.cpp.cs2450s1.p1.src;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;
/**
 *
 * @author Team SwingSharp
 */
public class PointClickGUI extends javax.swing.JFrame {
    Random r = new Random();
    String[] wordList = {"abstract", "cemetery", "nurse", "pharmacy", "climbing"};
    String[] colorWordList = {"Green", "Red", "Yellow", "Purple", "Blue"};
    
    int randomColorText;
    Integer score, score2;
    String chosenWord;
    String chosenColorWord;
    static int round = 0;

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
        DisplayScorePanel.setVisible(false);
        EnterNamePanel.setVisible(false);
        
        
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
        txtEndScore.setText(String.format("%d",score));
        PlayPanel.setVisible(false);
        PlayPanel2.setVisible(true);
        
        //Reset buttons
        JButton[] buttons = new JButton[]{AButton,BButton,CButton,DButton,EButton,FButton,GButton,HButton,IButton, 
            JButton,KButton,LButton,MButton,NButton,OButton,PButton,QButton,RButton,
            SButton,TButton,UButton,VButton,WButton,XButton,YButton,ZButton};
        
        for(JButton b : buttons){
            b.setEnabled(true);
        }
    }
    
    private void endGame2(){    //Game: Color Buttons
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
        /* PSEUDOCODE
        
        if(button = correct color)
            > score2 += 100
        else
            > score2 += 0;

        randomizeText();             //changes color and text
        changeButtonLocation();
        round++;                    //increase rounds by 1

        if(round >= 5)
            > endGame2();            
            > saveHighScore();
        */
    }
    
    private void randomizeText(){   //Game: Color Buttons
        Random randomword = new Random();
        int random = randomword.nextInt(colorWordList.length);
        chosenColorWord = colorWordList[random];
        //Set our boxes chosen word
        //TODO: This is for testing. The user won't actually know the word.
        colorText.setText(chosenColorWord);
        
        Random randomcolor = new Random();
        randomColorText = randomcolor.nextInt(4);
        //randomColorText = 1;
        if (randomColorText == 0){
            colorText.setForeground(new java.awt.Color(255, 43, 19)); //red
        }
        if (randomColorText == 1){
            colorText.setForeground(new java.awt.Color(17, 197, 33)); //green
        }
        if (randomColorText == 2){
            colorText.setForeground(new java.awt.Color(0, 93, 204)); //blue
        }
        if (randomColorText == 3){
            colorText.setForeground(new java.awt.Color(255, 242, 56)); //yellow
        }
        if (randomColorText == 4){
            colorText.setForeground(new java.awt.Color(139, 11, 255)); //purple
        }
    }
    
    
    private void changeButtonLocation(JPanel panel){   //Game: Color Buttons
        Random randomnumber1 = new Random();
        Random randomnumber2 = new Random();
       
        int Xvalue;
        int Yvalue;
   
        for(int counter = 1; counter <= 1; counter++){
            Xvalue = 2 + randomnumber1.nextInt(498);
            Yvalue = 60 + randomnumber2.nextInt(240);
            panel.setBounds (Xvalue, Yvalue, 100, 100);
        }
    }
    
    private void updateHighScores(){    //Game: Color Buttons
        
        
    }

    private void setBorder(JPanel panel){
        panel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, null, null, null, new java.awt.Color(0, 0, 0)));
    }
    
    private void resetBorder(JPanel panel){
        panel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
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
        jPanel3 = new javax.swing.JPanel();
        systemTimeText2 = new javax.swing.JTextField();
        txtScore2 = new javax.swing.JTextField();
        YellowPanel = new javax.swing.JPanel();
        RedPanel = new javax.swing.JPanel();
        GreenPanel = new javax.swing.JPanel();
        BluePanel = new javax.swing.JPanel();
        PurplePanel = new javax.swing.JPanel();
        HighscorePanel = new javax.swing.JPanel();
        HSLabel = new javax.swing.JLabel();
        HSBackButton = new javax.swing.JButton();
        User1Label = new javax.swing.JLabel();
        User2Label = new javax.swing.JLabel();
        User3Label = new javax.swing.JLabel();
        User4Label = new javax.swing.JLabel();
        User5Label = new javax.swing.JLabel();
        CreditsPanel = new javax.swing.JPanel();
        CreditsBackButton = new javax.swing.JButton();
        CreditsTitleLabel = new javax.swing.JLabel();
        CreditsLabel1 = new javax.swing.JLabel();
        CreditsLabel2 = new javax.swing.JLabel();
        CreditsLabel3 = new javax.swing.JLabel();
        CreditsLabel4 = new javax.swing.JLabel();
        EndPanel = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        txtEndScore = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        DisplayScorePanel = new javax.swing.JPanel();
        jLabel9 = new javax.swing.JLabel();
        YesButton = new javax.swing.JButton();
        NoButton = new javax.swing.JButton();
        EnterNamePanel = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        NameTextField = new javax.swing.JTextField();
        SaveButton = new javax.swing.JButton();

        randomTest.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        randomTest.setText("Random word test");
        randomTest.setEnabled(false);
        randomTest.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                randomTestActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        jLabel1.setFont(new java.awt.Font("Calibri", 1, 36)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Point and Click Game");

        jLabel2.setFont(new java.awt.Font("Calibri", 0, 14)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("By: Team SwingSharp");

        javax.swing.GroupLayout StartPanelLayout = new javax.swing.GroupLayout(StartPanel);
        StartPanel.setLayout(StartPanelLayout);
        StartPanelLayout.setHorizontalGroup(
            StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StartPanelLayout.createSequentialGroup()
                .addContainerGap(96, Short.MAX_VALUE)
                .addGroup(StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StartPanelLayout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 432, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(91, 91, 91))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, StartPanelLayout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(224, 224, 224))))
        );
        StartPanelLayout.setVerticalGroup(
            StartPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(StartPanelLayout.createSequentialGroup()
                .addGap(101, 101, 101)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 92, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(91, 91, 91)
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
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(HButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(AButton)
                        .addComponent(BButton)
                        .addComponent(CButton)
                        .addComponent(DButton)
                        .addComponent(EButton)
                        .addComponent(FButton)
                        .addComponent(GButton)
                        .addComponent(IButton)
                        .addComponent(JButton)
                        .addComponent(KButton)
                        .addComponent(LButton)
                        .addComponent(MButton)))
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
            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 599, javax.swing.GroupLayout.PREFERRED_SIZE)
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

        colorText.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        colorText.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        colorText.setText("Color");
        PlayPanel2.add(colorText);
        colorText.setBounds(230, 20, 110, 30);

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
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(systemTimeText2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtScore2, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 7, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(systemTimeText2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtScore2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        PlayPanel2.add(jPanel3);
        jPanel3.setBounds(450, 0, 150, 60);

        YellowPanel.setBackground(new java.awt.Color(255, 242, 56));
        YellowPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        YellowPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                YellowPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                YellowPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                YellowPanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout YellowPanelLayout = new javax.swing.GroupLayout(YellowPanel);
        YellowPanel.setLayout(YellowPanelLayout);
        YellowPanelLayout.setHorizontalGroup(
            YellowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        YellowPanelLayout.setVerticalGroup(
            YellowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PlayPanel2.add(YellowPanel);
        YellowPanel.setBounds(50, 70, 100, 100);

        RedPanel.setBackground(new java.awt.Color(255, 43, 19));
        RedPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        RedPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                RedPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                RedPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                RedPanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout RedPanelLayout = new javax.swing.GroupLayout(RedPanel);
        RedPanel.setLayout(RedPanelLayout);
        RedPanelLayout.setHorizontalGroup(
            RedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        RedPanelLayout.setVerticalGroup(
            RedPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PlayPanel2.add(RedPanel);
        RedPanel.setBounds(50, 240, 100, 100);

        GreenPanel.setBackground(new java.awt.Color(17, 197, 33));
        GreenPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        GreenPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                GreenPanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                GreenPanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                GreenPanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout GreenPanelLayout = new javax.swing.GroupLayout(GreenPanel);
        GreenPanel.setLayout(GreenPanelLayout);
        GreenPanelLayout.setHorizontalGroup(
            GreenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        GreenPanelLayout.setVerticalGroup(
            GreenPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PlayPanel2.add(GreenPanel);
        GreenPanel.setBounds(230, 160, 100, 100);

        BluePanel.setBackground(new java.awt.Color(0, 93, 204));
        BluePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        BluePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                BluePanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                BluePanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                BluePanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout BluePanelLayout = new javax.swing.GroupLayout(BluePanel);
        BluePanel.setLayout(BluePanelLayout);
        BluePanelLayout.setHorizontalGroup(
            BluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        BluePanelLayout.setVerticalGroup(
            BluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PlayPanel2.add(BluePanel);
        BluePanel.setBounds(440, 250, 100, 100);

        PurplePanel.setBackground(new java.awt.Color(139, 11, 255));
        PurplePanel.setBorder(javax.swing.BorderFactory.createCompoundBorder());
        PurplePanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                PurplePanelMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                PurplePanelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                PurplePanelMouseExited(evt);
            }
        });

        javax.swing.GroupLayout PurplePanelLayout = new javax.swing.GroupLayout(PurplePanel);
        PurplePanel.setLayout(PurplePanelLayout);
        PurplePanelLayout.setHorizontalGroup(
            PurplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        PurplePanelLayout.setVerticalGroup(
            PurplePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        PlayPanel2.add(PurplePanel);
        PurplePanel.setBounds(440, 100, 100, 100);

        HSLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        HSLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        HSLabel.setText("Highscores");

        HSBackButton.setText("Back");
        HSBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                HSBackButtonActionPerformed(evt);
            }
        });

        User1Label.setText("user ........ userscore");

        User2Label.setText("user ........ userscore");

        User3Label.setText("user ........ userscore");

        User4Label.setText("user ........ userscore");

        User5Label.setText("user ........ userscore");

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
                        .addComponent(HSLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 149, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(HighscorePanelLayout.createSequentialGroup()
                        .addGap(217, 217, 217)
                        .addGroup(HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(User2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(User1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(User3Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(User4Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(User5Label, javax.swing.GroupLayout.PREFERRED_SIZE, 166, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(217, Short.MAX_VALUE))
        );
        HighscorePanelLayout.setVerticalGroup(
            HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(HighscorePanelLayout.createSequentialGroup()
                .addGap(63, 63, 63)
                .addComponent(HSLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(HighscorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(HighscorePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(HSBackButton)
                        .addContainerGap())
                    .addGroup(HighscorePanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(User1Label, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(User2Label, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(User3Label, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(User4Label, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(User5Label, javax.swing.GroupLayout.PREFERRED_SIZE, 24, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(112, Short.MAX_VALUE))))
        );

        CreditsBackButton.setText("Back");
        CreditsBackButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CreditsBackButtonActionPerformed(evt);
            }
        });

        CreditsTitleLabel.setFont(new java.awt.Font("Calibri", 1, 24)); // NOI18N
        CreditsTitleLabel.setText("Credits");
        CreditsTitleLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

        CreditsLabel1.setText("Robert Berger Engle, Bronco #014011517");

        CreditsLabel2.setText("Cynthia Luong, Bronco #011673490");

        CreditsLabel3.setText("Kenneth Shuto, Bronco #012585989");

        CreditsLabel4.setText("Rida Siddiqui, Bronco #014147900");

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
                    .addComponent(CreditsTitleLabel)
                    .addComponent(CreditsLabel1)
                    .addComponent(CreditsLabel2)
                    .addComponent(CreditsLabel3)
                    .addComponent(CreditsLabel4))
                .addGap(191, 191, 191))
        );
        CreditsPanelLayout.setVerticalGroup(
            CreditsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CreditsPanelLayout.createSequentialGroup()
                .addContainerGap(71, Short.MAX_VALUE)
                .addComponent(CreditsTitleLabel)
                .addGap(55, 55, 55)
                .addComponent(CreditsLabel1)
                .addGap(31, 31, 31)
                .addComponent(CreditsLabel2)
                .addGap(33, 33, 33)
                .addComponent(CreditsLabel3)
                .addGap(28, 28, 28)
                .addComponent(CreditsLabel4)
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

        DisplayScorePanel.setMinimumSize(new java.awt.Dimension(300, 200));

        jLabel9.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel9.setText("Would you like to save your score?");

        YesButton.setText("Yes");
        YesButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                YesButtonActionPerformed(evt);
            }
        });

        NoButton.setText("No");
        NoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DisplayScorePanelLayout = new javax.swing.GroupLayout(DisplayScorePanel);
        DisplayScorePanel.setLayout(DisplayScorePanelLayout);
        DisplayScorePanelLayout.setHorizontalGroup(
            DisplayScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisplayScorePanelLayout.createSequentialGroup()
                .addGroup(DisplayScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(DisplayScorePanelLayout.createSequentialGroup()
                        .addGap(22, 22, 22)
                        .addComponent(jLabel9))
                    .addGroup(DisplayScorePanelLayout.createSequentialGroup()
                        .addGap(134, 134, 134)
                        .addGroup(DisplayScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(NoButton)
                            .addComponent(YesButton))))
                .addContainerGap(23, Short.MAX_VALUE))
        );
        DisplayScorePanelLayout.setVerticalGroup(
            DisplayScorePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DisplayScorePanelLayout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addComponent(jLabel9)
                .addGap(31, 31, 31)
                .addComponent(YesButton)
                .addGap(18, 18, 18)
                .addComponent(NoButton)
                .addContainerGap(43, Short.MAX_VALUE))
        );

        EnterNamePanel.setMinimumSize(new java.awt.Dimension(300, 200));

        jLabel8.setFont(new java.awt.Font("Lucida Grande", 0, 18)); // NOI18N
        jLabel8.setText("Please Enter your Initials:");
        jLabel8.setLocation(new java.awt.Point(-32538, -32567));

        NameTextField.setFont(new java.awt.Font("Lucida Grande", 0, 14)); // NOI18N
        NameTextField.setText("Enter Initials");
        NameTextField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        NameTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NameTextFieldActionPerformed(evt);
            }
        });

        SaveButton.setText("Save");
        SaveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SaveButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout EnterNamePanelLayout = new javax.swing.GroupLayout(EnterNamePanel);
        EnterNamePanel.setLayout(EnterNamePanelLayout);
        EnterNamePanelLayout.setHorizontalGroup(
            EnterNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EnterNamePanelLayout.createSequentialGroup()
                .addContainerGap(43, Short.MAX_VALUE)
                .addGroup(EnterNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(EnterNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EnterNamePanelLayout.createSequentialGroup()
                            .addComponent(SaveButton)
                            .addGap(110, 110, 110))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, EnterNamePanelLayout.createSequentialGroup()
                            .addComponent(jLabel8)
                            .addGap(39, 39, 39)))))
        );
        EnterNamePanelLayout.setVerticalGroup(
            EnterNamePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(EnterNamePanelLayout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel8)
                .addGap(18, 18, 18)
                .addComponent(NameTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(12, 12, 12)
                .addComponent(SaveButton)
                .addContainerGap(38, Short.MAX_VALUE))
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
                    .addComponent(DisplayScorePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(EnterNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
                    .addComponent(DisplayScorePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, Short.MAX_VALUE)))
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(layout.createSequentialGroup()
                    .addGap(0, 0, Short.MAX_VALUE)
                    .addComponent(EnterNamePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        //changeButtonLocation();
        
        
        
        
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
        /*if (Userscore > LowestHighScore or 5th high score){
            then display DisplayScorePanel
      
        }*/
        //if user's score is greater than the lowest displayed highscore, when they press the end button
        //a frame will pop out asking them, if they want to put their highscore on the highscores board, 
        //if they click yes, let them enter their name, if they click no, go back to displayPanel
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

    private void SaveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SaveButtonActionPerformed
        String username = NameTextField.getText();
        User1Label.setText(username + "....." + score);
    
    }//GEN-LAST:event_SaveButtonActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void systemTimeText2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemTimeText2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_systemTimeText2ActionPerformed

    private void txtScore2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtScore2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtScore2ActionPerformed

    private void YesButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_YesButtonActionPerformed
        DisplayScorePanel.setVisible(false);
        EnterNamePanel.setVisible(true);
    }//GEN-LAST:event_YesButtonActionPerformed

    private void NoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NoButtonActionPerformed
        DisplayScorePanel.setVisible(false);
        DisplayPanel.setVisible(true);
    }//GEN-LAST:event_NoButtonActionPerformed

    private void NameTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NameTextFieldActionPerformed
        
    }//GEN-LAST:event_NameTextFieldActionPerformed

    private void YellowPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_YellowPanelMouseEntered
        setBorder(YellowPanel);
    }//GEN-LAST:event_YellowPanelMouseEntered

    private void YellowPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_YellowPanelMouseExited
        resetBorder(YellowPanel);
    }//GEN-LAST:event_YellowPanelMouseExited

    private void RedPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedPanelMouseEntered
        setBorder(RedPanel);
    }//GEN-LAST:event_RedPanelMouseEntered

    private void RedPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedPanelMouseExited
        resetBorder(RedPanel);
    }//GEN-LAST:event_RedPanelMouseExited

    private void GreenPanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GreenPanelMouseEntered
        setBorder(GreenPanel);
    }//GEN-LAST:event_GreenPanelMouseEntered

    private void GreenPanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GreenPanelMouseExited
        resetBorder(GreenPanel);
    }//GEN-LAST:event_GreenPanelMouseExited

    private void PurplePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PurplePanelMouseEntered
        setBorder(PurplePanel);
    }//GEN-LAST:event_PurplePanelMouseEntered

    private void PurplePanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PurplePanelMouseExited
        resetBorder(PurplePanel);
    }//GEN-LAST:event_PurplePanelMouseExited

    private void BluePanelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BluePanelMouseEntered
        setBorder(BluePanel);
    }//GEN-LAST:event_BluePanelMouseEntered

    private void BluePanelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BluePanelMouseExited
        resetBorder(BluePanel);
    }//GEN-LAST:event_BluePanelMouseExited

    private void YellowPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_YellowPanelMouseClicked
        //restart color game
        //if correct choice then update score (100 points for every correct choice)
       //changeButtonLocation();
       changeButtonLocation(YellowPanel);
       changeButtonLocation(PurplePanel);
       changeButtonLocation(BluePanel);
       changeButtonLocation(GreenPanel);
       changeButtonLocation(RedPanel);
       
    }//GEN-LAST:event_YellowPanelMouseClicked

    private void RedPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_RedPanelMouseClicked
        // TODO add your handling code here:
        //changeButtonLocation(RedPanel);
    }//GEN-LAST:event_RedPanelMouseClicked

    private void GreenPanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_GreenPanelMouseClicked
        //changeButtonLocation(GreenPanel);
    }//GEN-LAST:event_GreenPanelMouseClicked

    private void PurplePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_PurplePanelMouseClicked
       //changeButtonLocation(PurplePanel)
    }//GEN-LAST:event_PurplePanelMouseClicked

    private void BluePanelMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_BluePanelMouseClicked
        //changeButtonLocation(BluePanel);
    }//GEN-LAST:event_BluePanelMouseClicked
    
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
    private javax.swing.JPanel BluePanel;
    private javax.swing.JButton CButton;
    private javax.swing.JButton CreditsBackButton;
    private javax.swing.JButton CreditsButton;
    private javax.swing.JLabel CreditsLabel1;
    private javax.swing.JLabel CreditsLabel2;
    private javax.swing.JLabel CreditsLabel3;
    private javax.swing.JLabel CreditsLabel4;
    private javax.swing.JPanel CreditsPanel;
    private javax.swing.JLabel CreditsTitleLabel;
    private javax.swing.JButton DButton;
    private javax.swing.JLabel DisplayIcon;
    private javax.swing.JPanel DisplayPanel;
    private javax.swing.JPanel DisplayScorePanel;
    private javax.swing.JButton EButton;
    private javax.swing.JPanel EndPanel;
    private javax.swing.JPanel EnterNamePanel;
    private javax.swing.JButton FButton;
    private javax.swing.JButton GButton;
    private javax.swing.JPanel GreenPanel;
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
    private javax.swing.JTextField NameTextField;
    private javax.swing.JButton NoButton;
    private javax.swing.JButton OButton;
    private javax.swing.JButton PButton;
    private javax.swing.JButton PlayButton;
    private javax.swing.JPanel PlayPanel;
    private javax.swing.JPanel PlayPanel2;
    private javax.swing.JPanel PurplePanel;
    private javax.swing.JButton QButton;
    private javax.swing.JButton RButton;
    private javax.swing.JPanel RedPanel;
    private javax.swing.JButton SButton;
    private javax.swing.JButton SaveButton;
    private javax.swing.JPanel StartPanel;
    private javax.swing.JButton TButton;
    private javax.swing.JButton UButton;
    private javax.swing.JLabel User1Label;
    private javax.swing.JLabel User2Label;
    private javax.swing.JLabel User3Label;
    private javax.swing.JLabel User4Label;
    private javax.swing.JLabel User5Label;
    private javax.swing.JButton VButton;
    private javax.swing.JButton WButton;
    private javax.swing.JButton XButton;
    private javax.swing.JButton YButton;
    private javax.swing.JPanel YellowPanel;
    private javax.swing.JButton YesButton;
    private javax.swing.JButton ZButton;
    private javax.swing.JLabel blankLabel;
    private javax.swing.JLabel bodyImage;
    private javax.swing.JLabel colorText;
    private javax.swing.JLabel headImage;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JLabel leftArmImage;
    private javax.swing.JLabel leftLegImage;
    private javax.swing.JLabel platformImage;
    private javax.swing.JTextField randomTest;
    private javax.swing.JLabel rightArmImage;
    private javax.swing.JLabel rightLegImage;
    private javax.swing.JButton skipButton;
    private javax.swing.JTextField systemTimeText;
    private javax.swing.JTextField systemTimeText2;
    private javax.swing.JTextField txtEndScore;
    private javax.swing.JTextField txtScore;
    private javax.swing.JTextField txtScore2;
    // End of variables declaration//GEN-END:variables
}
