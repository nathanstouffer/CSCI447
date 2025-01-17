/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neuralnets;

import neuralnets.layer.Vector;
import neuralnets.layer.Layer;
import datastorage.*;

/**
 *
 * @author natha
 */
public interface INeuralNet {
    
    public void train(Set training_set);
    public double[] test (Set testing_set);
    public double predict(Example ex);
    public Vector[] genLayerOutputs(Example ex);
    public Vector[] genLayerDeriv();
    public Layer getLayer(int index);
    public int[][] getLayerDim();
    
}
