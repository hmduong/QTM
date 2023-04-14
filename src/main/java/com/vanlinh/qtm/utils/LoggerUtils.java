package com.vanlinh.qtm.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class LoggerUtils
{
	private static Logger logger;

	private static Logger getLogger()
	{
		if(logger == null)
			logger = LoggerFactory.getLogger(LoggerUtils.class);
		return logger;
	}

	public static void log (String message)
	{
		getLogger().info(message);
	}

	public static void warn (String message)
	{
		getLogger().warn(message);
	}

	public static void error (String message)
	{
		getLogger().error(message);
	}

	public static void log (Exception e)
	{
		getLogger().error("{0}", e);
	}
}