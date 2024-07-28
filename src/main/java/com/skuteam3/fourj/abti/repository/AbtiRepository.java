package com.skuteam3.fourj.abti.repository;

import com.skuteam3.fourj.abti.domain.Abti;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AbtiRepository extends JpaRepository<Abti, Long> {

}
