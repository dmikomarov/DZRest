package specs;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import static helpers.CustomApiListener.withCustomTemplates;
import static io.restassured.RestAssured.with;
import static io.restassured.filter.log.LogDetail.BODY;
import static io.restassured.filter.log.LogDetail.STATUS;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;

public class Specs {
  public static RequestSpecification baseRequestSpec = with()
          .baseUri("https://reqres.in")
          .basePath("/api")
          .log().uri()
          .log().headers()
          .log().body()
       .filter(withCustomTemplates())
          .contentType(ContentType.JSON);


  public static ResponseSpecification responseSpecPsw400 = new ResponseSpecBuilder()
          .log(STATUS)
          .log(BODY)
          .expectStatusCode(400)
          .expectBody("error", notNullValue())
          .build();

  public static ResponseSpecification responseSpecPsw404 = new ResponseSpecBuilder()
          .log(STATUS)
          .log(BODY)
          .expectStatusCode(404)
          .build();

  public static ResponseSpecification updateResponseSpec = new ResponseSpecBuilder()
          .log(STATUS)
          .log(BODY)
          .expectStatusCode(200)
          .expectBody("name", notNullValue())
          .build();

  public static ResponseSpecification createResponseSpec = new ResponseSpecBuilder()
          .log(STATUS)
          .log(BODY)
          .expectStatusCode(201)
          .expectBody("job", notNullValue())
          .build()
          .body("name", is("morpheus"))
          .body("job", is("leader"));

  public static ResponseSpecification totalPageResponseSpec = new ResponseSpecBuilder()
          .log(STATUS)
          .log(BODY)
          .expectStatusCode(200)
          .expectBody("total_pages", is(2))
          .build();
}
