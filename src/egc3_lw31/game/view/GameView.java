package egc3_lw31.game.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.geom.Position;
import map.MapLayer;
import map.MapPanel;
import map.ToggleAnnotation;

/**
 * The game view panel that will be send through the client's addComponentFactory
 * @author Rocmeister
 *
 */
public class GameView extends JPanel {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -5461051552579021877L;

	/**
	 * Indicates if this client is taking Asia or Europe
	 */
	private int routeOption;
	
	/**
	 * View to model adapter for the game
	 */
	private IGameView2ModelAdapter v2mAdpt;
	
	/**
	 * Number of milliseconds left in timer
	 */
	private int timerMillis = 300000;
	
	/**
	 * The wwd map panel that is the core of the game
	 */
	MapPanel _mapPanel = new MapPanel() {
		/**
		 * 
		 */
		private static final long serialVersionUID = -825135380557129925L;
		
		@Override
		/**
		 * Modify the select listener to toggle ToggleAnnotation objects
		 */
		protected void setupAnnotationToggling() {
			getWWD().addSelectListener(new SelectListener() {
				public void selected(SelectEvent event) {
					if (event.getEventAction().equals(SelectEvent.LEFT_CLICK)) {
						if (event.hasObjects()) {
							Object obj = event.getTopObject();
							if (obj instanceof ToggleAnnotation) {
								ToggleAnnotation annotation = (ToggleAnnotation) obj;
								annotation.toggleText();
								
								// additional actions								
								JFrame quizFrame = city2Quiz.get(annotation.getText());
								quizFrame.setPreferredSize(new Dimension(500, 300));
								quizFrame.pack();
								quizFrame.setLocationRelativeTo(null);
								quizFrame.setVisible(true);

								_mapLayer.removeAnnotation(annotation);
							}
						}
					}
				}
			});
		}
	};
	
	/**
	 * The map layer that displays the annotations
	 */
	MapLayer _mapLayer = new MapLayer();
	
	/* Other less significant GUI components */
	/**
	 * The content panel
	 */
	private JPanel _displayPanel;
	/**
	 * The score text field
	 */
	private JTextField score;
	/**
	 * The control panel
	 */
	private JPanel _ctrlPanel;
	/**
	 * The submit button
	 */
	private final JButton btnSubmit = new JButton("Submit");;
	/**
	 * The submit sub panel
	 */
	private JPanel panelSubmit;
	/**
	 * The status scorll panel
	 */
	private JScrollPane panelStatus;
	/**
	 * The correct answer text area
	 */
	private JTextArea correctAnsTxtFld;
	/**
	 * The countdown timer
	 */
	private Timer timer;
	
	/* Start of Initialization of maps and constants */
	/**
	 * Map from City to Position
	 */
	private Map<String, Position> cityMap = new HashMap<>();
		
	/**
	 * Map from City to JFrame Quizzes
	 */
	private Map<String, JFrame> city2Quiz = new HashMap<>();
	
	/**
	 * Map from City to Points
	 */
	private Map<String, Integer> city2Points = new HashMap<>();

	/**
	 * The clock label
	 */
	private JLabel clockLbl;

	/**
	 * The timer panel
	 */
	private JPanel panelTimer;
	
	/* Static constants */
	// Africa
	/**
	 * Africa's position coordinates in (latitude, longitude)
	 */
	private static Position AFRICA_POSITION = Position.fromDegrees(-8.7832, 34.5085);
	
	// Americas
	/**
	 * Americas' position coordinates in (latitude, longitude)
	 */
	private static Position AMERICAS_POSITION = Position.fromDegrees(54.5260, -105.2551);
	/**
	 * Houston's position coordinates in (latitude, longitude)
	 */
	private static Position HOUSTON_POSITION = Position.fromDegrees(29.7604, -95.3698);
	/**
	 * NYC's position coordinates in (latitude, longitude)
	 */
	private static Position NYC_POSITION = Position.fromDegrees(40.7128, -74.0060);
	/**
	 * Los Angeles' position coordinates in (latitude, longitude)
	 */
	private static Position LA_POSITION = Position.fromDegrees(34.0522, -118.2437);	
	
	// Asia
	/**
	 * Asia's position coordinates in (latitude, longitude)
	 */
	private static Position ASIA_POSITION = Position.fromDegrees(34.0479, 100.6197);
	/**
	 * Japan's position coordinates in (latitude, longitude)
	 */
	private static Position JAPAN_POSITION = Position.fromDegrees(36.2048, 138.2529);
	/**
	 * Korea's position coordinates in (latitude, longitude)
	 */
	private static Position KOREA_POSITION = Position.fromDegrees(35.9078, 127.7669);
	/**
	 * China's position coordinates in (latitude, longitude)
	 */
	private static Position CHINA_POSITION = Position.fromDegrees(39.913818, 116.363625);
	/**
	 * Vietnam's position coordinates in (latitude, longitude)
	 */
	private static Position VIETNAM_POSITION = Position.fromDegrees(14.0583, 108.2772);
	/**
	 * Cambodia's position coordinates in (latitude, longitude)
	 */
	private static Position CAMBODIA_POSITION = Position.fromDegrees(12.5657, 104.9910);
	/**
	 * Mongolia's position coordinates in (latitude, longitude)
	 */
	private static Position MONGOLIA_POSITION = Position.fromDegrees(46.8625, 103.8467);
	/**
	 * Indonesia's position coordinates in (latitude, longitude)
	 */
	private static Position INDONESIA_POSITION = Position.fromDegrees(-0.7893, 113.9213);
	/**
	 * Sri Lanka's position coordinates in (latitude, longitude)
	 */
	private static Position SRILANKA_POSITION = Position.fromDegrees(7.8731, 80.7718);
	/**
	 * Japan2's position coordinates in (latitude, longitude)
	 */
	private static Position JAPAN2_POSITION = Position.fromDegrees(35.0116, 135.7680);
	/**
	 * Bangladesh's position coordinates in (latitude, longitude)
	 */
	private static Position BANGLADESH_POSITION = Position.fromDegrees(23.6850, 90.3563);


