import io.restassured.http.ContentType;
import org.hamcrest.core.IsEqual;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class testaCliente {

    String baseURL = "http://localhost:8080";
    String endPointCliente = "/cliente";
    String urlClienteRisco = "/risco/11";
    String idCliente = "/1002";

    String apagaTodosClientes = "/apagaTodos";


    @BeforeEach
    public void deletarTodosClientes() {

        String clienteCadastrado = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String clienteCadastrado2 = "{\n" +
                "  \"id\": 1003,\n" +
                "  \"idade\": 29,\n" +
                "  \"nome\": \"Donnald\",\n" +
                "  \"risco\": 5\n" +
                "}";

        String respostaEsperada = "{}";


        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .post(baseURL + endPointCliente);

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado2)
                .when()
                .post(baseURL + endPointCliente);

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado);
        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado2)
                .when()
                .delete(baseURL + endPointCliente);
        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .delete(baseURL + endPointCliente + apagaTodosClientes)
                .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaEsperada));


    }

    @Test
    @DisplayName("Quando eu pegar a lista de clientes depois de cadastrar, entao a lista deve mostrar os clientes cadastrados")
    public void consultarTodosClientes() {

        String clienteCadastrado1 = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String clienteCadastrado2 = "{\n" +
                "  \"id\": 1003,\n" +
                "  \"idade\": 29,\n" +
                "  \"nome\": \"Donnald\",\n" +
                "  \"risco\": 5\n" +
                "}";
        String respostaEsperada = "{\"1002\":{\"nome\":\"Minnie Mouse\",\"idade\":25,\"id\":1002,\"risco\":0},\"1003\":{\"nome\":\"Donnald\",\"idade\":29,\"id\":1003,\"risco\":5}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado1)
                .when()
                .post(baseURL + endPointCliente);
        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado2)
                .when()
                .post(baseURL + endPointCliente);


        given()
                .contentType(ContentType.JSON)
                .when()
                .get(baseURL)

                .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));

    }

    @Test
    @DisplayName("Quando eu pegar um cliente especifico, entao ele deve aparecer cadastrado")
    public void consultarClienteEspecifico() {

        String clienteCadastrado = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "{\"nome\":\"Minnie Mouse\",\"idade\":25,\"id\":1002,\"risco\":0}";
        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .post(baseURL + endPointCliente);
        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)

                .when()
                .get(baseURL + endPointCliente + idCliente)
                .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));


    }

    @Test
    @DisplayName("Quando eu pegar um cliente pelo risco, entao ele deve aparecer cadastrado informando o campo risco")
    public void consultarClienteRisco() {

        String clienteCadastrado = "{\n" +
                "  \"id\": 11,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 15\n" +
                "}";

        String respostaEsperada = "{\"nome\":\"Minnie Mouse\",\"idade\":25,\"id\":11,\"risco\":-15}";


        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .post(baseURL + endPointCliente);

        given()
                .auth()
                .basic("aluno", "senha")

                .when()
                .get(baseURL + urlClienteRisco)
                .then()
                .statusCode(200)
                .assertThat().body(new IsEqual<>(respostaEsperada));


    }

    @Test
    @DisplayName("Quando cadastrar um cliente, entao ele deve estar disponivel no resultado")
    public void cadastrarClientes() {


        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String respostaEsperada = "{\"1002\":{\"nome\":\"Minnie Mouse\",\"idade\":25,\"id\":1002,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .post(baseURL + endPointCliente)

                .then()
                .statusCode(201)
                .assertThat().body(containsString(respostaEsperada));
    }

    @Test
    @DisplayName("Quando  atualizar um cliente, entao ele deve estar com os dados atualizados")
    public void atualizarClientes() {


        String clienteCadastrado = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String clienteParaCadastrar = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 28,\n" +
                "  \"nome\": \"Mickey Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";
        String clienteAtualizado = "{\"1002\":{\"nome\":\"Mickey Mouse\",\"idade\":28,\"id\":1002,\"risco\":0}}";

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .post(baseURL + endPointCliente);

        given()
                .contentType(ContentType.JSON)
                .body(clienteParaCadastrar)
                .when()
                .put(baseURL + endPointCliente)


                .then()
                .statusCode(200)
                .assertThat().body(containsString(clienteAtualizado));
    }

    @Test
    @DisplayName("Quando eu deletar um cliente especifico, entao ele nao deve estar mais na lista de clientes.")
    public void deletarClienteEspecifico() {


        String clienteCadastrado = "{\n" +
                "  \"id\": 1002,\n" +
                "  \"idade\": 25,\n" +
                "  \"nome\": \"Minnie Mouse\",\n" +
                "  \"risco\": 0\n" +
                "}";

        String respostaEsperada = "CLIENTE REMOVIDO: { NOME: Minnie Mouse, IDADE: 25, ID: 1002 }";

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .post(baseURL + endPointCliente);

        given()
                .contentType(ContentType.JSON)
                .body(clienteCadastrado)
                .when()
                .delete(baseURL + endPointCliente + idCliente)
                .then()
                .statusCode(200)
                .assertThat().body(containsString(respostaEsperada));
    }


}
