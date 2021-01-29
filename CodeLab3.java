package codesEK280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.java3d.utils.geometry.Cylinder;
import org.jogamp.vecmath.*;

public class CodeLab3 extends JPanel {
	
	public int selection = 0;
	
	private static final long serialVersionUID = 1L;
	
	/**
	 * This function uses a LineArray to represent the three axis frame in 3D
	 * @param yColor color selected for y axis (upwards)
	 * @param length the length of each axis starting from the origin
	 * @return a Shape3D of the created LineArray
	 */
	private static Shape3D axisFrame (Color3f yColor, float length) {
		
		//Declaring necessary points to encompass every direction and the length away from origin
		Point3f coor[] = new Point3f[3];
		coor[0] = new Point3f(length, 0.0f, 0.0f);
		coor[1] = new Point3f(0.0f, length, 0.0f);
		coor[2] = new Point3f(0.0f, 0.0f, length);
		
		//Declaring an array of Color3f to later color each axis accordingly
		Color3f Red = new Color3f(1.0f, 0.0f, 0.0f);
		Color3f Green = new Color3f(0.0f, 1.0f, 0.0f);
		Color3f clr[] = {Green, yColor, Red};
		
		LineArray lineArr = new LineArray(6, LineArray.COLOR_3 | LineArray.COORDINATES);	//LineArray created for axis frame
		
		//Introducing the points into the LineArray and setting their appropriate color relative to the current direction
		for (int i = 0; i < 3; i++) {
			lineArr.setCoordinate(i * 2 , new Point3f(0.0f, 0.0f, 0.0f));
			lineArr.setCoordinate(i * 2 + 1, coor[i]);
			lineArr.setColor(i * 2, clr[i]);
			lineArr.setColor(i * 2 + 1, clr[i]);
		}
		
		return new Shape3D(lineArr);	//Return axis frame
	
	}
	
	/**
	 * This function creates a ColorCube or a right pyramid with its base being an n-sided regular polygon
	 * @param n number of sides for polygon base
	 * @return a Shape3D containing the constructed ColorCube or right pyramid
	 */
	private static BranchGroup drawPavilion (int n) {
		
		BranchGroup objBG = new BranchGroup();
		
		if (n < 3) { //If n < 3 produce a ColorCube(0.35)
			objBG.addChild(new ColorCube(0.35));
			return objBG;
		}
		
		else { //Produce a regular polygon with n sides in equal length
		
			float r = 0.6f, x, z;					//Constant distance of 0.6 to the side corners                         
			Point3f coor[] = new Point3f[n];
			
			//Arrays necessary for adding and positioning cylinders
			Vector3f position[] = new Vector3f[n];	
			Cylinder columns[] = new Cylinder[n]; 
			
			//Necessary for translation of cylinders
			Transform3D trans3d = new Transform3D();
			TransformGroup trans = null;
			
			int before = 1; int after = 2;

			TriangleArray triArr = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);	//Creating a TriangleArray

			for (int i = 0; i < n; i++) {
				// Need to make sure all points to be populated are evenly spaced along 360 degrees and XZ plane
				x = (float) Math.cos(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				z = (float) Math.sin(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				coor[i] = new Point3f(x, 0.5f, z); //Need to generate points along 360 degrees with base center at 0.5f on Y-axis
			}
			
			//Loop populates an array of Vector3f such that the distance from the origin to the point is 0.5f (the cylinder location)
			for (int i = 0; i < n; i++) {
				// Need to make sure all points to be populated are evenly spaced along 360 degrees and XY plane
				x = (float) Math.cos(Math.PI / 180 * (90 + (360 / n) * i)) * 0.5f;
				z = (float) Math.sin(Math.PI / 180 * (90 + (360 / n) * i)) * 0.5f;
				position[i] = new Vector3f(x, 0.0f, z);
			}
			
			//Right Pyramid's Top
			for (int i = 0; i < n; i++) {
				//Setting coordinates for the TriangleArray using three points at a time
				triArr.setCoordinate(i * 3, new Point3f(0.0f, 0.65f, 0.0f)); //Apex of pyramid 0.65f on Y-axis
				triArr.setCoordinate(i * 3 + 1, coor[i]);
				triArr.setCoordinate(i * 3 + 2, coor[(i + 1) % n]);
				
				//Setting colors for the TriangleArray using three points at a time
				triArr.setColor(i * 3, CommonsEK.Blue);
				triArr.setColor(i * 3 + 1, CommonsEK.Clrs[before]);
				triArr.setColor(i * 3 + 2, CommonsEK.Clrs[after]);

				//Creating an Appearance and ColorAttributes which will alternate color
				Appearance app = new Appearance();
				ColoringAttributes ca = new ColoringAttributes(CommonsEK.Clrs[before], ColoringAttributes.NICEST);
				app.setColoringAttributes(ca);
				
				columns[i] = new Cylinder(0.04f, 1f, app);	//Creating a new cylinder with given specification
				
				//Need to translate each created cylinder to correct position using Transform3D
				trans3d.setTranslation(position[i]);
				trans = new TransformGroup(trans3d);
				objBG.addChild(trans);
				trans.addChild(columns[i]);
				
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
			
			TriangleArray triArr2 = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);	//Creating a TriangleArray
			
			//Right Pyramid's Base
			for (int i = 0; i < n; i++) {
				//Setting coordinates for the TriangleArray using three points at a time
				triArr2.setCoordinate(i * 3, new Point3f(0.0f, 0.5f, 0.0f));
				triArr2.setCoordinate(i * 3 + 1, coor[i]);
				triArr2.setCoordinate(i * 3 + 2, coor[(i + 1) % n]);
				
				//Setting colors for the TriangleArray using three points at a time
				triArr2.setColor(i * 3, CommonsEK.Blue);
				triArr2.setColor(i * 3 + 1, CommonsEK.Blue);
				triArr2.setColor(i * 3 + 2, CommonsEK.Blue);
			}
			
			//Creating an appearance and setting polygon attributes so base does not disappear during rotation or setEye change
			Appearance app2 = new Appearance();
			PolygonAttributes roof = new PolygonAttributes();
			roof.setCullFace(PolygonAttributes.CULL_NONE);
			app2.setPolygonAttributes(roof);
			
			Shape3D pyramid = new Shape3D(triArr, app2);
			pyramid.addGeometry(triArr2);
			
			objBG.addChild(pyramid);
			return objBG;
			
		}
		
	}

	/* a function to create and return the scene BranchGroup */
	public static BranchGroup createScene() {
		
		BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
		TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
		
		sceneBG.addChild(sceneTG); 							 // add TG to the scene BranchGroup				 
		sceneBG.addChild(CommonsEK.rotateBehavior(10000, sceneTG)); // make sceneTG continuously rotating 
		
		Color3f color = CommonsEK.Cyan;
		sceneBG.addChild(axisFrame(color, 0.5f)); //Child added to sceneBG, not sceneTG (because axis is not meant to rotate)
		
		sceneTG.addChild(drawPavilion(6));						 //Add BranchGroup created to the scene
	
		sceneBG.compile();                                   // optimize objsBG
		return sceneBG;
	}

	/* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				CommonsEK.setEye(new Point3d(1.35, 0.35, 2.0));
				new CommonsEK.MyGUI(createScene(), "EK's Lab 3");
			}
		});
	}
}
