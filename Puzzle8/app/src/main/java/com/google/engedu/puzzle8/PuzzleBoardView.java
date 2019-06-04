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

package com.google.engedu.puzzle8;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Random;

public class PuzzleBoardView extends View {
    public static final int NUM_SHUFFLE_STEPS = 40;
    private Activity activity;
    private PuzzleBoard puzzleBoard;
    private ArrayList<PuzzleBoard> animation;

    public PuzzleBoardView(Context context) {
        super(context);
        activity = (Activity) context;
        animation = null;
    }

    public void initialize(Bitmap imageBitmap) {
        int width = getWidth();
        puzzleBoard = new PuzzleBoard(imageBitmap, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (puzzleBoard != null) {
            if (animation != null && animation.size() > 0) {
                puzzleBoard = animation.remove(0);
                puzzleBoard.draw(canvas);
                if (animation.size() == 0) {
                    animation = null;
                    puzzleBoard.reset();
                    Toast toast = Toast.makeText(activity, "Solved! ", Toast.LENGTH_LONG);
                    toast.show();
                } else {
                    this.postInvalidateDelayed(500);
                }
            } else {
                puzzleBoard.draw(canvas);
            }
        }
    }

    public void shuffle() {
        if (animation == null && puzzleBoard != null) {
            // Do something. Then:
            ArrayList<PuzzleBoard> nextconfigs;
            Random rn=new Random();
            for(int i=0;i<NUM_SHUFFLE_STEPS;i++)
            {

                nextconfigs=puzzleBoard.neighbours();
                int x=rn.nextInt(nextconfigs.size());
                puzzleBoard=nextconfigs.get(x);

            }
            puzzleBoard.reset();
            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (animation == null && puzzleBoard != null) {
            switch(event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    if (puzzleBoard.click(event.getX(), event.getY())) {
                        invalidate();
                        if (puzzleBoard.resolved()) {
                            Toast toast = Toast.makeText(activity, "Congratulations!", Toast.LENGTH_LONG);
                            toast.show();
                        }
                        return true;
                    }
            }
        }
        return super.onTouchEvent(event);
    }

    public void solve() {

    puzzleBoard.steps=0;
    puzzleBoard.previous=null;
        class puzzleComparator implements Comparator<PuzzleBoard> {

            // Overriding compare()method of Comparator
            // for descending order of cgpa

            public int compare(PuzzleBoard s1, PuzzleBoard s2) {
                int pr1=s1.priority();
                int pr2=s2.priority();
                if (pr1>pr2)
                    return 1;
                else if (pr1<pr2)
                    return -1;
                return 0;
            }
        }
     PriorityQueue<PuzzleBoard> pq=new PriorityQueue<>(11,new puzzleComparator());
    pq.add(puzzleBoard);
    while(!pq.isEmpty())
    {
        PuzzleBoard pb=pq.poll();
        if(pb.priority()==pb.steps)
        {
            ArrayList<PuzzleBoard> answer=new ArrayList<>();
            while(pb!=null)
            {
                answer.add(pb);
                pb=pb.previous;
            }
            Collections.reverse(answer);
            animation=answer;
            puzzleBoard=answer.get(answer.size()-1);
            invalidate();
            break;
        }
        else
        {
            ArrayList<PuzzleBoard> neigh=pb.neighbours();
            for(PuzzleBoard p:neigh)
            {
                    p.steps = pb.steps + 1;
                    p.previous = pb;
                    if(pb.previous==null||p.empty!=pb.previous.empty)
                    {
                        pq.add(p);
                    }
            }
        }
    }
    }
}
