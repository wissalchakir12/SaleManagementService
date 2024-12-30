package com.example.SaleManagementService.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import java.util.List;

@RestController
@RequestMapping("SaleManagementService/api/clients")
public class ClientController {
    @Autowired
    private DiscoveryClient discoveryClient;

    private final RestTemplate restTemplate = new RestTemplate();

    // Récupérer tous les clients
    @GetMapping
    public List<?> getAllClients() {
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientService trouvée !");
        }

        String clientServiceUrl = instances.get(0).getUri().toString();
        String getAllClientsUrl = clientServiceUrl + "/ClientService/api/clients";

        return restTemplate.getForObject(getAllClientsUrl, List.class);
    }

    // Récupérer un client par ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getClientById(@PathVariable Long id) {
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientService trouvée !");
        }

        String clientServiceUrl = instances.get(0).getUri().toString();
        String getClientByIdUrl = clientServiceUrl + "/ClientService/api/clients/" + id;

        return restTemplate.getForEntity(getClientByIdUrl, Object.class);
    }

    // Ajouter un client
    @PostMapping
    public ResponseEntity<?> saveClient(@RequestBody Object client) {
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientService trouvée !");
        }

        String clientServiceUrl = instances.get(0).getUri().toString();
        String saveClientUrl = clientServiceUrl + "/ClientService/api/clients";

        return restTemplate.postForEntity(saveClientUrl, client, Object.class);
    }

    // Supprimer un client
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClient(@PathVariable Long id) {
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientService trouvée !");
        }

        String clientServiceUrl = instances.get(0).getUri().toString();
        String deleteClientUrl = clientServiceUrl + "/ClientService/api/clients/" + id;

        restTemplate.delete(deleteClientUrl);
        return ResponseEntity.noContent().build();
    }
    // Mettre à jour un client par ID
    @PutMapping("/{id}")
    public ResponseEntity<?> updateClient(@PathVariable Long id, @RequestBody Object updatedClient) {
        var instances = discoveryClient.getInstances("ClientService");

        if (instances.isEmpty()) {
            throw new IllegalStateException("Aucune instance de ClientService trouvée !");
        }

        String clientServiceUrl = instances.get(0).getUri().toString();
        String updateClientUrl = clientServiceUrl + "/ClientService/api/clients/" + id;

        // Appeler l'API ClientService pour mettre à jour le client
        restTemplate.put(updateClientUrl, updatedClient);

        return ResponseEntity.noContent().build();  // Indique que la mise à jour a réussi sans renvoyer de contenu
    }


}
