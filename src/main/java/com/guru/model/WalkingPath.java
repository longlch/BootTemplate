package com.guru.model;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

public class WalkingPath {
	
    private Integer stationFromId;
    private Integer stationToId;
    private Integer distance;

    public WalkingPath(Integer stationFromId, Integer stationToId, Integer distance) {
        this.stationFromId = stationFromId;
        this.distance = distance;
        this.stationToId = stationToId;
    }

    @Override
    public String toString() {
        return "WalkingPath [stationFromId=" + stationFromId + ", stationToId=" + stationToId + ", distance=" + distance
                + "]";
    }

    public Integer getStationFromId() {
        return stationFromId;
    }

    public void setStationFromId(Integer stationFromId) {
        this.stationFromId = stationFromId;
    }

    public Integer getStationToId() {
        return stationToId;
    }

    public void setStationToId(Integer stationToId) {
        this.stationToId = stationToId;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
}
	
}
