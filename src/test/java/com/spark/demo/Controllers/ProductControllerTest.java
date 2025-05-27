package com.spark.demo.Controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.demo.Models.Product;
import com.spark.demo.Models.SearchObject;
import com.spark.demo.Services.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    // Dummy product for avoid keeping creating one each time
    private Product createMockProduct(Long id, String name) {
        return new Product(
                id,
                name,
                "Cat A",
                10.0,
                "2025-12-31",
                "2024-01-01",
                "2024-05-25",
                20
        );
    }

    @Test
    void testGetAllProducts() throws Exception {
        List<Product> products = Arrays.asList(createMockProduct(1L, "Product 1"));

        Mockito.when(productService.getAllProducts()).thenReturn(products);

        mockMvc.perform(get("/getProducts"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"));
    }

    @Test
    void testPostNewProduct() throws Exception {
        Product input = createMockProduct(null, "New product");
        Product saved = createMockProduct(2L, "New product");

        Mockito.when(productService.insertProduct(any(Product.class))).thenReturn(saved);

        mockMvc.perform(post("/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2L))
                .andExpect(jsonPath("$.name").value("New product"));
    }

    @Test
    void testUpdateProduct_Success() throws Exception {
        Mockito.when(productService.updateProduct(any(Product.class), eq(1L))).thenReturn(true);

        Product product = createMockProduct(null, "updated");

        mockMvc.perform(put("/products/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product updated correctly"));//same as response in controller
    }

    @Test
    void testGetCategories() throws Exception {
        List<String> categories = Arrays.asList("Cat A", "Cat B");
        Mockito.when(productService.getUniqueCategories()).thenReturn(categories);

        mockMvc.perform(get("/getCategories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]").value("Cat A"));
    }

    @Test
    void testSetOutOfStock_Success() throws Exception {
        Mockito.when(productService.manageStock(1L)).thenReturn(true);

        mockMvc.perform(post("/products/1/outofstock"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock updated"));
    }

    @Test
    void testSetInStock_Success() throws Exception {
        Mockito.when(productService.manageStock(1L)).thenReturn(true);

        mockMvc.perform(put("/products/1/instock"))
                .andExpect(status().isOk())
                .andExpect(content().string("Stock updated"));
    }

    @Test
    void testSearchProducts() throws Exception {
        SearchObject search = new SearchObject("Product 1", "Cat A", "true");

        List<Product> result = Arrays.asList(
                createMockProduct(1L, "Product 1"),
                createMockProduct(2L, "Specific product")
        );

        Mockito.when(productService.getFilteredProducts(any(SearchObject.class))).thenReturn(result);

        mockMvc.perform(post("/getProductsFiltered")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(search)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Product 1"))
                .andExpect(jsonPath("$[1].name").value("Specific product"));
    }

}
