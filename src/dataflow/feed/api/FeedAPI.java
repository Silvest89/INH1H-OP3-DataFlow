/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataflow.feed.api;

/**
 * Interface which can be implemented if new feed types arrive
 * @author Johnnie Ho
 */
abstract class FeedAPI {
    abstract void connect();    
    abstract void fetchFeed();    
}
