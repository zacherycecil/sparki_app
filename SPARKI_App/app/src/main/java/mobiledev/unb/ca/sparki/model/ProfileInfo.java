package mobiledev.unb.ca.sparki.model;

public class ProfileInfo {

    private String profileName;
    private Electrode[] electrodeList;
    private String mode;

    public ProfileInfo(String profileName, Electrode[] electrodeList, String mode) {
        this.electrodeList =  electrodeList;
        this.profileName = profileName;
        this.mode = mode;
    }

    public String getProfileName() {
        return profileName;
    }
    public String getProfileMode() {
        return mode;
    }

    public Electrode[] getElectrodeList() {
        return electrodeList;
    }

    public void setProfileName(String profileName) {
        this.profileName = profileName;
    }

    public void setElectrodeList(Electrode[] electrodeList) {
        this.electrodeList =  electrodeList;
    }

    public String getElectrodeString(int index)
    {
        String electrodeData;
        if (mode.equals("Therapeutic")) {
            String[] therapeuticVals = electrodeList[index].getTherapeuticValues();
            electrodeData = "Frequency: " + therapeuticVals[0] + "Hz"
                    + "\nRamping time (Pulse Width): " + therapeuticVals[1] + "sec"
                    + "\nPulse Width: " + therapeuticVals[2] + "ms"
                    + "\nStimulation Time: " + therapeuticVals[3] + "min"
                    + "\nRamping time (Frequency): " + therapeuticVals[4] + "sec"
                    + "\nRamping time (amplitude): " + therapeuticVals[5] + "sec"
                    + "\nOn time: " + therapeuticVals[6] + "sec"
                    + "\nOff time: " + therapeuticVals[7] + "sec";
        }
        else if (mode.equals("FES")) {
            electrodeData = "Macro: " + electrodeList[index].getMacro()
                    + "\nTrigger: " + electrodeList[index].getTrigger();
        }
        else if (mode.equals("FreeRun"))
            electrodeData = "Macro: " + electrodeList[index].getMacro();
        else
            electrodeData = null;
        return electrodeData;
    }
}
