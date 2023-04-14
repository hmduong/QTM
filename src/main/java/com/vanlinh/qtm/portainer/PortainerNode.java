package com.vanlinh.qtm.portainer;

import com.ftl.dictionary.Dictionary;
import com.ftl.dictionary.DictionaryNode;

public class PortainerNode
{
	public DictionaryNode config;
	public int nodeId;
	public String name;

	public PortainerNode(DictionaryNode config, int nodeId, String name)
	{
		this.config = config;
		this.nodeId = nodeId;
		this.name = name;
	}
}