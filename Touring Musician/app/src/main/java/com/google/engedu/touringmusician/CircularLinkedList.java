/* Copyright 2016 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.engedu.touringmusician;


import android.graphics.Point;

import java.util.Iterator;

public class CircularLinkedList implements Iterable<Point> {

    private class Node {
        Point point;
        Node prev, next;

    }

    Node head=null;
    public Point rethead(){

        return head.point;

    }
    public boolean headnull()
    {
        if(head==null)
            return true;
        else return false;
    }
    public void insertBeginning(Point p) {
       Node nd=new Node();
       nd.point=p;
       if(head==null){
       nd.prev=nd;
       nd.next=nd;
       head=nd;
       }
       else
       {
           nd.next=head;
           nd.prev=head.prev;
           head=nd;
           head.next.prev=head;
           head.prev.next=head;
       }
    }

    private float distanceBetween(Point from, Point to) {
        return (float) Math.sqrt(Math.pow(from.y-to.y, 2) + Math.pow(from.x-to.x, 2));
    }

    public float totalDistance() {
        Node p=head;
        float total = 0;
        while(p.next!=head)
        {
            total=total+distanceBetween(p.point,p.next.point);
            p=p.next;
        }
        total=total+distanceBetween(p.point,p.next.point);
        return total;
    }

    public void insertNearest(Point p) {
       Node newnode=new Node(),it;
       newnode.point=p;

       Node shortest;float shortestdist,dist;
       if(head==null)
       {
           newnode.prev=newnode;
           newnode.next=newnode;
           head=newnode;
       }
       else{
           it=head;
       shortest=it;
       shortestdist=distanceBetween(p,it.point);
       it=it.next;
       while(it!=head)
       {
           dist=distanceBetween(it.point,p);
           if(dist<shortestdist)
           {
               shortest=it;
               shortestdist=dist;
           }
           it=it.next;
       }
           newnode.next=shortest;
           newnode.prev=shortest.prev;
           shortest=newnode;
           shortest.next.prev=shortest;
           shortest.prev.next=shortest;
       }

    }

    public void insertSmallest(Point p) {
        /**
         **
         **  YOUR CODE GOES HERE
         **
         **/
    }

    public void reset() {
        head = null;
    }

    private class CircularLinkedListIterator implements Iterator<Point> {

        Node current;

        public CircularLinkedListIterator() {
            current = head;
        }

        @Override
        public boolean hasNext() {
            return (current != null);
        }

        @Override
        public Point next() {
            Point toReturn = current.point;
            current = current.next;
            if (current == head) {
                current = null;
            }
            return toReturn;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    @Override
    public Iterator<Point> iterator() {
        return new CircularLinkedListIterator();
    }


}
