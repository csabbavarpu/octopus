/*package framework.security;
import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.parosproxy.paros.extension.report.ReportGenerator;
import org.zaproxy.clientapi.core.Alert;
import org.zaproxy.clientapi.core.ClientApi;


public class ZapProcessUtilBackup {

	Logger logger = Logger.getLogger(ZapProcessUtilBackup.class);
    private static final int ZAP_PORT = 8090;
    private static final String TARGET = "http://localhost:8080/bodgeit/";

    private static void startZap() throws Exception {
        System.out.println("Starting ZAP...");
        // Path to zap.sh or zap.bat
        new ProcessBuilder("/Users/pushpinderrattan/Desktop/Zap/OWASP ZAP.app/Contents/Java/zap.sh", "-daemon", "-port " + ZAP_PORT).start();

        System.out.println("Waiting for ZAP...");
        Thread.sleep(15000);

    }

    static Alert alert2;
    public static void main(String[] args) throws Exception {
        startZap();

        final ClientApi clientApi = new ClientApi("localhost", ZAP_PORT);

        System.out.println("Accessing target: " + TARGET);
        clientApi.accessUrl(TARGET);

        
       //Execute Scan twice , first to record URL and second to find vulnerabilities
        clientApi.spider.scan("http://localhost:8080/bodgeit/");
       
		
        
       // clientApi.spider.scan("http://localhost:8080/bodgeit/");
		System.out.println("Sleep for 10 sec");
        Thread.sleep(10000);
        
        
	    List<Alert> alert= clientApi.getAlerts("", 0, 100);
         for (Iterator iterator = alert.iterator(); iterator.hasNext();) {
			alert2 = (Alert) iterator.next();
			System.out.println("alert2"+alert2);
			
		}
         
         System.out.println("Generating HTML report");
         generateReport();
         
        System.out.println("Shutdown ZAP.");
        clientApi.core.shutdown();
      
    }

    public static void generateReport()
    {
    	
    	ReportGenerator report = new ReportGenerator();
    	report.entityEncode(alert2.toString());
    	ReportGenerator.stringToHtml(alert2.toString(),alert2.toString(),"test.html");
    	
    	System.out.println("File at"+new File("test.html").getAbsolutePath());
    	
    }
}
*/