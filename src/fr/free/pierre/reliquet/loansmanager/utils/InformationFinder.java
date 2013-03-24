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
package fr.free.pierre.reliquet.loansmanager.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import android.util.Log;
import fr.free.pierre.reliquet.loansmanager.BuildConfig;

/**
 * @author Pierre Reliquet
 * 
 */
public class InformationFinder {
    
    private static final String              END_PATTERN       = "</h3>";
    private static final String              END_SUB_PATTERN   = "</span>";
    private static final String              SEARCH_LINK       = "http://www.amazon.";
    private static final String              SEARCH_LINK_END   = "/s/ref=nb_sb_noss?url=search-alias%3Daps&field-keywords=";
    private static final String              START_PATTERN     = "<div id=\"result_0\"";
    private static final String              START_SUB_PATTERN = "<span class=\"lrg bold\">";
    private static final Map<String, String> LANGUAGES;
    static {
        LANGUAGES = new HashMap<String, String>();
        LANGUAGES.put("fr", "fr");
        LANGUAGES.put("de", "de");
    }
    
    public static String findTitleFromBarcode(String barcode) {
        String extension = "";
        String locale = Locale.getDefault().getLanguage();
        if (LANGUAGES.containsKey(locale)) {
            extension = LANGUAGES.get(locale);
        } else {
            extension = "com";
        }
        String result = "";
        try {
            result = internalFindTitleFromBarcode(barcode, extension);
        } catch (Exception e) {
            result = "";
        }
        return result;
    }
    
    private static String internalFindTitleFromBarcode(String barcode,
            String extension) throws Exception {
        URL url = new URL(SEARCH_LINK + extension + SEARCH_LINK_END + barcode);
        BufferedReader in = new BufferedReader(new InputStreamReader(
                url.openStream()));
        StringBuilder result = new StringBuilder();
        String parsing;
        String line = "";
        try {
            while ((line = in.readLine()) != null) {
                result.append(line);
            }
            parsing = result.substring(result.indexOf(START_PATTERN));
            parsing = parsing.substring(0, parsing.indexOf(END_PATTERN));
            parsing = parsing.substring(parsing.indexOf(START_SUB_PATTERN)
                    + START_SUB_PATTERN.length());
            parsing = parsing.substring(0, parsing.indexOf(END_SUB_PATTERN));
        } catch (Exception e) {
            if (BuildConfig.DEBUG) {
                Log.e("PARSING",
                        "An error occured while searching the information over the internet");
                e.printStackTrace();
            }
            parsing = "";
        } finally {
            in.close();
        }
        return StringEscapeUtils.unescapeHtml(parsing);
    }
}
