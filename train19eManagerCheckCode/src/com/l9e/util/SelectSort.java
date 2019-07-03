package com.l9e.util;

public class SelectSort {
	private int nElems;
	private char[] selectArr;
	
	private String result;
	
	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public SelectSort(int size){
		nElems = 0;
		selectArr = new char[size];
	}
	
	public void insert(char value){
		selectArr[nElems++] = value;
	}
	
	public void display(){
		StringBuffer sb = new StringBuffer();
		for(int i=0; i<nElems; i++){
			sb.append(selectArr[i]+"");
			System.out.println(selectArr[i]);
		}
		result = sb.toString();
	}
	
	public void selectSort(){
		int inner, outer;
		for(outer=0; outer<nElems-1; outer++){
			int min = outer;
			for(inner = outer; inner<nElems;inner++){
				if(selectArr[inner]<selectArr[min]){
					min=inner;
				}
			}
			swap(outer,min);
		}
	}
	
	public void swap(int in, int out){
		char temp;
		temp = selectArr[in];
		selectArr[in] = selectArr[out];
		selectArr[out] = temp;
	}
}
