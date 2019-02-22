package com.temal.chakib.fr.gpstrackerandroid;

import android.location.Location;

public class LocationC {
    private LocationC.OnLocationListner listner = null;

    public static LocationC getInstance(){
        return new LocationC();
    }

    public void setListner(OnLocationListner listner){
        this.listner = listner;
    }

    public LocationC.OnLocationListner getListner(){
        return this.listner;
    }

    public interface OnLocationListner{
        void onGetLocation(Location location);
    }
}
