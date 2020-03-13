
public class CircleROI implements Region {

   Point center;
   int radius;

    public CircleROI(int x, int y, int radius) {

       center = new Point(x, y);
       this.radius = radius;

    }

    public boolean contains(Point p) {

        if(p.getDistanceTo(center)<=radius) return true;
        return false;

    }


    public Point getPoint() {
        return center;
    }


    public int getRadius() {
        return radius;
    }



}
