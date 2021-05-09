package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Forms;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the Forms entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FormsRepository extends JpaRepository<Forms, Long> {}
