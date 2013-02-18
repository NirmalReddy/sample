import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.*;

import difflib.Delta;
import difflib.Patch;

public class DiffServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int index = 0;

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		Difflib diff = new Difflib();
		List<String> original, revised;
		Patch patch;

		response.setContentType("text/html");
		response.setHeader("Access-Control-Allow-Origin", "*");
		PrintWriter out = response.getWriter();
		String file1 = request.getParameter("input1");
		String file2 = request.getParameter("input2");

		original = inputManip(file1);
		revised = inputManip(file2);

		patch = diff.recieveFiles(original, revised);

		outputManip(patch, out, original, revised);
	}

	/**
	 * Converting JSON String to String Array
	 */
	public List<String> inputManip(String file1) {
		List<String> lines = new LinkedList<String>();
		try {
			String linebuffer1 = null;
			JSONArray array1 = new JSONArray(file1);
			for (int i = 0; i < array1.length(); i++) {
				linebuffer1 = array1.get(i).toString();
				linebuffer1 = linebuffer1.trim().replaceAll("( )+", " ");
				if (!linebuffer1.isEmpty()) {
					lines.add(linebuffer1);
				}
			}
		} catch (JSONException j) {
			throw new RuntimeException(j);
		}
		return lines;
	}

	/**
	 * Manipulating the delta output
	 */
	public void outputManip(Patch patch, PrintWriter out,
			List<String> original, List<String> revised) {

		for (Delta delta : patch.getDeltas()) {
			decideDelta(delta, out, original, revised);
			// out.println("1"+delta);
		}
	}

	public void decideDelta(Delta delta, PrintWriter out,
			List<String> original, List<String> revised) {

		if (delta.getOriginal().size() == 0) {
			addDelta(delta, out, original, revised);
		} else if (delta.getRevised().size() == 0) {
			deleteDelta(delta, out, original, revised);
		} else {
			changeDelta(delta, out, original, revised);
		}
	}

	public void changeDelta(Delta delta, PrintWriter out,
			List<String> original, List<String> revised) {
		printCFormat(delta, out);
		if ((delta.getOriginal().getPosition() - 3) > 0) {
			for (int i = delta.getOriginal().getPosition() - 3; i < delta
					.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
		} else {
			for (int i = 0; i < delta.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
		}
		for (int i = 0; i < delta.getOriginal().getLines().size(); i++) {
			out.println("<font color=\"red\"> < "
					+ delta.getOriginal().getLines().get(i) + " </font> </br>");
		}
		out.println("-------</br>");
		for (int i = 0; i < delta.getRevised().getLines().size(); i++) {
			out.println("<font color=\"green\"> > "
					+ delta.getRevised().getLines().get(i) + "</font></br>");
		}
		if ((delta.getRevised().getPosition()
				+ delta.getRevised().getLines().size() + 3) < revised.size()) {
			for (int i = delta.getRevised().getPosition()
					+ delta.getRevised().getLines().size(); i <= delta
					.getRevised().getPosition()
					+ delta.getRevised().getLines().size() + 2; i++) {
				out.print(revised.get(i) + "</br>");
			}
		}
		out.println("</br>");
	}

	public void printCFormat(Delta delta, PrintWriter out) {
		if (delta.getOriginal().size() != 1 && delta.getRevised().size() != 1) {
			out.print("<font color=\"blue\">"
					+ (delta.getOriginal().getPosition() + 1)
					+ ","
					+ (delta.getOriginal().getPosition() + delta.getOriginal()
							.size()));
			out.print("c");
			out.print(""
					+ (delta.getRevised().getPosition() + 1)
					+ ","
					+ (delta.getRevised().getPosition() + delta.getRevised()
							.size()) + " </font></br>");
		} else if (delta.getOriginal().size() == 1
				&& delta.getRevised().size() == 1) {
			out.print("<font color=\"blue\">"
					+ (delta.getOriginal().getPosition() + 1));
			out.print("c");
			out.print("" + (delta.getRevised().getPosition() + 1)
					+ " </font></br>");
		} else if (delta.getOriginal().size() != 1
				&& delta.getRevised().size() == 1) {
			out.print("<font color=\"blue\">"
					+ (delta.getOriginal().getPosition() + 1)
					+ ","
					+ (delta.getOriginal().getPosition() + delta.getOriginal()
							.size()));
			out.print("c");
			out.print("" + (delta.getRevised().getPosition() + 1)
					+ " </font></br>");
		} else {
			out.print("<font color=\"blue\">"
					+ (delta.getOriginal().getPosition() + 1));
			out.print("c");
			out.print(""
					+ (delta.getRevised().getPosition() + 1)
					+ ","
					+ (delta.getRevised().getPosition() + delta.getRevised()
							.size()) + " </font></br>");
		}
	}

	public void addDelta(Delta delta, PrintWriter out, List<String> original,
			List<String> revised) {
		out.print("<font color=\"blue\">" + (delta.getOriginal().getPosition()));
		out.print("a");
		out.print(""
				+ (delta.getRevised().getPosition() + 1)
				+ ","
				+ (delta.getRevised().getPosition() + delta.getRevised().size())
				+ " </font></br>");
		if ((delta.getOriginal().getPosition() - 3) > 0) {
			for (int i = delta.getOriginal().getPosition() - 3; i < delta
					.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
		} else {
			for (int i = 0; i < delta.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
		}
		for (int i = 0; i < delta.getRevised().getLines().size(); i++) {
			// out.println("> "+delta.getRevised().getLines().get(i) + "</br>");
			out.println("<font color=\"green\"> > "
					+ delta.getRevised().getLines().get(i) + "</font></br>");
		}
		if ((delta.getRevised().getPosition()
				+ delta.getRevised().getLines().size() + 3) < revised.size()) {
			for (int i = delta.getRevised().getPosition()
					+ delta.getRevised().getLines().size() + 1; i <= delta
					.getRevised().getPosition()
					+ delta.getRevised().getLines().size() + 3; i++) {
				out.print(revised.get(i) + "</br>");
			}
		}
		out.println("</br>");
	}

	public void deleteDelta(Delta delta, PrintWriter out,
			List<String> original, List<String> revised) {
		out.print("<font color=\"blue\">"
				+ (delta.getOriginal().getPosition() + 1)
				+ ","
				+ (delta.getOriginal().getPosition() + delta.getOriginal()
						.size()));
		out.print("d");
		out.print("" + (delta.getRevised().getPosition()) + " </font></br>");

		printBeforeContext(delta, out, original, revised);
		for (int i = 0; i < delta.getOriginal().getLines().size(); i++) {
			// out.println("- "+delta.getOriginal().getLines().get(i) +
			// "</br>");
			out.println("<font color=\"red\"> - "
					+ delta.getOriginal().getLines().get(i) + " </font> </br>");
		}
		if ((delta.getOriginal().getPosition()
				+ delta.getOriginal().getLines().size() + 3) < revised.size()) {
			for (int i = delta.getOriginal().getPosition()
					+ delta.getOriginal().getLines().size() + 1; i <= delta
					.getOriginal().getPosition()
					+ delta.getOriginal().getLines().size() + 3; i++) {
				out.print(revised.get(i) + "</br>");
			}
		}
		out.println("</br>");
	}

	public void printBeforeContext(Delta delta, PrintWriter out,
			List<String> original, List<String> revised) {

		int expand_from = 0;
		index++;
		if ((delta.getOriginal().getPosition() - 3) > 0) {
			expand_from = delta.getOriginal().getPosition() - 3;
			out.print("<a id='div" + index
					+ "' onclick = \"javascript:blocking('next_div" + index
					+ "');changeText(id)\">-</a>" + revised.get(expand_from)
					+ "<strong><br>");

			out.print("<div id='next_div" + index + "'>");
			for (int i = delta.getOriginal().getPosition() - 2; i < delta
					.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
			out.print("</div>");
		} else {
			expand_from = 0;
			out.print("<a id='div" + index
					+ "' onclick = \"javascript:blocking('next_div" + index
					+ "');changeText(id)\">-</a>" + revised.get(expand_from)
					+ "<strong><br>");

			out.print("<div id='next_div" + index + "'>");
			for (int i = 1; i < delta.getOriginal().getPosition(); i++) {
				out.print(original.get(i) + "</br>");
			}
			out.print("</div>");
		}
	}
}