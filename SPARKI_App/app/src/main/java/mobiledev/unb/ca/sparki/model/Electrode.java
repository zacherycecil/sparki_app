package mobiledev.unb.ca.sparki.model;

public class Electrode {

    public String[] tValues = new String[8];
    public String macro;
    public String trigger;

    public Electrode(String[] tValues)
    {
        this.tValues = tValues;
    }
    public Electrode(String macro)
    {
        this.macro = macro;
    }
    public Electrode(String macro, String trigger)
    {
        this.macro = macro;
        this.trigger = trigger;
    }
    public Electrode()
    {
        tValues = null;
        macro = null;
        trigger = null;
    }

    public String[] getTherapeuticValues()
    {
        return tValues;
    }

    public String getMacro()
    {
        return macro;
    }

    public String getTrigger()
    {
        return trigger;
    }
}