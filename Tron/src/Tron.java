/***************************************************************************************
	File: Tron.java 
	Purpose: Tron game that can be played with either 1 or 2 players, using JFrame and 
      Graphics to draw lines.  Single player has a computer player controlled by semi-
      random changes in direction based on difficulty.
	Author: Thomas Rosehart
	Date: December 22, 2017
	Based on: ICS4U Java Assignment 3, Question 1
*****************************************************************************************/
import java.awt.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Color;
import javax.swing.event.*;
import java.awt.Font.*;
import java.io.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Tron extends JFrame implements KeyListener, Runnable, ChangeListener, ActionListener
{
   //to allow user input
   static Tron frame;
   JTextArea displayArea;
   JTextField typingArea;
   //starting positions and values
   static int x = 50, y = 50;
   static int x2 = 450, y2 = 450;
   static String[][] checkFull = new String[500][520];
   static boolean up=false,down=true,left=false,right=false;
   static boolean up2=false,down2=false,left2=false,right2=false;
   static int score1 = 0, score2 = 0;
   GraphicsPanel graphicsPanel = new GraphicsPanel();
   //fonts for scores, etc
   static Font f = new Font("Serif",Font.BOLD, 20);
   static Font f2 = new Font("Serif",Font.BOLD, 50);
   
   //music and crash sound effect
   static MediaPlayer mp1 = new MediaPlayer("resources/scream.wav",false);// = new MediaPlayer("C:/Users/trose/eclipse-workspace/Tron/resources/scream.wav",false);
   static MediaPlayer mp2 = new MediaPlayer("resources/Popcorn Original Song.wav",true);// = new MediaPlayer("C:/Users/trose/eclipse-workspace/Tron/resources/Popcorn Original Song.wav",true);
   
   
   //sliders and labels to choose colour, winning score, speed of game
   JSlider rSlider, bSlider, gSlider, maxSlider, speedSlider, diffSlider;
   JLabel r = new JLabel("r");
   JLabel g = new JLabel("g");
   JLabel b = new JLabel("b");
   JLabel m = new JLabel("Winning score");
   JLabel s = new JLabel("Speed");
   JLabel d = new JLabel("Difficulty");
   //colours for each player
   static int r1=0, g1=255, b1=255, r2=255, g2=128, b2=0;
   static Color c1 = new Color(r1,g1,b1);
   static Color c2 = new Color(r2,g2,b2);
   //maximum score how long it sleeps for
   static int maxScore = 5;
   static int speed = 5;
   //button to start game
   JButton goButton = new JButton("Go!");
   static boolean go = false;
   //radio buttons so both users can change colour
   JRadioButton p1 = new JRadioButton("Player 1",true);
   JRadioButton p2 = new JRadioButton("Player 2",false);
   ButtonGroup player = new ButtonGroup();
   Box buttonBox;
   //panels for start screen
   JPanel sliderPanel = new JPanel();
   JPanel playerPanel = new JPanel();
   JPanel colourPanel = new JPanel();
   JPanel controlPanel = new JPanel();
   //for setting if there are 1 or 2 players
   JRadioButton oneP = new JRadioButton("One player",false);
   JRadioButton twoP = new JRadioButton("Two player",true);
   ButtonGroup numPlayers = new ButtonGroup();;
   static boolean twoPlayer = true, cpuChange = false;
   static int randomNumber, difficulty;  
   //image to show controls
   static BufferedImage controls;
   //things needed for different backgrounds
   static int background;
   static int[] starX = new int[200];
   static int[] starY = new int[200];
   //gradients for rainbow background
   GradientPaint redToOrange = new GradientPaint(0,0,Color.red,0,104,new Color(255,128,0));
   GradientPaint orangeToYellow = new GradientPaint(0,104,new Color(255,128,0),0,208,Color.yellow);
   GradientPaint yellowToGreen = new GradientPaint(0,208,Color.yellow,0,312,Color.green);
   GradientPaint greenToBlue = new GradientPaint(0,312,Color.green,0,416,Color.cyan);
   GradientPaint blueToPurple = new GradientPaint(0,416,Color.cyan,0,500,new Color(128,0,255));
   //background images
   static BufferedImage image1, image2, image3, image4, image5, image6, image7;
   //things for boosting speed of player
   static int bCounter1 = 0, bCounter2 = 0, remainingBoost1 = 3, remainingBoost2 = 3;
   static boolean boost1 = false, boost2 = false;
   
   public Tron(String name)
   {
      super(name);

      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      if(!go)
      {   
         //setting up start screen with sliders and buttons
         setBounds(300,50,500,900);
         
         Thread bgSound = new Thread(mp2);
         bgSound.start();
         
         //colour sliders
         rSlider = new JSlider(JSlider.HORIZONTAL,0,255,0);
         rSlider.setMajorTickSpacing(85);
         rSlider.setMinorTickSpacing(15);
         rSlider.setPaintTicks(true);
         rSlider.setPaintLabels(true);
         rSlider.setSnapToTicks(true);
         rSlider.addChangeListener(this);
         
         gSlider = new JSlider(JSlider.HORIZONTAL,0,255,255);
         gSlider.setMajorTickSpacing(85);
         gSlider.setMinorTickSpacing(15);
         gSlider.setPaintTicks(true);
         gSlider.setPaintLabels(true);
         gSlider.setSnapToTicks(true);
         gSlider.addChangeListener(this);
         
         bSlider = new JSlider(JSlider.HORIZONTAL,0,255,255);
         bSlider.setMajorTickSpacing(85);
         bSlider.setMinorTickSpacing(15);
         bSlider.setPaintTicks(true);
         bSlider.setPaintLabels(true);
         bSlider.setSnapToTicks(true);
         bSlider.addChangeListener(this);
         //max score slider
         maxSlider = new JSlider(JSlider.HORIZONTAL,1,16,5);
         maxSlider.setMajorTickSpacing(5);
         maxSlider.setMinorTickSpacing(1);
         maxSlider.setPaintTicks(true);
         maxSlider.setPaintLabels(true);
         maxSlider.setSnapToTicks(true);
         maxSlider.addChangeListener(this);
         //speed of game sliders
         speedSlider = new JSlider(JSlider.HORIZONTAL,5,25,21);
         speedSlider.setMajorTickSpacing(5);
         speedSlider.setMinorTickSpacing(1);
         speedSlider.setPaintTicks(true);
         speedSlider.setPaintLabels(true);
         speedSlider.setSnapToTicks(true);
         speedSlider.addChangeListener(this);
         //slider for difficulty
         diffSlider = new JSlider(JSlider.HORIZONTAL,0,5,3);
         diffSlider.setMajorTickSpacing(5);
         diffSlider.setMinorTickSpacing(1);
         diffSlider.setPaintTicks(true);
         diffSlider.setPaintLabels(true);
         diffSlider.setSnapToTicks(true);
         diffSlider.addChangeListener(this);
         diffSlider.setEnabled(false);
         //panel for all sliders and labels
         sliderPanel.setLayout(new FlowLayout());
         sliderPanel.add(r); sliderPanel.add(rSlider);
         sliderPanel.add(g); sliderPanel.add(gSlider);
         sliderPanel.add(b); sliderPanel.add(bSlider);
         sliderPanel.add(m); sliderPanel.add(maxSlider);
         sliderPanel.add(s); sliderPanel.add(speedSlider); 
         sliderPanel.add(d); sliderPanel.add(diffSlider);
         sliderPanel.setPreferredSize(new Dimension(200,420));
         //for choosing which user is changing their colour
         p1.addChangeListener(this);
         p2.addChangeListener(this);
         player.add(p1);
         player.add(p2);
         //for choosing one or two players
         oneP.addChangeListener(this);
         twoP.addChangeListener(this);
         numPlayers.add(oneP);
         numPlayers.add(twoP);
         buttonBox = new Box(BoxLayout.Y_AXIS);
         buttonBox.add(new JLabel("Player"));
         buttonBox.add(Box.createRigidArea(new Dimension(1,4)));
         buttonBox.add(p1);  
         buttonBox.add(p2);
         buttonBox.add(oneP);
         buttonBox.add(twoP);
         buttonBox.add(goButton);
         buttonBox.add(Box.createRigidArea(new Dimension(1,1)));
         goButton.addActionListener(this);
         
         //adding image to panel
         try {
        	 JLabel picLabel = new JLabel(new ImageIcon(controls));
        	 controlPanel.add(picLabel);
         }
         catch(Exception e) {}
         
         //adding all sliders and buttons to screen
         playerPanel.setLayout(new FlowLayout());
         colourPanel.setPreferredSize(new Dimension(200,200));
         playerPanel.add(colourPanel);
         playerPanel.add(goButton);
         playerPanel.setPreferredSize(new Dimension(200,360));
         
         setLayout(new FlowLayout());
         add(sliderPanel);
         add(playerPanel);
         add(buttonBox);
         add(controlPanel);
      }
      if(go)
      {
         //setting up playing screen
         fillGrid();
         addComponentsToPane();
         setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         setBounds(300,50,500,600);
         
         setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
         graphicsPanel.setPreferredSize(new Dimension(500,500));
         add(graphicsPanel);
      }
   
      pack();
      setVisible(true);
      setResizable(false); 
   }

   static void fillGrid() {
      go = false;
      //resetting game each time someone crashes
      x = 50;
      y = 50;
      down = true;
      right = false;
      up = false;
      left = false;
      x2 = 450;
      y2 = 450;
      down2 = false;
      right2 = false;
      up2 = false;
      left2 = false; 
      remainingBoost1 = 3;
      remainingBoost2 = 3;
      //choosing random background        
      //0 = black, 1 = grid, 2 = stars, 3 = some Guy, 4 = some person, 5 = trump, 6 = crying, 7 = adam, 8 = dabs, 9 = some girl, 10 = rainbow
      background = (int)(Math.random()*11);
   
      if(background == 2){
         for(int i = 0; i < 200; i++){
            randomNumber = (int)(Math.random()*500);
            starX[i] =  randomNumber;
            randomNumber = (int)(Math.random()*500);
            starY[i] =  randomNumber;
         }
      }
   
      //choosing which direction computer starts in
      if(!twoPlayer){
         randomNumber = (int)(Math.random()*2);
         if(randomNumber == 0)
            up2 = true;
         if(randomNumber == 1)
            left2 = true;      
      }
      //two player will always have player 2 going up to start
      else
         up2 = true;
      //resets array 
      for(int a = 0; a < 500; a++) {
         for(int b = 0; b < 520; b++) {
            checkFull[a][b] = "empty";
         }
      }
      //have crashing sound here so I only need this line once instead of every time checks for a crash
      if(score1 > 0 || score2 > 0){
         Thread sound = new Thread(mp1);
         sound.start();
      }
      else{
         go = true;
      }
   }
    
   private void addComponentsToPane() { 
      typingArea = new JTextField(20);
      typingArea.addKeyListener(this);
       
      displayArea = new JTextArea();
      displayArea.setEditable(false);
      if(go)
         getContentPane().add(typingArea, BorderLayout.PAGE_START);
   }
   
   public void stateChanged(ChangeEvent evt)
   {
      //radio button and slider change listener actions
      if(evt.getSource().equals(p1) || evt.getSource().equals(p2))
      {
         //if they change the radio button, it sets the colour for the player
         if(p1.isSelected())
         {
            rSlider.setValue(r1);
            gSlider.setValue(g1);
            bSlider.setValue(b1);
            colourPanel.setBackground(c1);
         }
         else if(p2.isSelected())
         {
            rSlider.setValue(r2);
            gSlider.setValue(g2);
            bSlider.setValue(b2);
            colourPanel.setBackground(c2);
         }
      }
      else
      {
         //changes colour of each user
         if(p1.isSelected())
         {
            r1 = rSlider.getValue();
            g1 = gSlider.getValue();
            b1 = bSlider.getValue();
            c1 = new Color(r1,g1,b1);
            colourPanel.setBackground(c1);  
         }
         else if(p2.isSelected())
         {
            r2 = rSlider.getValue();
            g2 = gSlider.getValue();
            b2 = bSlider.getValue();
            c2 = new Color(r2,g2,b2);
            colourPanel.setBackground(c2);
         }
         speed = 26-speedSlider.getValue();
         maxScore = maxSlider.getValue();
      }
      //checks if it is one or two player and changes slider label
      if(oneP.isSelected()){
         p2.setText("Computer");
         twoPlayer = false;
         diffSlider.setEnabled(true);
         difficulty = 6-diffSlider.getValue();
      }
      else if(twoP.isSelected()){
         p2.setText("Player 2");
         twoPlayer = true;
         diffSlider.setEnabled(false);
      }
   }
   
   public void actionPerformed(ActionEvent evt)
   {
      //starts game when user hits go button
      go = true;
      //closes start screen
      frame.dispose();
      frame = new Tron("Tron");
      //starts thread for movement
      Thread t = new Thread(frame);
      t.start();
   }
   
   private class GraphicsPanel extends JPanel 
   {
      public void paintComponent(Graphics g) 
      {
         Graphics2D g2d = (Graphics2D) g;
         g2d.setFont(f);
         super.paintComponent(g);
         //colours background
         if(background == 0){
            g2d.setPaint(Color.black);
            g2d.fillRect(0,0,520,520);
         }
         else if(background == 1){
            g2d.setPaint(new Color(0,0,160));
            g2d.fillRect(0,0,500,500);
            g2d.setPaint(new Color(192,192,192));
            for(int i = 0; i < 25; i++){
               g2d.drawLine(i*25,0,i*25,520);
               g2d.drawLine(0,i*25,520,i*25);
            }
         }
         else if(background == 2){
            g2d.setPaint(Color.black);
            g2d.fillRect(0,0,520,520);
            
            for(int i = 0; i < 200; i++){
               g2d.setPaint(Color.blue);
               g2d.fillOval(starX[i]+i%2,starY[i]+i%3,6,6);
               g2d.setPaint(new Color(253,253,111));
               g2d.fillOval(starX[i],starY[i],3,3);
            }
         }
         else if(background == 3){
            try{
               g2d.drawImage(image1,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 4){
            try{
               g2d.drawImage(image2,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 5){
            try{
               g2d.drawImage(image3,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 6){
            try{
               g2d.drawImage(image4,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 7){
            try{
               g2d.drawImage(image5,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 8){
            try{
               g2d.drawImage(image6,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 9){
            try{
               g2d.drawImage(image7,0,0,520,520,null);
            }
            catch(Exception exc){
            }
         }
         else if(background == 10){
            g2d.setPaint(redToOrange); g2d.fillRect(0,0,520,104);
            g2d.setPaint(orangeToYellow); g2d.fillRect(0,104,520,208);
            g2d.setPaint(yellowToGreen); g2d.fillRect(0,208,520,312);
            g2d.setPaint(greenToBlue); g2d.fillRect(0,312,520,416);
            g2d.setPaint(blueToPurple); g2d.fillRect(0,416,520,520);
         }
         //redraws line where player has gone and a different colour depending on user
         for (int a = 0; a < 500; a++) {
            for (int b = 0; b < 500; b++) {
               if (checkFull[a][b].equals("1")) {
                  g2d.setPaint(c1);
                  g2d.drawRect(a,b,5,5);
               }
               if (checkFull[a][b].equals("2")) {
                  g2d.setPaint(c2);
                  g2d.drawRect(a,b,5,5);
               }
            }
         }
         //draws score
         if(background == 9){
            g2d.setPaint(Color.black);
         }
         else{
            g2d.setPaint(Color.white);
         }
         g2d.drawString("P1: "+score1,40,20);
         g2d.drawString("P2: "+score2,100,20);
         //tells them to hit space to continue
         if(!go && score1 != maxScore && score2 != maxScore){
            try{
               Thread.sleep(100);
            }
            catch(Exception exc){
            }
            g2d.drawString("Hit space to continue",50,460);
         }
         //says who wins if someone reaches the max score chosen on starting screen
         if(score1 == maxScore)
         {
            g2d.setFont(f2);
            g2d.drawString("Player 1 wins!",100,250);
         }
         else if(score2 == maxScore)
         {
            g2d.setFont(f2);
            g2d.drawString("Player 2 wins!",100,250);
         }
         //draws number of boosts each player has remaining
         g2d.setPaint(c1);
         for(int i = 0; i < remainingBoost1; i++){
            g2d.fillRect(70+i*10,20,9,20);
         }
         g2d.setPaint(c2);
         for(int i = 0; i < remainingBoost2; i++){
            g2d.fillRect(130+i*10,20,9,20);
         }
      }
   }
   
   public void keyPressed(KeyEvent e)
   {
      //finds which key has been pressed to change direction of each player, checks if that position has been filled already, and will give points if player crashed
      int id = e.getKeyCode();
      if(score1 < maxScore && score2 < maxScore && go)
      {
         if (id == 37 && !right && !left) {
            down = false;
            right = false;
            up = false;
            left = true; 
         } 
         else if (id == 38 && !down && !up) {
            down = false;
            right = false;
            up = true;
            left = false;
         } 
         else if (id == 39 && !left && !right) {
            down = false;
            right = true;
            up = false;
            left = false;   
         } 
         else if (id == 40 && !up && !down) {
            down = true;
            right = false;
            up = false;
            left = false;       
         }//shift
         else if(id == 16){
            if(remainingBoost1 > 0)
               boost1 = true;
            remainingBoost1--;
         }
         //only allows keys to change player 2's direction if two player is enabled
         if(twoPlayer)
         {
            if (id == 65 && !left2 && !right2) {
               down2 = false;
               right2 = false;
               up2 = false;
               left2 = true;  
            } 
            else if (id == 87 && !up2 && !down2) {
               down2 = false;
               right2 = false;
               up2 = true;
               left2 = false;
            } 
            else if (id == 68 && !right2 && !left2) {
               down2 = false;
               right2 = true;
               up2 = false;
               left2 = false;    
            } 
            else if (id == 83 && !down2 && !up2) {
               down2 = true;
               right2 = false;
               up2 = false;
               left2 = false;        
            }//e
            else if(id == 69){
               if(remainingBoost2 > 0)
                  boost2 = true;
               remainingBoost2--;
            }
         }
      }
      //pausing between rounds
      else if(!go && id == 32){
         try{
            Thread.sleep(500);
         }
         catch(Exception exc)
         {}
         go = true;
      }
      //clears text box every time something is typed
      typingArea.setText("");
      repaint();
   }

   @Override
   public void keyReleased(KeyEvent e)
   {
   
   }

   @Override
   public void keyTyped(KeyEvent e)
   {
   
   }
    
   public static void main (String[] args)
   {
      mp1.setVolume(2.0f);
      Thread bgSound = new Thread(mp1);
      bgSound.start();
      try{
    	  controls = ImageIO.read(new File("resources/controls.jpg"));
          image1 = ImageIO.read(new File("resources/background1.jpg"));
          image2 = ImageIO.read(new File("resources/background2.jpg"));
          image3 = ImageIO.read(new File("resources/background3.jpg"));
          image4 = ImageIO.read(new File("resources/background4.jpg"));
          image5 = ImageIO.read(new File("resources/background5.jpg"));
          image6 = ImageIO.read(new File("resources/background6.jpg"));
          image7 = ImageIO.read(new File("resources/background7.jpg"));
      }
      catch(Exception exc){
      }
      frame = new Tron("Start Screen");
   }
   //makes new threads for both players
   public void run(){
      new Thread(
            new Runnable(){
               public void run(){
                  run1();
               }
            }
         ).start();
      new Thread(
            new Runnable(){
               public void run(){
                  run2();
               }
            }
         ).start();
   }
   //run method for first player
   public void run1(){
      while(score1 < maxScore && score2 < maxScore)
      {
         if(go)
         {
            if(up)
               y--;
            if(down)
               y++;
            if(right)
               x++;
            if(left)
               x--;
            try{
               if(!checkFull[x][y].equals("empty"))
               {
                  score2++;
                  fillGrid();
                  go = false;
               }
               else
                  checkFull[x][y] = "1";
            }
            catch(Exception e)
            {
               score2++;
               fillGrid();
               go = false;
            }
            try{
               //if the player uses a boost, it decreases how long it sleeps for
               if(boost1){
                  Thread.sleep((long)(speed/2));
                  bCounter1++;
                  if(bCounter1 > 200){
                     boost1 = false;
                     bCounter1 = 0;
                  }
               }
               else
                  Thread.sleep(speed);
            }
            catch(Exception exc){
            }
         } 
         //repaints screen and pauses for a short time based on chosen speed from main screen
         repaint();
      } 
   }
   //run method for second player
   public void run2(){
      while(score1 < maxScore && score2 < maxScore)
      {
         if(go)
         {
            //allows player 2 to move if two player was chosen
            if(twoPlayer)
            {
               if(up2)
                  y2--;
               if(down2)
                  y2++;
               if(right2)
                  x2++;
               if(left2)
                  x2--;
               try{
                  if(!checkFull[x2][y2].equals("empty"))
                  {
                     score1++;
                     fillGrid();
                     go = false;
                  }
                  else 
                  {
                     checkFull[x2][y2] = "2";
                  }
               }
               catch(Exception e)
               {
                  score1++;
                  fillGrid();
                  go = false;
               }
            }
            //moves player 2 if single player was chosen
            else if(!twoPlayer)
            {
               //checks if computer should change direction
               if(up2){
                  y2--;
                  try{
                     if(!checkFull[x2][y2-difficulty].equals("empty") || !checkFull[x2][y2-difficulty*2].equals("empty") || !checkFull[x2][y2-difficulty*3].equals("empty"))
                        cpuChange = true;
                  }
                  catch(Exception e){
                     cpuChange = true;
                  }
               }
               if(down2){
                  y2++;
                  try{
                     if(!checkFull[x2][y2+difficulty].equals("empty") || !checkFull[x2][y2+difficulty*2].equals("empty") || !checkFull[x2][y2-difficulty*3].equals("empty"))
                        cpuChange = true;
                  }
                  catch(Exception e){
                     cpuChange = true;
                  }
               }
               if(right2){
                  x2++;
                  try{
                     if(!checkFull[x2+difficulty][y2].equals("empty") || !checkFull[x2+difficulty*2][y2].equals("empty") || !checkFull[x2+difficulty*3][y2].equals("empty"))
                        cpuChange = true;
                  }
                  catch(Exception e){
                     cpuChange = true;
                  }
               }
               if(left2){
                  x2--;
                  try{
                     if(!checkFull[x2-difficulty][y2].equals("empty")  || !checkFull[x2-difficulty*2][y2].equals("empty") || !checkFull[x2-difficulty*3][y2].equals("empty"))
                        cpuChange = true;
                  }
                  catch(Exception e){
                     cpuChange = true;
                  }
               }
               //randomly changes direction even if no line/edge is near it
               randomNumber = (int)(Math.random()*(800/(7-difficulty)));
               if(randomNumber == 1){ 
                  cpuChange = true;
               }
               while(cpuChange){
                  //decides which direction the computer will turn
                  if(!right2 && !left2 && (x2 < 250 || !checkFull[x2-difficulty][y2].equals("empty"))){
                     left2 = false;
                     down2 = false;
                     right2 = true;
                     up2 = false;
                     cpuChange = false;
                  }
                  else if(!right2 && !left2 && (x2 > 250 || !checkFull[x2+difficulty][y2].equals("empty"))){
                     left2 = true;
                     down2 = false;
                     right2 = false;
                     up2 = false;
                     cpuChange = false;
                  }
                  else if(!up2 && !down2 && (y2 < 250 || !checkFull[x2][y2-difficulty].equals("empty"))){
                     left2 = false;
                     down2 = true;
                     right2 = false;
                     up2 = false;
                     cpuChange = false;
                  }
                  else if(!up2 && !down2 && (y2 > 250 || !checkFull[x2][y2+difficulty].equals("empty"))){
                     left2 = false;
                     down2 = false;
                     right2 = false;
                     up2 = true;
                     cpuChange = false;
                  }
                  //if none of the above options is good, it just does something random
                  if(cpuChange){
                     randomNumber = (int)(Math.random()*4);
                     if(randomNumber == 0){
                        if(!right2 && !left2 && checkFull[x2-difficulty][y2].equals("empty")){
                           left2 = true;
                           down2 = false;
                           right2 = false;
                           up2 = false;
                           cpuChange = false;
                        }
                     }
                     if(randomNumber == 1){
                        if(!left2 && !right2 && checkFull[x2+difficulty][y2].equals("empty")){
                           left2 = false;
                           down2 = false;
                           right2 = true;
                           up2 = false;
                           cpuChange = false;
                        }
                     }
                     if(randomNumber == 2){
                        if(!down2 && !up2 && checkFull[x2][y2-difficulty].equals("empty")){
                           left2 = false;
                           down2 = false;
                           right2 = false;
                           up2 = true;
                           cpuChange = false;
                        }
                     }
                     if(randomNumber == 3){
                        if(!up2 && !down2 && checkFull[x2][y2+difficulty].equals("empty")){
                           left2 = false;
                           down2 = true;
                           right2 = false;
                           up2 = false;
                           cpuChange = false;
                        }
                     }
                  }
               }
               //checks if computer lost
               try{
                  if(!checkFull[x2][y2].equals("empty"))
                  {
                     score1++;
                     fillGrid();
                     go = false;
                  }
                  else 
                  {
                     checkFull[x2][y2] = "2";
                  }
               }
               catch(Exception e)
               {
                  score1++;
                  fillGrid();
                  go = false;
               }
            }
            try
            {
               //if the player uses a boost, it decreases how long it sleeps for
               if(boost2){
                  Thread.sleep((long)(speed/2));
                  bCounter2++;
                  if(bCounter2 > 200){
                     boost2 = false;
                     bCounter2 = 0;
                  }
               }
               else
                  Thread.sleep(speed);
            } 
            catch (Exception exc){
            }
         }
         //repaints screen and pauses for a short time based on chosen speed from main screen
         repaint();
      }
   }
}