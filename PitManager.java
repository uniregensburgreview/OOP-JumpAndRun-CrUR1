import de.ur.mi.graphics.Color;
import de.ur.mi.graphics.Rect;
import de.ur.mi.util.RandomGenerator;

/**
 * This class manages the obstacles that are represented as pits. The rects (pits) are moving horizontal from the right
 * canvas border to the left.
 * It offers the methods to create, move and draw these pits and to check a possible collision with the player.
 */
public class PitManager {

    /* Min and Max width of the pits */
    
    /* Woher ist der Wert 800?
    * Du solltest keine Magic Numbers verwenden. Da du bereits eine Config-Klasse hast, in der die Breite des Canvas gespeichert ist,
    * solltest du diese auch verwenden.
    * Außerdem ändern sich die Werte nicht, weshalb du sie als Konstanten initialisieren solltest:
    *
    * private static final int MIN_WIDTH = Configuration.CANVAS_WIDTH / 10;
    * private static final int MAX_WIDTH = Configuration.CANVAS_WIDTH / 6;
   *
    */ 
    private int minWidth = 800 / 10;
    private int maxWidth = 800 / 6;

    private Rect pit;
    private RandomGenerator random;
    private double dx; //horizontal speed of the pit
    private double dy; // vertical speed of the pit
    private Counter counter;
 
    // Variable wird nicht benötigt. Siehe Anmerkung dazu in der checkIfPlayerFallsInPit-Methode
    private boolean fallsInPit;

    public PitManager() {
        initRandomGenerator();
        // keine Magic Numbers
        int minSpeed = -6;
        int maxSpeed = -8;
        initSpeed(minSpeed, maxSpeed);
        newPit();
        // keine Magic Numbers
        counter = new Counter(10, 50);
    }

    /* Creates a new pit every time the current pit is disappearing of the very left end of the Canvas */
    public void createNewPit() {
        if(pit.getRightBorder() <= 0) {
            newPit();
        }
    }

    /* Creates an obstacle for the jumper represented as a pit with a random width */
    private void newPit() {
        double xPos = Configuration.GROUND_WIDTH;
        double yPos = Configuration.GROUND_Y_POS;
        int width = random.nextInt(minWidth, maxWidth);
        int height = Configuration.GROUND_HEIGHT + 10;
        Color color = Configuration.BACKGROUND_COLOR;
        pit = new Rect(xPos, yPos, width, height, color);
    }

    /* RandomGenerator is used for the width and speed of the pits */
    private void initRandomGenerator() {
        random = RandomGenerator.getInstance();
    }


    private void initSpeed(double minSpeed, double maxSpeed) {
        dx = random.nextDouble(minSpeed, maxSpeed);
        dy = 0;
    }

    /* Is moving the pits horizontal to create a running effect of the player */
    public void update() {
        pit.move(dx, dy);
    }


    public void draw() {
        pit.setBorderColor(Configuration.BACKGROUND_COLOR);
        pit.draw();
        counter.draw();
    }

    /** Checks the collision of a pit with a player. The player collides with the pit when the player is completely
     * above the pit and the height of the player is equalling the ground height. In the case of a collision the
     * pits stop moving and the Counter is giving back a high score.
     * If the player does not collide with the pit, the counter is adding points.
     */
    
    /** Deine Methode lässt sich vereinfachen. Du musst nicht zuerst eine Variable deklarieren, der du einen Wert zuweist,
    * sondern stattdessen solltest du es lieber so machen:
    * if(playerBounds.getLeftBorder() >= pit.getLeftBorder() && playerBounds.getRightBorder() <= pit.getRightBorder()
                && playerBounds.getBottomBorder() >= Configuration.GROUND_Y_POS) {
    *        dx = 0;
    *        counter.highScore();
    *        return true;
    *   }
    *    if (playerBounds.getLeftBorder() >= pit.getLeftBorder() && playerBounds.getRightBorder() <= pit.getRightBorder()
    *            && playerBounds.getBottomBorder() <= Configuration.GROUND_Y_POS) {
    *        counter.add();
    *    }
    *    return false;
    */
    public boolean checkIfPlayerFallsInPit(Rect playerBounds) {
        fallsInPit = false;
        if(playerBounds.getLeftBorder() >= pit.getLeftBorder() && playerBounds.getRightBorder() <= pit.getRightBorder()
                && playerBounds.getBottomBorder() >= Configuration.GROUND_Y_POS) {
            dx = 0;
            counter.highScore();
            fallsInPit = true;
        }
        if (playerBounds.getLeftBorder() >= pit.getLeftBorder() && playerBounds.getRightBorder() <= pit.getRightBorder()
                && playerBounds.getBottomBorder() <= Configuration.GROUND_Y_POS) {
            counter.add();
        }
        return fallsInPit;
    }
}

