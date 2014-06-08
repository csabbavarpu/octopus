package framework.util;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.URL;

public class IOUtils
{

    /**
     * Reads the input stream for all bytes and returns the result
     * 
     * @param is
     * @return
     * @throws IOException
     */
    public static byte[] getStreamAsByteArray(InputStream is) throws IOException
    {
        byte[] bytes = new byte[1024];
        ByteArrayBuffer buffer = new ByteArrayBuffer(10240); // 10k

        int read;
        while ((read = is.read(bytes)) != -1) {
            buffer.append(bytes, 0, read);
        }

        return buffer.getBytes();
    }

    /**
     * Returns the contents of a file that is contained on the path of the
     * classloader. This is useful when retriving embedded files from a jar.
     * 
     * @param c
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] getFileFromClassLoader(Class c, String path) throws IOException
    {
        InputStream is = c.getClassLoader().getResourceAsStream(path);
        return getStreamAsByteArray(is);
    }

    /**
     * Retrieves the project root folder by finding the parent directory of the
     * classloader for the given class. This works by using the classloaders
     * protection domain. A classloader exists on either /bin or a jar usually,
     * so the project root is the parent directory.
     * 
     * @param c
     * @return
     */
    public static File getProjectRoot(Class c)
    {
        return getClasspathRoot(c).getParentFile();
    }

    /**
     * Retrieves the classpath root where the classes are located.  This is useful
     * to find resources.  This will not work point to a jar file or a directory.
     * 
     * @param c
     * @return
     */
    public static File getClasspathRoot(Class c)
    {
    	 URL jarurl = c.getProtectionDomain().getCodeSource().getLocation();
         return new File(jarurl.getFile());
    }
    
    /**
     * Retrieves the jar or root folder in use the classloader for the given 
     * class. This works by using the classloaders protection domain. A 
     * classloader exists for either either /bin type folder or a jar usually
     * 
     * @param c
     * @return
     */
    public static File getCodeSource(Class c)
    {
        URL jarurl = c.getProtectionDomain().getCodeSource().getLocation();
        return new File(jarurl.getFile());
    }
    
    /**
     * Function will attempt to open a connection to the specified host and port
     * with a 2 second timeout. Returns if the connection is successful. This is
     * useful when waiting for a server to come back online after a reboot, or a
     * vmware image boot up sequence.
     * 
     * @param ip
     * @param port
     * @return
     */
    public static boolean isPortReady(String ip, int port)
    {
        Socket s = new Socket();
        try {
            SocketAddress endpoint = new InetSocketAddress(ip, port);
            s.connect(endpoint, 2000);
            return true;
        }
        catch (Exception e) {
            return false;
        }
        finally {
            try {
                s.close();
            }
            catch (Exception e) {
            }
        }
    }
    
    
    /**
     * A method to ping remote IP and return reachable or not
     * 
     * @param ip
     * @param timeout in unit millisecond
     * @return
     */
    public static boolean isReachable(String ip, int timeout)
    {
    	try {
			InetAddress adr = InetAddress.getByName(ip);
			return adr.isReachable(timeout);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
    }
}
