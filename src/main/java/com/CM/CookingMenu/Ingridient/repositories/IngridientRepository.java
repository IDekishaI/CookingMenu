package com.CM.CookingMenu.Ingridient.repositories;

import com.CM.CookingMenu.Ingridient.entities.Ingridient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngridientRepository extends JpaRepository<Ingridient, Integer> {
}
