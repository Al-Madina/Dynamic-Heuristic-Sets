/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 *
 * @author User
 */
public class Utility {
        
    public double diff(double currentValue, double newValue){
        return (currentValue - newValue);
    } 
    
    public double percDiff(double currentValue, double newValue){
        return (currentValue - newValue)/currentValue;
    }    
    
    // For performance monitoring
    public double[] calcAbsolutePerf(double[] arr1, double[] arr2){
        int numHeurs = arr1.length;
        double[] results = new double[numHeurs];
        for(int idx=0; idx < numHeurs; idx++){
            if(arr1[idx] + arr2[idx] == 0) continue;
            results[idx] = 100*arr1[idx]/(arr1[idx] + arr2[idx]);
        }
        return results;
    }
    
    //Performance relative to other heuristics
    public double[] calcRelativePerf(double[] arr){

        double total = 0;
        for(double v : arr) total += v;
        
        int numHeurs = arr.length;
        double[] results = new double[numHeurs];        
        if(total == 0) return results;
        
        for(int idx=0; idx < numHeurs; idx++){
            results[idx] = 100*arr[idx]/total;
        }
        return results;
    }    
    
    
    public List<Integer> filter(double[] values, double asp){
        double mean = mean(values);
        List<Integer> removed = new ArrayList<>(values.length);
        for(int idx=0; idx<values.length; idx++){
            if(values[idx] < asp*mean) removed.add(idx);
        }
        return removed;
    }

    public int getIndexOfMinValue(List<Double> values){
        double v = values.get(0);
        int index = 0;
        for(int idx=1; idx < values.size(); idx++){
            if(values.get(idx) < v){
                v = values.get(idx);
                index = idx;
            }
        }
        return index;
    }
    
    
    public int getIndexOfMinValue(List<Double> values, Set<Integer> except){
        double v = Double.MAX_VALUE;
        int index = -1;
        for(int idx=0; idx < values.size(); idx++){
            if(except.contains(idx)) continue;
            if(values.get(idx) < v){
                v = values.get(idx);
                index = idx;
            }
        }
        return index;
    }
    
    
    public int getIndexOfMinValue(double[] values){
        double v = values[0];
        int index = 0;
        for(int idx=1; idx < values.length; idx++){
            if(values[idx] < v){
                v = values[idx];
                index = idx;
            }
        }
        return index;
    }    

    
    public double min(double[] array){
        double min = Double.POSITIVE_INFINITY;
        for(double value : array){
            if(value < min){
                min = value;
            }
        }
        return min;
    }
    
    public double min(List<? extends Number> list){
        double min = Double.POSITIVE_INFINITY;
        for(Number value : list){
            if(value.doubleValue() < min){
                min = value.doubleValue();
            }
        }
        return min;
    }    
       
    
    public double mean(double[] array){
        double mean = 0;
        for(double value : array) mean += value;
        mean = mean/array.length;
        return mean;
    } 
    
    public double mean(List<Double> list){
        double mean = 0;
        for(double value : list) mean += value;
        mean = mean/list.size();
        return mean;
    }     
    
    public double std(List<Double> list){
        double std = 0;
        double mean = mean(list);
        for(double v : list){
            std += (mean - v)*(mean - v);
        }
        std /= list.size();
        return std;
    }
    
    public double median(List<Double> list){
        Collections.sort(list);
        double median = list.get(list.size()/2);
        if(list.size() % 2 == 0){
            median += list.get(list.size()/2 - 1);
            median /= 2;
        }
        return median;        
    }
    
    /**
     * Returns the index of the maximum value in an array list that is not in 
     * a set of tabu indexes. 
     * @param values array list of the values
     * @param except set of tabu indexes
     * @return the index of the maximum value which is not in <code>except</code>
     */
    public int getIndexOfMaxValue(List<Double> values, Set<Integer> except){
        double v = Double.NEGATIVE_INFINITY;
        int index = -1;
        for(int idx=0; idx < values.size(); idx++){
            if(except.contains(idx)) continue;
            if(values.get(idx) > v){
                v = values.get(idx);
                index = idx;
            }
        }
        return index;
    }
    
    public double max(double[] array){
        double max = Double.NEGATIVE_INFINITY;
        for(double value : array){
            if(value > max){
                max = value;
            }
        }
        return max;
    }    
    
    public double max(List<Double> list){
        double max = Double.NEGATIVE_INFINITY;
        for(double value : list){
            if(value > max){
                max = value;
            }
        }
        return max;
    }     
    
    
    /**
     * Compute the mean of heuristic performance.
     * Return
     * @param perfList a list of arrays where heurPerfList[t] is the array
     * representing the heuristic performance computed by thread t and perfList[t][h]
     * is the heuristic performance for heuristic 'h' computed by thread 't'
     * @return an array of the average performance for all heuristics where the value at index
     * <code>idx</code> is the average performance of the heuristic at index <code>idx</code>
     * in the universal set.
     */
    public double[] meanHeurPerf(List<double[]> perfList){
        int numHeurs = perfList.get(0).length;
        List<List<Double>> heurPerfList = new ArrayList<>(numHeurs);
        for(int i=0; i < numHeurs; i++) heurPerfList.add(new ArrayList<>(heurPerfList.size()));
        for(double[] perfArray : perfList){
            for(int heur=0; heur < perfArray.length; heur++){
                heurPerfList.get(heur).add(perfArray[heur]);
            }
        }
        double[] results = new double[numHeurs];
        for(int heur=0; heur < numHeurs; heur++){
            results[heur] = mean(heurPerfList.get(heur));
        }
        return results;
    }
}
