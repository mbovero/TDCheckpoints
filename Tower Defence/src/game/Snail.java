/**
 * This class creates and allows for interactions and use of a Snail object.
 *
 * @author Miles Bovero, Kirt Robinson
 * @version November 14, 2022
 */
package game;

import java.awt.Graphics;
import java.awt.Point;

public class Snail extends GameObject{

    private double percentage;
        
    public Snail(State state, Control control) 
    {
        percentage = 0;
        isVisible = true;
        isExpired = false;
        this.state = state;
        this.control = control;
    }

	@Override
	public void update(double elapsedTime) 
	{
        if (percentage >= 1)
        {
        	isExpired = true;
        	isVisible = false;
        	state.changeHealth(-1);								// Remove health
            state.addGameObject(new Snail(state, control));  	// Add another enemy
            //System.out.println(state.health);					// Testing for properly subtracting health
        }
		
		percentage += 0.0025;
	}

	@Override
	public void draw(Graphics g) 
	{
        Point loc = control.getPath().convertToCoordinates(percentage);
        g.drawImage(control.loadImage("snail.png"), loc.x, loc.y, null);
    }
}