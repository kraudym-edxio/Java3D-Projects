package codesEK280;

/* *********************************************************
 * For use by students to work on assignments and project.
 * Permission required material. Contact: xyuan@uwindsor.ca 
 **********************************************************/

import javax.swing.JPanel;

import org.jogamp.java3d.*;
import org.jogamp.java3d.utils.image.TextureLoader;
import org.jogamp.vecmath.*;

public class CodeAssign2 extends JPanel {

	private static final long serialVersionUID = 1L;

	/**
	 * The following function is responsible for loading the specified texture given by the String path
	 * @return Texture2D object containing texture
	 */
	private static Texture setTexture() {
		
		String path = "images/backgroundLight.jpg";
		TextureLoader tl = new TextureLoader(path, null);
		ImageComponent2D image = tl.getImage();
		
		if (image == null) {
			System.out.println("Load failed for texture: " + path);
		}
		
		Texture2D texture = new Texture2D(Texture.BASE_LEVEL, Texture.RGBA, image.getWidth(), image.getHeight());
		texture.setImage(0, image);
		
		return texture;
		
	}
	
	/**
	 * The following function adds texture and attributes to an Appearance object  
	 * @return an Appearance object with texture attributes, polygon attributes, and texture coordinate generation
	 */
	private static Appearance texturedApp() {
		
		Appearance app = new Appearance();
		app.setTexture(setTexture());
		
		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		app.setPolygonAttributes(pa);
		
		TextureAttributes ta = new TextureAttributes();
		ta.setTextureMode(TextureAttributes.REPLACE);
		app.setTextureAttributes(ta);

		return app;
		
	}
	
	/**
	 * A basic appearance for rendering the base model
	 * @return a basic appearance with specified PolygonAttributes
	 */
	private static Appearance base() {
		
		Appearance app = new Appearance();

		PolygonAttributes pa = new PolygonAttributes();
		pa.setCullFace(PolygonAttributes.CULL_NONE);
		app.setPolygonAttributes(pa);

		return app;
		
	}
	
	/**
	 * Creating an illusion of a background to be used for the scene
	 * @param scl the scale factor to be used when specifying background size
	 * @return a Shape3D with an image as its texture
	 */
	private static Shape3D drawBackground (float scl) {
		
		//Creating an array of points to represent the vertices of the background according to assignment specifications
		Point3f coor[] = {
				new Point3f(- 7.5f * scl, 7.5f * scl, - 17f),
				new Point3f(- 7.5f * scl, - 7.5f * scl, - 17f),
				new Point3f(7.5f * scl, - 7.5f * scl, - 17f),
				new Point3f(7.5f * scl, 7.5f * scl, - 17f),
		};
		
		QuadArray quadArr = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.COLOR_3 | GeometryArray.TEXTURE_COORDINATE_2);	//Creating a QuadArray
		quadArr.setCoordinates(0, coor);	//Setting the coordinates

		float uv0[] = {0f, 0f};
		float uv1[] = {1f, 0f};
		float uv2[] = {1f, 1f};
		float uv3[] = {0f, 1f};
		
		//Setting the texture coordinates
		quadArr.setTextureCoordinate(0,  0, uv0);
		quadArr.setTextureCoordinate(0,  1, uv1);
		quadArr.setTextureCoordinate(0,  2, uv2);
		quadArr.setTextureCoordinate(0,  3, uv3);

