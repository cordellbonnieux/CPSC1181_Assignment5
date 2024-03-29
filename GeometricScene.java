import java.util.ArrayList;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Ellipse;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/**
 * Kitty Maker
 * A simple program that allows you to create custom scale kitties on a scenic north-African inspired landscape.
 * Created kitties will appear on the image, as well as the right side UI display, from there the user can set the
 * visibility or delete the kitty. There are a maximum of 9 kitties allowed on the screen at one time. 
 * The background elements are also toggle-able via the bottom pane of the UI.
 * @author Cordell Bonnieux
 * 
 */
public class GeometricScene extends Application {
	/*
	 * LAYOUT ELEMENTS
	 */
	// width and height for graphical image
	private final int HEIGHT = 800;
	private final int WIDTH = 1200;
	// UI Elements
	private BorderPane root = new BorderPane();
	private Ground foreground = new Ground();
	private Background background = new Background(300, 300);
	private Pane center = new Pane();
	/*
	 * BACKGROUND CONTROL
	 */
	// top controls
	private CheckBox pyramidCheckBox;
	private CheckBox moonCheckBox;
	private CheckBox nightCheckBox;
	/*
	 * KITTY ELEMENTS
	 */
	// bottom right controls
	private RadioButton tall;
	private RadioButton smol;
	private RadioButton thicc;
	private RadioButton hungry;
	private Button createBtn = new Button("Create A Kitty");
	private KittyChoices kittyType = new KittyChoices();
	private KittyCreator spawnKitty = new KittyCreator();
	// bottom right control groups
	private ToggleGroup kittyWidth;
	private ToggleGroup kittyHeight;
	// Keep track of spawned kitties
	private ArrayList<Kitty> kittyTracker = new ArrayList<Kitty>();
	// Keep track of spawned kitty controls
	private ArrayList<CheckBox> kittyVisibilityTracker = new ArrayList<CheckBox>();
	private ArrayList<Button> kittyDeleteTracker = new ArrayList<Button>();
	// create a kitty temp variables
	private boolean isTall;
	private boolean isSmol;
	private boolean isThicc;
	private boolean isHungry;
	
	/**
	 * Launch Application
	 * @param args
	 */
	public static void main(String[] args) {
		Application.launch(args);
	}

	/**
	 * Start
	 * JavaFX program logic
	 * @param stage
	 */
	@Override
	public void start(Stage stage) throws Exception {
		InitializeCenterImage();
		InitializeMainPane(stage);
		InitializeKittyCreatorControls();
	}
	
	/**
	 * Initialize the main border pane
	 * @param stage Stage
	 */
	private void InitializeMainPane(Stage stage) {
		root.setStyle("-fx-background-color:#ffffff;");
		root.setPadding(new Insets(10));
		
		root.setRight(get_currentKitties());
		
		HBox bgControls = get_bgControls();
		root.setBottom(new HBox(bgControls, get_kittyControls()));
		HBox.setMargin(bgControls, new Insets(15, 0, 0, 0));
		
		Scene scene = new Scene(root);
		stage.setTitle("Kitty Maker");
		stage.setResizable(false);
		stage.setScene(scene);
		stage.show();
	}
	
	/**
	 * Initialize Event Listeners for Kitty Creator.
	 */
	private void InitializeKittyCreatorControls() {
		tall.setOnAction(kittyType);
		smol.setOnAction(kittyType);
		thicc.setOnAction(kittyType);
		hungry.setOnAction(kittyType);
		createBtn.setOnAction(spawnKitty);
	}
	
