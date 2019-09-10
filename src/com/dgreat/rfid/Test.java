package com.dgreat.rfid;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Test {
	public static void main(String[] args){
		String urlString = "http://menate.kapotiglobal.com/system/m801_notify.php?oper=notify&tags=E200680D0000000000000001";
		String urlString1 = "https://nims.infinityalps.online:12101/notify_upm/server/home.php?notificationSubmit=1&tag_id=E200680D0000000000000001&msg=New+Pick+Up+For+Tag+ID%3A+E200680D0000000000000001";
		
		readRedirect(urlString);
		//readUrl(urlString1);
	}
	
	public static void readUrl(String urlString) {
		try {
			BufferedReader reader = null;
		    try {
		        URL url = new URL(urlString);
		        reader = new BufferedReader(new InputStreamReader(url.openStream()));
		        StringBuffer buffer = new StringBuffer();
		        int read;
		        char[] chars = new char[1024];
		        while ((read = reader.read(chars)) != -1)
		            buffer.append(chars, 0, read); 
		        System.out.println(buffer.toString());
		    } finally {
		        if (reader != null)
		            reader.close();
		    }
		}catch(Exception e) {System.out.println(e);}
	}
	
	public static void readRedirect(String urlString) {
		try {

			String url = "http://www.twitter.com";

			URL obj = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");

			System.out.println("Request URL ... " + url);

			boolean redirect = false;

			// normally, 3xx is redirect
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
					|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
				redirect = true;
			}
			BufferedReader in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
			String inputLine;
			StringBuffer html = new StringBuffer();
			
			while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);
			}
			in.close();
			
			System.out.println("URL Content... \n" + html.toString());
			System.out.println("Done");
			System.out.println("Response Code ... " + status);

			if (redirect) {

				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField("Location");
				readRedirect(newUrl);
							
			}

			

	    } catch (Exception e) {
		e.printStackTrace();
	    }

	 }
}
