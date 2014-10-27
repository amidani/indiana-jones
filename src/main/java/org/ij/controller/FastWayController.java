package org.ij.controller;

import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.ij.model.Trip;
import org.ij.service.CsvParserService;
import org.ij.service.DijktraAlgorithmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Abdelkader Midani on 25/10/14.
 */
@Controller
public class FastWayController {

    @Autowired
    private CsvParserService csvParserService;

    @Autowired
    private DijktraAlgorithmService dijktraAlgorithmService;

    @RequestMapping("/journey")
    public @ResponseBody LinkedList<String> getJourney(){
        return dijktraAlgorithmService.getPath(csvParserService.getTripToDo().getTo());
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    public @ResponseBody String uploadFile(HttpServletRequest req) throws IOException, FileUploadException {
        ServletFileUpload upload = new ServletFileUpload();
        FileItemIterator iter = upload.getItemIterator(req);
        if(iter.hasNext()) {
            FileItemStream item = iter.next();
            csvParserService.parseCsvFile(item.openStream());

            try {
                csvParserService.parseCsvFile(item.openStream());
                dijktraAlgorithmService.searchFastWay(csvParserService.getAvailableTrips(), csvParserService.getTripToDo());
            } catch (FileItemStream.ItemSkippedException e) {
            } catch (Exception e) {
                e.printStackTrace();
                return "You failed to upload " + item.getName()+".";
            }
        }else {
            return "You failed to upload, because the file was empty.";
        }
        return "You successfully uploaded!";
    }
}
