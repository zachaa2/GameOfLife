package main.java;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;



public class GUI extends JFrame {
    
    /**
     * Grid object which will act as the "engine" of the simulation
     * All changes to the grid will be initiated from the UI and be
     * reflected back to the UI 
     */
    Grid g = new Grid();

    /**
     * path to input file
     */
    String inputPath = "";
    /**
     * Overlying panel which will hold the sub panels
     */
    private static JPanel parentPanel = new JPanel(new BorderLayout());
    /**
     * Panel to hold the grid layout
     */
    JPanel mainPanel = new JPanel();
    /**
     * Panel to act as the footer, may hold info
     */
    JPanel footer = new JPanel();
    /**
     * Panel to the left of the Grid. May hold info
     */
    JPanel leftPanel = new JPanel();
    /**
     * Panel to the right of the Grid. May hold info.
     */
    JPanel rightPanel = new JPanel();
    /**
     * Panel to act as the header, holds the Title and may hold other info
     */
    JPanel header = new JPanel();
    /**
     * Title object, represented as a text area
     */
    JTextArea title = new JTextArea("Game Of Life Universe");
    /**
     * ToolBar Object, to hold common functions in the form of pressable buttons
     */
    JToolBar bar = new JToolBar();
    /**
     * Label for the current tick info
     */
    JLabel tickLabel = new JLabel();
    /**
     * Label to display tick information
     */
    JLabel tickRangeLabel = new JLabel();
    /**
     * Label to display the number of living cells
     */
    JLabel livingLabel = new JLabel();
    /**
     * Label to display the maximum generation value
     */
    JLabel limitLabel = new JLabel();
    /**
     * label to display the output filepattern name
     */
    JLabel outputLabel = new JLabel();

