/**
 * Copyright 2013 Pierre Reliquet©
 * 
 * Loans Manager is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any
 * later version.
 * 
 * Loans Manager is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * Loans Manager. If not, see <http://www.gnu.org/licenses/>
 */
package org.pierrrrrrrot.loanmanager.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * @author Pierre Reliquet
 * 
 */
public class InformationFinder {
    
    private static final String END_PATTERN       = "</h3>";
    private static final String END_SUB_PATTERN   = "</span>";
    private static final String SEARCH_LINK       = "http://www.amazon.";
    private static final String SEARCH_LINK_END   = "/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=";
    private static final String START_PATTERN     = "<div id=\"result_0\"";
    private static final String START_SUB_PATTERN = "<span class=\"lrg bold\">";
    
    public static String findTitleFromBarcode(String barcode) {
        String result = "";
        try {
            result = internalFindTitleFromBarcode(barcode, "com");
        } catch (Exception e) {
            result = "";
        }
        if (Utils.isNullOrEmpty(result)) {
            try {
                result = internalFindTitleFromBarcode(barcode, "fr");
            } catch (IOException e) {
                result = "";
            }
        }
        return result;
    }
    
    private static String internalFindTitleFromBarcode(String barcode,
            String extension) throws IOException {
        URL url = new URL(SEARCH_LINK + extension + SEARCH_LINK_END + barcode);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));
        StringBuilder result = new StringBuilder();
        String line = "";
        while ((line = in.readLine()) != null) {
            result.append(line);
        }
        in.close();
        String parsing = result.substring(result.indexOf(START_PATTERN));
        parsing = parsing.substring(0, parsing.indexOf(END_PATTERN));
        parsing = parsing.substring(parsing.indexOf(START_SUB_PATTERN)
                + START_SUB_PATTERN.length());
        parsing = parsing.substring(0, parsing.indexOf(END_SUB_PATTERN));
        return StringEscapeUtils.unescapeHtml(parsing);
    }
}