	// Europe
	/**
	 * Europe's position coordinates in (latitude, longitude)
	 */
	private static Position EUROPE_POSITION = Position.fromDegrees(60.0279, -3.9123);
	/**
	 * Italy's position coordinates in (latitude, longitude)
	 */
	private static Position ITALY_POSITION = Position.fromDegrees(41.8719, 12.5674);
	/**
	 * Greenland's position coordinates in (latitude, longitude)
	 */
	private static Position GREENLAND_POSITION = Position.fromDegrees(71.7069, -42.6043);
	/**
	 * Slovenia's position coordinates in (latitude, longitude)
	 */
	private static Position SLOVENIA_POSITION = Position.fromDegrees(46.1512, 14.9955);
	/**
	 * Switzerland's position coordinates in (latitude, longitude)
	 */
	private static Position SWITZERLAND_POSITION = Position.fromDegrees(46.8182, 8.2275);
	/**
	 * Mediterranean Sea's position coordinates in (latitude, longitude)
	 */
	private static Position Mediterranean_Sea_POSITION = Position.fromDegrees(34.5531, 18.0480);
	/**
	 * England's position coordinates in (latitude, longitude)
	 */
	private static Position ENGLAND_POSITION = Position.fromDegrees(51.5074, -0.1278);
	/**
	 * France's position coordinates in (latitude, longitude)
	 */
	private static Position FRANCE_POSITION = Position.fromDegrees(46.2276, 2.2137);
	/**
	 * Finland's position coordinates in (latitude, longitude)
	 */
	private static Position FINLAND_POSITION = Position.fromDegrees(61.9241, 25.7482);
	/**
	 * Ladoga's position coordinates in (latitude, longitude)
	 */
	private static Position LADOGA_POSITION = Position.fromDegrees(60.8665, 31.5104);
	/**
	 * Holland's position coordinates in (latitude, longitude)
	 */
	private static Position HOLLAND_POSITION = Position.fromDegrees(52.1326, 5.2913);
	
	// Magic Numbers
	/**
	 * Penalty for opening and skipping a quetion
	 */
	private final int cheat_penalty = -2;
	
	/**
	 * Constructor
	 * @param routeOption -- the route option
	 * @param adpt -- IChatRoomView2ModelAdapter
	 */
	public GameView(int routeOption, IGameView2ModelAdapter adpt) {
		this.routeOption = routeOption;
		this.v2mAdpt = adpt;
		
		/* initialize City to Position Map and City to Points Map */				
		switch(routeOption) {
		// Africa
		case 0:
			 break;
		// Americas
		case 1:
			cityMap.put("Houston", HOUSTON_POSITION);
			cityMap.put("New York City", NYC_POSITION);
			cityMap.put("Los Angeles", LA_POSITION);
			break;
		// Asia	
		case 2:
			// cityMap
			cityMap.put("Japan", JAPAN_POSITION);
			cityMap.put("Cambodia", CAMBODIA_POSITION);
			cityMap.put("Korea", KOREA_POSITION);
			//cityMap.put("Thailand", THAILAND_POSITION);
			cityMap.put("Vietnam", VIETNAM_POSITION);
			cityMap.put("China", CHINA_POSITION);
			cityMap.put("Mongolia", MONGOLIA_POSITION);
			//cityMap.put("Philippines", PHILIPPINES_POSITION);
			cityMap.put("Indonesia", INDONESIA_POSITION);
			//cityMap.put("India", INDIA_POSITION);
			cityMap.put("Sri Lanka", SRILANKA_POSITION);
			cityMap.put("Japan2", JAPAN2_POSITION);
			cityMap.put("Bangladesh", BANGLADESH_POSITION);

			//cityPoints
			city2Points.put("Japan", 4);
			city2Points.put("Cambodia", 2);
			city2Points.put("Korea", 1);
			//city2Points.put("Thailand", 1);
			city2Points.put("Vietnam", 2);
			city2Points.put("China", 1);
			//city2Points.put("Philippines", 2);
			//city2Points.put("India", 2);
			city2Points.put("Indonesia", 3);
			city2Points.put("Mongolia", 1);
			city2Points.put("Sri Lanka", 2);
			city2Points.put("Japan2", 3);
			city2Points.put("Bangladesh", 3);

			break;
			
		// Europe
		case 3:
			// cityMap
			cityMap.put("Italy", ITALY_POSITION);
			cityMap.put("Greenland", GREENLAND_POSITION);
			cityMap.put("Slovenia", SLOVENIA_POSITION);
			cityMap.put("Switzerland", SWITZERLAND_POSITION);
			cityMap.put("Mediterranean Sea", Mediterranean_Sea_POSITION);
			cityMap.put("England", ENGLAND_POSITION);
			cityMap.put("France", FRANCE_POSITION);
			//cityMap.put("Greece", GREECE_POSITION);
			//cityMap.put("Montenegro", MONTENEGRO_POSITION);
			cityMap.put("Finland", FINLAND_POSITION);
			cityMap.put("Ladoga", LADOGA_POSITION);
			cityMap.put("Holland", HOLLAND_POSITION);


			// cityPoints
			city2Points.put("Italy", 1);
			city2Points.put("Greenland", 1);
			city2Points.put("Slovenia", 3);
			city2Points.put("Switzerland", 3);
			city2Points.put("Mediterranean Sea", 2);
			city2Points.put("France", 2);
			//city2Points.put("Greece", 2);
			//city2Points.put("Montenegro", 2);
			city2Points.put("Holland", 3);
			city2Points.put("Ladoga", 4);
			city2Points.put("Finland", 1);
			city2Points.put("England", 2);
			break;
		default:
			break;
		}
		
		/* initialize City to JFrame Map */
		writeQuiz(routeOption);
		
		/* initialize the view framework */
		initGUI();
//		System.out.println("Altitude is: ");
//		System.out.println(_mapPanel.getCurrentAltitude());
	}
	
	/**
	 * Starts the view
	 */
	public void start() {
		timer.start();
		//_mapPanel.start();
		//setVisible(true);
		//setLocationRelativeTo(null);
	}
	
	/**
	 * @param points -- total score
	 */
	public void displayPoints(int points) {
		score.setText(String.valueOf(points));
		setVisible(true);
	}
	
