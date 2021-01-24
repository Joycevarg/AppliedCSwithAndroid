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

package com.google.engedu.anagrams;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Random;

public class AnagramDictionary {

    private static final int MIN_NUM_ANAGRAMS = 5;
    private static final int DEFAULT_WORD_LENGTH = 3;
    private static final int MAX_WORD_LENGTH = 7;
    private static int sizeofstartword=DEFAULT_WORD_LENGTH;
    private Random random = new Random();
    ArrayList<String> wordList=new ArrayList<String>();
    HashSet<String> wordSet=new HashSet<String>();
    HashMap<String,ArrayList<String>> letterToWord = new HashMap<String, ArrayList<String>>();
    HashMap<Integer,ArrayList<String>> sizeToWord= new HashMap<>();
    public AnagramDictionary(Reader reader) throws IOException {
        BufferedReader in = new BufferedReader(reader);
        String line;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            wordList.add(word);
            wordSet.add(word);
            String key =sortLetters(word);
            Integer nokey=word.length();
            if(letterToWord.containsKey(key))
            {
                ArrayList<String> anagramList=letterToWord.get(key);
                anagramList.add(word);
                letterToWord.put(key,anagramList);
            }
            else
            {
                ArrayList<String> anagramList=new ArrayList<>();
                anagramList.add(word);
                letterToWord.put(key,anagramList);
            }
            if(sizeToWord.containsKey(nokey))
            {
                ArrayList<String> anagramList=sizeToWord.get(nokey);
                anagramList.add(word);
                sizeToWord.put(nokey,anagramList);
            }
            else
            {
                ArrayList<String> anagramList=new ArrayList<>();
                anagramList.add(word);
                sizeToWord.put(nokey,anagramList);
            }
        }

    }

  
    public List<String> getAnagrams(String targetWord) {
        ArrayList<String> result = new ArrayList<String>();
        String sorted=sortLetters(targetWord);
        for(String s:wordList)
        {
            if(sortLetters(s).equals(sorted))
                result.add(s);
        }
        return result;
    }

    public List<String> getAnagramsWithOneMoreLetter(String word) {
        ArrayList<String> result = new ArrayList<String>();
        String wordandletter=new String();
        for(char ch='a';ch<='z';ch++)
        {  wordandletter=word+ch;
           String sorted=sortLetters(wordandletter);
              for(String s:wordList)
        {
            if(sortLetters(s).equals(sorted))
                result.add(s);
        }
        }
        return result;
    }

    public String pickGoodStarterWord() {

        ArrayList<String> wrdlst=new ArrayList<>();
        wrdlst=sizeToWord.get(sizeofstartword);
        Log.d("sizetoword",wrdlst.toString());
        if(sizeofstartword<MAX_WORD_LENGTH)
        sizeofstartword++;
        int noofwords=wrdlst.size();
        int startpoint=random.nextInt(noofwords);
        int noofan;
        String startword=new String();
        String sortedstartwrdwithlet;
        ArrayList<String> wordanags=new ArrayList<>();
        for(int i=startpoint+1;i%noofwords!=startpoint;i++) {
            noofan = 0;
            startword =wrdlst.get(startpoint);
            char ch = 'a';
            while (noofan <= MIN_NUM_ANAGRAMS&&ch<='z') {
                sortedstartwrdwithlet = sortLetters(startword + ch);
                if (letterToWord.containsKey(sortedstartwrdwithlet))
                {wordanags = letterToWord.get(sortedstartwrdwithlet);
                noofan += wordanags.size();}
                ch++;
            }
            if (noofan >= 5)
                return startword;

        }
        return "stop";
    }
    public  String sortLetters(String str)
    {
        char ch[]=str.toCharArray();
        Arrays.sort(ch);
        String sorted=new String(ch);
        return sorted;

    }
}