	/**
	 * Paint the Image to the center pane region.
	 */
	private void InitializeCenterImage() {
		center.setPrefHeight(HEIGHT);
		center.setPrefWidth(WIDTH);
		center.setPadding(new Insets(10));
		center.getChildren().addAll(background, foreground);
		
		Rectangle clip = new Rectangle(0,0,WIDTH, HEIGHT);
		center.setClip(clip);		
		root.setCenter(center);
	}
	/**
	 * Current Kitties
	 * Builds a GUI list of all current kitties created
	 * @return VBox - UI element containing the current kitties
	 */
	private VBox get_currentKitties() {
		Text heading = new Text("Current Kitties");
		heading.setFill(Color.rgb(44, 44, 44));
		heading.setStyle("-fx-font-weight:700; -fx-font-size:1.4em;");
		VBox kittyHolder = new VBox(5);
		for (int i = 0; i < kittyTracker.size(); i++) {
			KittyChoices handler = new KittyChoices();
			
			CheckBox visible = new CheckBox("visible");
			visible.setStyle("-fx-text-fill:#2c2c2c;");
			visible.setSelected(true);
			visible.setOnAction(handler);
			kittyVisibilityTracker.add(visible);
			
			
			Button delete = new Button("delete");
			delete.setStyle("-fx-background-color:#fc284f; -fx-text-fill:#ffffff; -fx-border-width:1; -fx-border-color:#2c2c2c;");
			delete.setOnAction(handler);
			kittyDeleteTracker.add(delete);
			
			HBox info = new HBox(30, kittyTracker.get(i).getCopyUI(), kittyVisibilityTracker.get(i), kittyDeleteTracker.get(i));
			info.setStyle("-fx-background-color:#ffffff; -fx-padding:5px; -fx-background-radius:4px; -fx-font-weight:bold;");
			kittyHolder.getChildren().add(info);
		}
		Text numOfKitties = new Text(kittyTracker.size() + "/9");
		numOfKitties.setStyle("-fx-font-weight:700; -fx-font-size:1.4em;");
		numOfKitties.setFill(Color.rgb(252, 40, 78));
		HBox topText = new HBox(10, heading, numOfKitties);
		VBox container = new VBox(10, topText, kittyHolder);
		container.setPrefWidth(327.00);
		container.setStyle("-fx-background-color:#F2F5F7; -fx-padding:5px; -fx-border-width:4px 4px 0px 4px; -fx-border-style:dotted; -fx-border-color:#ffffff;");
		BorderPane.setMargin(container, new Insets(0, 10, 0, 10));
		return container;
	}
	
	/**
	 * Background Controls
	 * Checkboxes to control background elements via GUI
	 * @return HBox containing control UI elements
	 */
	private HBox get_bgControls() {
		BackgroundEditor bgChooser = new BackgroundEditor();
		Text bgText = new Text("Toggle Background Elements:");
		bgText.setStyle("-fx-font-size:1.1em; -fx-font-weight:bold;");
		bgText.setFill(Color.rgb(44, 44, 44));
		
		pyramidCheckBox = new CheckBox("Pyramid");
		moonCheckBox = new CheckBox("Moon");
		nightCheckBox = new CheckBox("Night");
		
		pyramidCheckBox.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		moonCheckBox.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		nightCheckBox.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		
		pyramidCheckBox.setSelected(true);
		moonCheckBox.setSelected(true);
		nightCheckBox.setSelected(true);
		
		pyramidCheckBox.setOnAction(bgChooser);
		moonCheckBox.setOnAction(bgChooser);
		nightCheckBox.setOnAction(bgChooser);
		
		HBox bgControls = new HBox(10, bgText, pyramidCheckBox, nightCheckBox, moonCheckBox);
		bgControls.setPrefWidth(WIDTH+10);
		return bgControls;
	}
	
	/**
	 * Kitty Maker Controls
	 * Controls for instantiating new kitties
	 * @return
	 */
	private HBox get_kittyControls() {
		kittyHeight = new ToggleGroup();
		tall = new RadioButton("tall");
		tall.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		smol = new RadioButton("smol");
		smol.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		smol.setToggleGroup(kittyHeight);
		tall.setToggleGroup(kittyHeight);
		
		kittyWidth = new ToggleGroup();
		thicc = new RadioButton("thicc");
		thicc.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		hungry = new RadioButton("hungry");
		hungry.setStyle("-fx-color:#d1d0d7; -fx-text-fill:#2c2c2c; -fx-font-weight:bold; -fx-border-width:1px;");
		thicc.setToggleGroup(kittyWidth);
		hungry.setToggleGroup(kittyWidth);
		
		createBtn.setStyle("-fx-background-color:#fc284f; -fx-text-fill:#ffffff; -fx-border-width:1; -fx-border-color:#2c2c2c; -fx-font-weight:bold;");
		
		VBox kittyMakerL = new VBox(10, tall, smol);
		VBox kittyMakerR = new VBox(10, thicc, hungry);

		HBox container = new HBox(20, kittyMakerL, kittyMakerR, createBtn);
		container.setStyle("-fx-background-color:#DCE3E8; -fx-padding:10px; -fx-border-width:0px 4px 4px 4px; -fx-border-style:dotted; -fx-border-color:#ffffff;");
		return container;
	}
	
	/**
	 * RandomX
	 * @return double - a random valid X coordinate
	 */
	private double randomX() {
		return 50 + Math.random() * ((WIDTH-75) - 50 + 1);
	}
	
