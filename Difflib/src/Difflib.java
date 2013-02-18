import difflib.*;

import java.io.*;
import java.util.*;

public class Difflib {
	// Helper method for get the file content
	private static List<String> fileToLines(String filename) {
		List<String> lines = new LinkedList<String>();
		String line = "";
		try {
			BufferedReader in = new BufferedReader(new FileReader(filename));
			while ((line = in.readLine()) != null) {
				lines.add(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return lines;
	}

	public Patch recieveFiles(List<String> original, List<String> revised) {

		Patch patch = DiffUtils.diff(original, revised);
		for (Delta delta : patch.getDeltas()) {
			System.out.println(delta);
		}
		return patch;

	}

	public static void main(String[] args) {
		List<String> original = fileToLines("C:/Users/nirmal/workspace/FileDiff/src/Sample1.java");
		List<String> revised = fileToLines("C:/Users/nirmal/workspace/FileDiff/src/Sample2.java");

		// Compute diff. Get the Patch object. Patch is the container for
		// computed deltas.
		Patch patch = DiffUtils.diff(original, revised);
		
		for (int i = 0; i < original.size(); i++) {

			// System.out.println(delta.getOriginal()+""+delta.getRevised());
			System.out.println(original.get(i));
		}
		
		for (Delta delta : patch.getDeltas()) {

			//System.out.println(delta.getOriginal() + "" + delta.getRevised());
			System.out.println(delta);
		}
	}
}