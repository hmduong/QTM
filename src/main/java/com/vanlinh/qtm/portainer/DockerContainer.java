package com.vanlinh.qtm.portainer;

import com.ftl.dictionary.Dictionary;
import com.ftl.dictionary.DictionaryNode;

import java.util.List;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
public class DockerContainer
{
	DictionaryNode config;
	PortainerNode portainerNode;
	String id;
	String containerName;
	List<String> logFile;

	public DictionaryNode getConfig()
	{
		return config;
	}

	public void setConfig(DictionaryNode config)
	{
		this.config = config;
	}

	public PortainerNode getPortainerNode()
	{
		return portainerNode;
	}

	public void setPortainerNode(PortainerNode portainerNode)
	{
		this.portainerNode = portainerNode;
	}

	public String getId()
	{
		return id;
	}

	public void setId(String id)
	{
		this.id = id;
	}

	public String getContainerName()
	{
		return containerName;
	}

	public void setContainerName(String containerName)
	{
		this.containerName = containerName;
	}

	public List<String> getLogFile()
	{
		return logFile;
	}

	public void setLogFile(List<String> logFile)
	{
		this.logFile = logFile;
	}

	public DockerContainer(DictionaryNode config, PortainerNode portainerNode, String id, String containerName, List<String> logFile)
	{
		this.config = config;
		this.portainerNode = portainerNode;
		this.id = id;
		this.containerName = containerName;
		this.logFile = logFile;
	}

	public DockerContainer()
	{
	}

	@Override
	public String toString()
	{
		return "DockerContainer{" +
				"config=" + config.name +
				", portainerNode=" + portainerNode +
				", id='" + id + '\'' +
				", containerName='" + containerName + '\'' +
				", logFile=" + logFile.size() +
				'}';
	}
}
