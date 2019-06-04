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

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;

import java.util.ArrayList;


public class PuzzleBoard {

    private static final int NUM_TILES = 3;
    private static final int[][] NEIGHBOUR_COORDS = {
            { -1, 0 },
            { 1, 0 },
            { 0, -1 },
            { 0, 1 }
    };
    public ArrayList<PuzzleTile> tiles;
    int empty,steps=0;
    int pr;
    PuzzleBoard previous=null;
    PuzzleBoard(Bitmap bitmap, int parentWidth) {
        tiles=new ArrayList<>();
        bitmap=Bitmap.createScaledBitmap(bitmap,parentWidth,parentWidth,false);
        int width=bitmap.getWidth()/NUM_TILES;
        Bitmap tile;
        for(int i=0;i<NUM_TILES;i++)
            for(int j=0;j<NUM_TILES;j++)
            {
                tile=Bitmap.createBitmap(bitmap,j*width,i*width,width,width);
                PuzzleTile oneTile=new PuzzleTile(tile,i*NUM_TILES+j);
                tiles.add(oneTile);}
        tiles.remove(tiles.size()-1);
            tiles.add(null);
            empty=NUM_TILES*NUM_TILES-1;

    }

    PuzzleBoard(PuzzleBoard otherBoard) {
        tiles = (ArrayList<PuzzleTile>) otherBoard.tiles.clone();
    }

    public void reset() {
        // Nothing for now but you may have things to reset once you implement the solver.
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        return tiles.equals(((PuzzleBoard) o).tiles);
    }

    public void draw(Canvas canvas) {
        if (tiles == null) {
            return;
        }
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                tile.draw(canvas, i %NUM_TILES, i / NUM_TILES);
            }
        }
    }

    public boolean click(float x, float y) {
        for (int i = 0; i < NUM_TILES * NUM_TILES; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile != null) {
                if (tile.isClicked(x, y, i % NUM_TILES, i / NUM_TILES)) {
                    return tryMoving(i % NUM_TILES, i / NUM_TILES);
                }
            }
        }
        return false;
    }

    private boolean tryMoving(int tileX, int tileY) {
        for (int[] delta : NEIGHBOUR_COORDS) {
            int nullX = tileX + delta[0];
            int nullY = tileY + delta[1];
            if (nullX >= 0 && nullX < NUM_TILES && nullY >= 0 && nullY < NUM_TILES &&
                    tiles.get(XYtoIndex(nullX, nullY)) == null) {
                swapTiles(XYtoIndex(nullX, nullY), XYtoIndex(tileX, tileY));
                empty=tileX+NUM_TILES*tileY;
                return true;
            }

        }
        return false;
    }

    public boolean resolved() {
        for (int i = 0; i < NUM_TILES * NUM_TILES - 1; i++) {
            PuzzleTile tile = tiles.get(i);
            if (tile == null || tile.getNumber() != i)
                return false;
        }
        return true;
    }

    private int XYtoIndex(int x, int y) {
        return x + y * NUM_TILES;
    }

    protected void swapTiles(int i, int j) {
        PuzzleTile temp = tiles.get(i);
        tiles.set(i, tiles.get(j));
        tiles.set(j, temp);
    }

    public ArrayList<PuzzleBoard> neighbours() {
        ArrayList<PuzzleBoard> configs=new ArrayList<>();
        int nullX=empty/3;
        int nullY=empty%3;
        PuzzleBoard config;
        for (int[] delta : NEIGHBOUR_COORDS) {
            int X = nullX + delta[0];
            int Y = nullY + delta[1];
            if (X >= 0 && X < NUM_TILES && Y >= 0 && Y < NUM_TILES)
            {
                config=new PuzzleBoard(this);
                int newtile=Y+NUM_TILES*X;
                config.swapTiles(empty,newtile);
                config.empty=newtile;
                configs.add(config);
            }
        }

        return configs;
    }

    public int priority() {
        PuzzleTile tile;
        int pos,xdif,ydif,cost=0;
        for(int i=0;i<NUM_TILES*NUM_TILES;i++) {
            tile=tiles.get(i);
            if(tile!=null)
                pos=tile.getNumber();
            else
                pos=NUM_TILES*NUM_TILES-1;
            xdif=(pos/3-i/3);
            if(xdif<0)
                xdif=xdif*-1;
            ydif=(pos%3-i%3);
            if(ydif<0)
                ydif=ydif*-1;
            cost=cost +xdif+ydif;

//            Log.d("Order "+Integer.valueOf(i).toString()," Pos "+ Integer.valueOf(pos).toString()+" Cost "+cost);
        }
        pr=cost+steps;
        return cost+steps;
    }

}
