package com.paul.util;

public class StringUtil {
	
	private StringUtil(){}
	
	
	/**
	 * Returns the appended string with the supplied string OR if the supplied string is empty append the 2nd parameter
	 *  
	 * @param sb - String builder to modify
	 * @param appendString - String to be appended and checked if empty/null 
	 * @param appendIfEmpty - String to be appended if the 1st parameter is empty/null
	 * @return appended string builder
	 */
	public static StringBuilder appendOr(StringBuilder sb, String appendString, String appendIfEmpty){
		if(appendString != null && appendString.isEmpty()){
			sb.append(appendIfEmpty);
		} else {
			sb.append(appendString);
		}
		return sb;
	}
	
	public static boolean isInteger(String s) {
	    return isInteger(s,10);
	}

	private static boolean isInteger(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}

}
