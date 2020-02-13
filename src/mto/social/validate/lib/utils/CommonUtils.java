/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mto.social.validate.lib.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import java.lang.reflect.Type;
import org.apache.log4j.Logger;

/**
 *
 * @author vanntl
 */
public class CommonUtils {
    
    private static final Logger logger = Logger.getLogger(CommonUtils.class);
    
    public static <T> T jsonToObject(String json, Class<T> cls) {
        try {
            Object fromJson = new GsonBuilder().serializeNulls().create().fromJson(json, cls);
            if (cls.isInstance(fromJson)) {
                return (T) fromJson;
            }
        } catch (JsonSyntaxException ex) {
            logger.warn(ex.getMessage(), ex);
        }
        return null;
    }

    public static <T> T jsonToObject(String json, Type type) {
        try {
            Object fromJson = new GsonBuilder().serializeNulls().create().fromJson(json, type);
            return (T) fromJson;
        } catch (JsonSyntaxException ex) {
            logger.warn(ex.getMessage(), ex);
        }
        return null;
    }
    
    public static String objectToString(Object obj) {
        try {
            return new GsonBuilder().disableHtmlEscaping().create().toJson(obj);
        } catch (Exception ex) {
            logger.warn(ex.getMessage(), ex);
            return null;
        }
    }

    private CommonUtils() {
    }
}
