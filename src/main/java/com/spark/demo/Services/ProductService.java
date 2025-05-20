package com.spark.demo.Services;

import com.spark.demo.Models.Product;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.spark.demo.Models.SearchObject;
import java.io.IOException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import java.util.stream.Collectors;
import java.util.Objects;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    private final Map<Long, Product> productosEnMemoria;

    public ProductService() {
        this.productosEnMemoria = loadProductsFromFile();
    }

    private Map<Long, Product> loadProductsFromFile() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = new ClassPathResource("data/products.json").getInputStream();
            List<Product> lista = mapper.readValue(is, new TypeReference<List<Product>>() {
            });
            return lista.stream().collect(Collectors.toMap(Product::getId, p -> p));
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar productos desde archivo JSON", e);
        }
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productosEnMemoria.values());
    }

    public List<String> getUniqueCategories() {
        return productosEnMemoria.values().stream()
                .map(Product::getCategory)
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public List<Product> getFilteredProducts(SearchObject searchObject) {
        return productosEnMemoria.values().stream()
                .filter(p -> "".equals(searchObject.getName()) || p.getName().toLowerCase().contains(searchObject.getName().toLowerCase()))
                .filter(p -> searchObject.getCategory().equalsIgnoreCase("AC") || p.getCategory().equalsIgnoreCase(searchObject.getCategory()))
                .filter(p -> matchesAvailability(p, searchObject.getAvailability()))
                .collect(Collectors.toList());
    }

    private boolean matchesAvailability(Product product, String availability) {
        Integer stock = product.getQuantityInStock();

        return switch (availability.toLowerCase()) {
            case "in" ->
                stock != null && stock > 0;
            case "out" ->
                stock == null || stock == 0;
            case "all" ->
                true;
            default ->
                true;
        };
    }

    public Product insertProduct(Product producto) {
        long newId = productosEnMemoria.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
        producto.setId(newId);
        productosEnMemoria.put(newId, producto);
        return producto;
    }

    public boolean updateProduct(Product producto, Long id) {
        Product existente = productosEnMemoria.get(id);
        if (existente != null) {
            existente.setName(producto.getName());
            existente.setCategory(producto.getCategory());
            existente.setQuantityInStock(producto.getQuantityInStock());
            existente.setUnitPrice(producto.getUnitPrice());
            existente.setExpirationDate(producto.getExpirationDate());
            existente.setUpdateDate(producto.getUpdateDate());
            return true;
        }
        return false;
    }

    public boolean deleteProduct(Long id) {
        return productosEnMemoria.remove(id) != null;
    }

    public boolean manageStock(Long id) {
        Product existente = productosEnMemoria.get(id);
        if (existente != null) {

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            String formattedDate = LocalDateTime.now().format(formatter);

            int stock = existente.getQuantityInStock();

            if (stock == 0) {
                existente.setQuantityInStock(10);
            } else {
                existente.setQuantityInStock(0);
            }

            existente.setUpdateDate(formattedDate);
            return true;
        }
        return false;

    }

}
