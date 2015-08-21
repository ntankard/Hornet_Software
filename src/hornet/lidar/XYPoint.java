package hornet.lidar;

/**
 * Created by Angela on 21/08/2015.
 */
public class XYPoint {

    private double _xP;  // Point x percentage
    private double _yP;  // Point y percentage

    public XYPoint(SweepPoint p, float max, float offset) {

        double x = p.get_distance() * Math.sin((double) p.get_angle());
        double y = p.get_distance() * Math.cos((double) p.get_angle());

        _xP = (x/max) * 100;
        _yP = (y/max) * 100;
    }
}
