/**
 * 
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

    private static final String SEARCH_LINK = "http://www.amazon.fr/s/ref=nb_sb_noss/275-9152685-9132939?__mk_fr_FR=%C3%85M%C3%85Z%C3%95%C3%91&url=search-alias%3Daps&field-keywords=";
    private static final String START_PATTERN = "<div id=\"result_0\"";
    private static final String END_PATTERN = "</h3>";
    private static final String START_SUB_PATTERN = "<span class=\"lrg bold\">";
    private static final String END_SUB_PATTERN = "</span>";

    public static String findTitleFromBarcode(String barcode)
            throws IOException {
        URL url = new URL(SEARCH_LINK + barcode);
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
