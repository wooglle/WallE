package com.woog.walle.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ConfigReader {
	//http://www.cnblogs.com/Jermaine/archive/2010/10/24/1859673.html
	private Map<String,Map<String,List<String>>> map = null;
	private String Section = null;
	
	/**
	 * 读取
	 * @param path
	 */
	public ConfigReader(String path){
		map = new HashMap<String,Map<String,List<String>>>();
		try{
			BufferedReader reader = new BufferedReader(new FileReader(path));
			read(reader);
		}catch(IOException e){
			e.printStackTrace();
			throw new RuntimeException("IO Exception:" + e);
		}
	}

	/**
	 * 读取文件
	 * @param reader
	 * @throws IOException 
	 */
	private void read(BufferedReader reader) throws IOException {
		String line = null;
		while((line=reader.readLine()) != null){
			parseLine(line);
		}
	}

	/**
	 * 转换
	 * @param line
	 */
	private void parseLine(String line) {
		line = line.trim();
		if(line.matches("^\\#.*$")){
			//此部分为注释，格式为：#string
			return;
		}else if(line.matches("^\\[\\S+\\]$")){
			//section，格式为：[SECTION]
			String section = line.replace("^\\[(\\S+)\\]$", "$1");
			addSection(map,section);
		}else if(line.matches("^\\S+=.*$")){
			//key,value，格式为：key=value
			int i = line.indexOf("=");
		}
		
	}

	/**
	 * 增加Section
	 * @param map2
	 * @param section
	 */
	private void addSection(Map<String, Map<String, List<String>>> map2, String section) {
		if(!map.containsKey(section)){
			Section = section;
			Map<String,List<String>> childMap = new HashMap<String,List<String>>();
			map.put(section, childMap);
		}
		
	}
	
	/**
	 * 获取指定文件里指定section的指定key的值
	 * @param section
	 * @param key
	 * @return
	 */
	public List<String> get(String section, String key){
		if(map.containsKey(section)){
			return map.get(section).containsKey(key) ? map.get(section).get(key) : null;
		}
		return null;
	}
	
	/**
	 * 获取配置文件指定的section的子键和值
	 * @param section
	 * @return
	 */
	public Map<String, List<String>> get(String section){
		return map.containsKey(section) ? map.get(section) : null;
	}
	
	/**
	 * 获取这个配置文件的节点和值
	 * @return
	 */
	public Map<String, Map<String, List<String>>> get(){
		return map;
	}
}
