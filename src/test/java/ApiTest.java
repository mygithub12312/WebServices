import api.ShoppingCart;
import org.testng.annotations.Test;
import pages.ShoppingCartPage;
import payload.Payload;
import product.ProductPayLoad;
import product.Product;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class ApiTest {

    @Test
    public void addProductToBasketUsingPojoVerification() {
        ShoppingCart shoppingCart = new ShoppingCart();
        ProductPayLoad productPayload = new ProductPayLoad(new Product("5156733"), 1);
        String guid = shoppingCart.getGuIdFromCreateShoppingCartPostApiCall();

        given().
            contentType("application/json").
            accept("application/json").
            body(productPayload).
            log().
            all().
            when().
            post(shoppingCart.getAddProductToShoppingCartEndpoint(guid)).
            then().
            body(matchesJsonSchemaInClasspath("post.json")).
            body(shoppingCart.getProductCodeJsonPath(), equalTo(productPayload.getProduct().getCode())).
            body(shoppingCart.getProductQtyJsonPath(), equalTo(productPayload.getQuantity())).
            statusCode(200);
    }

    @Test
    public void addProductToBasketUsingPayloadTemplate() throws IOException {
        String path = "src/main/resources/addProductToCartPayload";
        File file = new File(path);
        Scanner scanner = new Scanner(file);
        String payloadTemplate = scanner.nextLine();
        scanner.close();

        Payload payloadManager = new Payload();
        ShoppingCart shoppingCartPageApi = new ShoppingCart();

        Map<String, Object> valuesProduct = new HashMap<>();
        valuesProduct.put("code", "5156733");
        valuesProduct.put("quantity", 1);

        String guid = shoppingCartPageApi.getGuIdFromCreateShoppingCartPostApiCall();

        given().
            contentType("application/json").
            accept("application/json").
            body(payloadManager.getAddProductToCardPayload(valuesProduct, payloadTemplate)).
            when().
            post(shoppingCartPageApi.getAddProductToShoppingCartEndpoint(guid)).
            then().
            body(shoppingCartPageApi.getProductCodeJsonPath(), equalTo(valuesProduct.get("code"))).
            body(shoppingCartPageApi.getProductQtyJsonPath(), equalTo(valuesProduct.get("quantity"))).
            statusCode(200);
    }

    @Test
    public void isProductAddedToShoppingCart() {
        ShoppingCartPage shoppingCartPage = new ShoppingCartPage();
        ShoppingCart shoppingCartPageApi = new ShoppingCart();
        ProductPayLoad productPayload = new ProductPayLoad(new Product("5156733"), 1);
        String guid = shoppingCartPageApi.getGuIdFromCreateShoppingCartPostApiCall();

        given().
            contentType("application/json").
            accept("application/json").
            body(productPayload).
            when().
            post(shoppingCartPageApi.getAddProductToShoppingCartEndpoint(guid)).
            then().
            statusCode(200);

        shoppingCartPage.openShoppingCartPageByBrowser(guid);
        assertThat(shoppingCartPage.getProductLineItemLink().getAttribute("href").contains(productPayload.getProduct().getCode()))
            .overridingErrorMessage("Product is not added to Shopping Cart");
    }

}
