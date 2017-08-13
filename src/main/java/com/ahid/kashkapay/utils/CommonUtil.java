/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ahid.kashkapay.utils;

import java.util.UUID;

/**
 *
 * @author cccc
 */
public final class CommonUtil {
    
    public static String uniqueString() {
        return UUID.randomUUID().toString();
    }
    
    public static String getCommonDateFormat() {
        return "yyyy-MM-dd";
    }
}