    /**
     * constructor
     * @param name name for the JFrame
     */
    public GUI(String name){
        super(name);
        setResizable(false);
        setSize(800, 600);
        setTitle("Game Of Life");
        createMenuBar();
        createGrid();
        createToolBar();
        setLabels();
        leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.PAGE_AXIS));
        
    }

    /**
     * Function to initialize the labels on the side of the application. 
     * These labels are used for displaying relevant info about the 
     * state of the grid, and some application settings.
     */
    public void setLabels(){
        
        // set fonts for the labels
        tickLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
        tickRangeLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
        livingLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
        limitLabel.setFont(new Font("Verdana", Font.PLAIN, 13));
        outputLabel.setFont(new Font("Verdana", Font.PLAIN, 13));

        // assign the text for each label
        updateText();

        //add to panel
        leftPanel.add(Box.createRigidArea(new Dimension(0,40)));
        leftPanel.add(limitLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(0,10)));
        leftPanel.add(tickLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(5, 10)));
        leftPanel.add(tickRangeLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(5,10)));
        leftPanel.add(livingLabel);
        leftPanel.add(Box.createRigidArea(new Dimension(5,10)));
        leftPanel.add(outputLabel);
    }

    /**
     * Helper function to update the text for each label after any settings are changed,
     * or when the grid changes.
     */
    public void updateText(){
        // assign the text for each label
        tickLabel.setText("Tick: " + Integer.toString(g.getTick()));
        tickRangeLabel.setText("Saving ticks from " + Integer.toString(g.tickRange[0]) + " to " + Integer.toString(g.tickRange[1]));
        String l = this.g.g == null ? "0" : Integer.toString(g.getLivingCells());
        livingLabel.setText("# of Living Cells: " + l);
        limitLabel.setText("Max Tick: " + Integer.toString(g.getTotalTick()));
        outputLabel.setText("Output Pattern: " + g.getPattern());

    }


    /**
     * Function to easily set the background color of the JFrame
     * @param c color to set the background to
     */
    public void setColor(Color c){
        this.getContentPane().setBackground(c);
        mainPanel.setBackground(c);
        footer.setBackground(c);
        header.setBackground(c);
        leftPanel.setBackground(c);
        rightPanel.setBackground(c);
        title.setBackground(c);
        bar.setBackground(c);
    }

    

    /**
     * Helper function to update the Grid UI. It will recreate a new Grid
     * with the row and col attributes from the Grid object. 1 is Green, and 
     * everything else is Red. 
     */
    private void updateGrid(){

        mainPanel.removeAll();
        mainPanel.setBackground(mainPanel.getBackground());
        mainPanel.repaint();
        mainPanel.setLayout(new GridLayout(g.getRow(), g.getCol(), 10, 10));
        int[][] data = g.getGrid();
        for(int i = 0; i < data.length; i++){
            for(int j = 0;j < data[i].length; j++){
                JButton b = new JButton();
                if(data[i][j] == 1){
                    b.setBackground(Color.GREEN);
                } else {
                    b.setBackground(Color.RED);
                }
                mainPanel.add(b);
            }
        }
        mainPanel.revalidate();
        // write grid to output file
        if(g.currentTick >= g.tickRange[0] && g.currentTick <= g.tickRange[1] ){
            String dir = System.getProperty("user.dir");
            dir = dir.replace("src" + File.separator + "main" + File.separator + "java", "bin" + File.separator);
            g.writeGrid(dir + g.outputFilePattern + Integer.toString(g.currentTick) + ".txt", g.currentTick);
        }   
    }
    
    /**
     * Function to build the grid from the selected path via File Opener
     * @param path absolute path to the text file to build the grid
     */
    public void setGrid(String path){
        try {
            g.buildGrid(path);
        } catch (IOException e){
            JOptionPane.showMessageDialog(this, "IO Error while building the Grid!\nCheck the input file!", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e){
            JOptionPane.showMessageDialog(this, "IO Error while building the Grid!\nCheck the input file!", "Error", JOptionPane.ERROR_MESSAGE);
        }
        
        updateGrid();
    }
    /**
     * Function to initialize the grid panels, and grid contents
     * The initial grid is 5x7 and has only dead cells. It is 
     * configurable by the user via toolbar or input file
     */
    public void createGrid(){
        this.add(parentPanel);

        // set panel sizes
        mainPanel.setPreferredSize(new Dimension(100, 60));
        footer.setPreferredSize(new Dimension(100, 150));
        header.setPreferredSize(new Dimension(100, 100));
        leftPanel.setPreferredSize(new Dimension(175, 100));
        rightPanel.setPreferredSize(new Dimension(175, 100));

        //add Title above the grid
        title.setFont(new Font("Ariel Black", Font.BOLD, 32));
        header.add(title);

        // initialize a 5x5 grid with all dead cells
        // this will be updated when an input file is loaded
        // and every time the user simulated to the next generation
        mainPanel.setLayout(new GridLayout(5, 7, 10, 10));
        for(int i = 0;i < 5; i++){
            for(int j = 0;j < 7; j++){
                JButton b = new JButton();
                b.setBackground(Color.red);
                mainPanel.add(b);
            }
        }
        
        // add panels to the Parent panel
        parentPanel.add(mainPanel, BorderLayout.CENTER);
        parentPanel.add(footer, BorderLayout.SOUTH);
        parentPanel.add(leftPanel, BorderLayout.WEST);
        parentPanel.add(rightPanel, BorderLayout.EAST);
        parentPanel.add(header, BorderLayout.NORTH);
    }

    /**
     * Function to create the MenuBar and all its items
    */
    public void createMenuBar(){
        JMenuBar bar = new JMenuBar();
        
        // setting menu
        JMenu settingMenu = new JMenu("Settings");
        bar.add(settingMenu);
        // input file
        JMenuItem item1 = new JMenuItem("Select Input File");
        item1.addActionListener(new ActionListener()
        {
            public void actionPerformed(ActionEvent e1){
                JFileChooser fileChooser = new JFileChooser();
                FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files", "txt");
                fileChooser.setFileFilter(filter); fileChooser.setCurrentDirectory(new File(".." + File.separator + ".." + File.separator + ".." + File.separator + "bin"));
                int r = fileChooser.showOpenDialog(null);
                if(r == JFileChooser.APPROVE_OPTION){
                    String path = fileChooser.getSelectedFile().getAbsolutePath();
                    inputPath = path;
                    setGrid(path);
                    updateText();
                }
                
            }
        });
        settingMenu.add(item1);
        // output file  pattern
        JMenuItem item2 = new JMenuItem("Select Output Pattern");
        settingMenu.add(item2);
        item2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                String pattern = JOptionPane.showInputDialog(null, "Enter Output Filepattern (Default = \"out\")", "out");
                if(pattern != null){
                    g.setPattern(pattern);
                    updateText();
                }
            }
        });
        
        // select iteration count
        JMenuItem item3 = new JMenuItem("Set Iteration Count");
        item3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                int itr = 5;
                try {
                    itr = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Iteration Count (Default = 5)"));
                } catch (NumberFormatException e){
                    JOptionPane.showMessageDialog(null, "Please Enter an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                // set max tick only when the sim hasnt ran yet
                if(g.getTick() == 0){
                    g.setTotalTick(itr);
                    g.tickRange[1] = itr;
                    updateText();
                }
            }
        });
        settingMenu.add(item3);

        // set lower tick to save
        JMenuItem item5 = new JMenuItem("Save Lower Tick");
        item5.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                int lowTick = 0;
                try {
                    lowTick = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Lower Bound"));
                } catch (NumberFormatException e1){
                    JOptionPane.showMessageDialog(null, "Please Enter an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (lowTick < 0 || lowTick >= g.tickRange[1]){
                    JOptionPane.showMessageDialog(null, "Lower Bound must be between 0 and Upper Bound", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    g.tickRange[0] = lowTick;
                    updateText();
                }
                
            }
        });
        settingMenu.add(item5);

        // set upper tick to save
        JMenuItem item6 = new JMenuItem("Save Upper Tick");
        item6.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                int upperTick = g.getTotalTick();
                try {
                    upperTick = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Upper Bound"));
                } catch (NumberFormatException e2){
                    JOptionPane.showMessageDialog(null, "Please Enter an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (upperTick > g.getTotalTick() || upperTick <= g.tickRange[0]){
                    JOptionPane.showMessageDialog(null, "Upper Bound must be between Lower Bound and Total Tick Count", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    g.tickRange[1] = upperTick;
                    updateText();
                }
                
            } 
        });
        settingMenu.add(item6);

        // --- view menu ---
        JMenu viewMenu = new JMenu("View");
        bar.add(viewMenu);
        // Background color
        JMenuItem item4 = new JMenuItem("Select Background Color");
        item4.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0){
                Color c = JColorChooser.showDialog(null, "Please Select a Color", Color.WHITE);
                setColor(c);
            }

        });
        viewMenu.add(item4);

        // set Menu Bar
        bar.revalidate();
        this.setJMenuBar(bar);
    }
    /**
     * Function to create the toolbar, all the toolbar buttons and their implementations. 
     */
    void createToolBar(){
        // set image icons
        ImageIcon smallStep = new ImageIcon(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + "right.png");
        ImageIcon bigStep = new ImageIcon(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + "fullright.png");
        ImageIcon reset = new ImageIcon(".." + File.separator + ".." + File.separator + ".." + File.separator + "images" + File.separator + "reset.png");
        
        // set buttons for toolbar
        JButton b1 = new JButton(smallStep);
        b1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                if((g.getTick() < g.getTotalTick()) && g.getGrid() != null){
                    g.nextStep();
                    updateGrid();
                    updateText();
                }
            }
        });
        JButton b2 = new JButton(bigStep);
        b2.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e2){
                if((g.getTick() < g.getTotalTick()) && g.getGrid() != null){
                    while(g.getTick() < g.getTotalTick()){
                        g.nextStep();
                    }
                    updateGrid();
                    updateText();
                }
            }
        });
        JButton b3 = new JButton(reset);
        b3.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e3){
                if(inputPath != ""){
                    setGrid(inputPath);
                    updateText();
                } else {
                    //only reset if g is not null
                    if(g.g == null){
                        return;
                    }
                    for(int i = 0;i < g.g.length; i++){
                        for(int j = 0;j < g.g[i].length;j++){
                            g.g[i][j] = 0;
                        }
                    }
                    g.currentTick = 0;
                    updateGrid();
                    updateText();
                }
            }
        });
        JButton b4 = new JButton();
        b4.setText("Configure");
        
        b4.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent ev){
                if(g.getTick() == 0){
                    Configure c = new Configure(g, "Configure Grid", mainPanel, livingLabel);
                    c.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Please Reset Before Configuration!", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        //make toolbar
        this.bar.add(b3);
        this.bar.add(b1);
        this.bar.add(b2);
        this.bar.add(b4);
        this.bar.setFloatable(false);
        this.bar.setRollover(true);
        
        //add toolbar to panel
        footer.add(bar);



    }
    
    //static "main" method
    public static void generateGUI(){
        GUI frame = new GUI("JFrame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        
    }

}

/**
 * Helper class which handles everything related to the 
 * "Configure" function via Toolbar. You can set dimensions, 
 * set alive/dead cells and load it into the main grid.
 */
class Configure extends JFrame {
    
    // Various Contents use for the Frame
    // Useful as instance fields so all methods can 
    // access them easily 
    JPanel masterPanel;
    JLabel llabel;
    Grid grid;
    JPanel mainPanel = new JPanel(new BorderLayout());
    JPanel gridPanel = new JPanel();
    JPanel footerPanel = new JPanel();
    JPanel headerPanel = new JPanel();
    JButton rowButton = new JButton();
    JButton colButton = new JButton();
    JButton loadButton = new JButton();
    int rows = 0;
    int cols = 0;
    JButton[][] gridButtons;
    boolean readyToLoad = false;


    /**
     * constructor to build the "Configure" Frame 
     * @param g The grid object from the original GUI class
     * @param name name of the Frame
     * @param mp the mainPanel object from the original GUI class.
     * @param ll the living cells label from the original GUI object
     */
    Configure(Grid g, String name, JPanel mp, JLabel ll){
        
        super(name);
        setResizable(false);
        setSize(600, 600);
        setTitle("Configure the Grid");
        this.grid = g;
        this.masterPanel = mp;
        this.llabel = ll;
        setPanels();
    }

    /**
     * Method to initialize the various JPanels and their properties
     */
    void setPanels(){
        
        this.add(mainPanel);
        
        headerPanel.setPreferredSize(new Dimension(600, 100));
        gridPanel.setPreferredSize(new Dimension(600, 400));
        footerPanel.setPreferredSize(new Dimension(600, 100));

        setHeader();
        setFooter();

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(footerPanel, BorderLayout.SOUTH);

    }

    /**
     * Method to set grid contents and implement the button color changing functionality
     */
    void setGrid(){
        gridPanel.removeAll();
        gridPanel.setLayout(new GridLayout(rows, cols));
        gridButtons = new JButton[rows][cols];

        for(int i = 0;i < rows; i++){
            for(int j = 0; j < cols; j++){
                JButton b = new JButton();
                b.setBackground(Color.RED);
                b.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent a){
                        //get pressed button
                        JButton button = (JButton) a.getSource();
                        Color c = button.getBackground();
                        // set Color
                        if(c.equals(Color.RED)){
                            button.setBackground(Color.GREEN);
                        } else if (c.equals(Color.GREEN)) {
                            button.setBackground(Color.RED);
                        }
                        button.revalidate();
                    }
                });
                gridButtons[i][j] = b;
                gridPanel.add(b);
            }
        }

        gridPanel.revalidate();
               
    }
    /**
     * Method to set the header panel, which is effectively just a title
     */
    void setHeader(){
        JLabel title = new JLabel("Configure the Grid");
        JLabel desc = new JLabel("Set Dimensions and Click on the Cells to Mark them Alive/Dead");

        title.setFont(new Font("Ariel Black", Font.BOLD, 32));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(title);
        
        desc.setFont(new Font("Ariel Black", Font.BOLD, 18));
        desc.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(Box.createRigidArea(new Dimension(600, 10)));
        headerPanel.add(desc);
    }

    
    /**
     * Method to set the footer panel, which holds the buttons related to user input
     * and loading data
     */
    void setFooter(){
        // set layout
        footerPanel.setLayout(new BoxLayout(footerPanel, BoxLayout.PAGE_AXIS));
        // configure buttons
        rowButton.setText("Enter Row Count");
        colButton.setText("Enter Column Count");
        loadButton.setText("Load Grid Data");
        rowButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        colButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        loadButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        // action listeners for the buttons
        rowButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    rows = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Rows"));
                } catch (NumberFormatException e1) {
                    JOptionPane.showMessageDialog(null, "Please Enter an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(rows < 0){
                    JOptionPane.showMessageDialog(null, "Row count must be a positive integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    rows = 0;
                }
                if(rows > 0 && cols > 0){
                    setGrid();
                }
            }
        });

        colButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try {
                    cols = Integer.parseInt(JOptionPane.showInputDialog(null, "Enter Columns"));
                } catch (NumberFormatException e2) {
                    JOptionPane.showMessageDialog(null, "Please Enter an Integer!", "Error", JOptionPane.ERROR_MESSAGE);
                }
                if(cols < 0){
                    JOptionPane.showMessageDialog(null, "Column count must be a positive integer!", "Error", JOptionPane.ERROR_MESSAGE);
                    cols = 0;
                }
                if(rows > 0 && cols > 0){
                    setGrid();
                }
            }
        });

        loadButton.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent w){
                if(gridButtons == null){
                    return;
                }
                grid.r = rows;
                grid.c = cols;
                int[][] arr = new int[rows][cols];
                for(int i = 0;i < gridButtons.length; i++){
                    for(int j = 0;j < gridButtons[i].length; j++){
                        if(gridButtons[i][j].getBackground().equals(Color.GREEN)){
                            arr[i][j] = 1;
                        } else {
                            arr[i][j] = 0; 
                        }
                    }
                }
                grid.g = arr;
                // update main panel
                masterPanel.removeAll();
                masterPanel.setBackground(masterPanel.getBackground());
                masterPanel.repaint();
                masterPanel.setLayout(new GridLayout(grid.getRow(), grid.getCol(), 10, 10));

                int[][] data = grid.getGrid();
                for(int i = 0; i < data.length; i++){
                    for(int j = 0;j < data[i].length; j++){
                        JButton b = new JButton();
                        if(data[i][j] == 1){
                            b.setBackground(Color.GREEN);
                        } else {
                            b.setBackground(Color.RED);
                        }
                        masterPanel.add(b);
                    }
                }
                llabel.setText("# of Living Cells: " + grid.getLivingCells());
                masterPanel.revalidate();
                Configure.this.dispose();
            }
        });
        
        // add buttons
        footerPanel.add(Box.createRigidArea(new Dimension(10,5)));
        footerPanel.add(rowButton);
        footerPanel.add(Box.createRigidArea(new Dimension(10,7)));
        footerPanel.add(colButton);
        footerPanel.add(Box.createRigidArea(new Dimension(10,7)));
        footerPanel.add(loadButton);
    }

}