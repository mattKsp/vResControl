package com.visutal.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class ServerComms {

	private String serverIp = "0.0.0.0";			//"192.168.0.1";
	private InetAddress serverAddr;
	private String multicastIp = "255.255.255.255";
	private InetAddress multicastAddr;
	private int serverUdpPort = 8000;				//9997
	//localUdpPort = 7001 / taken from saved prefs
	private String resolumeIp = "192.168.0.25";
	private InetAddress resolumeAddr;
	private int resolumeUdpPort = 7000;
		
	public ServerComms() throws UnknownHostException{
		serverAddr = InetAddress.getByName(this.serverIp);
		multicastAddr = InetAddress.getByName(this.multicastIp);
	}
	
	public ServerComms(String serverIp1)throws UnknownHostException{
		this.serverIp = serverIp1;
		serverAddr = InetAddress.getByName(this.serverIp);
		multicastAddr = InetAddress.getByName(this.multicastIp);
	}

	public ServerComms(String serverIp1, int serverUdpPort)throws UnknownHostException{
		this.serverIp = serverIp1;
		this.serverUdpPort = serverUdpPort;
		this.serverAddr = InetAddress.getByName(this.serverIp);
		multicastAddr = InetAddress.getByName(this.multicastIp);
	}
	
	public ServerComms(String serverIp1, int serverUdpPort, String resolumeIp1, int resolumeUdpPort)throws UnknownHostException{
		this.serverIp = serverIp1;
		this.resolumeIp = resolumeIp1;
		this.serverUdpPort = serverUdpPort;
		this.resolumeUdpPort = resolumeUdpPort;
		this.serverAddr = InetAddress.getByName(this.serverIp);
		this.resolumeAddr = InetAddress.getByName(this.resolumeIp);
		multicastAddr = InetAddress.getByName(this.multicastIp);
	}
	
	public String getServerIp1() {
		return serverIp;
	}
	
	public String getResolumeIp1() {
		return resolumeIp;
	}
	
	public String getMulticastIp() {
		return multicastIp;
	}

	public void setServerIp1(String serverIp1)throws UnknownHostException {
		this.serverIp = serverIp1;
		this.serverAddr = InetAddress.getByName(this.serverIp);
	}

	public InetAddress getServerAddr() {
		return serverAddr;
	}
	
	public InetAddress getResolumeAddr() {
		return resolumeAddr;
	}
	
	public InetAddress getMulticastAddr() {
		return multicastAddr;
	}

	public void setServerAddr(InetAddress serverAddr) {
		this.serverAddr = serverAddr;
	}
	
	public void setResolumeAddr(InetAddress resolumeAddr) {
		this.resolumeAddr = resolumeAddr;
	}

	public int getServerUdpPort() {
		return serverUdpPort;
	}
	
	public int getResolumeUdpPort() {
		return resolumeUdpPort;
	}

	public void setServerUdpPort(int serverUdpPort) {
		this.serverUdpPort = serverUdpPort;
	}
	
	public void setResolumeUdpPort(int resolumeUdpPort) {
		this.resolumeUdpPort = resolumeUdpPort;
	}
	
}
