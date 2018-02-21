package com.nitesh.brill.saleslines.Common_Files.PermissionHelper;

import java.util.List;

/**
 * Created by nitesh on 29/11/17.
 */

public class Utils {

    public static String[] convertListToArray(List<String> list) {
        String[] converted = new String[list.size()];
        return list.toArray(converted);

    }
}