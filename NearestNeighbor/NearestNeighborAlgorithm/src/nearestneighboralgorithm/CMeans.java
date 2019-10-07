/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nearestneighboralgorithm;

import java.util.ArrayList; 
import java.util.Random;
import java.lang.Double;
/**
 *
 * @author erick
 */
public class CMeans implements IDataReducer{

    Random rd = new Random();
    ArrayList<Cluster> clusterList;
    ArrayList<Example> oldcenters;
    int c;

    private IDistMetric metric;
    private KNNClassifier learner;
    private Set original;    
    
    CMeans(int k, IDistMetric metric, Set input){
        this.c = k;
        this.metric = metric;
        this.learner = new KNNClassifier();
        this.learner.setK(k);
        this.learner.setDistMetric(metric);
        this.original = input;
        this.oldcenters = new ArrayList<Example>();
    }
   
   public Set reduce(Set original){
       int maxIter = 1000;
       int i = 0;
       initializeClusters();
       populateClusters();
       boolean different = true;
       while (i <= maxIter && different){
           for (Cluster cluster : clusterList){
               oldcenters.add(cluster.getRepresentative());
           }
           updateCenters();
           populateClusters();
           maxIter++;
           for (int j = 0; j < clusterList.size(); j++){
               if (clusterList.get(j).getRepresentative() == oldcenters.get(j)){
                   different = false;
               }
           }
       }
       Set reduced = new Set(original.getNumAttributes(), original.getNumClasses(), original.getClassNames());
       for (Cluster cluster : clusterList){
           reduced.addExample(cluster.getRepresentative());
       }
       return(reduced);
   }
   
   public void initializeClusters(){
       for (int i = 0; i < c; i++){
           Example center = original.getExample(rd.nextInt(original.getNumExamples()));
           Cluster cluster = new Cluster(center, metric, original.getNumAttributes(), original.getNumClasses(), original.getClassNames());
           clusterList.add(cluster);
       }
   }
   
   public void populateClusters(){
       for (Cluster cluster : clusterList){
           cluster.clearCluster();
       }
       for(Example ex : original){
           double min = Double.MAX_VALUE;
           Cluster closestCluster = clusterList.get(0);
           for (Cluster cluster : clusterList){
               double dist = metric.dist(ex, cluster.getRepresentative());
               if (dist < min){
                   min = dist;
                   closestCluster = cluster;
               }
           }
           closestCluster.clusterAdd(ex);
       }
   }
   
   public void updateCenters(){
       for (Cluster cluster : clusterList){
           Example center = cluster.getRepresentative();
           ArrayList<Double> newAttributes = new ArrayList<Double>(center.getAttributes().size());
           for (int i = 0; i < center.getAttributes().size(); i++){
               for (Example ex : original){
                   ArrayList<Double> exAttr = ex.getAttributes();
                   double currentsum = newAttributes.get(i) + exAttr.get(i);
                   newAttributes.set(i, currentsum);
               }
               double average = newAttributes.get(i)/original.getNumExamples();
               newAttributes.set(i, average);
           }
           Example newCenter = new Example(newAttributes);
           cluster.representativeChange(newCenter);
       }
    }
}

