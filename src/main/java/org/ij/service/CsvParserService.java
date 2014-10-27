package org.ij.service;

import org.ij.model.Trip;
import org.ij.utils.DateUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Abdelkader Midani on 26/10/14.
 */
@Service
public class CsvParserService {



    private Trip tripToDo;
    private List<Trip> availableTrips;

    public Trip getTripToDo() {
        return tripToDo;
    }

    public void setTripToDo(Trip tripToDo) {
        this.tripToDo = tripToDo;
    }

    public List<Trip> getAvailableTrips() {
        return availableTrips;
    }

    public void setAvailableTrips(List<Trip> availableTrips) {
        this.availableTrips = availableTrips;
    }

    BufferedReader br;
    String cvsSplitBy = ";";


    public void parseCsvFile(InputStream csvFile){

        int nbAvailableTrips = 0;
        String line = "";

        try {

            br = new BufferedReader(new InputStreamReader(csvFile));

            int index = 0;
            while ((line = br.readLine()) != null) {
                if(index == 1){
                    nbAvailableTrips = Integer.valueOf(line);
                    if(nbAvailableTrips > 0){
                        availableTrips = new ArrayList<Trip>();
                    }else{
                        break;
                    }
                    index++;
                    continue;
                }
                // use semicolon as separator
                String[] trip = line.split(cvsSplitBy);
                Trip tripTmp = new Trip();
                tripTmp.setDepartureTime(trip[0]);
                tripTmp.setFrom(trip[1]);
                tripTmp.setTo(trip[2]);
                if(index == 0){
                    tripToDo = tripTmp;
                }else{
                    tripTmp.setTerm(DateUtils.getNbMinutesFromTime(trip[3]));
                    tripTmp.setArrivalTime(DateUtils.getArrivalTime(trip[0],trip[3]));
                    availableTrips.add(tripTmp);
                }

                index++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
