package hornet.lidar;

import hornet.VirtualHornet;

import java.util.ArrayList;

/**
 * Created by Angela on 19/08/2015.
 */
public class LidarManager {
    ArrayList<Point> _sweep = new ArrayList<Point>();
    ArrayList<Point> _pendingSweep = new ArrayList<Point>();
    Float _lastRotation = 380f;
    ArrayList<SweepPoint> _sweepPoints = new ArrayList<>();
    VirtualHornet _virtualHornet;

    public  LidarManager(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
    }

    public void processData(Float angle, Float distance, Float rotation)
    {
        if(rotation < _lastRotation)
            {
                _sweep = new ArrayList<Point>();

                for (int i = 0; i<_pendingSweep.size(); i++)
                {
                    _sweep.add(i,_pendingSweep.get(i));
                }

                _pendingSweep = new ArrayList<Point>();
                _lastRotation = 380f;

            }
            else {
                _pendingSweep.add(new Point(angle, distance, rotation));
                _lastRotation = rotation;
            }

    }

    public void addPoint(float angle, float distance)
    {
        _sweepPoints.add(new SweepPoint(angle,distance));
    }

    public void addEOSweep(float pitch, float roll, float yaw)
    {
        ArrayList<XYPoint> XYPoints = new ArrayList<>();

        for (int i =0; i<_sweepPoints.size(); i++) {

            XYPoints.add(new XYPoint(_sweepPoints.get(i),6000, yaw));
        }

        _virtualHornet.getSweepPoints(XYPoints);
    }

}


