package main.java;
import java.io.*;

/**
 * Grid Class
 * The Grid class handles the operation used by Main to simulate Conway's Game of Life.
 * The underlying representation is a 2D array of integers which hold 1 or 0 (alive or dead respectively)
 * 
 * @author Aaron Zachariah
 * 
 */
public class Grid {

    /**
     * 2D array of integers (1 or 0) which represent the status of a cell
     */
    public int[][] g;
    /**
     * row variable for the grid dimentions
     */
    public int r;
    /**
     * Column variable for the grid dimentions.
     */
    public int c;
    /**
     * Tick counter to represent the generation
     */
    public int currentTick = 0;
    /**
     * Variable to hold the total amout of iterations for the simulation
     */
    public int totalTick = 5;
    
    /**
     * 2 element array to hold the tick range that will be saved to a file
     */
    int[] tickRange = {0, totalTick};
    
    /**
     * String to hold output filepattern. Defualt is "out"
     */
    public String outputFilePattern = "out";
    /**
     * getter for row count
     * @return number of rows
     */
    public int getRow(){
        return this.r;
    }

    /**
     * getter for column count
     * @return number of columns
     */
    public int getCol(){
        return this.c;
    }

    /**
     * Getter for Tick value
     * @return the current generation count
     */
    public int getTick(){
        return this.currentTick;
    }
    /**
     * Getter for the total iteration count
     * @return total iteration count
     */
    public int getTotalTick(){
        return this.totalTick;
    }
    /**
     * Getter for the output file pattern
     * @return string value of the pattern name
     */
    public String getPattern(){
        return this.outputFilePattern;
    }
    /**
     * getter for the grid
     * @return 2D array of integers representing the grid
     */
    public int[][] getGrid(){
        return this.g;
    }
    /**
     * Function to count the number of living cells in the grid
     * @return the number of living cells
     */
    public int getLivingCells(){
        int ctr = 0;
        for(int i = 0;i < this.g.length;i++){
            for(int j = 0;j < this.g[i].length;j++){
                if(this.g[i][j] == 1){
                    ctr ++;
                }
            }
        }
        return ctr;
    }

    /**
     * Setter to set the output file pattern (configured via the GUI)
     * @param pattern String value of the output file pattern name
     */
    public void setPattern(String pattern){
        this.outputFilePattern = pattern;
    }
    /**
     * Setter for the total iteration count
     * @param tickcount int value of the total generations to simulate
     */
    public void setTotalTick(int tickcount){
        this.totalTick = tickcount;
    }
    
    /**
     * 
     * @param path Full path of the input filename to read from and parse.
     * @throws IOException if there is an error in reading from the file or closing the file.
     * @throws NumberFormatException if a number cannot be parseed from the file.
     * @throws FileNotFoundException if there is a problem in the specified path. Ex: The file doesn't exist
     * @throws RuntimeException if there are other misc. problems with reading the file. A useful message will be printed to the terminal.
     * 
     */
    public void buildGrid(String path) throws IOException {
        this.currentTick = 0;
        BufferedReader inFile = null;
        try {
            // open file reader
            inFile = new BufferedReader(new FileReader(path));
            
            //parse file
            String line;
            int line_ctr = 0;
            int rows = 0;
            int cols = 0;
            while( (line = inFile.readLine()) != null ) {
                // split a line by spaces and commas
                String[] nums = line.split("[ ,]+");
                // parsing first line...
                if(line_ctr == 0){
                    if(nums.length != 2) {
                        throw new RuntimeException("Incorrect number of dimensions specified for the grid! Check the User Manual for more information!");
                    }
                    rows = Integer.parseInt(nums[0]);
                    cols = Integer.parseInt(nums[1]);
                    if(rows <= 0 || cols <= 0){
                        throw new RuntimeException("Row and Column size must be greater than 0! Check the User Manual for more information!");
                    }
                    this.r = rows;
                    this.c = cols;
                    this.g = new int[rows][cols];
                } 
                // parsing all other lines...
                else {
                    if(nums.length != cols){
                        throw new RuntimeException("Number of columns do not match! Check the User Manual for more information!");
                    }
                    if(line_ctr > rows){
                        throw new RuntimeException("Too many rows in the input file! Check the User Manual for more information!");
                    }
                    for(int i = 0; i < cols; i++){
                        this.g[line_ctr-1][i] = Integer.parseInt(nums[i]);
                        
                    }
                    
                }
                
                line_ctr++;
            }

            if(line_ctr <= rows){
                throw new RuntimeException("Not enough rows in the input file! Check the User Manual for more information!");
            }
        } finally {
            if(inFile != null){
                inFile.close();
            }
        }

    }

