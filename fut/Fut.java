package fut;
import robocode.*;
import java.awt.geom.*;
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
	int lastDetectionTicks = 100;
	AdvancedEnemyBot enemy = new AdvancedEnemyBot();
	private byte moveDirection = 1;
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
			//Radar and Firing
			if(lastDetectionTicks<15)
			{
				lastDetectionTicks++;
				if (getRadarTurnRemaining() == 0)
				{
					double turn = getHeading() - getRadarHeading() + enemyBearing;
					turn += 20 * radarDirection;
					radarDirection *= -1;
					
					setTurnRadarRight(normalizeBearing(turn));
				}
			}
			else//Robot not detected for x ticks
			{
			System.out.println("Robot not detected for "+lastDetectionTicks+" ticks.");
			turnRadarLeft(360);
			}
			execute();
			
			//Moving
			doMove();
		}
	}

	public void onScannedRobot(ScannedRobotEvent e) {
		enemy.update(e);
		enemyBearing = e.getBearing();
		lastDetectionTicks = 0;
		
	
		// calculate firepower based on distance
		double firePower = Math.min(500 / enemy.getDistance(), 3);
		// calculate speed of bullet
		double bulletSpeed = 20 - firePower * 3;
		// distance = rate * time, solved for time
		long time = (long)(enemy.getDistance() / bulletSpeed);
		
		if ( enemy.none() || e.getDistance() < enemy.getDistance() - 70 ||
			e.getName().equals(enemy.getName())) {

		// track him using the NEW update method
		enemy.update(e, this);
		// calculate gun turn to predicted x,y location
		double futureX = enemy.getFutureX(time);
		double futureY = enemy.getFutureY(time);
		double absDeg = absoluteBearing(getX(), getY(), futureX, futureY);
		// turn the gun to the predicted x,y location
		setTurnGunRight(normalizeBearing(absDeg - getGunHeading()));
		}
		//if(getHeading() - getGunHeading() + e.getBearing()<10)
		fire(firePower);
	//	else
		//System.out.println("Not in range");
	}

	/**
	 * onHitByBullet: What to do when you're hit by a bullet
	 */
	public void onHitByBullet(HitByBulletEvent e) {
		// Replace the next line with any behavior you would like
		setTurnRight(enemy.getBearing() - 90);
		moveDirection*=-1;
		setAhead(150 * moveDirection);
	}
	
	/**
	 * onHitWall: What to do when you hit a wall
	 */
	public void onHitWall(HitWallEvent e) {
		// Replace the next line with any behavior you would like
		setTurnRight(90);
		ahead(20);
		
	}
	
	public void update()
	{
		
	}
	
	public void doMove() {
	
		// always square off against our enemy
		setTurnRight(enemy.getBearing() + 90);
	
		// strafe by changing direction every 20 ticks
		if (getTime() % 20 == 0) {
			moveDirection *= -1;
			setAhead(150 * moveDirection);
		}
	}	
	//----------------------------------------------------------------------------
	public double normalizeBearing(double angle)
	{
		while (angle > 180) angle -= 360;
		while (angle < -180) angle += 360;
		return angle;
	}
	// computes the absolute bearing between two points

	double absoluteBearing(double x1, double y1, double x2, double y2) {
		double xo = x2-x1;
		double yo = y2-y1;
		double hyp = Point2D.distance(x1, y1, x2, y2);
		double arcSin = Math.toDegrees(Math.asin(xo / hyp));
		double bearing = 0;
	
		if (xo > 0 && yo > 0) { // both pos: lower-Left
			bearing = arcSin;
		} else if (xo < 0 && yo > 0) { // x neg, y pos: lower-right
			bearing = 360 + arcSin; // arcsin is negative here, actuall 360 - ang
		} else if (xo > 0 && yo < 0) { // x pos, y neg: upper-left
			bearing = 180 - arcSin;
		} else if (xo < 0 && yo < 0) { // both neg: upper-right
			bearing = 180 - arcSin; // arcsin is negative here, actually 180 + ang
		}
	
		return bearing;
	}
	//-------------------------------------------------------------------------------------------------
}
