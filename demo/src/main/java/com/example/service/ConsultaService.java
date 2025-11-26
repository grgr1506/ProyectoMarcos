package com.example.service;

import com.example.model.Consulta;
import com.example.repository.ConsultaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class ConsultaService {

    private final ConsultaRepository repo;

    public ConsultaService(ConsultaRepository repo) {
        this.repo = repo;
    }

    public List<Consulta> listar() {
        return repo.findAllByOrderByCreadoEnDesc();
    }

    @Transactional
    public Consulta guardar(Consulta c) {
        return repo.save(c);
    }

    @Transactional
    public boolean cambiarEstado(Long id, String estado) {
        return repo.findById(id).map(c -> {
            c.setEstado(estado);
            repo.save(c);
            return true;
        }).orElse(false);
    }

    @Transactional
    public boolean eliminar(Long id) {
        if (!repo.existsById(id))
            return false;
        repo.deleteById(id);
        return true;
    }
}
