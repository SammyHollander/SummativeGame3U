
import jaco.mp3.player.MP3Player;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.awt.MouseInfo;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 *
 * @author holls9719
 */
public class Test extends JComponent {
    //new class for the attributes that will change depending on the level of monster

    public class monsterAtrib {

        //declare vairables
        public Rectangle maPos;
        public int maSpeedX;
        public int maSpeedY;
        public Color maColor;

        //initalizer
        public monsterAtrib(int x0, int y0, int w, int h, int speedX, int speedY, int[] color) {
            maPos = new Rectangle(x0, y0, w, h);
            maSpeedX = speedX;
            maSpeedY = speedY;
            maColor = new Color(color[0], color[1], color[2]);
        }
    }
    // Height and Width of our game
    static final int WIDTH = 800;
    static final int HEIGHT = 600;
    //Title of the window
    String title = "Adventure Game";
    // sets the framerate and delay for our game
    // you just need to select an approproate framerate
    long desiredFPS = 60;
    long desiredTime = (1000) / desiredFPS;
    // YOUR GAME VARIABLES WOULD GO HERE
    //mouse position
    //mouseX
    int mouseX = MouseInfo.getPointerInfo().getLocation().x;
    //mouseY
    int mouseY = MouseInfo.getPointerInfo().getLocation().y;
    //walls of the dungon
    Rectangle leftWall = new Rectangle(0, 0, 10, HEIGHT);
    Rectangle rightWall = new Rectangle(WIDTH - 10, 0, 10, HEIGHT);
    Rectangle topWall = new Rectangle(0, 0, WIDTH, 10);
    Rectangle bottomWall = new Rectangle(0, HEIGHT - 10, WIDTH, 10);
    //main menu buttons
    Rectangle startButton = new Rectangle(250, 450, 70, 20);
    Rectangle quitButton = new Rectangle(450, 450, 70, 20);
    Rectangle infoButton = new Rectangle(350, 450, 70, 20);
    Rectangle mainMenu = new Rectangle(WIDTH / 2 - 50, HEIGHT - 50, 100, 20);
    Rectangle leftButton = new Rectangle(WIDTH / 2 - 100, HEIGHT - 100, 50, 20);
    Rectangle rightButton = new Rectangle(WIDTH / 2 + 50, HEIGHT - 100, 50, 20);
    //death screen buttons
    Rectangle mainMenu2 = new Rectangle(600, HEIGHT / 2, 100, 20);
    Rectangle quit2 = new Rectangle(600, HEIGHT / 2 + 50, 100, 20);
    //rectangle for mouse position
    Rectangle rectMouse = new Rectangle(mouseX, mouseY, 1, 1);
    //hole the mosters come from
    Rectangle hole = new Rectangle(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
    //new list for monsters
    List<Test.monsterAtrib> mobs = new ArrayList<Test.monsterAtrib>();
    //hero character
    Rectangle hero = new Rectangle(0, 0, 20, 30);
    //rectangle for the magic
    Rectangle magic = new Rectangle(hero.x, hero.y, 5, 15);
    //hero movement
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    //magic fireing
    boolean downPressed;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    //boolean to see if more monsters need to spawn-set false to start
    boolean spawn = false;
    //boolean for first spawn to see if its happened yet when game reset-set to true furst becuase it hasnt happened yet
    boolean firstSpawn = true;
    //end game boolean
    boolean done;
    //death by monsters
    boolean death;
    //death by falling boolean
    boolean falldeath;
    //int for if the game should start or not (because I need three options)
    //set to zero to start
    int start = 0;
    //boolean for information screen-set to false
    boolean info = false;
    //score system for how many monsters you kill
    int Score = 0;
    //for saveing the high scores
    int endScore = 0;
    //boolean to only call the scorekeeper method once per death
    boolean setScore;
    //rule page number(start at 0)
    int ruleNum = 0;
    //magic direction
    int[] xydir = new int[2];
    //XY magic direction-temp setting to zero
    int Xdir = 0;
    int Ydir = 0;
    //magic speedX
    int asX = 4;
    //magic speedY
    int asY = 4;
    //hero speedX
    int hsX = 2;
    //hero speedY
    int hsY = 2;
    //mouse pressed
    boolean lclick;
    //hero health
    int hearts = 3;
    //new counter
    int count = 0;
    //game fonts
    Font text = new Font("Areal", Font.BOLD, 50);
    Font smallText = new Font("Areal", Font.PLAIN, 15);
    //text color
    Color textColor = new Color(42, 92, 17);
    //images
    //hearts
    BufferedImage heart;
    //wizzard character sprite
    BufferedImage WBL;
    BufferedImage WBR;
    BufferedImage WFL;
    BufferedImage WFR;
    //title screen
    BufferedImage Title1;
    //rules
    BufferedImage ruleNum0;
    BufferedImage ruleNum1;
    BufferedImage ruleNum2;
    BufferedImage ruleNum3;
    BufferedImage ruleNum4;
    BufferedImage ruleNum5;
    BufferedImage ruleNum6;
    //game over image
    BufferedImage gameOver;
    //booleans for if the mouse is over buttons on the title screen
    boolean mouseOverStart;
    boolean mouseOverInfo;
    boolean mouseOverQuit;
    boolean mouseOverLeft;
    boolean mouseOverRight;
    boolean mouseOverMain;
    //booleans for it the mouse is over the buttons on the death screen
    boolean mouseOverMain2;
    boolean mouseOverquit2;
    //sound
    MP3Player music = new MP3Player(ClassLoader.getSystemResource("Sound/backgroundMusic.mp3"));

    // GAME VARIABLES END HERE   
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Test() {
        // creates a windows to show my game
        JFrame frame = new JFrame(title);

        // sets the size of my game
        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        // adds the game to the window
        frame.add(this);

        // sets some options and size of the window automatically
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        // shows the window to the user
        frame.setVisible(true);

        // add listeners for keyboard and mouse
        frame.addKeyListener(new Test.Keyboard());
        Test.Mouse m = new Test.Mouse();

        this.addMouseMotionListener(m);
        this.addMouseWheelListener(m);
        this.addMouseListener(m);

        music.setRepeat(true);
        music.play();
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        //if they died(from falling or from lack of life)
        if (death == true || falldeath == true) {
            //clear the screen
            g.clearRect(0, 0, WIDTH, HEIGHT);
            //balck backroud
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            //draw the game over screen
            g.drawImage(gameOver, 100, 100, 400, 400, null);
            //write your score on the game over screen
            //set the text to large text
            g.setFont(text);
            //set the color
            g.setColor(textColor);
            //write the score
            g.drawString("You Killed " + Score + " Monsters", 100, 550);
            //draw the main menu button
            //set color if you're mouseing over it to change
            if (mouseOverMain2 == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverMain2 == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw main menu button
            g.fillRect(mainMenu2.x, mainMenu2.y, mainMenu2.width, mainMenu2.height);
            //draw the quit button
            if (mouseOverquit2 == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverquit2 == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw main menu button
            g.fillRect(quit2.x, quit2.y, quit2.width, quit2.height);

            //draw the button text
            //set font to small text font
            g.setFont(smallText);
            //set the Text color to text color
            g.setColor(textColor);
            g.drawString("Main Menu", mainMenu2.x + 15, mainMenu2.y + 15);
            g.drawString("Quit", quit2.x + 35, quit2.y + 15);
        }
        //before the game starts (title screen)
        if (start == 0) {
            //clear the screen
            g.clearRect(0, 0, WIDTH, HEIGHT);
            //balck backroud
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            //title screen image
            g.drawImage(Title1, WIDTH / 2 - 200, 200, 400, 176, null);
            //set color to green
            g.setColor(textColor);
            //setfont
            g.setFont(text);
            //title Text
            g.drawString("Magic & Monsters", 200, 50);

            //if the mouse is hovering over any of the buttons change the color of that button
            //start button
            if (mouseOverStart == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverStart == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw start button
            g.fillRect(startButton.x, startButton.y, startButton.width, startButton.height);
            if (mouseOverInfo == true) {
                //change button color
                g.setColor(Color.GREEN);
            }
            if (mouseOverInfo == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw the info button
            g.fillRect(infoButton.x, infoButton.y, infoButton.width, infoButton.height);
            //game quit button
            if (mouseOverQuit == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverQuit == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw the quit button
            g.fillRect(quitButton.x, infoButton.y, infoButton.width, infoButton.height);

            //set text color
            g.setColor(textColor);
            //set font
            g.setFont(smallText);
            //button text
            g.drawString("Start", 270, 465);
            g.drawString("Info", 370, 465);
            g.drawString("Quit", 470, 465);
        }
        //if they hit the info screen
        if (info == true) {
            //clear the screen
            g.clearRect(0, 0, WIDTH, HEIGHT);
            //balck backroud
            g.setColor(Color.black);
            g.fillRect(0, 0, WIDTH, HEIGHT);
            //flip page button(s)
            //left button
            //when it is moused over it turns green
            if (mouseOverLeft == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverLeft == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw left button
            g.fillRect(leftButton.x, leftButton.y, leftButton.width, leftButton.height);
            //right button
            //when it is moused over it turns green
            if (mouseOverRight == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverRight == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw right button
            g.fillRect(rightButton.x, rightButton.y, rightButton.width, rightButton.height);
            //main menu button
            //when it is moused over it turns green
            if (mouseOverMain == true) {
                //change button color
                g.setColor(Color.green);
            }
            if (mouseOverMain == false) {
                //set color to white
                g.setColor(Color.white);
            }
            //draw main menu button
            g.fillRect(mainMenu.x, mainMenu.y, mainMenu.width, mainMenu.height);
            //button text
            //set text color
            g.setColor(textColor);
            //set font to small text font
            g.setFont(smallText);
            //draw button text
            //main menu
            g.drawString("Main Menu", WIDTH / 2 - 35, HEIGHT - 35);
            //left
            g.drawString("<--", WIDTH / 2 - 85, HEIGHT - 85);
            //right
            g.drawString("-->", WIDTH / 2 + 65, HEIGHT - 85);

            //rules/backstory
            //the rule number corrosponding to what rule it is drawing
            if (ruleNum == 0) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum0, 50, 100, 700, 250, null);
            }
            if (ruleNum == 1) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum1, 50, 100, 700, 250, null);
            }
            if (ruleNum == 2) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum2, 50, 100, 700, 250, null);
            }
            if (ruleNum == 3) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum3, 50, 100, 700, 250, null);
            }
            if (ruleNum == 4) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum4, 50, 100, 700, 250, null);
            }
            if (ruleNum == 5) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum5, 50, 100, 700, 250, null);
            }
            if (ruleNum == 6) {
                //clear away the previous rule with drawing a black rectangle on top
                g.setColor(Color.black);
                g.fillRect(50, 100, 700, 250);
                g.drawImage(ruleNum6, 50, 100, 700, 250, null);
            }

        }
        //if they start the game
        if (start == 1) {
            // clear the screen
            g.clearRect(0, 0, WIDTH, HEIGHT);

            // GAME DRAWING GOES HERE
            //set the color to black for the walls
            g.setColor(Color.black);
            //draw the left wall
            g.fillRect(leftWall.x, leftWall.y, 10, HEIGHT);
            //draw the right wall
            g.fillRect(rightWall.x, rightWall.y, 10, HEIGHT);
            //draw the top wall
            g.fillRect(topWall.x, topWall.y, WIDTH, 10);
            //draw the bottom wall
            g.fillRect(bottomWall.x, bottomWall.y, WIDTH, HEIGHT - 10);
            //draw the hole
            g.fillRect(hole.x, hole.y, hole.width, hole.height);

            //set the color to text color
            g.setColor(textColor);
            //set the font
            g.setFont(text);
            //draw the score (number of monsters killed
            g.drawString("" + Score, 30, 50);

            //drawing the hero
            //if mashing all the magic keys
            if (!(wPressed == false && sPressed == false && aPressed == false && dPressed == false)) {
                g.drawImage(WFR, hero.x, hero.y, 20, 30, null);
            }
            //if standing still
            if (wPressed == false && sPressed == false && aPressed == false && dPressed == false) {
                g.drawImage(WFR, hero.x, hero.y, 20, 30, null);
            }
            //if moveing forward left
            if (aPressed == true && sPressed == true) {
                g.drawImage(WFL, hero.x, hero.y, 20, 30, null);
            }
            //if moveing forward right
            if (dPressed == true && sPressed == true) {
                g.drawImage(WFR, hero.x, hero.y, 20, 30, null);
            }
            //up and right
            if (dPressed == true && wPressed == true) {
                g.drawImage(WBR, hero.x, hero.y, 20, 30, null);
            }
            //up and left
            if (aPressed == true && wPressed == true) {
                g.drawImage(WBL, hero.x, hero.y, 20, 30, null);
            }
            //if moveing right
            if (wPressed == false && sPressed == false && aPressed == false && dPressed == true) {
                g.drawImage(WFR, hero.x, hero.y, 20, 30, null);
            }
            //if moveing left
            if (wPressed == false && sPressed == false && aPressed == true && dPressed == false) {
                g.drawImage(WFL, hero.x, hero.y, 20, 30, null);
            }
            //if moveing up
            if (wPressed == true && sPressed == false && aPressed == false && dPressed == false) {
                g.drawImage(WBR, hero.x, hero.y, 20, 30, null);
            }

            //drawing the healthbar
            if (hearts == 3) {
                //draw three hearts
                g.drawImage(heart, hero.x - 10, hero.y + 30, 15, 15, null);
                g.drawImage(heart, hero.x + 5, hero.y + 30, 15, 15, null);
                g.drawImage(heart, hero.x + 20, hero.y + 30, 15, 15, null);

            }
            if (hearts == 2) {
                //draw two hearts
                g.drawImage(heart, hero.x - 10, hero.y + 30, 15, 15, null);
                g.drawImage(heart, hero.x + 5, hero.y + 30, 15, 15, null);

            }
            if (hearts == 1) {
                //draw one heart
                g.drawImage(heart, hero.x - 10, hero.y + 30, 15, 15, null);
            }

            //draw the monsters
            for (int i = 0; i < mobs.size(); i++) {
                //set the monster Color
                g.setColor(mobs.get(i).maColor);
                g.fillRect(mobs.get(i).maPos.x, mobs.get(i).maPos.y, mobs.get(i).maPos.width, mobs.get(i).maPos.height);

            }

            //if a key was pressed to fire an magic
            //horozontal shot
            if (leftPressed == true) {
                //red magic
                g.setColor(Color.red);
                g.fillRect(magic.x, magic.y, 15, 5);
            }
            if (rightPressed == true) {
                //red magic
                g.setColor(Color.red);
                g.fillRect(magic.x, magic.y, 15, 5);
            }
            //vertical shot
            if (upPressed == true) {
                //red magic
                g.setColor(Color.red);
                g.fillRect(magic.x, magic.y, 5, 15);
            }
            if (downPressed == true) {
                //red magic
                g.setColor(Color.red);
                g.fillRect(magic.x, magic.y, 5, 15);
            }

            // GAME DRAWING ENDS HERE
        }
    }
