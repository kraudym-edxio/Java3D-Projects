package codesEK280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/

import javax.swing.JPanel;
import java.awt.Font;
import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.geometry.ColorCube;
import org.jogamp.vecmath.*;

public class CodeAssign1 extends JPanel {
	
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
	 * This function created a rectangle to be drawn and displayed
	 * @param color the color of the rectangle to be drawn
	 * @param size the point representing rectangle's size
	 * @param scale the vector representing rectangle's scale
	 * @return a Shape3D containing the constructed rectangle
	 */
	private static Shape3D drawRectangle (Color3f color, Point3f size, Vector2f scale) {
		
		//Declaring the points of the rectangle which will have (2 * size.x * scale.x) width and (2 * size.y * scale.y) height
		Point3f coor[] = {
				new Point3f(- size.x * scale.x, size.y * scale.y, size.z),
				new Point3f(- size.x * scale.x, - size.y * scale.y, size.z),
				new Point3f(size.x * scale.x, - size.y * scale.y, size.z),
				new Point3f(size.x * scale.x, size.y * scale.y, size.z),
		};
		
		QuadArray quadArr = new QuadArray(coor.length, QuadArray.COLOR_3 | QuadArray.COORDINATES);	//Creating a QuadArray
		quadArr.setCoordinates(0, coor);	//Setting the coordinates
		
		for (int i = 0; i < 4; i++) {
			quadArr.setColor(i, color);		//Setting the color
		}
	
		return new Shape3D(quadArr);
		
	}
	
	/**
	 * This function creates a ColorCube or a right pyramid with its base being an n-sided regular polygon
	 * @param n number of sides for polygon base
	 * @return a Shape3D containing the constructed ColorCube or right pyramid
	 */
	private static Shape3D drawPyramid (int n) {
		
		if (n < 3) { //If n < 3 produce a ColorCube(0.35)
			return new ColorCube(0.35);
		}
		
		else { //Produce a regular polygon with n sides in equal length
		
			float r = 0.45f, x, y;                            
			Point3f coor[] = new Point3f[n];
			int before = 1; int after = 2;

			TriangleArray triArr = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);	//Creating a TriangleArray

			for (int i = 0; i < n; i++) {
				// Need to make sure all points to be populated are evenly spaced along 360 degrees and XY plane
				x = (float) Math.cos(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				y = (float) Math.sin(Math.PI / 180 * (90 + (360 / n) * i)) * r;
				coor[i] = new Point3f(x, y, 0.25f); //Need to generate points along 360 degrees with base center at 0.25f on Z-axis
			}
			
			//Right Pyramid's Top
			for (int i = 0; i < n; i++) {
				//Setting coordinates for the TriangleArray using three points at a time
				triArr.setCoordinate(i * 3, new Point3f(0.0f, 0.0f, 0.50f));
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
			
			TriangleArray triArr2 = new TriangleArray(n * 3, TriangleArray.COLOR_3 | TriangleArray.COORDINATES);	//Creating a TriangleArray
			
			//Right Pyramid's Base
			for (int i = 0; i < n; i++) {
				//Setting coordinates for the TriangleArray using three points at a time
				triArr2.setCoordinate(i * 3, new Point3f(0.0f, 0.0f, 0.25f));
				triArr2.setCoordinate(i * 3 + 1, coor[i]);
				triArr2.setCoordinate(i * 3 + 2, coor[(i + 1) % n]);
				
				//Setting colors for the TriangleArray using three points at a time
				triArr2.setColor(i * 3, CommonsEK.Blue);
				triArr2.setColor(i * 3 + 1, CommonsEK.Blue);
				triArr2.setColor(i * 3 + 2, CommonsEK.Blue);
			}
			
			//Creating an appearance and setting polygon attributes so base does not disappear during rotation
			Appearance app = new Appearance();
			PolygonAttributes pa = new PolygonAttributes();
			pa.setCullFace(PolygonAttributes.CULL_NONE);
			app.setPolygonAttributes(pa);
			
			Shape3D pyramid = new Shape3D(triArr, app);
			pyramid.addGeometry(triArr2);
			
			return pyramid;
			
		}
		
	}
	
	/**
	 * This function created three dimensional text 
	 * @param txt a string 
	 * @param scl the scaling factor for the text
	 * @param pnt the positioning point for the text
	 * @param clr the color of the text
	 * @return a TransformGroup containing a Shape3D of the three dimensional text
	 */
	private static TransformGroup letters3D (String txt, double scl, Point3f pnt, Color3f clr) {
		
		//Creating 2D font and specifying FontExtrusion
		Font my2DFont = new Font("Arial", Font.PLAIN, 1);
		FontExtrusion myExtrude = new FontExtrusion();
		
		//Create 3D text using string and Font3D in a Text3D
		Font3D font3D = new Font3D(my2DFont, myExtrude);
		Text3D text3D = new Text3D(font3D, txt, pnt);
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);	//Set scaler for Transform3D
		
		//Setting the appearance and coloring attributes
		Appearance app = new Appearance();
		ColoringAttributes ca = new ColoringAttributes(clr, ColoringAttributes.NICEST);
		app.setColoringAttributes(ca);
		
		//Appearance and geometry of new Shape3D is set 
		Shape3D word = new Shape3D();
		word.setGeometry(text3D);
		word.setAppearance(app);
		
		TransformGroup scene_TG = new TransformGroup(scaler); //Perform scaling
		scene_TG.addChild(word);
		
		return scene_TG;
		
	}

	/* A function to create and return the scene BranchGroup */
	public static BranchGroup createScene() {
		
		BranchGroup sceneBG = new BranchGroup();		     			//Create 'objsBG' for content
		TransformGroup sceneTG = new TransformGroup();       			//Create a TransformGroup (TG)
		sceneBG.addChild(sceneTG);	                        			//Add TG to the scene BranchGroup
		sceneBG.addChild(CommonsEK.rotateBehavior(10000, sceneTG)); 	//Make sceneTG continuously rotating 
		
		//Adding the axis frame to the scene with specified arguments
		sceneBG.addChild(axisFrame(CommonsEK.Cyan, 0.5f)); //Child added to sceneBG, not sceneTG (because axis is not meant to rotate)
		
		//Adding the rectangle to the scene with specified arguments
		sceneTG.addChild(drawRectangle(CommonsEK.Grey, new Point3f(1f, 0.8f, -0.25f), new Vector2f(0.6f, 0.6f)));
		
		//Adding the generated shape based on the value of n to the scene
		sceneTG.addChild(drawPyramid(7));
		
		//Adding the 3D text to the scene with specified arguments
		String str = "Edxio";
		sceneTG.addChild(letters3D(str, 0.2d, new Point3f(-str.length() / 4f, 0, 0), CommonsEK.White));
	
		sceneBG.compile();                                   			//Optimize objsBG
		return sceneBG;
		
	}

	/* The main entrance of the application via 'MyGUI()' of "CommonsEK.java" */
	public static void main(String[] args) {
		
		java.awt.EventQueue.invokeLater(new Runnable() {
			
			public void run() {
				CommonsEK.setEye(new Point3d(1.35, 0.35, 2.0));
				new CommonsEK.MyGUI(createScene(), "EK's Assignment 1");
			}
			
		});
		
	}
	
}