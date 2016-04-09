package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.game_objects.Character;

import java.util.List;

/**
 * Created by Christopher on 3/9/2016.
 */
public class Seek {

    private static final float SPEED_FACTOR = 5;
    private static boolean done = false;

    public static void seek(Character character, Vector2 targetPosition, List objects) {

        Vector2 characterPosition = new Vector2(character.getPosition());
        characterPosition.add(character.getWidth() / 2, character.getHeight() / 2);
        done = false;

        if (Math.abs(characterPosition.dst2(targetPosition)) > 15) {

            //Subtract target position from character's position to get the vector pointing from the character to the target
            //Then normalize the vector to get the direction to the target from the character's position
            //Scale that direction vector by the character's max speed to get the desired velocity
            //Take into account the current velocity of the character so that the net velocity of the character is the desired velocity
            Vector2 desiredVelocity = new Vector2(targetPosition);
            desiredVelocity.sub(characterPosition).nor();
            character.setAngle(calculateAngleToFaceTarget(desiredVelocity));
            desiredVelocity = desiredVelocity.scl(SPEED_FACTOR);
            desiredVelocity = desiredVelocity.sub(character.getVelocity());

            character.move(desiredVelocity, objects);
        }
        else {
            done = true;
        }
    }

    private static float calculateAngleToFaceTarget(Vector2 directionToTarget){

        float angle;
        angle = (float) Math.atan(directionToTarget.y/directionToTarget.x);
        angle = (float) Math.toDegrees(angle);

        //Adjust to quadrant
        //Quadrant 1
        angle = -angle;
        if (directionToTarget.x >= 0 && directionToTarget.y >= 0) {
            return 360 + angle;
        }
        //Quadrant 2
        if (directionToTarget.x <= 0 && directionToTarget.y >= 0) {
            return 180 + angle;
        }
        //Quadrant 3
        if (directionToTarget.x <= 0 && directionToTarget.y <= 0) {
            return 180 + angle;
        }
        //Quadrant 4
        if (directionToTarget.x >= 0 && directionToTarget.y <= 0) {
            return angle;
        }
        return angle;
    }

    public static boolean isDone() {
        return done;
    }

    public static void setDone(boolean done) {
        Seek.done = done;
    }
}
