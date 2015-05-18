package fut;
import robocode.*;
//import java.awt.Color;

// API help : http://robocode.sourceforge.net/docs/robocode/robocode/Robot.html

/**
 * Fut - a robot by (your name here)
 */
public class Fut extends AdvancedRobot
{
	private double enemyBearing = 0;
	
	/**
	 * run: Fut's default behavior
	 */
	double[] rlocation;
	public void run() {
		// Initialization of the robot should be put here

		// After trying out your robot, try uncommenting the import at the top,
		// and the next line:

		// setColors(Color.red,Color.blue,Color.green); // body,gun,radar

		turnRadarRightRadians(2*Math.PI);
		
		// Robot main loop
		setAdjustRadarForRobotTurn(true);
		int radarDirection = 1;
		while(true)
		{
			//turnRadarLeftRadians(2*Math.PI/6);
			if (getRadarTurnRemaining() == 0)
			{
				double turn = getHeading() - getRadarHeading() + enemyBearing;
				turn += 20 * radarDirection;
				radarDirection *= -1;
				
				setTurnRadarRight(normalizeBearing(turn));
			}
			
			execute();
		}
	}
	
	public double normalizeBearing(double angle)
	{
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}

	/**
	 * onScannedRobot: What to do when you see another robot
	 */
	public void onScannedRobot(ScannedRobotEvent e) {
	
		enemyBearing = e.getBearing();
		
		if(getHeading() - enemyBearing > 0)
		turnGunRight(5);
		else
		turnGunLeft(5);
		//fire(Math.random()*3);
		rlocation = new double[]{e.getDistance()*Math.cos(e.getBearingRadians()),e.getDistance()*Math.sin(e.getBearingRadians())};
	System.out.println(rlocation[0]+", "+rlocation[1]);
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		back(10);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		ahead(20);
	}	
}
