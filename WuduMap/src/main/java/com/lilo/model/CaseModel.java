package com.lilo.model;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class CaseModel implements Parcelable{

	private String caseId;
	private String caseName;
	private double lon;
	private double lat;
	private String gridCode;
	public static final Creator<CaseModel> CREATOR = new Creator<CaseModel>() {

		@Override
		public CaseModel createFromParcel(Parcel source) {
			CaseModel caseModel = new CaseModel();
			caseModel.caseId = source.readString();
			caseModel.caseName = source.readString();
			caseModel.lon = source.readDouble();
			caseModel.lat = source.readDouble();
			caseModel.gridCode = source.readString();
			return caseModel;
		}

		@Override
		public CaseModel[] newArray(int size) {

			return new CaseModel[size];
		}
	};
	
	public CaseModel() {
		super();
	}
	
	public CaseModel(String caseId, double lon, double lat) {
		super();
		this.caseId = caseId;
		this.lon = lon;
		this.lat = lat;
	}

	public CaseModel(String caseId, String caseName, double lon, double lat) {
		super();
		this.caseId = caseId;
		this.caseName = caseName;
		this.lon = lon;
		this.lat = lat;
	}

	public CaseModel(String caseId, double lon, double lat, String gridCode) {
		super();
		this.caseId = caseId;
		this.lon = lon;
		this.lat = lat;
		this.gridCode = gridCode;
	}

	public CaseModel(double lon, double lat, String gridCode) {
		super();
		this.lon = lon;
		this.lat = lat;
		this.gridCode = gridCode;
	}

	public String getCaseId() {
		return caseId;
	}

	public void setCaseId(String caseId) {
		this.caseId = caseId;
	}

	public double getLon() {
		return lon;
	}

	public void setLon(double lon) {
		this.lon = lon;
	}

	public double getLat() {
		return lat;
	}
	
	public void setLat(double lat) {
		this.lat = lat;
	}

	public String getGridCode() {
		return gridCode;
	}

	public void setGridCode(String gridCode) {
		this.gridCode = gridCode;
	}

	public String getCaseName() {
		return caseName;
	}
	
	public void setCaseName(String caseName) {
		this.caseName = caseName;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(caseId);
		dest.writeString(caseName);
		dest.writeDouble(lon);
		dest.writeDouble(lat);
		dest.writeString(gridCode);
	}

}
