import com.fasterxml.jackson.databind.util.JSONPObject;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.junit.BeforeClass;
import org.junit.Test;


import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.collection.IsMapContaining.hasKey;

public class Teste {

    @BeforeClass
    public static void base(){
        baseURI = "https://api.thecatapi.com/v1/";
    }

    private static String apiKey = "x-api-key";
    private static String apiValue = "3e95bc77-c37a-4362-823a-b8e20110f1a2";


    public String buscarImagem(){
        Response response =
                given()
                        .header(apiKey, apiValue)
                        .contentType("application/json")
                        .when().get("images/search");

        return response.path("id[0]").toString();
    }

    @Test
    public void validandoBuscaDeImagem() {
       var imgId =  buscarImagem();

        Response response =
                given()
                        .header(apiKey, apiValue)
                        .when().get("images/" + imgId);

        response.then()
                .statusCode(200)
                .body(imgId, equalTo(response.path("Id")));
    }

    @Test
    public void votarEmImagem() {
        var imgId =  buscarImagem();

        JSONObject jsonObject = new JSONObject();

        jsonObject.put("image_id", imgId);
        jsonObject.put("value", 1);

        Response response =
                given()
                        .header(apiKey, apiValue)
                        .contentType("application/json")
                        .accept("JSON")
                        .body(jsonObject.toString())
                        .when().post("votes");

        response.then()
                .statusCode(200)
                .body(containsString("SUCCESS"));

        System.out.println(response.body().asString());
    }

}

//api_key = 3e95bc77-c37a-4362-823a-b8e20110f1a2