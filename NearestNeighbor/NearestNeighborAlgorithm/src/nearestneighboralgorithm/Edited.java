/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nearestneighboralgorithm;

import java.util.ArrayList;
import java.util.Random;

/**
 * Class to reduce the size of a data set for the use of 
 * a Nearest Neighbor algorithm.
 * 
 * The Edited Nearest Neighbor (E-NN) algorithm edits removes examples
 * from the data set that are misclassified by their k-nearest
 * neighbors. This process is iteratively repeated until performance
 * on a validation set decreases.
 * 
 * @author natha
 */
public class Edited implements IDataReducer {
    
    // batch size for editing
    private final int BATCH_SIZE = 100;
    // variable to determine how many neighbors the learning
    // algorithm will view
    private int k;
    // metric that the reduciing algorithm will use
    private IDistMetric metric;
    // learner used for reducing purposes
    // learner must be of type KNNClassifier since Edited 
    // Nearest Neighbor is limited to classification problems
    private KNNClassifier learner;
    // set that exists for validation purposes to test
    // whether E-NN algorithm should be terminated
    private Set validation_set;    
    
    /**
     * constructor to initialize global variables
     * @param k
     * @param metric
     * @param validation_set 
     */
    Edited(int k, IDistMetric metric, Set validation_set){
        this.k = k;
        this.metric = metric;
        this.learner = new KNNClassifier();
        this.learner.setK(k);
        this.learner.setDistMetric(metric);
        
        this.validation_set = validation_set;
    }
    
    /**
     * method to reduce the size of a set for the use of k-nearest
     * neighbors as long as performance does not degrade.
     * This will be determined using
     * the accuracy metric since Edited Nearest Neighbors is
     * used only on classification problems
     * @param orig
     * @return 
     */
    public Set reduce(Set orig){
        // clone orig
        Set excessive = orig.clone();
        // declare the edited set
        Set edited;
        
        System.out.println("ORIG SIZE: " + excessive.getNumExamples());
        
        this.learner.train(excessive);        
        double orig_acc = computeAccuracy();
        
        boolean edit = true;
        int iterations = 0;
        do {
            iterations++;
            // train learner with excessive
            this.learner.train(excessive);
            // compute accuracy for excessive learner (uses validation_set)
            // double excessive_acc = computeAccuracy();
            
            // get misclassifed examples in the data set
            ArrayList<Example> misclassified = findMisclassified(excessive);
            
            // initialize edited set
            edited = excessive.clone();
            // delete missclassified examples from edited
            for (Example ex: misclassified){ edited.delExample(ex); }
            
            // train learner with edited data set
            this.learner.train(edited);
            // compute accuracy for edited learner (uses validation_set)
            double edited_acc = computeAccuracy();
            
            // stop editing set when performance degrades
            if (iterations == 10){//edited_acc < orig_acc) {//excessive_acc){
                edit = false;
                // we have edited too far, and must revert one iteration
                edited = excessive;
            }
            // otherwise, run another iteration of the editing process
            else{ excessive = edited; }
        } while (edit);
        
        System.out.println("EDITED ITERATIONS: " + iterations);
        
        return edited;
    }
    
    /**
     * method to compute the accuracy of a learner using the
     * global variable validation_set
     * @param learner
     * @return 
     */
    private double computeAccuracy(){
        double[] pred = this.learner.test(this.validation_set);
        
        // create evaluator object
        ClassificationEvaluator eval = new ClassificationEvaluator(pred, this.validation_set);
        // compute accuracy
        double acc = eval.getAccuracy();
        
        // return accuracy
        return acc;
    }
    
    /**
     * method to find the misclassified examples using k-NN
     * @param clone
     * @return 
     */
    private ArrayList<Example> findMisclassified(Set orig){
        // clone clone
        Set clone = orig.clone();
        // ArrayList for misclassified data points
        ArrayList<Example> misclassified = new ArrayList<Example>();
        // array for a batch that we will edit
        // the size of batch is BATCH_SIZE
        // Example[] batch = new Example[this.BATCH_SIZE];
        
        // populate batch with random Examples from clone
        /*Random rnd = new Random();
        for (int i = 0; i < this.BATCH_SIZE; i++){
            // get random value in to index clone
            int rand_index = rnd.nextInt(orig.getNumExamples());
            
            // get the example at rand_value in clone
            Example ex = orig.getExample(rand_index);
            // remove this example from clone (so it cannot be selected again)
            orig.delExample(ex);
            
            // insert ex into batch
            batch[i] = ex;
        }*/
        
        // reinsert values of batch into clone
        //for (int i = 0; i < batch.length; i++){ orig.addExample(batch[i]); }
        
        // using learner, classify all points in clone
        // for (Example ex: clone){
        for (int i = 0; i < clone.getNumExamples(); i++){
            // the ith example
            Example ex = clone.getExample(i);
            // actual classification
            double actual = ex.getValue();
            
            // this removes ex from the training set
            clone.delExample(ex);
            // find predicted classification
            double pred = learner.classify(ex);
            
            // add ex back into training set
            clone.addExample(i, ex);

            // if incorrect classification, add ex to misclassified ArrayList
            if (pred != actual){ misclassified.add(ex); }
        }
        
        return misclassified;
    }
    
}
