package it.polito.tdp.crimes.model;

public class OffenseIdEdge {

	private String v1;
	private String v2;
	private Integer weight;
	
	public OffenseIdEdge(String v1, String v2, Integer weight) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.weight = weight;
	}
	
	public String getV1() {
		return v1;
	}
	public String getV2() {
		return v2;
	}
	public Integer getWeight() {
		return weight;
	}

	@Override
	public String toString() {
		return v1 + " -- " + v2 + " --> " + weight;
	}
	
	
}
