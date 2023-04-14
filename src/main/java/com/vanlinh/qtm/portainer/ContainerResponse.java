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
public class ContainerResponse
{

	@SerializedName("Id")
	public String id;

	@SerializedName("Image")
	public String image;

	@SerializedName("Names")
	public List<String> names;

	@SerializedName("Path")
	public String path;

	@SerializedName("Config")
	public String config;

	@SerializedName("State")
	public String state;

	public String getName()
	{
		String name = names.get(0);
		if (name.startsWith("/"))
			return name.substring(1);
		return name;
	}

	public String getImage()
	{
		String image = this.image;
		int index = image.indexOf("@");
		return image.substring(0, index == -1 ? image.length() : index);
	}
}
