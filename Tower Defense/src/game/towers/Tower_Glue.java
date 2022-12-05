/**
 * This class holds the information for interacting with and displaying
 * Glue Tower objects.
 *
 * @author Miles Bovero, Kirt Robinson
 * @version December 3, 2022
 */
package game.towers;

import game.Control;
import game.State;

public class Tower_Glue extends Tower
{
    //Constructor
    public Tower_Glue(State state, Control control, boolean isMoving, PurchaseTower purchaseTower)
    {
        super(state, control, isMoving, purchaseTower);
        this.fireRate = 1.5;
        this.spriteFile = "glue_bottle.png";
        this.range = 150;
    }

    @Override
    public void shoot() {state.addGameObject(new Projectile_Glue(state, control, x, y));}
}
