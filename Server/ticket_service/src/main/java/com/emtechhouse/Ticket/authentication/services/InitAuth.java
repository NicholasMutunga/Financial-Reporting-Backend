package com.emtechhouse.Ticket.authentication.services;

import com.emtechhouse.Ticket.authentication.basicActions.Basicactions;
import com.emtechhouse.Ticket.authentication.models.Privilege;
import com.google.gson.Gson;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class InitAuth {
    @Value("${spring.application.basicActions}")
    private String basicActions;

    public List<Privilege> getAllPriviledges(){
        String json = "{ \"privileges\":  [\n" +
                "      { name: 'DASHBOARD', selected: true, code: 1 },\n" +
                "      { name: 'CONFIGURATIONS', selected: true, code: 2 },\n" +
                "      { name: 'ASSIGN TICKETS', selected: true, code: 3 },\n" +
                "      { name: 'MY TICKETS', selected: true, code: 4 },\n" +
                "      { name: 'MANAGE TICKETS', selected: true, code: 5 },\n" +
                "      { name: 'ALL TICKETS', selected: true, code: 6 },\n" +
                "      { name: 'USER GUIDE', selected: true, code: 7 },\n" +
                "      { name: 'SURVEYS', selected: true, code: 8 },\n" +
                "      { name: 'REPORTS', selected: true, code: 9 },\n" +
                "      { name: 'ACCESS MANAGEMENT', selected: true, code: 10 },\n" +
                "      { name: 'APPROVAL MANAGEMENT', selected: false, code: 11 },\n" +
                "    ]}";
        List<Privilege> privilegeList = new ArrayList<>();
        JSONObject jo = new JSONObject(json);
        JSONArray ja = jo.getJSONArray("privileges");
        for (Object obj : ja) {
            JSONObject jo2 = new JSONObject(obj.toString());
            Privilege privilege = new Gson().fromJson(jo2.toString(),Privilege.class);
            privilegeList.add(privilege);
        }
        return privilegeList;
    }
    public List<Basicactions> getAllBasicActions() {
        List<Basicactions> basicactionsList = new ArrayList<>();
        try {
            File file = ResourceUtils.getFile(basicActions);
            System.out.println("*****************************************************************");
            if (file.exists()){
                System.out.println("PATH FOR BASIC ACTIONS: " + file.getName() );
            }
            InputStream in = new FileInputStream(file);
            String strbasicactions = readFileToString(file.getAbsolutePath());
            JSONObject jo = new JSONObject(strbasicactions);
            JSONArray ja = jo.getJSONArray("basicActions");
            for (Object obj : ja) {
                JSONObject jo2 = new JSONObject(obj.toString());
                Basicactions basicActions = new Gson().fromJson(jo2.toString(),Basicactions.class);
                basicactionsList.add(basicActions);
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return basicactionsList;
    }

    public String readFileToString(String path)
            throws IOException
    {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, Charset.defaultCharset());
    }


    public List<Basicactions> getViewBasicActions() {
        List<Basicactions> allActions = getAllBasicActions();
        List<Basicactions> viewActions = new ArrayList<>();
        for (Basicactions basicactions: allActions){
            if (isViewAction(basicactions.getName())){
                viewActions.add(basicactions);
            }
        }
        return viewActions;
    }

    private boolean isViewAction(String name) {
        String[] exceptions = new String[]{"ADD","MODIFY","DELETE","VERIFY","SATISFY","CLOSE","POST","ENTER","DETACH",
                "CASH WITHDRAWAL","CASH DEPOSIT","TRANSFER","PROCESS CHEQUE"};
        for (String e: exceptions) {
            if (name.equalsIgnoreCase(e))
                return false;
        }
        return true;
    }
}
