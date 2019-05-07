package it.reply.util;

import Exception.ProvisioningUtilsException;

import java.io.*;
import java.util.Properties;
import java.util.Scanner;

public class ProvisioningUtils {
    public ProvisioningUtils() {
        super();
    }
    
    public static String getConfigProperty(String propName) throws ProvisioningUtilsException {
            String propVal = null;
            Properties prop = new Properties();
            InputStream is = null;
            String properties_file = System.getProperty("user.dir") + "/properties/Vodafoneconfig.properties";
            try {
                is = new FileInputStream(properties_file);
                prop.load(is);
                propVal = prop.getProperty(propName);
                is.close();
            } catch (FileNotFoundException e) {
                throw new ProvisioningUtilsException("COULD_NOT_FIND_PROP_LIST_FILE", e);
            } catch (IOException e) {
                throw new ProvisioningUtilsException("GENERIC_IO_EXCEPTION", e);
            }

            return propVal;
        }
        
        public static String getComponentConfigProperty(String component, String propName) throws ProvisioningUtilsException {
            String propVal = null;
            Properties prop = new Properties();
            InputStream is = null;
            String properties_file = System.getProperty("user.dir") + "/properties/"+component+"_properties/"+component+".properties";
            try {
                is = new FileInputStream(properties_file);
                prop.load(is);
                propVal = prop.getProperty(propName);
                is.close();
            } catch (FileNotFoundException e) {

                throw new ProvisioningUtilsException("COULD_NOT_FIND_PROP_LIST_FILE", e);
            } catch (IOException e) {
                throw new ProvisioningUtilsException("GENERIC_IO_EXCEPTION", e);
            }

            return propVal;
        }
}


