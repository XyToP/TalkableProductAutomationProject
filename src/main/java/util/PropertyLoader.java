package util;

import util.logging.Log;

import java.io.IOException;
import java.util.Properties;


public class PropertyLoader {

    private static final String PROP_FILE = "/application.properties";

    private static final String VOID_PROP_FILE = "/void.properties";
    private static final String PROD_PROP_FILE = "/prod.properties";
    private static final String BASTION_PROP_FILE = "/bastion.properties";

    private PropertyLoader() {}

    public static String loadProperty(String name) {
        Properties props = new Properties();
        try {
            props.load(PropertyLoader.class.getResourceAsStream(PROP_FILE));
        } catch (IOException e) {
            e.printStackTrace();
        }

        String value = "";

        if (name != null) {
            value = props.getProperty(name);
        }
        return value;
    }

    public static String loadEnvProperty(String name){
        String filePath = "";
        //property described in pom.xml  <test.environment>${env.NAME}</test.environment>
        EnvFactory.EnvType env = EnvFactory.getEnvType();

        switch (env){
            case PROD:
                filePath = PROD_PROP_FILE;
                break;
            case VOID:
                filePath = VOID_PROP_FILE;
                break;
            case BASTION:
                filePath = BASTION_PROP_FILE;
                break;
        }
        Properties props = getPropertiesFile(filePath);
        Log.debagRecord("Property loaded from <"+ filePath+ "> file. Property name: <" + name + ">");

        String propValue = "";

        if (name != null) {
            assert props != null;
            propValue = props.getProperty(name);
        }
        return propValue;
    }


    private static Properties getPropertiesFile(String fileName){
        Properties props = new Properties();
        try {
            props.load(PropertyLoader.class.getResourceAsStream(fileName));
            return props;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }





}
