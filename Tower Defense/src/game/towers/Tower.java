/**
 * This superclass creates and allows for interactions and use of tower objects.
 *
 * @author Miles Bovero, Kirt Robinson
 * @version December 5, 2022
 */
package game.towers;

import game.Clickable;
import game.Control;
import game.GameObject;
import game.State;
import game.gui.TowerMenu;

import java.awt.*;
import java.awt.image.BufferedImage;

abstract public class Tower extends GameObject implements Clickable
{
    protected boolean isMoving;                 // Whether the tower is being moved or not
    protected int x;                            // The tower's x position
    protected int y;                            // The tower's y position
    protected double fireRate;                  // The rate at which the tower fires projectiles
    protected double range;                     // The range that the tower can shoot within (diameter)
    protected String spriteFile;                // The name of the file to be used as the enemy's sprite
    private double timeToNextProjectileCycle;   // Used to track projectile shooting
    protected PurchaseTower purchaseTower;      // The tower purchase button related to this tower
    protected String towerName;
    protected boolean[] upgrades = new boolean[] {false, false, false};
    protected int totalUpgrades = 0;
    protected int shots;
    protected int shotType;

    //Constructor
    public Tower(State state, Control control, boolean isMoving, PurchaseTower purchaseTower)
    {
        this.state = state;
        this.control = control;
        this.purchaseTower = purchaseTower;
        isVisible = true;
        isExpired = false;
        this.isMoving = isMoving;
        timeToNextProjectileCycle = fireRate;
    }

    @Override
    public void update(double elapsedTime)
    {
        // Moves the tower with the mouse for initial placement
        if (isMoving)
        {
            x = control.getMouseX();
            y = control.getMouseY();
        }
        // Shoot projectile based on fire rate
        timeToNextProjectileCycle -= elapsedTime;
        if (!isMoving && timeToNextProjectileCycle <= 0 && !state.getGameOver())
        {
            if (state.findNearestEnemy(new Point(x, y), range) != null)           // If there is an enemy to shoot...
            {
                shoot(shotType, shots);
                timeToNextProjectileCycle = fireRate;
            }
        }
        customUpdate();
    }

    /**
     * Method that draws the Tower object.
     *
     * @param g a graphics object to draw onto
     */
    @Override
    public void draw(Graphics g)
    {

        // Load tower image
        BufferedImage image = control.loadImage(spriteFile);
        // Draw the range of the tower during placement
        if (isMoving) {
            Point p = state.findNearestPathPoint(new Point(x, y));
            double distance = Math.sqrt(Math.pow((p.x - x), 2) + Math.pow((p.y - y), 2));
            if (distance < 33) {
                g.setColor(new Color(255, 40, 40, 146));}
            else {
                g.setColor(new Color(76, 76, 76, 146));}
            g.fillOval(x - (int) range / 2, y - (int) range / 2, (int) range, (int) range);
        }

        // Draw tower centered on x/y position
        g.drawImage(image, x-(image.getWidth()/2), y-(image.getHeight()/2), null);


        // Display tower's distance from path
//        if (isMoving)
//        {
//            Point p = state.findNearestPathPoint(new Point(x,y));
//            g.setColor(Color.blue);
//            g.drawLine(p.x,p.y,x,y);
//        }
    }

    /**
     * Method that allows for the placement of the tower by updating the
     * isMoving value to false when it is "placed".
     *
     * @param mouseX the mouse's x position
     * @param mouseY the mouse's y position
     * @return boolean value that states if the object is moving
     */
    @Override
    public boolean consumeClick(int mouseX, int mouseY)
    {
        Point p = state.findNearestPathPoint(new Point(x,y));
        double distance = Math.sqrt(Math.pow((p.x - x),2) + Math.pow((p.y - y),2));
        double nearTower = Math.sqrt(Math.pow((x - mouseX),2) + Math.pow((y - mouseY),2));

        if (isMoving &&
            mouseX < 600 && mouseY < 600 &&                 // Restrict placement to game area
            distance > 33)                                  // Restrict placement from path
        {
            isMoving = false;                               // Stop moving the tower
            state.changeMoney(-purchaseTower.getCost());    //Changes the current money held
            purchaseTower.changeCost(1.1);           //Changes the associated PurchaseTower's cost
            control.setPlacingTower(false);
            return true;
        }
        else if (!isMoving && mouseX < 600 && mouseY < 600 &&                 // Restrict placement to game area
                nearTower < 15 && state.getTowerMenu() == false)
        {
            state.addGameObject(new TowerMenu(this, state, control));   //Add the TowerMenu to display
            state.setTowerMenu(true);                                         //Tell the game we are in the TowerMenu
            return true;
        }
        return false;
    }

    /**
     * Method to return the moving state of the Tower object.
     * @return
     */
    public boolean getMoving (){return isMoving;}

    /**
     * Method that removes the Tower object from play.
     */
    public void trash ()
    {
        isMoving = false;
        isVisible = false;
        isExpired = true;
    }

    /**
     * A method that determines what projectile is
     * shot from the tower.
     */
    abstract public void shoot (int version, int shots);

    /**
     * Accessor method to return the name of the current tower.
     *
     * @return Name of the current tower
     */
    public String getTowerName () {return towerName;}

    /**
     * An accessor method that returns the boolean state of a selected upgrade.
     *
     * @return boolean value relating to having an upgrade
     */
    public boolean getUpgrades (int upgrade) {return upgrades[upgrade];}

    /**
     * A method that changes the state of the upgrades on this Tower object.
     *
     * @param upgrade which upgrade to change
     * @param value what state to set the upgrade to
     */
    abstract public void setUpgrades (int upgrade, boolean value);

    /**
     * Collects the specific upgrade information from a chosen place
     * inside the information String[].
     *
     * @param upgrade which upgrade currently selecting
     * @param info the place that holds a certain piece of information to call to
     * @return
     */
    abstract public String getUpgradeInfo (int upgrade, int info);

    /**
     * Abstract method to execute an individual tower's updating
     * without losing general updating functionality.
     */
    abstract public void customUpdate ();

    /**
     * Accessor method that returns the total amount of
     * upgrades a tower currently has.
     *
     * @return an int amount of upgrades
     */
    public int getTotalUpgrades () {return totalUpgrades;}
}
