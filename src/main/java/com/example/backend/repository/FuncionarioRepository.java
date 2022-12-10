package com.example.backend.repository;

import com.example.backend.model.Funcionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface FuncionarioRepository extends JpaRepository<Funcionario, Long> {

    @Query(value = "select f from Funcionario f where f.email = ?1")
    Funcionario findByLogin(String login);
}
