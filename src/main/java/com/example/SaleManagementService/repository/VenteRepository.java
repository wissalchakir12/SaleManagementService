package com.example.SaleManagementService.repository;



import com.example.SaleManagementService.models.Vente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VenteRepository extends JpaRepository<Vente, Long> {

}
