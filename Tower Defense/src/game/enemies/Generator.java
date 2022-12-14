/**
 * This superclass creates and allows for the systematic generation of enemy objects.
 *
 * @author Miles Bovero, Kirt Robinson
 * @version December 3, 2022
 */
package game.enemies;

import game.Control;
import game.GameObject;
import game.State;

import java.awt.*;

abstract public class Generator extends GameObject {

    private double timeToNextCycle;                         // The time until the next spawn cycle
    protected double frequency;                             // The time interval (in seconds) that the enemy is spawned in
    protected double initialDelay;                          // The time (in seconds) before the enemy begins spawning
    protected State state;
    protected Control control;

    // Constructor
    public Generator (State state, Control control)
    {
        this.state = state;
        this.control = control;
        this.isExpired = false;
        this.isVisible = false;
        timeToNextCycle = frequency;
    }

    /**
     * A method that controls the cycle in which enemies are spawned,
     * its delay, and the increasing frequency of enemy generation.
     *
     * @param elapsedTime the time elapsed since the last frame
     */
    @Override
    public void update(double elapsedTime)
    {
            if (state.getTotalTime() > initialDelay)
        {
            doGenerationCycle(elapsedTime);
        }
    }

    /**
     * The default method used to systematically generate enemies.
     *
     * @param elapsedTime the time elapsed since the last frame
     */
    public void doGenerationCycle(double elapsedTime)
    {
        timeToNextCycle -= elapsedTime;
        if (timeToNextCycle <= 0 && !state.getGameOver())
        {
            generate();
            timeToNextCycle += frequency;
        }
    }

    /**
     *  A method that generates an enemy that
     *  is specified by the generator being used.
     */
    abstract public void generate();
}
