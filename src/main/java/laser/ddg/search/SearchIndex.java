package laser.ddg.search;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;

/**
 * Manages lists of nodes by type to facilitate searching.
 *
 * @author Barbara Lerner
 * @version Sep 2, 2015
 */
public class SearchIndex {
    //2D Array to hold information on each type of node for search within Current DDG
    private ArrayList<SearchElement> errorList = new ArrayList<>();
    private ArrayList<SearchElement> dataList = new ArrayList<>();
    private ArrayList<SearchElement> fileList = new ArrayList<>();
    private ArrayList<SearchElement> urlList = new ArrayList<>();
    private ArrayList<OperationSearchElement> operationList = new ArrayList<>();
    private ArrayList<SearchElement> allList = new ArrayList<>();

    /**
     * Adds a node to the appropriate search index based on the node's type.
     * All nodes go into a general list as well.
     *
     * @param type the type of node:  one of "Exception", "Data", "File", "URL", or "Operation"
     * @param id   the node id used by Prefuse
     * @param name the node's label
     * @param time
     */
    public void addToSearchIndex(String type, int id, String name, String time) throws ParseException {
        SearchElement element;

        if (type.equals("Operation")) {
            NumberFormat nf = NumberFormat.getInstance();

            double parsedTime = nf.parse(time).doubleValue();
            OperationSearchElement opElement = new OperationSearchElement(type, name, id, parsedTime);
            operationList.add(opElement);
            element = opElement;

        } else {
            // hold individual node information
            element = new SearchElement(type, name, id);

            // store each node with associated type
            if (type.equals("Exception"))
                errorList.add(element);
            else if (type.equals("Data") || type.equals("Snapshot"))
                dataList.add(element);
            else if (type.equals("File"))
                fileList.add(element);
            else if (type.equals("URL"))
                urlList.add(element);
        }

        // keep track of all nodes in DDG
        allList.add(element);
    }

    /**
     * @return associated error nodes in the search list
     */
    public ArrayList<SearchElement> getErrorList() {
        return errorList;
    }

    /**
     * @return associated data nodes in the search list
     */
    public ArrayList<SearchElement> getDataList() {
        return dataList;
    }

    /**
     * @return associated URL nodes in the search list
     */
    public ArrayList<SearchElement> getURLList() {
        return urlList;
    }

    /**
     * @return associated file nodes in the search list
     */
    public ArrayList<SearchElement> getFileList() {
        return fileList;
    }

    /**
     * @return associated operation nodes in the search list
     */
    public ArrayList<OperationSearchElement> getOperationList() {
        return operationList;
    }

    /**
     * @return all nodes in the search list
     */
    public ArrayList<SearchElement> getAllList() {
        return allList;
    }

}
