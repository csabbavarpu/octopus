package framework.security;
import java.io.File;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.report.ReportGenerator;
import org.parosproxy.paros.view.SiteMapPanel;
import org.testng.annotations.Test;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ApiResponse;
import org.zaproxy.clientapi.core.ApiResponseFactory;
import org.zaproxy.clientapi.core.ClientApi;
import org.zaproxy.clientapi.core.ClientApiMain;
import org.zaproxy.clientapi.gen.Ascan;
import org.zaproxy.clientapi.gen.Spider;
import org.zaproxy.zap.extension.ascan.ActiveScan;
import org.zaproxy.zap.extension.httppanel.component.all.response.ResponseAllComponent;
import org.zaproxy.zap.extension.httppanel.view.HttpPanelView;
import org.zaproxy.zap.extension.httppanel.view.image.ResponseImageView;

import framework.util.PropertyLoader;


public class ZapSecurityTest {

	Logger logger = Logger.getLogger(ZapSecurityTest.class);
 //   private static int ZAP_PORT = 8090;
   // private static String TARGET = "http://localhost:8080/bodgeit/";
    
    private static int ZAP_PORT  ;
    private static String TARGET ;

    private static void startZap() throws Exception {
        System.out.println("Starting ZAP...");
        // Path to zap.sh or zap.bat
        new ProcessBuilder("/Users/pushpinderrattan/Desktop/Zap/OWASP ZAP.app/Contents/Java/zap.sh", "-daemon", "-port " + ZAP_PORT).start();

        System.out.println("Waiting for ZAP...");
        Thread.sleep(15000);

    }

    static Alert alert2;
    
    @Test(testName="SecurityTest")
    public  void testVulnerabilities() throws Exception {
        startZap();
       ZapSecurityTest util = new ZapSecurityTest();
        util.loadFromPropertyFile();
        
        final ClientApi clientApi = new ClientApi("localhost", ZAP_PORT);
        //clientApi.accessUrl(TARGET);
       // clientApi.activeScanSiteInScope(TARGET);
 
        System.out.println("Accessing target: " + TARGET);
        
        clientApi.accessUrl(TARGET);
        
        System.out.println("-------Running Spider-----");
        clientApi.spider.scan(TARGET);
     
		System.out.println("Sleep for 10 sec, to  record URL to find vulnerabilities");
        Thread.sleep(10000);
        
       // System.out.println("Peforming Active Scan");
        //clientApi.ascan.scan(TARGET,"true","true");
     
 
        
	    List<Alert> alert= clientApi.getAlerts("", 0, 100);
         for (Iterator iterator = alert.iterator(); iterator.hasNext();) {
			alert2 = (Alert) iterator.next();
			System.out.println("-----Alert is-----"+alert2.getAlert());
			System.out.println("URL is::  "+alert2.getUrl());
			System.out.println("Risk is::  "+alert2.getRisk());
			
			
		}
         
       //  System.out.println("Generating HTML report");
       //  generateReport();
       
        System.out.println("Shutdown ZAP.");
        clientApi.core.shutdown();
  
    }


    public static void generateReport()
    {
    	
    	ReportGenerator report = new ReportGenerator();
    	report.entityEncode(alert2.toString());
    	ReportGenerator.stringToHtml(report.entityEncode(alert2.toString()),"test.html");
    	
    	System.out.println("File at"+new File("test.html").getAbsolutePath());
    	
    }
    public void loadFromPropertyFile() throws IOException
    {
    	//File file = new File("zap.properties");
    	InputStream in = getClass().getResourceAsStream("zap.properties"); 
    	PropertyLoader.loadProperties(in, true);
        ZAP_PORT = Integer.parseInt(PropertyLoader.getProperty("zap.port"));
        System.out.println("ZAP_PORT "+ZAP_PORT);
        TARGET =PropertyLoader.getProperty("zap.targetApp");
        System.out.println("TARGET "+TARGET);
    	
    }
}
