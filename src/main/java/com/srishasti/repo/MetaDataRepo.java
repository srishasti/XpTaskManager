package com.srishasti.repo;

import com.srishasti.model.MetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MetaDataRepo extends JpaRepository<MetaData,String> {
}
