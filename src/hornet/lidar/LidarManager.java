package hornet.lidar;

import hornet.VirtualHornet;
import hornet.coms.DataPacket;

import java.util.ArrayList;

/**
 * Created by Angela on 19/08/2015.
 */
public class LidarManager {
    /*ArrayList<Point> _sweep = new ArrayList<Point>();
    ArrayList<Point> _pendingSweep = new ArrayList<Point>();
    Float _lastRotation = 380f;
    ArrayList<SweepPoint> _sweepPoints = new ArrayList<>();*/

    ArrayList<SweepPoint>[] _sweepPoints = new ArrayList[2];
    int _sweep =0;
    VirtualHornet _virtualHornet;
    float _lastAngle =0;
    int sweepCount =0;

    public  LidarManager(VirtualHornet theVirtualHornet)
    {
        _virtualHornet = theVirtualHornet;
        _sweepPoints[0] = new ArrayList<>();
        _sweepPoints[1] = new ArrayList<>();
    }

    public void newAnchor(DataPacket data,int i)
    {
        float angle = data.getShortPayload()[0]/90;
        float distance = data.getShortPayload()[1];


        XYPoint toAdd = new XYPoint( new SweepPoint(angle,distance), 6000, 0);

        _virtualHornet.newAnchor(toAdd,i);
    }

    public void newPoint(DataPacket data)
    {
        float angle = data.getShortPayload()[0]/90;
        float distance = data.getShortPayload()[1];

        if(_lastAngle >=300 && angle < _lastAngle)
        {
            if(sweepCount <5)
            {
                sweepCount++;
                _lastAngle = angle;
                _sweepPoints[_sweep].add(new SweepPoint(angle,distance));
            }else {
                // eos

                XYPoint[] toSend = new XYPoint[_sweepPoints[_sweep].size()];

                for (int i = 0; i < _sweepPoints[_sweep].size(); i++) {
                    toSend[i] = new XYPoint(_sweepPoints[_sweep].get(i), 6000, 0);
                }

                _virtualHornet.newSweepData(toSend);

                _lastAngle = angle;
                if (_sweep == 0) {
                    _sweep = 1;
                } else {
                    _sweep = 0;
                }

                _sweepPoints[_sweep] = new ArrayList<>();
                sweepCount =0;
            }
        }
        else
        {
            _lastAngle = angle;
            _sweepPoints[_sweep].add(new SweepPoint(angle,distance));
        }
    }

   /* public void processData(Float angle, Float distance, Float rotation)
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

       // _virtualHornet.getSweepPoints(XYPoints);
    }*/

}


