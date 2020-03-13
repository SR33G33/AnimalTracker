import processing.core.PApplet;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class MouseTracker implements PixelFilter {
    private static final int MAX_FRAMES = 4501;  // TODO:  Change this value to match video
    Point p = new Point(320, 240);
    Cluster cluster = new Cluster(p);
    DataSet dataset;
    int frameCount = 0;
    Point mouseLoc = new Point(0, 0);

    public MouseTracker() {
        dataset = new DataSet(25, 1/(5.316));  // TODO:  feel free to change the constructor
    }

    ObjectTracker ot = new ObjectTracker();
    ConvolutionFilter cf = new ConvolutionFilter();




    @Override
    public DImage processImage(DImage img) {

        frameCount++;


        if (frameCount < MAX_FRAMES) {

            short[][] pixels = img.getBWPixelGrid();

            for (int r = 0; r < pixels.length; r++) {
                for (int c = 0; c < pixels[0].length; c++) {
                    if((r - 235)*(r - 235) + (c - 309)*(c - 309) > 208*208){
                        pixels[r][c] = 255;
                    }
                    if(pixels[r][c] > 100){
                        pixels[r][c] = 0;
                    }else{
                        pixels[r][c] = 255;
                    }
                }
            }
            locateMouse(pixels);

            img.setPixels(pixels);
            return img;

//        1).  Filter the image to isolate mouse
//        2).  Extract information about the mouse
//        3).  Load information into dataset.


        } else if (frameCount == MAX_FRAMES) {     // If last frame, output CSV data
            displayInfo(dataset);           // display info if you wish
            outputCSVData("SIVA_SREEGANESH.csv", dataset);         // output data to csv file, if you wis
        }

        return img;
    }
    private void displayInfo(DataSet dataset) {

    }

    public Point getMouseLoc() {
        return mouseLoc;
    }

    private void outputCSVData(String filepath, DataSet dataset) {

        ArrayList<Point> mouseLocations = dataset.getMouseLocations();

        try(FileWriter f = new FileWriter(filepath);
            BufferedWriter b = new BufferedWriter(f);
            PrintWriter p = new PrintWriter(b);){

            for (int loc = 0; loc < mouseLocations.size(); loc++) {
               Point point = mouseLocations.get(loc);

                p.println(point.getRow() + "," + point.getCol());

            }



        }catch(IOException error){
            System.err.println("There was a problem writing to the file: " + filepath);
            error.printStackTrace();
        }


    }


    public void locateMouse(short[][] pixels){
        int rowSum = 0;
        int colSum = 0;
        int totalWhitePoints = 0;

            for (int row = 0; row < pixels.length; row++) {
                for (int col = 0; col < pixels[0].length; col++) {
                    if (pixels[row][col] == 255) {
                        totalWhitePoints++;
                        rowSum += row;
                        colSum += col;
                    }
                }
            }

            if (totalWhitePoints > 0) {
                mouseLoc.setRow((int) (rowSum / totalWhitePoints));
                mouseLoc.setCol((int) (colSum / totalWhitePoints));


            }
            dataset.addMouseLocation(mouseLoc);

        }



    @Override
    public void drawOverlay(PApplet window, DImage original, DImage filtered) {
           window.fill(255, 0, 0);
           window.ellipse(mouseLoc.getCol(), mouseLoc.getRow(), 5, 5);

    }

}

