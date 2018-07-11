package util;

import org.testng.Assert;

public class ExecutionVariables {
    /*The flag indicates whether the test will be executed on Local or Remote machine.
    !!! Please note always put the flag to TRUE before creation of pull request to origin/master !!!*/
    private static final boolean remoteExecution = true;

    public static boolean remoteExecution(){
        return remoteExecution;
    }

    public static String getMavenEnvName(){
        //property described in pom.xml  <test.environment>${env.NAME}</test.environment>
        //correct working:
        String env = System.getProperty("test.environment");
        //for local running of the project:
//        String env = "VOID";
//        String env = "PROD";
//        String env = "BASTION";

        System.out.println("LOG - EnvFactory: Test is running on <" + env + "> environment");
        if(env == null){
            Assert.fail("ERROR: 'env' variable is null in ExecutionVariables.getMavenEnvName(). Make sure that env.NAME is provided.");
        }
        return env;
    }

}
