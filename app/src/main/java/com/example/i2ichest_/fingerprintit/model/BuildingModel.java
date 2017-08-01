package com.example.i2ichest_.fingerprintit.model;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.Serializable;

public class BuildingModel {
    private Building building;
    Gson gson = new GsonBuilder().create();

    public BuildingModel(){
        building = new Building();
    }

    public BuildingModel(String jsonResponse){
        building = gson.fromJson(jsonResponse,Building.class);
    }

    public Building getBuilding(){
        return building;
    }

    public String toJSONString(){
        return gson.toJson(this.building);
    }

    public class Building implements Serializable {
        private Long buildingID;
        private String buildingName;

        public Building() {
        }

        public Building(Long buildingID, String buildingName) {
            this.buildingID = buildingID;
            this.buildingName = buildingName;
        }

        public String getBuildingName() {
            return buildingName;
        }

        public void setBuildingName(String buildingName) {
            this.buildingName = buildingName;
        }

        public Long getBuildingID() {
            return buildingID;
        }

        public void setBuildingID(Long buildingID) {
            this.buildingID = buildingID;
        }
    }

}
