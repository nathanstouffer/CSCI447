/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poptrain;

import datastorage.Set;
import neuralnets.MLP;

/**
 *
 * @author natha
 */
public interface IPopTrain {
    
    public void train(Set training);
    public MLP getBest();
    
}
