/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package naivebayesalgorithm;

// import libraries
import java.util.ArrayList;

/**
 * Class that represents one example in a dataset
 * Each example has a class type, a subset that it belongs to,
 * and a list of attributes
 * @author natha
 */
public class Example {
    
    // global variable to store the type of class an example is in
    private final int class_type;
    // global variable to store which subset an example belongs to
    private final int subset_index;
    // global array to store values of each attribute for the example
    private ArrayList<Integer> attr;
    
    /**
     * Constructor to populate global variables
     * @param input
     * @param num_attr 
     */
    Example(String line, int num_attr){
        // initialize size of attr array
        this.attr = new ArrayList<Integer>(num_attr);

        // split the input line into a String array
        String[] data = line.split(",");
        
        // populate class type and subset_index;
        this.class_type = Integer.parseInt(data[0]);
        this.subset_index = Integer.parseInt(data[1]);
        
        // populate attr ArrayList with attribute values
        for (int i = 0; i < num_attr; i++){ this.attr.add(Integer.parseInt(data[i+2])); }
    }
    
    // getter methods
    public int getClassType(){ return this.class_type; }
    public int getSubsetIndex(){ return this.subset_index; }
    public ArrayList<Integer> getAttributes(){ return this.attr; }
    
}