	/**
	 * Initializes the view.
	 */
	private void initGUI() {
		// Initialization Section
//		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//		setBounds(100, 100, 600, 600);
//		this.setTitle("The Silk Road");
//	    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
//	    contentPane.setBounds(150, 150, 150, 150);
//	    contentPane.setLayout(new BorderLayout(0, 0));
//	    setContentPane(contentPane);
	    this.setBorder(new EmptyBorder(5, 5, 5, 5));
	    this.setBounds(150, 150, 150, 150);
	    this.setLayout(new BorderLayout(0, 0));
	    
	    // Bottom display/control panel
	    _displayPanel = new JPanel(new GridLayout(2, 1));
	    _displayPanel.setBackground(UIManager.getColor("EditorPane.selectionBackground"));
	    this.add(_displayPanel, BorderLayout.SOUTH);
	    

	    _ctrlPanel = new JPanel(new FlowLayout());
//	    FlowLayout jPanel1Layout = new FlowLayout();
//	    _ctrlPanel.setLayout(jPanel1Layout);
//	    this.add(_ctrlPanel, BorderLayout.SOUTH);
//	    _ctrlPanel.setBackground(UIManager.getColor("EditorPane.selectionBackground"));

	    // Submit score panel	    
	    panelSubmit = new JPanel();
	    panelSubmit.setBorder(new TitledBorder(null, "Submit score when done!", 
	    		TitledBorder.LEADING, TitledBorder.TOP, null, null));
	    _ctrlPanel.add(panelSubmit);

	    score = new JTextField("0");
	    score.setEditable(false);
	    panelSubmit.add(score);
	    score.setColumns(10);

	    btnSubmit.addActionListener(new ActionListener() {
	        public void actionPerformed(ActionEvent evt) {
	          String s = score.getText();
	          //String team = lblTeam.getText();
	          v2mAdpt.submitScore(Integer.parseInt(s));
	          btnSubmit.setEnabled(false);
	        }
	      });
	    panelSubmit.add(btnSubmit);
	    
	    // Status panel
//	    panelStatus = new JPanel();
//	    panelStatus.setBorder(new TitledBorder(null, "Correct answer is...", TitledBorder.LEADING, TitledBorder.TOP, null, null));
//	    _ctrlPanel.add(panelStatus);
//
//	    correctAnsTxtFld = new JTextField();
//	    correctAnsTxtFld.setEditable(false);
//	    correctAnsTxtFld.setColumns(10);
//	    panelStatus.add(correctAnsTxtFld);

		
	    // MapPanel Section
	    _mapPanel.setPreferredSize(new java.awt.Dimension(900, 600));
	    _mapPanel.start();
		this.add(_mapPanel, BorderLayout.CENTER);
		//pack();
		// MapLayer ToggleAnnotationSection
		this.setAnnotations();
		_mapPanel.addLayer(_mapLayer);
//		double[] newPos = ASIA_POSITION.asDegreesArray();
//		Position newAsia = Position.fromDegrees(newPos[0], newPos[1], 4000000);
//		_mapPanel.setPosition(newAsia, false);
		//mapPanel.setCurrentAltitude(1000);
		
		// center the map properly
		switch(routeOption) {
		case 0: _mapPanel.setPosition(AFRICA_POSITION, false); break;
		case 1: _mapPanel.setPosition(AMERICAS_POSITION, false); break;
		case 2: 
			Position newAsia = Position.fromDegrees(ASIA_POSITION.asDegreesArray()[0], 
					ASIA_POSITION.asDegreesArray()[1], 15000000);
			_mapPanel.setPosition(newAsia, false);
			break;
		case 3: 
			Position newEurope = Position.fromDegrees(EUROPE_POSITION.asDegreesArray()[0], 
					EUROPE_POSITION.asDegreesArray()[1], 10500000);
			_mapPanel.setPosition(newEurope, false);
			break;
		}
		
		
		//Timer panel
		panelTimer = new JPanel();
		panelTimer.setBorder(new TitledBorder(null, "Time", 
		    		TitledBorder.LEADING, TitledBorder.TOP, null, null));
		_ctrlPanel.add(panelTimer);
		clockLbl = new JLabel();
		ActionListener updateClockAction = new ActionListener() {
			  public void actionPerformed(ActionEvent e) {
				  if (timerMillis > 0) {
					  timerMillis = timerMillis - 1000;
					  String clockTime = String.format("%02d:%02d", 
							  TimeUnit.MILLISECONDS.toMinutes(timerMillis) -  
							  TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(timerMillis)), 
							  TimeUnit.MILLISECONDS.toSeconds(timerMillis) - 
							  TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timerMillis)));  
					  clockLbl.setText(clockTime); 
				  }
			   }
		};
		timer = new Timer(1000, updateClockAction);
		panelTimer.add(clockLbl);
		
		// correct answer display
		correctAnsTxtFld = new JTextArea(4, 20);
	    correctAnsTxtFld.setEditable(false);
	    correctAnsTxtFld.setWrapStyleWord(true);
	    
	    panelStatus = new JScrollPane(correctAnsTxtFld, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);    
	    panelStatus.setBorder(new TitledBorder(null, "Correct answer is...", TitledBorder.LEADING, TitledBorder.TOP, null, null));

	    _displayPanel.add(_ctrlPanel, BorderLayout.NORTH);
	    _displayPanel.add(panelStatus, BorderLayout.SOUTH);
	}
	
	/**
	 * Set annotations to the layer by initializing the annotations to cityMap's cities.
	 */
	private void setAnnotations() {
	    _mapLayer.removeAllAnnotations();
	    for (String word : cityMap.keySet()) {
	    	String point = Integer.toString(city2Points.get(word));
	    	Position pos = cityMap.get(word);
	        _mapLayer.addToggleAnnotation(point, word, pos);
	    }
	}
	
	/**
	 * Initialize the quiz frames and add them to the dictionary of quizzes.
	 * @param routeOption -- Asia or Europe
	 */
	private void writeQuiz(int routeOption) {
		switch(routeOption) {
		case 0: writeQuizAfrica(); break;
		case 1: writeQuizAmericas(); break;
		case 2: writeQuizAsia(); break;
		case 3: writeQuizEurope(); break;
		}
	}
	
	/**
	 * Writes Africa quiz helper
	 */
	private void writeQuizAfrica() {}
	
	/**
	 * Helper for writing Americas quiz
	 */
	private void writeQuizAmericas() {
		/* quiz for Houston */
		JFrame houston = new JFrame("Did you know ... about Houston");
		addCloseAction(houston);
		
		JLabel jLabel0 = new JLabel("Does Houston have over 40 colleges?");
	    jLabel0.setFont(new Font("Verdana", 1, 12));
	    
	    JPanel panelAnswer0 = new JPanel(new GridLayout(1, 2));
	    JButton yes0 = new JButton("Yes");
	    panelAnswer0.add(yes0);
	    yes0.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				v2mAdpt.updatePoints(1);
			    houston.dispose();	
			}
		});
	    JButton no0 = new JButton("No");
	    panelAnswer0.add(no0);

		houston.getContentPane().setLayout(new BorderLayout(0, 0));
		houston.getContentPane().add(jLabel0, BorderLayout.NORTH);
		houston.getContentPane().add(panelAnswer0, BorderLayout.SOUTH);
	    	    
		this.city2Quiz.put("Houston", houston);
		
		/* quiz for NYC */
		JFrame nyc = new JFrame("Did you know ... about New York City");
		addCloseAction(nyc);
		
		JLabel jLabel1 = new JLabel("New York City");
	    jLabel1.setFont(new Font("Verdana",1,12));
	    JButton yes1 = new JButton("Yes");
	    JButton no1 = new JButton("No");
	    JPanel panelAnswer1 = new JPanel(new GridLayout(1, 2));
	    panelAnswer1.add(yes1);
	    panelAnswer1.add(no1);
	    
		nyc.getContentPane().setLayout(new BorderLayout(0, 0));
		nyc.getContentPane().add(jLabel1, BorderLayout.NORTH);
		nyc.getContentPane().add(panelAnswer1, BorderLayout.SOUTH);
		this.city2Quiz.put("New York City", nyc);
		
		/* quiz for LA */
		JFrame la = new JFrame("Did you know ... about Los Angeles");
		addCloseAction(la);
		
		JLabel jLabel2 = new JLabel("Los Angeles");
	    jLabel2.setFont(new Font("Verdana",1,12));
	    JButton yes2 = new JButton("Yes");
	    JButton no2 = new JButton("No");
	    JPanel panelAnswer2 = new JPanel(new GridLayout(1, 2));
	    panelAnswer2.add(yes2);
	    panelAnswer2.add(no2);
	    
		la.getContentPane().setLayout(new BorderLayout(0, 0));
		la.getContentPane().add(jLabel2, BorderLayout.NORTH);
		la.getContentPane().add(panelAnswer2, BorderLayout.SOUTH);
		this.city2Quiz.put("Los Angeles", la);
	}
		
	/**
	 * Helper for writing Asia's quiz
	 */
	private void writeQuizAsia() {
		/* quiz for China */
		JFrame china = new JFrame("Did you know ... about China?");
		addCloseAction(china);
		
		JLabel jLabel0 = new JLabel("What is the main color of China’s flag?");
	    jLabel0.setFont(new Font("Verdana", 1, 12));
	    
	    JPanel panelAnswer0 = new JPanel(new GridLayout(1, 4));
	    JButton Btn00 = buttonMaker("green", china, 0, "red\nChina’s flag is a large red field with yellow stars and a hammer and sickle device.");
	    JButton Btn01 =  buttonMaker("white", china, 0, "red\nChina’s flag is a large red field with yellow stars and a hammer and sickle device.");
	    JButton Btn02 =  buttonMaker("red", china, 1, "Correct!\nChina’s flag is a large red field with yellow stars and a hammer and sickle device.");
	    JButton Btn03 =  buttonMaker("blue", china, 0, "red\nChina’s flag is a large red field with yellow stars and a hammer and sickle device.");

	    panelAnswer0.add(Btn00);
	    panelAnswer0.add(Btn01);
	    panelAnswer0.add(Btn02);
	    panelAnswer0.add(Btn03);

	    china.getContentPane().setLayout(new BorderLayout(0, 0));
	    china.getContentPane().add(jLabel0, BorderLayout.NORTH);
	    china.getContentPane().add(panelAnswer0, BorderLayout.SOUTH);
			    
		this.city2Quiz.put("China", china);

		/* quiz for Mongolia */
		JFrame mongolia = new JFrame("Did you know ... about Mongolia");
		addCloseAction(mongolia);
		
		JLabel jLablMongolia = new JLabel("Which desert is in Mongolia?");
		jLablMongolia.setFont(new Font("Verdana", 1, 12));

	    JPanel panelAnswer1 = new JPanel(new GridLayout(1, 3));
	    JButton desert1Btn = buttonMaker("Taklamakan Desert", mongolia, 0, "Gobi Desert\nThe Gobi Desert is a large desert region in Asia. \nIt covers parts of northern and northwestern China, and of southern Mongolia.");
	    JButton desert2Btn =  buttonMaker("Gobi Desert", mongolia, 1, "Correct!\nThe Gobi Desert is a large desert region in Asia. \nIt covers parts of northern and northwestern China, and of southern Mongolia.");
	    JButton desert3Btn =  buttonMaker("Ordos Desert", mongolia, 0, "Gobi Desert\nThe Gobi Desert is a large desert region in Asia. \nIt covers parts of northern and northwestern China, and of southern Mongolia.");

	    panelAnswer1.add(desert1Btn);
	    panelAnswer1.add(desert2Btn);
	    panelAnswer1.add(desert3Btn);

	    mongolia.getContentPane().setLayout(new BorderLayout(0, 0));
	    mongolia.getContentPane().add(jLablMongolia, BorderLayout.NORTH);
	    mongolia.getContentPane().add(panelAnswer1, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Mongolia", mongolia);
		
		/* quiz for Korea */
		JFrame korea = new JFrame("Did you know ... about Korea");
		addCloseAction(korea);
		
		JLabel jLblKorea = new JLabel("What are dumplings in Korea called?");
		jLblKorea.setFont(new Font("Verdana", 1, 12));

	    JPanel panelAnswer2 = new JPanel(new GridLayout(1, 3));
	    JButton dumpling1Btn = buttonMaker("Gyoza", korea, 0, "Mandu\nMandu are dumplings in Korean cuisine. \nMandu can be steamed, boiled, pan-fried, or deep-fried.\nMandu were long part of Korean royal court cuisine.");
	    JButton dumpling2Btn =  buttonMaker("Jiaozi", korea, 0, "Mandu\nMandu are dumplings in Korean cuisine. \nMandu can be steamed, boiled, pan-fried, or deep-fried.\nMandu were long part of Korean royal court cuisine.");
	    JButton dumpling3Btn =  buttonMaker("Mandu", korea, 1, "Correct!\nMandu are dumplings in Korean cuisine. \nMandu can be steamed, boiled, pan-fried, or deep-fried.\nMandu were long part of Korean royal court cuisine.");

	    panelAnswer2.add(dumpling1Btn);
	    panelAnswer2.add(dumpling2Btn);
	    panelAnswer2.add(dumpling3Btn);

	    korea.getContentPane().setLayout(new BorderLayout(0, 0));
	    korea.getContentPane().add(jLblKorea, BorderLayout.NORTH);
	    korea.getContentPane().add(panelAnswer2, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Korea", korea);
		
		/* quiz for Japan */
		JFrame japan = new JFrame("Did you know ... about Japan");
		addCloseAction(japan);
		
		JLabel jLblJapan = new JLabel("What can you not find at a 7-Eleven in Japan?");
		jLblJapan.setFont(new Font("Verdana", 1, 12));

	    JPanel panelAnswerJapan = new JPanel(new GridLayout(1, 4));
	    JButton japan1Btn = buttonMaker("Hard boiled eggs", japan, -3, "Doritos chips");
	    JButton japan2Btn =  buttonMaker("Alcohol juice boxes", japan, -3, "Doritos chips");
	    JButton japan3Btn =  buttonMaker("Doritos chips", japan, 4, "Doritos chips");
	    JButton japan4Btn =  buttonMaker("Avocado chips", japan, -3, "Doritos chips");

	    panelAnswerJapan.add(japan1Btn);
	    panelAnswerJapan.add(japan2Btn);
	    panelAnswerJapan.add(japan3Btn);
	    panelAnswerJapan.add(japan4Btn);

	    japan.getContentPane().setLayout(new BorderLayout(0, 0));
	    japan.getContentPane().add(jLblJapan, BorderLayout.NORTH);
	    japan.getContentPane().add(panelAnswerJapan, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Japan", japan);
		
		/* quiz for Thailand */
		JFrame bang = new JFrame("Did you know ... about Bangladesh");
		addCloseAction(bang);
		
		JLabel jLblThailand = new JLabel("What is the capital of Bangladesh?");
		jLblThailand.setFont(new Font("Verdana", 1, 12));
		
		String msgB = "Dhaka, the capital of Bangladesh, lies on the Buriganga River in the center of the country.";
		JPanel panelAnswerThailand = new JPanel(new GridLayout(1, 4));
	    JButton bBtn1 = buttonMaker("Dhaka", bang, 3, "Correct! \n" + msgB);
	    JButton bBtn2 = buttonMaker("Deccan", bang, -2, "Dhaka \n" + msgB);
	    JButton bBtn3 = buttonMaker("Accra", bang, -2, "Dhaka \n" + msgB);
	    JButton bBtn4 = buttonMaker("Aden", bang, -2, "Dhaka \n" + msgB);

	    panelAnswerThailand.add(bBtn1);
	    panelAnswerThailand.add(bBtn2);
	    panelAnswerThailand.add(bBtn3);
	    panelAnswerThailand.add(bBtn4);

	    bang.getContentPane().setLayout(new BorderLayout(0, 0));
	    bang.getContentPane().add(jLblThailand, BorderLayout.NORTH);
	    bang.getContentPane().add(panelAnswerThailand, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Bangladesh", bang);
		
		/* quiz for Vietnam */
		JFrame vietnam = new JFrame("Did you know ... about the Saigon River");
		addCloseAction(vietnam);
		
		JLabel jLblVietnam = new JLabel("Which of these cities lies on the Saigon River?");
		jLblVietnam.setFont(new Font("Verdana", 1, 12));

	    JPanel pnlAnswerVietnam = new JPanel(new GridLayout(1, 4));
	    String msgV = "Ho Chi Minh City, Vietnam, was once called Saigon and was the capital of South Vietnam. \nWhen the Vietnam War ended, the country unified.";
	    JButton vBtn1 = buttonMaker("Hong Kong", vietnam, -1, "Ho Chi Minh City\n" + msgV);
	    JButton vBtn2 =  buttonMaker("HaiPhong", vietnam, -1, "Ho Chi Minh City\n" + msgV);
	    JButton vBtn3 =  buttonMaker("Ho Chi Minh City", vietnam, 2, "Correct!n" + msgV);
	    JButton vBtn4 =  buttonMaker("Hanoi", vietnam, -1, "Ho Chi Minh City\n" + msgV);

	    pnlAnswerVietnam.add(vBtn1);
	    pnlAnswerVietnam.add(vBtn2);
	    pnlAnswerVietnam.add(vBtn3);
	    pnlAnswerVietnam.add(vBtn4);
	    
	    vietnam.getContentPane().setLayout(new BorderLayout(0, 0));
	    vietnam.getContentPane().add(jLblVietnam, BorderLayout.NORTH);
	    vietnam.getContentPane().add(pnlAnswerVietnam, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Vietnam", vietnam);
		
		/* quiz for Cambodia */
		JFrame cambodia = new JFrame("Did you know ... about Cambodia");
		addCloseAction(cambodia);
		
		JLabel jLblCambodia = new JLabel("Which is not an ancient ruin in Cambodia?");
		jLblCambodia.setFont(new Font("Verdana", 1, 12));

	    JPanel pnlAnswerCambodia = new JPanel(new GridLayout(1, 3));
	    String msgCamb = "Located in the Tandjouaré Prefecture in the Savanes Region of northern Togo, \nAfrica, the caves of Nôk and Mamproug contain shelter and refuge structures built between the 17th and 19th centuries mainly for defensive purposes.";
	    JButton cBtn1 = buttonMaker("Angkor Wat", cambodia, -1, "Nok Caves\n" + msgCamb);
	    JButton cBtn2 =  buttonMaker("Preah Khan", cambodia, -1, "Nok Caves\n" + msgCamb);
	    JButton cBtn3 =  buttonMaker("Nok Caves", cambodia, 2, "Correct! \n" + msgCamb);

	    pnlAnswerCambodia.add(cBtn1);
	    pnlAnswerCambodia.add(cBtn2);
	    pnlAnswerCambodia.add(cBtn3);
	    
	    cambodia.getContentPane().setLayout(new BorderLayout(0, 0));
	    cambodia.getContentPane().add(jLblCambodia, BorderLayout.NORTH);
	    cambodia.getContentPane().add(pnlAnswerCambodia, BorderLayout.SOUTH);
		
		this.city2Quiz.put("Cambodia", cambodia);
		
	    // question about Sri Lanka
		JFrame sl = new JFrame("Did you know ... about Sri Lanka?");
		addCloseAction(sl);
		
 		JLabel jLabel9 = new JLabel("How far, in kilometers, is Sri Lanka from India?");
 		jLabel9.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer9 = new JPanel(new GridLayout(1, 4));
 	    JButton b90 = buttonMaker("127", sl, -1, "29 \nSri Lanka is a pear-shaped island about 18 miles (29 kilometers) from the southeastern tip of India.");
 	    JButton b91 = buttonMaker("251", sl, -1, "29 \nSri Lanka is a pear-shaped island about 18 miles (29 kilometers) from the southeastern tip of India.");
 	    JButton b92 = buttonMaker("29", sl, 2, "Correct! \nSri Lanka is a pear-shaped island about 18 miles (29 kilometers) from the southeastern tip of India.");
 	    JButton b93 = buttonMaker("44", sl, -1, "29 \nSri Lanka is a pear-shaped island about 18 miles (29 kilometers) from the southeastern tip of India.");
 	    
 	    panelHelper("Sri Lanka", sl, jLabel9, panelAnswer9, b90, b91, b92, b93);
 	    
	    // question about Indonesia
		JFrame indonesia = new JFrame("Did you know ... about Indonesia?");
		addCloseAction(indonesia);
		
 		JLabel jLabel10 = new JLabel("What is the capital of Indonesia?");
 		jLabel10.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer10 = new JPanel(new GridLayout(1, 4));
 	    String msg10 = "Indonesia’s capital is Jakarta. \nThe city is located in the northwest of the island of Java.";
 	    JButton b100 = buttonMaker("Kuala Lumpur",indonesia, -2, "Jakarta\n" + msg10);
 	    JButton b101 = buttonMaker("Port Moresby", indonesia, -2, "Jakarta\n" + msg10);
 	    JButton b102 = buttonMaker("Medan", indonesia, -2, "Jakarta\n" + msg10);
 	    JButton b103 = buttonMaker("Jakarta", indonesia, 3, "Correct!\n" + msg10);
 	    
 	    panelHelper("Indonesia", indonesia, jLabel10, panelAnswer10, b100, b101, b102, b103);
 	    
	    // question about india
		JFrame jp2 = new JFrame("Did you know ... about Japan?");
		addCloseAction(jp2);
		
 		JLabel jLabel3 = new JLabel("When did Buddhism arrive in Japan?");
 		jLabel3.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 		String msgjp = "During the Yamato period, about 400 CE, Buddhism came to Japan from Korea. \nFor many centuries the Japanese also borrowed heavily from Chinese culture, \nusing Chinese characters to write down the Japanese language.";
 	    JPanel panelAnswer3 = new JPanel(new GridLayout(1, 4));
 	    JButton b30 = buttonMaker("400 CE",jp2, 3, "Correct!\n" + msgjp);
 	    JButton b31 = buttonMaker("200 BCE", jp2, -2, "400 CE\n" + msgjp);
 	    JButton b32 = buttonMaker("1 CE", jp2, -2, "400 CE\n" + msgjp);
 	    JButton b33 = buttonMaker("1200 CE", jp2, -2, "400 CE\n" + msgjp);
 	    
 	    panelHelper("Japan2", jp2, jLabel3, panelAnswer3, b30, b31, b32, b33);
	}
	
	/**
	 * 
	 */
	private void writeQuizEurope() {
		/* quiz for Italy */
		JFrame italy = new JFrame("Did you know ... about Italy");
		addCloseAction(italy);
		
		// question
		JLabel jLabel0 = 
				new JLabel("Which of the following dumplings did not originate in Italy?");
	    jLabel0.setFont(new Font("Verdana", 1, 12));
	    
	    // answer buttons
	    JPanel panelAnswer0 = new JPanel(new GridLayout(1, 4));
	    JButton b00 = buttonMaker("Ravioli", italy, 0, "Pierogi\nPierogi are of Polish origin.");
	    JButton b01 = buttonMaker("Tortellini", italy, 0, "Pierogi\nPierogi are of Polish origin.");
	    JButton b02 = buttonMaker("Agnolotti", italy, 0, "Pierogi\nPierogi are of Polish origin.");
	    JButton b03 = buttonMaker("Pierogi", italy, 1, "Correct!\nPierogi are of Polish origin.");

	    panelAnswer0.add(b00);
	    panelAnswer0.add(b01);
	    panelAnswer0.add(b02);
	    panelAnswer0.add(b03);

		italy.getContentPane().setLayout(new BorderLayout(0, 0));
		italy.getContentPane().add(jLabel0, BorderLayout.NORTH);
		italy.getContentPane().add(panelAnswer0, BorderLayout.SOUTH);
	    	    
		this.city2Quiz.put("Italy", italy);
		
		/* quiz for Greenland */
		JFrame greenland = new JFrame("Did you know ... about Greenland");
		addCloseAction(greenland);
		
		// question
		JLabel jLabel1 = 
				new JLabel("Greenland’s official name is:");
	    jLabel1.setFont(new Font("Verdana", 1, 12));
	    
	    // answer buttons
	    JPanel panelAnswer1 = new JPanel(new GridLayout(1, 4));
	    JButton b10 = buttonMaker("Nippon", greenland, 0, "Kalaallit Nunaat\nThe name of the country in the indigenous Greenlandic language is Kalaallit Nunaat"
	    		+ "(\"land of the Kalaallit\"). \nThe Kalaallit are the indigenous Greenlandic Inuit people who inhabit the country's western region.");
	    JButton b11 = buttonMaker("Kalaallit Nunaat", greenland, 1, "Correct!\nThe name of the country in the indigenous Greenlandic language is Kalaallit Nunaat"
	    		+ "(\"land of the Kalaallit\").\nThe Kalaallit are the indigenous Greenlandic Inuit people who inhabit the country's western region.");
	    JButton b12 = buttonMaker("Deutschland", greenland, 0, "Kalaallit Nunaat\nThe name of the country in the indigenous Greenlandic language is Kalaallit Nunaat"
	    		+ "(\"land of the Kalaallit\").\nThe Kalaallit are the indigenous Greenlandic Inuit people who inhabit the country's western region.");
	    JButton b13 = buttonMaker("Karnaka", greenland, 0, "Kalaallit Nunaat\nThe name of the country in the indigenous Greenlandic language is Kalaallit Nunaat"
	    		+ "(\"land of the Kalaallit\").\nThe Kalaallit are the indigenous Greenlandic Inuit people who inhabit the country's western region.");
	    
	    panelHelper("Greenland", greenland, jLabel1, panelAnswer1, b10, b11, b12, b13);
		
		/* quiz for Slovenia */
		JFrame slovenia = new JFrame("Did you know ... about Slovenia");
		addCloseAction(slovenia);
		
		// question
		JLabel jLabel2 = new JLabel("What is the capital of Slovenia?");
	    jLabel2.setFont(new Font("Verdana", 1, 12));
	    
	    // answer buttons
	    JPanel panelAnswer2 = new JPanel(new GridLayout(1, 4));
	    JButton b20 = buttonMaker("Vienna", slovenia, -2, 
	    		"Ljubljana\nSlovenia’s capital is Ljubljana. \nIt has a population of about 280,000 and a small size—you can walk from one side of the city to the other in just a few minutes.");
	    JButton b21 = buttonMaker("Yerevan", slovenia, -2, "Correct!\nSlovenia’s capital is Ljubljana. \nIt has a population of about 280,000 and a small size—you can walk from one side of the city to the other in just a few minutes.");
	    JButton b22 = buttonMaker("Ljubljana", slovenia, 3, "Ljubljana\nSlovenia’s capital is Ljubljana. \nIt has a population of about 280,000 and a small size—you can walk from one side of the city to the other in just a few minutes.");
	    JButton b23 = buttonMaker("Zagreb", slovenia, -2, "Ljubljana\nSlovenia’s capital is Ljubljana. \nIt has a population of about 280,000 and a small size—you can walk from one side of the city to the other in just a few minutes.");
	    
	    panelHelper("Slovenia", slovenia, jLabel2, panelAnswer2, b20, b21, b22, b23);
	    
	    /* quiz for Switzerland */
		JFrame swiss = new JFrame("Did you know ... about Switzerland");
		addCloseAction(swiss);
		
		// question
		JLabel jLabel3 = new JLabel("Which are Switzerland’s official national languages?");
	    jLabel3.setFont(new Font("Verdana", 1, 12));
	    
	    // answer buttons
	    JPanel panelAnswer3 = new JPanel(new GridLayout(3, 1));
	    JButton b30 = buttonMaker("French, English, German and Dutch", swiss, -2, "German, Italian, French and Romansh");
	    JButton b31 = buttonMaker("German, Italian, French and Romansh", swiss, 3, "Correct! \nIt's German, Italian, French and Romansh");
	    JButton b32 = buttonMaker("German, French, Bavarian and English", swiss, -2, "German, Italian, French and Romansh");
	    
	    panelHelper("Switzerland", swiss, jLabel3, panelAnswer3, b30, b31, b32);
	    
	    /* quiz for Mediterranean Sea */
		JFrame med = new JFrame("Did you know ... about the Mediterranean Sea");
		addCloseAction(med);
		
		// question
		JLabel jLabel4 = new JLabel("Which of these European countries has a coast on the Mediterranean Sea?");
	    jLabel4.setFont(new Font("Verdana", 1, 12));
	    
	    // answer buttons
	    JPanel panelAnswer4 = new JPanel(new GridLayout(1, 3));
	    JButton b40 = buttonMaker("Macedonia", med, -1, "Croatia");
	    JButton b41 = buttonMaker("Croatia", med, 2, "Correct! \nIt's Croatia.");
	    JButton b42 = buttonMaker("Romania", med, -1, "Croatia");
	    
	    panelHelper("Mediterranean Sea", med, jLabel4, panelAnswer4, b40, b41, b42);
	    
	    // question about london
		JFrame eng = new JFrame("Did you know ... about England?");
		addCloseAction(eng);
		
 		JLabel jLabel5 = new JLabel("How many Michelin starred Indian restaurants are there in London?");
 		jLabel5.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer5 = new JPanel(new GridLayout(1, 3));
 	    JButton b50 = buttonMaker("2",eng, -1, "7");
 	    JButton b51 = buttonMaker("7", eng, 2, "7");
 	    JButton b52 = buttonMaker("9", eng, -1, "7");
 	    
 	    panelHelper("England", eng, jLabel5, panelAnswer5, b50, b51, b52);
 	    
 	    // question about holland
		JFrame holland = new JFrame("Did you know ... about Holland?");
		addCloseAction(holland);
		
 		JLabel jLabel6 = new JLabel("What is Holland’s seat of government?");
 		jLabel6.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer6 = new JPanel(new GridLayout(1, 4));
 	    JButton b60 = buttonMaker("Rotterdam",holland, -2, "The Hague\nThe Hague is the seat of government of Holland, also called The Netherlands. \nThe capital, however, is Amsterdam.");
 	    JButton b61 = buttonMaker("Amsterdam", holland, -2, "The Hague\nThe Hague is the seat of government of Holland, also called The Netherlands. \nThe capital, however, is Amsterdam.");
 	    JButton b62 = buttonMaker("The Hague",holland, 3, "Correct!\nThe Hague is the seat of government of Holland, also called The Netherlands. \nThe capital, however, is Amsterdam.");
 	    JButton b63 = buttonMaker("Utrecht",holland, -2, "The Hague\nThe Hague is the seat of government of Holland, also called The Netherlands. \nThe capital, however, is Amsterdam.");

 	    panelHelper("Holland", holland, jLabel6, panelAnswer6, b60, b61, b62, b63);
 	    
 	    // question about france
		JFrame france = new JFrame("Did you know ... about France?");
		addCloseAction(france);
		
 		JLabel jLabel7 = new JLabel("Counting its overseas territories, how many time zones does France have?");
 		jLabel7.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer7 = new JPanel(new GridLayout(1, 3));
 	    JButton b70 = buttonMaker("3",france, -1, "12\nFrance has twelve time zones including those of Metropolitan France, \nFrench Guiana and numerous islands, inhabited and uninhabited.");
 	    JButton b71 = buttonMaker("7", france, -1, "12\nFrance has twelve time zones including those of Metropolitan France, \nFrench Guiana and numerous islands, inhabited and uninhabited.");
 	    JButton b72 = buttonMaker("12", france, 2, "Correct!\nFrance has twelve time zones including those of Metropolitan France, \nFrench Guiana and numerous islands, inhabited and uninhabited.");
 	    
 	    panelHelper("France", france, jLabel7, panelAnswer7, b70, b71, b72);
 	    
	    // question about greece
		JFrame finland = new JFrame("Did you know ... about Finland?");
		addCloseAction(finland);
		
 		JLabel jLabel8 = new JLabel("Finland is the home of Santa Claus.");
 		jLabel8.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer8 = new JPanel(new GridLayout(1, 3));
 	    JButton b80 = buttonMaker("True",finland, 0, "Correct!\nYou can meet Santa Claus and cross the magical Arctic Circle every day at the Santa Claus Village in Rovaniemi in Lapland, Finland. \nRovaniemi is the Official Hometown of Santa Claus in Lapland.");
 	    JButton b81 = buttonMaker("False", finland, 1, "True\nYou can meet Santa Claus and cross the magical Arctic Circle every day at the Santa Claus Village in Rovaniemi in Lapland, Finland. \nRovaniemi is the Official Hometown of Santa Claus in Lapland.");
 	    
 	    panelHelper("Finland", finland, jLabel8, panelAnswer8, b80, b81);
 	    
	    // question about greece
		JFrame lake = new JFrame("Did you know ... about this lake?");
		addCloseAction(lake);
		
 		JLabel jLabel9 = new JLabel("What is the largest lake in Europe?");
 		jLabel9.setFont(new Font("Verdana", 1, 12));
 	    
 	    // answer buttons
 	    JPanel panelAnswer9 = new JPanel(new GridLayout(1, 3));
 	    JButton b90 = buttonMaker("Lake Baikal", lake, -3, "Lake Ladoga\nLake Ladoga is Europe’s largest lake. \nIt is 6,700 square miles (17,600 square km) in area—exclusive of islands—and 136 miles (219 km) long, \nwith an average width of 51 miles (82 km) and an average depth of 167 feet (51 m).");
 	    JButton b91 = buttonMaker("the Mediterranean Sea", lake, -3, "Lake Ladoga\nLake Ladoga is Europe’s largest lake. \nIt is 6,700 square miles (17,600 square km) in area—exclusive of islands—and 136 miles (219 km) long,\\nwith an average width of 51 miles (82 km) and an average depth of 167 feet (51 m).");
 	    JButton b92 = buttonMaker("Lake Titicaca", lake, -3, "Lake Ladoga\nLake Ladoga is Europe’s largest lake. \nIt is 6,700 square miles (17,600 square km) in area—exclusive of islands—and 136 miles (219 km) long, \nwith an average width of 51 miles (82 km) and an average depth of 167 feet (51 m).");
 	    JButton b93 = buttonMaker("Lake Ladoga", lake, 4, "Lake Ladoga\nLake Ladoga is Europe’s largest lake. \nIt is 6,700 square miles (17,600 square km) in area—exclusive of islands—and 136 miles (219 km) long, \nwith an average width of 51 miles (82 km) and an average depth of 167 feet (51 m).");
 	    
 	    panelHelper("Ladoga", lake, jLabel9, panelAnswer9, b90, b91, b92, b93);
 	    
	}
	
	
	/* Helper methods */
	/**
	 * @param label -- string title
	 * @param frame -- frame to display
	 * @param increment -- increment of points
	 * @param correctAns -- correct answer
	 * @return a factorized jbutton
	 */
	private JButton buttonMaker(String label, JFrame frame, int increment, String correctAns) {
		JButton btn = new JButton(label);
	    btn.addActionListener(new ActionListener() {
	    	@Override
			public void actionPerformed(ActionEvent e) {
				v2mAdpt.updatePoints(increment);
			    frame.dispose();	
			    correctAnsTxtFld.setText(correctAns);
			}
		});
	    return btn;
	}
	
	/**
	 * @param f -- the JFrame in question
	 */
	private void addCloseAction(JFrame f) {
		f.addWindowListener(new WindowAdapter()
        {
            @Override
            public void windowClosing(WindowEvent e)
            {
                System.out.println("Closed");
                v2mAdpt.updatePoints(cheat_penalty);
                e.getWindow().dispose();
            }
        });
	}
	
	/**
	 * @param frameName -- name of the frame
	 * @param mainFrame -- the main frame
	 * @param question -- the question string
	 * @param panelAnswer -- the answer panel
	 * @param buttons -- the array of buttons to be added
	 */
	private void panelHelper(String frameName, JFrame mainFrame, JLabel question, JPanel panelAnswer, JButton... buttons) {
		for (JButton b : buttons) {
			panelAnswer.add(b);
		}

		mainFrame.getContentPane().setLayout(new BorderLayout(0, 0));
		mainFrame.getContentPane().add(question, BorderLayout.NORTH);
		mainFrame.getContentPane().add(panelAnswer, BorderLayout.SOUTH);
	    	    
		this.city2Quiz.put(frameName, mainFrame);
	}
	
//	private void quizFrameMaker(String cityKey, JLabel question, JPanel answerPanel) {
//		JFrame newFrame = new JFrame("Did you know ... about "+ cityKey);	    
//	    JLabel jLabel = question;
//	    JPanel panelAnswer = answerPanel;
//		newFrame.getContentPane().setLayout(new BorderLayout(0, 0));
//		newFrame.getContentPane().add(jLabel, BorderLayout.NORTH);
//		newFrame.getContentPane().add(panelAnswer, BorderLayout.SOUTH);
//		addCloseAction(newFrame);
//	    	    
//		this.city2Quiz.put(cityKey, newFrame);
//	}
}