// This method is used to do any pre-setup you might need to do
// This is run before the game loop begins!

    public void preSetup() {
        //load images
        heart = loadImage("Images/heart.png");
        WBL = loadImage("Images/WizBackLeft.png");
        WBR = loadImage("Images/WizBackRight.png");
        WFL = loadImage("Images/WizfrontLeft.png");
        WFR = loadImage("Images/WizfrontRight.png");
        Title1 = loadImage("Images/Title1.png");
        //all the rules
        ruleNum0 = loadImage("Images/ruleNum0.png");
        ruleNum1 = loadImage("Images/ruleNum1.png");
        ruleNum2 = loadImage("Images/ruleNum2.png");
        ruleNum3 = loadImage("Images/ruleNum3.png");
        ruleNum4 = loadImage("Images/ruleNum4.png");
        ruleNum5 = loadImage("Images/ruleNum5.png");
        ruleNum6 = loadImage("Images/ruleNum6.png");
        //game ocer screen
        gameOver = loadImage("Images/gameOver.png");
    }

    // The main game loop
    // In here is where all the logic for my game will go
    public void run() {
        // Used to keep track of time used to draw and update the game
        // This is used to limit the framerate later on
        long startTime;
        long deltaTime;

        preSetup();

        // the main game loop section
        // game will end if you set done = false;
        done = false;
        while (!done) {
            // determines when we started so we can keep a framerate
            startTime = System.currentTimeMillis();
            //get the mouse coordinates
            rectMouse.x = mouseX;
            rectMouse.y = mouseY;

            //if they died(from falling or from lack of life)
            if (death == true || falldeath == true) {
                //save the high score to the thing
                endScore = Score;
                //call the scorekeeper method to add to the high score sheet
                if (setScore == true) {
                    scoreKeeper(endScore);
                }
                //when the mouse hovers over the buttons make them change color
                //if the mouse is over the main menu button
                if (rectMouse.intersects(mainMenu2)) {
                    //set the boolean to say the mouse is over the button to true
                    mouseOverMain2 = true;
                    //if they push the main menu button
                    if (lclick == true) {
                        //reset everything
                        //reset highscore
                        endScore = 0;
                        //set start int to 0 to start game
                        start = 0;
                        //set health to full
                        hearts = 3;
                        //set score to zero to reset
                        Score = 0;
                        //reset the hero position
                        hero.x = 20;
                        hero.y = 20;
                        //remove all the monsters from the array list
                        mobs.clear();
                        //reset the death and falldeath booleans to restart game
                        death = false;
                        falldeath = false;
                        //set first spawn to true so the first new mobs spawn
                        firstSpawn = true;
                        //reset the speed of the wizzard and magic
                        //magic speedX
                        asX = 4;
                        //magic speedY
                        asY = 4;
                        //hero speedX
                        hsX = 2;
                        //hero speedY
                        hsY = 2;

                    }
                }
                //if the mouse is not over the main menu button
                if (!(rectMouse.intersects(mainMenu2))) {
                    //set the boolean to say the mouse is over the button to true
                    mouseOverMain2 = false;
                }
                //when the mouse hovers over the quit button make it change color
                if (rectMouse.intersects(quit2)) {
                    //set the boolean to say the mouse is over the button to true
                    mouseOverquit2 = true;
                    //if they push the quit button
                    if (lclick == true) {
                        //end game
                        System.exit(0);
                    }
                }
                //if the mouse is not over the quit button
                if (!(rectMouse.intersects(quit2))) {
                    //keep the button color white 
                    mouseOverquit2 = false;
                }
                // update the drawing (calls paintComponent)
                repaint();
            }
            //if the game is not started yet
            if (start == 0) {
                //when the mouse hovers over the buttons make the button change color
                //if the mouse is over the start button
                if (rectMouse.intersects(startButton)) {
                    //set the mouse over start boolean to true to make the button color change
                    mouseOverStart = true;
                    //if they push start
                    if (lclick == true) {
                        //set start int to 1 to start game
                        start = 1;
                    }
                }
                //if the mouse is not over start
                if (!(rectMouse.intersects(startButton))) {
                    //set the mouse over start boolean to false when not over it
                    mouseOverStart = false;
                }
                //if the mouse is over the info button
                if (rectMouse.intersects(infoButton)) {
                    //set the mouse over info boolean to true to make the button color change
                    mouseOverInfo = true;
                    //if they push info
                    if (lclick == true) {
                        //set the info boolean to true to open info screen
                        info = true;
                    }
                }
                //if the mouse is not over info
                if (!(rectMouse.intersects(infoButton))) {
                    //set the mouse over info boolean to false when not over it
                    mouseOverInfo = false;
                }
                //if the mouse is over the quit button
                if (rectMouse.intersects(quitButton)) {
                    //set the mouse over quit boolean to true to make the button color change
                    mouseOverQuit = true;
                    //if they hit quit
                    if (lclick == true) {
                        //end the game
                        System.exit(0);
                    }
                }
                //if the mouse is not over quit
                if (!(rectMouse.intersects(quitButton))) {
                    //set the mouse over quit boolean to false when not over it
                    mouseOverQuit = false;
                }
                // update the drawing (calls paintComponent)
                repaint();
            }
            //if they pushed the info button
            if (info == true) {

                //if the mouse is over the left button
                if (rectMouse.intersects(leftButton)) {
                    //set the mouse over left boolean to true to make the button color change
                    mouseOverLeft = true;
                    //if they push left
                    if (lclick == true) {
                        //subract one from rule page number
                        ruleNum = ruleNum - 1;
                        //if it would be less than zero, make it zero again
                        if (ruleNum < 0) {
                            ruleNum = 0;
                        }
                        //set the lclick to false again to not spam the thing
                        lclick = false;
                    }
                }
                //if the mouse is not over left
                if (!(rectMouse.intersects(leftButton))) {
                    //set the mouse over left boolean to false when not over it
                    mouseOverLeft = false;
                }
                //if the mouse is over the right button
                if (rectMouse.intersects(rightButton)) {
                    //set the mouse over right boolean to true to make the button color change
                    mouseOverRight = true;
                    //if they push right
                    if (lclick == true) {
                        //add one to rule page number
                        ruleNum = ruleNum + 1;
                        //if it would exceed number of rules, make it last rule number again
                        if (ruleNum > 6) {
                            ruleNum = 6;
                        }
                        //set the lclick to false again to not spam the thing
                        lclick = false;
                    }
                }
                //if the mouse is not over right
                if (!(rectMouse.intersects(rightButton))) {
                    //set the mouse over right boolean to false when not over it
                    mouseOverRight = false;
                }
                //if the mouse is over the main menu button
                if (rectMouse.intersects(mainMenu)) {
                    //set the mouse over main boolean to true to make the button color change
                    mouseOverMain = true;
                    //if they hit main menu
                    if (lclick == true) {
                        //return to main menu
                        info = false;
                    }
                }
                //if the mouse is not over main menu
                if (!(rectMouse.intersects(mainMenu))) {
                    //set the mouse over main boolean to false when not over it
                    mouseOverMain = false;
                }
                // update the drawing (calls paintComponent)
                repaint();
            }
            //if they started the game
            if (start == 1) {
                //spawn first round if it hasnt spawned anything yet
                if (firstSpawn == true) {
                    spawn = true;
                }
                //check for collisions
                collisions();

                //leveling up character(increasing speed)-max of 10 level ups
                for (int i = 0; i < 10; i++) {
                    //every thrid monster killed = level up
                    if (Score == 3 * i && spawn == true) {
                        //increase the speed of magic
                        asX = asX + 1;
                        asY = asY + 1;
                        //increase the speed of player
                        hsX = hsX + 1;
                        hsY = hsY + 1;
                    }
                }

                //check if new monsters should spawn
                if (spawn == true) {
                    //call spawning method to add monsters to the Arraylist
                    spawning();
                }

                //setting death boolean
                if (hearts > 0) {
                    death = false;

                }
                //if they reach 0 lives
                if (hearts == 0) {
                    //set death to true
                    death = true;
                    //set setscore to true
                    setScore = true;
                    //set start to 3
                    start = 3;
                }

                //makeing it so the hero can't walk over the walls of the dungeon
                //leftwall
                if (hero.x <= 10) {
                    hero.x = leftWall.x + 10;
                }
                //right wall
                if (hero.x >= rightWall.x - 20) {
                    hero.x = rightWall.x - 20;
                }
                //top wall
                if (hero.y <= 10) {
                    hero.y = topWall.y + 10;
                }
                //bottom wall
                if (hero.y >= bottomWall.y - 30) {
                    hero.y = bottomWall.y - 30;
                }
                //moveing the monsters
                for (int i = 0; i < mobs.size(); i++) {
                    mobs.get(i).maPos.x += mobs.get(i).maSpeedX;
                    mobs.get(i).maPos.y += mobs.get(i).maSpeedY;
                }

                //moveing the hero forward
                if (wPressed) {
                    hero.y = hero.y - hsY;
                }
                //moveing the hero down
                if (sPressed) {
                    hero.y = hero.y + hsY;
                }
                //moveing the hero left
                if (aPressed) {
                    hero.x = hero.x - hsX;
                }
                //moveing the hero right
                if (dPressed) {
                    hero.x = hero.x + hsX;
                }

                //update magic x and y
                //if upwards shot
                if (upPressed) {
                    magic.y = magic.y - asY;
                }
                //if downwards shot
                if (downPressed) {
                    magic.y = magic.y + asY;
                }
                //left shot
                if (leftPressed) {
                    magic.x = magic.x - asX;
                }
                //right shot
                if (rightPressed) {
                    magic.x = magic.x + asX;
                }
                //if none of the magic keys are pressed, reset magic x and y
                if ((upPressed == false && downPressed == false && leftPressed == false && rightPressed == false)) {
                    magic.x = hero.x;
                    magic.y = hero.y;
                }

                // GAME LOGIC ENDS HERE 
                // update the drawing (calls paintComponent)
                repaint();

                // SLOWS DOWN THE GAME BASED ON THE FRAMERATE ABOVE
                // USING SOME SIMPLE MATH
                deltaTime = System.currentTimeMillis() - startTime;
                try {
                    if (deltaTime > desiredTime) {
                        //took too much time, don't wait
                        Thread.sleep(1);
                    } else {
                        // sleep to make up the extra time
                        Thread.sleep(desiredTime - deltaTime);
                    }
                } catch (Exception e) {
                };

            }
        }
    }

    // Used to implement any of the Mouse Actions
    private class Mouse extends MouseAdapter {
        // if a mouse button has been pressed down

        @Override
        public void mousePressed(MouseEvent e) {
            //pushing button1
            if (e.getButton() == MouseEvent.BUTTON1) {
                lclick = true;
            }
        }

        // if a mouse button has been released
        @Override
        public void mouseReleased(MouseEvent e) {
            //releasing button 1
            if (e.getButton() == MouseEvent.BUTTON1) {
                lclick = false;
            }
        }

        // if the scroll wheel has been moved
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
        }

        // if the mouse has moved positions
        @Override
        public void mouseMoved(MouseEvent e) {
            mouseX = e.getX();
            mouseY = e.getY();
        }
    }

