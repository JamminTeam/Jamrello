package com.sparta.jamrello.domain.catalog.repository;

import com.sparta.jamrello.domain.catalog.repository.entity.Catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, Long>, CatalogQueryRepository {

}
