package com.countrypicker;

/**
 * POJO
 *
 */
public class Country {
	private String code;
	private String name;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
	    String lpszName = name;
	
	    if (name != null && name.isEmpty() == false) {
	        String[] lpszNames = name.split(";");
	
	        if (lpszNames.length == 2) {
	            lpszName = lpszNames[1].trim();
	        }
	    }
	
	    return lpszName;
	}
	
	public String getCountryCode() {
	    String lpszName = "";
	
	    if (name != null && name.isEmpty() == false) {
	        String[] lpszNames = name.split(";");
	
	        if (lpszNames.length == 2) {
	            lpszName = lpszNames[0].trim();
	        }
	    }
	
	    return lpszName;
	}

	public void setName(String name) {
		this.name = name;
	}

}
