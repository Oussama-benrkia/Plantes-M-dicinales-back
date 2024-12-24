package ma.m3achaba.plantes.services.imp;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import ma.m3achaba.plantes.common.PageResponse;
import ma.m3achaba.plantes.dto.CommentaireRequest;
import ma.m3achaba.plantes.dto.CommentaireResponse;
import ma.m3achaba.plantes.mapper.CommentaireMapper;
import ma.m3achaba.plantes.model.Commentaire_plant;
import ma.m3achaba.plantes.model.Plantes;
import ma.m3achaba.plantes.model.User;
import ma.m3achaba.plantes.repo.CommentairepltRepository;
import ma.m3achaba.plantes.repo.PlantesRepository;
import ma.m3achaba.plantes.repo.UserRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class CommentaireService {
    private final CommentairepltRepository commentairepltRepository;
    private final CommentaireMapper commentaireMapper;
    private final PlantesRepository plantesRepository;
    private final UserRepo userRepo;


    public Optional<CommentaireResponse> save(CommentaireRequest commentaireRequest,Long idPlante) {
        Commentaire_plant cmt=commentaireMapper.toEntity(commentaireRequest);
        Plantes pl=plantesRepository.findById(idPlante).orElseThrow(() -> new EntityNotFoundException("Plante " + 1 + " not found"));
        cmt.setPlante(pl);
        User use=userRepo.findById(1L).orElseThrow(() -> new EntityNotFoundException("User not found"));
        cmt.setUtilisateur(use);
        return Optional.ofNullable(commentaireMapper.toResponse(commentairepltRepository.save(cmt)));
    }

    public PageResponse<CommentaireResponse> list(Long idPlante,int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Plantes pl=plantesRepository.findById(idPlante).orElseThrow(() -> new EntityNotFoundException("Plante " + 1 + " not found"));
        Page<Commentaire_plant> commentaire=commentairepltRepository.findAllByPlante(pl,pageable);
        return PageResponse.<CommentaireResponse>builder()
                .totalPages(commentaire.getTotalPages())
                .totalElements(commentaire.getTotalElements())
                .last(commentaire.isLast())
                .first(commentaire.isFirst())
                .number(commentaire.getNumber())
                .size(commentaire.getSize())
                .content(commentaire.getContent().stream().map(commentaireMapper::toResponse).collect(Collectors.toList()))
                .build();
    }

}
