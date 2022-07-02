package test.java;

import java.io.*;
import main.java.Grid;



public class GridTest {
    
    GridTest(){

    }

    public void ConstructorTest(){
        Grid g = new Grid();
        System.out.println("ConstructorTest Passed");
    }

    public void ReadGridTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("ReadGridTest Failed");
        }   

        if(g.r == 5 && g.c == 7 && g.g[1][3] == 1){
            System.out.println("ReadGridTest Passed");
        }
        
    }

    public void MaxTickTest(){
        Grid g = new Grid();
        g.setTotalTick(13);
        if(g.totalTick == 13){
            System.out.println("MaxTickTest Passed");
        } else {
            System.err.println("MaxTickTest Failed");
        }
    }
    
    public void OutPatternTest() {
        Grid g = new Grid();
        g.setPattern("output");
        if(g.outputFilePattern.equals("output")){
            System.out.println("OutPatternTest Passed");
        } else {
            System.err.println("OutPatternTest Failed");
        }
    }

    public void LivingCellsTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("LivingCellsTest Failed");
        } 
        int r = g.getLivingCells();
        if(r == 16){
            System.out.println("LivingCellsTest Passed");
        } else {
            System.err.println("LivingCellsTest Failed");
        }

    }

    public void NextStepTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("NextStepTest Failed");
        }

        g.nextStep();
        if(g.g[1][3] == 1 && g.g[2][3] == 1){
            System.out.println("NextStepTest Passed");
        } else {
            System.err.println("NextStepTest Failed");
        }
    }

    public void IncrementTickTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("IncrementTickTest Failed");
        }

        g.nextStep();
        if(g.getTick() == 1){
            System.out.println("IncrementTickTest Passed");
        } else {
            System.err.println("IncrementTickTest Failed");
        }
    }

    public void LivingCellsAfterStepTest() {
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("LivingCellsAfterStepTest Failed");
        }

        g.nextStep();
        int r = g.getLivingCells();
        if(r == 11){
            System.out.println("LivingCellsAfterStepTest Passed");
        } else {
            System.err.println("LivingCellsAfterStepTest Failed");
        }
    }

    public void updateCellSurviveTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("updateCellSurviveTest Failed");
        }

        int r = g.updateCell(1, 3, g.g);
        if(r == 1){
            System.out.println("updateCellSurviveTest Passed");
        } else {
            System.err.println("updateCellSurviveTest Failed");
        }
    }

    public void updateCellDiedTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("updateCellDiedTest Failed");
        }

        int r = g.updateCell(2, 5, g.g);
        if(r == 0){
            System.out.println("updateCellDiedTest Passed");
        } else {
            System.err.println("updateCellDiedTest Failed");
        }
    }

    public void updateCellBornTest(){
        Grid g = new Grid();
        try{
            g.buildGrid(".." + File.separator + "resources" + File.separator + "input.txt");
        } catch (IOException ex){
            System.err.println("updateCellBornTest Failed");
        }

        int r = g.updateCell(0, 0, g.g);
        if(r == 1){
            System.out.println("updateCellBornTest Passed");
        } else {
            System.err.println("updateCellBornTest Failed");
        }
    }

    public static void main(String[] args){
        GridTest g = new GridTest();
        System.out.println("\n-------- RUNNING TESTS --------\n");
        g.ConstructorTest();
        g.ReadGridTest();
        g.MaxTickTest();
        g.OutPatternTest();
        g.LivingCellsTest();
        g.NextStepTest();
        g.IncrementTickTest();
        g.LivingCellsAfterStepTest();
        g.updateCellSurviveTest();
        g.updateCellDiedTest();
        g.updateCellBornTest();
        System.out.println("\n-------- FINISHED TESTS --------\n");
    }

}
