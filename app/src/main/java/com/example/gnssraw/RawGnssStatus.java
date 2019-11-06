package com.example.gnssraw;

import android.location.GnssStatus;
import android.widget.TextView;


//non importante per adesso...
public class RawGnssStatus  extends GnssStatus.Callback
{
    TextView myText;

    public RawGnssStatus(TextView myText) {
        this.myText = myText;
    }

    @Override
    public void onSatelliteStatusChanged(GnssStatus status) {
        System.out.println("onGnssStatusChanged: " + gnssStatusToString(status));
        myText.append("onGnssStatusChanged: " + gnssStatusToString(status));
    }

    private String gnssStatusToString(GnssStatus gnssStatus) {

        StringBuilder builder = new StringBuilder("SATELLITE_STATUS | [Satellites:\n");
        for (int i = 0; i < gnssStatus.getSatelliteCount(); i++) {
            builder
                    .append("Constellation = ")
                    .append(getConstellationName(gnssStatus.getConstellationType(i)))
                    .append(", ");
            builder.append("Svid = ").append(gnssStatus.getSvid(i)).append(", ");
            builder.append("Cn0DbHz = ").append(gnssStatus.getCn0DbHz(i)).append(", ");
            builder.append("Elevation = ").append(gnssStatus.getElevationDegrees(i)).append(", ");
            builder.append("Azimuth = ").append(gnssStatus.getAzimuthDegrees(i)).append(", ");
            builder.append("hasEphemeris = ").append(gnssStatus.hasEphemerisData(i)).append(", ");
            builder.append("hasAlmanac = ").append(gnssStatus.hasAlmanacData(i)).append(", ");
            builder.append("usedInFix = ").append(gnssStatus.usedInFix(i)).append("\n");
        }
        builder.append("]");
        return builder.toString();
    }

    private String getConstellationName(int id) {
        switch (id) {
            case 1:
                return "GPS";
            case 2:
                return "SBAS";
            case 3:
                return "GLONASS";
            case 4:
                return "QZSS";
            case 5:
                return "BEIDOU";
            case 6:
                return "GALILEO";
            default:
                return "UNKNOWN";
        }
    }
}
