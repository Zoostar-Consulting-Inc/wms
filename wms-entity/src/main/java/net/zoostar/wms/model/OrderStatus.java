package net.zoostar.wms.model;

public enum OrderStatus {
	O("Committed"),
	S("Shipped"),
	D("Delivered"),
	P("Pickup Up"),
	R("Recieved");
	
	private final String status;
	
	OrderStatus(String status) {
		this.status = status;
	}
	
	public String status() {
		return status;
	}
}
