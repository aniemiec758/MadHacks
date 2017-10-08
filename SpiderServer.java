/**
 * @(#)SpiderServer.java
 *
 *
 * @author Amthony Niemiec
 * @version 1.00 2017/10/8
 */
import java.net.*;
import java.io.*;

public class SpiderServer {
	public static void main(String[] args) throws IOException {
		int androidPort = 4321;	// port number for the Android socket
		int flashPort = 4322;	// port number for the Flash game
		
		try (
			ServerSocket androidSocket = new ServerSocket(androidPort); // creates Android socket
			Socket androidClient = androidSocket.accept(); // Android client, accepts the socket
			ServerSocket flashSocket = new ServerSocket(flashPort); // creates Flash game socket
			Socket flashClient = flashSocket.accept(); // Flash client, accepts the socket
			PrintWriter out = new PrintWriter(flashClient.getOutputStream(), true); // writes output to the Flash game
			BufferedReader input = new BufferedReader(new InputStreamReader(androidClient.getInputStream())); // reads input from the Android app
		) {
			String rotInput,		// rotation of the bow
				numArrowInput;	// number of arrows to be shot at once
			/**/ // command to fire arrows via "fire();"
			int rotOutput,		// to be given to Flash
				numArrowOutput;
			
			while ((rotInput = input.readLine()) != null) { // rotation of bow is being read from the BufferedReader based on androidSocket
				rotOutput = Integer.parseInt(rotInput);
				/**/ // something that gives this to Flash game
				System.out.print("Input read: " + rotOutput + ".\n"); // **DEBUG TESTING**
			}
		} catch (IOException e) {
			System.out.println(e.getMessage()); // too bad
		}
	}    
    
}