		return new Shape3D(quadArr, texturedApp());
		
	}
	
	/**
	 * TransformGroup for top portion of base
	 * @param color the color of the top
	 * @return TransformGroup of top
	 */
	private static TransformGroup platformTop (Color3f color) {
		
		//Creating an array of points to represent the vertices of the top according to assignment specifications
		Point3f coor[] = {
				new Point3f(-1f, 0, -1f),
				new Point3f(-1f, 0, 1f),
				new Point3f(1f, 0, 1f),
				new Point3f(1f, 0, -1f),		
		};
		
		QuadArray quadArr = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.COLOR_3);	//Creating a QuadArray
		quadArr.setCoordinates(0, coor);	//Setting the coordinates
		
		for (int i = 0; i < 4; i++) {
			quadArr.setColor(i, color);		//Setting the color
		}
		
		Shape3D top = new Shape3D(quadArr, base());
		
		//Applying translation such that the center of the model is at the origin
		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		
		trans3d.setTranslation(new Vector3f(0, 0.04f, 0));
		trans = new TransformGroup(trans3d);
		trans.addChild(top);
		
		return trans;
		
	}
	
	/**
	 * TransformGroup for the bottom portion of the base
	 * @param color the color of the bottom
	 * @return TransformGroup of bottom
	 */
	private static TransformGroup platformBottom (Color3f color) {
		
		//Define using already created face
		TransformGroup bottom = platformTop(color);
		
		//Applying translation such that the center of the model is at the origin
		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		
		trans3d.setTranslation(new Vector3f(0, -0.08f, 0));
		trans = new TransformGroup(trans3d);
		trans.addChild(bottom);
		
		return trans;	
		
	}
	
	/**
	 * TransformGroup for the front portion of the base
	 * @param color the color of the front
	 * @return TransformGroup of front
	 */
	private static TransformGroup platformFront (Color3f color) {
		
		Point3f coor[] = {
				new Point3f(-1f, 0.04f, 0),
				new Point3f(-1f, -0.04f, 0),
				new Point3f(1f, -0.04f, 0),
				new Point3f(1f, 0.04f, 0),		
		};
		
		QuadArray quadArr = new QuadArray(4, GeometryArray.COORDINATES | GeometryArray.COLOR_3);	//Creating a QuadArray
		quadArr.setCoordinates(0, coor);	//Setting the coordinates
		
		for (int i = 0; i < 4; i++) {
			quadArr.setColor(i, color);		//Setting the color
		}
		
		Shape3D front = new Shape3D(quadArr, base());
		
		//Applying translation such that the center of the model is at the origin
		Transform3D trans3d = new Transform3D();
		TransformGroup trans = null;
		
		trans3d.setTranslation(new Vector3f(0, 0, 1)); //Need to bring face to front 
		trans = new TransformGroup(trans3d);
		trans.addChild(front);
		
		return trans;
		
	}
	
	/**
	 * TransformGroup for the right portion of the base
	 * @param color the color of the right
	 * @return TransformGroup of right
	 */
	private static TransformGroup platformRight (Color3f color) {
	
		//Define using already created face
		TransformGroup right = platformFront(color);
		
		//Applying rotation such that face can be moved to the correct placement along the base
		Transform3D rotator = new Transform3D();
		TransformGroup trans = null;
		
		rotator.rotY(Math.PI / 2);
		trans = new TransformGroup(rotator);
		trans.addChild(right);
		
		return trans;	
		
	}
	
	/**
	 * TransformGroup for the back portion of the base
	 * @param color the color of the back
	 * @return TransformGroup of back
	 */
	private static TransformGroup platformBack (Color3f color) {
		
		//Define using already created face
		TransformGroup back = platformRight(color);
		
		//Applying rotation such that face can be moved to the correct placement along the base
		Transform3D rotator = new Transform3D();
		TransformGroup trans = null;
		
		rotator.rotY(Math.PI / 2);
		trans = new TransformGroup(rotator);
		trans.addChild(back);
		
		return trans;	
		
	}
	
	/**
	 * TransformGroup for the left portion of the base
	 * @param color the color of the left
	 * @return TransformGroup of left
	 */
	private static TransformGroup platformLeft (Color3f color) {
		
		//Define using already created face
		TransformGroup left = platformBack(color);
		
		//Applying rotation such that face can be moved to the correct placement along the base
		Transform3D rotator = new Transform3D();
		TransformGroup trans = null;
		
		rotator.rotY(Math.PI / 2);
		trans = new TransformGroup(rotator);
		trans.addChild(left);
		
		return trans;	
		
	}
	
	/**
	 * This function returns a scalable base for the structure
	 * @param scl scale factor to be applied to base
	 * @return base model as a TransformGroup
	 */
	private static TransformGroup baseComplete (float scl) {
		
		Transform3D scaler = new Transform3D();
		scaler.setScale(scl);	//Set scale factor

		Transform3D translator = new Transform3D();
		translator.setTranslation(new Vector3f(0, -0.04f * scl - 0.5f, 0));	//Set translation (will always go down the amount of 0.5 + scaled radius)
		
		//Multiply each matrix, with the last one being the first transformation to be applied
		Transform3D trfm = new Transform3D();
		trfm.mul(translator, scaler);
		
		TransformGroup base = new TransformGroup(trfm);	//Create a TransformGroup using the Transform3D
		
		//Adding each component of the base to the TransformGroup
		base.addChild(platformTop(CommonsEK.Blue));
		base.addChild(platformBottom(CommonsEK.Magenta));
		base.addChild(platformFront(CommonsEK.Red));
		base.addChild(platformRight(CommonsEK.Green));
		base.addChild(platformBack(CommonsEK.Cyan));
		base.addChild(platformLeft(CommonsEK.Yellow));
		
		return base;
		
	}
	
	/* a function to create and return the scene BranchGroup */
	public static BranchGroup createScene() {
		
		BranchGroup sceneBG = new BranchGroup();		     // create 'objsBG' for content
		TransformGroup sceneTG = new TransformGroup();       // create a TransformGroup (TG)
		
		sceneBG.addChild(sceneTG); 							 			// add TG to the scene BranchGroup				 
		sceneBG.addChild(CommonsEK.rotateBehavior(10000, sceneTG)); 	// make sceneTG continuously rotating 
		
		Color3f color = CommonsEK.Cyan;
		sceneBG.addChild(CodeAssign1.axisFrame(color, 0.5f)); //Child added to sceneBG, not sceneTG (because axis is not meant to rotate)
		
		sceneBG.addChild(drawBackground(1.5f));				//Add drawBackgorund to sceneBG because it must remain stationary
		sceneTG.addChild(CodeLab3.drawPavilion(6));			//Add pavilion model to the scene TransformGroup		 
		
		sceneTG.addChild(baseComplete(0.6f));				//Add base to the scene TransformGroup with specified scale factor
	
		sceneBG.compile();                                  //Optimize objsBG
		return sceneBG;
		
	}
  
	/* the main entrance of the application via 'MyGUI()' of "CommonXY.java" */
	public static void main(String[] args) {
		java.awt.EventQueue.invokeLater(new Runnable() {
			public void run() {
				CommonsEK.setEye(new Point3d(0.05, 0.35, 2.5));
				new CommonsEK.MyGUI(createScene(), "EK's Assignment 2");
			}
		});
	}
}
