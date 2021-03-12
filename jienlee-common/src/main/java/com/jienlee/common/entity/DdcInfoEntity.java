package com.jienlee.common.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * {"role":"1","os":"iPhone+OS_7.1.2;v2.0.0","deviceToken":"6e5dbe3c276e914104915e9953dbfc0a3f21d8906342a5ed46ecb2f7cbeed814","net":"wifi","mobiletype":"1","idfa":"15AE095C-2EB6-4405-8882-9C9140C49D83","longitude":"","screen":"568*320","version":"2.0.0","latitude":"","model":"iPod+touch"}
 * {"screen":"1280*720_2.0","os":"17_4.2.2","model":"HM+NOTE+1W","imei":"863989028994234","role":"0","longitude":"116.486802","mac":"64:b4:73:45:79:c7","latitude":"40.002629","imsi":"460015061506248","net":"wifi","mobiletype":"2","version":"2.0.0.didapinche_taxi_official"}
 * @author jien.lee
 */
@Data
public class DdcInfoEntity implements Serializable{

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private String version;
	private String mobileType;
	private String os;
	private String screen;
	private String model;
	private String longitude;
	private String latitude;
	private String dir;
	private String speed;
	private String altitude;
	private String role;
	private String net;
	private String location_time;
	private String deviceId;

	//ios only
	private String idfa;
	private String deviceToken;

	//android only
	private String channel;
	private String mac;
	private String imei;
	private String imsi;

	private String ip;
}
