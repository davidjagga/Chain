package org.tj.chain;

import edu.fcps.karel2.Display;
import edu.fcps.karel2.Robot;

import java.util.LinkedList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SnakeRobots {
    private static Logger logger = LogManager.getLogger(SnakeRobots.class);
    // List of robots that will be created
    // Any item added to the linked list will be added to the end of the list
    // Last item added to the list will be the tail of the list
    // Fist item added to the list will be the head of the list
    // Example: Add "A" "B" "C" to the list.
    // Tail is "C", Head is "A"
    LinkedList<Robot> robots = new LinkedList<>();

    /**
     * This function creates a new robot at a specified location
      * @param x
     * @param y
     * @param d
     */
    public void create(int x, int y, int d) {
        Robot r = null;
        try {
            r = new Robot(x, y, d, 0);
        } catch (Exception e) {
            logger.error("Received an exception " + e.getMessage());
            throw e;
        }
        if(r != null)
            getRobots().add(r);
    }

    /**
     * This function adds a new robot in the beginning of the chain
     * @param x
     * @param y
     * @param d
     */
    public void moveLastLinkToTheFront(int x, int y, int d) {
        Robot r = null;

        try {
            if (d == Display.NORTH) { // NORTH
                r = new Robot(x, y+1, Display.NORTH, 0);
            } else if (d == Display.WEST) { //WEST
                r = new Robot(x-1, y, Display.WEST, 0);
            } else if (d == Display.SOUTH) { //SOUTH
                r = new Robot(x, y-1, Display.SOUTH, 0);
            } else if (d == Display.EAST) { //EAST
                r = new Robot(x+1, y, Display.EAST, 0);
            }
        } catch (Exception e) {
            logger.error("Received an exception " + e.getMessage());
            throw e;
        }

        if(r != null)
            getRobots().add(r);

    }

    /**
     * This function removes the first link in the chain
     */
    public void removeFirstLink() {
        Robot r = getRobots().getFirst();
        getRobots().removeFirst();
        r.explode();
    }

    /**
     * This method will move all links in a specified direction
     * @param direction
     */
    public void moveAllInOneDirection(int direction) {
        moveAllInOneDirection(direction, false);
    }

    /**
     * This function moves all links but decides whether to remove the last link
     * @param direction
     * @param grow
     */
    public void moveAllInOneDirection(int direction, boolean grow) {

        // Get the last item in the linked list
        // This last item was added new to the list
        // It represents the head of the chain
        Robot r = robots.getLast();
        int x = r.getX();
        int y = r.getY();
        int d = direction;

        logger.info("The last link in the chain x = " + x + ", y = " + y + ", d = " + d);

        // First remove the first link and move the
        // last to the front
        if(!grow) {
            logger.info("Removing the last item in the chain");
            removeFirstLink();
        }
        moveLastLinkToTheFront(x, y, d);
    }



    public static void main(String[] args) throws Exception {

        // Initialize the project
        Display.openWorld("maps/clear.map");
        Display.setSize(15, 15);
        Display.setSpeed(1);

        logger.info(" Adding three links to the chain ");

        SnakeRobots snakeRobots = new SnakeRobots();
        snakeRobots.create(5,1,1);
        snakeRobots.create(5,2,1);
        snakeRobots.create(5,3,1);

        Thread.sleep(3000);

        LinkedList<Robot> listOfSnakeRobots = snakeRobots.getRobots();

        logger.info("Going to move all links in one direction");
        snakeRobots.moveAllInOneDirection(listOfSnakeRobots.getLast().getDirection());

        Thread.sleep(3000);

        //move turn west
        logger.info("Going to move all links to the west");
        snakeRobots.moveAllInOneDirection(2);

        Thread.sleep(3000);

        // move forward
        logger.info("Going to move all links in one direction but add a new link to the chain");
        snakeRobots.moveAllInOneDirection(listOfSnakeRobots.getLast().getDirection(), true);

        Thread.sleep(3000);

        //grow forward
        logger.info("Move all links in one direction");
        snakeRobots.moveAllInOneDirection(listOfSnakeRobots.getLast().getDirection());

        Thread.sleep(3000);

        //grow down
        logger.info("Turn the links");
        snakeRobots.moveAllInOneDirection(3);

    }


    public LinkedList<Robot> getRobots() {
        return robots;
    }

    public void setRobots(LinkedList<Robot> robots) {
        this.robots = robots;
    }
}
