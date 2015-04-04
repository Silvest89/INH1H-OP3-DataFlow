/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow;

import dataflow.screens.GraphsController;

/**
 *
 * @author Anton
 */
public class test {
      public static void main(String[] args) {
    System.out.println("neu" +  GraphsController.neutralCommentList.size());
    System.out.println("posi" + GraphsController.positiveCommentList.size());
    System.out.println("nega" + GraphsController.negativeCommentList.size());
    
}
}

