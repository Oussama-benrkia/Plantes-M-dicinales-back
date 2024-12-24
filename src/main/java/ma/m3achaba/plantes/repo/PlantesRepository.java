package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.Maladies;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlantesRepository extends JpaRepository<Plantes, Long> {
    Page<Plantes> findAllByNameContainingIgnoreCase(String name, Pageable pageable);
    List<Plantes> findAllByNameContainingIgnoreCase(String name);
    boolean existsByName(String name);
    Page<Plantes> findAllByMaladies(Maladies maladies, Pageable pageable);

    @Query("""
        SELECT p
        FROM Plantes p
        JOIN p.maladies m
        WHERE m.id = (
            SELECT m2.id
            FROM Maladies m2
            JOIN m2.plantes p2
            WHERE p2.id = :planteId
        )
    """)
    Page<Plantes> findByMaladieIdLinkedToPlante(@Param("planteId") Long planteId, Pageable pageable);
}
