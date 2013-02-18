/*
 * 
 * This file is part of the sample code for the article,
 * "What is Jetty,"
 * http://www.onjava.com/pub/a/onjava/2006/06/14/what-is-jetty.html
 * by Ethan McCallum
 * 
 * This code was tested against Jetty 5.1.10 and Sun's JDK 1.5.0_03.
 * 
 */

/*import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;*/

import org.mortbay.jetty.Server;
import org.mortbay.jetty.servlet.ServletHttpContext;

public class Step1Driver {

	private static final String LISTEN_ADDY = "localhost:8080";
	private static final String CONTEXT_PATH = "/embed";

	private static final String SERVLET_CLASS = "DiffServlet";
	private static final String SERVLET_PATH = "/TryThis";
	private static final String SERVLET_MAPPING = SERVLET_PATH + "/*";

	public static void main(final String[] args) {

		try {
			final Server service = new Server();

			// configure container to listen on a given socket for requests
			service.addListener(LISTEN_ADDY);

			// define a web application at a given context path
			ServletHttpContext ctx = (ServletHttpContext) service
					.getContext(CONTEXT_PATH);

			// map a servlet class to a URI
			ctx.addServlet("Simple", SERVLET_MAPPING, SERVLET_CLASS);

			service.start();
			System.out.println("Server started");

			service.join();

			//service.stop();

		} catch (Throwable t) {
			t.printStackTrace();
			System.exit(1);
		}

	} 
} 