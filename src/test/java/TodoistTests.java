import com.github.javafaker.Faker;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.thucydides.core.annotations.Title;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;

@RunWith(SerenityRunner.class)
public class TodoistTests
{
    private static TodoistEndPoint endPoint;
    private static final String defaultName = "Inbox";
    private static String randomName;

    @BeforeClass
    public static void createEndPoint()
    {
        RestAssured.defaultParser = Parser.JSON;
        endPoint = new TodoistEndPoint();
        randomName = new Faker().color().name();
    }

    @Title("Verification of get request")
    @Test
    public void getProjects()
    {
        endPoint
                .getProjects()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Title("Verification of get request")
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

    @Title("Verification of get request")
    @Test
    public void createProject()
    {
        endPoint
                .createProject(randomName)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Title("Verification of get request")
    @Test
    public void deleteLastProject()
    {
        var projects = endPoint.getProjects();
        List<String> jsonResponse = projects.jsonPath().getList("$");
        var lastId = projects.jsonPath().getString(String.format("id[%s] ", jsonResponse.size() - 1));

        endPoint
                .deleteProject(lastId)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Title("get tasks")
    @Test
    public void getTasks()
    {
        endPoint
                .getActiveTasks()
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Title("create task")
    @Test
    public void createTask()
    {
        endPoint
                .createTask(randomName)
                .then()
                .assertThat()
                .statusCode(200);
    }

    @Title("delete last task")
    @Test
    public void deleteLastTask()
    {
        var tasks = endPoint.getActiveTasks();
        List<String> jsonResponse = tasks.jsonPath().getList("$");
        var lastId = tasks.jsonPath().getString(String.format("id[%s] ", jsonResponse.size() - 1));

        endPoint
                .deleteTask(lastId)
                .then()
                .assertThat()
                .statusCode(204);
    }

    @Title("delete last task")
    @Test
    public void deleteNonExistedTask()
    {
        endPoint
                .deleteTask("wrong_id")
                .then()
                .assertThat()
                .statusCode(204);
    }
}
