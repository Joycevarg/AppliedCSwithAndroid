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

package com.google.engedu.wordladder;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Queue;

public class PathDictionary {
    private static final int MAX_WORD_LENGTH = 4;
    private static HashSet<String> words = new HashSet<>();
    private static HashMap<String,ArrayList<String>> wordGraph=new HashMap<>();

    public PathDictionary(InputStream inputStream) throws IOException {
        if (inputStream == null) {
            return;
        }
        Log.i("Word ladder", "Loading dict");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        String line = null;
        Log.i("Word ladder", "Loading dict");
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() > MAX_WORD_LENGTH) {
                continue;
            }



            ArrayList<String> neighbours=new ArrayList<>();
            ArrayList<String> neighneigs;
            for(String eachword:words)
            {

                if(areNeighbours(eachword,word))
                {
                    neighneigs=wordGraph.get(eachword);
                    neighneigs.add(word);
                    wordGraph.put(eachword,neighneigs);
                    neighbours.add(eachword);
//                    Log.d(eachword,word+" "+printNeighbours(word,eachword));
                }
            }
            wordGraph.put(word,neighbours);
            words.add(word);
        }
//        ArrayList<String> n;
//        for(String word:words) {
//            n = wordGraph.get(word);
//            Log.d(word,"Hello");
//            for(String ni:n)
//            {
//                Log.d(word,ni);
//            }
//        }
    }

    public static boolean areNeighbours(String word1,String word2)
    {
        int f=0;
        if(word1.length()==word2.length())
        {
            for(int i=0;i<word1.length();i++)
            {
                if(word1.charAt(i)!=word2.charAt(i))
                {
                    if(f==1)
                        return false;
                    else
                         f=1;
                }
            }
            return true;

        }
        else
            return false;

    }

    public String printNeighbours(String word1,String word2)
    {
        int f=0;
        if(word1.length()==word2.length())
        {
            for(int i=0;i<word1.length();i++)
            {
                if(word1.charAt(i)!=word2.charAt(i))
                {
                    if(f==1)
                        return "false";
                    else
                        f=1;
                }
            }
            return "true";

        }
        else
            return "false";

    }



    public boolean isWord(String word) {
        return words.contains(word.toLowerCase());
    }

    private ArrayList<String> neighbours(String word) {

        return wordGraph.get(word);
    }

    public ArrayList<ArrayList<String>> findPath(String start, String end) {
        ArrayDeque<ArrayList<String>> paths=new ArrayDeque<>();
        HashMap<String,Integer> visited=new HashMap<>();
        ArrayList<ArrayList<String>> allpaths=new ArrayList<>();
        ArrayList<String> next=new ArrayList<>();
        ArrayList<String> nextnext=new ArrayList<>();

        for(String word:words)
        {
            visited.put(word,8);
        }
        visited.put(start,0);
        next.add(start);
        paths.add(next);
        while(!paths.isEmpty()&&next.size()<=visited.get(end))
        {
            next=paths.removeFirst();
            ArrayList<String> nextneigh=neighbours(next.get(next.size()-1));
            for(String neigh:nextneigh)
            {
                nextnext=new ArrayList<>(next);
//                x=visited.get(neigh);
//                Log.d(neigh,x.toString());
                if(visited.get(neigh)>=next.size()) {
                    nextnext.add(neigh);
                    visited.put(neigh,next.size());
                    paths.add(nextnext);
                    if(neigh.equals(end))
                    {
                        Log.d("Path found",nextnext.toString());
                        allpaths.add(nextnext);
                    }
                }
            }
        }
        Integer x=visited.get(end);
        if(allpaths.isEmpty())
            return null;
        else
            return allpaths;


    }
}