// Used to implements any of the Keyboard Actions
    private class Keyboard extends KeyAdapter {
        // if a key has been pressed down

        @Override
        public void keyPressed(KeyEvent e) {
            //hero movement, so the WASD keys can be used to move character
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = true;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = true;
            }

            //if the magic keys are pressed, so they can be used to fire magics
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                //true for only a second
                downPressed = true;

            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                //true for only a second
                upPressed = true;

            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                //true for only a second
                leftPressed = true;

            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                //true for only a seccond
                rightPressed = true;

            }

        }

        // if a key has been released
        @Override
        public void keyReleased(KeyEvent e) {
            //hero movement, so the WASD keys can be used to move character
            if (e.getKeyCode() == KeyEvent.VK_W) {
                wPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_S) {
                sPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_A) {
                aPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_D) {
                dPressed = false;
            }

            //if the magic keys are released
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                downPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                upPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                leftPressed = false;
            }
            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                rightPressed = false;
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // creates an instance of my game
        Test game = new Test();
        // starts the game loop
        game.run();
    }

    //method for doing scores-adding to high score page (needs the end score)
    public void scoreKeeper(int endScore) {
        try {
            //new filewritter and bufferedwriter to append the scores txt
            FileWriter writer = new FileWriter("trial.txt");
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            //add the final score for that game to the text doc
            bufferedWriter.write("boo");
            bufferedWriter.newLine();
            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //set to false so it does not call this method over and over agian
        setScore = false;
    }

    // A method used to load in an image
    // The filname is used to pass in the EXACT full name of the image from the src folder
    // i.e.  images/picture.png
    public BufferedImage loadImage(String filename) {

        BufferedImage img = null;

        try {
            // use ImageIO to load in an Image
            // ClassLoader is used to go into a folder in the directory and grab the file
            img = ImageIO.read(ClassLoader.getSystemResourceAsStream(filename));
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }

        return img;
    }

    //spawning method
    public void spawning() {
        //get a random number for the number of monsters to spawn
        int spawnNum = (int) (Math.random() * (3 - 1 + 1)) + 1;
        //add the monsters to the list of monsterss, equal to the random number selected
        for (int i = 0; i < spawnNum; i++) {
            //randomize the speed
            int speed = (int) (Math.random() * (8 - 1 + 1)) + 1;
            //randomize the size (min of 15, max of 25)
            int size = (int) (Math.random() * (25 - 15 + 15)) + 15;
            //generate a random color by puting 3 random rgb values in an int array
            //new int array for color
            int[] color = new int[3];
            //red value
            color[0] = (int) (Math.random() * (255 - 1 + 1)) + 1;
            //green value
            color[1] = (int) (Math.random() * (255 - 1 + 1)) + 1;
            //blue value
            color[2] = (int) (Math.random() * (255 - 1 + 1)) + 1;

            Test.monsterAtrib mon = new Test.monsterAtrib(WIDTH / 2, HEIGHT / 2, size, size, speed, speed, color);
            //adding new monster to the list
            mobs.add(mon);

        }
        //make sure first spawn is set to false
        firstSpawn = false;
        //set spawn to false
        spawn = false;
    }

    //collision method
    public void collisions() {
        //when the hero hits a monster
        for (int i = 0; i < mobs.size(); i++) {
            if (hero.intersects(mobs.get(i).maPos)) {
                //subract a life
                hearts = hearts - 1;
                //reset monster
                mobs.get(i).maPos.x = WIDTH / 2;
                mobs.get(i).maPos.y = HEIGHT / 2;
                //adds one to score
                Score += 1;
                //spawn more monsters
                spawn = true;

            }
        }
        //when the hero falls in the hole
        if (hero.intersects(hole)) {
            //set falldeath boolean to true
            falldeath = true;
            //set setscore to true
            setScore = true;
            //set start to 3
            start = 3;
        }
        //when the magic hits the walls
        if (magic.intersects(topWall) || magic.intersects(bottomWall) || magic.intersects(leftWall) || magic.intersects(rightWall)) {
            //return magic to hero
            magic.x = hero.x;
            magic.y = hero.y;
        }
        //when the magic hits the monsters
        for (int i = 0; i < mobs.size(); i++) {
            if (magic.intersects(mobs.get(i).maPos)) {
                //return magic to hero
                magic.x = hero.x;
                magic.y = hero.y;
                //add one to score
                Score += 1;
                //reset position
                mobs.get(i).maPos.x = WIDTH / 2;
                mobs.get(i).maPos.y = HEIGHT / 2;
                //spawn more monsters
                spawn = true;

            }
        }

        //collisions for all the monsters with the walls
        //if the monster hits the topwall
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).maPos.intersects(topWall)) {
                //change the velocity
                mobs.get(i).maSpeedY = -1 * (mobs.get(i).maSpeedY);
            }
        }
        //if the monster hits bottom topwall
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).maPos.intersects(bottomWall)) {
                //change the velocity
                mobs.get(i).maSpeedY = -1 * (mobs.get(i).maSpeedY);
            }
        }
        //if the monster hits the right topwall
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).maPos.intersects(rightWall)) {
                //change the velocity
                mobs.get(i).maSpeedX = -1 * (mobs.get(i).maSpeedX);
            }
        }
        //if the monster hits the left wall
        for (int i = 0; i < mobs.size(); i++) {
            if (mobs.get(i).maPos.intersects(leftWall)) {
                //change the velocity
                mobs.get(i).maSpeedX = -1 * (mobs.get(i).maSpeedX);
            }
        }
    }
}
