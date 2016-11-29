package lnyswz.common.util;

public class Common {
	public static String joinString(String source, String dest, String split){
		if(source.indexOf(dest) < 0){
			if(source.length() > 0){
				source += split;
			}
			source += dest;
		}
		return source;
	}
}
