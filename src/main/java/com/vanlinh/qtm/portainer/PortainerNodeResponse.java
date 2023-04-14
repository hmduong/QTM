package com.vanlinh.qtm.portainer;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
public class PortainerNodeResponse
{
	@SerializedName("Id")
	public int id;

	@SerializedName("Name")
	public String name;

	@SerializedName("Type")
	public String type;

	@SerializedName("URL")
	public String url;

	@SerializedName("Status")
	public String status;

	@SerializedName("GroupId")
	public Long groupId;

	@SerializedName("Snapshots")
	public List<Snapshot> snapshots;

	public Long totalContainer;

	public class Snapshot
	{
		public Long RunningContainerCount;
		public Long StoppedContainerCount;
		public Long HealthyContainerCount;
		public Long UnhealthyContainerCount;
		public Long VolumeCount;
		public Long ImageCount;
		public Long ServiceCount;
		public Long TotalCPU;
		public Double TotalMemory;
	}
}
