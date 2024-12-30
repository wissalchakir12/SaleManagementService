package com.example.SaleManagementService.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("SaleManagementService/api/produits")
public class ProduitController {

    @Autowired
    private DiscoveryClient discoveryClient;

    private RestTemplate restTemplate = new RestTemplate();

    // Récupérer tous les produits depuis le microservice Produit
    @GetMapping
    public List<?> getAllProduits() {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String getAllProduitsUrl = produitServiceUrl + "/ProduitsService/api/produits";

        return restTemplate.getForObject(getAllProduitsUrl, List.class);
    }

    // Ajouter un produit via le microservice Produit
    @PostMapping("/add")
    public ResponseEntity<?> addProduit(@RequestBody Object produit) {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String addProduitUrl = produitServiceUrl + "/ProduitsService/api/produits/add";

        return restTemplate.postForEntity(addProduitUrl, produit, Object.class);
    }

    // Récupérer les détails d'un produit par ID depuis le microservice Produit
    @GetMapping("/{id}")
    public ResponseEntity<?> getProduitById(@PathVariable int id) {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String getProduitByIdUrl = produitServiceUrl + "/ProduitsService/api/produits/" + id;

        return restTemplate.getForEntity(getProduitByIdUrl, Object.class);
    }

    // Supprimer un produit par ID
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProduit(@PathVariable int id) {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String deleteProduitUrl = produitServiceUrl + "/ProduitsService/api/produits/" + id;

        restTemplate.delete(deleteProduitUrl);
        return ResponseEntity.noContent().build();
    }
    // Mettre à jour un produit par ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProduit(@PathVariable int id, @RequestBody Object produit) {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String updateProduitUrl = produitServiceUrl + "/ProduitsService/api/produits/" + id;

        restTemplate.put(updateProduitUrl, produit);
        return ResponseEntity.ok().build();
    }
    // Récupérer les produits les plus stockés
    @GetMapping("/top-stocked")
    public ResponseEntity<List<?>> getTopStockedProducts() {
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }


        String produitServiceUrl = instances.get(0).getUri().toString();


        String topStockedUrl = produitServiceUrl + "/ProduitsService/api/produits"+"/top-stocked";


        List<?> topStockedProducts = restTemplate.getForObject(topStockedUrl, List.class);


        return ResponseEntity.ok(topStockedProducts);
    }







}