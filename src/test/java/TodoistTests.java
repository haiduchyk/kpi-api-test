import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.List;
import static org.hamcrest.Matchers.equalTo;


public class TodoistTests
{
    private static TodoistEndPoint endPoint;
    private static final String defaultName = "Inbox";
    private static String randomRepo;

    @BeforeClass
    public static void createEndPoint()
    {
        RestAssured.defaultParser = Parser.JSON;
        endPoint = new TodoistEndPoint();
        randomRepo = new Faker().color().name();
    }

    @Test
    public void getProjects()
    {
        endPoint
                .getProjects()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void getDefaultProject()
    {
        var projects = endPoint.getProjects();
        var firstId = projects.jsonPath().getString(String.format("id[%s] ", 0));

        endPoint
                .getProject(firstId)
                .then()
                .assertThat()
                .statusCode(200)
                .body("name", equalTo(defaultName));
    }

    @Test
    public void createProject()
    {
        endPoint
                .createProject(randomRepo)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Test
    public void deleteLastProject()
    {
        var projects = endPoint.getProjects();
        List<String> jsonResponse = projects.jsonPath().getList("$");
        var lastId = projects.jsonPath().getString(String.format("id[%s] ", jsonResponse.size() - 1));

        endPoint
                .deleteTask(lastId)
                .then()
                .assertThat()
                .statusCode(204);
    }
}
