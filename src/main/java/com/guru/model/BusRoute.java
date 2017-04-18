package com.guru.model;

public class BusRoute {
	    private boolean turn;//true if way to go, false if way to return
	    private Integer id;
	    private String name;
	    private String information;

	    @Override
	    public String toString() {
	        return "{'turn':"+turn+", 'id':" + id + ", 'name':'" + name + "', 'information':'" + information + "'}";
	    }
	    public BusRoute(boolean turn, Integer id, String name) {
	        super();
	        this.turn = turn;
	        this.id = id;
	        this.name = name;
	    }
	    
	    public BusRoute(boolean turn, Integer id, String name, String information) {
			super();
			this.turn = turn;
			this.id = id;
			this.name = name;
			this.information = information;
		}
		public BusRoute(BusRoute b) {
	        this.turn = b.isTurn();
	        this.id = b.getId();
	        this.name = b.getName();
	    }

	    public Integer getId() {
	        return id;
	    }
	    public void setId(Integer id) {
	        this.id = id;
	    }
	    public String getName() {
	        return name;
	    }
	    public void setName(String name) {
	        this.name = name;
	    }
	    public boolean isTurn() {
	        return turn;
	    }
	    public void setTurn(boolean turn) {
	        this.turn = turn;
	    }
	    public String getInformation() {
	        return information;
	    }

	    public void setInformation(String information) {
	        this.information = information;
	    }
	
}
