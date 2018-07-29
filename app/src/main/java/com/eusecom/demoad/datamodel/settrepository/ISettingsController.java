package com.eusecom.demoad.datamodel.settrepository;


public interface ISettingsController {


    public void setGpsInterval(long interval);

    public long getGpsInterval();

    public void setLineSize(float size);

    public float getLineSize();

    public void setLineColor(int color);

    public int getLineColor();



}
