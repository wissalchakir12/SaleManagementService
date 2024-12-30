package com.example.SaleManagementService.service;


import com.example.SaleManagementService.models.Vente;

import com.example.SaleManagementService.repository.VenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VenteService {

    private final VenteRepository venteRepository;

    @Autowired
    public VenteService(VenteRepository venteRepository) {
        this.venteRepository = venteRepository;
    }


    public Vente createVente(Vente vente) {
        return venteRepository.save(vente);
    }



    public Optional<Vente> getVenteById(Long id) {
        return venteRepository.findById(id);
    }



    public List<Vente> getAllVentes() {
        return venteRepository.findAll();
    }


    public Vente updateVente(Long id, Vente updatedVente) {
        return venteRepository.findById(id).map(vente -> {
            vente.setClient(updatedVente.getClient());
            vente.setProduit(updatedVente.getProduit());
            vente.setDate(updatedVente.getDate());
            vente.setQuantite(updatedVente.getQuantite());
            return venteRepository.save(vente);
        }).orElseThrow(() -> new RuntimeException("Vente non trouvée avec l'ID : " + id));
    }


    public void deleteVente(Long id) {
        if (venteRepository.existsById(id)) {
            venteRepository.deleteById(id);
        } else {
            throw new RuntimeException("Vente not found with id: " + id);
        }
    }


    //Les quantités vendus par date

    @GetMapping("/par-date")
    public List<Map<String, Object>> getVentesParDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd"); // Format lisible
        List<Vente> ventes = venteRepository.findAll();
        Map<String, Integer> ventesParDate = new HashMap<>();

        for (Vente vente : ventes) {
            String date = dateFormat.format(vente.getDate()); // Convertir la date
            ventesParDate.put(date, ventesParDate.getOrDefault(date, 0) + vente.getQuantite());
        }

        List<Map<String, Object>> result = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : ventesParDate.entrySet()) {
            Map<String, Object> venteParDate = new HashMap<>();
            venteParDate.put("date", entry.getKey());
            venteParDate.put("quantite", entry.getValue()); // Clé adaptée au frontend
            result.add(venteParDate);
        }

        return result;
    }
}