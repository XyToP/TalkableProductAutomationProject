package util;

import org.testng.Assert;
import util.logging.Log;

import static util.EnvFactory.EnvType.*;

public class EnvFactory {

    private static final String VOID_LINK = "void.talkable.com";
    private static final String PROD_LINK = "talkable.com";
    private static final String BASTION_LINK = "bastion.talkable.com";

    private static final String COMMON_USER = PropertyLoader.loadProperty("talkable.user");
    private static final String REPORTS_USER = PropertyLoader.loadProperty("talkable.user.reports");

    private static final String password = PropertyLoader.loadProperty("talkable.password");
    private static final String registrationPath = "/register?object_or_array";
    private static final String baseApiPath = "/api/v2";

    private static EnvType envType;

    enum EnvType{PROD, VOID, BASTION, BART}



    private static String getUrl()
    {
        switch (getEnvType()){
            case PROD:
                return PROD_LINK;
            case VOID:
                return VOID_LINK;
            case BASTION:
                return BASTION_LINK;
            default:
                    return null;
        }

    }

    public static EnvType getEnvType(){
        if(envType==null){
            setEnvType();
        }
        return envType;
    }


    private static void setEnvType(){
        //property described in pom.xml  <test.environment>${env.NAME}</test.environment>
        if(envType == null) {
            String envName = ExecutionVariables.getMavenEnvName();
            if (envName == null) {
                Assert.fail("ERROR: envType is null from maven variable env.NAME");
            }

            switch (envName) {
                case "PROD":
                    envType = PROD;
                    break;
                case "VOID":
                    envType = VOID;
                    break;
                case "BASTION":
                    envType = BASTION;
                    break;
                case "BART":
                    envType = BART;
                    Assert.fail("ERROR: Test framework is not yet configured to <" + envType + "> environment.");
                    break;
                default:
                    Assert.fail("ERROR: Unknown env type: <" + envName + ">.");
            }
        }
        Log.logRecord("Test is running on <" + envType.toString() + "> environment");
    }


    public static String getEnvUrl(){
        return "https://" + getUrl();

    }

    public static String getAdminUrl(){
        return "https://admin." + getUrl();
    }

    public static String getCommonUser(){
        return COMMON_USER;
    }


    public static String getReportsUser() {
        return REPORTS_USER;
    }

    public static String getPassword(){
        return password;
    }

    public static String getRegistrationURL(){
        return getAdminUrl() + registrationPath;
    }

    public static String getApiUrl(){
        getUrl();
        if(envType == PROD) {
            return "https://www." + getUrl() + baseApiPath;
        }else {
            return getEnvUrl() + baseApiPath;
        }
    }

//    private static String getMavenEnvName(){
//        //property described in pom.xml  <test.environment>${env.NAME}</test.environment>
//        //correct working:
////        String env = System.getProperty("test.environment");
//        //for local running of the project:
//        String env = "VOID";
////        String env = "PROD";
////        String env = "BASTION";
//
//        System.out.println("LOG - EnvFactory: Test is running on <" + env + "> environment");
//        if(env == null){
//            Assert.fail("ERROR: 'env' variable is null in EnvFactory.getMavenEnvName(). Make sure that env.NAME is provided.");
//        }
//        return env;
//    }

}
