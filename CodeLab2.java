package codesEK280;

import java.awt.Font;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;


public class CodeLab2 extends JPanel {
	
	public int selection = 0;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * Method which returns a transform group containing 3D text
	 * @param n number of sides polygon contains
	 * @return scene_TG TransformGroup
	 */
	public static TransformGroup letters3D(int n) {
	
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);
		FontExtrusion myExtrude = new FontExtrusion();
		Font3D font3D = new Font3D(my2DFont, myExtrude);
		
		//Text3D object to describe N-sided polygon created with TriangleArray (centered with specification of Point3f and last two arguments)
		Text3D text3D = new Text3D(font3D, "Polygon" + n, new Point3f(0.0f, 0.0f, 0.0f), Text3D.ALIGN_CENTER, Text3D.PATH_RIGHT);
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(0.15);	//Selected scalar such that Text3D can fit within the polygon
		
		TransformGroup scene_TG = new TransformGroup(scaler);
		scene_TG.addChild(new Shape3D(text3D));
		
		return scene_TG;
		
	}
	
	private static Shape3D lineShape(int n) {
		
		if (n < 3) { // if n < 3 produce a ColorCube(0.35)
			return new ColorCube(0.35);
		}
		
		else { // produce a regular polygon with n sides in equal length
		
			float r = 0.6f, x, y;                             // vertex at 0.06 away from origin
			Point3f coor[] = new Point3f[n];
			
			//The following are variables used to keep track of color alternations
			int before = 1;
			int after = 2;

			TriangleArray triArr = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);	// creating a TriangleArray

			for (int i = 0; i < n; i++) {
				// Need to make sure all points to be populated are evenly spaced along 360 degrees and XY plane
				x = (float) Math.cos(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				y = (float) Math.sin(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				coor[i] = new Point3f(x, y, 0.0f);
			}
			
			for (int i = 0; i < n; i++) {
				//Setting coordinates for the TriangleArray using three points at a time
				triArr.setCoordinate(i * 3, new Point3f(0.0f, 0.0f, 0.0f));
				triArr.setCoordinate(i * 3 + 1, coor[i]);
				triArr.setCoordinate(i * 3 + 2, coor[(i + 1) % n]);
				
				//Setting colors for the TriangleArray using three points at a time
				triArr.setColor(i * 3, CommonsEK.Blue);
				triArr.setColor(i * 3 + 1, CommonsEK.Clrs[before]);
				triArr.setColor(i * 3 + 2, CommonsEK.Clrs[after]);
				
				//If and else to determine appropriate color for iteration (i) of the for-loop.
				if ((after < 7) && (i != n - 2)) {
					before = after;
					after++;
				}
				
				else {
					
					if ((after < 7) && (i == n - 2)) {
						before = after;
						after = 1;
					}
					
					else {
						before = after;
						after = 1;
					}
					
				}
				
			}
			
			return new Shape3D(triArr);
		
		}
		
	}

	/* a function to create and return the scene BranchGroup */
	public static BranchGroup createScene() {
		
		BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
		TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
		
		sceneBG.addChild(sceneTG); 							 // add TG to the scene BranchGroup				 
		sceneBG.addChild(CommonsEK.rotateBehavior(10000, sceneTG)); // make sceneTG continuously rotating 
		
		sceneTG.addChild(lineShape(5));	//Add triArr created shape to the scene
		sceneTG.addChild(letters3D(5));	//Add 3D text "PolygonN" to the scene
	
		sceneBG.compile();                                   // optimize objsBG
		return sceneBG;
	}

	/* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				CommonsEK.setEye(new Point3d(1.35, 0.35, 2.0));
				new CommonsEK.MyGUI(createScene(), "EK's Lab 2");
			}
		});
	}
}
