package com.example.SaleManagementService.controllers;

import com.example.SaleManagementService.models.Vente;
import com.example.SaleManagementService.repository.VenteRepository;
import com.example.SaleManagementService.service.VenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@RestController
@RequestMapping("SaleManagementService/api/ventes")
public class VenteController {

    @Autowired
    private DiscoveryClient discoveryClient;
    @Autowired
    private VenteService venteService;

    private RestTemplate restTemplate = new RestTemplate();

    // Créer une nouvelle vente
    @PostMapping
    public Vente createVente(@RequestBody Vente vente) {
        return venteService.createVente(vente);
    }

    // Récupérer toutes les ventes
    @GetMapping
    public List<Vente> getAllVentes() {
        return venteService.getAllVentes();
    }

    // Récupérer une vente par ID
    @GetMapping("/{id}")
    public ResponseEntity<Vente> getVenteById(@PathVariable Long id) {
        return venteService.getVenteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    // Supprimer une vente
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVente(@PathVariable Long id) {
        venteService.deleteVente(id);
        return ResponseEntity.noContent().build();
    }



    // Mettre à jour une vente
    @PutMapping("/{id}")
    public ResponseEntity<Vente> updateVente(@PathVariable Long id, @RequestBody Vente updatedVente) {
        try {
            Vente updated = venteService.updateVente(id, updatedVente);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }


    @GetMapping("/clients")
    public List<?> getAllClientsFromClientMicroservice() {
        // Obtenir les instances du service ClientMicroService depuis Consul
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientMicroService trouvée !");
        }

        // Récupérer l'URL de la première instance
        String clientServiceUrl = instances.get(0).getUri().toString();

        // Appeler l'API ClientMicroService pour obtenir les clients
        RestTemplate restTemplate = new RestTemplate();
        String getAllClientsUrl = clientServiceUrl + "/ClientService/api/clients";
        return restTemplate.getForObject(getAllClientsUrl, List.class);
    }



    @GetMapping("/products")
    public List<?> getAllProducttsFromProducttMicroservice(){
        var instances = discoveryClient.getInstances("Produit-Service"); //le nom du micro-service dans consul

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ProduitsService trouvée !");
        }
        String clientServiceUrl = instances.get(0).getUri().toString();

        // Appeler l'API ClientMicroService pour obtenir les clients
        RestTemplate restTemplate = new RestTemplate();
        String getAllProductsUrl = clientServiceUrl + "/ProduitsService/api/produits";
        return restTemplate.getForObject(getAllProductsUrl, List.class);
}
    //les quantités vendus par date
        @GetMapping("/par-date")
        public List<Map<String, Object>> getVentesParDate() {
           return venteService.getVentesParDate();
        }

    //calculer le prix total + consomation dyal getprix/produit li drt
    @GetMapping("/{id}/prix-total")
    public ResponseEntity<Float> getPrixTotalVente(@PathVariable Long id) {
        // Récupérer la vente par ID
        Optional<Vente> venteOpt = venteService.getVenteById(id);

        if (venteOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Vente vente = venteOpt.get();
        Long produitId = vente.getProduit();
        int quantite = vente.getQuantite();

        // Obtenir le prix du produit
        var instances = discoveryClient.getInstances("Produit-Service");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de Produit-Service trouvée !");
        }

        String produitServiceUrl = instances.get(0).getUri().toString();
        String getPrixUrl = produitServiceUrl + "/ProduitsService/api/produits/" + produitId + "/";

        Float prixProduit = restTemplate.getForObject(getPrixUrl, Float.class);

        if (prixProduit == null) {
            return ResponseEntity.notFound().build();
        }

        // Calcul dyal prixTotal xoxo
        float prixTotal = prixProduit * quantite;


        return ResponseEntity.ok(prixTotal);
    }
}



