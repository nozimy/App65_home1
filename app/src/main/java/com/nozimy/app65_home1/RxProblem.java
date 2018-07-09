package com.nozimy.app65_home1;

import io.reactivex.Observable;

public class RxProblem {

    Observable<Integer> getData(){
        Integer[] nums = {0, 1, 2, 1, 1, 3, 0};
        return Observable.fromArray(nums);
    }

    public void main(String[] args){

        getData()
                .scan(new Integer[]{}, (accumulator, currentValue)-> new Integer[]{accumulator[1], currentValue})
                .filter(array -> array[0]!=null && array[0] < array[1])
                .map(array -> array[1])
                .subscribe(System.out::println);
    }

}
