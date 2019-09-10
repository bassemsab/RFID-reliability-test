package com.dgreat.rfid;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class Props {
	Properties prop = new Properties();
	InputStream input = null;
	OutputStream output = null;
	
	public Props() {
		String now = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());

		this.setProp("LastRun", now);
	}
	public String getProp(String key) {
		String ret = "Not Found";
		
		try {
			input = new FileInputStream("config.properties");
			// load a properties file
			if(input==null){
	            System.out.println("Sorry, unable to find properties");
			    return ret;
			}
			prop.load(input);
			ret = prop.getProperty(key);
		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return ret;
	}
	
	public boolean setProp(String key,String value) {
		boolean ret = false;
		try {
			input = new FileInputStream("config.properties");
			prop.load(input); 
			input.close();
			
			output = new FileOutputStream("config.properties");			
			prop.setProperty(key, value);
			
			// save properties to project root folder
			prop.store(output, null);

		} catch (IOException io) {
			io.printStackTrace();
		} finally {
			if (output != null) {
				try {
					input.close();
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

		}

		return ret;
	}
	
	
	
	public static void main(String[] args) {
		Props p = new Props();
		System.out.println(p.getProp("LastRun"));
		p.setProp("license", "2048");
		//p.test();
	}

}
