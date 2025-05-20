package com.spark.demo.Controllers;

import com.spark.demo.Models.Product;
import com.spark.demo.Services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.spark.demo.Models.SearchObject;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/getProducts")
    public List<Product> getAll() {
        return productService.getAllProducts();
    }

    @GetMapping("/getCategories")
    public List<String> getCategories() {
        return productService.getUniqueCategories();
    }

    @PostMapping("/getProductsFiltered")
    public List<Product> getProductsFiltered(@RequestBody SearchObject searchObject) {
        return productService.getFilteredProducts(searchObject);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> postNewProduct(@RequestBody Product producto) {
        Product savedProduct = productService.insertProduct(producto);
        return ResponseEntity.ok().body(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity updateProduct(@RequestBody Product product, @PathVariable Long id) {
        boolean success = productService.updateProduct(product, id);
        if (success) {
            return ResponseEntity.ok("Producto actualizado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }

    @PostMapping("/products/{id}/outofstock")
    public ResponseEntity setOutOfStock(@PathVariable Long id) {
        boolean success = productService.manageStock(id);
        if (success) {
            return ResponseEntity.ok("Stock actualizado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

    }
    @PutMapping("/products/{id}/instock")
    public ResponseEntity setInStock(@PathVariable Long id) {
        boolean success = productService.manageStock(id);
        if (success) {
            return ResponseEntity.ok("Stock actualizado");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }

    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity deleteProduct(@PathVariable Long id) {
        boolean success = productService.deleteProduct(id);
        if (success) {
            return ResponseEntity.ok("Producto borrado correctamente");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Producto no encontrado");
        }
    }
}
