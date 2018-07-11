package api.objects.origin;

import api.objects.Site;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import util.EnvFactory;


public class Origin{

    private static final String URL = EnvFactory.getApiUrl() + "/origins";

    public Response postOriginPurchaseWithUUID(Site site, String email, String uuid){
        String postBody =
                "{\n" +
                        "  \"api_key\": \"" + site.getApiKey() + "\",\n" +
                        "  \"site_slug\": \"" + site.getSiteSlug() + "\",\n" +
                        "  \"type\": \"Purchase\",\n" +
                        "  \"data\": {\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"order_number\": \"" + String.valueOf(System.currentTimeMillis()).substring(7) + "\",\n" +
                        "    \"subtotal\": 100,\n" +
                        "    \"uuid\": \"" + uuid + "\",\n" +
                        "    \"campaign_tags\": \"api\",\n" +
                        "    \"items\": [\n" +
                        "      {\n" +
                        "        \"price\": 25,\n" +
                        "        \"quantity\": 4,\n" +
                        "        \"product_id\": \"TSHIRT\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"
                ;
        System.out.println("DEBAG: " + postBody);

        return postAndLogResponse(postBody);
    }

    public Response postOriginPurchase(Site site, String email, String uuid, String ipAddress){
        String postBody =
                "{\n" +
                        "  \"api_key\": \"" + site.getApiKey() + "\",\n" +
                        "  \"site_slug\": \"" + site.getSiteSlug() + "\",\n" +
                        "  \"type\": \"Purchase\",\n" +
                        "  \"data\": {\n" +
                        "    \"email\": \"" + email + "\",\n" +
                        "    \"order_number\": \"" + String.valueOf(System.currentTimeMillis()).substring(7) + "\",\n" +
                        "    \"subtotal\": 100,\n" +
                        "    \"uuid\": \"" + uuid + "\",\n" +
                        "    \"ip_address\": \"" + ipAddress + "\",\n" +
                        "    \"campaign_tags\": \"api\",\n" +
                        "    \"items\": [\n" +
                        "      {\n" +
                        "        \"price\": 25,\n" +
                        "        \"quantity\": 4,\n" +
                        "        \"product_id\": \"TSHIRT\"\n" +
                        "      }\n" +
                        "    ]\n" +
                        "  }\n" +
                        "}"
                ;
        System.out.println("DEBAG: " + postBody);

        return postAndLogResponse(postBody);
    }

    private Response postAndLogResponse(String postBody){
        Response response = RestAssured
                .given()
                .contentType("application/json")
                .body(postBody)
                .when()
                .post(URL);
        Assert.assertEquals(response.getStatusCode(), 201, "FAILED: Incorrect status code in response for post Origin. \r\n" +
                "Request body: " + postBody + "\r\n" +
                "Response status line: " + response.getStatusLine() + "\r\n" +
                "Response body: " + response.body().print());

        System.out.println("LOG: Response body for POST to /origin :");
        System.out.println("DEBAG: Status line: " +  response.getStatusLine());
        response.body().print();
        return response;
    }



    public Response postOriginPurchase(Site site){
        String defaultUUID = "b3967d36-4e7f-46bc-92b3-57344347cd6a";
        String defaultEmail = "api.email@test.com";
        return postOriginPurchaseWithUUID(site, defaultEmail, defaultUUID);
    }






}
