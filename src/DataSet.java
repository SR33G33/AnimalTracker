import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class DataSet {
    private static int FPS = 0;
    private static double cmPerPixel = 0;
    private ArrayList<Point> mouseLocations;
    private ArrayList<Region> regions;


    public DataSet(int FPS, double cmPerPixel) {
        this.FPS = FPS;
        this.cmPerPixel = cmPerPixel;
        mouseLocations = new ArrayList<Point>();
        regions = new ArrayList<Region>();
    }

    public DataSet() {
        mouseLocations = new ArrayList<Point>();

    }

    public ArrayList<Point> getMouseLocations() {
        return mouseLocations;
    }

    public void setMouseLocations(ArrayList<Point> mouseLocations) {
        this.mouseLocations = mouseLocations;
    }

    public static void setFPS(int FPS) {
        DataSet.FPS = FPS;
    }

    public void setCmPerPixel(int cmPerPixel) {
        DataSet.cmPerPixel = cmPerPixel;
    }

    public void addMouseLocation(Point mouseLoc) {
        mouseLocations.add(new Point(mouseLoc.getRow(), mouseLoc.getCol()));
        System.out.println("Frame #: " + mouseLocations.size() + " " + "Position: " + mouseLoc.getCol() + ", " + mouseLoc.getRow());
    }

    public Point getPositionAt(double time) {
        int frames = (int) (FPS * time);

        return mouseLocations.get(frames);

    }

    public double getCurrentTime() {
        return (double) (mouseLocations.size()) / FPS;
    }

    public double getSpeedAt(double time) {
        int speed = 0;

        int frames = (int) (FPS * time);
        if (frames == 0 || frames > mouseLocations.size() - 1) return -1;
        Point previousLoc = mouseLocations.get(frames - 1);
        Point currentLoc = mouseLocations.get(frames);

        double pixelDistance = currentLoc.getDistanceTo(previousLoc);
        double cmDist = pixelDistance * cmPerPixel;

        return cmDist / (1 / FPS);
    }

    public double getSpeedAt(int frames) {
        int speed = 0;

        if (frames == 0 || frames > mouseLocations.size() - 1) return -1;
        Point previousLoc = mouseLocations.get(frames - 1);
        Point currentLoc = mouseLocations.get(frames);

        double pixelDistance = currentLoc.getDistanceTo(previousLoc);
        double cmDist = pixelDistance * cmPerPixel;

        return cmDist / (1 / FPS);
    }

    public double getAverageSpeed() {
        int counter = 0;
        double speedSum = 0;


        for (int loc = 0; loc < mouseLocations.size(); loc++) {
            counter++;
            speedSum += getSpeedAt(loc);
        }


        return speedSum / counter;


    }


    public double getAverageSpeed(double startTime, double endTime) {
        double dist = getTotalDistance(startTime, endTime);

        return dist/(endTime-startTime);

    }


    public void addROI(Region r) {
        regions.add(r);
    }

    public double getAmountOfTimeInROI() {

        double counter = 0;

        for (int loc = 0; loc < mouseLocations.size(); loc++) {
            for (int region = 0; region < regions.size(); region++) {
                Region r = regions.get(region);

                if (r.contains(mouseLocations.get(loc))) {
                    counter++;
                }


            }
        }


        return counter / FPS;
    }

    public double getPercentageOfTimeInROI() {

        return getAmountOfTimeInROI() / (mouseLocations.size() / FPS);

    }

    public double getTotalDistance() {
        double totalDistSoFar = 0;

        for (int loc = 0; loc < mouseLocations.size() - 1; loc++) {
            totalDistSoFar += mouseLocations.get(loc).getDistanceTo(mouseLocations.get(loc + 1));
        }

        return totalDistSoFar * cmPerPixel;
    }

    public double getTotalDistance(double startTime, double endTime) {
        if (startTime < 0 || endTime > mouseLocations.size() - 1 || endTime < startTime) return -1;

        int startTimeS = (int)(startTime*FPS);
        int endTimeS = (int)(endTime*FPS);


        double totalDistSoFar = 0;


        for (int loc = startTimeS; loc < endTimeS-1; loc++) {
            totalDistSoFar += mouseLocations.get(loc).getDistanceTo(mouseLocations.get(loc + 1));
        }

        return totalDistSoFar * cmPerPixel;
    }


    public void readFileAsPoints(String filepath) {
        try (Scanner scanner = new Scanner(new File(filepath))) {

            while (scanner.hasNext()) {
                String line = scanner.nextLine();
                String[] loc = line.split(",");
                mouseLocations.add(new Point(Integer.parseInt(loc[0]), Integer.parseInt(loc[1])));
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
