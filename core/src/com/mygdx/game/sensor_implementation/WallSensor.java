package com.mygdx.game.sensor_implementation;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.game.game_objects.Character;
import com.mygdx.game.game_objects.GameObject;

import java.util.List;

/**
 * This is the class used to add a wall sensor to the character
 */
public class WallSensor {

    private static final float RANGE = 200;
    private Character character;
    private float angle;
    private Ray ray = new Ray();
    private Vector3 intersection = new Vector3();
    private String name;
    private float distToClosestObject;

    public WallSensor(float angle, Character character, String name) {
        this.character = character;
        this.angle = angle;
        this.name = name;
    }

    /**
     * Sense will do the wall sensing for the given character and offset angle
     * @param objects - the objects in the game world
     */
    public void Sense(List<GameObject> objects) {

        //To implement the wall sensor, we are going to create a ray
        //The ray will start at the center of the character and extend at a specified offset angle with respect to the character
        //The exact angle the ray will move is the heading of the character + the angle passed in
        //We want the ray to start at the center of the character so we will use its size to find the middle
        Vector3 characterCenter = new Vector3(character.getPosition().x + (character.getWidth()/2),
                character.getPosition().y + (character.getHeight()/2), 0);


        //To find the direction of the ray, we just have to find the cos(angle) and sin(angle)
        //cos(angle) is the x component of the direction vector, while sin is the y component
        //The angles we are using to represent character position increase clockwise while the unit circle increases counterclockwise
        //To find the unitCircleRelativeAngle we do 360 - character angle + the offset angle
        float resultantAngle = 360 - (character.getAngle() + angle);

        Vector3 rayDirection = new Vector3((float) Math.cos(Math.toRadians(resultantAngle)), (float) Math.sin(Math.toRadians(resultantAngle)), 0);
        ray.set(characterCenter, rayDirection);

        distToClosestObject = Float.MAX_VALUE;

        for (GameObject object : objects) {
//            if(!object.getIsAgent()){
                boolean intersected;

                //For each game object, create a bounding box for it
                //Then check if the ray intersects the bounding box
                //If it intersects, it will update intersection (a vector) to be the intersection point
                Vector3 min = new Vector3(object.getPosition().x, object.getPosition().y, 0);
                Vector3 max = new Vector3(object.getPosition().x + object.getWidth(), object.getPosition().y + object.getHeight(), 0);

                BoundingBox boundingBox = new BoundingBox(min, max);
                intersected = Intersector.intersectRayBounds(ray, boundingBox, intersection);

                //Now find the distance to the intersection point (if there was one)
                if (intersected) {
                    float intersectDistance = characterCenter.dst(intersection);
                    //Check to see if this object is the closest object
                    if (intersectDistance < distToClosestObject) {
                        distToClosestObject = intersectDistance;
                    }
                }
//            }

        }
        if (distToClosestObject > RANGE) {
            distToClosestObject = -1;
        }
    }

    public String getName() {
        return name;
    }

    public float getDistToClosestObject() {
        return distToClosestObject;
    }
}
