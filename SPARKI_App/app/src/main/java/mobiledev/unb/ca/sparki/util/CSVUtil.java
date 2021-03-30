package mobiledev.unb.ca.sparki.util;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import mobiledev.unb.ca.sparki.model.Electrode;
import mobiledev.unb.ca.sparki.model.ProfileInfo;

public class CSVUtil {

    public static ProfileInfo[] getProfileData(Context context) {
        try {
            String[] fileNames = context.getAssets().list("Profiles");
            ProfileInfo[] profileInfos = new ProfileInfo[fileNames.length];
            String profileName;
            Electrode[] electrodeList;

            for (int i = 0; i < fileNames.length; i++) {
                // GET FILE
                InputStream inputStream = context.getAssets().open("Profiles/" + fileNames[i]);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

                // SET NAME
                electrodeList = new Electrode[4];
                profileName = fileNames[i];

                // READ CONTENTS
                String rowData;
                int j = 0;

                String profileMode = reader.readLine().split(",")[0];
                switch (profileMode) {
                    case "Therapeutic":
                        while ((rowData = reader.readLine()) != null) {
                            String[] rowSplit = rowData.split(",");
                            Electrode electrode = new Electrode(rowSplit);
                            electrodeList[j] = electrode;
                            j++;
                        }
                        break;
                    case "FES": {
                        String h = reader.readLine();
                        String[] macros = h.split(",");
                        String[] triggers = reader.readLine().split(",");
                        for (j = 0; j < 4; j++)
                        {
                            Electrode electrode;
                            if (j < macros.length)
                            {
                                electrode = new Electrode(macros[j], triggers[j]);
                            }
                            else
                                electrode = new Electrode();
                            electrodeList[j] = electrode;
                        }
                        break;
                    }
                    case "FreeRun": {
                        String[] macros = reader.readLine().split(",");
                        for (j = 0; j < macros.length; j++) {
                            Electrode electrode;
                            if (j < macros.length) {
                                electrode = new Electrode(macros[j]);
                            }
                            else
                                electrode = new Electrode();
                            electrodeList[j] = electrode;
                        }
                        break;
                    }
                    default:
                        Log.v("h", "smallPROBLEM!\n" + profileMode);
                        break;
                }

                // SETUP PROFILE INFO
                ProfileInfo profileInfo = new ProfileInfo(profileName, electrodeList, profileMode);
                profileInfos[i] = profileInfo;

                reader.close();
                i++;
            }
            return profileInfos;
        } catch (Exception e) {
            Log.v("h", "PROBLEM!");
            Log.v("", e.getMessage());
            return null;
        }
    }
}
