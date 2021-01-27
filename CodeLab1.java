package codesEK280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

public class CodeLab1 extends JPanel {
	
	private static final long serialVersionUID = 1L;
	
	private static Shape3D lineShape(int n) {
		
		if (n < 3) { // if n < 3 produce a ColorCube(0.35)
			return new ColorCube(0.35);
		}
		
		else { // produce a regular polygon with n sides in equal length
			
			float r = 0.6f, x, y;                             // vertex at 0.06 away from origin
			Point3f coor[] = new Point3f[n];

			LineArray lineArr = new LineArray(n * 2, LineArray.COLOR_3 | LineArray.COORDINATES);	// creating a LineArray

			for (int i = 0; i < n; i++) {
				// Need to make sure all points to be populated are evenly spaced along 360 degrees and XY plane
				x = (float) Math.cos(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				y = (float) Math.sin(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				coor[i] = new Point3f(x, y, 0.0f);
			}
			
			for (int i = 0; i < n; i++) {
				lineArr.setCoordinate(i * 2 , coor[i]);
				lineArr.setCoordinate(i * 2 + 1, coor[(i + 1) % n]); //Line changed in order to connect neighboring points rather than star formation
				lineArr.setColor(i * 2, CommonsEK.Red);
				lineArr.setColor(i * 2 + 1, CommonsEK.Green);
			}
		
			return new Shape3D(lineArr);
			
		}
		
	}

	/* a function to create and return the scene BranchGroup */
	public static BranchGroup createScene() {
		BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
		TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
		sceneBG.addChild(sceneTG);	                         // add TG to the scene BranchGroup
		sceneBG.addChild(CommonsEK.rotateBehavior(10000, sceneTG)); 
		                                                     // make sceneTG continuously rotating 
		sceneTG.addChild(lineShape(5));
	
		sceneBG.compile();                                   // optimize objsBG
		return sceneBG;
	}

	/* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				CommonsEK.setEye(new Point3d(1.35, 0.35, 2.0));
				new CommonsEK.MyGUI(createScene(), "EK's Lab 1");
			}
		});
	}
}
