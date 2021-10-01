package util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * A utility class that provides helper methods.
 * @author Ahmed Hassan (ahmedhassan@aims.ac.za)
 */
public class Utility {
    
    /**
     * Calculates delta between the current and new value.
     * @param currentValue the solution value before applying a heuristic
     * @param newValue the solution value after applying the heuristic
     * @return the delta between the current and new value
     */
    public double diff(double currentValue, double newValue){
        return (currentValue - newValue);
    } 
    
    /**
     * Calculates the percentage delta between the current and new value.
     * @param currentValue the solution value before applying a heuristic
     * @param newValue the solution value after applying a heuristic
     * @return the percentage delta between the current and new value
     */
    public double percDiff(double currentValue, double newValue){
        return (currentValue - newValue)/currentValue;
    }    
    
    /**
     * Calculates the <i>absolute performance</i> of each heuristic measured by
     * the ratio of improvement (disimprovement) to the total <i>change</i> in 
     * the objective function caused by the heuristic where the the change in 
     * the objective function is measured by the sum of  <code>arr1</code> and 
     * <code>arr2</code>.
     * @param arr1 the array that contains either the improvement or disimprovement
     *              of each heuristic where <code>arr1[idx]</code> gives either the 
     *              improvement or disimprovement of the heuristic identified by 
     *              <code>heurList[idx]</code> and <code>heurList</code> is the
     *              array list that contains the integer identifiers for each heuristic
     * @param arr2 Similar to <code>arr1</code>. If <code>arr1</code> represents
     *              the improvement, then <code>arr2</code> will represent the
     *              disimprovement and vice versa.
     * @return the <i>absolute performance</i> of each heuristic
     */
    public double[] calcAbsolutePerf(double[] arr1, double[] arr2){
        int numHeurs = arr1.length;
        double[] results = new double[numHeurs];
        for(int idx=0; idx < numHeurs; idx++){
            if(arr1[idx] + arr2[idx] == 0) continue;
            results[idx] = 100*arr1[idx]/(arr1[idx] + arr2[idx]);
        }
        return results;
    }
    
    /**
     * Calculates the <i>relative performance</i> of a heuristic relative to other
     * heuristics which is measured by how much the heuristic contributes to the 
     * total improvement (disimprovement).
     * @param arr an array containing the improvement or disimprovement made by 
     *              each heuristic.
     * @return the <i>relative performance</i> of each heuristic
     */
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
    
    /**
     * Returns the indexes of the heuristics that perform lower than the average
     * reduced by an aspiration parameter.
     * @param values an array containing the heuristic performance values
     * @param asp an aspiration parameter to not 
     * @return a list of indexes of the heuristics that are filtered out (removed)
     */
    public List<Integer> filter(double[] values, double asp){
        double mean = mean(values);
        List<Integer> removed = new ArrayList<>(values.length);
        for(int idx=0; idx<values.length; idx++){
            if(values[idx] < asp*mean) removed.add(idx);
        }
        return removed;
    }

    /**
     * Returns the index of the minimum value.
     * @param values a list of values
     * @return the index of the minimum value
     */
    public int getIndexOfMinValue(List<? extends Number> values){
        double x = values.get(0).doubleValue();
        int index = 0;
        for(int idx=1; idx < values.size(); idx++){
            if(values.get(idx).doubleValue() < x){
                x = values.get(idx).doubleValue();
                index = idx;
            }
        }
        return index;
    }
    
    /**
     * Returns the index of the minimum value.
     * @param values an array of values
     * @return the index of the minimum value
     */
    public int getIndexOfMinValue(double[] values){
        double x = values[0];
        int index = 0;
        for(int idx=1; idx < values.length; idx++){
            if(values[idx] < x){
                x = values[idx];
                index = idx;
            }
        }
        return index;
    }    
    
    /**
     * Returns the index of the minimum value that is not in <code>except</code>
     * and returns -1 if <code>except</code> contains all of the indexes.
     * @param values a list of values
     * @param except a set of the indexes that should not be considered
     * @return the index of the minimum value that is not in <code>except</code>
     *          and returns -1 if <code>except</code> contains all of the indexes
     */
    public int getIndexOfMinValue(List<? extends Number> values, Set<Integer> except){
        double v = Double.MAX_VALUE;
        int index = -1;
        for(int idx=0; idx < values.size(); idx++){
            if(except.contains(idx)) continue;
            if(values.get(idx).doubleValue() < v){
                v = values.get(idx).doubleValue();
                index = idx;
            }
        }
        return index;
    }
    
    /**
     * Returns the minimum value in an array.
     * @param array values
     * @return the minimum value in an array
     */
    public double min(double[] array){
        double min = Double.POSITIVE_INFINITY;
        for(double value : array){
            if(value < min){
                min = value;
            }
        }
        return min;
    }
    
    /**
     * Returns the minimum value in a list of numbers.
     * @param list values
     * @return the minimum value in a list of numbers
     */
    public double min(List<? extends Number> list){
        double min = Double.POSITIVE_INFINITY;
        for(Number value : list){
            if(value.doubleValue() < min){
                min = value.doubleValue();
            }
        }
        return min;
    }    
    
    /**
     * Returns the maximum value in an array.
     * @param array values
     * @return the maximum value in an array
     */
    public double max(double[] array){
        double max = Double.NEGATIVE_INFINITY;
        for(double value : array){
            if(value > max){
                max = value;
            }
        }
        return max;
    }    
    
    /**
     * Returns the minimum value in a list of numbers.
     * @param list values
     * @return the minimum value in a list of numbers
     */
    public double max(List<? extends Number> list){
        double max = Double.NEGATIVE_INFINITY;
        for(Number value : list){
            if(value.doubleValue() > max){
                max = value.doubleValue();
            }
        }
        return max;
    }
       
    /**
     * Returns the mean of values in an array.
     * @param array values
     * @return the mean of values in an array
     */
    public double mean(double[] array){
        double mean = 0;
        for(double value : array) mean += value;
        mean = mean/array.length;
        return mean;
    } 
    
    /**
     * Returns the mean of values in a list of number.
     * @param list values
     * @return the mean of values in a list of number
     */
    public double mean(List<? extends Number> list){
        double mean = 0;
        for(Number value : list) mean += value.doubleValue();
        mean = mean/list.size();
        return mean;
    }     
    
    /**
     * Returns the standard deviation.
     * @param list values
     * @return the standard deviation
     */
    public double std(List<? extends Number> list){
        double std = 0;
        double mean = mean(list);
        for(Number v : list){
            std += (mean - v.doubleValue())*(mean - v.doubleValue());
        }
        std = Math.sqrt(std/(list.size()-1));
        return std;
    }
    
    /**
     * Returns the median of a list.
     * @param list values
     * @return the median of a list
     */
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
    public int getIndexOfMaxValue(List<? extends Number> values, Set<Integer> except){
        double v = Double.NEGATIVE_INFINITY;
        int index = -1;
        for(int idx=0; idx < values.size(); idx++){
            if(except.contains(idx)) continue;
            if(values.get(idx).doubleValue() > v){
                v = values.get(idx).doubleValue();
                index = idx;
            }
        }
        return index;
    }             
    
    /**
     * Computes the average of heuristic performance across several threads.
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
        for(int i=0; i < numHeurs; i++) heurPerfList.add(new ArrayList<>(perfList.size()));
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
