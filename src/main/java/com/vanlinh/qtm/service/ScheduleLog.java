package com.vanlinh.qtm.service;

import com.ftl.dictionary.Dictionary;
import com.ftl.dictionary.DictionaryNode;
import com.vanlinh.qtm.portainer.BasePortainer;
import com.vanlinh.qtm.portainer.DockerContainer;
import com.vanlinh.qtm.portainer.PortainerNode;
import com.vanlinh.qtm.utils.LoggerUtils;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>Title: </p>
 * <p>Copyright (c) 2022</p>
 * <p>Company: </p>
 *
 * @author Nguyen Van Linh
 * @version 1.0
 */
@Configuration
@EnableScheduling
public class ScheduleLog
{
	List<DockerContainer> containerList;
	Dictionary config;

	public void init() throws IOException
	{
		containerList = new ArrayList<>();
		config = new Dictionary("conf/config.dic");
		DictionaryNode portainerNode = config.getChild("Portainer.PORTAINER_NODE");
		for (DictionaryNode node : portainerNode.getChildList())
		{
			String name = node.getString("Name");
			String nodeId = node.getString("Id");
			int id = Integer.parseInt(nodeId.isEmpty() ? "-1" : nodeId);
			PortainerNode portainer = new PortainerNode(node, id, name);
			initContainer(portainer, node.getChild("Containers"));
		}
	}

	private void initContainer(PortainerNode portainer, DictionaryNode node)
	{
		for (DictionaryNode containerNode : node.getChildList())
		{
			String name = containerNode.getString("Name");
			String id = containerNode.getString("Id");
			DictionaryNode logNode = containerNode.getChild("Log");
			List<String> logs = new ArrayList<>();
			for (DictionaryNode log : logNode.getChildList())
				logs.add(log.value);
			DockerContainer container = new DockerContainer(containerNode, portainer, id, name, logs);
			containerList.add(container);
		}
	}

	@Scheduled(fixedDelay = 30000)
	public void scheduleFixedDelayTask() throws Exception
	{
		init();
		String logFolder = config.getString("LogFolder");
		BasePortainer basePortainer = BasePortainer.getInstance();
		for (DockerContainer container : containerList)
		{
			for (String logUrl : container.getLogFile())
			{
				String url = logFolder + "/" + container.getPortainerNode().name + "/" +
						container.getContainerName() + "/" + logUrl;
				InputStream inputStream = null;
				OutputStream outputStream = null;
				try
				{
					inputStream = basePortainer.getFileInContainer(container.getPortainerNode().nodeId,
						container.getContainerName(), logUrl);
					File file = new File(url);
					file.getParentFile().mkdirs();
					outputStream = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int length;
					while ((length = inputStream.read(buffer)) > 0)
					{
						outputStream.write(buffer, 0, length);
					}
					inputStream.close();
					outputStream.close();
				}
				catch (FileNotFoundException e)
				{
					LoggerUtils.warn("can't connect to container: " + container.getContainerName());
					LoggerUtils.warn("can't get file: " + url);
				}
			}
		}
		LoggerUtils.log(
				"Fixed delay task - " + System.currentTimeMillis() / 1000);
	}
}
