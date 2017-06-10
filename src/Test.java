
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Font;
import java.util.List;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;

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
    //walls of the dungon
    Rectangle leftWall = new Rectangle(0, 0, 10, HEIGHT);
    Rectangle rightWall = new Rectangle(WIDTH - 10, 0, 10, HEIGHT);
    Rectangle topWall = new Rectangle(0, 0, WIDTH, 10);
    Rectangle bottomWall = new Rectangle(0, HEIGHT - 10, WIDTH, 10);
    //hole the mosters come from
    Rectangle hole = new Rectangle(WIDTH / 2 - 50, HEIGHT / 2 - 50, 100, 100);
    //new list for monsters
    List<Test.monsterAtrib> mobs = new ArrayList<Test.monsterAtrib>();
    //hero character
    Rectangle hero = new Rectangle(0, 0, 20, 20);
    //rectangle for the arrow
    Rectangle arrow = new Rectangle(hero.x, hero.y, 5, 15);
    //hero movement
    boolean wPressed;
    boolean sPressed;
    boolean aPressed;
    boolean dPressed;
    //arrow fireing
    boolean downPressed;
    boolean upPressed;
    boolean leftPressed;
    boolean rightPressed;
    //boolean to see if more monsters need to spawn-set false to start
    boolean spawn = false;
    //end game boolean
    boolean done;
    //death by monsters
    boolean death;
    //death by falling boolean
    boolean falldeath;
    //score system for how many monsters you kill
    int Score = 0;
    //arrow direction
    int[] xydir = new int[2];
    //XY arrow direction-temp setting to zero
    int Xdir = 0;
    int Ydir = 0;
    //arrow speedX
    int asX = 4;
    //arrow speedY
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
    //game font
    Font text = new Font("Areal", Font.BOLD, 50);
    //text color
    Color textColor = new Color(42, 92, 17);

    // GAME VARIABLES END HERE   
    // Constructor to create the Frame and place the panel in
    // You will learn more about this in Grade 12 :)
    public Test() {
//        //initialize the monster atributes (start with two on screen when game loads)
//        for (int i = 0; i < 2; i++) {
//            Test.monsterAtrib mon = new Test.monsterAtrib(WIDTH / 2, HEIGHT / 2, 15, 15, 3 + i, 3 + i);
//            //adding new monster to the list
//            mobs.add(mon);
//        }
        //set spawn to true to spawn the first wave
        spawn = true;
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
    }

    // drawing of the game happens in here
    // we use the Graphics object, g, to perform the drawing
    // NOTE: This is already double buffered!(helps with framerate/speed)
    @Override
    public void paintComponent(Graphics g) {
        // always clear the screen first!
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

        //set the color to blue
        g.setColor(Color.blue);
        //drawing the hero
        g.fillRect(hero.x, hero.y, 20, 20);

        //drawing the healthbar
        if (hearts == 3) {
            //set color to green
            g.setColor(Color.green);
            g.fillRect(hero.x - 5, hero.y + 25, 30, 10);
        }
        if (hearts == 2) {
            //set color to orange
            g.setColor(Color.orange);
            g.fillRect(hero.x - 5, hero.y + 25, 20, 10);
        }
        if (hearts == 1) {
            //set color to red
            g.setColor(Color.red);
            g.fillRect(hero.x - 5, hero.y + 25, 10, 10);
        }
        //if they die from monsters
        if (death == true) {
            //set text color
            g.setColor(textColor);
            //setfont
            g.setFont(text);
            //death message
            g.drawString("And the hero was slain", 30, HEIGHT / 2);
            //end game
            done = true;
        }

        //draw the monsters
        for (int i = 0; i < mobs.size(); i++) {
            //set the monster Color
            g.setColor(mobs.get(i).maColor);
            g.fillRect(mobs.get(i).maPos.x, mobs.get(i).maPos.y, mobs.get(i).maPos.width, mobs.get(i).maPos.height);

        }

        //if the hero falls down the hole (game over)
        if (falldeath == true) {
            //set font color
            g.setColor(textColor);
            //set font
            g.setFont(text);
            //game over screen
            g.drawString("And the hero fell to his death...", 30, HEIGHT / 2);
            //end game
            done = true;
        }

        //if a key was pressed to fire an arrow
        //horozontal shot
        if (leftPressed == true) {
            //red arrow
            g.setColor(Color.red);
            g.fillRect(arrow.x, arrow.y, 15, 5);
        }
        if (rightPressed == true) {
            //red arrow
            g.setColor(Color.red);
            g.fillRect(arrow.x, arrow.y, 15, 5);
        }
        //vertical shot
        if (upPressed == true) {
            //red arrow
            g.setColor(Color.red);
            g.fillRect(arrow.x, arrow.y, 5, 15);
        }
        if (downPressed == true) {
            //red arrow
            g.setColor(Color.red);
            g.fillRect(arrow.x, arrow.y, 5, 15);
        }

        // GAME DRAWING ENDS HERE
    }
// This method is used to do any pre-setup you might need to do
// This is run before the game loop begins!

    public void preSetup() {
        // Any of your pre setup before the loop starts should go here
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

            // all your game rules and move is done in here
            // GAME LOGIC STARTS HERE 
            //check for collisions
            collisions();

            //leveling up character(increasing speed)-max of 10 level ups
            for (int i = 0; i < 10; i++) {
                //every thrid monster killed = level up
                if (Score == 3 * i && spawn == true) {
                    //increase the speed of arrow
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

            //if they fall down the pit, end the game
            if (falldeath == true) {
                done = true;
            }
            //death death boolean
            if (hearts > 0) {
                death = false;
            }
            //if they reach 0 lives
            if (hearts == 0) {
                death = true;
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
            if (hero.y >= bottomWall.y - 20) {
                hero.y = bottomWall.y - 20;
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

            //update arrow x and y
            //if upwards shot
            if (upPressed) {
                arrow.y = arrow.y - asY;
            }
            //if downwards shot
            if (downPressed) {
                arrow.y = arrow.y + asY;
            }
            //left shot
            if (leftPressed) {
                arrow.x = arrow.x - asX;
            }
            //right shot
            if (rightPressed) {
                arrow.x = arrow.x + asX;
            }
            //if none of the arrow keys are pressed, reset arrow x and y
            if ((upPressed == false && downPressed == false && leftPressed == false && rightPressed == false)) {
                arrow.x = hero.x;
                arrow.y = hero.y;
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

            //if the arrow keys are pressed, so they can be used to fire arrows
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

            //if the arrow keys are released
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
        //set spawn to false again
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
        }
        //when the arrow hits the walls
        if (arrow.intersects(topWall) || arrow.intersects(bottomWall) || arrow.intersects(leftWall) || arrow.intersects(rightWall)) {
            //return arrow to hero
            arrow.x = hero.x;
            arrow.y = hero.y;
        }
        //when the arrow hits the monsters
        for (int i = 0; i < mobs.size(); i++) {
            if (arrow.intersects(mobs.get(i).maPos)) {
                //return arrow to hero
                arrow.x = hero.x;
                arrow.y = hero.y;
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
