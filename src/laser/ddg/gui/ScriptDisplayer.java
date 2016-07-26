package laser.ddg.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;

import laser.ddg.visualizer.PrefuseGraphBuilder;
import laser.ddg.visualizer.TextLineNumber;

/**
 * This class maintains the information needed to show source
 * code and highlight specific lines of the source.
 */
public class ScriptDisplayer {
	// The contents of the file to display
	private String fileContents;
	
	// The character position where each line starts.  Needed to do the highlighting.
	private ArrayList<Integer> lineStarts = new ArrayList<>();
	
	// The frame that is displaying this file
	private JFrame fileFrame;
	
	// The text area that is displaying this file
	private JTextArea fileTextArea;
	
	// The objects needed to highlight specific parts of the file.
	private Highlighter fileHighlighter;
	private HighlightPainter fileHighlightPainter;

	/**
	 * Create the display for a script
	 * @param scriptNum the number of the script as referenced in the ddg
	 */
	public ScriptDisplayer(PrefuseGraphBuilder builder, int scriptNum) {
		String fileName = builder.getScriptPath(scriptNum);
		if (fileName == null) {
			JOptionPane.showMessageDialog(DDGExplorer.getInstance(),
					"There is no script available for " + builder.getProcessName());
			return;
		}
		File theFile = new File(fileName);
		if (!theFile.exists()) {
			JOptionPane.showMessageDialog(DDGExplorer.getInstance(),
					"There is no script available for " + builder.getProcessName());
			return;
		}

		// System.out.println("Reading script from " + fileName);

		try {
			readFile(theFile);
			displayFileContents();
		} catch (FileNotFoundException e) {
			DDGExplorer.showErrMsg("There is no script available for " + fileName + "\n\n");
		}
	}

	private void readFile(File theFile) throws FileNotFoundException {
		StringBuilder contentsBuilder = new StringBuilder();
		Scanner readFile = null;
		try {
			readFile = new Scanner(theFile);
			lineStarts = new ArrayList<>();
			// System.out.println("\n" + str);
			// Read the file one line at a time and remember where each line starts.
			while (readFile.hasNextLine()) {
				String line = readFile.nextLine();
				lineStarts.add(contentsBuilder.length());
				contentsBuilder.append(line + "\n");
			}
			fileContents = contentsBuilder.toString();
		} finally {
			if (readFile != null) {
				readFile.close();
			}
		}
	}

	private void displayFileContents() {
		fileFrame = new JFrame();
		fileTextArea = new JTextArea();
		fileTextArea.setText(fileContents);
		fileTextArea.setEditable(false);
		JScrollPane scroller = new JScrollPane(fileTextArea);
		fileFrame.add(scroller, BorderLayout.CENTER);
		fileFrame.setSize(600, 800);
		fileHighlighter = fileTextArea.getHighlighter();
		fileHighlightPainter = new DefaultHighlighter.DefaultHighlightPainter(Color.YELLOW);

		TextLineNumber tln = new TextLineNumber(fileTextArea);
		scroller.setRowHeaderView(tln);
	}

	public void highlight(int firstLine, int lastLine) {
		try {
			fileFrame.setVisible(true);
			fileTextArea.setCaretPosition(lineStarts.get(firstLine - 1));
			fileHighlighter.removeAllHighlights();
			if (lastLine < lineStarts.size()) {
				fileHighlighter.addHighlight(lineStarts.get(firstLine - 1), lineStarts.get(lastLine),
						fileHighlightPainter);
			} else {
				fileHighlighter.addHighlight(lineStarts.get(firstLine - 1), fileContents.length() - 1,
						fileHighlightPainter);
			}
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace(System.err);
		}

	}

}