package laser.ddg.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import laser.ddg.ScriptNode;
import laser.ddg.r.RDataInstanceNode;
import laser.ddg.visualizer.WorkflowGraphBuilder;

public class Workflow {

	private ArrayList<WorkflowEdge> edges;
	private Map<Integer, RDataInstanceNode> fileNodes;
	private Map<Integer, ScriptNode> scriptNodes;
	private ArrayList<RDataInstanceNode> addedFiles;
	private ArrayList<ScriptNode> addedScripts;

	public Workflow(WorkflowGraphBuilder builder) {
		edges = new ArrayList<WorkflowEdge>();
		fileNodes = new HashMap<Integer, RDataInstanceNode>();
		scriptNodes = new HashMap<Integer, ScriptNode>();
		addedFiles = new ArrayList<RDataInstanceNode>();
		addedScripts = new ArrayList<ScriptNode>();
	}

	public void addFile(RDataInstanceNode rdin) {
		fileNodes.put(rdin.getId(), rdin);
	}

	public void addScript(ScriptNode sn) {
		scriptNodes.put(sn.getId(), sn);
	}

	public void addEdge(String type, int source, int target) {
		WorkflowEdge we = new WorkflowEdge(type, source, target);
		edges.add(we);
	}

	public void assemble(WorkflowGraphBuilder builder, int index) {
		ScriptNode sn = scriptNodes.get(index);
		RDataInstanceNode rdin = fileNodes.get(index);
		if (sn != null) {
			if (addedScripts.contains(sn)) {
				builder.addNode(sn, index);
			}
			addedScripts.add(sn);
			// Recursive call
			// Add edge
		} else if (rdin != null) {
			if (addedScripts.contains(sn)) {
				builder.addNode(rdin.getType(), index, rdin.getName(), rdin.getValue(),
						rdin.getCreatedTime(), rdin.getLocation(), null);
			}
			addedFiles.add(rdin);
			// Recursive call
			// Add edge
		}
		return;

	}
}