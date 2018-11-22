package Definitions;


/**
 * classse qui definie une trame 
 * 
 * 
 * **/
public class Frame {

	private final String FLAG="01111110";
	private String type;
	private int num;
	private String data;
	private String crc;
	
	public Frame(String type,int num,String data,String crc){
		this.type=type;
		this.num=num;
		this.data=data;
		this.crc=crc;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getCrc() {
		return crc;
	}

	public void setCrc(String crc) {
		this.crc = crc;
	}

	public String getFLAG() {
		return FLAG;
	}

	public String toString() {
		return "Frame [FLAG=" + FLAG + ", type=" + type + ", num=" + num
				+ ", data=" + data + ", crc=" + crc + "]";
	}
	
	
	
	
	
	
	
	
	
	
	
}
