/**
  * Copyright 2017 aTool.org 
  */
package com.roy.douproject.bean.movie;
import java.util.List;

/**
 * Auto-generated: 2017-02-15 10:36:15
 *
 * @author aTool.org (i@aTool.org)
 * @website http://www.atool.org/json2javabean.php
 */
public class JsonMovieBean {

    private int count;
    private int start;
    private int total;
    private List<Subjects> subjects;
    private String title;
    public void setCount(int count) {
         this.count = count;
     }
     public int getCount() {
         return count;
     }

    public void setStart(int start) {
         this.start = start;
     }
     public int getStart() {
         return start;
     }

    public void setTotal(int total) {
         this.total = total;
     }
     public int getTotal() {
         return total;
     }

    public void setSubjects(List<Subjects> subjects) {
         this.subjects = subjects;
     }
     public List<Subjects> getSubjects() {
         return subjects;
     }

    public void setTitle(String title) {
         this.title = title;
     }
     public String getTitle() {
         return title;
     }

}