package supersql.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

//added by goto 20131016
public class LevenshteinDistance {
	
	public static String checkLevenshteinAndSuggest(String Name, ArrayList<String> Names) {
		String checkdStrings = checkLevenshteinDistance(Name, Names);
		String firstString = "";
		if(checkdStrings.contains(","))
			firstString = checkdStrings.substring(0,checkdStrings.indexOf(","));
		else
			firstString = checkdStrings;
		
		if(!firstString.isEmpty())
			Log.err("\nDid you mean... '"+firstString+"' ?");
		
		return checkdStrings;
	}
	
	public static int getLevenshteinDistance(CharSequence str1, CharSequence str2) {
	        int[][] distance = new int[str1.length() + 1][str2.length() + 1];
	
	        for (int i = 0; i <= str1.length(); i++)
	                distance[i][0] = i;
	        for (int j = 1; j <= str2.length(); j++)
	                distance[0][j] = j;
	
	        for (int i = 1; i <= str1.length(); i++)
	                for (int j = 1; j <= str2.length(); j++)
	                        distance[i][j] = minimum(
	                                        distance[i - 1][j] + 1,
	                                        distance[i][j - 1] + 1,
	                                        distance[i - 1][j - 1] + ((str1.charAt(i - 1) == str2.charAt(j - 1)) ? 0 : 1));
	
	        return distance[str1.length()][str2.length()];    
	}
    private static int minimum(int a, int b, int c) {
        return Math.min(Math.min(a, b), c);
	}

	//類似度判定 (measuring similarity)
	//return: sorted b
	public static String checkLevenshteinDistance(String a, ArrayList<String> b){
		String str1 = "";
		String str2 = "";
        ArrayList<LevensteinClass> al = new ArrayList<LevensteinClass>();
        String sortedString = "";
		
		//1に近いほど似ている
        for(int i=0; i<b.size(); i++){
        	str1 = a.toLowerCase();
        	str2 = b.get(i).toLowerCase();
        	int ld = getLevenshteinDistance(str1, str2);
        	al.add(new LevensteinClass(ld, str2));
        	//Log.err("LevenshteinDistance("+str1+", "+str2+")：" + ld);
        }
        
        //descending sort
        Collections.sort(al, new LevensteinComparator());
        //Collections.reverse(al);
        
        Iterator<LevensteinClass> it = al.iterator();
        while (it.hasNext()) {
        	LevensteinClass data = it.next();
        	sortedString += data.getLevensteinClassString()+", ";
            //Log.info(data.getLevenshteinDistance() + ": " + data.getLevensteinClassString());
        }
        if(!sortedString.equals("") && sortedString.trim().endsWith(","))
        	sortedString = sortedString.substring(0, sortedString.length()-2);
        
        //TODO: 同じLevenshteinDistance値のものはアルファベット順にソート
        
        return sortedString;	//sorted b
	}
	public static class LevensteinClass {
	    private int LevenshteinDistance;
	    private String str;

	    //Constructor
	    public LevensteinClass(int a, String b) {
	        this.LevenshteinDistance = a;
	        this.str = b;
	    }
	    public int getLevenshteinDistance(){
	        return this.LevenshteinDistance;
	    }
	    public String getLevensteinClassString(){
	        return this.str;
	    }
	}
	public static class LevensteinComparator implements Comparator<LevensteinClass> {
	    public int compare(LevensteinClass a, LevensteinClass b) {
	    	int ld1 = a.getLevenshteinDistance();
	    	int ld2 = b.getLevenshteinDistance();

	        //ascending sort
	        if (ld1 > ld2) {
//	        //descending sort
//	        if (ld1 < ld2) {
	            return 1;
	        } else if (ld1 == ld2) {
	            return 0;
	        } else {
	            return -1;
	        }
	    }
	}
	
}