	/**
	 * RandomY
	 * @param g - Ground object
	 * @return double - a random valid Y coordinate
	 */
	private double randomY(Ground g) {
		return g.getHeight() + Math.random() * ((HEIGHT-75) - g.getHeight() + 1);
	}
	
	private int randomRGB() {
		return (int)(1 + Math.random() * (255 - 1 + 1));
	}
	
	/**
	 * Random Size
	 * Used for previous assignment, keeping for potential later use.
	 * @return double - a random valid size scale
	 */
	@SuppressWarnings("unused")
	private int randomSize() {
		return (int)(1 + Math.random() * (2 - 1 + 1));
	}
	
	/**
	 * Background Editor
	 * Event Listeners for background changing elements
	 *
	 */
	private class BackgroundEditor implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (e.getSource() == pyramidCheckBox) {
				if (background.pyramidLeft.isVisible() && background.pyramidRight.isVisible()) {
					background.setPyramidVisible(false);
					pyramidCheckBox.setSelected(false);
				} else {
					background.setPyramidVisible(true);
					pyramidCheckBox.setSelected(true);
				}
			} else if (e.getSource() == moonCheckBox) {
				if (background.moon.isVisible()) {
					background.moon.setVisible(false);
					moonCheckBox.setSelected(false);
				} else {
					background.moon.setVisible(true);
					moonCheckBox.setSelected(true);
				}
			} else if (e.getSource() == nightCheckBox) {
				if (background.isNight()) {
					background.setNight(false);
					nightCheckBox.setSelected(false);
					moonCheckBox.setText("Sun");
				} else {
					background.setNight(true);
					nightCheckBox.setSelected(true);
					moonCheckBox.setText("Moon");
				}
			}
		}
	}
	
	/**
	 * KittyChoices
	 * Used to handle radio checks on the Kitty Creator Panel
	 */
	private class KittyChoices implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (e.getSource() == tall) {
				isTall = true;
				isSmol = false;
			} else if (e.getSource() == smol) {
				isTall = false;
				isSmol = true;
			} else if (e.getSource() == thicc) {
				isThicc = true;
				isHungry = false;
			} else if (e.getSource() == hungry) {
				isThicc = false;
				isHungry = true;
			}
			for (int i = 0; i < kittyTracker.size(); i++) {
				if (e.getSource() == kittyVisibilityTracker.get(i)) {
					if (kittyVisibilityTracker.get(i).isSelected()) {
						kittyTracker.get(i).setVisible(true);
					} else {
						kittyTracker.get(i).setVisible(false);
					}
				} else if (e.getSource() == kittyDeleteTracker.get(i)) {
					kittyTracker.get(i).setVisible(false);
					kittyTracker.remove(i);
					kittyVisibilityTracker.remove(i);
					kittyDeleteTracker.remove(i);
					root.setRight(get_currentKitties());
				}
			}
		}
	}
	
	/**
	 * KittyCreator 
	 * Handles Button down events for the kitty creator button and instantiates kitties.
	 */
	private class KittyCreator implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent e) {
			if (e.getSource() == createBtn && kittyTracker.size() < 9) {
				if (isTall && !isSmol) {
					if (isThicc) {
						// create tall thicc kitty
						Kitty cat = new Kitty(Color.rgb(randomRGB(), randomRGB(), randomRGB()), Color.rgb(randomRGB(), randomRGB(), randomRGB()), randomX(), randomY(foreground), 2, 2);
						kittyTracker.add(cat);
						center.getChildren().addAll(cat);
					} else if (isHungry) {
						// create tall hungry kitty
						Kitty cat = new Kitty(Color.rgb(randomRGB(), randomRGB(), randomRGB()), Color.rgb(randomRGB(), randomRGB(), randomRGB()), randomX(), randomY(foreground), 1, 2);
						kittyTracker.add(cat);
						center.getChildren().addAll(cat);
					}
				} else if (isSmol && !isTall) {
					if (isThicc && !isHungry) {
						// create smol thicc kitty
						Kitty cat = new Kitty(Color.rgb(randomRGB(), randomRGB(), randomRGB()), Color.rgb(randomRGB(), randomRGB(), randomRGB()), randomX(), randomY(foreground), 2, 1);
						kittyTracker.add(cat);
						center.getChildren().addAll(cat);
					} else if (isHungry && !isThicc) {
						// create smol hungry kitty
						Kitty cat = new Kitty(Color.rgb(randomRGB(), randomRGB(), randomRGB()), Color.rgb(randomRGB(), randomRGB(), randomRGB()), randomX(), randomY(foreground), 1, 1);
						kittyTracker.add(cat);
						center.getChildren().addAll(cat);
					}
				}
				smol.setSelected(false);
				tall.setSelected(false);
				thicc.setSelected(false);
				hungry.setSelected(false);
				isSmol = false;
				isTall = false;
				isThicc = false;
				isHungry = false;
				root.setRight(get_currentKitties());
			}
		}
	}
	
	/**
	 * Background
	 * Used to create background group of shapes
	 */
	private class Background extends Group{
		private Rectangle backdrop;
		private Ellipse moon;
		private Polygon pyramidLeft;
		private Polygon pyramidRight;
		private Color night = Color.rgb(0, 20, 64);
		private Color day = Color.rgb(173, 213, 247);
		 
		/**
		 * Class Constructor
		 * @param px double - Y-intersect of the top triangle points
		 * @param py double - X-intercept of the top triangle points
		 */
		public Background(double px, double py) {
			backdrop = new Rectangle((double)WIDTH, (double)HEIGHT);
			backdrop.setFill(night);
			backdrop.setY(0);
			backdrop.setX(0);
			
			moon = new Ellipse((double)WIDTH-250, 250.00, 125.00, 125.00);
			moon.setFill(Color.ORANGE);
			
			pyramidLeft = new Polygon();
			pyramidLeft.setStroke(Color.rgb(172, 96, 24));
			pyramidLeft.setStrokeWidth(2);
			pyramidLeft.setFill(Color.rgb(210, 128, 49));
			pyramidLeft.getPoints().addAll(new Double[] {
					px, py,
					px+75, (double)HEIGHT,
					px-400, (double)HEIGHT,
			});
			
			pyramidRight = new Polygon();
			pyramidRight.setStroke(Color.rgb(172, 96, 24));
			pyramidRight.setStrokeWidth(2);
			pyramidRight.setFill(Color.rgb(233, 158, 87));
			pyramidRight.getPoints().addAll(new Double[] {
					px, py,
					px+75, (double)HEIGHT,
					px+400, (double)HEIGHT,
			});
			
			getChildren().addAll(backdrop, moon, pyramidLeft, pyramidRight);
		}
		public void setNight(boolean x) {
			if (x == true) {
				backdrop.setFill(night);
			} else {
				backdrop.setFill(day);
			}
		}
		
		@SuppressWarnings("unused")
		public void setMoonVisible(boolean x) {
			moon.setVisible(x);
		}
		
		public void setPyramidVisible(boolean x) {
			pyramidLeft.setVisible(x);
			pyramidRight.setVisible(x);
		}
		
		public boolean isNight() {
			return (backdrop.getFill() == night) ? true : false;
		}
	}
	
	/**
	 * Ground
	 * Used to create a foreground
	 */
	private class Ground extends Group {
		private Polygon groundLeft;
		private Polygon groundRight;
		private final double heightLeft = 600.00;
		private final double heightRight = 500.00;
		
		/**
		 * Ground
		 * Class Constructor
		 */
		public Ground() {
			groundLeft = new Polygon();
			groundLeft.setFill(Color.rgb(255, 243, 222));
			groundLeft.getPoints().addAll(new Double[] {
					0.00, heightLeft,
					0.00, (double)HEIGHT,
					400.00, (double)HEIGHT,
			});

			groundRight = new Polygon();
			groundRight.setFill(Color.rgb(255, 243, 222));
			groundRight.getPoints().addAll(new Double[] {
					00.00, (double)HEIGHT,
					(double)WIDTH, heightRight,
					(double)WIDTH, (double)HEIGHT,
			});
			
			getChildren().addAll(groundLeft, groundRight);
		}
		
		public double getHeight() {
			return (this.heightLeft > this.heightRight) ? this.heightRight : this.heightLeft;
		}
	}
	
	/**
	 * Kitty
	 * Used to create foreground objects
	 *
	 */
	private class Kitty extends Group {
		private Ellipse paw1;
		private Ellipse paw3;
		private Ellipse tail;
		private Ellipse body;
		private Ellipse head;
		private Ellipse paw2;
		private Ellipse paw4;
		private Ellipse eyeLeft;
		private Ellipse eyeRight;
		private Polygon earLeft;
		private Polygon earRight;
		private Line innerEyeRight;
		private Line innerEyeLeft;
		// Store User Preferences
		private Color colorOne;
		private Color colorTwo;
		private double posX;
		private double posY;
		// To later implement moving kitties
		@SuppressWarnings("unused")
		private double sizeX;
		@SuppressWarnings("unused")
		private double sizeY;

		/**
		 * Kitty
		 * Class Constructor
		 * @param mainColor Color - main color
		 * @param eyeColor Color - eye color
		 * @param centerX double - center X position for the kitty
		 * @param centerY double - center Y position for the kitty
		 * @param scale int - scale of the kitty 
		 */
		public Kitty(Color mainColor, Color eyeColor, double centerX, double centerY, double scaleX, double scaleY) {
			colorOne = mainColor;
			colorTwo = eyeColor;
			posX = centerX;
			posY = centerY;
			sizeX = scaleX;
			sizeY = scaleY;
			
			double radiusX = 90 * scaleX;
			double radiusY = 40 * scaleY;
			double[] range = new double[] {centerX-radiusX, centerX+radiusX, centerY-radiusY, centerY+radiusY};
			
			body = new Ellipse(centerX, centerY, radiusX, radiusY);
			paw1 = new Ellipse(range[0] + radiusX/4, range[3] - radiusY/4, scaleX*15, scaleY*10);
			paw3 = new Ellipse(range[1] - radiusX, range[3] - radiusY/10, scaleX*15, scaleY*10);
			tail = new Ellipse(range[1] - radiusX/8, range[2], scaleX*12, scaleY*52);
			head = new Ellipse(range[0] + radiusX/3, range[2] + radiusY/2, scaleX*40, scaleY*30);
			paw2 = new Ellipse(range[0] + radiusX/1.75, range[3] - radiusY/8, scaleX*15, scaleY*10);
			paw4 = new Ellipse(range[1] - radiusX/3, range[3] - radiusY/6, scaleX*15, scaleY*10);
			eyeLeft = new Ellipse(range[0] + radiusX/8, range[2] + radiusY/2, scaleX*15, scaleY*10);
			eyeRight = new Ellipse(range[0] + radiusX/2, range[2] + radiusY/2, scaleX*15, scaleY*10);
			innerEyeLeft = new Line(range[0] + radiusX/8, range[2] + radiusY/3.5, range[0] + radiusX/8, range[2] + radiusY/1.5);
			innerEyeRight = new Line(range[0] + radiusX/2, range[2] + radiusY/3.5, range[0] + radiusX/2, range[2] + radiusY/1.5);
			earLeft = new Polygon();
			earRight = new Polygon();
			
			innerEyeRight.setStrokeWidth(scaleX*4);
			innerEyeLeft.setStrokeWidth(scaleX*4);
			
			earLeft.getPoints().addAll(new Double[] {
					centerX-(105*scaleX), centerY-(58*scaleY),
					centerX-(75*scaleX), centerY-(48*scaleY),
					centerX-(95*scaleX), centerY-(28*scaleY)
			});
			
			earRight.getPoints().addAll(new Double[] {
					centerX-(15*scaleX), centerY-(58*scaleY),
					centerX-(45*scaleX), centerY-(48*scaleY),
					centerX-(20*scaleX), centerY-(28*scaleY)
			});
			
			paw1.setFill(mainColor);
			paw3.setFill(mainColor);
			tail.setFill(mainColor);
			body.setFill(mainColor);
			head.setFill(mainColor);
			paw2.setFill(mainColor);
			paw4.setFill(mainColor);
			earLeft.setFill(mainColor);
			earRight.setFill(mainColor);
			eyeLeft.setFill(eyeColor);
			eyeRight.setFill(eyeColor);
			
			getChildren().addAll(paw1,paw3,tail,body,head,paw2,paw4,earLeft,earRight,eyeLeft,eyeRight,innerEyeLeft,innerEyeRight);			
		}
		
		/**
		 * Returns a mini duplicate of the caller's kitty (used for UI)
		 * @return Kitty 
		 */
		public Kitty getCopyUI() {
			Kitty copy = new Kitty(colorOne, colorTwo, posX, posY, 0.5, 0.5);
			return copy;
		}
		
		/**
		 * Gets the X center pos
		 * To later implement moving kitties
		 * @return double X
		 */
		@SuppressWarnings("unused")
		public double getX() {
			return this.posX;
		}
		
		/**
		 * Gets the Y center pos
		 * * To later implement moving kitties
		 * @return double Y
		 */
		@SuppressWarnings("unused")
		public double getY() {
			return this.posY;
		}
	}
}
