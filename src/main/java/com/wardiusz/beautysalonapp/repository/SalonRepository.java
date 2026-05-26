package com.wardiusz.beautysalonapp.repository;

import com.wardiusz.beautysalonapp.entity.Salon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SalonRepository extends JpaRepository<Salon, Long>, JpaSpecificationExecutor<Salon> {}
