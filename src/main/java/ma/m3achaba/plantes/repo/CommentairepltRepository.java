package ma.m3achaba.plantes.repo;

import ma.m3achaba.plantes.model.Commentaire_plant;
import ma.m3achaba.plantes.model.Plantes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentairepltRepository extends JpaRepository<Commentaire_plant,Long> {
    Page<Commentaire_plant> findAllByPlante(Plantes plante, Pageable pageable);
}
