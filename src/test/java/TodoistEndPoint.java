import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.simple.JSONObject;

public class TodoistEndPoint
{
    private static final String url = "https://api.todoist.com/rest/v1";
    private static final String token = "Bearer " + System.getenv().get("TODOIST_TOKEN");

    public Response getProjects()
    {
        return given()
                .when()
                .get("/projects")
                .then()
                .contentType(ContentType.JSON)
                .extract()
                .response();
    }

    public Response createProject(String name)
    {
        JSONObject body = new JSONObject();
        body.put("name", name);
        return given()
                .body(body.toJSONString())
                .when()
                .post("/projects")
                .then()
                .extract()
                .response();
    }

    public Response getProject(String id)
    {
        return given()
                .when()
                .get(String.format("/projects/%s", id))
                .then()
                .extract()
                .response();
    }

    public Response deleteProject(String id)
    {
        return given()
                .when()
                .delete(String.format("/projects/%s", id))
                .then()
                .extract()
                .response();
    }

    public Response getActiveTasks()
    {
        return given()
                .when()
                .get("/tasks")
                .then()
                .extract()
                .response();
    }

    public Response deleteTask(String id)
    {
        return given()
                .when()
                .delete(String.format("/tasks/%s", id))
                .then()
                .extract()
                .response();
    }


    public Response createTask(String content)
    {
        JSONObject body = new JSONObject();

        body.put("content", content);
        body.put("due_string", "tomorrow at 12:00");
        body.put("due_lang", "en");
        body.put("priority", 1);

        return given()
                .body(body.toJSONString())
                .when()
                .post("/tasks")
                .then()
                .extract()
                .response();
    }

    private RequestSpecification given()
    {
        return RestAssured.given()
                .log().uri()
                .baseUri(url)
                .contentType(ContentType.JSON)
                .header("Authorization", token);
    }
}
