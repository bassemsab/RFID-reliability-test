
package com.dgreat.rfid;




import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.Toolkit;

public class MainFrame {
	private JFrame frame;
	private JTextField txtHttprfidsystemnotifyphp;
	private JTextArea resultHere;
	private final String USER_AGENT = "Mozilla/5.0";
    int interval=5000;
    private PrintStream standardOut;
    private JTextField baudRate;
    private JTextField portNo;
    private JButton btnStop;
    private JButton btnStart;
	private volatile boolean bStop = false;
	private JButton btnClear;
	Props props = new Props();
	Tag tag = new Tag();
	public String bd = "5";
	public String pt = "3";
	public String license = "2048";
	public String notify = "http//:demo.epaxcis.com/system/m801_notify.php";
	private JButton btnSaveSetting;
	RFID rfid = new RFID();
	int open = 55;
	int readFlag = 0;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.setProperty("java.net.preferIPv4Stack" , "true");	
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainFrame window = new MainFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainFrame() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 400);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//frame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setTitle("Dgreat's Chafon CF MU801/804 Integration");
		frame.setSize(450, 400);
		frame.setLocationRelativeTo(null);    // centers on screen
		java.net.URL url = ClassLoader.getSystemResource("com/dgreat/resources/icon.png");
		Toolkit kit = Toolkit.getDefaultToolkit();
		Image img = kit.createImage(url);
		frame.setIconImage(img);
		
		//Property Values
		bd = props.getProp("baud");
		pt = props.getProp("port");
		license = props.getProp("license");
		notify = props.getProp("notify");
		
		txtHttprfidsystemnotifyphp = new JTextField();
		txtHttprfidsystemnotifyphp.setText("http://demo.epaxcis.com/system/m801_notify.php");
		txtHttprfidsystemnotifyphp.setBounds(10, 90, 315, 35);
		frame.getContentPane().add(txtHttprfidsystemnotifyphp);
		txtHttprfidsystemnotifyphp.setColumns(10);
		if(notify != "Not Found" && notify != null) {
			txtHttprfidsystemnotifyphp.setText(notify);
		}
		
		JLabel lblServerUrl = new JLabel("Server URL");
		lblServerUrl.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblServerUrl.setBounds(10, 65, 92, 14);
		frame.getContentPane().add(lblServerUrl);
        
        JLabel lblNewLabel = new JLabel("Reader Result");
        lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblNewLabel.setBounds(10, 136, 104, 20);
        frame.getContentPane().add(lblNewLabel);
        
        JLabel lblBaudRate = new JLabel("Baud Rate");
        lblBaudRate.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblBaudRate.setBounds(10, 11, 79, 20);
        frame.getContentPane().add(lblBaudRate);
        
        baudRate = new JTextField();
        baudRate.setText("5");
        baudRate.setBounds(10, 34, 104, 20);
        frame.getContentPane().add(baudRate);
        baudRate.setColumns(10);
        if(bd != "Not Found" && bd != null) {
        	baudRate.setText(bd);
		}
        
        JLabel lblPortNo = new JLabel("Port No.");
        lblPortNo.setFont(new Font("Tahoma", Font.BOLD, 14));
        lblPortNo.setBounds(125, 11, 79, 20);
        frame.getContentPane().add(lblPortNo);
        
        portNo = new JTextField();
        portNo.setText("3");
        portNo.setBounds(125, 34, 92, 20);
        frame.getContentPane().add(portNo);
        portNo.setColumns(10);
        if(pt != "Not Found" && pt != null) {
        	portNo.setText(pt);
		}
        
        btnStart = new JButton("Start");
        btnStart.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnStart.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.out.print("Starting");
        		bStop = false;
        		try {
        			String pu = txtHttprfidsystemnotifyphp.getText();
        			String nb = baudRate.getText();
        			String np = portNo.getText();
        			byte baud = Byte.valueOf(baudRate.getText());
                    int port = Integer.valueOf(portNo.getText());
 


        			props.setProp("baud", nb);
        			props.setProp("port", np);
        			props.setProp("notify", pu);
        			open = rfid.openComPort(port, baud);
            		//System.out.print("About t getRFID");
        			getRFID();
        			btnStart.setEnabled(false);
        			btnStop.setEnabled(true);
        		} catch (Exception e1) {

        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
        		
        	}
        });
        btnStart.setBackground(Color.GREEN);
        btnStart.setBounds(236, 33, 89, 23);
        frame.getContentPane().add(btnStart);
        
        btnStop = new JButton("Stop");
        btnStop.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnStop.setBackground(Color.RED);
        btnStop.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		System.out.print("Stopping");
        		if(readFlag == 0) {
	        		rfid.closeComPort();
	        		bStop = true;
	        		btnStart.setEnabled(true);
	    			btnStop.setEnabled(false);
        		}
        	}
        });
        btnStop.setBounds(335, 33, 89, 23);
        frame.getContentPane().add(btnStop);
        btnStop.setEnabled(false);
        
        resultHere = new JTextArea(10, 51);
        resultHere.setBounds(10, 167, 414, 183);
        resultHere.setEditable(false);   
        PrintStream printStream = new PrintStream(new CustomOutputStream(resultHere));
        //frame.getContentPane().add(resultHere);
        
        JScrollPane scrollPane = new JScrollPane(resultHere);
        scrollPane.setBounds(10, 167, 414, 183);
        frame.getContentPane().add(scrollPane);
        
        btnClear = new JButton("Clear");
        btnClear.setFont(new Font("Tahoma", Font.BOLD, 12));
        btnClear.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			resultHere.getDocument().remove(0,
        					resultHere.getDocument().getLength());
                    //standardOut.println("Text area cleared");
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
        	}
        });
        btnClear.setForeground(Color.WHITE);
        btnClear.setBackground(Color.BLUE);
        btnClear.setBounds(335, 137, 89, 23);
        frame.getContentPane().add(btnClear);
        
        btnSaveSetting = new JButton("Save");
        btnSaveSetting.addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		try {
        			String pu = txtHttprfidsystemnotifyphp.getText();
        			String nb = baudRate.getText();
        			String np = portNo.getText();
        			
        			props.setProp("baud", nb);
        			props.setProp("port", np);
        			props.setProp("notify", pu);
        		} catch (Exception e1) {
        			// TODO Auto-generated catch block
        			e1.printStackTrace();
        		}
        	}
        });
        btnSaveSetting.setToolTipText("Save Current Settings as Start Up Default");
        btnSaveSetting.setFont(new Font("Tahoma", Font.BOLD, 14));
        btnSaveSetting.setBounds(335, 90, 89, 35);
        frame.getContentPane().add(btnSaveSetting);
        
        // keeps reference of standard output stream
        standardOut = System.out;
         
        // re-assigns standard output stream and error output stream
        System.setOut(printStream);
        System.setErr(printStream);
        
        
        //license check
  		long now = Instant.now().toEpochMilli();
  		//System.out.println(now);
  		String check = license.replaceAll("[a-zA-Z]","");

  		long lic = Long.valueOf(check);
  		if(now > lic) {
  			JOptionPane.showMessageDialog(frame, lic+"=="+now+" - License Has Expired.");
  			btnStart.setEnabled(false);
			btnStop.setEnabled(false);
			btnSaveSetting.setEnabled(false);
			btnClear.setEnabled(false);
  		}
  		/*else {
  			JOptionPane.showMessageDialog(frame, lic+"=="+now+" - Active License.");
  		}*/
  		
	}
	
	public void getRFID() throws Exception {
		
		  Thread thread = new Thread(new Runnable() {
			
            @Override
            public void run() {
            	
            	String postUrl = txtHttprfidsystemnotifyphp.getText();
            	byte baud = Byte.valueOf(baudRate.getText());
                int port = Integer.valueOf(portNo.getText());
                int counter = 1;
    			long times=0;
    	        ArrayList<String> tstamp= new ArrayList<String>();
		     	tstamp.add("0"); 
			    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");

            	while (!bStop) {
            		


                    try {

                        //Thread.sleep(interval); // 10000 = 10 sec
                		if(open == 0) {
                			
            				System.out.println("Port Open");             
                			readFlag = 1;
	                		String tags = rfid.readInventory();
	        		     	tstamp.add(formatter.format((new Date()))); 


	                                           
	                		String[] tagArr = tags.replace("|", "&").split("&");
	                		//System.out.println(counter + " @ " + tags); 
	                		if(tags!=null && tags != ""){

                				System.out.println("____________ Tag Business: " + (new Date())+"________");
                				System.out.println("________________________________________________________");
                				//System.out.println("Cards Found : \n -" + tags.replace("|", "\n -"));            		         
           	    		    	//System.out.println(tstamp.get(tstamp.size()-1));

                		                      		     	
                    				try{ 
                    	    			 if (tstamp.get(tstamp.size()-1).equals(tstamp.get(tstamp.size()-2))) {
                    	    					 System.out.println("iffing:");
                    	    					 times+=1;
                    	    					// tag.add( tagArr[0],tagArr[1],times, tstamp.get(tstamp.size()-1)); 
                    	    					// System.out.println("Card Added : " + tagArr[1]); 
                                   	    	  }
                    	    			 else {  
        		                				 System.out.println("Elsing : " + times);
                       	    				 	tag.add( tagArr[0],tagArr[1],times,tstamp.get(tstamp.size()-1)); 
       		                			  	 	System.out.println("Card Added : " + tagArr[1]);

                    	    					//System.out.println(tstamp.get(tstamp.size()-1));
                    	    					//System.out.println(tstamp.get(tstamp.size()-2));
                    	    					 times=1;


                    	    			 	   }                   			
                    	    			 }	
                    				
                    				catch(Exception e)                   				
                    					{System.out.println("Failed To Add : " + tagArr[1]);
                    				     //times=20;
                    					//System.out.println("Failed To Add : -" + times);
                    					}	                			
                    				
	                //			counter = counter - 5;
	                //			interval = 1;
	                			//}else {
	                				//System.out.println("Increaase Counter"); 
	                				//counter = counter + 1;
	                				//interval = 1;
	                			//}
	                		};
	                		
	                		readFlag = 0;
                		};
                	//	System.out.println("Unable to Open Port");
                		//rfid.closeComPort();
                    	//interval = 1;		
                    }
                     catch (Exception e) { e.printStackTrace();}                  
                         
                		};// while (!bStop)
            };
        
		});
			 
        thread.start();
	}
            
	private static String readUrl(String urlString) throws Exception {
	    BufferedReader reader = null;
	    try {
	        URL url = new URL(urlString);
	        reader = new BufferedReader(new InputStreamReader(url.openStream()));
	        StringBuffer buffer = new StringBuffer();
	        int read;
	        char[] chars = new char[1024];
	        while ((read = reader.read(chars)) != -1)
	            buffer.append(chars, 0, read); 
	        return buffer.toString();
	    } finally {
	        if (reader != null)
	            reader.close();
	    }
	}
	public static String readRedirect(String urlString) {
		String ret = "";
		try {
			URL obj = new URL(urlString);
			HttpURLConnection conn = (HttpURLConnection) obj.openConnection();
			conn.setReadTimeout(5000);
			conn.addRequestProperty("Accept-Language", "en-US,en;q=0.8");
			conn.addRequestProperty("User-Agent", "Mozilla");
			conn.addRequestProperty("Referer", "google.com");

			System.out.println("Requestes URL ... ");
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
			
			//System.out.println("URL Content... \n" + html.toString());
			//System.out.println("Done");
			//System.out.println("Response Code ... " + status);
			ret = html.toString();
			if (redirect) {
				System.out.println("Redirecting URL ... ");
				// get redirect url from "location" header field
				String newUrl = conn.getHeaderField("Location");
				readRedirect(newUrl);			
			}
	    } catch (Exception e) { e.printStackTrace(); }
		
		return ret;
	}
}
