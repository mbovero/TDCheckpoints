package game;

import java.awt.*;
import java.awt.image.BufferedImage;

public class FlyingSalt extends GameObject
{
    private double xVelocity;
    private double yVelocity;
    private double xPos;
    private double yPos;

    public FlyingSalt (State state, Control control, int x, int y, Enemy e)
    {
        isVisible = true;
        isExpired = false;
        this.state = state;
        this.control = control;

        xPos = x;
        yPos = y;
        xVelocity = -(x - e.getPosition().x);
        yVelocity = -(y - e.getPosition().y);
        System.out.println("Salt shot with: " + xVelocity + " " + yVelocity);
        //System.out.println("Shooting at the enemy at: " + e.getPosition().x + " " + e.getPosition().y + " " + e);
    }

    @Override
    public void update(double elapsedTime)
    {
        if (xPos > 600 || yPos > 600 ||
            xPos < 0 || yPos < 0)
        {
            this.isExpired = true;
            this.isVisible = false;
        }

        xPos += xVelocity * elapsedTime;
        yPos += yVelocity * elapsedTime;

        Enemy e = state.findNearestEnemy(new Point((int)xPos, (int)yPos));
        if (e != null)
        {
            double distance = Math.sqrt(Math.pow((e.getPosition().x - xPos), 2) + Math.pow((e.getPosition().y - yPos), 2));
            if (distance < 50) {
                e.isExpired = true;
                e.isVisible = false;
                this.isExpired = true;
                this.isVisible = false;
                state.changeScore(1);
                state.changeMoney(5);
            }
        }
    }

    @Override
    public void draw(Graphics g)
    {
        BufferedImage image = control.loadImage("salt_crystals.png");
        g.drawImage(image, (int)xPos-(image.getWidth()/2), (int)yPos-(image.getHeight()/2), null);
    }
}
