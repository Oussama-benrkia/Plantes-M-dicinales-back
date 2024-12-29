package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.PlantComment;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentairepltRepository extends JpaRepository<PlantComment,Long> {
    Page<PlantComment> findAllByPlantes(Plantes plante, Pageable pageable);
}