    /**
     * Function to generate the state of the grid after one unit of time.
     * 
     */
    public void nextStep(){
        
        int[][] new_grid = new int[this.r][this.c];

        for(int i =0; i < g.length; i++){
            for(int j = 0; j < g[0].length; j++){
                new_grid[i][j] = updateCell(i, j, this.g);
            }
        }

        this.g = new_grid;
        this.currentTick = this.currentTick + 1;
    }

    /**
     * Function which returns the updated result of a given cell based off the state of the old grid
     * @param rw row index to update
     * @param cl column index to update
     * @param old_grid the old grid to determine the new status of index rw, cl int the new grid
     * @return 0 or 1 depending on if the cell should be alive or dead
     */

    public int updateCell(int rw, int cl, int[][] old_grid){
        // count the living neighbors 
        int living_neighbors = 0;
        for(int i = rw-1; i<= rw+1; i++){
            for(int j = cl-1; j <= cl+1; j++){
                if(i == rw && j == cl){
                    continue;
                } else {
                    int x = i;
                    int y = j;
                    // check boundaries
                    if(x < 0){
                        x = this.r - 1;
                    } else if (x >= this.r) {
                        x = 0;
                    } 
                    if (y < 0) { 
                        y = this.c - 1;
                    } else if (y >= this.c) {
                        y = 0;
                    }
                    
                    if(old_grid[x][y] == 1){
                        living_neighbors++;
                    }
                }
            }
        }
        // Any live cell with fewer than two live neighbors dies, as if caused by underpopulation.
        if(old_grid[rw][cl] == 1 && living_neighbors < 2 ) {
            return 0;
        } 
        // Any live cell with two or three live neighbors lives on to the next generation.
        else if (old_grid[rw][cl] == 1 && living_neighbors < 4) {
            return 1;
        } 
        // Any live cell with more than three live neighbors dies, as if by overpopulation.
        else if (old_grid[rw][cl] == 1 && living_neighbors >= 4) {
            return 0;
        } 
        // Any dead cell with exactly three live neighbors becomes a live cell, as if by reproduction.
        else if (old_grid[rw][cl] == 0 && living_neighbors == 3) {
            return 1;
        } 
        // Any dead cell w/ > or < 3 live neighbors will remain dead.
        else {
            return 0;
        }
        
    }
    
    /**
     * Function to write the grid to the output file specified
     * 
     * @param path the full path of the output file to write to.
     * @param itr the iteration of the simulation (how much time has passed)
     * 
     */
    public void writeGrid(String path, int itr){

        FileWriter fw = null;
        try {
            // create file
            File file = new File(path);
            file.createNewFile();
            // Create writer object
            fw = new FileWriter(file);
            // write to file 
            fw.write("Iteration: " + Integer.toString(itr) + System.getProperty("line.separator"));
            for(int i = 0; i < this.g.length; i++){
                for(int j = 0; j < this.g[0].length - 1; j++){
                    fw.write(this.g[i][j] + ", ");
                }
                fw.write(this.g[i][this.c-1] + System.getProperty("line.separator"));
            }

            fw.close();

        } catch (IOException e) {
            System.err.println("Error In Output to File!");
            e.printStackTrace();
        }

    }
    
    /**
     * Function to print the grid to the terminal.
     * Will print in a matrix like shape, with dimensions: [this.r x this.c]
     * 
     */
    public void printGrid(){
        for(int i = 0; i < g.length; i++){
            for(int j = 0;j < g[0].length; j++){
                System.out.print(g[i][j] + " ");
            }
            System.out.print("\n");
        }

        

    }




}
